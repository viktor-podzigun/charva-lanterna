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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.Rectangle;
import charva.awt.event.ActionEvent;
import charva.awt.event.FocusEvent;
import charva.awt.event.FocusListener;
import charva.awt.event.InputEvent;
import charva.awt.event.KeyEvent;
import charva.awt.event.MouseEvent;
import charva.awt.event.MouseListener;
import charvax.swing.AbstractAction;
import charvax.swing.ActionMap;
import charvax.swing.InputMap;
import charvax.swing.JComponent;
import charvax.swing.JList;
import charvax.swing.KeyStroke;
import charvax.swing.ListModel;
import charvax.swing.ListSelectionModel;
import charvax.swing.SwingUtilities;
import charvax.swing.event.ListDataEvent;
import charvax.swing.event.ListDataListener;
import charvax.swing.event.ListSelectionEvent;
import charvax.swing.event.ListSelectionListener;


/**
 * JList UI behavior.
 */
public class ListUI {

    protected JList                     list;

    // Listeners that this UI attaches to the JList
    protected FocusListener             focusListener;
    protected MouseListener             mouseListener;
    protected ListSelectionListener     listSelectionListener;
    protected ListDataListener          listDataListener;
    protected PropertyChangeListener    propertyChangeListener;

    private static InputMap             inputMap;
    private static ActionMap            actionMap;
    
    
    /**
     * Selected the previous row and force it to be visible.
     * 
     * @see JList#ensureIndexIsVisible
     */
    protected void selectPreviousIndex() {
        int s = list.getSelectedIndex();
        if (s > 0) {
            s -= 1;
            list.setSelectedIndex(s);
            list.ensureIndexIsVisible(s);
        }
    }

    /**
     * Selected the previous row and force it to be visible.
     * 
     * @see JList#ensureIndexIsVisible
     */
    protected void selectNextIndex() {
        int s = list.getSelectedIndex();
        if ((s + 1) < list.getModel().getSize()) {
            s += 1;
            list.setSelectedIndex(s);
            list.ensureIndexIsVisible(s);
        }
    }

