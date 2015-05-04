/* class JFrame
 *
 * Copyright (C) 2001-2003  R M Pitman
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

import charva.awt.BorderLayout;
import charva.awt.Color;
import charva.awt.ColorPair;
import charva.awt.ColorScheme;
import charva.awt.Container;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Window;
import charva.awt.event.WindowEvent;
import charvax.swing.border.AbstractBorder;
import charvax.swing.border.Border;
import charvax.swing.border.LineBorder;
import charvax.swing.border.TitledBorder;


/**
 * JFrame is a top-level window with a menu-bar
 */
public class JFrame extends Window implements WindowConstants {
    
    public static final int EXIT_ON_CLOSE       = 3;

    private int         defaultCloseOperation = HIDE_ON_CLOSE;
    
    private JMenuBar    menubar;
    private JPanel      contentPane = new JPanel();
    
    private String      title;
    private Border      border;
    
    
    /**
     * Creates a frame window with empty title.
     */
    public JFrame() {
        this("");
    }

    /**
     * Creates a frame window with the specified title.
     */
    public JFrame(String title) {
        super(null);
        
        this.title  = title;
        this.border = new TitledBorder(
                new LineBorder(
                        currentColors.getColor(ColorScheme.DIALOG_BORDER), 
                        new Insets(1, 1, 1, 1)), 
                title, TitledBorder.LEFT, TitledBorder.TOP, 
                currentColors.getColor(ColorScheme.DIALOG_TITLE));
        
        add(contentPane, BorderLayout.CENTER);
        contentPane.setLayout(new BorderLayout());
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

    public Container getContentPane() {
        return contentPane;
    }

    /**
     * Sets the menubar for this frame.
     */
    public void setJMenuBar(JMenuBar menubar) {
        this.menubar = menubar;
        add(menubar, BorderLayout.NORTH);
    }

    public void setTitle(String title) {
        this.title = title;
    
        if (border instanceof TitledBorder) {
            ((TitledBorder)border).setTitle(title);
        
            if (isDisplayable()) {
//                getTerminal().setTitle(title);
            }
        }
    }

    /**
     * Returns this Frame's title, or an empty string if the
     * frame does not have a title
     */
    public String getTitle() {
        return (title != null ? title : "");
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public Border getBorder() {
        return border;
    }

    public Insets getInsets() {
        if (border != null)
            return border.getBorderInsets(this);
        
        return super.getInsets();
    }

    /**
     * Draws border around this component
     * 
     * @param t  graphics context in which drawing is performed
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

    /**
     * Overrides the corresponding method in Container
     */
    public Dimension getMinimumSize() {
        Dimension minsize = super.getMinimumSize();
        if (menubar == null)
            return minsize;

        Insets insets = getInsets();
        Dimension menubarSize = menubar.getMinimumSize();
        if (menubarSize.width + insets.left + insets.right > minsize.width)
            minsize.width = menubarSize.width + insets.left + insets.right;

        if (menubarSize.height > minsize.height)
            minsize.height = menubarSize.height;

        return minsize;
    }

    /**
     * Sets the foreground color of this JFrame and its content pane
     */
    public void setForeground(Color color) {
        super.setForeground(color);
        contentPane.setForeground(color);
    }

    /**
     * Sets the background color of this JFrame and its content pane
     */
    public void setBackground(Color color) {
        super.setBackground(color);
        contentPane.setBackground(color);
    }
}
