/* class JOptionPane
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
import charva.awt.FlowLayout;
import charva.awt.Toolkit;
import charva.awt.Window;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.awt.event.KeyEvent;
import charva.awt.event.KeyListener;
import charva.awt.event.WindowEvent;
import charvax.swing.border.EmptyBorder;


/**
 * JOptionPane makes it easy to pop up a standard dialog box that prompts the
 * user for information or displays some information.
 * <p>
 * The labels of the option-buttons displayed within the popup dialog can be
 * customized by changing static variables in the JOptionPane class. Similarly,
 * "accelerator keys" can be set to have the same effect as the option-buttons.
 * For example, the following code would set the label of the OK button to "OK
 * (F1)", and set the F1 key to have the same effect as the OK button:
 * <pre>
 * JOptionPane.OK_LABEL = &quot;OK (F1)&quot;;
 * JOptionPane.OK_ACCELERATOR = KeyEvent.VK_F1;
 * </pre>
 * Note that after the buttons have been customized, they stay customized for
 * all future invocations of the JOptionPane "showXXXDialog()" methods.
 * <p>
 * The parameters to these methods follow consistent patterns:
 * <blockquote>
 * <dl>
 * <dt>parentComponent
 * <dd>Defines the Component that is to be the parent of this dialog box. It is
 * used in two ways: its screen coordinates are used in the placement of the
 * dialog box, and the dialog box inherits its foreground and background colors
 * from <code>parentComponent</code> (unless the the JOptionPane's colors have
 * been set explicitly). In general, the dialog box centered on top of
 * <code>parentComponent</code>. This parameter may be null, in which case a
 * default Frame is used as the parent, and the dialog will be centered on the
 * screen.
 * <p>
 * <dt>message
 * <dd>A descriptive message to be placed in the dialog box. In the most common
 * usage, message is just a String or String constant. However, the type of this
 * parameter is actually Object. Its interpretation depends on its type: <p/>
 * <blockquote>
 * <dl>
 * <dt>String
 * <dd>The string is displayed as a message on one line.
 * <dt>Object[]
 * <dd>An array of Objects is interpreted as a series of messages arranged in a
 * vertical stack. Each Object is converted to a String using its toString()
 * method. The result is wrapped in a JLabel and displayed.
 * </dl>
 * </blockquote>
 * <p>
 * <dt>messageType
 * <dd>Defines the style of the message. The possible values are:
 * <ul>
 * <li><code>ERROR_MESSAGE</code>
 * <li><code>INFORMATION_MESSAGE</code>
 * <li><code>WARNING_MESSAGE</code>
 * <li><code>QUESTION_MESSAGE</code>
 * <li><code>PLAIN_MESSAGE </code>
 * </ul>
 * <p>
 * <dt>optionType
 * <dd>Defines the set of option buttons that appear at the bottom of the
 * dialog box:
 * <ul>
 * <li><code>DEFAULT_OPTION</code>
 * <li><code>YES_NO_OPTION</code>
 * <li><code>YES_NO_CANCEL_OPTION</code>
 * <li><code>OK_CANCEL_OPTION</code>
 * </ul>
 * You aren't limited to this set of option buttons. You can provide any buttons
 * you want using the <code>options</code> parameter.
 * <p>
 * <dt>options
 * <dd>A more detailed description of the set of option buttons that will
 * appear at the bottom of the dialog box. The usual value for the options
 * parameter is an array of Strings. But the parameter type is an array of
 * Objects. A button is created for each object depending on its type:
 * <p>
 * <dl>
 * <dt>Component
 * <dd>The component is added to the button row directly.
 * <dt>other
 * <dd>The Object is converted to a string using its toString method and the
 * result is used to label a JButton.
 * </dl>
 * <p>
 * <dt>title
 * <dd>The title for the dialog box.
 * <p>
 * <dt>initialValue
 * <dd>The default selection (input value).
 * <p>
 * </dl>
 * </blockquote>
 */
public class JOptionPane {

