/* class JTextArea
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
import charva.awt.Component;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.Rectangle;
import charva.awt.event.KeyEvent;
import charva.awt.event.MouseEvent;
import charvax.swing.text.JTextComponent;


/**
 * JTextArea is an (optionally editable) multi-line area that displays
 * plain text.
 * <p>
 * The JTextArea class implements the Scrollable interface, which enables
 * it to be placed inside a charvax.swing.JScrollPane. In fact, in the
 * CHARVA framework it should ALWAYS be used inside a JScrollPane, otherwise
 * it will be unusable (its size depends on the text it contains).
 * <p>
 * Note that, unlike the Java SWING's JTextArea, pressing the TAB key while
 * the keyboard focus is in the JTextArea will not cause a tab to be
 * inserted; instead, it will move the keyboard input focus to the next
 * focus-traversable component (if there is one).
 */
public class JTextArea extends JTextComponent implements Scrollable {

    private static final int LINE_COUNT         = 1;
    private static final int LINE_START_OFFSET  = 2;
    private static final int LINE_END_OFFSET    = 3;
    private static final int LINE_OF_OFFSET     = 4;
    
    
    private int         rows;
    private int         columns;

    private int         preferredRows;
    private int         preferredColumns;

    /** The caret is updated only when the component is drawn       */
    private Point       caret = new Point(0, 0);

    private boolean     lineWrap;
    private boolean     wrapStyleWord;

    
    /**
     * The default constructor creates an empty text area with 10 rows
     * and 10 columns.
     */
    public JTextArea() {
        this("");
    }

    /**
     * Construct a text area with 10 rows and 10 columns, and containing
     * the specified text.
     */
    public JTextArea(String text) {
        this(text, 10, 10);
    }

    /**
     * Construct a text area wth the specified number of rows and columns,
     * and containing the specified text.
     */
    public JTextArea(String text, int rows, int columns) {
        setDocument(text);
        
        this.rows             = rows;
        this.preferredRows    = rows;
        this.columns          = columns;
        this.preferredColumns = columns;
        setCaretPosition(0);
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        
        setColumns(width);
        setRows(height);
    }
    
    /**
     * Sets the number of columns in this JTextArea
     */
    public void setColumns(int columns) {
        this.columns     = columns;
        preferredColumns = columns;
    }

    /**
     * Returns the number of columns in this JTextArea
     */
    public int getColumns() {
        return preferredColumns;
    }

    /**
     * Sets the number of rows in this JTextArea
     */
    public void setRows(int rows) {
        this.rows     = rows;
        preferredRows = rows;
    }

    /**
     * Returns the number of rows in this JTextArea
     */
    public int getRows() {
        return preferredRows;
    }

    /**
     * Returns the size of this component
     */
    public Dimension getSize() {
        return new Dimension(columns, rows);
    }

    public int getWidth() {
        return columns;
    }

    public int getHeight() {
        return rows;
    }

    /**
     * Appends the specified text to the end of the document
     */
    public void append(String text) {
        document.append(text);
        caretPosition = document.length();

        refresh();
    }

    /**
     * Inserts the specified text at the specified position (ie at the
     * specified offset from the start of the document)..
     */
    public void insert(String text, int pos) {
        super.document.insert(pos, text);
        caretPosition = pos + text.length();

        refresh();
    }

    /**
     * Replaces the text from the specified start position to end position
     * with the specified text.
     */
    public void replaceRange(String text, int start, int end) {
        super.document.replace(start, end, text);
        caretPosition = start + text.length();

        refresh();
    }

    /**
     * Sets the position of the text insertion caret for this JTextArea.
     */
    public void setCaretPosition(int caret) {
        super.setCaretPosition(caret);

        refresh();
    }

    /**
     * Returns the number of lines of text displayed in the JTextArea.
     */
    public int getLineCount() {
        return offsetCalc(LINE_COUNT, 0);
    }

    /**
     * Returns the offset of the first character in the specified line
     * of text.
     */
    public int getLineStartOffset(int line) {
        return offsetCalc(LINE_START_OFFSET, line);
    }

    /**
     * Returns the offset of the last character in the specified line.
     */
    public int getLineEndOffset(int line) {
        return offsetCalc(LINE_END_OFFSET, line);
    }

    /**
     * Translates an offset (relative to the start of the document)
     * to a line number.
     */
    public int getLineOfOffset(int offset) {
        return offsetCalc(LINE_OF_OFFSET, offset);
    }