    /**
     * Registers the keyboard bindings on the <code>JList</code> that the
     * <code>BasicListUI</code> is associated with. This method is called at
     * installUI() time.
     * 
     * @see #installUI
     */
    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED,
                getInputMap(JComponent.WHEN_FOCUSED));

        SwingUtilities.replaceUIActionMap(list, getActionMap());
    }

    InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_FOCUSED) {
            if (inputMap == null) {
                inputMap = createInputMap();
            }
            
            return inputMap;
        }
        
        return null;
    }
    
    InputMap createInputMap() {
        InputMap map = new UIInputMap();
        
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), 
                "selectPreviousRow");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK), 
                "selectPreviousRowExtendSelection");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), 
                "selectNextRow");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK), 
                "selectNextRowExtendSelection");
        
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), 
                "selectFirstRow");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.SHIFT_MASK), 
                "selectFirstRowExtendSelection");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), 
                "selectLastRow");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, InputEvent.SHIFT_MASK), 
                "selectLastRowExtendSelection");
        
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), 
                "scrollUp");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.SHIFT_MASK), 
                "scrollUpExtendSelection");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), 
                "scrollDown");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.SHIFT_MASK), 
                "scrollDownExtendSelection");
        
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK), 
                "selectAll");
        
        return map;
    }

    ActionMap getActionMap() {
        if (actionMap == null) {
            actionMap = createActionMap();
        }
        
        return actionMap;
    }

    ActionMap createActionMap() {
        ActionMap map = new UIActionMap();
        
        map.put("selectPreviousRow", new IncrementLeadSelectionAction(
                "selectPreviousRow", CHANGE_SELECTION, -1));
        map.put("selectPreviousRowExtendSelection", new IncrementLeadSelectionAction(
                "selectPreviousRowExtendSelection", EXTEND_SELECTION, -1));
        map.put("selectNextRow", new IncrementLeadSelectionAction(
                "selectNextRow", CHANGE_SELECTION, 1));
        map.put("selectNextRowExtendSelection", new IncrementLeadSelectionAction(
                "selectNextRowExtendSelection", EXTEND_SELECTION, 1));
        
        map.put("selectFirstRow", new HomeAction(
                "selectFirstRow", CHANGE_SELECTION));
        map.put("selectFirstRowExtendSelection", new HomeAction(
                "selectFirstRowExtendSelection", EXTEND_SELECTION));
        map.put("selectLastRow", new EndAction(
                "selectLastRow", CHANGE_SELECTION));
        map.put("selectLastRowExtendSelection", new EndAction(
                "selectLastRowExtendSelection", EXTEND_SELECTION));
        
        map.put("scrollUp", new PageUpAction(
                "scrollUp", CHANGE_SELECTION));
        map.put("scrollUpExtendSelection", new PageUpAction(
                "scrollUpExtendSelection", EXTEND_SELECTION));
        map.put("scrollDown", new PageDownAction(
                "scrollDown", CHANGE_SELECTION));
        map.put("scrollDownExtendSelection", new PageDownAction(
                "scrollDownExtendSelection", EXTEND_SELECTION));
        
        map.put("selectAll", new SelectAllAction(
                "selectAll"));
        
        return map;
    }

    /**
     * Unregisters keyboard actions installed from
     * <code>installKeyboardActions</code>. This method is called at
     * uninstallUI() time - subclassess should ensure that all of the keyboard
     * actions registered at installUI time are removed here.
     * 
     * @see #installUI
     */
    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(list, null);
        SwingUtilities.replaceUIInputMap(list, JComponent.WHEN_FOCUSED, null);
    }

    /**
     * Create and install the listeners for the JList, its model, and its
     * selectionModel. This method is called at installUI() time.
     * 
     * @see #installUI
     * @see #uninstallListeners
     */
    protected void installListeners() {
        focusListener           = createFocusListener();
        mouseListener           = createMouseListener();
        propertyChangeListener  = createPropertyChangeListener();
        listSelectionListener   = createListSelectionListener();
        listDataListener        = createListDataListener();

        list.addFocusListener(focusListener);
        list.addMouseListener(mouseListener);
        list.addPropertyChangeListener(propertyChangeListener);

        ListModel model = list.getModel();
        if (model != null)
            model.addListDataListener(listDataListener);

        ListSelectionModel selectionModel = list.getSelectionModel();
        if (selectionModel != null)
            selectionModel.addListSelectionListener(listSelectionListener);
    }

    /**
     * Remove the listeners for the JList, its model, and its selectionModel.
     * All of the listener fields, are reset to null here. This method is called
     * at uninstallUI() time, it should be kept in sync with installListeners.
     * 
     * @see #uninstallUI
     * @see #installListeners
     */
    protected void uninstallListeners() {
        list.removeFocusListener(focusListener);
        list.removeMouseListener(mouseListener);
        list.removePropertyChangeListener(propertyChangeListener);

        ListModel model = list.getModel();
        if (model != null)
            model.removeListDataListener(listDataListener);

        ListSelectionModel selectionModel = list.getSelectionModel();
        if (selectionModel != null)
            selectionModel.removeListSelectionListener(listSelectionListener);

        focusListener           = null;
        mouseListener           = null;
        listSelectionListener   = null;
        listDataListener        = null;
        propertyChangeListener  = null;
    }

    /**
     * Initialize JList properties, e.g. font, foreground, and background, and
     * add the CellRendererPane. The font, foreground, and background properties
     * are only set if their current value is either null or a UIResource, other
     * properties are set if the current value is null.
     * 
     * @see #uninstallDefaults
     * @see #installUI
     */
    protected void installDefaults() {
//        columnCount = 1;
//
//        list.setLayout(null);
//
//        LookAndFeel.installBorder(list, "List.border");
//
//        LookAndFeel.installColorsAndFont(list, "List.background",
//                "List.foreground", "List.font");
//
//        if (list.getCellRenderer() == null) {
//            list.setCellRenderer((ListCellRenderer) (UIManager
//                    .get("List.cellRenderer")));
//        }
//
//        Color sbg = list.getSelectionBackground();
//        if (sbg == null || sbg instanceof UIResource) {
//            list.setSelectionBackground(UIManager
//                    .getColor("List.selectionBackground"));
//        }
//
//        Color sfg = list.getSelectionForeground();
//        if (sfg == null || sfg instanceof UIResource) {
//            list.setSelectionForeground(UIManager
//                    .getColor("List.selectionForeground"));
//        }
    }

    /**
     * Set the JList properties that haven't been explicitly overridden to null.
     * A property is considered overridden if its current value is not a
     * UIResource.
     * 
     * @see #installDefaults
     * @see #uninstallUI
     */
    protected void uninstallDefaults() {
//        LookAndFeel.uninstallBorder(list);
//        if (list.getFont() instanceof UIResource) {
//            list.setFont(null);
//        }
//        if (list.getForeground() instanceof UIResource) {
//            list.setForeground(null);
//        }
//        if (list.getBackground() instanceof UIResource) {
//            list.setBackground(null);
//        }
//        if (list.getSelectionBackground() instanceof UIResource) {
//            list.setSelectionBackground(null);
//        }
//        if (list.getSelectionForeground() instanceof UIResource) {
//            list.setSelectionForeground(null);
//        }
    }

    /**
     * Initializes <code>this.list</code> by calling
     * <code>installDefaults()</code>, <code>installListeners()</code>,
     * and <code>installKeyboardActions()</code> in order.
     * 
     * @see #installDefaults
     * @see #installListeners
     * @see #installKeyboardActions
     */
    public void installUI(JComponent c) {
        list = (JList) c;

        installDefaults();
        installListeners();
        installKeyboardActions();
    }

    /**
     * Uninitializes <code>this.list</code> by calling
     * <code>uninstallListeners()</code>,
     * <code>uninstallKeyboardActions()</code>, and
     * <code>uninstallDefaults()</code> in order. Sets this.list to null.
     * 
     * @see #uninstallListeners
     * @see #uninstallKeyboardActions
     * @see #uninstallDefaults
     */
    public void uninstallUI(JComponent c) {
        uninstallListeners();
        uninstallDefaults();
        uninstallKeyboardActions();

        list = null;
    }

    /**
     * Convert a point in <code>JList</code> coordinates to the closest index
     * of the cell at that location. To determine if the cell actually
     * contains the specified location use a combination of this method and
     * <code>getCellBounds</code>.  Returns -1 if the model is empty.
     *
     * @return The index of the cell at location, or -1.
     * @see ListUI#locationToIndex
     */
    public int locationToIndex(JList list, Point location) {
        int x = location.x;
        int y = location.y;
        return convertLocationToRow(x, y, true);
    }

    /**
     * @return The origin of the index'th cell, null if index is invalid.
     * @see ListUI#indexToLocation
     */
    public Point indexToLocation(JList list, int index) {
        Rectangle rect = getCellBounds(list, index, index);
        return (rect != null ? new Point(rect.x, rect.y) : null);
    }

    /**
     * @return The bounds of the index'th cell.
     * @see ListUI#getCellBounds
     */
    public Rectangle getCellBounds(JList list, int index0, int index1) {
        int minIndex = Math.min(index0, index1);
        int maxIndex = Math.max(index0, index1);

        if (minIndex >= list.getModel().getSize()) {
            return null;
        }

        Rectangle minBounds = getCellBounds(list, minIndex);
        if (minBounds == null) {
            return null;
        }
        
        if (minIndex == maxIndex) {
            return minBounds;
        }
        
        Rectangle maxBounds = getCellBounds(list, maxIndex);
        if (maxBounds != null) {
            minBounds.add(maxBounds);
        }
        
        return minBounds;
    }
    
    /**
     * Gets the bounds of the specified model index, returning the resulting
     * bounds, or null if <code>index</code> is not valid.
     */
    private Rectangle getCellBounds(JList list, int index) {
        if (index == -1) {
            return null;
        }

        Insets insets = list.getInsets();
        int x = insets.left;
        int w = list.getWidth() - (insets.left + insets.right);
        int y = insets.top + index;
        int h = 1;
        
        return new Rectangle(x, y, w, h);
    }

    /**
     * Returns the row at location x/y.
     *
     * @param closest If true and the location doesn't exactly match a
     *                particular location, this will return the closest row.
     */
    private int convertLocationToRow(int x, int y0, boolean closest) {
        int size = list.getModel().getSize();
        if (size <= 0) {
            return -1;
        }
        
        Insets insets = list.getInsets();
        int row = (y0 - insets.top);
        if (closest) {
            if (row < 0) {
                row = 0;
            } else if (row >= size) {
                row = size - 1;
            }
        }
        
        return row;
    }

    /**
     * Mouse input, and focus handling for JList. An instance of this class is
     * added to the appropriate charva.awt.Component lists at installUI() time.
     * Note keyboard input is handled with JComponent KeyboardActions, see
     * installKeyboardActions().
     * 
     * @see #createMouseListener
     * @see #installKeyboardActions
     * @see #installUI
     */
    private class MouseHandler implements MouseListener {
        
        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (e.isConsumed()) {
                selectedOnPress = false;
                return;
            }
            selectedOnPress = true;
            adjustFocusAndSelection(e);
        }

        void adjustFocusAndSelection(MouseEvent e) {
            if (!(e.getButton() == MouseEvent.BUTTON1) || !list.isEnabled()) {
                return;
            }

            /*
             * Request focus before updating the list selection. This implies
             * that the current focus owner will see a focusLost() event before
             * the lists selection is updated IF requestFocus() is synchronous
             * (it is on Windows). See bug 4122345
             */
            if (!list.hasFocus() && list.isFocusTraversable()) {
                list.requestFocus();
            }

            int row = convertLocationToRow(e.getX(), e.getY(), true);
            if (row != -1) {
                boolean adjusting = (e.getID() == MouseEvent.MOUSE_PRESSED ? 
                        true : false);
                list.setValueIsAdjusting(adjusting);
                int anchorIndex = list.getAnchorSelectionIndex();
                if (e.isControlDown()) {
                    if (list.isSelectedIndex(row)) {
                        list.removeSelectionInterval(row, row);
                    } else {
                        list.addSelectionInterval(row, row);
                    }
                } else if (e.isShiftDown() && (anchorIndex != -1)) {
                    list.setSelectionInterval(anchorIndex, row);
                } else {
                    list.setSelectionInterval(row, row);
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (selectedOnPress) {
                if (!(e.getButton() == MouseEvent.BUTTON1)) {
                    return;
                }

                list.setValueIsAdjusting(false);
            } else {
                adjustFocusAndSelection(e);
            }
        }

        private boolean selectedOnPress;
    }

    /**
     * Creates a delegate that implements MouseListener. The delegate is
     * added to the corresponding charva.awt.Component listener lists at
     * installUI() time. Subclasses can override this method to return a custom
     * MouseListener, e.g.
     * 
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected MouseListener &lt;b&gt;createMouseListener&lt;/b&gt;() {
     *        return new MyMouseHandler();
     *    }
     *    public class MyMouseHandler extends MouseHandler {
     *        public void mouseMoved(MouseEvent e) {
     *            // do some extra work when the mouse moves
     *            super.mouseMoved(e);
     *        }
     *    }
     * }
     * </pre>
     * 
     * @see MouseHandler
     * @see #installUI
     */
    protected MouseListener createMouseListener() {
        return new MouseHandler();
    }

    /**
     * This inner class is marked &quot;public&quot; due to a compiler bug. This
     * class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of BasicTableUI.
     */
    private class FocusHandler implements FocusListener {
        
        protected void repaintCellFocus() {
            int leadIndex = list.getLeadSelectionIndex();
            if (leadIndex != -1) {
//                Rectangle r = getCellBounds(list, leadIndex, leadIndex);
//                if (r != null)
                    list.repaint();
            }
        }

        /*
         * The focusGained() focusLost() methods run when the JList focus
         * changes.
         */

        public void focusGained(FocusEvent e) {
            // hasFocus = true;
            repaintCellFocus();
        }

        public void focusLost(FocusEvent e) {
            // hasFocus = false;
            repaintCellFocus();
        }
    }

    protected FocusListener createFocusListener() {
        return new FocusHandler();
    }

    /**
     * The ListSelectionListener that's added to the JLists selection model at
     * installUI time, and whenever the JList.selectionModel property changes.
     * When the selection changes we repaint the affected rows.
     * 
     * @see #createListSelectionListener
     * @see #getCellBounds
     * @see #installUI
     */
    private class ListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {

//            Rectangle bounds = getCellBounds(list, e.getFirstIndex(), e
//                    .getLastIndex());
//
//            if (bounds != null)
                list.repaint();
        }
    }

    /**
     * Creates an instance of ListSelectionHandler that's added to the JLists by
     * selectionModel as needed. Subclasses can override this method to return a
     * custom ListSelectionListener, e.g.
     * 
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected ListSelectionListener &lt;b&gt;createListSelectionListener&lt;/b&gt;() {
     *        return new MySelectionListener();
     *    }
     *    public class MySelectionListener extends ListSelectionHandler {
     *        public void valueChanged(ListSelectionEvent e) {
     *            // do some extra work when the selection changes
     *            super.valueChange(e);
     *        }
     *    }
     * }
     * </pre>
     * 
     * @see ListSelectionHandler
     * @see #installUI
     */
    protected ListSelectionListener createListSelectionListener() {
        return new ListSelectionHandler();
    }

    private void redrawList() {
        list.invalidate();
        list.repaint();
    }

    /**
     * The ListDataListener that's added to the JLists model at installUI time,
     * and whenever the JList.model property changes.
     * 
     * @see JList#getModel
     * @see #createListDataListener
     * @see #installUI
     */
    private class ListDataHandler implements ListDataListener {
        public void intervalAdded(ListDataEvent e) {
            int minIndex = Math.min(e.getIndex0(), e.getIndex1());
            int maxIndex = Math.max(e.getIndex0(), e.getIndex1());

            /*
             * Sync the SelectionModel with the DataModel.
             */

            ListSelectionModel sm = list.getSelectionModel();
            if (sm != null) {
                sm.insertIndexInterval(minIndex, maxIndex - minIndex + 1, true);
            }

            /*
             * Repaint the entire list, from the origin of the first added cell,
             * to the bottom of the component.
             */
            redrawList();
        }

        public void intervalRemoved(ListDataEvent e) {
            /*
             * Sync the SelectionModel with the DataModel.
             */

            ListSelectionModel sm = list.getSelectionModel();
            if (sm != null) {
                sm.removeIndexInterval(e.getIndex0(), e.getIndex1());
            }

            /*
             * Repaint the entire list, from the origin of the first removed
             * cell, to the bottom of the component.
             */

            redrawList();
        }

        public void contentsChanged(ListDataEvent e) {
            redrawList();
        }
    }

    /**
     * Creates an instance of ListDataListener that's added to the JLists by
     * model as needed. Subclasses can override this method to return a custom
     * ListDataListener, e.g.
     * 
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected ListDataListener &lt;b&gt;createListDataListener&lt;/b&gt;() {
     *        return new MyListDataListener();
     *    }
     *    public class MyListDataListener extends ListDataHandler {
     *        public void contentsChanged(ListDataEvent e) {
     *            // do some extra work when the models contents change
     *            super.contentsChange(e);
     *        }
     *    }
     * }
     * </pre>
     * 
     * @see ListDataListener
     * @see JList#getModel
     * @see #installUI
     */
    protected ListDataListener createListDataListener() {
        return new ListDataHandler();
    }

    /**
     * The PropertyChangeListener that's added to the JList at installUI time.
     * When the value of a JList property that affects layout changes, we set a
     * bit in updateLayoutStateNeeded. If the JLists model changes we
     * additionally remove our listeners from the old model. Likewise for the
     * JList selectionModel.
     * 
     * @see #createPropertyChangeListener
     * @see #installUI
     */
    private class PropertyChangeHandler implements PropertyChangeListener {
        
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();

            /*
             * If the JList.model property changes, remove our listener,
             * listDataListener from the old model and add it to the new one.
             */
            if (propertyName.equals("model")) {
                ListModel oldModel = (ListModel) e.getOldValue();
                ListModel newModel = (ListModel) e.getNewValue();
                if (oldModel != null) {
                    oldModel.removeListDataListener(listDataListener);
                }
                if (newModel != null) {
                    newModel.addListDataListener(listDataListener);
                }
                redrawList();
            }

            /*
             * If the JList.selectionModel property changes, remove our
             * listener, listSelectionListener from the old selectionModel and
             * add it to the new one.
             */
            else if (propertyName.equals("selectionModel")) {
                ListSelectionModel oldModel = (ListSelectionModel)e.getOldValue();
                ListSelectionModel newModel = (ListSelectionModel)e.getNewValue();
                if (oldModel != null) {
                    oldModel.removeListSelectionListener(listSelectionListener);
                }
                if (newModel != null) {
                    newModel.addListSelectionListener(listSelectionListener);
                }
                redrawList();
            }
        }
    }

    /**
     * Creates an instance of PropertyChangeHandler that's added to the JList by
     * installUI(). Subclasses can override this method to return a custom
     * PropertyChangeListener, e.g.
     * 
     * <pre>
     * class MyListUI extends BasicListUI {
     *    protected PropertyChangeListener &lt;b&gt;createPropertyChangeListener&lt;/b&gt;() {
     *        return new MyPropertyChangeListener();
     *    }
     *    public class MyPropertyChangeListener extends PropertyChangeHandler {
     *        public void propertyChange(PropertyChangeEvent e) {
     *            if (e.getPropertyName().equals(&quot;model&quot;)) {
     *                // do some extra work when the model changes
     *            }
     *            super.propertyChange(e);
     *        }
     *    }
     * }
     * </pre>
     * 
     * @see PropertyChangeListener
     * @see #installUI
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return new PropertyChangeHandler();
    }

    // Keyboard navigation actions.
    // NOTE: DefaultListSelectionModel.setAnchorSelectionIndex and
    // DefaultListSelectionModel.setLeadSelectionIndex both force the
    // new index to be selected. Because of this not all the bindings
    // could be appropriately implemented. Specifically those that
    // change the lead/anchor without selecting are not enabled.
    // Once this has been fixed the following actions will appropriately
    // work with selectionType == CHANGE_LEAD.

    /**
     * Used by IncrementLeadSelectionAction. Indicates the action should change
     * the lead, and not select it.
     */
    //private static final int CHANGE_LEAD = 0;
    
    /**
     * Used by IncrementLeadSelectionAction. Indicates the action should change
     * the selection and lead.
     */
    private static final int CHANGE_SELECTION = 1;
    /**
     * Used by IncrementLeadSelectionAction. Indicates the action should extend
     * the selection from the anchor to the next index.
     */
    private static final int EXTEND_SELECTION = 2;

    
    /**
     * Action to increment the selection in the list up/down a row at a type.
     * This also has the option to extend the selection, or only move the lead.
     */
    private static class IncrementLeadSelectionAction extends AbstractAction {
        
        /** Amount to offset, subclasses will define what this means. */
        protected int amount;
        
        /** One of CHANGE_LEAD, CHANGE_SELECTION or EXTEND_SELECTION. */
        protected int selectionType;

        protected IncrementLeadSelectionAction(String name, int type) {
            this(name, type, -1);
        }

        protected IncrementLeadSelectionAction(String name, int type, int amount) {
            super(name);
            this.amount = amount;
            this.selectionType = type;
        }

        /**
         * Returns the next index to select. This is based on the lead selected
         * index and the <code>amount</code> ivar.
         */
        protected int getNextIndex(JList list) {
            int index = list.getLeadSelectionIndex();
            int size = list.getModel().getSize();

            if (index == -1) {
                if (size > 0) {
                    if (amount > 0) {
                        index = 0;
                    } else {
                        index = size - 1;
                    }
                }
            } else {
                index += getAmount(list);
            }
            return index;
        }

        /**
         * Returns the amount to increment by.
         */
        protected int getAmount(JList list) {
            return amount;
        }

        /**
         * Ensures the particular index is visible. This simply forwards the
         * method to list.
         */
        protected void ensureIndexIsVisible(JList list, int index) {
            list.ensureIndexIsVisible(index);
        }

        /**
         * Invokes <code>getNextIndex</code> to determine the next index to
         * select. If the index is valid (not -1 and < size of the model), this
         * will either: move the selection to the new index if the selectionType ==
         * CHANGE_SELECTION, move the lead to the new index if selectionType ==
         * CHANGE_LEAD, otherwise the selection is extend from the anchor to the
         * new index and the lead is set to the new index.
         */
        public void actionPerformed(ActionEvent e) {
            JList list = (JList) e.getSource();
            int index = getNextIndex(list);
            if (index >= 0 && index < list.getModel().getSize()) {
                ListSelectionModel lsm = list.getSelectionModel();

                if (selectionType == EXTEND_SELECTION) {
                    /*
                     * The following block is supposed to handle the case when
                     * the control modifier is used to move the lead without
                     * changing the selection. The DefaultListSelectionModel
                     * needs a new property here, to change the behavior of
                     * "setLeadSelectionIndex" so that it does not adjust the
                     * selection. Until then, this cannot be implemented
                     * properly and we will remove this code altogether to fix
                     * bug #4317662.
                     */
                    /*
                     * int anchor = lsm.getAnchorSelectionIndex(); if (anchor ==
                     * -1) { anchor = index; } list.setSelectionInterval(anchor,
                     * index); lsm.setAnchorSelectionIndex(anchor);
                     */
                    lsm.setLeadSelectionIndex(index);
                } else if (selectionType == CHANGE_SELECTION) {
                    list.setSelectedIndex(index);
                } else {
                    lsm.setLeadSelectionIndex(index);
                }
                ensureIndexIsVisible(list, index);
            }
        }
    }

    /**
     * Action to move the selection to the first item in the list.
     */
    private static class HomeAction extends IncrementLeadSelectionAction {
        protected HomeAction(String name, int type) {
            super(name, type);
        }

        protected int getNextIndex(JList list) {
            return 0;
        }
    }

    /**
     * Action to move the selection to the last item in the list.
     */
    private static class EndAction extends IncrementLeadSelectionAction {
        protected EndAction(String name, int type) {
            super(name, type);
        }

        protected int getNextIndex(JList list) {
            return list.getModel().getSize() - 1;
        }
    }

    /**
     * Action to move up one page.
     */
    private static class PageUpAction extends IncrementLeadSelectionAction {
        
        protected PageUpAction(String name, int type) {
            super(name, type);
        }

        protected int getNextIndex(JList list) {
            int index = list.getFirstVisibleIndex();
            ListSelectionModel lsm = list.getSelectionModel();

            if (lsm.getLeadSelectionIndex() == index) {
                Rectangle visRect = list.getVisibleRect();
                visRect.y = Math.max(0, visRect.y - visRect.height);
                index = list.locationToIndex(visRect.getLocation());
            }
            return index;
        }

        protected void ensureIndexIsVisible(JList list, int index) {
            Rectangle visRect = list.getVisibleRect();
            Rectangle cellBounds = list.getCellBounds(index, index);
            cellBounds.height = visRect.height;
            list.scrollRectToVisible(cellBounds);
        }
    }

    /**
     * Action to move down one page.
     */
    private static class PageDownAction extends IncrementLeadSelectionAction {
        protected PageDownAction(String name, int type) {
            super(name, type);
        }

        protected int getNextIndex(JList list) {
            int index = list.getLastVisibleIndex();
            ListSelectionModel lsm = list.getSelectionModel();

            if (index == -1) {
                // Will happen if size < viewport size.
                index = list.getModel().getSize() - 1;
            }
            if (lsm.getLeadSelectionIndex() == index) {
                Rectangle visRect = list.getVisibleRect();
                visRect.y += visRect.height + visRect.height - 1;
                index = list.locationToIndex(visRect.getLocation());
                if (index == -1) {
                    index = list.getModel().getSize() - 1;
                }
            }
            return index;
        }

        protected void ensureIndexIsVisible(JList list, int index) {
            Rectangle visRect = list.getVisibleRect();
            Rectangle cellBounds = list.getCellBounds(index, index);
            cellBounds.y = Math.max(0, cellBounds.y + cellBounds.height
                    - visRect.height);
            cellBounds.height = visRect.height;
            list.scrollRectToVisible(cellBounds);
        }
    }

    /**
     * Action to select all the items in the list.
     */
    private static class SelectAllAction extends AbstractAction {
        
        private SelectAllAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            JList list = (JList) e.getSource();
            // Select all should not alter the lead and anchor.
            // ListSelectionModel encforces the selection to the anchor/lead,
            // so it is commented out.

            // ListSelectionModel lsm = list.getSelectionModel();
            // int anchor = lsm.getAnchorSelectionIndex();
            // int lead = lsm.getLeadSelectionIndex();
            int size = list.getModel().getSize();
            if (size > 0) {
                ListSelectionModel lsm = list.getSelectionModel();
                if (lsm.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
                    if (list.getMinSelectionIndex() == -1) {
                        list.setSelectionInterval(0, 0);
                    }
                } else {
                    list.setSelectionInterval(0, size - 1);
                    list.ensureIndexIsVisible(list.getLeadSelectionIndex());
                }
            }
            // lsm.setAnchorSelectionIndex(anchor);
            // lsm.setLeadSelectionIndex(lead);
        }
    }
}
