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

package charvax.swing.plaf;

import charva.awt.Component;
import charva.awt.Dimension;
import charva.awt.Point;
import charva.awt.Rectangle;
import charva.awt.event.ActionEvent;
import charva.awt.event.InputEvent;
import charva.awt.event.KeyEvent;
import charvax.swing.AbstractAction;
import charvax.swing.ActionMap;
import charvax.swing.BoundedRangeModel;
import charvax.swing.InputMap;
import charvax.swing.JComponent;
import charvax.swing.JScrollBar;
import charvax.swing.JScrollPane;
import charvax.swing.JViewport;
import charvax.swing.KeyStroke;
import charvax.swing.ScrollPaneConstants;
import charvax.swing.Scrollable;
import charvax.swing.SwingConstants;
import charvax.swing.SwingUtilities;
import charvax.swing.event.ChangeEvent;
import charvax.swing.event.ChangeListener;


public class ScrollPaneUI implements ScrollPaneConstants {

    protected JScrollPane       scrollpane;
    protected ChangeListener    vsbChangeListener;
    protected ChangeListener    hsbChangeListener;
    protected ChangeListener    viewportChangeListener;

    private static InputMap     inputMap;
    private static ActionMap    actionMap;
    
    
    /**
     * State flag that shows whether setValue() was called from a user program
     * before the value of "extent" was set in right-to-left component
     * orientation.
     */
    private boolean setValueCalled = false;

//    public static ComponentUI createUI(JComponent x) {
//        return new ScrollPaneUI();
//    }

    protected void installDefaults(JScrollPane scrollpane) {
//        LookAndFeel.installBorder(scrollpane, "ScrollPane.border");
//        LookAndFeel.installColorsAndFont(scrollpane, "ScrollPane.background",
//                "ScrollPane.foreground", "ScrollPane.font");
//
//        Border vpBorder = scrollpane.getViewportBorder();
//        if ((vpBorder == null) || (vpBorder instanceof UIResource)) {
//            vpBorder = UIManager.getBorder("ScrollPane.viewportBorder");
//            scrollpane.setViewportBorder(vpBorder);
//        }
    }

    protected void installListeners(JScrollPane c) {
        vsbChangeListener       = createVSBChangeListener();
        hsbChangeListener       = createHSBChangeListener();
        viewportChangeListener  = createViewportChangeListener();

        JViewport  viewport = scrollpane.getViewport();
        JScrollBar vsb      = scrollpane.getVerticalScrollBar();
        JScrollBar hsb      = scrollpane.getHorizontalScrollBar();

        if (viewport != null)
            viewport.addChangeListener(viewportChangeListener);
        
        if (vsb != null)
            vsb.getModel().addChangeListener(vsbChangeListener);
        
        if (hsb != null)
            hsb.getModel().addChangeListener(hsbChangeListener);
    }

    protected void installKeyboardActions(JScrollPane c) {
        SwingUtilities.replaceUIInputMap(c,
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, 
                getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));
        
