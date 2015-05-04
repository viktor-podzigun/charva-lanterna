/* class JTabbedPane
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
 * Modified Jul 14, 2003 by Tadpole Computer, Inc.
 * Modifications Copyright 2003 by Tadpole Computer, Inc.
 *
 * Modifications are hereby licensed to all parties at no charge under
 * the same terms as the original.
 *
 * Modified to allow tabs to be focus traversable.  Also added the
 * setEnabledAt and isEnabledAt methods.
 */

package charvax.swing;

import java.util.ArrayList;
import charva.awt.BorderLayout;
import charva.awt.Component;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.GraphicsConstants;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;


/**
 * A component that lets the user display one of a set of components (usually
 * Panels) at a time. The management of the tab-selection has to be performed
 * outside of this component because it is possible that the currently selected
 * tab does not contain any focusTraversable components. In this case, this
 * JTabbedPane will never get the keyboard focus.
 */
public class JTabbedPane extends JComponent {

    private ArrayList       tabComponents  = new ArrayList();
    private ArrayList       tabs           = new ArrayList();
    private int             selectedIndex  = -1;
    private ButtonGroup     buttongroup    = new ButtonGroup();
    
    private Insets          insets;

    
    /**
     * Constructs a JTabbedPane
     */
    public JTabbedPane() {
        insets    = new Insets(2, 1, 1, 1);
        layoutMgr = new BorderLayout();
    }

    /**
     * Add the specified component to the tabbed pane. If
     * <code>constraints</code> is a String, it will be used as the tab's
     * title; otherwise the component's name will be used as the title.
     */
    public void add(Component component, Object constraints) {
        String label;
        if (constraints instanceof String) {
            label = (String)constraints;
        } else {
            label = component.getName();
        }

        addTab(label, 0, component);
    }

    /**
     * Add a new tab with the specified component, title and function-key
     * 
     * @param title      the title of this tab
     * @param mnemonic   the key code that can be pressed to select this tab
     * @param component  the component to be added in this tab
     */
    public void addTab(String title, int mnemonic, Component component) {
        TabButton tb = new TabButton(title, component);
        tb.setMnemonic(mnemonic);
        
        tabComponents.add(component);
        add(tb);
        tabs.add(tb);
        buttongroup.add(tb);

        // arrange for our TabButton to be in the focus list...
        if (selectedIndex == -1) {
            setSelectedIndex(0);
        
        } else if (isDisplayable()) {
            // If this component is already displayed, generate a PaintEvent 
            // and post it onto the queue
            repaint();
        }
    }

