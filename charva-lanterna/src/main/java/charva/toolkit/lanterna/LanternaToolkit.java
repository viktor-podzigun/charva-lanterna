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

package charva.toolkit.lanterna;

import java.io.IOException;
import charva.awt.Dimension;
import charva.awt.EventQueue;
import charva.awt.GraphicsConstants;
import charva.awt.Point;
import charva.awt.TerminalWindow;
import charva.awt.Toolkit;
import charva.awt.Window;
import charva.awt.event.KeyEvent;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * Lanterna {@link Toolkit} implementation.
 */
public class LanternaToolkit extends Toolkit {

    private final TerminalScreen screen;
    private final EventWorker eventWorker;

    private boolean isCursorVisible;
    private int cursorX;
    private int cursorY;
    
    public LanternaToolkit(final Terminal terminal) throws IOException {
        screen = new TerminalScreen(terminal);
        eventWorker = new EventWorker();

        setDefaultToolkit(this);
    }

    private TextColor getTextColor(final int code) {
        switch (code) {
        case 0:
            return TextColor.ANSI.BLACK;
        case 1:
            return TextColor.ANSI.BLUE;
        case 2:
            return TextColor.ANSI.GREEN;
        case 3:
            return TextColor.ANSI.CYAN;
        case 4:
            return TextColor.ANSI.RED;
        case 5:
            return TextColor.ANSI.MAGENTA;
        case 6:
            return TextColor.ANSI.YELLOW;
        case 7:
            return TextColor.ANSI.WHITE;

        default:
            return TextColor.ANSI.DEFAULT;
        }
    }

    private TextCharacter getTextCharacter(final int chr, final int attrib) {
        final TextColor foregroundColor = getTextColor(attrib & 0x07);
        final TextColor backgroundColor = getTextColor((attrib >>> 4) & 0x07);

        if ((attrib & 0x08) != 0) {
            return new TextCharacter(mapVirtualSign((char) chr),
                                     foregroundColor,
                                     backgroundColor,
                                     SGR.BOLD);
        }

        return new TextCharacter(mapVirtualSign((char) chr),
                                 foregroundColor,
                                 backgroundColor);
    }
    
    protected void drawChar(int x, int y, int chr, int attrib) {
        screen.setCharacter(x, y, getTextCharacter(chr, attrib));
    }

    protected void drawString(int x, int y, String str, int attrib) {
        for (int i = 0, count = str.length(); i < count; i++) {
            screen.setCharacter(x + i,
                                y,
                                getTextCharacter(str.charAt(i), attrib));
        }
    }

    protected void drawLine(int x, int y, int length, int chr, 
            int attrib, boolean isHorizontal) {

        final TextCharacter textChar = getTextCharacter(chr, attrib);

        for (int i = 0; i < length; i++) {
            if (isHorizontal) {
                screen.setCharacter(x + i, y, textChar);
            } else {
                screen.setCharacter(x, y + i, textChar);
            }
        }
    }

    protected void fillBox(int x, int y, int width, int height, int attrib) {
        final TextCharacter textChar = getTextCharacter(' ', attrib);

        for (int j = 0; j < height; j++) {
            screen.setCursorPosition(new TerminalPosition(x, y + j));
            
            for (int i = 0; i < width; i++) {
                screen.setCharacter(x + i, y + j, textChar);
            }
        }
    }

    protected void setCursor(int x, int y) {
        cursorX = x;
        cursorY = y;
        
        screen.setCursorPosition(new TerminalPosition(x, y));
    }

    protected boolean isCursorVisible() {
        return isCursorVisible;
    }
    
    protected void setCursorVisible(boolean isVisible) {
        this.isCursorVisible = isVisible;

        screen.setCursorPosition(isVisible ? new TerminalPosition(
                cursorX,
                cursorY) : null);
    }
    
    /**
     * Returns absolute cursor position
     */
    protected int getCursorXY() {
        return (cursorX & 0xffff) | (cursorY << 16);
    }

