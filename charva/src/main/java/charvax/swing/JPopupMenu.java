/* class JPopupMenu
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

package charvax.swing;

import charva.awt.ColorScheme;
import charva.awt.Component;
import charva.awt.IllegalComponentStateException;
import charva.awt.Point;
import charva.awt.Toolkit;
import charva.awt.event.ActionEvent;
import charva.awt.event.KeyEvent;
import charvax.swing.border.AbstractBorder;
import charvax.swing.border.Border;
import charvax.swing.border.DoubleLineBorder;
import charvax.swing.border.TitledBorder;


/**
 * An implementation of a popup menu - a small window that pops up and
 * displays a number of choices.
 */
public class JPopupMenu extends JComponent {

    private Component       invoker;
    private String          label;
    
    private JPopupWindow    popup;
    private int             closeKeyCode;
    
    
    public JPopupMenu() {
        this(null);
    }
    
    /**
     * Constructs a <code>JPopupMenu</code> with the specified title.
     *
     * @param label  the string that a UI may use to display as a title 
     * for the popup menu.
     */
    public JPopupMenu(String label) {
        setBorder(new DoubleLineBorder(null));
        setLabel(label);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Appends the specified menu item to the end of this menu. 
     *
     * @param menuItem the <code>JMenuItem</code> to add
     * @return the <code>JMenuItem</code> added
     */
    public JMenuItem add(JMenuItem menuItem) {
        super.add(menuItem);
        return menuItem;
    }

    /**
     * Creates a new menu item with the specified text and appends
     * it to the end of this menu.
     *  
     * @param s the string for the menu item to be added
     */
    public JMenuItem add(String s) {
        return add(new JMenuItem(s));
    }
    
    /**
     * Appends a new separator at the end of the menu.
     */
    public void addSeparator() {
        super.add(new JSeparator());
    }

    /**
     * Returns the popup menu's label
     *
     * @return a string containing the popup menu's label
     * 
     * @see #setLabel
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Sets the popup menu's label.  Different look and feels may choose
     * to display or not display this.
     *
     * @param label a string specifying the label for the popup menu
     *
     * @see #getLabel
     */
    public void setLabel(String label) {
        String oldValue = this.label;
        this.label = label;
        if (label != null) {
            Border border = getBorder();
            if (border instanceof TitledBorder) {
                ((TitledBorder)border).setTitle(label);
            } else {
                setBorder(new TitledBorder(border, 
                        label, TitledBorder.CENTER, TitledBorder.TOP));
            }
        }
        
        firePropertyChange("label", oldValue, label);
        invalidate();
        repaint();
    }

    public void setColors(ColorScheme colors) {
        super.setColors(colors);
    
        color = colors.getColor(ColorScheme.MENU);
        
        if (border instanceof AbstractBorder) {
            ((AbstractBorder)border).setLineColor(
                    colors.getColor(ColorScheme.MENU_BORDER));
        
        } else if (border instanceof TitledBorder) {
            TitledBorder tb = (TitledBorder)border;
            Border        b = tb.getBorder();
            if (b instanceof AbstractBorder) {
                ((AbstractBorder)b).setLineColor(
                        colors.getColor(ColorScheme.MENU_BORDER));
            }
            
            tb.setTitleColor(colors.getColor(ColorScheme.MENU_TITLE));
        }
    }
    
    /**
     * Sets the invoker of this popup menu -- the component in which
     * the popup menu is to be displayed.
     *
     * @param invoker the <code>Component</code> in which the popup
     *      menu is displayed
     */
    public void setInvoker(Component invoker) {
        this.invoker = invoker;
    }

    /**
     * Returns the component which is the 'invoker' of this 
     * popup menu.
     *
     * @return the <code>Component</code> in which the popup menu is displayed
     */
    public Component getInvoker() {
        return invoker;
    }

    /**
     * Returns the menu item at the specified index. If the item
     * is a JSeparator, it returns null.
     */
    private JMenuItem getMenuItem(int index) {
        Component c = getComponent(index);
        if (c instanceof JMenuItem)
            return (JMenuItem)c;
        
        return null;
    }

    boolean hasRootMenuBar() {
        Component c = getInvoker();
        while (c instanceof JMenu && !((JMenu)c).isTopLevelMenu()) {
            c = c.getParent();
            if (c instanceof JPopupMenu) {
                c = ((JPopupMenu)c).getInvoker();
            }
        }
        
        return (c instanceof JMenu);
    }
    
    private void updateMenuItemsWidth(int newWidth) {
        final int count = getComponentCount();
        for (int i = 0; i < count; i++) {
            JMenuItem item = getMenuItem(i);
            if (item != null) {
                item.setWidth(newWidth);
            }
        }
    }

    /**
     * Returns true if the popup menu is visible (currently being displayed)
     */
    public boolean isVisible() {
        return (popup != null);
    }

    /**
     * Sets the location of the upper left corner of the
     * popup menu using x, y coordinates.
     *
     * @param x the x coordinate of the popup's new position
     * @param y the y coordinate of the popup's new position
     */
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        
//        int oldX = desiredLocationX;
//        int oldY = desiredLocationY;
//
//        desiredLocationX = x;
//        desiredLocationY = y;
//        if(popup != null && (x != oldX || y != oldY)) {
//            popup = getPopup();
//        }
    }

