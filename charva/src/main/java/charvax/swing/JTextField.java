/* class JTextField
 *
 * Copyright (C) 2001  R M Pitman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package charvax.swing;

import java.util.ArrayList;
import charva.awt.ColorPair;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.Toolkit;
import charva.awt.event.AWTEvent;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.awt.event.KeyEvent;
import charva.awt.event.MouseEvent;
import charvax.swing.text.JTextComponent;


/**
 * JTextField is a component that allows the display and editing of a
 * single line of text.
 * <p>
 * The JTextField class, being a subclass of JComponent, has a setBorder()
 * method which allows an optional Border to be set.
 */
public class JTextField extends JTextComponent {

    protected int       columns;

    /**
     * Index (from the start of the string) of the character displayed
     * in the left corner of the field. This is always 0 if the string
     * is shorter than the field.
     */
    protected int       offset;

    /**
     * The string that is sent inside an ActionEvent when ENTER is pressed.
     */
    private String      actionCommand = new String("");

    /**
     * A list of ActionListeners registered for this component.
     */
    protected ArrayList actionListeners;
    
    
    /**
     * Construct a text field. The initial string is empty and the
     * number of columns is 10.
     */
    public JTextField() {
        this("", 10);
    }

    /**
     * Use this constructor when you want to initialize the value.
     */
    public JTextField(String text) {
        this(text, text.length());
    }

    /**
     * Use this constructor when you want to leave the text field empty
     * but set its length.
     */
    public JTextField(int length) {
        this("", length);
    }

    /**
     * Use this constructor when you want to set both the initial value and the
     * length.
     */
    public JTextField(String text, int length) {
        setText(text);
        setColumns(length);
        
        caretPosition = document.length();
        if (document.length() > columns)
            offset = document.length() - columns;
        else
            offset = 0;
    }

    /**
     * Sets the number of columns in this Textfield, and then invalidates
     * the layout
     */
    public void setColumns(int columns) {
        this.columns = columns;

        invalidate();
        repaint();
    }
    
    public void setSize(int width, int height) {
        super.setSize(width, height);
        
        setColumns(width);
    }

    /**
     * Return the number of columns in the text field
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Set the action command
     */
    public void setActionCommand(String cmd) {
        actionCommand = cmd;
    }

    /**
     * Get the action command
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * Return the size of the text field. Overrides the method in the
     * Component superclass.
     */
    public Dimension getSize() {
        Insets insets = getInsets();
        return new Dimension(columns + insets.left + insets.right,
                1 + insets.top + insets.bottom);
    }

    public int getWidth() {
        Insets insets = getInsets();
        return columns + insets.left + insets.right;
    }

    public int getHeight() {
        Insets insets = getInsets();
        return 1 + insets.top + insets.bottom;
    }

    /**
     * Called by the LayoutManager.
     */
    public Dimension getMinimumSize() {
        return getSize();
    }

    /**
     * Sets whether this textfield can be edited.
     */
    public void setEditable(boolean editable) {
        super.setEnabled(editable);
    }

    public boolean isEditable() {
        return isEnabled();
    }

    public void paint(Graphics g) {
        // draw the border inherited from JComponent, if it exists
        super.paint(g);

        Insets insets = getInsets();
        g.translate(insets.left, insets.top);

        ColorPair colorPair;
        if (isEnabled() && isEditable()) {
            colorPair = color;
        } else {
            colorPair = disabledColor;
        }
        
        StringBuffer padbuf = new StringBuffer();
        for (int i = 0; i < columns; i++) {
            padbuf.append(' ');
        }
        
        String padding = padbuf.toString();

        g.setColor(colorPair);
        g.drawString(padding, 0, 0);

        // get the displayable portion of the string
        int end;
        if (getText().length() > (offset + columns)) {
            end = offset + columns;
        } else {
            end = getText().length();
        }

        g.drawString(getText().substring(offset, end).toString(), 0, 0);
    }

