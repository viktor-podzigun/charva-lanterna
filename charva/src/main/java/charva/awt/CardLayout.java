
package charva.awt;

import java.util.HashMap;


/**
 * 
 * 
 * @author lapo@lapo.it
 */
public class CardLayout implements LayoutManager2 {

    protected HashMap   names   = new HashMap();
    protected int       current = -1;
    protected int       max;

    
    public void addLayoutComponent(Component component, Object constraints) {
        if (!(constraints instanceof String)) {
            throw new IllegalArgumentException(
                    "Cannot add to layout: constraint must be a string");
        }
        
        String name = (String) constraints;
        if ((max == 0) && (component.isVisible()))
            current = 0;
        
        if (max > 0) {
            // initially only the first card may be visible
            component.setVisible(false);
        }
        
        names.put(name, new Integer(max));
        
        ++max;
    }

    /**
     * @deprecated   replaced by
     *      <code>addLayoutComponent(Component, Object)</code>.
     */
    public void addLayoutComponent(String name, Component comp) {
        addLayoutComponent(comp, name);
    }
    
    /**
     * Removes the specified component from the layout
     * 
     * @param comp  the component to be removed
     */
    public void removeLayoutComponent(Component comp) {
        //TODO: implement me
    }

    public void invalidateLayout(Container target) {
        // this layout manager caches nothing
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension min = new Dimension(0, 0);
        for (int i = 0, j = parent.getComponentCount(); i < j; ++i) {
            Dimension t = parent.getComponent(i).getMinimumSize();
            if (t.width > min.width)
                min.width = t.width;
            if (t.height > min.height)
                min.height = t.height;
        }
        
        Insets insets = parent.getInsets();
        min.width += insets.left + insets.right;
        min.height += insets.top + insets.bottom;
        return min;
    }

    public void layoutContainer(Container parent) {
        Dimension size = parent.getSize();
        Insets insets = parent.getInsets();
        Point cardOrigin = new Point(insets.left, insets.top);
        Dimension cardSize = new Dimension(size.width
                - (insets.left + insets.right), size.height
                - (insets.top + insets.bottom));
        
        for (int i = 0, j = parent.getComponentCount(); i < j; ++i) {
            Component c = parent.getComponent(i);
            c.setBounds(cardOrigin, cardSize);
            if (c instanceof Container)
                ((Container) c).doLayout();
        }
    }

    /**
     * Flips to the component with the given insertion index. If no such
     * component exists, then nothing happens.
     * 
     * @param container  the parent container in which to do the layout
     * @param index      the component index
     * 
     * @see #addLayoutComponent(Component, Object)
     */
    public void show(Container container, int index) {
        if ((index < 0) || (index >= max) || (index == current))
            return;
        
        for (int i = 0, j = container.getComponentCount(); i < j; ++i) {
            Component c = container.getComponent(i);
            if (c.isVisible())
                c.setVisible(false);
        }
        
        current = index;
        container.getComponent(index).setVisible(true);
        container.getComponent(index).requestFocus();
        container.validate();
    }

    /**
     * Flips to the component that was added to this layout with the specified
     * <code>name</code>, using <code>addLayoutComponent</code>. If no
     * such component exists, then nothing happens.
     * 
     * @param container  the parent container in which to do the layout
     * @param name       the component name
     * 
     * @see #addLayoutComponent(Component, Object)
     */
    public void show(Container container, String name) {
        Integer val = (Integer) names.get(name);
        if (val != null)
            show(container, val.intValue());
    }

    /**
     * Flips to the first card of the container
     * 
     * @param container  the parent container in which to do the layout
     * 
     * @see #last(Container)
     */
    public void first(Container container) {
        show(container, 0);
    }

    /**
     * Flips to the last card of the container
     * 
     * @param container  the parent container in which to do the layout
     * 
     * @see #first(Container)
     */
    public void last(Container container) {
        show(container, max - 1);
    }
}
