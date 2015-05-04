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
 * Default implementation of Graphics object.
 */
public class TerminalGraphics extends Graphics implements GraphicsConstants {

    protected final TerminalWindow  peer;
    
    protected int                   offsetX;
    protected int                   offsetY;
    
    /** The current color               */
    protected ColorPair             colorPair = Toolkit.getDefaultColor();
    
    /** The current clipping rectangle  */
    protected Rectangle             clipRect;
    
    
    public TerminalGraphics(TerminalWindow peer) {
        this(peer, 0, 0);
    }
    
    protected TerminalGraphics(TerminalWindow peer, int offsetX, int offsetY) {
        this.peer    = peer;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    public Graphics create() {
        TerminalGraphics g = new TerminalGraphics(peer, offsetX, offsetY);
        if (clipRect != null) {
            g.setClip(clipRect);
        }
        
        return g;
    }

    public ColorPair getColor() {
        return colorPair;
    }

    public void setColor(ColorPair colorPair) {
        if (colorPair == null) {
            throw new NullPointerException("colorPair");
        }
        
        this.colorPair = colorPair;
    }

    public void translate(int x, int y) {
        this.offsetX += x;
        this.offsetY += y;
    }
    
    public boolean hitClip(int x, int y, int width, int height) {
        if (clipRect == null) {
            return true;
        }
        
        return clipRect.intersects(x, y, width, height);
    }
    
    public Rectangle getClipBounds(Rectangle clip) {
        if (clip == null) {
            throw new NullPointerException("clip");
        }
        
        Rectangle clipRect = this.clipRect;
        if (clipRect != null) {
            clip.x      = clipRect.x;
            clip.y      = clipRect.y;
            clip.width  = clipRect.width;
            clip.height = clipRect.height;
        }
        
        return clip;
    }
    
    public Rectangle getClipBounds() {
        if (clipRect == null) {
            return null;
        }
        
        return new Rectangle(clipRect);
    }

    public void clipRect(int x, int y, int width, int height) {
        if (clipRect == null) {
            clipRect = new Rectangle(x, y, width, height);
            return;
        }
        
        intersectClip(clipRect, x, y, width, height);
    }

    /**
     * Helper method for computing intersection of clipping area
     *
     * @see Rectangle#intersection(Rectangle)
     */
    protected static void intersectClip(Rectangle clip, int x, int y, 
            int width, int height) {
        
        int  tx1 = clip.x;
        int  ty1 = clip.y;
        int  rx1 = x;
        int  ry1 = y;
        long tx2 = tx1;
        tx2 += clip.width;
        
        long ty2 = ty1;
        ty2 += clip.height;
        long rx2 = rx1;
        rx2 += width;
        long ry2 = ry1;
        ry2 += height;
        
        if (tx1 < rx1) {
            tx1 = rx1;
        }
        if (ty1 < ry1) {
            ty1 = ry1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        
        tx2 -= tx1;
        ty2 -= ty1;
        
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE) {
            tx2 = Integer.MIN_VALUE;
        }
        if (ty2 < Integer.MIN_VALUE) {
            ty2 = Integer.MIN_VALUE;
        }
        
        clip.setBounds(tx1, ty1, (int) tx2, (int) ty2);
    }
    
    public void setClip(int x, int y, int width, int height) {
        if (clipRect == null) {
            clipRect = new Rectangle();
        }
        
        clipRect.setBounds(x, y, width, height);
    }

    public void setClip(Rectangle clip) {
        setClip(clip.x, clip.y, clip.width, clip.height);
    }

    public void drawHLine(int x, int y, int length) {
        drawHLine(x, y, length, VS_HLINE);
    }

    public void drawHLine(int x, int y, int length, char chr) {
        if (clipRect != null) {
            drawLine(new Rectangle(), x, y, length, chr, true);
        }
    }

    public void drawVLine(int x, int y, int length) {
        drawVLine(x, y, length, VS_VLINE);
    }

    public void drawVLine(int x, int y, int length, char chr) {
        if (clipRect != null) {
            drawLine(new Rectangle(), x, y, length, chr, false);
        }
    }

    public void drawRect(int x, int y, int width, int height) {
        if (clipRect != null && width > 0 && height > 0) {
            drawRect(x, y, width, height, false);
        }
    }
    
    public void drawDoubleRect(int x, int y, int width, int height) {
        if (clipRect != null && width > 0 && height > 0) {
            drawRect(x, y, width, height, true);
        }
    }
    
    public void drawChar(char chr, int x, int y) {
        if (clipRect != null && clipRect.contains(x, y)) {
            peer.drawChar(offsetX + x, offsetY + y, chr, 
                    colorPair.getColorCode());
        }
    }
    
    public void drawString(String str, int x, int y) {
        int length;
        if (clipRect != null && str != null && (length = str.length()) > 0) {
            // drawing within clipping rectangle
            Rectangle clip = new Rectangle(clipRect);
            intersectClip(clip, x, y, length, 1);
            if (!clip.isEmpty()) {
                peer.drawString(offsetX + clip.x, offsetY + clip.y, 
                        str.substring(clip.x - x, clip.x - x + clip.width), 
                        colorPair.getColorCode());
            }
        }
    }

    public void fillRect(int x, int y, int width, int height) {
        if (clipRect != null && width > 0 && height > 0) {
            // drawing within clipping rectangle
            Rectangle clip = new Rectangle(clipRect);
            intersectClip(clip, x, y, width, height);
            if (!clip.isEmpty()) {
                peer.fillBox(offsetX + clip.x, offsetY + clip.y, 
                        clip.width, clip.height, colorPair.getColorCode());
            }
        }
    }
    
    private void drawLine(Rectangle clip, int x, int y, int length, char chr, 
            boolean isHorizontal) {
        
        // drawing within clipping rectangle
        clip.setBounds(clipRect);
        
        if (isHorizontal) {
            intersectClip(clip, x, y, length, 1);
            if (!clip.isEmpty()) {
                peer.drawLine(offsetX + clip.x, offsetY + clip.y, 
                        clip.width, chr, colorPair.getColorCode(), true);
            }
        } else {
            intersectClip(clip, x, y, 1, length);
            if (!clip.isEmpty()) {
                peer.drawLine(offsetX + clip.x, offsetY + clip.y, 
                        clip.height, chr, colorPair.getColorCode(), false);
            }
        }
    }

    private void drawRect(int x, int y, int width, int height, 
            boolean isDouble) {
        
        // drawing within clipping rectangle
        Rectangle clip = new Rectangle();
        
        int x2 = x + width  - 1;
        int y2 = y + height - 1;
        
        // draw the top of the box
        drawChar(isDouble ? VS_DBL_ULCORNER : VS_ULCORNER, x, y);
        drawLine(clip, x + 1, y, width - 2, 
                isDouble ? VS_DBL_HLINE : VS_HLINE, true);
        drawChar(isDouble ? VS_DBL_URCORNER : VS_URCORNER, x2, y);

        // draw the bottom of the box
        drawChar(isDouble ? VS_DBL_LLCORNER : VS_LLCORNER, x, y2);
        drawLine(clip, x + 1, y2, width - 2, 
                isDouble ? VS_DBL_HLINE : VS_HLINE, true);
        drawChar(isDouble ? VS_DBL_LRCORNER : VS_LRCORNER, x2, y2);

        // draw the left and right sides of the box
        drawLine(clip,  x, y + 1, height - 2, 
                isDouble ? VS_DBL_VLINE : VS_VLINE, false);
        drawLine(clip, x2, y + 1, height - 2, 
                isDouble ? VS_DBL_VLINE : VS_VLINE, false);
    }
}