    /**
     * Process KeyEvents that have been generated by this JTextField.
     */
    protected void processKeyEvent(KeyEvent ke) {
        // first call all KeyListener objects that may have been registered
        // for this component
        super.processKeyEvent(ke);
        if (ke.isConsumed())
            return;

        final int key = ke.getKeyCode();
        
        if ((key == KeyEvent.VK_TAB && ke.isShiftDown()) 
                || key == KeyEvent.VK_UP) {
            
            transferFocusBackward();
            return;
        }

        if (key == KeyEvent.VK_TAB || key == KeyEvent.VK_DOWN) {
            transferFocus();
            return;
        }
        
        if (!ke.isActionKey() && (ke.getID() == KeyEvent.KEY_TYPED)) {
            // It is a control-character or a printable character. 
            // If it is not a control character, insert it into the text buffer.
            if (key >= ' ') {
                document.insert(caretPosition, ke.getKeyChar());
                caretPosition++;
                if (caretPosition - offset > columns)
                    offset++;
            }
        } else {
            // It is an action key
            if (key == KeyEvent.VK_LEFT && caretPosition > 0) {
                caretPosition--;
                if (caretPosition < offset)
                    offset--;

            } else if (key == KeyEvent.VK_RIGHT 
                    && caretPosition < document.length()) {
                
                caretPosition++;
                if (caretPosition - offset > columns)
                    offset++;

            } else if (key == KeyEvent.VK_BACK_SPACE && caretPosition > 0) {
                caretPosition--;
                document.deleteCharAt(caretPosition);
                if (caretPosition < offset)
                    offset--;

            } else if (key == KeyEvent.VK_DELETE 
                    && caretPosition >= 0 
                    && caretPosition < document.length()) {
                
                document.deleteCharAt(caretPosition);

            } else if (key == KeyEvent.VK_HOME) {
                caretPosition = 0;
                offset        = 0;

            } else if (key == KeyEvent.VK_END) {
                caretPosition = document.length();
                if (document.length() > columns)
                    offset = document.length() - columns;
                else
                    offset = 0;
            
            } else if (key == KeyEvent.VK_ENTER) {
                // post an action event if ENTER was pressed
                ActionEvent ae = new ActionEvent(this, getActionCommand());
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(ae);
            }
        }
        
        repaint();
    }

    /**
     * Process a MouseEvent that was generated by clicking the mouse
     * somewhere inside this JTextField.
     * Clicking the mouse inside the JTextField moves the caret position
     * to where the mouse was clicked.
     */
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.isConsumed())
            return;

        if (e.getButton() == MouseEvent.BUTTON1 &&
                e.getID() == MouseEvent.MOUSE_CLICKED &&
                isFocusTraversable()) {

            // get the absolute  of this component
            Point  origin  = getLocationOnScreen();
            Insets insets  = getInsets();
            origin.translate(insets.left, insets.top);

            int new_caret = offset + e.getX();
            caretPosition = (new_caret < document.length() ? 
                    new_caret : document.length());
            repaint();
        }
    }

    /**
     * Set the text that is presented by this JTextField.
     */
    public void setText(String text) {
        super.setText(text);

        caretPosition = document.length();
        if (document.length() > columns)
            offset = document.length() - columns;
        else
            offset = 0;

        // If this component is already displayed, generate a PaintEvent
        // and post it onto the queue
        repaint();
    }

    /**
     * Process events
     */
    protected void processEvent(AWTEvent evt) {
        super.processEvent(evt);

        if (evt instanceof ActionEvent)
            postActionEvent((ActionEvent) evt);
    }

    /** 
     * Processes action events occurring on this textfield by
     * dispatching them to any registered <code>ActionListener</code> objects.
     * This is normally called by the controller registered with
     * textfield.
     */  
    public void postActionEvent(ActionEvent ae) {
        if (actionListeners != null) {
            for (int i = actionListeners.size() - 1; i >= 0; i--) {
                ActionListener al = (ActionListener) actionListeners.get(i);
                al.actionPerformed(ae);
            }
        }
    }

    /**
     * Invoke all the ActionListener callbacks that may have been registered
     * for this component. The listener list is processed in last to first order.
     */
    public void fireActionPerformed() {
        ActionEvent ae = new ActionEvent(this, getActionCommand());
        postActionEvent(ae);
    }

    /**
     * Register an ActionListener object for this component.
     */
    public void addActionListener(ActionListener al) {
        if (actionListeners == null)
            actionListeners = new ArrayList();
        
        actionListeners.add(al);
    }

    public void removeActionListener(ActionListener al) {
        if (actionListeners != null)
            actionListeners.remove(al);
    }

    public void requestFocus() {
        // generate the FOCUS_GAINED event
        super.requestFocus();

        // get the absolute origin of this component
        Point  origin = getLocationOnScreen();
        Insets insets = getInsets();
        origin.translate(insets.left, insets.top);
        
        SwingUtilities.windowForComponent(this).setCursor(
                origin.addOffset(caretPosition - offset, 0));
    }

    /**
     * Returns a string representation of this <code>JTextField</code>.
     * This method is intended to be used only for debugging purposes,
     * and the content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JTextField</code>
     */
    protected String paramString() {
        String commandString = (actionCommand != null ? actionCommand : "");

        return super.paramString() + ",columns=" + columns 
                + ",command=" + commandString;
    }
}