    //
    // Option types
    //
    /** 
     * Type meaning Look and Feel should not supply any options -- only
     * use the options from the <code>JOptionPane</code>.
     */
    /** Type used for <code>showConfirmDialog</code>. */
    public static final int         DEFAULT_OPTION = -1;
    /** Type used for <code>showConfirmDialog</code>. */
    public static final int         YES_NO_OPTION = 0;
    /** Type used for <code>showConfirmDialog</code>. */
    public static final int         YES_NO_CANCEL_OPTION = 1;
    /** Type used for <code>showConfirmDialog</code>. */
    public static final int         OK_CANCEL_OPTION = 2;

    //
    // Return values.
    //
    /** Return value from class method if YES is chosen. */
    public static final int         YES_OPTION = 0;
    /** Return value from class method if NO is chosen. */
    public static final int         NO_OPTION = 1;
    /** Return value from class method if CANCEL is chosen. */
    public static final int         CANCEL_OPTION = 2;
    /** Return value form class method if OK is chosen. */
    public static final int         OK_OPTION = 0;
    /** Return value from class method if user closes window without selecting
     * anything, more than likely this should be treated as either a
     * <code>CANCEL_OPTION</code> or <code>NO_OPTION</code>. */
    public static final int         CLOSED_OPTION = -1;

    //
    // Message types. Used by the UI to determine what icon to display,
    // and possibly what behavior to give based on the type.
    //
    /** Used for error messages. */
    public static final int         ERROR_MESSAGE = 0;
    /** Used for information messages. */
    public static final int         INFORMATION_MESSAGE = 1;
    /** Used for warning messages. */
    public static final int         WARNING_MESSAGE = 2;
    /** Used for questions. */
    public static final int         QUESTION_MESSAGE = 3;
    /** No icon is used. */
    public static final int         PLAIN_MESSAGE = -1;
    
    private static final EmptyBorder    BORDER_MSG = 
        new EmptyBorder(0, 1, 0, 1);

    private static final EmptyBorder    BORDER_INPUT = 
        new EmptyBorder(0, 1, 1, 1);

    // Label values - can be changed to customize appearance
    private static String   YES_LABEL               = "Yes";
    private static String   NO_LABEL                = "No";
    private static String   CANCEL_LABEL            = "Cancel";
    private static String   OK_LABEL                = "OK";

    // Accelerator keystrokes - can be customized.
    private static int      YES_ACCELERATOR         = -1;
    private static int      NO_ACCELERATOR          = -1;
    private static int      CANCEL_ACCELERATOR      = -1;
    private static int      OK_ACCELERATOR          = -1;

    
    protected Object        message;
    protected int           messagetype;

    /**
     * Determines which option buttons to display (unless an array of options is
     * explicitly specified with <code>setOptions()</code>).
     */
    protected int           optiontype;

    /**
     * If true, an TextField will be displayed for the user to provide input.
     */
    protected boolean       wantsInput;

    protected String        inputValue = "";

    /**
     * Array of options to display to the user in the bottom button-panel. The
     * objects in this array can be any combination of Strings or components
     * which are subclasses of AbstractButton. Buttons are just added to the
     * bottom button-panel; Strings are wrapped in a JButton which is then added
     * to the button-panel.
     */
    protected Object[]      options;

    /**
     * Option that should be initially selected in <code>options</code>.
     */
    protected Object        initialValue;

    /**
     * The currently selected option.
     */
    protected Object        value;
    

    /**
     * Creates a JOptionPane with a test message
     */
    public JOptionPane() {
        this("This is a test message", PLAIN_MESSAGE, DEFAULT_OPTION, 
                null, null);
    }

    /**
     * Creates an instance of JOptionPane to display the specified message.
     * 
     * @param message
     *            the message to display. It can be a String or an array of
     *            Strings. If it is an array of Strings, they are stacked
     *            vertically
     */
    public JOptionPane(Object message) {
        this(message, PLAIN_MESSAGE, DEFAULT_OPTION, null, null);
    }

    /**
     * Creates an instance of JOptionPane to display the specified message
     * 
     * @param message
     *            the message to display. It can be a String or an array of
     *            Strings
     * @param messageType
     *            the type of message to be displayed:
     *            <code>ERROR_MESSAGE</code>,
     *            <code>INFORMATION_MESSAGE</code>,
     *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
     *            or <code>PLAIN_MESSAGE</code>
     */
    public JOptionPane(Object message, int messagetype) {
        this(message, messagetype, DEFAULT_OPTION, null, null);
    }

