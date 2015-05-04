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
 * Contains Graphics constants definition.
 */
public interface GraphicsConstants {

    //
    // Graphical virtual symbols
    //
    // Using Unicode safe area... 
    // Unicode Private Use Area on 0xE000 position
    //
    
    // single box
    public static final char VS_ULCORNER        = 0xe000;
    public static final char VS_LLCORNER        = 0xe001;
    public static final char VS_URCORNER        = 0xe002;
    public static final char VS_LRCORNER        = 0xe003;
    public static final char VS_LTEE            = 0xe004;
    public static final char VS_RTEE            = 0xe005;
    public static final char VS_BTEE            = 0xe006;
    public static final char VS_TTEE            = 0xe007;
    public static final char VS_HLINE           = 0xe008;
    public static final char VS_VLINE           = 0xe009;
    public static final char VS_CROSS           = 0xe00a;

    // double box
    public static final char VS_DBL_ULCORNER    = 0xe00b;
    public static final char VS_DBL_LLCORNER    = 0xe00c;
    public static final char VS_DBL_URCORNER    = 0xe00d;
    public static final char VS_DBL_LRCORNER    = 0xe00e;
    public static final char VS_DBL_LTEE        = 0xe00f;
    public static final char VS_DBL_RTEE        = 0xe010;
    public static final char VS_DBL_BTEE        = 0xe011;
    public static final char VS_DBL_TTEE        = 0xe012;
    public static final char VS_DBL_HLINE       = 0xe013;
    public static final char VS_DBL_VLINE       = 0xe014;
    public static final char VS_DBL_CROSS       = 0xe015;
    
    // single separator corners for double box
    public static final char VS_DBL_LSEP        = 0xe016;
    public static final char VS_DBL_RSEP        = 0xe017;
    public static final char VS_DBL_TSEP        = 0xe018;
    public static final char VS_DBL_BSEP        = 0xe019;
    
    // double separator corners for single box
    public static final char VS_L_DBLSEP        = 0xe01a;
    public static final char VS_R_DBLSEP        = 0xe01b;
    public static final char VS_T_DBLSEP        = 0xe01c;
    public static final char VS_B_DBLSEP        = 0xe01d;
    
    // other graphical symbols
    public static final char VS_ARROW_DOWN      = 0xe01e;
    public static final char VS_ARROW_UP        = 0xe01f;
    public static final char VS_ARROW_RIGHT     = 0xe020;
    public static final char VS_ARROW_LEFT      = 0xe021;
    public static final char VS_DOWN            = 0xe022;
    public static final char VS_UP              = 0xe023;
    public static final char VS_RIGHT           = 0xe024;
    public static final char VS_LEFT            = 0xe025;
    
    public static final char VS_WTBOARD         = 0xe026;
    public static final char VS_CKBOARD         = 0xe027;
    public static final char VS_BOARD           = 0xe028;
    public static final char VS_BLBOARD         = 0xe029;

    public static final char VS_TICK            = 0xe02a;
    public static final char VS_RADIO           = 0xe02b;

}