    protected void sync() {
        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void closeWindow(LanternaWindow window) {
        // repaint the main window in stack, 
        // this will torn out to repaint other windows
        Window[] winList = getWindows();
        if (winList.length > 0) {
            winList[0].repaint();
        }
    }

    protected void showWindow(LanternaWindow window) {
        final Window w = window.getCharvaWindow();

        final Point location = w.getLocationOnScreen();
        
        // call native implementation
//        showWindow(pluginImpl, w.hasShadow(), 
//                location.x, location.y, w.getWidth(), 
//                w.getHeight());
        
        window.init();
    }

    public Dimension getScreenSize() {
        final TerminalSize size = screen.getTerminalSize();
        return new Dimension(size.getColumns(), size.getRows());
    }
    
    protected TerminalWindow createWindowPeer(Window charvaWindow) {
        return new LanternaWindow(this, charvaWindow);
    }
    
    private char mapVirtualSign(char key) {
        switch (key) {
        // single box
        case GraphicsConstants.VS_ULCORNER:     return Symbols.SINGLE_LINE_TOP_LEFT_CORNER;
        case GraphicsConstants.VS_LLCORNER:     return Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER;
        case GraphicsConstants.VS_URCORNER:     return Symbols.SINGLE_LINE_TOP_RIGHT_CORNER;
        case GraphicsConstants.VS_LRCORNER:     return Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER;
        case GraphicsConstants.VS_LTEE:         return Symbols.SINGLE_LINE_T_RIGHT;
        case GraphicsConstants.VS_RTEE:         return Symbols.SINGLE_LINE_T_LEFT;
        case GraphicsConstants.VS_BTEE:         return Symbols.SINGLE_LINE_T_UP;
        case GraphicsConstants.VS_TTEE:         return Symbols.SINGLE_LINE_T_DOWN;
        case GraphicsConstants.VS_HLINE:        return Symbols.SINGLE_LINE_HORIZONTAL;
        case GraphicsConstants.VS_VLINE:        return Symbols.SINGLE_LINE_VERTICAL;
        case GraphicsConstants.VS_CROSS:        return Symbols.SINGLE_LINE_CROSS;

        // double box
        case GraphicsConstants.VS_DBL_ULCORNER: return Symbols.DOUBLE_LINE_TOP_LEFT_CORNER;
        case GraphicsConstants.VS_DBL_LLCORNER: return Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER;
        case GraphicsConstants.VS_DBL_URCORNER: return Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER;
        case GraphicsConstants.VS_DBL_LRCORNER: return Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER;
        case GraphicsConstants.VS_DBL_LTEE:     return Symbols.DOUBLE_LINE_T_RIGHT;
        case GraphicsConstants.VS_DBL_RTEE:     return Symbols.DOUBLE_LINE_T_LEFT;
        case GraphicsConstants.VS_DBL_BTEE:     return Symbols.DOUBLE_LINE_T_UP;
        case GraphicsConstants.VS_DBL_TTEE:     return Symbols.DOUBLE_LINE_T_DOWN;
        case GraphicsConstants.VS_DBL_HLINE:    return Symbols.DOUBLE_LINE_HORIZONTAL;
        case GraphicsConstants.VS_DBL_VLINE:    return Symbols.DOUBLE_LINE_VERTICAL;
        case GraphicsConstants.VS_DBL_CROSS:    return Symbols.DOUBLE_LINE_CROSS;
        
        // single separator corners for double box
        case GraphicsConstants.VS_DBL_LSEP:     return Symbols.DOUBLE_LINE_T_SINGLE_RIGHT;
        case GraphicsConstants.VS_DBL_RSEP:     return Symbols.DOUBLE_LINE_T_SINGLE_LEFT;
        case GraphicsConstants.VS_DBL_TSEP:     return Symbols.DOUBLE_LINE_T_SINGLE_DOWN;
        case GraphicsConstants.VS_DBL_BSEP:     return Symbols.DOUBLE_LINE_T_SINGLE_UP;
        
        // double separator corners for single box
        case GraphicsConstants.VS_L_DBLSEP:     return Symbols.SINGLE_LINE_T_DOUBLE_RIGHT;
        case GraphicsConstants.VS_R_DBLSEP:     return Symbols.SINGLE_LINE_T_DOUBLE_LEFT;
        case GraphicsConstants.VS_T_DBLSEP:     return Symbols.SINGLE_LINE_T_DOUBLE_DOWN;
        case GraphicsConstants.VS_B_DBLSEP:     return Symbols.SINGLE_LINE_T_DOUBLE_UP;
        
        // other graphical symbols
        case GraphicsConstants.VS_ARROW_DOWN:   return Symbols.ARROW_DOWN;
        case GraphicsConstants.VS_ARROW_UP:     return Symbols.ARROW_UP;
        case GraphicsConstants.VS_ARROW_RIGHT:  return Symbols.ARROW_RIGHT;
        case GraphicsConstants.VS_ARROW_LEFT:   return Symbols.ARROW_LEFT;
        case GraphicsConstants.VS_DOWN:         return 0x25BC;
        case GraphicsConstants.VS_UP:           return 0x25B2;
        case GraphicsConstants.VS_RIGHT:        return 0x25BA;
        case GraphicsConstants.VS_LEFT:         return 0x25C4;
        
        case GraphicsConstants.VS_WTBOARD:      return Symbols.BLOCK_SPARSE;
        case GraphicsConstants.VS_CKBOARD:      return Symbols.BLOCK_MIDDLE;
        case GraphicsConstants.VS_BOARD:        return Symbols.BLOCK_DENSE;
        case GraphicsConstants.VS_BLBOARD:      return Symbols.BLOCK_SOLID;

        case GraphicsConstants.VS_TICK:         return 0x221A;
        case GraphicsConstants.VS_RADIO:        return Symbols.BULLET;
        }
        
        return key;
    }
    
    private int mapVirtualKey(KeyStroke key) {
        switch (key.getKeyType()) {
        case Character:     return key.getCharacter();
        
        case Escape:        return KeyEvent.VK_ESCAPE;
        case Backspace:     return KeyEvent.VK_BACK_SPACE;
        case ArrowLeft:     return KeyEvent.VK_LEFT;
        case ArrowRight:    return KeyEvent.VK_RIGHT;
        case ArrowUp:       return KeyEvent.VK_UP;
        case ArrowDown:     return KeyEvent.VK_DOWN;
        case Insert:        return KeyEvent.VK_INSERT;
        case Delete:        return KeyEvent.VK_DELETE;
        case Home:          return KeyEvent.VK_HOME;
        case End:           return KeyEvent.VK_END;
        case PageUp:        return KeyEvent.VK_PAGE_UP;
        case PageDown:      return KeyEvent.VK_PAGE_DOWN;
        case Tab:           return KeyEvent.VK_TAB;
//        case ReverseTab:    return KeyEvent.VK_UNDEFINED;
        case Enter:         return KeyEvent.VK_ENTER;
        
        case F1:            return KeyEvent.VK_F1;
        case F2:            return KeyEvent.VK_F2;
        case F3:            return KeyEvent.VK_F3;
        case F4:            return KeyEvent.VK_F4;
        case F5:            return KeyEvent.VK_F5;
        case F6:            return KeyEvent.VK_F6;
        case F7:            return KeyEvent.VK_F7;
        case F8:            return KeyEvent.VK_F8;
        case F9:            return KeyEvent.VK_F9;
        case F10:           return KeyEvent.VK_F10;
        case F11:           return KeyEvent.VK_F11;
        case F12:           return KeyEvent.VK_F12;
        case F13:           return KeyEvent.VK_F13;
        case F14:           return KeyEvent.VK_F14;
        case F15:           return KeyEvent.VK_F15;
        case F16:           return KeyEvent.VK_F16;
        case F17:           return KeyEvent.VK_F17;
        case F18:           return KeyEvent.VK_F18;
        case F19:           return KeyEvent.VK_F19;
        }
        
        return KeyEvent.VK_UNDEFINED;
    }

    public void startEventThread() {
        if (!eventWorker.isRunning()) {
            new Thread(eventWorker).start();
        }
    }

    public void stopEventThread() {
        eventWorker.stopRequest();
    }

    private final class EventWorker implements Runnable {

        private volatile boolean running;
        private volatile boolean stopRequest;

        public boolean isRunning() {
            return running;
        }

        public void stopRequest() {
            stopRequest = true;
        }

        public void run() {
            running = true;
            try {
                screen.startScreen();

                try {
                    //screen.clear();
                    //screen.setCursorPosition(new TerminalPosition(0, 0));

                    final EventQueue eventQueue = EventQueue.getInstance();

                    while (!stopRequest) {
                        final KeyStroke keyStroke = screen.pollInput();
                        if (keyStroke != null) {
                            processKeyEvent(getTopWindow(),
                                            mapVirtualKey(keyStroke),
                                            0);
                        }

                        if (!eventQueue.isEmpty()) {
                            processIdleEvent();

                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                stopRequest();
                            }
                        }
                    }
                } finally {
                    screen.stopScreen();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
