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

package charva.awt;


/**
 * The <code>Graphics</code> class is the abstract base class for 
 * all graphics contexts that allow an application to draw onto 
 * components that are realized on various devices, as well as 
 * onto off-screen images.
 * <p>
 * A <code>Graphics</code> object encapsulates state information needed
 * for the basic rendering operations that Java supports.  This
 * state information includes the following properties:
 * <p>
 * <ul>
 * <li>The <code>Component</code> object on which to draw.
 * <li>A translation origin for rendering and clipping coordinates.
 * <li>The current clip.  
 * <li>The current color pair.
 * </ul>
 * <p>
 * All coordinates that appear as arguments to the methods of this
 * <code>Graphics</code> object are considered relative to the 
 * translation origin of this <code>Graphics</code> object prior to 
 * the invocation of the method.
 * <p>
 * All rendering operations modify only char-attribute pair which lie within the
 * area bounded by the current clip, which is specified by a {@link Rectangle} 
 * in user space and is controlled by the program using the 
 * <code>Graphics</code> object.  This <i>user clip</i> 
 * is transformed into device space and combined with the 
 * <i>device clip</i>, which is defined by the visibility of windows and
 * device extents.  The combination of the user clip and device clip 
 * defines the <i>composite clip</i>, which determines the final clipping
 * region.  The user clip cannot be modified by the rendering 
 * system to reflect the resulting composite clip. The user clip can only
 * be changed through the <code>setClip</code> or <code>clipRect</code> 
 * methods.
 * All drawing or writing is done in the current color pair. 
 * 
 * @see     charva.awt.Component
 * @see     charva.awt.Graphics#clipRect(int, int, int, int)
 * @see     charva.awt.Graphics#setColor(charva.awt.ColorPair)
 */
public abstract class Graphics {

    /**
     * Constructs a new <code>Graphics</code> object.  
     * This constructor is the default contructor for a graphics 
     * context. 
     * <p>
     * Since <code>Graphics</code> is an abstract class, applications 
     * cannot call this constructor directly. Graphics contexts are 
     * obtained from other graphics contexts. 
     * 
     * @see charva.awt.Graphics#create()
     */
    protected Graphics() {
    }

    /**
     * Creates a new <code>Graphics</code> object that is 
     * a copy of this <code>Graphics</code> object.
     * 
     * @return  a new graphics context that is a copy of this graphics context
     */
    public abstract Graphics create();

    /**
     * Creates a new <code>Graphics</code> object based on this 
     * <code>Graphics</code> object, but with a new translation and clip area.
     * The new <code>Graphics</code> object has its origin 
     * translated to the specified point (<i>x</i>,&nbsp;<i>y</i>). 
     * Its clip area is determined by the intersection of the original
     * clip area with the specified rectangle.  The arguments are all
     * interpreted in the coordinate system of the original 
     * <code>Graphics</code> object. The new graphics context is 
     * identical to the original, except in two respects: 
     * <p>
     * <ul>
     * <li>
     * The new graphics context is translated by (<i>x</i>,&nbsp;<i>y</i>).  
     * That is to say, the point (<code>0</code>,&nbsp;<code>0</code>) in the 
     * new graphics context is the same as (<i>x</i>,&nbsp;<i>y</i>) in 
     * the original graphics context. 
     * <li>
     * The new graphics context has an additional clipping rectangle, in 
     * addition to whatever (translated) clipping rectangle it inherited 
     * from the original graphics context. The origin of the new clipping 
     * rectangle is at (<code>0</code>,&nbsp;<code>0</code>), and its size  
     * is specified by the <code>width</code> and <code>height</code>
     * arguments.
     * </ul>
     * <p>
     * @param      x   the <i>x</i> coordinate.
     * @param      y   the <i>y</i> coordinate.
     * @param      width   the width of the clipping rectangle.
     * @param      height   the height of the clipping rectangle.
     * @return     a new graphics context.
     * 
     * @see        charva.awt.Graphics#translate
     * @see        charva.awt.Graphics#clipRect
     */
    public Graphics create(int x, int y, int width, int height) {
        Graphics g = create();
        if (g == null) {
            return null;
        }
        g.translate(x, y);
        g.clipRect(0, 0, width, height);
        return g;
    }