    /**
     * Creates an instance of JOptionPane to display the specified message.
     * 
     * @param message
     *            the message to display. It can be a String or an array of
     *            Strings.
     * @param messageType
     *            the type of message to be displayed:
     *            <code>ERROR_MESSAGE</code>,
     *            <code>INFORMATION_MESSAGE</code>,
     *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
     *            or <code>PLAIN_MESSAGE</code>
     * @param optiontype
     *            determines which option-buttons to display. Allowed values
     *            are: DEFAULT_OPTION, YES_NO_OPTION, YES_NO_CANCEL_OPTION, and
     *            OK_CANCEL_OPTION.
     */
    public JOptionPane(Object message, int messagetype, int optiontype) {
        this(message, messagetype, optiontype, null, null);
    }

    /**
     * Creates an instance of JOptionPane to display the specified message.
     * 
     * @param message
     *            the message to display. It can be a String or an array of
     *            Strings.
     * @param messageType
     *            the type of message to be displayed:
     *            <code>ERROR_MESSAGE</code>,
     *            <code>INFORMATION_MESSAGE</code>,
     *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
     *            or <code>PLAIN_MESSAGE</code>
     * @param optionType
     *            determines which option-buttons to display. Allowed values
     *            are: DEFAULT_OPTION, YES_NO_OPTION, YES_NO_CANCEL_OPTION, and
     *            OK_CANCEL_OPTION.
     * @param options
     *            the choices the user can select
     * @param initialValue
     *            the choice that is initially selected; if null, then nothing
     *            will be initially selected; only meaningful if "options" is
     *            used
     */
    public JOptionPane(Object message, int messageType, int optionType,
            Object[] options, Object initialValue) {
        
        this.message      = message;
        this.messagetype  = messageType;
        this.optiontype   = optionType;
        this.options      = options;
        this.initialValue = initialValue;
    }

    /**
     * If newvalue is true, a JTextField will be displayed for the user to
     * provide text input.
     */
    public void setWantsInput(boolean newvalue) {
        wantsInput = newvalue;
    }

    /**
     * Returns the value of wantsInput.
     */
    public boolean getWantsInput() {
        return wantsInput;
    }

    /**
     * Sets the default input value that is displayed to the user. Only used if
     * <code>wantsInput</code> is true.
     */
    public void setInitialSelectionValue(Object value) {
        inputValue = (String) value;
    }

    /**
     * Sets the initial value of the text field for the user to modify.
     */
    public void setInputValue(Object value) {
        inputValue = (String) value;
    }

    /**
     * Returns the value the user has input, (relevant only if wantsInput is
     * true).
     */
    public Object getInputValue() {
        return inputValue;
    }

    /**
     * Sets the options this pane displays in the button-panel at the bottom. If
     * an element in newOptions is an instance of a subclass of AbstractButton
     * (for example, a JButton), it is added directly to the pane, otherwise a
     * button is created for the element. The advantage of adding a button
     * rather than a string is that a mnemonic can be set for the button.
     */
    public void setOptions(Object[] newOptions) {
        options = newOptions;
    }

    /**
     * Returns the choices the user can make.
     */
    public Object[] getOptions() {
        return options;
    }

    /**
     * Sets the initial value that is to be enabled -- the Component that has
     * the focus when the pane is initially displayed. (NOT IMPLEMENTED YET).
     */
    public void setInitialValue(Object initialValue) {
    }

    /**
     * Returns the initial value that is to be enabled -- the Component that has
     * the focus when the pane is initially displayed. (NOT IMPLEMENTED YET).
     */
    public Object getInitialValue() {
        return null;
    }

    /**
     * Creates and returns a new JDialog for displaying the required message.
     * The dialog inherits the foreground and background colors of the
     * <code>owner</code> window and is centered on it.
     */
    public JDialog createDialog(Window owner, String title) {
        JOptionPane.Popup dlg = this.new Popup(owner, message, title);
        if (messagetype == ERROR_MESSAGE || messagetype == WARNING_MESSAGE) {
            dlg.setColors(Toolkit.getWarningColors());
        }
        
        dlg.setLocationRelativeTo(owner);
        return dlg;
    }

