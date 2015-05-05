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

import charva.awt.Dimension;
import charva.awt.EventQueue;
import charva.awt.GraphicsConstants;
import charva.awt.Point;
import charva.awt.TerminalWindow;
import charva.awt.Toolkit;
import charva.awt.Window;
import charva.awt.event.KeyEvent;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.ACS;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;


/**
 * Lanterna {@link Toolkit} implementation.
 */
public class LanternaToolkit extends Toolkit {

    private final Terminal terminal;
    private final EventWorker eventWorker;

    private boolean isCursorVisible;
    private int cursorX;
    private int cursorY;
    
    
    public LanternaToolkit() {
        terminal = TerminalFacade.createTerminal();
        eventWorker = new EventWorker();

        setDefaultToolkit(this);
    }
    
    protected void drawChar(int x, int y, int chr, int attrib) {
        terminal.applyBackgroundColor((attrib >>> 4) & 0x0f);
        terminal.applyForegroundColor(attrib & 0x0f);
        
        terminal.moveCursor(x, y);
        terminal.putCharacter(mapVirtualSign((char)chr));
    }

    protected void drawString(int x, int y, String str, int attrib) {
        terminal.applyBackgroundColor((attrib >>> 4) & 0x0f);
        terminal.applyForegroundColor(attrib & 0x0f);
        
        terminal.moveCursor(x, y);
        
        for (int i = 0, count = str.length(); i < count; i++) {
            terminal.putCharacter(mapVirtualSign(str.charAt(i)));
        }
    }

    protected void drawLine(int x, int y, int length, int chr, 
            int attrib, boolean isHorizontal) {
        
        terminal.applyBackgroundColor((attrib >>> 4) & 0x0f);
        terminal.applyForegroundColor(attrib & 0x0f);
        
        terminal.moveCursor(x, y);
        
        for (int i = 0; i < length; i++) {
            if (!isHorizontal) {
                terminal.moveCursor(x, y + i);
            }
                
            terminal.putCharacter(mapVirtualSign((char)chr));
        }
    }

    protected void fillBox(int x, int y, int width, int height, int attrib) {
        terminal.applyBackgroundColor((attrib >>> 4) & 0x0f);
        terminal.applyForegroundColor(attrib & 0x0f);
        
        for (int j = 0; j < height; j++) {
            terminal.moveCursor(x, y + j);
            
            for (int i = 0; i < width; i++) {
                terminal.putCharacter(' ');
            }
        }
    }

    protected void setCursor(int x, int y) {
        cursorX = x;
        cursorY = y;
        
        terminal.moveCursor(x, y);
    }

    protected boolean isCursorVisible() {
        return isCursorVisible;
    }
    
    protected void setCursorVisible(boolean isVisible) {
        this.isCursorVisible = isVisible;
        terminal.setCursorVisible(isVisible);
    }
    
    /**
     * Returns absolute cursor position
     */
    protected int getCursorXY() {
        return (cursorX & 0xffff) | (cursorY << 16);
    }

    protected void sync() {
        terminal.flush();
    }

    protected void closeWindow(LanternaWindow window) {
        // repaint the main window in stack, 
        // this will torn out to repaint other windows
//        Window[] winList = toolkit.getWindows();
//        if (winList.length > 0) {
//            winList[0].repaint();
//        }
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
        final TerminalSize size = terminal.getTerminalSize();
        return new Dimension(size.getColumns(), size.getRows());
    }
    
    protected TerminalWindow createWindowPeer(Window charvaWindow) {
        return new LanternaWindow(this, charvaWindow);
    }
    
