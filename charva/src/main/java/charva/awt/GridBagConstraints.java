/* class GridBagConstraints
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

package charva.awt;


/**
 * This class defines constraints used for laying out components in the
 * GridBagLayout layout manager
 */
public class GridBagConstraints {
    
    public static final int CENTER      = 100;
    public static final int NORTH       = 101;
    public static final int NORTHEAST   = 102;
    public static final int EAST        = 103;
    public static final int SOUTHEAST   = 104;
    public static final int SOUTH       = 105;
    public static final int SOUTHWEST   = 106;
    public static final int WEST        = 107;
    public static final int NORTHWEST   = 108;

    public static final int NONE        = 200;
    public static final int HORIZONTAL  = 201;
    public static final int VERTICAL    = 202;
    public static final int BOTH        = 203;
    
    
    public int      gridx;
    public int      gridy;
    public int      gridwidth   = 1;
    public int      gridheight  = 1;
    public double   weightx     = 0.0;
    public double   weighty     = 0.0;
    public int      anchor      = CENTER;
    public int      fill        = NONE; // not used
    public Insets   insets      = new Insets(0, 0, 0, 0);
    
    public int      ipadx; // not used
    public int      ipady; // not used

    
    /**
     * Creates a GridBagConstraints object with all of its fields set to their
     * default value
     */
    public GridBagConstraints() {
    }

    public GridBagConstraints(int gridx, int gridy, int gridwidth,
            int gridheight, double weightx, double weighty, int anchor,
            int fill, Insets insets, int ipadx, int ipady) {
        
        this.gridx      = gridx;
        this.gridy      = gridy;
        this.gridwidth  = gridwidth;
        this.gridheight = gridheight;
        this.weightx    = weightx;
        this.weighty    = weighty;
        this.anchor     = anchor;
        this.fill       = fill;
        this.insets     = insets;
        this.ipadx      = ipadx;
        this.ipady      = ipady;
    }

    public String toString() {
        return "gridx=" + gridx 
                + ", gridy=" + gridy 
                + ", gridwidth=" + gridwidth
                + ", gridheight=" + gridheight;
    }
}