    /**
     * Translates the origin of the graphics context to the point
     * (<i>x</i>,&nbsp;<i>y</i>) in the current coordinate system. 
     * Modifies this graphics context so that its new origin corresponds 
     * to the point (<i>x</i>,&nbsp;<i>y</i>) in this graphics context's 
     * original coordinate system.  All coordinates used in subsequent 
     * rendering operations on this graphics context will be relative 
     * to this new origin.
     * 
     * @param  x   the <i>x</i> coordinate.
     * @param  y   the <i>y</i> coordinate.
     */
    public abstract void translate(int x, int y);

    /**
     * Gets this graphics context's current color pair.
     * 
     * @return    this graphics context's current color.
     * 
     * @see       charva.awt.ColorPair
     * @see       charva.awt.Graphics#setColor(ColorPair)
     */
    public abstract ColorPair getColor();

    /**
     * Sets this graphics context's current color pair to the specified 
     * color pair. All subsequent graphics operations using this graphics 
     * context use this specified color pair. 
     * 
     * @param     c   the new rendering color.
     * 
     * @see       charva.awt.ColorPair
     * @see       charva.awt.Graphics#getColor
     */
    public abstract void setColor(ColorPair c);

    /**
     * Returns the bounding rectangle of the current clipping area.
     * This method refers to the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.  
     * If no clip has previously been set, or if the clip has been 
     * cleared using <code>setClip(null)</code>, this method returns
     * <code>null</code>.
     * The coordinates in the rectangle are relative to the coordinate
     * system origin of this graphics context.
     * 
     * @return      the bounding rectangle of the current clipping area,
     *              or <code>null</code> if no clip is set.
     * 
     * @see         charva.awt.Graphics#getClip
     * @see         charva.awt.Graphics#clipRect
     * @see         charva.awt.Graphics#setClip(int, int, int, int)
     * @see         charva.awt.Graphics#setClip(Rectangle)
     */
    public abstract Rectangle getClipBounds();

    /** 
     * Intersects the current clip with the specified rectangle.
     * The resulting clipping area is the intersection of the current
     * clipping area and the specified rectangle.  If there is no 
     * current clipping area, either because the clip has never been 
     * set, or the clip has been cleared using <code>setClip(null)</code>, 
     * the specified rectangle becomes the new clip.
     * This method sets the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.  
     * This method can only be used to make the current clip smaller.
     * To set the current clip larger, use any of the setClip methods.
     * Rendering operations have no effect outside of the clipping area.
     * 
     * @param x       the x coordinate of the rectangle to intersect the clip with
     * @param y       the y coordinate of the rectangle to intersect the clip with
     * @param width   the width of the rectangle to intersect the clip with
     * @param height  the height of the rectangle to intersect the clip with
     * 
     * @see #setClip(int, int, int, int)
     * @see #setClip(Rectangle)
     */
    public abstract void clipRect(int x, int y, int width, int height);

    /**
     * Sets the current clip to the rectangle specified by the given
     * coordinates.  This method sets the user clip, which is 
     * independent of the clipping associated with device bounds
     * and window visibility.  
     * Rendering operations have no effect outside of the clipping area.
     * 
     * @param       x the <i>x</i> coordinate of the new clip rectangle.
     * @param       y the <i>y</i> coordinate of the new clip rectangle.
     * @param       width the width of the new clip rectangle.
     * @param       height the height of the new clip rectangle.
     * 
     * @see         charva.awt.Graphics#clipRect
     * @see         charva.awt.Graphics#setClip(Rectangle)
     * @see         charva.awt.Graphics#getClip
     */
    public abstract void setClip(int x, int y, int width, int height);

