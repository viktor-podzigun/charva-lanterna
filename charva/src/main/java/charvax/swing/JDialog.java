/* class JDialog
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

import charva.awt.ColorPair;
import charva.awt.ColorScheme;
import charva.awt.Container;
import charva.awt.EventQueue;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Window;
import charva.awt.event.KeyEvent;
import charva.awt.event.WindowEvent;
import charvax.swing.border.AbstractBorder;
import charvax.swing.border.Border;
import charvax.swing.border.DoubleLineBorder;
import charvax.swing.border.TitledBorder;


/**
 * JDialog is a top-level window with border and title
 */
public class JDialog extends Window implements WindowConstants {

    private int         defaultCloseOperation = HIDE_ON_CLOSE;
    
    private String      title;
    private Border      border;
    
    
    /**
     * Creates a modal dialog with empty title.
     */
    public JDialog() {
        this(null, "");
    }

    /**
     * Creates a modal dialog with the specified title.
     */
    public JDialog(String title) {
        this(null, title);
    }

    /**
     * Creates a modal dialog for the specified owner window and empty title.
     */
    public JDialog(Window owner) {
        this(owner, "");
    }

    /**
     * Creates a modal dialog for the specified owner window and title
     */
    public JDialog(Window owner, String title) {
        super(owner);
        
        this.title  = title;
        this.border = new TitledBorder(
                new DoubleLineBorder(
                        currentColors.getColor(ColorScheme.DIALOG_BORDER), 
                        new Insets(2, 4, 2, 4)), 
                title, TitledBorder.CENTER, TitledBorder.TOP, 
                currentColors.getColor(ColorScheme.DIALOG_TITLE));
    }
    
    public void setColors(ColorScheme colors) {
        super.setColors(colors);
        
        if (border instanceof AbstractBorder) {
            ((AbstractBorder)border).setLineColor(
                    colors.getColor(ColorScheme.DIALOG_BORDER));
        
        } else if (border instanceof TitledBorder) {
            TitledBorder tb = (TitledBorder)border;
            Border        b = tb.getBorder();
            if (b instanceof AbstractBorder) {
                ((AbstractBorder)b).setLineColor(
                        colors.getColor(ColorScheme.DIALOG_BORDER));
            }
            
            tb.setTitleColor(colors.getColor(ColorScheme.DIALOG_TITLE));
        }
    }

    /**
     * Returns a reference to "this" (CHARVA doesn't distinguish between
     * "content panes", "root panes" and suchlike).
     */
    public Container getContentPane() {
        return (Container) this;
    }
    
    public void setTitle(String title) {
        this.title = title;
        
        if (border instanceof TitledBorder) {
            ((TitledBorder)border).setTitle(title);
            
            if (isDisplayable()) {
                //TODO: implement Toolkit.setTitle()
//                Toolkit.getDefaultToolkit().setTitle(title);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    /**
     * This method does nothing because dialogs are
     * ALWAYS modal in the CHARVA package.
     */
//    public void setModal(boolean modal) {
//    }
//
//    public boolean isModal() {
//        return true;
//    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public Border getBorder() {
        return border;
    }

    public Insets getInsets() {
        if (border != null) {
            return border.getBorderInsets(this);
        }
        
        return super.getInsets();
    }

    /**
     * Draws border around this component
     * 
     * @param g  graphics context in which drawing is performed
     */
    protected void paintBorder(Graphics g) {
        if (border != null) {
            ColorPair colorpair = g.getColor();
            border.paintBorder(this, g, 0, 0, getWidth(), getHeight());
            g.setColor(colorpair);
        }
    }
    
    public void paint(Graphics g) {
        g.setColor(getColor());
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw the enclosing frame
        paintBorder(g);

        // Draw all the contained components
        super.paint(g);
    }
    
    protected void processWindowEvent(WindowEvent evt) {
        super.processWindowEvent(evt);
        
        if (evt.getID() == WindowEvent.WINDOW_OPENED) {
//            getTerminal().setTitle(title);
        
        } else if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
            switch(defaultCloseOperation) {
            case HIDE_ON_CLOSE:
                hide();
                break;
             
            case DO_NOTHING_ON_CLOSE:
            default:
                break;
            }
            
            return;
        }
    }
    
    protected void processKeyEvent(KeyEvent ke) {
        super.processKeyEvent(ke);
        if (ke.isConsumed()) {
            return;
        }
        
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            EventQueue.getInstance().postEvent(new WindowEvent(
                    this, WindowEvent.WINDOW_CLOSING));
        }
    }

    public void setDefaultCloseOperation(int operation) {
        if (operation != DO_NOTHING_ON_CLOSE 
                && operation != HIDE_ON_CLOSE) {
            
            throw new IllegalArgumentException(
                    "defaultCloseOperation must be one of: " 
                    + "DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE");
        }

        this.defaultCloseOperation = operation;
    }

   /**
    * Returns the operation which occurs when the user
    * initiates a "close" on this dialog.
    *
    * @return an integer indicating the window-close operation
    * @see #setDefaultCloseOperation
    */
    public int getDefaultCloseOperation() {
        return defaultCloseOperation;
    }

    protected String paramString() {
        String defaultCloseOperationString;
        if (defaultCloseOperation == HIDE_ON_CLOSE) {
            defaultCloseOperationString = "HIDE_ON_CLOSE";
        } else if (defaultCloseOperation == DO_NOTHING_ON_CLOSE) {
            defaultCloseOperationString = "DO_NOTHING_ON_CLOSE";
        } else {
            defaultCloseOperationString = "";
        }
        
        return super.paramString() 
                + ",defaultCloseOperation=" + defaultCloseOperationString 
                + ",title=" + title;
    }

}