    private char mapVirtualSign(char key) {
        switch (key) {
        // single box
        case GraphicsConstants.VS_ULCORNER:     return ACS.SINGLE_LINE_UP_LEFT_CORNER;
        case GraphicsConstants.VS_LLCORNER:     return ACS.SINGLE_LINE_LOW_LEFT_CORNER;
        case GraphicsConstants.VS_URCORNER:     return ACS.SINGLE_LINE_UP_RIGHT_CORNER;
        case GraphicsConstants.VS_LRCORNER:     return ACS.SINGLE_LINE_LOW_RIGHT_CORNER;
        case GraphicsConstants.VS_LTEE:         return ACS.SINGLE_LINE_T_RIGHT;
        case GraphicsConstants.VS_RTEE:         return ACS.SINGLE_LINE_T_LEFT;
        case GraphicsConstants.VS_BTEE:         return ACS.SINGLE_LINE_T_UP;
        case GraphicsConstants.VS_TTEE:         return ACS.SINGLE_LINE_T_DOWN;
        case GraphicsConstants.VS_HLINE:        return ACS.SINGLE_LINE_HORIZONTAL;
        case GraphicsConstants.VS_VLINE:        return ACS.SINGLE_LINE_VERTICAL;
        case GraphicsConstants.VS_CROSS:        return ACS.SINGLE_LINE_CROSS;

        // double box
        case GraphicsConstants.VS_DBL_ULCORNER: return ACS.DOUBLE_LINE_UP_LEFT_CORNER;
        case GraphicsConstants.VS_DBL_LLCORNER: return ACS.DOUBLE_LINE_LOW_LEFT_CORNER;
        case GraphicsConstants.VS_DBL_URCORNER: return ACS.DOUBLE_LINE_UP_RIGHT_CORNER;
        case GraphicsConstants.VS_DBL_LRCORNER: return ACS.DOUBLE_LINE_LOW_RIGHT_CORNER;
        case GraphicsConstants.VS_DBL_LTEE:     return ACS.DOUBLE_LINE_T_RIGHT;
        case GraphicsConstants.VS_DBL_RTEE:     return ACS.DOUBLE_LINE_T_LEFT;
        case GraphicsConstants.VS_DBL_BTEE:     return ACS.DOUBLE_LINE_T_UP;
        case GraphicsConstants.VS_DBL_TTEE:     return ACS.DOUBLE_LINE_T_DOWN;
        case GraphicsConstants.VS_DBL_HLINE:    return ACS.DOUBLE_LINE_HORIZONTAL;
        case GraphicsConstants.VS_DBL_VLINE:    return ACS.DOUBLE_LINE_VERTICAL;
        case GraphicsConstants.VS_DBL_CROSS:    return ACS.DOUBLE_LINE_CROSS;
        
        // single separator corners for double box
        case GraphicsConstants.VS_DBL_LSEP:     return ACS.DOUBLE_LINE_T_SINGLE_RIGHT;
        case GraphicsConstants.VS_DBL_RSEP:     return ACS.DOUBLE_LINE_T_SINGLE_LEFT;
        case GraphicsConstants.VS_DBL_TSEP:     return ACS.DOUBLE_LINE_T_SINGLE_DOWN;
        case GraphicsConstants.VS_DBL_BSEP:     return ACS.DOUBLE_LINE_T_SINGLE_UP;
        
        // double separator corners for single box
        case GraphicsConstants.VS_L_DBLSEP:     return ACS.SINGLE_LINE_T_DOUBLE_RIGHT;
        case GraphicsConstants.VS_R_DBLSEP:     return ACS.SINGLE_LINE_T_DOUBLE_LEFT;
        case GraphicsConstants.VS_T_DBLSEP:     return ACS.SINGLE_LINE_T_DOUBLE_DOWN;
        case GraphicsConstants.VS_B_DBLSEP:     return ACS.SINGLE_LINE_T_DOUBLE_UP;
        
        // other graphical symbols
        case GraphicsConstants.VS_ARROW_DOWN:   return ACS.ARROW_DOWN;
        case GraphicsConstants.VS_ARROW_UP:     return ACS.ARROW_UP;
        case GraphicsConstants.VS_ARROW_RIGHT:  return ACS.ARROW_RIGHT;
        case GraphicsConstants.VS_ARROW_LEFT:   return ACS.ARROW_LEFT;
        case GraphicsConstants.VS_DOWN:         return 0x25BC;
        case GraphicsConstants.VS_UP:           return 0x25B2;
        case GraphicsConstants.VS_RIGHT:        return 0x25BA;
        case GraphicsConstants.VS_LEFT:         return 0x25C4;
        
        case GraphicsConstants.VS_WTBOARD:      return ACS.BLOCK_SPARSE;
        case GraphicsConstants.VS_CKBOARD:      return ACS.BLOCK_MIDDLE;
        case GraphicsConstants.VS_BOARD:        return ACS.BLOCK_DENSE;
        case GraphicsConstants.VS_BLBOARD:      return ACS.BLOCK_SOLID;

        case GraphicsConstants.VS_TICK:         return 0x221A;
        case GraphicsConstants.VS_RADIO:        return ACS.DOT;
        }
        
        return key;
    }
    
    private int mapVirtualKey(Key key) {
        switch (key.getKind()) {
        case NormalKey:     return key.getCharacter();
        
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
            terminal.enterPrivateMode();

            try {
                terminal.clearScreen();
                terminal.moveCursor(0, 0);
                
                final EventQueue eventQueue = EventQueue.getInstance();
                
                while (!stopRequest) {
                    final Key key = terminal.readInput();
                    if (key != null) {
                        processKeyEvent(getTopWindow(), mapVirtualKey(key), 0);
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
                terminal.exitPrivateMode();
            }
        }
    }
}