    /**
     * Gets the current clipping area.
     * This method returns the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     * If no clip has previously been set, or if the clip has been 
     * cleared using <code>setClip(null)</code>, this method returns 
     * <code>null</code>.
     * 
     * @return      a <code>Rectangle</code> object representing the 
     *              current clipping area, or <code>null</code> if
     *              no clip is set.
     * 
     * @see         charva.awt.Graphics#getClipBounds
     * @see         charva.awt.Graphics#clipRect
     * @see         charva.awt.Graphics#setClip(int, int, int, int)
     * @see         charva.awt.Graphics#setClip(Rectangle)
     */
    public Rectangle getClip() {
        return getClipBounds();
    }

    /**
     * Sets the current clipping area to an arbitrary clip shape.
     * Not all objects that implement the <code>Rectangle</code> 
     * interface can be used to set the clip.  The only 
     * <code>Rectangle</code> objects that are guaranteed to be 
     * supported are <code>Rectangle</code> objects that are
     * obtained via the <code>getClip</code> method and via 
     * <code>Rectangle</code> objects.  This method sets the
     * user clip, which is independent of the clipping associated
     * with device bounds and window visibility.
     * 
     * @param clip the <code>Rectangle</code> to use to set the clip
     * 
     * @see         charva.awt.Graphics#getClip()
     * @see         charva.awt.Graphics#clipRect
     * @see         charva.awt.Graphics#setClip(int, int, int, int)
     */
    public abstract void setClip(Rectangle clip);

    /**
     * Draws a horizontal line, using the current color, starting at the 
     * given point <code>(x1,&nbsp;y1)</code> in this graphics context's 
     * coordinate system and with the given length. 
     * 
     * @param x       the start point's <i>x</i> coordinate
     * @param y       the start point's <i>y</i> coordinate
     * @param length  the line length
     */
    public void drawHLine(int x, int y, int length) {
        drawHLine(x, y, length, '-');
    }

    /**
     * Draws a horizontal line, using the current color, starting at the 
     * given point <code>(x1,&nbsp;y1)</code> in this graphics context's 
     * coordinate system, with the given length and character. 
     * 
     * @param x       the start point's <i>x</i> coordinate
     * @param y       the start point's <i>y</i> coordinate
     * @param length  the line length
     * @param chr     the character to use for drawing the line
     */
    public abstract void drawHLine(int x, int y, int length, char chr);

    /**
     * Draws a vertical line, using the current color, starting at the 
     * given point <code>(x1,&nbsp;y1)</code> in this graphics context's 
     * coordinate system and with the given length. 
     * 
     * @param x       the start point's <i>x</i> coordinate
     * @param y       the start point's <i>y</i> coordinate
     * @param length  the line length
     */
    public void drawVLine(int x, int y, int length) {
        drawVLine(x, y, length, '|');
    }

    /**
     * Draws a vertical line, using the current color, starting at the 
     * given point <code>(x1,&nbsp;y1)</code> in this graphics context's 
     * coordinate system, with the given length and character. 
     * 
     * @param x       the start point's <i>x</i> coordinate
     * @param y       the start point's <i>y</i> coordinate
     * @param length  the line length
     * @param chr     the character to use for drawing the line
     */
    public abstract void drawVLine(int x, int y, int length, char chr);

    /** 
     * Fills the specified rectangle. 
     * The left and right edges of the rectangle are at 
     * <code>x</code> and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>. 
     * The top and bottom edges are at 
     * <code>y</code> and <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>. 
     * The resulting rectangle covers an area 
     * <code>width</code> pixels wide by 
     * <code>height</code> pixels tall.
     * The rectangle is filled using the graphics context's current color. 
     * 
     * @param         x   the <i>x</i> coordinate 
     *                         of the rectangle to be filled.
     * @param         y   the <i>y</i> coordinate 
     *                         of the rectangle to be filled.
     * @param         width   the width of the rectangle to be filled.
     * @param         height   the height of the rectangle to be filled.
     * 
     * @see           charva.awt.Graphics#drawRect
     */
    public abstract void fillRect(int x, int y, int width, int height);

