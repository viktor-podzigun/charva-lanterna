/* class JComboBox
 *
 * Copyright (C) 2001-2003  R M Pitman
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

/*
 * Modified Jul 15, 2003 by Tadpole Computer, Inc.
 * Modifications Copyright 2003 by Tadpole Computer, Inc.
 *
 * Modifications are hereby licensed to all parties at no charge under
 * the same terms as the original.
 *
 * Added the JComboBox(ComboBoxModel) constructor.
 * Added the removeAllItems method.
 * Added a stubbed out setEditable method (it would be nice to have editable
 * ComboBoxes, too.
 * Fixed color handling to inherit colors from our parent.
 */

package charvax.swing;

import java.util.ArrayList;
import charva.awt.ColorPair;
import charva.awt.ColorScheme;
import charva.awt.Component;
import charva.awt.Dimension;
import charva.awt.EventQueue;
import charva.awt.Graphics;
import charva.awt.GraphicsConstants;
import charva.awt.Insets;
import charva.awt.ItemSelectable;
import charva.awt.Point;
import charva.awt.Toolkit;
import charva.awt.Window;
import charva.awt.event.AWTEvent;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.awt.event.ItemEvent;
import charva.awt.event.ItemListener;
import charva.awt.event.KeyEvent;
import charva.awt.event.MouseEvent;
import charvax.swing.border.LineBorder;


/**
 * The JComboBox component allows the user to select an item from a pop-up
 * list of choices.
 * <p>
 * When the combobox is in a non-popped-up state, it looks like a JButton
 * with a "diamond" character on the right. To make the pop-up window appear,
 * the user positions the cursor over the combobox and presses ENTER.
 * <p>
 * When the user selects an item in the pop-up window (by positioning the
 * cursor on it and pressing ENTER), the pop-up menu disappears and
 * only the selected item is shown. At the same time the JComboBox
 * posts an ActionEvent onto the system event queue. The "action command"
 * encapsulated in the ActionEvent is the list item that was selected.
 */
public class JComboBox extends JComponent implements ItemSelectable {

    
    private ComboBoxModel   model;

    private int             maxColumns = 10;

    private int             columns    = 3;    // initial width

    /**
     * The default value of 0 indicates that there is no limit on the
     * number of rows to display in the pop-up window
     */
    private int             maxRows    = 3;

    /**
     * A list of ActionListeners registered for this component
     */
    protected ArrayList     actionListeners;

    /**
     * A list of ItemListeners registered for this component
     */
    protected ArrayList     itemListeners;

    private JComboBox.Popup popup;
    
    protected boolean       isEditable;
    
    protected ColorPair     arrowColor;
    protected ColorPair     selectedColor;
    protected ColorPair     disabledColor;
    
