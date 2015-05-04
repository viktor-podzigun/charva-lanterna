/* class AbstractButton
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
import charva.awt.ColorScheme;
import charva.awt.Container;
import charva.awt.EventQueue;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.ItemSelectable;
import charva.awt.Toolkit;
import charva.awt.Window;
import charva.awt.event.AWTEvent;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.awt.event.ItemEvent;
import charva.awt.event.ItemListener;
import charva.awt.event.KeyEvent;
import charva.awt.event.KeyListener;
import charva.awt.event.MouseEvent;


/**
 * This forms the base class for components that exhibit button-like
 * behavior.
 */
public abstract class AbstractButton extends JComponent implements 
        SwingConstants, ItemSelectable, KeyListener {

    private String      text;

    /**
     * The string that is sent in an ActionEvent when ENTER is pressed
     */
    private String      actionCommand;
    
    private int         horizontalAlignment     = CENTER;

    private int         mnemonic;
    private int         mnemonicIndex = -1;

    /**
     * The state of the button. This is package-private because ButtonGroup
     * needs to be able to change the state of the button without calling
     * setSelected().
     */
    boolean             selected;

    /**
     * A list of ActionListeners registered for this component
     */
    protected ArrayList actionListeners;

    /**
     * A list of ItemListeners registered for this component
     */
    protected ArrayList itemListeners;
    
    protected ColorPair selectedColor;
    protected ColorPair mnemonicColor;
    protected ColorPair disabledColor;
    

    public AbstractButton() {
    }
    
    public AbstractButton(String text) {
        this.text = text;
        this.actionCommand = text;
    }
    
    public AbstractButton(Action action) {
        this.text = (String)action.getValue(Action.NAME);
        this.actionCommand = (String)action.getValue(Action.ACTION_COMMAND_KEY);
        setMnemonic(((Integer)action.getValue(Action.MNEMONIC_KEY))
                .intValue());
        addActionListener(action);
    }
    
    public void setColors(ColorScheme colors) {
        super.setColors(colors);
    
        color         = colors.getColor(ColorScheme.BUTTON);
        selectedColor = colors.getColor(ColorScheme.BUTTON_SELECTED);
        mnemonicColor = colors.getColor(ColorScheme.BUTTON_MNEMONIC);
        disabledColor = colors.getColor(ColorScheme.BUTTON_DISABLED);
    }

    /**
     * Sets the button's text
     */
    public void setText(String text) {
        String oldValue = this.text;
        this.text = text;
        updateDisplayedMnemonicIndex(text, getMnemonic());
    
        if (text == null || oldValue == null || !text.equals(oldValue)) {
            invalidate();
            repaint();
        }
    }

    protected void addKeyListenerToAncestor() {
        Window ancestor = SwingUtilities.windowForComponent(this);
        if (ancestor != null) {
            ancestor.addKeyListener(this);
        }
    }

    /**
     * Set the button's mnemonic character.
     * The mnemonic is the key which will activate this button if focus
     * is contained somewhere within this button's ancestor window.
     * <p>
     * A mnemonic must correspond to a single key on the keyboard and should be
     * specified using one of the VK_XXX keycodes defined in
     * charva.awt.event.KeyEvent. Mnemonics are case-insensitive, therefore 
     * a key event with the corresponding keycode would cause the button to be
     * activated whether or not the Shift modifier was pressed.
     * <p>
     * If the character defined by the mnemonic is found within the button's
     * label string, the first occurrence of it will be underlined to
     * indicate the mnemonic to the user. If the corresponding character
     * is not contained within the button's label, then it will be displayed
     * to the right, surrounded by parentheses.
     * <p>
     * In the case of a JButton, JCheckBox or JRadioButton, this method
     * should only be called <em>after</em> the button has been added
     * to a Window, otherwise pressing the corresponding key will have
     * no effect.
     *
     * @param mnemonic  the keycode of the mnemonic key
     */
    public void setMnemonic(int newMnemonic) {
        addKeyListenerToAncestor();
        
        if (mnemonic != newMnemonic) {
            mnemonic = newMnemonic;
            
            updateDisplayedMnemonicIndex(getText(), mnemonic);
            invalidate();
            repaint();
        }
    }

    /**
     * Update the mnemonicIndex field. This method is called when either text or
     * mnemonic changes. The new value of the mnemonicIndex field is the index
     * of the first occurrence of mnemonic in text.
     */
    private void updateDisplayedMnemonicIndex(String text, int mnemonic) {
        int index = SwingUtilities.findDisplayedMnemonicIndex(text, mnemonic);
        if (index == -1) {
            mnemonicIndex = -1;
        } else {
            String t = getText();
            int textLength = (t == null) ? 0 : t.length();
            if (index < -1 || index >= textLength)
                throw new IllegalArgumentException("index == " + index);
            
            mnemonicIndex = index;
        }
    }

    /**
     * Returns the character, as an index, that the look and feel should
     * provide decoration for as representing the mnemonic character.
     *
     * @return index representing mnemonic character
     */
    protected int getDisplayedMnemonicIndex() {
        return mnemonicIndex;
    }

    public void setParent(Container container) {
    	if (mnemonic != 0 && parent != null && parent.get() != null) {
    	    throw new RuntimeException(
    	            "Removal from previous parent currently not implemented");
    	    //removeKeyListenerFromAncestor();
    	}
    	
    	super.setParent(container);
    	
    	if (mnemonic != 0)
    	    addKeyListenerToAncestor();
    }

    /**
     * Returns the button's label text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the button's mnemonic character.
     */
    public int getMnemonic() {
        return mnemonic;
    }

    /**
     * Sets the action command for this button
     */
    public void setActionCommand(String command) {
        actionCommand = command;
    }

    /**
     * Returns the action command for this button.
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * Utility method for drawing button's label
     */
    protected void paintText(Graphics g, String text) {
        Insets insets = getInsets();
        g.translate(insets.left, insets.top);

        ColorPair colorPair;
        if (isEnabled()) {
            if (hasFocus()) {
                colorPair = selectedColor;
            } else {
                colorPair = getColor();
            }
        } else {
            colorPair = disabledColor;
        }

        g.setColor(colorPair);
        g.drawString(text, 0, 0);
    
        if (isEnabled() && getMnemonic() > 0) {
            int mnemonicPos = getDisplayedMnemonicIndex();
            if (mnemonicPos != -1) {
                ColorPair colorpair = ColorPair.create(
                        mnemonicColor.getForeground(), 
                        colorPair.getBackground());
                
                int textIndex = text.indexOf(getText());
                if (textIndex != -1) {
                    mnemonicPos += textIndex;
                    g.setColor(colorpair);
                    g.drawChar(text.charAt(mnemonicPos), mnemonicPos, 0);
                }
            }
        }
    }
    
    /**
     * Utility method for drawing graphical character
     */
    protected void paintChar(Graphics g, int offset, char ch) {
        Insets insets = getInsets();
        g.translate(insets.left, insets.top);

        ColorPair colorPair;
        if (isEnabled()) {
            if (hasFocus()) {
                colorPair = selectedColor;
            } else {
                colorPair = getColor();
            }
        } else {
            colorPair = disabledColor;
        }

        g.setColor(colorPair);
        g.drawChar(ch, offset, 0);
    }
    
    /**
     * Process a MouseEvent that was generated by clicking the mouse
     * somewhere inside this component.
     */
    protected void processMouseEvent(MouseEvent e) {
        // Request focus if this is a MOUSE_PRESSED
        super.processMouseEvent(e);

        if (e.isConsumed())
            return;
        
        if (e.getButton() == MouseEvent.BUTTON1 &&
                e.getID() == MouseEvent.MOUSE_CLICKED &&
                this.isFocusTraversable()) {

            this.doClick();
        }
    }

    /**
     * Performs a "click" of this button
     */
    public void doClick() {

        // This is required because our parent window will send the KeyEvent
        // to the Container containing the component with the current focus.
//	super.requestFocus();
        getParent().setFocus(this);	// Mustn't generate FocusEvents (?)

        Toolkit.getDefaultToolkit().fireKeystroke(KeyEvent.VK_ENTER);
    }

    /**
     * Changes selected state of this abstract button.
     * <p>
     * Note that this method does not post an ActionEvent.
     */
    public void setSelected(boolean state) {
        if (selected != state) {
            // post an ItemEvent if the state has changed
            int statechange = state ? ItemEvent.SELECTED : ItemEvent.DESELECTED;
            ItemEvent evt = new ItemEvent(this, this, statechange);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(evt);
        
            selected = state;
            repaint();
        }
    }

    /**
     * Returns the state of the button
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Process events
     */
    protected void processEvent(AWTEvent evt) {
        super.processEvent(evt);

        if (evt instanceof KeyEvent) {
            KeyEvent key_event = (KeyEvent) evt;
            if ((!key_event.isConsumed()) 
                    && key_event.getKeyCode() == KeyEvent.VK_ENTER 
                    && super.isEnabled()) {

                EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
                queue.postEvent(new ActionEvent(this, getActionCommand()));
                key_event.consume();
            }
        } else if (evt instanceof ActionEvent) {
            fireActionPerformed((ActionEvent) evt);
        
        } else if (evt instanceof ItemEvent) {
            fireItemStateChanged((ItemEvent) evt);
        }
    }

    /**
     * Register an ActionListener object for this button
     */
    public void addActionListener(ActionListener al) {
        if (actionListeners == null) {
            actionListeners = new ArrayList();
        }
        
        if (!actionListeners.contains(al)) {
            actionListeners.add(al);
        }
    }

    /**
     * Invoke all the ActionListener callbacks that may have been registered
     * for this button
     */
    protected void fireActionPerformed(ActionEvent ae) {
        if (actionListeners != null) {
            for (int i = actionListeners.size() - 1; i >= 0; i--) {
                ActionListener al = (ActionListener) actionListeners.get(i);
                al.actionPerformed(ae);
            }
        }
    }

    /**
     * Register an ItemListener object for this component
     */
    public void addItemListener(ItemListener il) {
        if (itemListeners == null)
            itemListeners = new ArrayList();
        
        if (!itemListeners.contains(il))
            itemListeners.add(il);
    }

    public void removeItemListener(ItemListener listener) {
        if (itemListeners == null)
            return;
        
        itemListeners.remove(listener);
    }

    /**
     * Implements the KeyListener interface; this is called if a control
     * character or a function key or cursor key was typed
     */
    public void keyPressed(KeyEvent ke) {
    	int code = ke.getKeyCode();
    	int mnem = getMnemonic();
    	if (!ke.isActionKey()) {
    	    code = Character.toLowerCase((char)code);
    	    mnem = Character.toLowerCase((char)mnem);
    	}
        
    	if (code == mnem) {
            doClick();
            ke.consume();
        }
    }

    /**
     * Implements the KeyListener interface; this is called only if a
     * printable (ASCII or ISO8859-1) character was typed
     */
    public void keyTyped(KeyEvent ke) {
    }

    /**
     * Implements the KeyListener interface but is never invoked
     */
    public void keyReleased(KeyEvent ke) {
    }

    /**
     * Invoke all the ItemListener callbacks that may have been registered
     * for this component
     */
    protected void fireItemStateChanged(ItemEvent ie) {
        if (itemListeners != null) {
            for (int i = itemListeners.size() - 1; i >= 0; i--) {
                ItemListener il = (ItemListener) itemListeners.get(i);
                il.itemStateChanged(ie);
            }
        }
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        switch (horizontalAlignment) {
        case LEFT:
        case CENTER:
        case RIGHT:
            break;
            
        default:
            throw new IllegalArgumentException("horizontalAlignment: " 
                    + horizontalAlignment);
        }
    
        this.horizontalAlignment = horizontalAlignment;
    }
}