    /**
     * Brings up a dialog where the number of choices is dependent on the value
     * of the optiontype parameter.
     * 
     * @param parent      determines the frame in which the dialog is displayed
     * @param message     the String to display.
     * @param title       the title of the dialog
     * @param optiontype  must be YES_NO_OPTION or YES_NO_CANCEL_OPTION
     */
    public static int showConfirmDialog(Window parent, Object message,
            String title, int optiontype) {

        return showConfirmDialog(parent, message, title, optiontype, 
                PLAIN_MESSAGE);
    }

    /**
     * Brings up a dialog where the number of choices is dependent on the value
     * of the optiontype parameter.
     * 
     * @param parent      determines the frame in which the dialog is displayed
     * @param message     the String to display.
     * @param title       the title of the dialog
     * @param optiontype  must be YES_NO_OPTION or YES_NO_CANCEL_OPTION
     * @param messageType
     *            the type of message to be displayed:
     *            <code>ERROR_MESSAGE</code>,
     *            <code>INFORMATION_MESSAGE</code>,
     *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
     *            or <code>PLAIN_MESSAGE</code>
     */
    public static int showConfirmDialog(Window parent, Object message,
            String title, int optiontype, int messagetype) {

        JOptionPane pane = new JOptionPane(message, messagetype, optiontype);
        Popup dialog = (Popup) pane.createDialog(parent, title);
        dialog.show();
        return ((Integer) pane.getValue()).intValue();
    }

    /**
     * Brings up a dialog where the number of choices is dependent on the value
     * of the optiontype parameter.
     * 
     * @param message     the String to display.
     * @param title       the title of the dialog
     * @param optiontype  must be YES_NO_OPTION or YES_NO_CANCEL_OPTION
     */
    public static int showConfirmDialog(Object message,
            String title, int optiontype) {

        return showConfirmDialog(message, title, optiontype, PLAIN_MESSAGE);
    }

    /**
     * Brings up a dialog where the number of choices is dependent on the value
     * of the optiontype parameter.
     * 
     * @param message     the String to display.
     * @param title       the title of the dialog
     * @param optiontype  must be YES_NO_OPTION or YES_NO_CANCEL_OPTION
     * @param messageType
     *            the type of message to be displayed:
     *            <code>ERROR_MESSAGE</code>,
     *            <code>INFORMATION_MESSAGE</code>,
     *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
     *            or <code>PLAIN_MESSAGE</code>
     */
    public static int showConfirmDialog(Object message, String title, 
            int optiontype, int messagetype) {

        JOptionPane pane = new JOptionPane(message, messagetype, optiontype);
        Popup dialog = (Popup) pane.createDialog(null, title);
        dialog.show();
        return ((Integer) pane.getValue()).intValue();
    }

    /**
     * Brings up a dialog that allows the user to input a value
     * 
     * @param parent
     *            determines the frame in which the dialog is displayed
     * @param message
     *            the String to display
     * @param title
     *            the title of the dialog
     * @param messageType
     *            the type of message to be displayed:
     *            <code>ERROR_MESSAGE</code>,
     *            <code>INFORMATION_MESSAGE</code>,
     *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
     *            or <code>PLAIN_MESSAGE</code>
     */
    public static String showInputDialog(Window parent, Object message,
            String title, int messagetype) {

        JOptionPane pane = new JOptionPane(message, messagetype,
                OK_CANCEL_OPTION);
        pane.wantsInput = true;
        Popup dialog = (Popup) pane.createDialog(parent, title);
        dialog.show();
        int option = ((Integer) pane.getValue()).intValue();
        if (option == OK_OPTION) {
            return (String) pane.getInputValue();
        }
        
        return null;
    }

    /**
     * Brings up a dialog that allows the user to input a value
     * 
     * @param parent
     *            determines the frame in which the dialog is displayed
     * @param message
     *            the String to display
     * @param title
     *            the title of the dialog
     * @param messageType
     *            the type of message to be displayed:
     *            <code>ERROR_MESSAGE</code>,
     *            <code>INFORMATION_MESSAGE</code>,
     *            <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
     *            or <code>PLAIN_MESSAGE</code>
     */
    public static String showInputDialog(Object message, String title, 
            int messagetype) {

        JOptionPane pane = new JOptionPane(message, messagetype,
                OK_CANCEL_OPTION);
        pane.wantsInput = true;
        Popup dialog = (Popup) pane.createDialog(null, title);
        dialog.show();
        int option = ((Integer) pane.getValue()).intValue();
        if (option == OK_OPTION) {
            return (String) pane.getInputValue();
        }
        
        return null;
    }