    /** 
     * Draws the outline of the specified double-line rectangle. 
     * The left and right edges of the rectangle are at 
     * <code>x</code> and <code>x&nbsp;+&nbsp;width</code>. 
     * The top and bottom edges are at 
     * <code>y</code> and <code>y&nbsp;+&nbsp;height</code>. 
     * The rectangle is drawn using the graphics context's current color.
     * 
     * @param         x   the <i>x</i> coordinate 
     *                         of the rectangle to be drawn.
     * @param         y   the <i>y</i> coordinate 
     *                         of the rectangle to be drawn.
     * @param         width   the width of the rectangle to be drawn.
     * @param         height   the height of the rectangle to be drawn.
     * 
     * @see          charva.awt.Graphics#drawRect
     */
    public abstract void drawDoubleRect(int x, int y, int width, int height);
        
    /** 
     * Draws the outline of the specified rectangle. 
     * The left and right edges of the rectangle are at 
     * <code>x</code> and <code>x&nbsp;+&nbsp;width</code>. 
     * The top and bottom edges are at 
     * <code>y</code> and <code>y&nbsp;+&nbsp;height</code>. 
     * The rectangle is drawn using the graphics context's current color.
     * 
     * @param         x   the <i>x</i> coordinate 
     *                         of the rectangle to be drawn.
     * @param         y   the <i>y</i> coordinate 
     *                         of the rectangle to be drawn.
     * @param         width   the width of the rectangle to be drawn.
     * @param         height   the height of the rectangle to be drawn.
     * 
     * @see          charva.awt.Graphics#fillRect
     */
    public void drawRect(int x, int y, int width, int height) {
        if (width < 0 || height < 0) {
            return;
        }
        
        if (height == 0) {
            drawHLine(x, y, width);
        } else if (width == 0) {
            drawVLine(x, y, height);
        } else {
            // draw top line
            drawChar('+',    x,             y);
            drawHLine(x + 1, y,             width - 2);
            drawChar('+',    x + width - 1, y);
            
            // draw bottom line
            drawChar('+',    x,             y + height);
            drawHLine(x + 1, y + height,    width - 2);
            drawChar('+',    x + width - 1, y + height);
            
            // draw left and right lines
            drawVLine(x,         y + 1, height - 2);
            drawVLine(x + width, y + 1, height - 2);
        }
    }
    
    /** 
     * Draws the text given by the specified string, using this 
     * graphics context's current color. The baseline of the 
     * leftmost character is at position (<i>x</i>,&nbsp;<i>y</i>) in this 
     * graphics context's coordinate system. 
     * 
     * @param str  the string to be drawn
     * @param x    the <i>x</i> coordinate
     * @param y    the <i>y</i> coordinate
     * 
     * @see charva.awt.Graphics#drawChars
     */
    public abstract void drawString(String str, int x, int y);

    /**
     * Draws the char given by the specified character, using this 
     * graphics context's current color and position (<i>x</i>,&nbsp;<i>y</i>) 
     * in this graphics context's coordinate system. 
     * 
     * @param chr  the character to use for drawing the line
     * @param x    the <i>x</i> coordinate
     * @param y    the <i>y</i> coordinate
     * 
     * @see charva.awt.Graphics#drawString
     */
    public abstract void drawChar(char chr, int x, int y);

    /**
     * Draws the text given by the specified character array, using this 
     * graphics context's current color. The baseline of the 
     * first character is at position (<i>x</i>,&nbsp;<i>y</i>) in this 
     * graphics context's coordinate system. 
     * 
     * @param data the array of characters to be drawn
     * @param offset the start offset in the data
     * @param length the number of characters to be drawn
     * @param x the <i>x</i> coordinate of the baseline of the text
     * @param y the <i>y</i> coordinate of the baseline of the text
     * 
     * @see         charva.awt.Graphics#drawString
     */
    public void drawChars(char data[], int offset, int length, int x, int y) {
        drawString(new String(data, offset, length), x, y);
    }