    /**
     * Makes the tabbed button at the given index selected
     */
    public void setSelectedIndex(int index) {
        if (index >= tabComponents.size()) {
            throw new IndexOutOfBoundsException();
        }

        if (index == selectedIndex) {
            return;
        }

        // remove the previously-selected component from the container

        if (selectedIndex != -1 && selectedIndex < tabComponents.size()) {
            super.remove((Component)tabComponents.get(selectedIndex));
            // components.remove(tabComponents)
            // currentFocus = null;
        }

        selectedIndex = index;
        
        // add the newly-selected component to the container
        Component selected = (Component) tabComponents.get(index);
        super.add(selected);
        super.validate();
        repaint();

        TabButton tb = (TabButton) tabs.get(index);
        tb.setSelected(true);
        
        if (SwingUtilities.windowForComponent(this) != null) {
            tb.requestFocus();
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedComponent(Component component) {
        int index = tabComponents.indexOf(component);
        setSelectedIndex(index);
    }

    public Component getSelectedComponent() {
        if (selectedIndex != -1) {
            return (Component) tabComponents.get(selectedIndex);
        }
        
        return null;
    }

    public Dimension getSize() {
        // Override the method in Container. Balazs Poka suggested that we
        // shouldn't override getSize(), but my applications don't work unless 
        // I do override. (Rob).
        return getMinimumSize();
    }
    
    public Insets getInsets() {
        return insets;
    }

    /**
     * Override the method in Container.
     */
    public Dimension getMinimumSize() {
        if (super.isValid()) {
            return minimumSize;
        }

        // scan through the components in each tab and determine the smallest
        // rectangle that will enclose all of them
        int width  = 0;
        int height = 0;
        final int countComponents = tabComponents.size();
        for (int i = 0; i < countComponents; i++) {
            Component c = (Component) tabComponents.get(i);
            Dimension size = c.getMinimumSize();
            if (size.width > width) {
                width = size.width;
            }
            if (size.height > height) {
                height = size.height;
            }
        }

        // now scan through the titles of the tabs, and determine the width 
        // that all of them will fit into
        int tabwidth = 0;
        final int countTabs = tabs.size();
        for (int i = 0; i < countTabs; i++) {
            tabwidth += ((TabButton)tabs.get(i)).getWidth();
        }
        
        tabwidth += 2;
        if (tabwidth > width) {
            width = tabwidth;
        }

        // take into account the border and the height of the tabs
        Insets insets = getInsets();
        minimumSize = new Dimension(width + insets.left + insets.right,
                height + insets.top + insets.bottom);

        //isValid = true;
        return minimumSize;
    }

    public void paint(Graphics g) {
        g.setColor(getColor());

        // draw the enclosing frame
        Dimension size = getSize();
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawRect(0, 1, size.width, size.height - 1);

        // draw each of the tabs
        int hoffset = 1;
        final int countTabs = tabs.size();
        Point relative = new Point(0, 0);
        for (int i = 0; i < countTabs; i++) {
            TabButton c = (TabButton)tabs.get(i);
            c.setLocation(relative.addOffset(hoffset, 0));
            c.paint(g.create(c.getX(), c.getY(), 
                    c.getWidth(), c.getHeight()));
            
            hoffset += c.getWidth();
        }

        // now draw the selected component
        if (selectedIndex != -1) {
            Component c = (Component)tabComponents.get(selectedIndex);

            // Note that we draw the component even if isVisible() would be
            // false; it doesn't make sense to make a component invisible in a
            // JTabbedPane.
            c.paint(g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
        }
    }

    /**
     * Removes the tab and component which corresponds to the specified index
     */
    public void remove(int index) {
        // save this just in case
        // Component selected = (Component) tabComponents.elementAt(0);
        // remove little tab from parent
        super.remove((Component) tabs.get(index));
        
        // remove it from here also
        TabButton tb = (TabButton) tabs.remove(index);
        // here comes the button group
        buttongroup.remove(tb);
        // remove deleted container from parent
        super.remove((Component) tabComponents.get(index));
        // and from here
        tabComponents.remove(index);

        if (getSelectedIndex() == index) {
            selectedIndex = -1;
            if (index - 1 < 0) {
                if (getTabCount() > 0) {
                    // we NEED to revalidate selection
                    setSelectedIndex(0);
                } else {
                    this.setFocus(null);
                    super.validate();
                }
            } else {
                setSelectedIndex(index - 1);
            }
        }

        repaint();
    }

    /**
     * Returns the first tab index with the specified title, or -1 if no tab 
     * has the title
     */
    public int indexOfTab(String title) {
        final int countTabs = tabs.size();
        for (int i = 0; i < countTabs; i++) {
            if (title.equals(((TabButton) tabs.get(i)).getText())) {
                return (i);
            }
        }
        
        return -1;
    }

    /**
     * Returns the tab's button with the specified index
     */
    public JButton getButtonAt(int index) {
        return (TabButton)tabs.get(index);
    }

    /**
     * Returns the number of tabs in this tabbed pane
     */
    public int getTabCount() {
        return tabs.size();
    }

    private class TabButton extends JButton implements ActionListener {

        private Component   c;

        public TabButton(String label, Component c) {
            super(label);

            this.c = c;

            addActionListener(this);
        }

        public void actionPerformed(ActionEvent ev) {
            setSelectedComponent(c);
        }

        public void requestFocus() {
            super.requestFocus();
            
            Point  origin = getLocationOnScreen();
            Insets insets = getInsets();
            
            SwingUtilities.windowForComponent(this).setCursor(
                    origin.addOffset(2 + insets.left, insets.top));
        }

        public void paint(Graphics g) {
            Insets insets = getInsets();
            g.translate(insets.left, insets.top);

            paintText(g, "  " + getText() + "  ");
            
            g.setColor(JTabbedPane.this.getColor());
            g.drawChar(GraphicsConstants.VS_ULCORNER, 0, 0);
            g.drawChar(GraphicsConstants.VS_URCORNER, getText().length() + 3, 0);

            if (isSelected()) {
                g.drawChar(GraphicsConstants.VS_LRCORNER, 0, 1);
                final int len = getText().length() + 2;
                for (int j = 0; j < len; j++) {
                    g.drawChar(' ', j + 1, 1);
                }
                
                g.drawChar(GraphicsConstants.VS_LLCORNER, len + 1, 1);
            
            } else {
                g.drawChar(GraphicsConstants.VS_BTEE, 0, 1);
                g.drawChar(GraphicsConstants.VS_BTEE, getText().length() + 3, 1);
            }
        }

        public int getWidth() {
            Insets insets = getInsets();
            return getText().length() + insets.left + insets.right + 4;
        }
    }
}
