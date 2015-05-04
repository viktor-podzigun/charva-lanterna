/* Copyright (C) 2015 charva-lanterna
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

import charva.awt.ColorPair;
import charva.awt.Component;
import charva.awt.Container;
import charva.awt.Graphics;
import charva.awt.Rectangle;

/** 
 * This class is inserted in between cell renderers and the components that 
 * use them.  It just exists to thwart the repaint() and invalidate() methods 
 * which would otherwise propagate up the tree when the renderer was configured.
 * It's used by the implementations of JTable, JTree, and JList.  For example,
 * here's how CellRendererPane is used in the code the paints each row
 * in a JList:
 * <pre>
 *   cellRendererPane = new CellRendererPane();
 *   ...
 *   Component rendererComponent = renderer.getListCellRendererComponent();
 *   renderer.configureListCellRenderer(dataModel.getElementAt(row), row);
 *   cellRendererPane.paintComponent(g, rendererComponent, this, x, y, w, h);
 * </pre>
 * <p>
 * A renderer component must override isShowing() and unconditionally return
 * true to work correctly because the Swing paint does nothing for components
 * with isShowing false.  
 */
public class CellRendererPane extends Container {
    
    /**
     * Construct a CellRendererPane object.
     */
    public CellRendererPane() {
        super();
        setLayout(null);
        setVisible(false);
    }

    /** 
     * Overridden to avoid propagating a invalidate up the tree when the
     * cell renderer child is configured.
     */
    public void invalidate() {
    }

    /** 
     * Shouldn't be called.
     */
    public void paint(Graphics g) {
    }
  
    /**
     * Shouldn't be called.
     */
    public void update(Graphics g) {
    }

    /** 
     * If the specified component is already a child of this then we don't
     * bother doing anything - stacking order doesn't matter for cell
     * renderer components (CellRendererPane doesn't paint anyway).<
     */
    protected void addImpl(Component x, Object constraints) {
        if (x.getParent() == this) {
            return;
        }
        
        super.addImpl(x, constraints);
    }


    /** 
     * Paint a cell renderer component c on graphics object g.  Before the component
     * is drawn it's reparented to this (if that's necessary), it's bounds 
     * are set to w,h and the graphics object is (effectively) translated to x,y.  
     * If it's a JComponent, double buffering is temporarily turned off. After 
     * the component is painted it's bounds are reset to -w, -h, 0, 0 so that, if 
     * it's the last renderer component painted, it will not start consuming input.  
     * The Container p is the component we're actually drawing on, typically it's 
     * equal to this.getParent(). If shouldValidate is true the component c will be 
     * validated before painted.
     */
    public void paintComponent(Graphics g, Component c, Container p, 
            int x, int y, int w, int h, boolean shouldValidate) {
    
        if (c == null) {
            if (p != null) {
            ColorPair oldColor = g.getColor();
            g.setColor(p.getColor());
            g.fillRect(x, y, w, h);
            g.setColor(oldColor);
            }
            return;
        }
    
        if (c.getParent() != this) {
            this.add(c);
        }
    
        c.setBounds(x, y, w, h);
    
        if (shouldValidate) {
            c.validate();
        }
    
        Graphics cg = g.create(x, y, w, h);
        try {
            c.paint(cg);
        }
        finally {
            cg.dispose();
        }
    
        c.setBounds(-w, -h, 0, 0);
    }


    /**
     * Calls this.paintComponent(g, c, p, x, y, w, h, false).
     */
    public void paintComponent(Graphics g, Component c, Container p, 
            int x, int y, int w, int h) {
        
        paintComponent(g, c, p, x, y, w, h, false);
    }


    /**
     * Calls this.paintComponent() with the rectangles x,y,width,height fields.
     */
    public void paintComponent(Graphics g, Component c, Container p, Rectangle r) {
        paintComponent(g, c, p, r.x, r.y, r.width, r.height);
    }
}

