/* class JScrollBar
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

import charva.awt.ColorScheme;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.GraphicsConstants;
import charva.awt.Toolkit;


/**
 * An implementation of a scrollbar. The user positions the knob in the
 * scrollbar to determine the contents of the viewing area.
 */
public class JScrollBar extends JComponent {

    /**
     * Indicates that the <code>JScrollBar</code> has horizontal orientation
     */
    public static final int HORIZONTAL = 0; 

    /**
     * Indicates that the <code>JScrollBar</code> has vertical orientation
     */
    public static final int VERTICAL = 1;    

    
    /**
     * The model that represents the scrollbar's minimum, maximum, extent
     * (aka "visibleAmount") and current value.
     * 
     * @see #setModel
     */
    protected BoundedRangeModel model;


    /**
     * @see #setOrientation
     */
    protected int   orientation;


    /**
     * @see #setUnitIncrement
     */
    protected int   unitIncrement;


    /**
     * @see #setBlockIncrement
     */
    protected int   blockIncrement;

    
    private static void checkOrientation(int orientation) {
        switch (orientation) {
        case VERTICAL:
        case HORIZONTAL:
            break;
        
        default:
            throw new IllegalArgumentException(
                    "orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

   
    /**
     * Creates a vertical scrollbar with the following initial values:
     * <pre>
     * minimum = 0 
     * maximum = 100 
     * value = 0
     * extent = 10
     * </pre>
     */
    public JScrollBar() {
        this(VERTICAL);
    }

    /**
     * Creates a scrollbar with the specified orientation
     * and the following initial values:
     * <pre>
     * minimum = 0 
     * maximum = 100 
     * value = 0
     * extent = 10
     * </pre>
     */
    public JScrollBar(int orientation) {
        this(orientation, 0, 10, 0, 100);
    }

    /**
     * Creates a scrollbar with the specified orientation,
     * value, extent, minimum, and maximum.
     * The "extent" is the size of the viewable area. It is also known
     * as the "visible amount". 
     * <p>
     * Note: Use <code>setBlockIncrement</code> to set the block 
     * increment to a size slightly smaller than the view's extent.
     * That way, when the user jumps the knob to an adjacent position,
     * one or two lines of the original contents remain in view.
     * 
     * @exception IllegalArgumentException
     *                 if orientation is not one of VERTICAL, HORIZONTAL
     * 
     * @see #setOrientation
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public JScrollBar(int orientation, int value, int extent, int min, int max)   
    {
        checkOrientation(orientation);
        this.unitIncrement = 1;
        this.blockIncrement = (extent == 0) ? 1 : extent;
        this.orientation = orientation;
        this.model = new DefaultBoundedRangeModel(value, extent, min, max);
        
        color = Toolkit.getDefaultColor();
    }

    /**
     * Returns the component's orientation (horizontal or vertical). 
     *                     
     * @return VERTICAL or HORIZONTAL
     * @see #setOrientation
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Set the scrollbar's orientation to either VERTICAL or
     * HORIZONTAL.
     * 
     * @exception IllegalArgumentException 
     *              if orientation is not one of VERTICAL, HORIZONTAL
     * 
     * @see #getOrientation
     */
    public void setOrientation(int orientation) {
        checkOrientation(orientation);
        
        int oldValue = this.orientation;
        this.orientation = orientation;
    
        if (orientation != oldValue) {
            invalidate();
        }
    }
    
    /**
     * Returns data model that handles the scrollbar's four
     * fundamental properties: minimum, maximum, value, extent.
     * 
     * @see #setModel
     */
    public BoundedRangeModel getModel() { 
        return model; 
    }

    /**
     * Sets the model that handles the scrollbar's four
     * fundamental properties: minimum, maximum, value, extent.
     * 
     * @see #getModel
     */
    public void setModel(BoundedRangeModel newModel) {
        model = newModel;
    }

    /**
     * Returns the amount to change the scrollbar's value by,
     * given a unit up/down request.  A ScrollBarUI implementation
     * typically calls this method when the user clicks on a scrollbar 
     * up/down arrow and uses the result to update the scrollbar's
     * value.   Subclasses my override this method to compute
     * a value, e.g. the change required to scroll up or down one
     * (variable height) line text or one row in a table.
     * <p>
     * The JScrollPane component creates scrollbars (by default) 
     * that override this method and delegate to the viewports
     * Scrollable view, if it has one.  The Scrollable interface
     * provides a more specialized version of this method.
     * 
     * @param direction is -1 or 1 for up/down respectively
     * @return the value of the unitIncrement property
     * @see #setUnitIncrement
     * @see #setValue
     * @see Scrollable#getScrollableUnitIncrement
     */
    public int getUnitIncrement(int direction) { 
        return unitIncrement; 
    }

    /**
     * Sets the unitIncrement property.
     * 
     * @see #getUnitIncrement
     */
    public void setUnitIncrement(int unitIncrement) { 
        int oldValue = this.unitIncrement;
        this.unitIncrement = unitIncrement;
        
        if (oldValue != unitIncrement) {
            repaint();
        }
    }

    /**
     * Returns the amount to change the scrollbar's value by,
     * given a block (usually "page") up/down request.  A ScrollBarUI 
     * implementation typically calls this method when the user clicks 
     * above or below the scrollbar "knob" to change the value
     * up or down by large amount.  Subclasses my override this 
     * method to compute a value, e.g. the change required to scroll 
     * up or down one paragraph in a text document.
     * <p>
     * The JScrollPane component creates scrollbars (by default) 
     * that override this method and delegate to the viewports
     * Scrollable view, if it has one.  The Scrollable interface
     * provides a more specialized version of this method.
     * 
     * @param direction is -1 or 1 for up/down respectively
     * @return the value of the blockIncrement property
     * 
     * @see #setBlockIncrement
     * @see #setValue
     * @see Scrollable#getScrollableBlockIncrement
     */
    public int getBlockIncrement(int direction) { 
        return blockIncrement; 
    }


    /**
     * Sets the blockIncrement property.
     * 
     * @see #getBlockIncrement(int)
     */
    public void setBlockIncrement(int blockIncrement) { 
        int oldValue = this.blockIncrement;
        this.blockIncrement = blockIncrement;
        
        if (oldValue != blockIncrement)
            repaint();
    }

    /**
     * Returns the scrollbar's value.
     * 
     * @return the model's value property
     * @see #setValue
     */
    public int getValue() { 
        return getModel().getValue(); 
    }

    /**
     * Sets the scrollbar's value.  This method just forwards the value
     * to the model.
     *
     * @see #getValue
     * @see BoundedRangeModel#setValue
     */
    public void setValue(int value) {
        getModel().setValue(value);
    }

    /**
     * Returns the scrollbar's extent, aka its "visibleAmount".  In many 
     * scrollbar look and feel implementations the size of the 
     * scrollbar "knob" or "thumb" is proportional to the extent.
     * 
     * @return the value of the model's extent property
     * @see #setVisibleAmount
     */
    public int getVisibleAmount() { 
        return getModel().getExtent(); 
    }

    /**
     * Set the model's extent property.
     * 
     * @see #getVisibleAmount
     * @see BoundedRangeModel#setExtent
     */
    public void setVisibleAmount(int extent) { 
        getModel().setExtent(extent); 
    }

    /**
     * Returns the minimum value supported by the scrollbar 
     * (usually zero).
     *
     * @return the value of the model's minimum property
     * @see #setMinimum
     */
    public int getMinimum() { 
        return getModel().getMinimum(); 
    }

    /**
     * Sets the model's minimum property.
     *
     * @see #getMinimum
     * @see BoundedRangeModel#setMinimum
     */
    public void setMinimum(int minimum) { 
        getModel().setMinimum(minimum); 
    }

    /**
     * The maximum value of the scrollbar is maximum - extent.
     *
     * @return the value of the model's maximum property
     * @see #setMaximum
     */
    public int getMaximum() { 
        return getModel().getMaximum(); 
    }

    /**
     * Sets the model's maximum property.  Note that the scrollbar's value
     * can only be set to maximum - extent.
     * 
     * @see #getMaximum
     * @see BoundedRangeModel#setMaximum
     */
    public void setMaximum(int maximum) { 
        getModel().setMaximum(maximum); 
    }

    /**
     * True if the scrollbar knob is being dragged.
     * 
     * @return the value of the model's valueIsAdjusting property
     * @see #setValueIsAdjusting
     */
    public boolean getValueIsAdjusting() { 
        return getModel().getValueIsAdjusting(); 
    }

    /**
     * Sets the model's valueIsAdjusting property.  Scrollbar look and
     * feel implementations should set this property to true when 
     * a knob drag begins, and to false when the drag ends.  The
     * scrollbar model will not generate ChangeEvents while
     * valueIsAdjusting is true.
     * 
     * @see #getValueIsAdjusting
     * @see BoundedRangeModel#setValueIsAdjusting
     */
    public void setValueIsAdjusting(boolean b) { 
        getModel().setValueIsAdjusting(b);
    }

    /**
     * Sets the four BoundedRangeModel properties after forcing
     * the arguments to obey the usual constraints:
     * <pre>
     * minimum <= value <= value+extent <= maximum
     * </pre>
     * <p>
     * 
     * @see BoundedRangeModel#setRangeProperties
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     */
    public void setValues(int newValue, int newExtent, int newMin, int newMax) {
        BoundedRangeModel m = getModel();
        m.setRangeProperties(newValue, newExtent, newMin, newMax, 
                m.getValueIsAdjusting());
    }

    public Dimension getMinimumSize() {
        return getSize();
    }

    /**
     * Returns the screen size of the scrollbar
     */
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public int getWidth() {
        if (orientation == VERTICAL) {
            return 1;
        }
        
        return size.width;
    }

    public int getHeight() {
        if (orientation == HORIZONTAL) {
            return 1;
        }
        
        return size.height;
    }
    
    public boolean isFocusTraversable() {
        return false;
    }
    
    public void setColors(ColorScheme colors) {
        // do not change the color
        //super.setColors(colors);
    }

    public void paint(Graphics g) {
        int length;
        if (orientation == VERTICAL) {
            length = getHeight();
        } else {
            length = getWidth();
        }
        
        // don't count buttons
        length -= 2;
        
        BoundedRangeModel m = getModel();
        int value   = m.getValue();
        int extent  = m.getExtent();
        int maximum = m.getMaximum();
        
        float relation = (float)length / maximum;
        int offset  = (int)(value * relation);
        int visible = (int)(extent * relation);
        
        if (visible == 0) {
            visible = 1;
        } else if (visible >= length) {
            visible = length - 1;
        }
        
        if (offset == 0 && value > 0) {
            offset = 1;
        } else if (offset > (length - visible) || (value + extent) >= maximum) {
            offset = length - visible;
        }

//        if (orientation == HORIZONTAL) {
//        //if (orientation == VERTICAL) {
//            System.out.println("HORIZONTAL:\n" 
//                    + "\tlength:   " + length
//                    + "\tmaximum:  " + maximum
//                    + "\trelation: " + relation
//                    + "\tvalue:    " + value
//                    + "\textent:   " + extent
//                    + "\toffset:   " + offset
//                    + "\tvisible:  " + visible);
//        }
        
        g.setColor(getColor());
        
        if (orientation == VERTICAL) {
            g.drawChar(GraphicsConstants.VS_UP, 0, 0);
            for (int k = 1; k < length + 1; k++) {
                g.drawChar(GraphicsConstants.VS_CKBOARD, 0, k);
            }
            
            g.drawChar(GraphicsConstants.VS_DOWN, 0, length + 1);

            for (int i = 0; i < visible; i++) {
                g.drawChar(GraphicsConstants.VS_BLBOARD, 0, 1 + offset + i);
            }
        } else {
            g.drawChar(GraphicsConstants.VS_LEFT, 0, 0);
            for (int k = 1; k < length + 1; k++) {
                g.drawChar(GraphicsConstants.VS_CKBOARD, k, 0);
            }
            
            g.drawChar(GraphicsConstants.VS_RIGHT, length + 1, 0);

            for (int i = 0; i < visible; i++) {
                g.drawChar(GraphicsConstants.VS_BLBOARD, 1 + offset + i, 0);
            }
        }
    }
    
    /**
     * Returns a string representation of this JScrollBar. This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this JScrollBar
     */
    protected String paramString() {
        String orientationString = (orientation == HORIZONTAL ?
                "HORIZONTAL" : "VERTICAL");

        return super.paramString() +
                ",blockIncrement=" + blockIncrement +
                ",orientation=" + orientationString +
                ",unitIncrement=" + unitIncrement;
    }
}
