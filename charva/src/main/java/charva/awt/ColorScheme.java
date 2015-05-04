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

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


/**
 * Stores color scheme - color settings for all components
 */
public final class ColorScheme {

    public static final String DIALOG           = "charvax.swing.JDialog";
    public static final String DIALOG_TITLE     = "charvax.swing.JDialog.title";
    public static final String DIALOG_BORDER    = "charvax.swing.JDialog.border";
    
    public static final String BUTTON           = "charvax.swing.JButton";
    public static final String BUTTON_SELECTED  = "charvax.swing.JButton.selected";
    public static final String BUTTON_MNEMONIC  = "charvax.swing.JButton.mnemonic";
    public static final String BUTTON_DISABLED  = "charvax.swing.JButton.disabled";
    
    public static final String LIST             = "charvax.swing.JList";
    public static final String LIST_SELECTED    = "charvax.swing.JList.selected";
    public static final String LIST_HIGHLIGHTED = "charvax.swing.JList.highlighted";
    public static final String LIST_DISABLED    = "charvax.swing.JList.disabled";
    
    public static final String EDIT             = "charvax.swing.JTextField";
    public static final String EDIT_SELECTED    = "charvax.swing.JTextField.selected";
    public static final String EDIT_DISABLED    = "charvax.swing.JTextField.disabled";
    
    public static final String COMBO            = "charvax.swing.JComboBox";
    public static final String COMBO_SELECTED   = "charvax.swing.JComboBox.selected";
    
    public static final String MENU             = "charvax.swing.JMenu";
    public static final String MENU_TITLE       = "charvax.swing.JMenu.title";
    public static final String MENU_BORDER      = "charvax.swing.JMenu.border";
    public static final String MENU_SELECTED    = "charvax.swing.JMenu.selected";
    public static final String MENU_MNEMONIC    = "charvax.swing.JMenu.mnemonic";
    public static final String MENU_DISABLED    = "charvax.swing.JMenu.disabled";
    
    
    private final Properties    colors;
    
    
    public ColorScheme(Properties newColors) {
        this(newColors, null);
    }
    
    public ColorScheme(Properties newColors, ColorScheme defaults) {
        if (newColors == null)
            throw new IllegalArgumentException("newColors == null");

        this.colors = new Properties();
        if (defaults != null)
            setColors(this.colors, defaults.colors);
        
        setColors(this.colors, newColors);
    }
    
    private static void setColors(Properties colors, Properties newColors) {
        Iterator iter = newColors.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            String    id    = (String)entry.getKey();
            Object    color = entry.getValue();
            
            if (color instanceof String) {
                String colorStr = (String)color;
                int    radix    = 10;
                if (colorStr.startsWith("0x")) {
                    colorStr = colorStr.substring(2);
                    radix    = 16;
                }
                
                colors.put(id, ColorPair.valueOf(
                        Integer.parseInt(colorStr, radix)));
            
            } else if (color instanceof ColorPair) {
                colors.put(id, (ColorPair)color);
                
            } else {
                throw new IllegalArgumentException(
                        "Bad color value type: " + (color != null ? 
                                color.getClass().toString() : "null"));
            }
        }
    }
    
    /**
     * Returns registered color-pair for the specified class and 
     * property name
     * 
     * @param clazz     class object for which color is retrieved
     * @param property  property name (optional, may be <tt>null</tt>)
     * @return color-pair for the specified class and property name or default 
     *         color-pair if no such identifier was registered
     */
    public ColorPair getColor(Class clazz, String property) {
        if (clazz == null)
            throw new IllegalArgumentException("clazz == null");
        
        String id = clazz.getName();
        if (property != null && property.length() > 0)
            id += "." + property;
        
        return getColor(id);
    }

    /**
     * Returns registered color-pair for the specified identifier
     * 
     * @param id  color-pair identifier
     * @return color-pair for the specified identifier or default color-pair 
     *         if no such identifier was registered
     */
    public ColorPair getColor(String id) {
        ColorPair color = (ColorPair)colors.get(id);
        if (color == null)
            return Toolkit.getDefaultColor();
        
        return color;
    }
}
