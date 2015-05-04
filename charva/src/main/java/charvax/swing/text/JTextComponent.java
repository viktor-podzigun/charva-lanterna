/* class JTextComponent
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

package charvax.swing.text;

import charva.awt.ColorPair;
import charva.awt.ColorScheme;
import charvax.swing.JComponent;


/**
 * JTextComponent is a much-simplified version of the Swing
 * java.swing.text.JTextComponent class, and is the base class
 * for JTextArea.
 */
public abstract class JTextComponent extends JComponent {
    
    //TODO: Use Document interface
    
    /**
     * Index (from the start of the string) where next character will
     * be inserted.
     */
    protected int           caretPosition;

    protected StringBuffer  document;

    protected boolean       editable = true;

    protected ColorPair     selectedColor;
    protected ColorPair     disabledColor;
    
    
    public JTextComponent() {
    }

    public void setColors(ColorScheme colors) {
        super.setColors(colors);
        
        color         = colors.getColor(ColorScheme.EDIT);
        selectedColor = colors.getColor(ColorScheme.EDIT_SELECTED);
        disabledColor = colors.getColor(ColorScheme.EDIT_DISABLED);
    }
    
    public String getDocument() {
        return document.toString();
    }

    public void setDocument(String document) {
        this.document = new StringBuffer(document);
    }

    public String getText() {
        return document.toString();
    }

    public void setText(String text) {
        document = new StringBuffer(text);
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public void setCaretPosition(int caret) {
        caretPosition = caret;
    }

    /**
     * Returns the boolean flag indicating whether this TextComponent
     * is editable or not.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the boolean that indicates whether this TextComponent should be
     * editable or not.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * Returns a string representation of this <code>JTextComponent</code>.
     * This method is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * <p>
     * Overriding <code>paramString</code> to provide information about the
     * specific new aspects of the JFC components.
     * 
     * @return  a string representation of this <code>JTextComponent</code>
     */
    protected String paramString() {
        String editableString = (editable ? "true" : "false");

        return super.paramString() + ",editable=" + editableString;
    }
}