        SwingUtilities.replaceUIActionMap(c, getActionMap());
    }

    InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
            if (inputMap == null)
                inputMap = createInputMap();
            
            return inputMap;
        }
        
        return null;
    }
    
    InputMap createInputMap() {
        InputMap map = new UIInputMap();
        
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), 
                "scrollUp");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), 
                "scrollDown");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.CTRL_MASK), 
                "scrollHome");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, InputEvent.CTRL_MASK), 
                "scrollEnd");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), 
                "unitScrollUp");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), 
                "unitScrollDown");

        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK), 
                "scrollLeft");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK), 
                "scrollRight");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), 
                "unitScrollLeft");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), 
                "unitScrollRight");
        
        return map;
    }

    ActionMap getActionMap() {
        if (actionMap == null)
            actionMap = createActionMap();
        
        return actionMap;
    }

    ActionMap createActionMap() {
        ActionMap map = new UIActionMap();
        map.put("scrollUp", new ScrollAction(
                "scrollUp", SwingConstants.VERTICAL, -1, true));
        map.put("scrollDown", new ScrollAction(
                "scrollDown", SwingConstants.VERTICAL, 1, true));
        
        map.put("scrollHome", new ScrollHomeAction(
                "scrollHome"));
        map.put("scrollEnd", new ScrollEndAction(
                "scrollEnd"));
        
        map.put("unitScrollUp", new ScrollAction(
                "unitScrollUp", SwingConstants.VERTICAL, -1, false));
        map.put("unitScrollDown", new ScrollAction(
                "unitScrollDown", SwingConstants.VERTICAL, 1, false));

        map.put("scrollLeft", new ScrollAction(
                "scrollLeft", SwingConstants.HORIZONTAL, -1, true));
        map.put("scrollRight", new ScrollAction(
                "scrollRight", SwingConstants.HORIZONTAL, 1, true));
        
        map.put("unitScrollLeft", new ScrollAction(
                "unitScrollLeft", SwingConstants.HORIZONTAL, -1, false));
        map.put("unitScrollRight", new ScrollAction(
                "unitScrollRight", SwingConstants.HORIZONTAL, 1, false));
        
        return map;
    }

    public void installUI(JComponent x) {
        scrollpane = (JScrollPane) x;
        installDefaults(scrollpane);
        installListeners(scrollpane);
        installKeyboardActions(scrollpane);
    }

    protected void uninstallListeners(JComponent c) {
        JViewport  viewport = scrollpane.getViewport();
        JScrollBar vsb      = scrollpane.getVerticalScrollBar();
        JScrollBar hsb      = scrollpane.getHorizontalScrollBar();

        if (viewport != null)
            viewport.removeChangeListener(viewportChangeListener);
        
        if (vsb != null)
            vsb.getModel().removeChangeListener(vsbChangeListener);
        
        if (hsb != null)
            hsb.getModel().removeChangeListener(hsbChangeListener);

        vsbChangeListener       = null;
        hsbChangeListener       = null;
        viewportChangeListener  = null;
    }

    protected void uninstallKeyboardActions(JScrollPane c) {
        SwingUtilities.replaceUIActionMap(c, null);
        SwingUtilities.replaceUIInputMap(c, 
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);
    }

    public void uninstallUI(JComponent c) {
        uninstallListeners(scrollpane);
        uninstallKeyboardActions(scrollpane);
        scrollpane = null;
    }

    protected void syncScrollPaneWithViewport() {
        JViewport  viewport = scrollpane.getViewport();
        JScrollBar vsb      = scrollpane.getVerticalScrollBar();
        JScrollBar hsb      = scrollpane.getHorizontalScrollBar();
        JViewport  rowHead  = scrollpane.getRowHeader();
        JViewport  colHead  = scrollpane.getColumnHeader();
        boolean    ltr      = true;

        if (viewport != null) {
            Dimension extentSize = viewport.getExtentSize();
            Dimension viewSize = viewport.getViewSize();
            Point viewPosition = viewport.getViewPosition();

            if (vsb != null) {
                int extent = extentSize.height;
                int max = viewSize.height;
                int value = Math.max(0, Math.min(viewPosition.y, max - extent));
                vsb.setValues(value, extent, 0, max);
            }

            if (hsb != null) {
                int extent = extentSize.width;
                int max = viewSize.width;
                int value;

                if (ltr) {
                    value = Math.max(0, Math.min(viewPosition.x, max - extent));
                } else {
                    int currentValue = hsb.getValue();

                    /*
                     * Use a particular formula to calculate "value" until
                     * effective x coordinate is calculated.
                     */
                    if (setValueCalled
                            && ((max - currentValue) == viewPosition.x)) {
                        value = Math.max(0, Math.min(max - extent, currentValue));
                        
                        //
                        // After "extent" is set, turn setValueCalled flag off.
                        //
                        if (extent != 0) {
                            setValueCalled = false;
                        }
                    } else {
                        if (extent > max) {
                            viewPosition.x = max - extent;
                            viewport.setViewPosition(viewPosition);
                            value = 0;
                        } else {
                            /*
                             * The following line can't handle a small value of
                             * viewPosition.x like Integer.MIN_VALUE correctly
                             * because (max - extent - viewPositoiin.x) causes
                             * an overflow. As a result, value becomes zero.
                             * (e.g. setViewPosition(Integer.MAX_VALUE, ...) in
                             * a user program causes a overflow. Its expected
                             * value is (max - extent).) However, this seems a
                             * trivial bug and adding a fix makes this
                             * often-called method slow, so I'll leave it until
                             * someone claims.
                             */
                            value = Math.max(0, Math.min(max - extent, 
                                    max - extent - viewPosition.x));
                        }
                    }
                }
                hsb.setValues(value, extent, 0, max);
            }

            if (rowHead != null) {
                Point p = rowHead.getViewPosition();
                p.y = viewport.getViewPosition().y;
                p.x = 0;
                rowHead.setViewPosition(p);
            }

            if (colHead != null) {
                Point p = colHead.getViewPosition();
                if (ltr) {
                    p.x = viewport.getViewPosition().x;
                } else {
                    p.x = Math.max(0, viewport.getViewPosition().x);
                }
                
                p.y = 0;
                colHead.setViewPosition(p);
            }
        }
    }

    /**
     * Listener for viewport events.
     */
    public class ViewportChangeHandler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            syncScrollPaneWithViewport();
        }
    }

    protected ChangeListener createViewportChangeListener() {
        return new ViewportChangeHandler();
    }

    /**
     * Horizontal scrollbar listener.
     */
    public class HSBChangeListener implements ChangeListener {
        
        public void stateChanged(ChangeEvent e) {
            JViewport viewport = scrollpane.getViewport();
            if (viewport != null) {
                BoundedRangeModel model = (BoundedRangeModel)e.getSource();
                Point p = viewport.getViewPosition();
                int value = model.getValue();
                boolean isLeftToRight = true;
                
                if (isLeftToRight) {
                    p.x = value;
                } else {
                    int max = viewport.getViewSize().width;
                    int extent = viewport.getExtentSize().width;
                    int oldX = p.x;

                    /*
                     * Set new X coordinate based on "value".
                     */
                    p.x = max - extent - value;

                    /*
                     * If setValue() was called before "extent" was fixed, turn
                     * setValueCalled flag on.
                     */
                    if ((extent == 0) && (value != 0) && (oldX == max)) {
                        setValueCalled = true;
                    } else {
                        /*
                         * When a pane without a horizontal scroll bar was
                         * reduced and the bar appeared, the viewport should
                         * show the right side of the view.
                         */
                        if ((extent != 0) && (oldX < 0) && (p.x == 0)) {
                            p.x += value;
                        }
                    }
                }
                viewport.setViewPosition(p);
            }
        }
    }

    protected ChangeListener createHSBChangeListener() {
        return new HSBChangeListener();
    }

    /**
     * Vertical scrollbar listener.
     */
    public class VSBChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            JViewport viewport = scrollpane.getViewport();
            if (viewport != null) {
                BoundedRangeModel model = (BoundedRangeModel) (e.getSource());
                Point p = viewport.getViewPosition();
                p.y = model.getValue();
                viewport.setViewPosition(p);
            }
        }
    }

    protected ChangeListener createVSBChangeListener() {
        return new VSBChangeListener();
    }

    /**
     * Action to scroll left/right/up/down.
     */
    private static class ScrollAction extends AbstractAction {
        /** Direction to scroll. */
        protected int orientation;
        /** 1 indicates scroll down, -1 up. */
        protected int direction;
        /** True indicates a block scroll, otherwise a unit scroll. */
        private boolean block;

        protected ScrollAction(String name, int orientation, int direction,
                boolean block) {
            
            super(name);
            this.orientation = orientation;
            this.direction   = direction;
            this.block       = block;
        }

        public void actionPerformed(ActionEvent e) {
            JScrollPane scrollpane = (JScrollPane) e.getSource();
            JViewport vp = scrollpane.getViewport();
            Component view;
            if (vp != null && (view = vp.getView()) != null) {
                Rectangle visRect = vp.getViewRect();
                Dimension vSize = view.getSize();
                int amount;

                if (view instanceof Scrollable) {
                    if (block) {
                        amount = 
                            ((Scrollable)view).getScrollableBlockIncrement(
                                    visRect, orientation, direction);
                    } else {
                        amount = 
                            ((Scrollable)view).getScrollableUnitIncrement(
                                    visRect, orientation, direction);
                    }
                } else {
                    if (block) {
                        if (orientation == SwingConstants.VERTICAL) {
                            amount = visRect.height;
                        } else {
                            amount = visRect.width;
                        }
                    } else {
                        amount = 10;
                    }
                }
                if (orientation == SwingConstants.VERTICAL) {
                    visRect.y += (amount * direction);
                    if ((visRect.y + visRect.height) > vSize.height) {
                        visRect.y = Math.max(0, vSize.height - visRect.height);
                    } else if (visRect.y < 0) {
                        visRect.y = 0;
                    }
                } else {
                    boolean isLeftToRight = true;
                    
                    if (isLeftToRight) {
                        visRect.x += (amount * direction);
                        if ((visRect.x + visRect.width) > vSize.width) {
                            visRect.x = Math
                                    .max(0, vSize.width - visRect.width);
                        } else if (visRect.x < 0) {
                            visRect.x = 0;
                        }
                    } else {
                        visRect.x -= (amount * direction);
                        if (visRect.width > vSize.width) {
                            visRect.x = vSize.width - visRect.width;
                        } else {
                            visRect.x = Math.max(0, Math.min(vSize.width
                                    - visRect.width, visRect.x));
                        }
                    }
                }
                
                vp.setViewPosition(visRect.getLocation());
            }
        }
    }

    /**
     * Action to scroll to x,y location of 0,0.
     */
    private static class ScrollHomeAction extends AbstractAction {
        protected ScrollHomeAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            JScrollPane scrollpane = (JScrollPane) e.getSource();
            JViewport vp = scrollpane.getViewport();
            Component view;
            if (vp != null && (view = vp.getView()) != null) {
                boolean isLeftToRight = true;
                if (isLeftToRight) {
                    vp.setViewPosition(new Point(0, 0));
                } else {
                    Rectangle visRect = vp.getViewRect();
                    Rectangle bounds = view.getBounds();
                    vp.setViewPosition(new Point(bounds.width - visRect.width,
                            0));
                }
            }
        }
    }

    /**
     * Action to scroll to last visible location.
     */
    private static class ScrollEndAction extends AbstractAction {
        protected ScrollEndAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            JScrollPane scrollpane = (JScrollPane) e.getSource();
            JViewport vp = scrollpane.getViewport();
            Component view;
            if (vp != null && (view = vp.getView()) != null) {
                Rectangle visRect = vp.getViewRect();
                Rectangle bounds = view.getBounds();
                boolean isLeftToRight = true;
                
                if (isLeftToRight) {
                    vp.setViewPosition(new Point(bounds.width - visRect.width,
                            bounds.height - visRect.height));
                } else {
                    vp.setViewPosition(new Point(0, 
                            bounds.height - visRect.height));
                }
            }
        }
    }
}