    /**
     * Sets the line-wrapping policy of the JTextArea. If set to true,
     * lines will be wrapped if they are too long to fit within the
     * allocated width. If set to false, the lines will always be
     * unwrapped. The default value of this property is false.
     */
    public void setLineWrap(boolean wrap) {
        lineWrap = wrap;
        rows     = preferredRows;
        columns  = preferredColumns;
    }

    /**
     * Returns the line-wrapping policy of the JTextArea. If set to true,
     * lines will be wrapped if they are too long to fit within the
     * allocated width. If set to false, the lines will always be
     * unwrapped.
     */
    public boolean getLineWrap() {
        return lineWrap;
    }

    /**
     * Sets the line-wrapping style to be used if getLineWrap() is true.
     * If true, lines will be wrapped at word boundaries (whitespace) if
     * they are too long to fit in the allocated number of columns. If
     * false, lines will be wrapped at character boundaries. The default
     * value of this property is false.
     */
    public void setWrapStyleWord(boolean wrapWord) {
        wrapStyleWord = wrapWord;
    }

    /**
     * Returns the line-wrapping style to be used if getLineWrap() is true.
     * If true, lines will be wrapped at word boundaries (whitespace) if
     * they are too long to fit in the allocated number of columns. If
     * false, lines will be wrapped at character boundaries.
     */
    public boolean getWrapStyleWord() {
        return wrapStyleWord;
    }

    /**
     * Called by the LayoutManager.
     */
    public Dimension getMinimumSize() {
        return getSize();
    }

    /**
     * Process KeyEvents that have been generated by this JTextArea.
     */
    protected void processKeyEvent(KeyEvent ke) {
        // first call all KeyListener objects that may have been registered
        // for this component
        super.processKeyEvent(ke);
        if (ke.isConsumed())
            return;

        int caret = getCaretPosition();
        int line  = getLineOfOffset(caret);
        int key   = ke.getKeyCode();
        
        if ((key == KeyEvent.VK_TAB && ke.isShiftDown())) {
            transferFocusBackward();
            return;
        }
        
        if (key == KeyEvent.VK_TAB) {
            transferFocus();
            return;
        }
        
        if (key == KeyEvent.VK_LEFT && caret > 0) {
            setCaretPosition(caret - 1);
        
        } else if (key == KeyEvent.VK_RIGHT &&
                caret < getDocument().length()) {
            
            setCaretPosition(caret + 1);
        
        } else if (key == KeyEvent.VK_HOME) {
            int lineStart = getLineStartOffset(line);
            setCaretPosition(lineStart);
        
        } else if (key == KeyEvent.VK_END) {
            int lineEnd = getLineEndOffset(line);
            setCaretPosition(lineEnd);
        
        } else if ((key == KeyEvent.VK_PAGE_UP || key == KeyEvent.VK_PAGE_DOWN) 
                && getParent() instanceof JViewport) {

            JViewport viewport = (JViewport) getParent();
            int vertical_offset = -1 * viewport.getViewPosition().y;
            int viewport_height = viewport.getSize().height;
            if (key == KeyEvent.VK_PAGE_UP) {
                if (line > vertical_offset)
                    line = vertical_offset;
                else
                    line = vertical_offset - viewport_height;

                line = (line < 0) ? 0 : line;

            } else {
                if (line < vertical_offset + viewport_height - 1)
                    line = vertical_offset + viewport_height - 1;
                else
                    line = vertical_offset + (2 * viewport_height) - 1;

                line = (line > (getLineCount() - 1) ?
                        getLineCount() - 1 : line);
            }
            
            setCaretPosition(getLineStartOffset(line));

        } else if (key == KeyEvent.VK_UP && line > 0) {
            
            int column = caret - getLineStartOffset(line);
            int prevlineStart = getLineStartOffset(line - 1);
            int prevlineEnd = getLineEndOffset(line - 1);
            if (column > prevlineEnd - prevlineStart)
                column = prevlineEnd - prevlineStart;
            
            setCaretPosition(prevlineStart + column);

        } else if (key == KeyEvent.VK_DOWN 
                && line < getLineCount() - 1) {
            
            int column = caret - getLineStartOffset(line);
            int nextlineStart = getLineStartOffset(line + 1);
            int nextlineEnd = getLineEndOffset(line + 1);
            if (column > nextlineEnd - nextlineStart)
                column = nextlineEnd - nextlineStart;
            
            setCaretPosition(nextlineStart + column);

        } else if (!super.isEditable()) {
//            SwingUtilities.windowForComponent(this).getTerminal().beep();

        } else if (!ke.isActionKey() && ke.getID() == KeyEvent.KEY_TYPED) {
            char[] arry = {ke.getKeyChar()};
            insert(new String(arry), caret);

        } else if (key == KeyEvent.VK_ENTER) {
            char[] arry = {'\n'};
            insert(new String(arry), caret);

        } else if (key == KeyEvent.VK_BACK_SPACE && caret > 0) {
            replaceRange("", caret - 1, caret);

        } else if (key == KeyEvent.VK_DELETE 
                && caret < getDocument().length() - 1) {
            
            replaceRange("", caret, caret + 1);
        }

        // if this JTextArea is contained in a JViewport, let the JViewport
        // do the drawing, after setting the clip rectangle
        if (!(getParent() instanceof JViewport)) {
            repaint();
//            draw(0);
//            requestFocus();
//            super.requestSync();
        }
    }