    /**
     * Brings up a confirmation dialog titled "Message"
     */
    public static void showMessageDialog(Window parent, Object message) {
        showMessageDialog(parent, message, "Message", PLAIN_MESSAGE);
    }

    /**
     * Brings up a confirmation dialog with the specified title
     */
    public static void showMessageDialog(Window parent, Object message,
            String title, int msgtype) {

        JOptionPane pane = new JOptionPane(message, msgtype, DEFAULT_OPTION);
        JDialog dialog = pane.createDialog(parent, title);
        dialog.show();
    }

    /**
     * Brings up a confirmation dialog titled "Message"
     */
    public static void showMessageDialog(Object message) {
        showMessageDialog(message, "Message", PLAIN_MESSAGE);
    }

    /**
     * Brings up a confirmation dialog with the specified title
     */
    public static void showMessageDialog(Object message, String title, 
            int msgtype) {

        JOptionPane pane = new JOptionPane(message, msgtype, DEFAULT_OPTION);
        JDialog dialog = pane.createDialog(null, title);
        dialog.show();
    }

    /**
     * Returns the option the user has selected.
     */
    public Object getValue() {
        return value;
    }

    
    /**
     * This class used for the popup dialog that JOptionPane creates.
     */
    private class Popup extends JDialog implements ActionListener, KeyListener {
        
        private JButton     okButton;
        private JButton     yesButton;
        private JButton     noButton;
        private JButton     cancelButton;
        private JTextField  inputField = new JTextField(20);
        
        
        Popup(Window owner, Object message, String title) {
            super(owner, title);
            
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            setLayout(new BorderLayout());
            
            int alignX = JLabel.CENTER;
            if (message instanceof String) {
                String[] msgs = ((String) message).split("\n");
                if (msgs.length > 1) {
                    message = msgs;
                    alignX  = JLabel.LEFT;
                }
            }
            
            JPanel msgPanel = new JPanel();
            msgPanel.setBorder(BORDER_MSG);
            if (message instanceof String) {
                msgPanel.add(new JLabel((String)message, alignX));
                
            } else if (message instanceof String[]) {
                msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));

                String[] objects = (String[]) message;
                for (int i = 0; i < objects.length; i++) {
                    msgPanel.add(new JLabel(objects[i], alignX));
                }
            } else {
                throw new IllegalArgumentException("Illegal message type "
                        + message.getClass().getName());
            }

            if (wantsInput) {
                add(msgPanel, BorderLayout.NORTH);
                
                JPanel inputPanel = new JPanel();
                inputPanel.setBorder(BORDER_INPUT);
                inputField = new JTextField(inputValue, 20);
                inputField.addActionListener(this);
                inputPanel.add(inputField);
                add(inputPanel, BorderLayout.CENTER);
            } else {
                add(msgPanel, BorderLayout.CENTER);
            }

            add(createOptionsPanel(), BorderLayout.SOUTH);

            pack();

