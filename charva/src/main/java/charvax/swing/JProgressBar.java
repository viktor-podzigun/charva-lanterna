/* class JProgressBar
 *
 * Copyright (C) 2001, 2002, 2003  R M Pitman
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

import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.GraphicsConstants;
import charva.awt.Insets;
import charva.awt.event.KeyEvent;


/**
 * A component that displays an integer value within a bounded interval.
 * A progress bar is typically used to indicate the progress of some task
 * by displaying a percentage of completion and possibly a textual display
 * of this percentage.
 */
public class JProgressBar extends JComponent {

    protected int       minimum;
    protected int       maximum;
    protected int       value;
    
    protected boolean   stringPainted;
    protected String    string         = "";

    protected boolean   indeterminate;

    protected Thread    indeterminateThread;
    
    
    /**
     * Creates a horizontal progress bar that displays a border
     * but no progress string.
     */
    public JProgressBar() {
        this(0, 100);
    }

    /**
     * Creates a progress bar with the specified minimum and
     * maximum values.
     */
    public JProgressBar(int min, int max) {
        minimum = min;
        maximum = max;
        
        // preferred size of this component
        setSize(5, 1);
    }

    /**
     * Set the progress bar's minimum value.
     */
    public void setMinimum(int min) {
        if (minimum == min) {
            return;
        }
        
        minimum = min;
        if (maximum <= minimum) {
            maximum = minimum + 1;
        }
        
        if (value < minimum) {
            value = minimum;
        }
        
        repaint();
    }

    /**
     * Set the progress bar's value
     */
    public void setValue(int value) {
        if (this.value == value) {
            return;
        }
        
        if (value < minimum) {
            this.value = minimum;
        } else {
            this.value = value;
        }

        // If this component is already displayed, generate a PaintEvent
        // and post it onto the queue
        repaint();
    }

    /**
     * Set the progress bar's maximum value
     */
    public void setMaximum(int max) {
        if (maximum == max) {
            return;
        }
        
        maximum = max;
        if (minimum > maximum) {
            minimum = maximum - 1;
        }
        
        if (value > maximum) {
            value = maximum;
        }
        
        repaint();
    }

    /**
     * Get the screen size of the progress bar
     */
    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public int getWidth() {
        Insets insets = super.getInsets();
        return super.getWidth() + insets.left + insets.right;
    }

    public int getHeight() {
        Insets insets = super.getInsets();
        return super.getHeight() + insets.top + insets.bottom;
    }

    public void paint(Graphics g) {
        // draw the border if it exists
        super.paint(g);

        Insets insets = super.getInsets();
        g.translate(insets.left, insets.top);
        
        g.setColor(getColor());

        int width = getWidth();
        int offset = ((value - minimum) * width) / maximum;
        
        if (!isIndeterminate()) {
            for (int i = 0; i < offset; i++) {
                g.drawChar(GraphicsConstants.VS_BLBOARD, i, 0);
            }
            for (int k = offset; k < width; k++) {
                g.drawChar(GraphicsConstants.VS_CKBOARD, k, 0);
            }
        } else {
            for (int i = 0; i < width; i++) {
                g.drawChar(GraphicsConstants.VS_CKBOARD, i, 0);
            }
            
            g.drawChar(GraphicsConstants.VS_BLBOARD, offset, 0);
        }

        // Display the progress string if required
        if (isStringPainted()) {
            offset = (getSize().width - string.length()) / 2;
            g.drawString(string, offset, 0);
        }
    }

    /**
     * This component will not receive focus when Tab or Shift-Tab is pressed.
     */
    public boolean isFocusTraversable() {
        return false;
    }

    /**
     * The JProgressBar class ignores key events. A JProgressBar should never
     * have input focus anyway.
     */
    protected void processKeyEvent(KeyEvent ke) {
    }

    /**
     * The JProgressBar component never gets the keyboard input focus.
     */
    public void requestFocus() {
    }

    /**
     * Returns a string representation of this <code>JProgressBar</code>.
     * This method is intended to be used only for debugging purposes. The 
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JProgressBar</code>
     */
    protected String paramString() {
        String progressStringString = (string != null ? string : "");
        String paintStringString = (stringPainted ? "true" : "false");
        String indeterminateString = (indeterminate ? "true" : "false");

        return super.paramString() + ",paintString=" + paintStringString 
                + ",progressString=" + progressStringString
                + ",indeterminateString=" + indeterminateString
                + ",value=" + value 
                + ",minimum=" + minimum 
                + ",maximum=" + maximum;
    }
    
    public Dimension getMinimumSize() {
        return getSize();
    }

    public int getMinimum() {
        return minimum;
    }

    public int getValue() {
        return value;
    }

    public int getMaximum() {
        return maximum;
    }

    /**
     * Returns the value of the stringPainted property
     */
    public boolean isStringPainted() {
        return stringPainted;
    }

    /**
     * Set the value of the stringPainted property
     */
    public void setStringPainted(boolean stringPainted) {
        this.stringPainted = stringPainted;
    }

    /**
     * Sets the value of the progress string
     */
    public void setString(String string) {
        this.string = string;
        repaint();
    }

    public void setIndeterminate(boolean newval) {
        if (newval == indeterminate)
            return;   // no change in state.

        indeterminate = newval;
        if (newval) {
            setMinimum(0);
            setMaximum(100);
            indeterminateThread = new IndeterminateThread();
            indeterminateThread.start();
        } else {
            if (indeterminateThread != null &&
                    indeterminateThread.isAlive()) {

                indeterminateThread.interrupt();
            }
        }
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    /**
     * Returns the value of the progress string
     */
    public String getString() {
        return string;
    }

    public void finalize() {
        if (indeterminateThread != null 
                && indeterminateThread.isAlive()) {

            indeterminateThread.interrupt();
        }
    }

    /**
     * A nonstatic inner class that updates the progress bar
     * once per second when the progress bar is in "indeterminate" mode.
     */
    private class IndeterminateThread extends Thread {
        
        private boolean     right = true;
        private int         percent;
        
        private IndeterminateThread() {
        }

        /**
         * Twice per second, wake up and update the progress bar. Note that
         * since this thread is not the event-dispatching thread, we cannot
         * manipulate the screen components directly; instead, we must
         * call the static method "invokeLater()" of the EventQueue class,
         * which will cause the event-dispatching thread to update the progress
         * bar.
         * See "Core Java, Volume II" by Horstmann and Cornell, chapter 1;
         * Also see
         * http://java.sun.com/docs/books/tutorial/uiswing/overview/threads.html
         */
        public void run() {
            try {
                while (true) {
                    this.adjust();
                    Thread.sleep(50L);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            JProgressBar.this.setValue(percent);
                        }
                    });
                }
            } catch (InterruptedException e) {
                return;
            }
        }

        /**
         * Adjust the percent-completed so that the indicator moves right
         * and left continuously.
         */
        private void adjust() {
            if (right) {
                if (percent < 96)
                    percent += 4;
                else {
                    right = false;
                }
            } else {
                if (percent > 0)
                    percent -= 4;
                else {
                    right = true;
                }
            }
        }
    }
}