    /**
     * Disposes of this graphics context and releases 
     * any system resources that it is using. 
     * A <code>Graphics</code> object cannot be used after 
     * <code>dispose</code>has been called.
     * <p>
     * When a Java program runs, a large number of <code>Graphics</code>
     * objects can be created within a short time frame.
     * Although the finalization process of the garbage collector 
     * also disposes of the same system resources, it is preferable 
     * to manually free the associated resources by calling this
     * method rather than to rely on a finalization process which 
     * may not run to completion for a long period of time.
     * <p>
     * Graphics objects which are provided as arguments to the 
     * <code>paint</code> and <code>update</code> methods 
     * of components are automatically released by the system when 
     * those methods return. For efficiency, programmers should
     * call <code>dispose</code> when finished using
     * a <code>Graphics</code> object only if it was created 
     * directly from a component or another <code>Graphics</code> object.
     * 
     * @see         charva.awt.Component#paint
     * @see         charva.awt.Graphics#create
     */
    public void dispose() {
    }

    /**
     * Returns a <code>String</code> object representing this 
     * <code>Graphics</code> object's value.
     * 
     * @return  a string representation of this graphics context
     */
    public String toString() {
        return getClass().getName() + "[color=" + getColor() + "]";
    }

    /**
     * Returns true if the specified rectangular area might intersect 
     * the current clipping area.
     * The coordinates of the specified rectangular area are in the
     * user coordinate space and are relative to the coordinate
     * system origin of this graphics context.
     * This method may use an algorithm that calculates a result quickly
     * but which sometimes might return true even if the specified
     * rectangular area does not intersect the clipping area.
     * The specific algorithm employed may thus trade off accuracy for
     * speed, but it will never return false unless it can guarantee
     * that the specified rectangular area does not intersect the
     * current clipping area.
     * The clipping area used by this method can represent the
     * intersection of the user clip as specified through the clip
     * methods of this graphics context as well as the clipping
     * associated with the device or image bounds and window visibility.
     *
     * @param x       the x coordinate of the rectangle to test against the clip
     * @param y       the y coordinate of the rectangle to test against the clip
     * @param width   the width of the rectangle to test against the clip
     * @param height  the height of the rectangle to test against the clip
     * @return <code>true</code> if the specified rectangle intersects
     *         the bounds of the current clip; <code>false</code>
     *         otherwise.
     */
    public boolean hitClip(int x, int y, int width, int height) {
        // Note, this implementation is not very efficient.
        // Subclasses should override this method and calculate
        // the results more directly.
        Rectangle clipRect = getClipBounds();
        if (clipRect == null) {
            return true;
        }
        return clipRect.intersects(x, y, width, height);
    }

    /**
     * Returns the bounding rectangle of the current clipping area.
     * The coordinates in the rectangle are relative to the coordinate
     * system origin of this graphics context.  This method differs
     * from {@link #getClipBounds() getClipBounds} in that an existing 
     * rectangle is used instead of allocating a new one.  
     * This method refers to the user clip, which is independent of the
     * clipping associated with device bounds and window visibility.
     *  If no clip has previously been set, or if the clip has been 
     * cleared using <code>setClip(null)</code>, this method returns the 
     * specified <code>Rectangle</code>.
     * @param  r    the rectangle where the current clipping area is
     *              copied to.  Any current values in this rectangle are
     *              overwritten.
     * @return      the bounding rectangle of the current clipping area.
     */
    public Rectangle getClipBounds(Rectangle r) {
        if (r == null) {
            throw new IllegalArgumentException("r == null");
        }
        
        // Note, this implementation is not very efficient.
        // Subclasses should override this method and avoid
        // the allocation overhead of getClipBounds().
        Rectangle clipRect = getClipBounds();
        if (clipRect != null) {
            r.x      = clipRect.x;
            r.y      = clipRect.y;
            r.width  = clipRect.width;
            r.height = clipRect.height;
        }
        return r;
    }
}