    /**
     * Process a MouseEvent that was generated by clicking the mouse
     * somewhere inside this JTextArea.
     * <p>
     * Clicking the mouse inside the JTextArea moves the caret position
     * to where the mouse was clicked.
     */
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (e.isConsumed())
            return;
        
        if (e.getButton() == MouseEvent.BUTTON1 &&
                e.getID() == MouseEvent.MOUSE_CLICKED &&
                this.isFocusTraversable()) {

            // get the absolute origin of this component
            Point  origin = getLocationOnScreen();
            Insets insets = super.getInsets();
            origin.translate(insets.left, insets.top);

            int line = e.getY();
            if (line > getLineCount() - 1)
                return;

            int column = e.getX();
            int lineStart = getLineStartOffset(line);
            int lineEnd = getLineEndOffset(line);
            if (column > lineEnd - lineStart)
                column = lineEnd - lineStart;
            
            setCaretPosition(lineStart + column);
        }
    }

    // TODO: Implement drawing within clipping rectangle
    
    public void paint(Graphics g) {
        // draw the border inherited from JComponent, if it exists
        super.paint(g);

        Insets insets = getInsets();
        g.translate(insets.left, insets.top);

        final ColorPair colorPair;
        if (isEnabled() && isEditable()) {
            colorPair = color;
        } else {
            colorPair = disabledColor;
        }
        
        Point tempCaret = null;
        Point caret     = this.caret;

        // start by blanking out the text area
        g.setColor(colorPair);
        g.fillRect(0, 0, getWidth(), getHeight());

        // scan through the entire document, drawing each character in it
        int row = 0, col = 0;
        for (int i = 0; i < document.length(); i++) {
            // At some point during the scan of the document, the
            // caret position should match the scan index, unless the caret
            // position is after the last character of the document.
            if (caretPosition == i) {
                tempCaret = new Point(col, row);

                if (!tempCaret.equals(caret)) {
                    caret = tempCaret;
                }
            }

            char chr = document.charAt(i);
            if (col < columns) {
                if (chr == '\n') {
                    col = 0;
                    row++;
                    if (row >= rows) {
                        rows++;
                    }
                } else {
                    g.drawChar(chr, col, row);
                    col++;
                }
            } else {
                // We have reached the right-hand column
                if (lineWrap == false) {
                    if (chr == '\n') {
                        col = 0;
                        row++;
                        if (row >= rows) {
                            rows++;
                        }
                    } else {
                        g.drawChar(chr, col, row);
                        col++;
                        columns++;
                    }
                } else {
                    // line-wrap is true
                    if (wrapStyleWord == false) {
                        col = 0;
                        row++;
                        if (row >= rows) {
                            rows++;
                        }
                        
                        if (chr != '\n') {// thanks to Chris Rogers for this
                            g.drawChar(chr, col, row);
                        }
                    } else {
                        // We must back-track until we get to whitespace, so
                        // that we can move the word to the next line.
                        int j;
                        for (j = 0; j < columns; j++) {
                            char tmpchr = document.charAt(i - j);
                            if (Character.isWhitespace(tmpchr)) {
                                deleteEOL(g, col - j, row, colorPair);
                                col = 0;
                                row++;
                                if (row >= rows) {
                                    rows++;
                                }
                                
                                i -= j;
                                break;
                            }
                        }
                        
                        if (j == columns) {
                            // the word was too long
                            if (Character.isWhitespace(chr)) {
                                col = 0;
                                row++;
                                if (row >= rows) {
                                    rows++;
                                }
                            }
                        }
                    }
                }
            }
        }

        // check for the case where the caret position is after the last
        // character of the document
        if (caretPosition == document.length()) {
            tempCaret = new Point(col, row);
            caret = tempCaret;
        }

        if (!this.caret.equals(caret)) {
            this.caret = caret;
        }
    }

    public void requestFocus() {
        // generate the FOCUS_GAINED event
        super.requestFocus();

        // get the absolute origin of this component
        Point origin = getLocationOnScreen();
        SwingUtilities.windowForComponent(this).setCursor(
                origin.addOffset(caret));
    }

    /**
     * Returns the preferred size of the viewport for this JTextArea
     * when it is in a JScrollPane (this method implements the
     * Scrollable interface). The size is determined by the number of
     * rows and columns set for this JTextArea (either in the constructor
     * or in the setColumns() and setRows() methods).
     */
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(preferredColumns, preferredRows);
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        
        // TODO Auto-generated method stub
        return 0;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * Returns false which indicates that the width of the viewport does not 
     * determine the width of this component.
     *
     * @return false
     * @see Scrollable#getScrollableTracksViewportWidth
     */
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    /**
     * Returns false to indicate that the height of the viewport does not
     * determine the height of this component.
     *
     * @return false
     * @see Scrollable#getScrollableTracksViewportHeight
     */
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    /**
     * A private helper method to delete from a specified position
     * until the end of the line.
     */
    private void deleteEOL(Graphics g, int col, int row, ColorPair colorpair) {
        g.setColor(colorpair);
        for (int i = col; i < columns; i++) {
            g.drawChar(' ', i, row);
        }
    }

    /**
     * This helper method converts offset to line number and
     * vice versa.
     */
    private int offsetCalc(int mode, int value) {
        int lineOfOffset = 0;
        int row = 0;

        if (mode == LINE_START_OFFSET && value == 0)
            return 0;

        for (int col = 0, i = 0; i < document.length(); i++) {
            
            if (mode == LINE_OF_OFFSET && value == i)
                lineOfOffset = row;

            char chr = document.charAt(i);
            if (col < columns) {
                if (chr == '\n') {
                    col = 0;
                    row++;
                } else {
                    col++;
                }
            } else {
                // we have reached the right-hand column
                if (lineWrap == false) {
                    if (chr == '\n') {
                        col = 0;
                        row++;
                    }
                } else {
                    // line-wrap is true
                    if (wrapStyleWord == false) {
                        col = 0;
                        row++;
                    } else {
                        // we must back-track until we get to whitespace, so
                        // that we can move the word to the next line.
                        int j;
                        for (j = 0; j < columns; j++) {
                            char tmpchr = document.charAt(i - j);
                            if (tmpchr == ' ' || tmpchr == '\t') {
                                col = 0;
                                row++;
                                i -= j;
                                break;
                            }
                        }
                        
                        if (j == columns) {
                            // the word was too long
                            if (chr == ' ' || chr == '\n' || chr == '\t') {
                                col = 0;
                                row++;
                            }
                        }
                    }
                } // end if line-wrap is true
            } // end if we have reached the right-hand column

            if (mode == LINE_START_OFFSET && col == 0 && row == value)
                return i + 1;
            
            if (mode == LINE_END_OFFSET && col == 0 && row == value + 1)
                return i;
        }

        if (mode == LINE_OF_OFFSET) {
            if (value == document.length())
                return row;
            
            return lineOfOffset;
        }
        
        if (mode == LINE_COUNT)
            return row + 1;
        
        if (mode == LINE_END_OFFSET && row == value)
            return document.length();
        
        throw new IndexOutOfBoundsException(
                "Invalid offset or line number: mode=" + mode 
                    + ", value=" + value 
                    + ", row=" + row 
                    + ", doc=\"" + document + "\"");
    }

    /* Private helper method used to redraw the component if its state
     * has changed
     */
    private void refresh() {
        // If this JTextArea is contained in a JViewport, the PaintEvent
        // that we post must request a redraw of the JViewport, so that
        // the JViewport can set the clipping rectangle before calling the
        // draw() method of this JTextArea
        Component todraw;
        if (getParent() instanceof JViewport)
            todraw = getParent();
        else
            todraw = this;

        // If this component is already displayed, generate a PaintEvent
        // and post it onto the queue
        todraw.repaint();
    }

    /**
     * Returns a string representation of this JTextArea. This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this JTextArea.
     */
    protected String paramString() {
        String wrapString = (lineWrap ? "true" : "false");
        String wordString = (wrapStyleWord ? "true" : "false");

        return super.paramString() + ",colums=" + columns 
                + ",rows=" + rows 
                + ",word=" + wordString 
                + ",wrap=" + wrapString;
    }
}