    protected ColorPair     listColor;
    protected ColorPair     listSelectedColor;
    
    
    /**
     * Creates an empty JComboBox
     */
    public JComboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        setModel(model);
    }

    /**
     * Creates a JComboBox with the given model
     */
    public JComboBox(ComboBoxModel model) {
        setModel(model);
    }

    /**
     * Creates a JComboBox that contains the elements in the specified array
     *
     * @param items  the array of items to display in the combobox
     */
    public JComboBox(Object[] items) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);
        setModel(model);
    }

    /**
     * Sets the data model that the JComboBox uses to obtain the list of items
     */
    public void setModel(ComboBoxModel model) {
        this.model = model;

        for (int i = 0; i < model.getSize(); i++) {
            String str = model.getElementAt(i).toString();
            if (str.length() > columns)
                columns = str.length();
            
            if (columns >= maxColumns) {
                columns = maxColumns;
                break;
            }
        }

        if (super.isDisplayable()) {
            super.repaint();
        }
    }

    /**
     * Add the specified item into the list of items.
     * <p>
     * Note that this method works only if the data model is 
     * a MutableComboBoxModel (by default, it is).
     */
    public void addItem(Object item) {
        ((MutableComboBoxModel) model).addElement(item);
        final int len = item.toString().length();
        if (len > columns) {
            columns = len;
        }
    
        if (columns >= maxColumns) {
            columns = maxColumns;
        }
    }

    /**
     * Insert the specified item at the specified index.
     * <p>
     * Note that this method works only if the data model is
     * a MutableComboBoxModel (by default, it is).
     */
    public void insertItemAt(Object item, int index) {
        ((MutableComboBoxModel) model).insertElementAt(item, index);
        final int len = item.toString().length();
        if (len > columns) {
            columns = len;
        }
    
        if (columns >= maxColumns) {
            columns = maxColumns;
        }
    }

    /**
     * Remove the item at the specified index.
     * <p>
     * Note that this method works only if the data model is
     * a MutableComboBoxModel (by default, it is).
     */
    public void removeItemAt(int index) {
        ((MutableComboBoxModel) model).removeElementAt(index);
    }

    /**
     * Removes the specified item from the combobox's list. If the
     * item was not in the list, the list is not changed.
     * <p>
     * Note that this method works only if the data model is
     * a MutableComboBoxModel (by default, it is).
     */
    public void removeItem(Object item) {
        ((MutableComboBoxModel) model).removeElement(item);
    }

    /**
     * Removes all items.
     * <p/>
     * Note that this method works only if the data model is
     * a MutableComboBoxModel (by default, it is).
     */
    public void removeAllItems() {
        while (model.getSize() > 0)
            removeItemAt(0);
    }

    /**
     * Returns the selected item
     */
    public Object getSelectedItem() {
        return model.getSelectedItem();
    }

    /**
     * Sets the selected item in the JComboBox by specifying the
     * object in the list
     */
    public void setSelectedItem(Object obj) {
        model.setSelectedItem(obj);
    }

    /**
     * Returns the first item in the list that matches the given item.
     * <p>
     * The result is not always defined if the <code>JComboBox</code>
     * allows selected items that are not in the list.
     * Returns -1 if there is no selected item or if the user specified
     * an item which is not in the list.
     *
     * @return an integer specifying the currently selected list item,
     *         where 0 specifies the first item in the list;
     *         or -1 if no item is selected or if the currently selected
     *         item is not in the list
     */
    public int getSelectedIndex() {
        Object selectedItem = model.getSelectedItem();
        for (int i = 0; i < model.getSize(); i++) {
            Object obj = model.getElementAt(i);
            if (obj != null && obj.equals(selectedItem)) {
                return i;
            }
        }
        
        return -1;
    }

    /**
     * Sets the selected item in the JComboBox by specifying
     * the index in the list
     */
    public void setSelectedIndex(int index) {
        Object selected = model.getElementAt(index);
        model.setSelectedItem(selected);
    }

    /**
     * Sets the maximum number of rows that the JComboBox displays
     */
    public void setMaximumRowCount(int maxRows) {
        if (maxRows <= 0)
            throw new IllegalArgumentException("maxRows: " + maxRows);
        
        this.maxRows = maxRows;
    }
    
    public void setMaximumColumns(int maxColumns) {
        if (maxColumns <= 0)
            throw new IllegalArgumentException("maxColumns: " + maxColumns);
        
        this.maxColumns = maxColumns;
        
        if (columns >= maxColumns) {
            columns = maxColumns;
        }
    }

    /**
     * Returns width (including the diamond symbol)
     */
    public int getWidth() {
        Insets insets = super.getInsets();
        return columns + insets.left + insets.right + 1;
    }

    public int getHeight() {
        Insets insets = super.getInsets();
        return 1 + insets.top + insets.bottom;
    }

    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public Dimension getMinimumSize() {
        return this.getSize();
    }

    public void setColors(ColorScheme colors) {
        super.setColors(colors);
        
        arrowColor        = colors.getColor(ColorScheme.DIALOG);
        
        color             = colors.getColor(ColorScheme.EDIT);
        selectedColor     = colors.getColor(ColorScheme.EDIT_SELECTED);
        disabledColor     = colors.getColor(ColorScheme.EDIT_DISABLED);
        
        listColor         = colors.getColor(ColorScheme.COMBO);
        listSelectedColor = colors.getColor(ColorScheme.COMBO_SELECTED);
    }
    
    public void paint(Graphics g) {
        // Draw the border if it exists
        super.paint(g);

        Insets insets = getInsets();
        g.translate(insets.left, insets.right);

        ColorPair color;
        if (isEnabled()) {
            color = getColor();
            if (hasFocus()) {
                color = selectedColor;
            }
        } else {
            color = disabledColor;
        }
        
        StringBuffer blanks = new StringBuffer();
        for (int i = 0; i < columns; i++) {
            blanks.append(' ');
        }
        
        String emptyRow    = blanks.toString();
        Object selectedObj = model.getSelectedItem();
        String item        = (selectedObj == null ? "" : selectedObj.toString());
        
        final int len = item.length();
        
        g.setColor(color);
        
        if (len < columns) {
            g.drawString(item, 0, 0);
            g.drawString(emptyRow.substring(len), len, 0);
        
        } else if (len > columns) {
            g.drawString(item.substring(0, columns), 0, 0);
        
        } else {
            g.drawString(item, 0, 0);
        }
        
        g.setColor(arrowColor);
        g.drawChar(GraphicsConstants.VS_ARROW_DOWN, columns, 0);
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

    /**
     * Register an ActionListener object for this component
     */
    public void addActionListener(ActionListener al) {
        if (actionListeners == null)
            actionListeners = new ArrayList();
        
        if (!actionListeners.contains(al))
            actionListeners.add(al);
    }

    public void removeActionListener(ActionListener al) {
        if (actionListeners == null)
            return;
        
        actionListeners.remove(al);
    }

    /**
     * Invoke all the ActionListener callbacks that may have been registered
     * for this component.
     */
    protected void fireActionEvent(ActionEvent ae) {
        if (actionListeners != null) {
            for (int i = actionListeners.size() - 1; i >= 0; i--) {
                ActionListener al = (ActionListener) actionListeners.get(i);
                al.actionPerformed(ae);
            }
        }
    }

    /**
     * Overrides method in superclass
     */
    public void processEvent(AWTEvent evt) {
        super.processEvent(evt);

        if (evt instanceof ActionEvent) {
            fireActionEvent((ActionEvent) evt);
            ItemEvent itemEvent = new ItemEvent(this, this, ItemEvent.SELECTED);
            fireItemStateChanged(itemEvent);
        }
    }

    /**
     * Process KeyEvents that have been generated by this JComboBox component
     */
    protected void processKeyEvent(KeyEvent ke) {
        super.processKeyEvent(ke);
        if (ke.isConsumed())
            return;

        final int key = ke.getKeyCode();
        
        if (key == KeyEvent.VK_ENTER 
                || (key == KeyEvent.VK_DOWN && ke.isControlDown())) {
            
            activate();
            return;
        }
        
        if ((key == KeyEvent.VK_TAB && ke.isShiftDown()) 
                || key == KeyEvent.VK_UP
                || key == KeyEvent.VK_LEFT) {
            
            transferFocusBackward();
            return;
        }
        
        if (key == KeyEvent.VK_TAB 
                || key == KeyEvent.VK_DOWN
                || key == KeyEvent.VK_RIGHT) {
            
            transferFocus();
            return;
        }
    }

    /**
     * Process a MouseEvent that was generated by clicking the mouse
     * on this JComboBox.
     */
    protected void processMouseEvent(MouseEvent e) {
        // Request focus if this is a MOUSE_PRESSED
        super.processMouseEvent(e);
        if (e.isConsumed())
            return;

        if (e.getButton() == MouseEvent.BUTTON1 &&
                e.getID() == MouseEvent.MOUSE_CLICKED &&
                this.isFocusTraversable()) {

            if (popup != null && popup.isVisible())
                popup.hide();
            else
                activate();
        }
    }

    /**
     * Makes this combobox editable
     */
    public void setEditable(boolean editable) {
        isEditable = editable;
        //TODO: implement editable JComboBox
    }

    public void requestFocus() {
        // Generate the FOCUS_GAINED event
        super.requestFocus();

        if (getSelectedItem() == null && model.getSize() > 0)
            setSelectedIndex(0);
        
        // Get the absolute origin of this component
        Point  origin = getLocationOnScreen();
        Insets insets = getInsets();
        
        SwingUtilities.windowForComponent(this).setCursor(
                origin.addOffset(insets.left, insets.top));
    }

    /**
     * Returns a string representation of this <code>JComboBox</code>.
     * This method is intended to be used only for debugging purposes,
     * and the content and format of the returned string may vary between   
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JComboBox</code>
     */
    protected String paramString() {
        String isEditableString = (isEditable ? "true" : "false");

        return super.paramString() + ",isEditable=" + isEditableString
                + ",maxColumns=" + maxColumns
                + ",maxRows=" + maxRows;
    }
    
    private void activate() {
        popup = this.new Popup(this, model);
        popup.setMaximumRowCount(maxRows);
        popup.setWidth(getWidth() + 1);
        popup.setColors(getColors());

        // Get the absolute origin of this component
        Point origin = getLocationOnScreen();
        popup.setLocation(origin.addOffset(-1, 1));
        popup.show();
        
        // active item is probably changed
        repaint();
    }

    /**
     * This is a non-static inner class that implements the popup
     * window for the JComboBox component.
     */
    private class Popup extends JPopupWindow {
        
        private List   list;
        
        
        Popup(JComboBox parent, ComboBoxModel model) {
            super(SwingUtilities.windowForComponent(parent));
            setShadow(false);
            
            list = new List(this);
            //list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            JScrollPane scrollpane = new JScrollPane(list);
            scrollpane.setBorder(new LineBorder(
                    ColorPair.valueOf(listColor.getColorCode())));

            // ComboBoxModel is a subclass of ListModel, so this works
            list.setModel(model);
            //list.setColumns(columns);
            
            add(scrollpane);
            
            list.setSelectedIndex(parent.getSelectedIndex());
        }
        
        public void setColors(ColorScheme colors) {
            super.setColors(colors);
            
            if (list != null) {
                list.setColor(listColor);
                list.selectedColor    = listSelectedColor;
                list.highlightedColor = listSelectedColor;
            }
        }
        
        /**
         * Set the maximum number of rows to be displayed
         */
        void setMaximumRowCount(int rows) {
            list.setVisibleRowCount(rows);

            pack();
        }
        
        protected boolean checkCloseByMouse(MouseEvent me) {
            Window    w = SwingUtilities.windowForComponent(JComboBox.this);
            final int x = getX() + me.getX() - w.getX();
            final int y = getY() + me.getY() - w.getY();

            // If the mouse has been pressed outside the JComboBox's window
            if (!w.contains(x, y))
                return true;

            Component component = w.findComponentAt(x, y);
            return (component != JComboBox.this);
        }
        
        protected void processKeyEvent(KeyEvent ke) {
            super.processKeyEvent(ke);
            if (ke.isConsumed())
                return;
            
            if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                hide();
                return;
            }
        
            if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                doAction();
                return;
            }
        }
        
        private void doAction() {
            hide();

            // If the user presses ENTER on the list entry that is
            // already selected, it becomes deselected in the JList;
            // so that list.getselectedValue() returns null. The
            // caller must take this into account and ignore the value.
            Object selectedItem = list.getSelectedValue();
            if (selectedItem != null) {
                model.setSelectedItem(selectedItem);

                // Put an ActionEvent onto the system event queue
                EventQueue evtqueue =
                        Toolkit.getDefaultToolkit().getSystemEventQueue();
    
                evtqueue.postEvent(new ActionEvent(JComboBox.this, 
                        model.getSelectedItem().toString()));
            }
        }
    }
    
    private class List extends JList {
        
        Popup   parent;
        
        public List(Popup parent) {
            this.parent = parent;
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        
        protected void processMouseEvent(MouseEvent me) {
            super.processMouseEvent(me);
            
            if (me.getID() == MouseEvent.MOUSE_CLICKED
                    && me.getButton() == MouseEvent.BUTTON1
                    && me.getClickCount() == 1) {
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        parent.doAction();
                    }
                });
                return;
            }
        }
    }
}
