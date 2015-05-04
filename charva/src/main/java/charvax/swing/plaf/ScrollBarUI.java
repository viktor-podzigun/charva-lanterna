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

import charvax.swing.SwingConstants;


/**
 * Implementation of ScrollBarUI for the Basic Look and Feel
 */
public class ScrollBarUI implements SwingConstants {

//    private static final int POSITIVE_SCROLL = 1;
//    private static final int NEGATIVE_SCROLL = -1;
//
//    private static final int MIN_SCROLL = 2;
//    private static final int MAX_SCROLL = 3;
//
//    private static InputMap     inputMap;
//    private static ActionMap    actionMap;
//    
//    protected Dimension minimumThumbSize;
//    protected Dimension maximumThumbSize;
//
//    protected Color thumbHighlightColor;
//    protected Color thumbLightShadowColor;
//    protected Color thumbDarkShadowColor;
//    protected Color thumbColor;
//    protected Color trackColor;
//    protected Color trackHighlightColor;
//
//    protected JScrollBar scrollbar;
//    protected TrackListener trackListener;
//    protected ArrowButtonListener buttonListener;
//    protected ModelListener modelListener;
//
//    protected Rectangle thumbRect;
//    protected Rectangle trackRect;
//
//    //protected int trackHighlight;
//
//    protected static final int NO_HIGHLIGHT = 0;
//    protected static final int DECREASE_HIGHLIGHT = 1;
//    protected static final int INCREASE_HIGHLIGHT = 2;
//
//    protected ScrollListener scrollListener;
//    protected PropertyChangeListener propertyChangeListener;
//    //protected Timer scrollTimer;
//
////    private final static int scrollSpeedThrottle = 60; // delay in milli
////                                                        // seconds
//
//    /**
//     * Hint as to what width (when vertical) or height (when horizontal) should
//     * be.
//     */
//    private int scrollBarWidth;
//
//    protected void configureScrollBarColors() {
////        thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
////        thumbLightShadowColor = UIManager.getColor("ScrollBar.thumbShadow");
////        thumbDarkShadowColor = UIManager.getColor("ScrollBar.thumbDarkShadow");
////        thumbColor = UIManager.getColor("ScrollBar.thumb");
////        trackColor = UIManager.getColor("ScrollBar.track");
////        trackHighlightColor = UIManager.getColor("ScrollBar.trackHighlight");
//    }
//
//    public void installUI(JComponent c) {
//        scrollbar = (JScrollBar) c;
//        thumbRect = new Rectangle(0, 0, 0, 0);
//        trackRect = new Rectangle(0, 0, 0, 0);
//        installDefaults();
//        installComponents();
//        installListeners();
//        installKeyboardActions();
//    }
//
//    public void uninstallUI(JComponent c) {
//        scrollbar = (JScrollBar) c;
//        uninstallDefaults();
//        uninstallComponents();
//        uninstallListeners();
//        uninstallKeyboardActions();
//        thumbRect = null;
//        scrollbar = null;
//    }
//
//    protected void installDefaults() {
//        scrollBarWidth = 1;
//        if (scrollBarWidth <= 0) {
//            scrollBarWidth = 16;
//        }
//        minimumThumbSize = 1;
//        maximumThumbSize = 10;
//
//        trackHighlight = NO_HIGHLIGHT;
//        
//        //scrollbar.setOpaque(true);
//        configureScrollBarColors();
//    }
//
//    protected void installComponents() {
//    }
//
//    protected void uninstallComponents() {
//    }
//
//    protected void installListeners() {
//        trackListener = createTrackListener();
//        buttonListener = createArrowButtonListener();
//        modelListener = createModelListener();
//        propertyChangeListener = createPropertyChangeListener();
//
//        scrollbar.addMouseListener(trackListener);
//        scrollbar.getModel().addChangeListener(modelListener);
//        scrollbar.addPropertyChangeListener(propertyChangeListener);
//
//        scrollListener = createScrollListener();
//        //scrollTimer = new Timer(scrollSpeedThrottle, scrollListener);
//        //scrollTimer.setInitialDelay(300); // default InitialDelay?
//    }
//
//    protected void installKeyboardActions() {
//        ActionMap map = getActionMap();
//
//        SwingUtilities.replaceUIActionMap(scrollbar, map);
//        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
//        SwingUtilities.replaceUIInputMap(scrollbar, JComponent.WHEN_FOCUSED,
//                inputMap);
//    }
//
//    protected void uninstallKeyboardActions() {
//        SwingUtilities.replaceUIInputMap(scrollbar, JComponent.WHEN_FOCUSED, null);
//        SwingUtilities.replaceUIActionMap(scrollbar, null);
//    }
//
//    private InputMap getInputMap(int condition) {
//        if (condition == JComponent.WHEN_FOCUSED) {
//            if (inputMap == null) {
//                inputMap = createInputMap();
//            }
//            
//            return inputMap;
//        }
//        
//        return null;
//    }
//
//    private InputMap createInputMap() {
//        InputMap map = new UIInputMap();
//        
//        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
//            map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK), 
//                    "negativeUnitIncrement");
//            map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK), 
//                    "positiveUnitIncrement");
//        }
//
//        return map;
//    }
//
//    private ActionMap getActionMap() {
//        if (actionMap == null) {
//            actionMap = createActionMap();
//        }
//        
//        return actionMap;
//    }
//
//    private ActionMap createActionMap() {
//        ActionMap map = new UIActionMap();
//        map.put("positiveUnitIncrement", 
//                new SharedActionScroller(POSITIVE_SCROLL, false));
//        
//        map.put("positiveBlockIncrement", 
//                new SharedActionScroller(POSITIVE_SCROLL, true));
//        
//        map.put("negativeUnitIncrement", 
//                new SharedActionScroller(NEGATIVE_SCROLL, false));
//        
//        map.put("negativeBlockIncrement", 
//                new SharedActionScroller(NEGATIVE_SCROLL, true));
//        
//        map.put("minScroll", new SharedActionScroller(MIN_SCROLL, true));
//        map.put("maxScroll", new SharedActionScroller(MAX_SCROLL, true));
//        return map;
//    }
//
//    protected void uninstallListeners() {
//        //scrollTimer.stop();
//        //scrollTimer = null;
//
//        scrollbar.getModel().removeChangeListener(modelListener);
//        scrollbar.removeMouseListener(trackListener);
//        scrollbar.removePropertyChangeListener(propertyChangeListener);
//    }
//
//    protected void uninstallDefaults() {
//    }
//
//    protected TrackListener createTrackListener() {
//        return new TrackListener();
//    }
//
//    protected ArrowButtonListener createArrowButtonListener() {
//        return new ArrowButtonListener();
//    }
//
//    protected ModelListener createModelListener() {
//        return new ModelListener();
//    }
//
//    protected ScrollListener createScrollListener() {
//        return new ScrollListener();
//    }
//
//    protected PropertyChangeListener createPropertyChangeListener() {
//        return new PropertyChangeHandler();
//    }
//
//    /*
//     * Method for scrolling by a block increment. Added for mouse wheel
//     * scrolling support, RFE 4202656.
//     */
//    static void scrollByBlock(JScrollBar scrollbar, int direction) {
//        // This method is called from BasicScrollPaneUI to implement wheel
//        // scrolling, and also from scrollByBlock().
//        int oldValue = scrollbar.getValue();
//        int blockIncrement = scrollbar.getBlockIncrement(direction);
//        int delta = blockIncrement * (direction > 0 ? +1 : -1);
//
//        scrollbar.setValue(oldValue + delta);
//    }
//
//    protected void scrollByBlock(int direction) {
//        scrollByBlock(scrollbar, direction);
////        trackHighlight = (direction > 0 ? 
////                INCREASE_HIGHLIGHT : DECREASE_HIGHLIGHT);
//        scrollbar.repaint();
//    }
//
//    /*
//     * Method for scrolling by a unit increment. Added for mouse wheel scrolling
//     * support, RFE 4202656.
//     */
//    static void scrollByUnits(JScrollBar scrollbar, int direction, int units) {
//        // This method is called from BasicScrollPaneUI to implement wheel
//        // scrolling, as well as from scrollByUnit().
//        int delta = units;
//        if (direction > 0) {
//            delta *= scrollbar.getUnitIncrement(direction);
//        } else {
//            delta *= -scrollbar.getUnitIncrement(direction);
//        }
//
//        int oldValue = scrollbar.getValue();
//        int newValue = oldValue + delta;
//
//        // Check for overflow.
//        if (delta > 0 && newValue < oldValue) {
//            newValue = scrollbar.getMaximum();
//        } else if (delta < 0 && newValue > oldValue) {
//            newValue = scrollbar.getMinimum();
//        }
//        scrollbar.setValue(newValue);
//    }
//
//    protected void scrollByUnit(int direction) {
//        scrollByUnits(scrollbar, direction, 1);
//    }
//
//    
//    /**
//     * A listener to listen for model changes.
//     * 
//     */
//    protected class ModelListener implements ChangeListener {
//        public void stateChanged(ChangeEvent e) {
//            layoutContainer(scrollbar);
//        }
//    }
//
//    
//    /**
//     * Listener for cursor keys.
//     */
//    protected class ArrowButtonListener extends MouseAdapter {
//        // Because we are handling both mousePressed and Actions
//        // we need to make sure we don't fire under both conditions.
//        // (keyfocus on scrollbars causes action without mousePress
//        boolean handledEvent;
//
//        public void mousePressed(MouseEvent e) {
//            if (!scrollbar.isEnabled()) {
//                return;
//            }
//            // not an unmodified left mouse button
//            // if(e.getModifiers() != InputEvent.BUTTON1_MASK) {return; }
//            if (!SwingUtilities.isLeftMouseButton(e)) {
//                return;
//            }
//
//            int direction = (e.getSource() == incrButton) ? 1 : -1;
//
//            scrollByUnit(direction);
//            scrollTimer.stop();
//            scrollListener.setDirection(direction);
//            scrollListener.setScrollByBlock(false);
//            scrollTimer.start();
//
//            handledEvent = true;
//            if (!scrollbar.hasFocus() && scrollbar.isRequestFocusEnabled()) {
//                scrollbar.requestFocus();
//            }
//        }
//
//        public void mouseReleased(MouseEvent e) {
//            scrollTimer.stop();
//            handledEvent = false;
//            scrollbar.setValueIsAdjusting(false);
//        }
//    }
//
//    /**
//     * Listener for scrolling events initiated in the <code>ScrollPane</code>.
//     */
//    protected class ScrollListener implements ActionListener {
//        int direction = +1;
//        boolean useBlockIncrement;
//
//        public ScrollListener() {
//            direction = +1;
//            useBlockIncrement = false;
//        }
//
//        public ScrollListener(int dir, boolean block) {
//            direction = dir;
//            useBlockIncrement = block;
//        }
//
//        public void setDirection(int direction) {
//            this.direction = direction;
//        }
//
//        public void setScrollByBlock(boolean block) {
//            this.useBlockIncrement = block;
//        }
//
//        public void actionPerformed(ActionEvent e) {
//            if (useBlockIncrement) {
//                scrollByBlock(direction);
//                // Stop scrolling if the thumb catches up with the mouse
//                if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
//                    if (direction > 0) {
//                        if (getThumbBounds().y + getThumbBounds().height >= trackListener.currentMouseY)
//                            ((Timer) e.getSource()).stop();
//                    } else if (getThumbBounds().y <= trackListener.currentMouseY) {
//                        ((Timer) e.getSource()).stop();
//                    }
//                } else {
//                    if (direction > 0) {
//                        if (getThumbBounds().x + getThumbBounds().width >= trackListener.currentMouseX)
//                            ((Timer) e.getSource()).stop();
//                    } else if (getThumbBounds().x <= trackListener.currentMouseX) {
//                        ((Timer) e.getSource()).stop();
//                    }
//                }
//            } else {
//                scrollByUnit(direction);
//            }
//
//            if (direction > 0
//                    && scrollbar.getValue() + scrollbar.getVisibleAmount() >= scrollbar
//                            .getMaximum())
//                ((Timer) e.getSource()).stop();
//            else if (direction < 0
//                    && scrollbar.getValue() <= scrollbar.getMinimum())
//                ((Timer) e.getSource()).stop();
//        }
//    }
//
//    public class PropertyChangeHandler implements PropertyChangeListener {
//        public void propertyChange(PropertyChangeEvent e) {
//            String propertyName = e.getPropertyName();
//
//            if ("model".equals(propertyName)) {
//                BoundedRangeModel oldModel = (BoundedRangeModel) e
//                        .getOldValue();
//                BoundedRangeModel newModel = (BoundedRangeModel) e
//                        .getNewValue();
//                oldModel.removeChangeListener(modelListener);
//                newModel.addChangeListener(modelListener);
//                scrollbar.repaint();
//                scrollbar.revalidate();
//            } else if ("orientation".equals(propertyName)) {
//                Integer orient = (Integer) e.getNewValue();
//
//                if (scrollbar.getComponentOrientation().isLeftToRight()) {
//                    if (incrButton instanceof BasicArrowButton) {
//                        ((BasicArrowButton) incrButton).setDirection(orient
//                                .intValue() == HORIZONTAL ? EAST : SOUTH);
//                    }
//                    if (decrButton instanceof BasicArrowButton) {
//                        ((BasicArrowButton) decrButton).setDirection(orient
//                                .intValue() == HORIZONTAL ? WEST : NORTH);
//                    }
//                } else {
//                    if (incrButton instanceof BasicArrowButton) {
//                        ((BasicArrowButton) incrButton).setDirection(orient
//                                .intValue() == HORIZONTAL ? WEST : SOUTH);
//                    }
//                    if (decrButton instanceof BasicArrowButton) {
//                        ((BasicArrowButton) decrButton).setDirection(orient
//                                .intValue() == HORIZONTAL ? EAST : NORTH);
//                    }
//                }
//            } else if ("componentOrientation".equals(propertyName)) {
//                ComponentOrientation co = scrollbar.getComponentOrientation();
//                incrButton.setComponentOrientation(co);
//                decrButton.setComponentOrientation(co);
//
//                if (scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
//                    if (co.isLeftToRight()) {
//                        if (incrButton instanceof BasicArrowButton) {
//                            ((BasicArrowButton) incrButton).setDirection(EAST);
//                        }
//                        if (decrButton instanceof BasicArrowButton) {
//                            ((BasicArrowButton) decrButton).setDirection(WEST);
//                        }
//                    } else {
//                        if (incrButton instanceof BasicArrowButton) {
//                            ((BasicArrowButton) incrButton).setDirection(WEST);
//                        }
//                        if (decrButton instanceof BasicArrowButton) {
//                            ((BasicArrowButton) decrButton).setDirection(EAST);
//                        }
//                    }
//                }
//
//                InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
//                SwingUtilities.replaceUIInputMap(scrollbar,
//                        JComponent.WHEN_FOCUSED, inputMap);
//            }
//        }
//    }
//
//    /**
//     * Used for scrolling the scrollbar.
//     */
//    private static class SharedActionScroller extends AbstractAction {
//        private int dir;
//        private boolean block;
//
//        SharedActionScroller(int dir, boolean block) {
//            this.dir = dir;
//            this.block = block;
//        }
//
//        public void actionPerformed(ActionEvent e) {
//            JScrollBar scrollBar = (JScrollBar) e.getSource();
//            if (dir == NEGATIVE_SCROLL || dir == POSITIVE_SCROLL) {
//                int amount;
//                // Don't use the BasicScrollBarUI.scrollByXXX methods as we
//                // don't want to use an invokeLater to reset the trackHighlight
//                // via an invokeLater
//                if (block) {
//                    if (dir == NEGATIVE_SCROLL) {
//                        amount = -1 * scrollBar.getBlockIncrement(-1);
//                    } else {
//                        amount = scrollBar.getBlockIncrement(1);
//                    }
//                } else {
//                    if (dir == NEGATIVE_SCROLL) {
//                        amount = -1 * scrollBar.getUnitIncrement(-1);
//                    } else {
//                        amount = scrollBar.getUnitIncrement(1);
//                    }
//                }
//                scrollBar.setValue(scrollBar.getValue() + amount);
//            } else if (dir == MIN_SCROLL) {
//                scrollBar.setValue(scrollBar.getMinimum());
//            } else if (dir == MAX_SCROLL) {
//                scrollBar.setValue(scrollBar.getMaximum());
//            }
//        }
//    }
}