    /**
     * Displays the popup menu at the position x,y in the coordinate
     * space of the component invoker.
     *
     * @param invoker the component in whose space the popup menu is to appear
     * @param x       the x coordinate in invoker's coordinate space at which 
     *                the popup menu is to be displayed
     * @param y       the y coordinate in invoker's coordinate space at which 
     *                the popup menu is to be displayed
     */
    public void show(Component invoker, int x, int y) {
        setInvoker(invoker);
        if (invoker != null) {
            Point invokerOrigin = invoker.getLocationOnScreen();
            setLocation(invokerOrigin.x + x, invokerOrigin.y + y);
        } else {
            setLocation(x, y);
        }
        
        setVisible(true);
    }

    public void show() {
        //super.show();
        visible = true;
        
        // reset close key
        closeKeyCode = KeyEvent.VK_UNDEFINED;

        int maxWidth = 0;
        final int compCount = getComponentCount();
        for (int i = 0; i < compCount; i++) {
            JMenuItem item = getMenuItem(i);
            if (item != null) {
                final int w = item.getText().length();
                if (w > maxWidth) {
                    maxWidth = w;
                }
            }
        }
        
        updateMenuItemsWidth(maxWidth + 4);
        
        if (invoker == null) {
            throw new IllegalComponentStateException("Invoker have to be set");
        }

        popup = new JPopupWindow(SwingUtilities.windowForComponent(invoker));
        popup.add(this);
        popup.setLocation(getX(), getY());
        popup.show();
    }
    
    public void hide() {
        //super.hide();
        visible = false;
        
        if (popup != null) {
            popup.hide();
            popup = null;
        }
    }

    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        if (e.isConsumed())
            return;
        
        if (!visible)
            return;    // the popup has already been dismissed.

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            super.previousFocus();

        } else if (key == KeyEvent.VK_DOWN) {
            super.nextFocus();

        } else if (key == KeyEvent.VK_LEFT) {
            if (hasRootMenuBar()) {
                closeKeyCode = KeyEvent.VK_LEFT;
                hide();
            }
        } else if (key == KeyEvent.VK_RIGHT) {
            if (hasRootMenuBar()) {
                closeKeyCode = KeyEvent.VK_RIGHT;
                hide();
            }
        } else if (key == KeyEvent.VK_ENTER) {
            // Pressing ENTER sends an ActionEvent. The source of the
            // event is the menu item, not the menu; this means that the
            // client program has to add an ActionListener to each menu
            // item. This is inconvenient, but it's the way that the Java
            // Swing menus do it.
            JMenuItem item = (JMenuItem) getCurrentFocus();
            activate(item);
            e.consume();

        } else if (key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_ESCAPE) {
            // Backspace or ESC was pressed
            hide();
            
        } else if (e.getID() == KeyEvent.KEY_TYPED) {
            // Check if one of the mnemonic keys was pressed.
            // Note that the user can press a lowercase or an uppercase key.
            char keyLower = Character.toLowerCase((char) key);
            for (int i = 0; i < getComponentCount(); i++) {
                JMenuItem item = getMenuItem(i);
                if (item != null) {
                    if (item.getMnemonic() == -1)
                        continue;   // this item doesn't have a mnemonic

                    char mnemonicLower =
                            Character.toLowerCase((char) item.getMnemonic());
                    if (keyLower == mnemonicLower) {
                        activate(item);
                        e.consume();
                        return;
                    }
                }
            }
        }
    }
    
    int getCloseKeyCode() {
        return closeKeyCode;
    }

    /**
     * Private helper method for activating a menu item 
     * (either a JMenuItem or a JMenu)
     */
    private void activate(JMenuItem item) {
        if (item instanceof JMenu) {
            JMenu menu = (JMenu)item;
            int closeKey = menu.showPopupMenu();

            // the popup menu has hidden itself, check close key code
            if (closeKey == KeyEvent.VK_LEFT || closeKey == KeyEvent.VK_RIGHT)
                Toolkit.getDefaultToolkit().fireKeystroke(closeKey);
            
        } else {
            // hide this pop-up menu
            hide();
            
            // hide all parent pop-up menus
            Component c = getInvoker();
            while (c instanceof JMenu) {
                c = c.getParent();
                if (c instanceof JPopupMenu) {
                    JPopupMenu m =(JPopupMenu)c;
                    m.hide();
                    c = m.getInvoker();
                }
            }
            
            // send action event
            ActionEvent evt = new ActionEvent(item, item.getActionCommand());
            Toolkit term = Toolkit.getDefaultToolkit();
            term.getSystemEventQueue().postEvent(evt);
        }
    }
}