            // add a KeyListener in case one or more accelerators were set
            addKeyListener(this);
        }
        
        private JPanel createOptionsPanel() {
            JPanel southpan = new JPanel();
            southpan.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            if (options != null) {
                // Option buttons were explicitly specified
                for (int i = 0; i < options.length; i++) {
                    AbstractButton button = null;
                    Object opt = options[i];
                    if (opt instanceof String) {
                        button = new JButton((String) opt);
                    
                    } else if (opt instanceof AbstractButton) {
                        button = (AbstractButton) opt;
                    } else {
                        throw new IllegalArgumentException(
                                "Illegal option type "
                                + message.getClass().getName());
                    }
                    
                    button.addActionListener(this);
                    southpan.add(button);
                }
            } else {
                // Decide which option buttons to display, based on the
                // value of optiontype.
                if (optiontype == DEFAULT_OPTION
                        || optiontype == OK_CANCEL_OPTION) {

                    okButton = new JButton("OK");
                    okButton.setText(OK_LABEL);
                    okButton.addActionListener(this);
                    southpan.add(okButton);
                }
                if (optiontype == YES_NO_OPTION
                        || optiontype == YES_NO_CANCEL_OPTION) {

                    yesButton = new JButton("Yes");
                    yesButton.setText(YES_LABEL);
                    yesButton.addActionListener(this);
                    southpan.add(yesButton);
                }
                if (optiontype == YES_NO_OPTION
                        || optiontype == YES_NO_CANCEL_OPTION) {

                    noButton = new JButton("No");
                    noButton.setText(NO_LABEL);
                    noButton.addActionListener(this);
                    southpan.add(noButton);
                }
                if (optiontype == YES_NO_CANCEL_OPTION
                        || optiontype == OK_CANCEL_OPTION) {

                    cancelButton = new JButton("Cancel");
                    cancelButton.setText(CANCEL_LABEL);
                    cancelButton.addActionListener(this);
                    southpan.add(cancelButton);
                }
            }
            
            return southpan;
        }

        /**
         * Gets called when the user presses an option-button (or if ENTER is
         * pressed while focus is in the TextField).
         */
        public void actionPerformed(ActionEvent e) {
            if (wantsInput) {
                inputValue = inputField.getText();
            }

            if (options != null) {
                // Options were specified explicitly.
                // So ignore ENTER if pressed while focus is in textfield.
                if (e.getSource() == inputField) {
                    return;
                }

                AbstractButton source = (AbstractButton) e.getSource();
                String srcText = source.getText();
                for (int i = 0; i < options.length; i++) {
                    Object opt = options[i];
                    if (opt instanceof String && srcText.equals(opt)) {
                        value = opt;
                        break;
                    
                    } else if (source == opt) {
                        value = source;
                        break;
                    }
                }
            } else {
                // Options were not specified explicitly.
                Object source = e.getSource();
                if (source == okButton || source == inputField) {
                    value = SwingUtilities.valueOf(JOptionPane.OK_OPTION);
                
                } else if (source == yesButton) {
                    value = SwingUtilities.valueOf(JOptionPane.YES_OPTION);
                
                } else if (source == noButton) {
                    value = SwingUtilities.valueOf(JOptionPane.NO_OPTION);
                
                } else if (source == cancelButton) {
                    value = SwingUtilities.valueOf(JOptionPane.CANCEL_OPTION);
                }
            }

            hide();
        }

        protected void processWindowEvent(WindowEvent evt) {
            super.processWindowEvent(evt);
            
            if (evt.getID() == WindowEvent.WINDOW_CLOSING) {
                if (optiontype == DEFAULT_OPTION
                        || optiontype == YES_NO_CANCEL_OPTION
                        || optiontype == OK_CANCEL_OPTION) {

                    value = SwingUtilities.valueOf(JOptionPane.CLOSED_OPTION);
                    hide();
                }
            }
        }
        
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == OK_ACCELERATOR 
                    && (optiontype == DEFAULT_OPTION 
                            || optiontype == OK_CANCEL_OPTION)) {

                e.consume();
                value = SwingUtilities.valueOf(JOptionPane.OK_OPTION);
                inputValue = inputField.getText();
                hide();
            
            } else if (key == YES_ACCELERATOR
                    && (optiontype == YES_NO_OPTION 
                            || optiontype == YES_NO_CANCEL_OPTION)) {
                
                e.consume();
                value = SwingUtilities.valueOf(JOptionPane.YES_OPTION);
                inputValue = inputField.getText();
                hide();
            
            } else if (key == NO_ACCELERATOR
                    && (optiontype == YES_NO_OPTION 
                            || optiontype == YES_NO_CANCEL_OPTION)) {
                
                e.consume();
                value = SwingUtilities.valueOf(JOptionPane.NO_OPTION);
                hide();
            
            } else if (key == CANCEL_ACCELERATOR
                    && (optiontype == YES_NO_CANCEL_OPTION 
                            || optiontype == OK_CANCEL_OPTION)) {
                
                e.consume();
                value = SwingUtilities.valueOf(JOptionPane.CANCEL_OPTION);
                hide();
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }
}
