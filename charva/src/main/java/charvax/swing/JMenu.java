/* class JMenu
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

import java.util.ArrayList;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.GraphicsConstants;
import charva.awt.Point;
import charva.awt.Toolkit;
import charva.awt.event.ActionEvent;
import charva.awt.event.KeyEvent;


/**
 * Implements a menu containing JMenuItems and JSeparators
 */
public class JMenu extends JMenuItem {

    private ArrayList       menuItems = new ArrayList();
    
    private JPopupMenu      popupMenu;

    
    /**
     * Constructs a new JMenu with no text
     * (the text can be set later with the setText() method of the
     * superclass).
     */
    public JMenu() {
        super();
    }

    /**
     * Constructs a new JMenu with the specified string as its text
     */
    public JMenu(String text) {
        super(text);
    }

    /**
     * Add a JMenuItem (or JMenu) to the end of this JMenu.
     *
     * @return the JMenuItem that was added.
     */
    public JMenuItem add(JMenuItem item) {
        menuItems.add(item);
        return item;
    }

    /**
     * Add a horizontal separator to the end of the menu.
     */
    public void addSeparator() {
        menuItems.add(new JSeparator());
    }

    /**
     * Create a JMenuItem with the specified label and add it to the
     * menu.
     *
     * @return a reference to the newly created JMenuItem.
     */
    public JMenuItem add(String text) {
        JMenuItem item = new JMenuItem(text);
        add(item);
        return item;
    }

    public void paint(Graphics g) {
        // draw this menu item
        super.paint(g);
        
        if (!isTopLevelMenu()) {
            paintChar(g, getWidth() - 1, GraphicsConstants.VS_RIGHT);
        }
    }

    /**
     * Returns the menu item at the specified index.
     * If the object at the specified index is a JSeparator, it returns null.
     */
    public JMenuItem getMenuItem(int index) {
        Object o = menuItems.get(index);
        if (o instanceof JMenuItem) {
            return (JMenuItem) o;
        }
        
        return null;
    }

    public void fireActionPerformed(ActionEvent ae) {
        // Notify all the registered ActionListeners.
        super.fireActionPerformed(ae);

        int closeKey = showPopupMenu();

        // We get here when the popup menu has hidden itself.
        if (closeKey == KeyEvent.VK_LEFT) {
            Toolkit.getDefaultToolkit().fireKeystroke(KeyEvent.VK_LEFT);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    Toolkit.getDefaultToolkit().fireKeystroke(KeyEvent.VK_ENTER);
                }
            });
        } else if (closeKey == KeyEvent.VK_RIGHT) {
            Toolkit.getDefaultToolkit().fireKeystroke(KeyEvent.VK_RIGHT);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    Toolkit.getDefaultToolkit().fireKeystroke(KeyEvent.VK_ENTER);
                }
            });
        }
    }

    /**
     * Displays this menu's pop-up menu
     * 
     * @return key code that triggered close operation
     */
    protected int showPopupMenu() {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
            
            final int menuCount = menuItems.size();
            for (int i = 0; i < menuCount; i++) {
                Object item = menuItems.get(i);
                if (item instanceof JMenuItem) {
                    popupMenu.add((JMenuItem)item);
                } else if (item instanceof JSeparator) {
                    popupMenu.addSeparator();
                }
            }
        }
        
        Point menuLocation;
        if (!isTopLevelMenu()) {
            // If this menu is a submenu (i.e. it is not a direct
            // child of the menubar), check if there is enough
            // space on the right hand side of the parent menu.
            // If there is not enough space, position it on the
            // left of the parent menu.

            JPopupMenu parentpopup = (JPopupMenu)getParent();
            //JMenu parentmenu = (JMenu) getParentMenu();
            //JPopupMenu parentpopup = parentmenu.getPopupMenu();
            Point p = parentpopup.getLocationOnScreen();
            
            int verticalOffset = parentpopup.getComponentIndex(this);
            int parentwidth = parentpopup.getSize().width;
            
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            if (p.x + parentwidth + popupMenu.getWidth() < dim.width) {
                menuLocation = p.addOffset(parentwidth - 1, 
                        verticalOffset);
            } else {
                menuLocation = p.addOffset(-popupMenu.getWidth() + 1, 
                        verticalOffset);
            }
        } else {
            JMenuBar parentMenuBar = (JMenuBar) getParent();
            menuLocation = parentMenuBar.getPopupMenuLocation(this);
        }
        
        // On showing the popup menu, always give focus to the first JMenuItem
        //popupMenu.setFocus(popupMenu.getFirstMenuItem());
        popupMenu.setLocation(menuLocation.x, menuLocation.y);
        popupMenu.setInvoker(this);
        popupMenu.show();
        
        return popupMenu.getCloseKeyCode();
    }

    /**
     * Returns true if this menu is the direct child of a menubar
     */
    public boolean isTopLevelMenu() {
        return (getParent() instanceof JMenuBar);
    }
}
