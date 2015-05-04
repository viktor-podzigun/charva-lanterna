/* class JMenuBar
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

import charva.awt.Component;
import charva.awt.Container;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.IllegalComponentStateException;
import charva.awt.Point;
import charva.awt.event.KeyEvent;


/**
 * An implementation of a menubar
 */
public class JMenuBar extends JComponent {

    /**
     * Create a new menu bar
     */
    public JMenuBar() {
        // The menubar is always offset from the origin of its parent
        // JFrame by (1, 1)
        super.origin = new Point(1, 1);
    }

    /**
     * Appends the specified menu to the end of the JMenuBar
     *
     * @param menu  the menu to be added
     * @return      the menu that was added
     */
    public JMenu add(JMenu menu) {
        JMenu jmenu = (JMenu) menu;
        super.add(jmenu);
        return jmenu;
    }

    /**
     * Returns the number of menus in the menubar
     */
    public int getMenuCount() {
        return super.getComponentCount();
    }

    /**
     * Returns the menu at the specified index
     */
    public JMenu getMenu(int index) {
        return (JMenu) super.getComponent(index);
    }

    /**
     * Returns the menu that has the specified text label
     */
    public JMenu getMenu(String text) {
        for (int i = 0; i < getMenuCount(); i++) {
            JMenu menu = getMenu(i);
            if (menu.getText().equals(text))
                return menu;
        }
        
        throw new IllegalArgumentException("Menubar does not contain menu: \"" 
                + text + "\"");
    }

    /**
     * Draw this menubar
     */
    public void paint(Graphics g) {
        // build a horizontal line of spaces extending across the top
        // of the frame
        StringBuffer buf = new StringBuffer();
        int width = getSize().width;
        for (int i = 0; i < width; i++) {
            buf.append(' ');
        }
        
        g.setColor(getColor());
        g.drawString(buf.toString(), 0, 0);

        Component[] menus = super.getComponents();
        int x = 0;
        for (int i = 0; i < menus.length; i++) {
            Component c = menus[i];
            c.setLocation(x, 0);
            c.paint(g.create(c.getX(), c.getY(), 
                    c.getWidth(), c.getHeight()));
            x += c.getWidth();
        }
    }

    protected void processKeyEvent(KeyEvent ke) {
        super.processKeyEvent(ke);
        if (ke.isConsumed())
            return;
        
        final int key = ke.getKeyCode();
        
        if ((key == KeyEvent.VK_TAB && ke.isShiftDown())) {
            transferFocusBackward();
            ke.consume();
            return;
        }
        
        if (key == KeyEvent.VK_TAB) {
            transferFocus();
            ke.consume();
            return;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            Component currentFocus = super.getCurrentFocus();
            int menuCount = getMenuCount();
            int i = 0;
            for (; i < menuCount; i++) {
                if (currentFocus == super.getComponent(i))
                    break;
            }
            
            if (i == menuCount - 1)
                i = 0;
            else
                i++;
            
            getMenu(i).requestFocus();
            ke.consume();
        
        } else if (key == KeyEvent.VK_LEFT) {
            Component currentFocus = super.getCurrentFocus();
            int menuCount = getMenuCount();
            int i = 0;
            for (; i < menuCount; i++) {
                if (currentFocus == super.getComponent(i))
                    break;
            }
            
            if (i == 0)
                i = menuCount - 1;
            else
                i--;
            
            getMenu(i).requestFocus();
            ke.consume();
        
        } else {
            // Check if one of the mnemonic keys was pressed.
            // Note that the user can press a lowercase or an uppercase key.
            char keyLower = Character.toLowerCase((char) key);
            for (int i = 0; i < super.getComponentCount(); i++) {
                JMenu menu = getMenu(i);
                if (menu != null) {
                    if (menu.getMnemonic() == -1)
                        continue;   // this menu doesn't have a mnemonic

                    char mnemonicLower =
                            Character.toLowerCase((char)menu.getMnemonic());
                    if (keyLower == mnemonicLower) {
                        menu.doClick();
                        ke.consume();
                        return;
                    }
                }
            }
        }
    }

    public Dimension getMinimumSize() {
        int width = 0;
        for (int i = 0; i < getMenuCount(); i++)
            width += getMenu(i).getText().length() + 1;

        return new Dimension(width, 1);
    }

    public Dimension getSize() {
        return new Dimension(this.getWidth(), getHeight());
    }

    public int getWidth() {
        // get the width of our parent JFrame.
        Container parent = getParent();
        if (parent == null) {
            throw new IllegalComponentStateException(
                    "Can't get menubar size before " 
                    + "it has been added to a frame");
        }

        int parentwidth = parent.getWidth() - 2;
        int minwidth = getMinimumSize().width;
        int width = (parentwidth > minwidth) ? parentwidth : minwidth;
        return width;
    }

    public int getHeight() {
        return 1;
    }

    /**
     * Computes the absolute screen position for the specified JMenu.
     * This is a Charva-specific package-private method called by
     * the JMenu that wants to pop itself up. It is required because
     * JMenuBar is not implemented as a subclass of Container.
     * It is not intended to be called by application programmers.
     */
    Point getPopupMenuLocation(JMenu m) {
        // Get the origin of this menubar
        Point origin = getLocationOnScreen();

        int offset = 0;
        for (int i = 0; i < getMenuCount(); i++) {
            JMenu menu = getMenu(i);
            if (menu == m) {
                return origin.addOffset(offset, 1);
            }
            
            offset += menu.getWidth();
        }
        
        throw new IllegalArgumentException("Specified menu is not in menubar");
    }
}
