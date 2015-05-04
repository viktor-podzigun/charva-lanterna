/* class JFileChooser
 *
 * Copyright (C) 2003  R M Pitman
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

/*
 * Modified Jul 14, 2003 by Tadpole Computer, Inc.
 * Modifications Copyright 2003 by Tadpole Computer, Inc.
 *
 * Modifications are hereby licensed to all parties at no charge under
 * the same terms as the original.
 *
 * Fixed bug to allow save dialog to work when files do not exist.
 * Added setSelectedFile method.  Fixed fileSelectionMode to mean
 * that when FILES_ONLY, entry of a directory name in the textfield now
 * causes the appropriate setCurrentDirectory() call.
 */

package charvax.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventListener;
import java.util.Iterator;
import java.util.TreeSet;
import charva.awt.BorderLayout;
import charva.awt.Dimension;
import charva.awt.FlowLayout;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.Window;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.awt.event.KeyEvent;
import charva.awt.event.KeyListener;
import charvax.swing.border.EmptyBorder;
import charvax.swing.border.TitledBorder;
import charvax.swing.event.ListSelectionEvent;
import charvax.swing.event.ListSelectionListener;


/**
 * <p>The JFileChooser class displays a dialog from which the user can choose
 * a file.  The dialog is always modal (i.e. the user cannot interact
 * with any other windows until he closes the dialog).</p>
 * <p/>
 * <p>The dialog is displayed by calling its showDialog() method, which blocks
 * until the dialog is closed (by the user pressing the Approve or Cancel
 * buttons, or by pressing ENTER while the focus is in the "Filename" field).
 * After the dialog has been closed, the program can find out what
 * File was selected by calling the getSelectedFile() method.</p>
 * <p/>
 * The labels of the buttons that are displayed in the JFileChooser
 * can be customized by changing the following static variables:
 * <ul>
 * <li> <code>PARENT_DIRECTORY_LABEL</code>
 * <li> <code>NEW_DIRECTORY_LABEL</code>
 * <li> <code>APPROVE_LABEL</code>
 * <li> <code>CANCEL_LABEL</code>
 * </ul>
 * <p>"Accelerator keys" can also be set for the buttons. For example,
 * to set the F1 key to have the same effect as pressing the CANCEL
 * button, call the following code before using the JFileChooser:</p>
 * <pre>
 * 	JFileChooser.CANCEL_LABEL = "Cancel (F1)";
 * 	JFileChooser.CANCEL_ACCELERATOR = KeyEvent.VK_F1;
 * </pre>
 * Note that after the buttons have been customized, they stay customized
 * for all future invocations of JFileChooser (until they are re-customized
 * to some other value).
 */
class JFileChooser extends JComponent {

    //TODO: Implement this class

    /**
     * Constructs a JFileChooser pointing to the user's home directory.
     */
    public JFileChooser() {
        this((File)null);
    }

    /**
     * Constructs a JFileChooser pointing to the specified directory.
     * Passing in a null parameter causes the JFileChooser to point to
     * the user's home directory.
     */
    public JFileChooser(File currentDirectory) {
        setCurrentDirectory(currentDirectory);
    }

    /**
     * Constructs a JFileChooser with the specified pathname. Passing a value
     * of <code>null</code> causes the file chooser to point to the user's
     * home directory.
     */
    public JFileChooser(String currentDirectoryPath) {
        if (currentDirectoryPath == null)
            setCurrentDirectory(null);
        else
            setCurrentDirectory(new File(currentDirectoryPath));
    }


    /**
     * Set the current directory. Passing a parameter of <code>null</code>
     * cause the JFileChooser to point to the user's home directory.
     */
    public void setCurrentDirectory(File dir) {
        if (dir == null) {
            String home = System.getProperty("user.home");
            dir = new File(home);
        }

        if (dir.isDirectory() == false)
            throw new IllegalArgumentException("not a directory");
        
        currentDirectory = dir;
        location = dir.getAbsolutePath();
    }

    /**
     * Returns the currently displayed directory.
     */
    public File getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Get the File selected by the user.  If the user pressed Cancel,
     * the return value is null.
     */
    public File getSelectedFile() {
        if (cancelWasPressed)
            return null;

        return new File(location);
    }

    public void setSelectedFile(File file) {
        if (!file.isAbsolute())
            file = new File(currentDirectory, file.getPath());

        File parent = file.getParentFile();

        if (!file.isDirectory() && (parent != null)) {
            currentDirectory = parent;
            location         = file.getAbsolutePath();
        
        } else if (file.isDirectory()) {
            currentDirectory = file;
            location         = file.getAbsolutePath();
        }
        
        fireFileChooserEvent();
    }

    /**
     * Pops up a custom file chooser dialog with a custom approve button.
     *
     * @param parent            the parent component of the dialog;
     * @param approveButtonText the custom text string to display in the
     *                          Approve button
     * @return the return state of the file chooser on popdown:
     *         <ul><li>JFileChooser.CANCEL_OPTION
     *         <li>JFileChooser.APPROVE_OPTION
     *         <li>JFileChooser.ERROR_OPTION</ul>
     */
    public int showDialog(Window parent, String approveButtonText) {
        this.approveButtonText = approveButtonText;
        
        JDialog chooserDialog = new ChooserDialog(parent);
        
        chooserDialog.setLocationRelativeTo(parent);
        chooserDialog.show();
        if (cancelWasPressed) {
            return CANCEL_OPTION;
        }
        
        return APPROVE_OPTION;
    }
    
    public int showDialog(String approveButtonText) {
        return showDialog(null, approveButtonText);
    }
    
    /**
     * Pops up a "Save File" file chooser dialog; this is a convenience
     * method and is equivalent to showDialog(Component, "Save").
     *
     * @return the return state of the file chooser on popdown:
     *         <ul><li>JFileChooser.CANCEL_OPTION
     *         <li>JFileChooser.APPROVE_OPTION
     *         <li>JFileChooser.ERROR_OPTION</ul>
     */
    public int showSaveDialog(Window parent) {
        return showDialog(parent, "Save");
    }

    public int showSaveDialog() {
        return showDialog("Save");
    }

    /**
     * Pops up a "Open File" file chooser dialog; this is a convenience
     * method and is equivalent to showDialog(Component, "Open").
     *
     * @return the return state of the file chooser on popdown:
     *         <ul>
     *         <li>JFileChooser.CANCEL_OPTION
     *         <li>JFileChooser.APPROVE_OPTION
     *         <li>JFileChooser.ERROR_OPTION
     *         </ul>
     */
    public int showOpenDialog(Window parent) {
        return showDialog(parent, "Open");
    }

    public int showOpenDialog() {
        return showDialog("Open");
    }

    /**
     * Sets the <code>JFileChooser</code> to allow the user to select
     * files only directories only, or files and directories. The default
     * is JFileChooser.FILES_ONLY.
     */
    public void setFileSelectionMode(int mode) {
        if (mode < FILES_ONLY || mode > FILES_AND_DIRECTORIES)
            throw new IllegalArgumentException("Invalid file selection mode");

        fileSelectionMode = mode;
    }

    /**
     * Returns the current file-selection mode.
     *
     * @return the file-selection mode, one of the following:<p>
     *         <ul>
     *         <li><code>JFileChooser.FILES_ONLY</code>
     *         <li><code>JFileChooser.DIRECTORIES_ONLY</code>
     *         <li><code>JFileChooser.FILES_AND_DIRECTORIES</code>
     *         </ul>
     */
    public int getFileSelectionMode() {
        return fileSelectionMode;
    }

    public void setDialogTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the current file filter. The file filter is used by the
     * file chooser to filter out files from the user's view.
     */
    public void setFileFilter(FileFilter filter) {
        fileFilter = filter;
    }

    /**
     * Returns the currently selected file filter.
     */
    public FileFilter getFileFilter() {
        return fileFilter;
    }

    /**
     * Required to implement abstract method of JComponent (never used).
     */
    public Dimension getMinimumSize() {
        return null;
    }

    /**
     * Required to implement abstract method of JComponent (never used).
     */
    public Dimension getSize() {
        return null;
    }

    /**
     * Required to implement abstract method of JComponent (never used).
     */
    public int getHeight() {
        return 0;
    }

    /**
     * Required to implement abstract method of JComponent (never used).
     */
    public int getWidth() {
        return 0;
    }

    protected void addFileChooserListener(FileChooserListener listener) {
        filelisteners.add(listener);
    }

    protected void fireFileChooserEvent() {
        final int count = filelisteners.size();
        for (int i = 0; i < count; i++) {
            FileChooserListener l = (FileChooserListener)filelisteners.get(i);
            l.fileChanged(new FileChooserEvent(this));
        }
    }

    //====================================================================
    // INSTANCE VARIABLES
    protected String    title;
    protected String    approveButtonText = "Open File";

    /**
     * The current directory shown in the dialog.
     */
    protected File currentDirectory = null;
    protected JFileChooser.DirList dirList = this.new DirList();
    protected String location = "";
    protected boolean cancelWasPressed = true;
    protected int fileSelectionMode = FILES_ONLY;
    protected FileFilter fileFilter = null;
    protected ArrayList filelisteners = new ArrayList();

    protected static final int COLS = 50;
    protected static final int ROWS = 20;

    public static final int FILES_ONLY = 200;
    public static final int DIRECTORIES_ONLY = 201;
    public static final int FILES_AND_DIRECTORIES = 202;

    public static final int CANCEL_OPTION = 300;
    public static final int APPROVE_OPTION = 301;
    public static final int ERROR_OPTION = 302;

    // Default button labels - can be customized.
    public static String CANCEL_LABEL = "Cancel";
    public static String APPROVE_LABEL = "Approve";
    public static String PARENT_DIRECTORY_LABEL = "Parent Directory";
    public static String NEW_DIRECTORY_LABEL = "New Directory";

    // Button accelerators (disabled by default).
    public static int CANCEL_ACCELERATOR = -1;
    public static int APPROVE_ACCELERATOR = -1;
    public static int PARENT_DIRECTORY_ACCELERATOR = -1;
    public static int NEW_DIRECTORY_ACCELERATOR = -1;

    
    /**
     * This inner class used by JFileChooser to display a popup dialog.
     */
    private class ChooserDialog extends JDialog implements ActionListener, 
            ListSelectionListener, KeyListener, FileChooserListener {
        
        ChooserDialog(Window parent) {
            super(parent, title);
            init(parent);
        }
            
        void init(Window parent) {
            setSize(COLS, ROWS);

            // insert the directory list in the west
            dirList.setVisibleRowCount(12);
            dirList.setColumns(45);
            dirList.addListSelectionListener(this);
            displayCurrentDirectory();

            scrollPane = new JScrollPane(dirList);
            scrollPane.setBorder(new TitledBorder("Files"));
            add(scrollPane, BorderLayout.WEST);

            /* Insert a north panel that contains the Parent and New buttons.
             */
            JPanel toppanel = new JPanel();
            toppanel.setBorder(new EmptyBorder(0, 1, 0, 1));
            toppanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 0));
            toppanel.add(parentButton);
            parentButton.setText(PARENT_DIRECTORY_LABEL);
            parentButton.addActionListener(this);

            toppanel.add(newButton);
            newButton.setText(NEW_DIRECTORY_LABEL);
            newButton.addActionListener(this);
            add(toppanel, BorderLayout.NORTH);

            // insert a panel in the south for the textfield and the
            // Approve and Cancel buttons
            JPanel southpanel = new JPanel();
            southpanel.setLayout(new BorderLayout());

            JPanel topsouth = new JPanel();
            topsouth.add(new JLabel("Pathname:"));
            topsouth.add(locationField);
            locationField.setText(location);
            locationField.setActionCommand("locationField");
            locationField.addActionListener(this);
            southpanel.add(topsouth, BorderLayout.NORTH);

            JPanel bottomsouth = new JPanel();
            bottomsouth.setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 0));
            bottomsouth.setBorder(new EmptyBorder(1, 1, 0, 1));
            bottomsouth.add(approveButton);
            bottomsouth.add(cancelButton);
            approveButton.setText(approveButtonText);
            cancelButton.setText(CANCEL_LABEL);
            approveButton.addActionListener(this);
            cancelButton.addActionListener(this);
            southpanel.add(bottomsouth, BorderLayout.SOUTH);

            add(southpanel, BorderLayout.SOUTH);

            pack();
            Insets insets = getInsets();
            dirList.setColumns(this.getWidth() - insets.left - insets.right - 2);

            addKeyListener(this);
            addFileChooserListener(this);
        }

        /**
         * Implements the ActionListener interface. Handles button-presses,
         * and the ENTER keystroke in the Pathname field.
         */
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == parentButton) {
                doParentDirectoryAction();
            } else if (source == newButton) {
                doNewDirectoryAction();
            } else if (source == approveButton) {
                doApproveAction();
            } else if (source == cancelButton) {
                doCancelAction();
            } else if (source == locationField) {
                doApproveAction();
            }
        }

        /**
         * Implements the KeyListener interface
         */
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == PARENT_DIRECTORY_ACCELERATOR) {
                doParentDirectoryAction();
            } else if (key == NEW_DIRECTORY_ACCELERATOR) {
                doNewDirectoryAction();
            } else if (key == APPROVE_ACCELERATOR) {
                doApproveAction();
            } else if (key == CANCEL_ACCELERATOR) {
                doCancelAction();
            }
        }

        /**
         * Implements the KeyListener interface
         */
        public void keyTyped(KeyEvent e) {
        }

        /**
         * Implements KeyListener interface; is never called.
         */
        public void keyReleased(KeyEvent e) {
        }

        /**
         * Implements the ListSelectionListener interface.
         */
        public void valueChanged(ListSelectionEvent e) {
            String listitem = (String) dirList.getSelectedValue();
            if (listitem == null) {
                // The selection is empty; so there must have been a
                // file selected, but it has just been deselected.
                locationField.setText(currentDirectory.getAbsolutePath());
                return;
            }

            /* Strip the trailing "/"
             */
            if (listitem.endsWith("/"))
                listitem = listitem.substring(0, listitem.length() - 1);

            File file = new File(currentDirectory, listitem);
            if (file.canRead() == false) {
                String[] msgs = {
                    "File or directory not readable:",
                    file.getAbsolutePath()};
                JOptionPane.showMessageDialog(this, msgs, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (file.isDirectory() == false) {
                if (fileSelectionMode == DIRECTORIES_ONLY) {
                    String[] msgs = {
                        "Not a directory:",
                        file.getAbsolutePath()};
                    JOptionPane.showMessageDialog(this, msgs, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    locationField.setText(file.getAbsolutePath());
                }
                return;
            }

            // The selected file is a directory.
            setCurrentDirectory(file);
            displayCurrentDirectory();
            repaint();

            scrollPane.getViewport().setViewPosition(new Point(0, 0));
            scrollPane.repaint();

            // if the newly selected directory is a root directory,
            // don't allow the Parent button to be pressed
            if (isRoot(currentDirectory) == false)
                parentButton.setEnabled(true);
        }

        /**
         * Implements the FileChooserListener interface.
         */
        public void fileChanged(FileChooserEvent e) {
            displayCurrentDirectory();
            repaint();
        }

        private void doNewDirectoryAction() {
            JFileChooser.NewDirDialog dlg =
                    new NewDirDialog(this, currentDirectory);

            dlg.setLocation(getLocation().addOffset(2, 2));
            dlg.show();
            File newdir = dlg.getDirectory();
            if (newdir != null)
                setCurrentDirectory(newdir);
            displayCurrentDirectory();
            repaint();
        }

        private void doParentDirectoryAction() {
            if (isRoot(currentDirectory)) {
                // We are already in a root directory.  Display the
                // filesystem roots in the listbox. The list of
                // root directories is system-dependent; on Windows it
                // would be A:, B:, C: etc.  On Unix it would be "/".
                File[] roots = File.listRoots();
                for (int i = 0; i < roots.length; i++) {
                    DefaultListModel listModel =
                            (DefaultListModel) dirList.getModel();
                    listModel.addElement(roots[i].getAbsolutePath());
                }
                location = "";
            } else {
                File parent = currentDirectory.getParentFile();
                if (isRoot(parent)) {
                    parentButton.setEnabled(false);
                    dirList.requestFocus();
                }
                setCurrentDirectory(parent);
                displayCurrentDirectory();
                repaint();
            }
        }

        private void doApproveAction() {
            File file = new File(locationField.getText());
            String errmsg = null;

            if (fileSelectionMode == DIRECTORIES_ONLY &&
                    file.isDirectory() == false) {

                errmsg = "Entry is not a directory: ";
            } else if (fileSelectionMode == FILES_ONLY &&
                    file.isDirectory()) {

                setCurrentDirectory(file);
                displayCurrentDirectory();
                repaint();
                return;
            }

            if (errmsg != null) {
                String[] msgs = {errmsg, locationField.getText()};
                JOptionPane.showMessageDialog(this, msgs, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            cancelWasPressed = false;
            location = locationField.getText();
            hide();
        }

        private void doCancelAction() {
            cancelWasPressed = true;
            hide();
        }

        /**
         * Returns true if the specified file is a root directory.
         */
        private boolean isRoot(File dir) {
            String dirname = dir.getAbsolutePath();

            File[] roots = File.listRoots();
            for (int i = 0; i < roots.length; i++) {
                if (roots[i].getAbsolutePath().equals(dirname))
                    return true;
            }
            return false;
        }


        /**
         * Causes the JFileChooser to scan its file list for the current
         * directory, using the currently selected file filter if applicable.
         * Note that this method does not cause the file chooser to be redrawn.
         */
        private void displayCurrentDirectory() {
            // clear the list of Files in the current dir
            dirList.clear();

            DefaultListModel listModel = (DefaultListModel) dirList.getModel();

            // add all the current directory's children into the list
            File[] files = currentDirectory.listFiles();

            // Define and instantiate an anonymous class that implements
            // the Comparator interface. This will be used by the TreeSet
            // to keep the filenames in lexicographical order.
            Comparator fileSorter = new Comparator() {
                public int compare(Object obj1, Object obj2) {
                    String file1 = (String) obj1;
                    String file2 = (String) obj2;
                    return file1.compareTo(file2);
                }
            };

            TreeSet dirs = new TreeSet(fileSorter);
            int numEntries = 0;     // this variable is never used actually!
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    dirs.add(files[i].getName() + "/");
                } else if ((fileSelectionMode != DIRECTORIES_ONLY) &&
                        (fileFilter == null ||
                        fileFilter.accept(files[i]))) {
                    
                    // This is a regular file, and either there is no
                    // file filter or the file is accepted by the
                    // filter.
                    dirs.add(files[i].getName());
                }
                numEntries++;
            }

            // Copy the filenames from the TreeSet to the JList widget
            Iterator iter = dirs.iterator();
            while (iter.hasNext())
                listModel.addElement(iter.next());
            
            locationField.setText(location);
        }

        private JScrollPane scrollPane;
        protected JButton   cancelButton    = new JButton("Cancel");
        protected JButton   approveButton   = new JButton("Open");
        protected JButton   parentButton    = new JButton("Parent Directory");
        protected JButton   newButton       = new JButton("New Directory");
        private JTextField  locationField   = new JTextField(35);
    }

    /*
     * This is a non-static inner class used by the JFileChooser 
     * to implement a sorted list of directory names. The user can
     * find a directory quickly by entering the first few characters
     * of the directory name.
     */
    private class DirList extends JList {
        
        private StringBuffer matchbuf = new StringBuffer();
        
        DirList() {
            super();
            setVisibleRowCount(10);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        /**
         * Clears the list of files displayed by this JList.
         * Does not generate any ListSelectionEvents. (?)
         */
        void clear() {
            // Clear the selection model without notifying any
            // ListSelectionListeners.
            int min = getSelectionModel().getMinSelectionIndex();
            if (min != -1) {
                int max = getSelectionModel().getMaxSelectionIndex();
                getSelectionModel().removeIndexInterval(min, max);
            }

            // Clear the contents of the data model.
            ((DefaultListModel) getModel()).clear();
            matchbuf.setLength(0);
        }

        /**
         * Overrides corresponding method in JList, and allows the
         * user to find a directory quickly by typing the first few letters
         * of the filename.
         */
        protected void processKeyEvent(KeyEvent evt) {
            int key = evt.getKeyCode();
            ListModel listmodel = super.getModel();
            if (listmodel.getSize() > 0 
                    && (key == KeyEvent.VK_BACK_SPACE 
                            || (key > ' ' && key < 255))) {

                if (key == KeyEvent.VK_BACK_SPACE) {
                    if (matchbuf.length() > 0) {
                        // truncate
                        matchbuf.setLength(matchbuf.length() - 1);
                    }
                } else {
                    matchbuf.append((char) key);
                }

                /* Scan through the items in the list until we get
                 * to an item that is lexicographically greater than the
                 * pattern we have typed.
                 */
                String matchstring = matchbuf.toString();
                int i;
                for (i = 0; i < listmodel.getSize(); i++) {
                    Object o = listmodel.getElementAt(i);
                    if (o != null && matchstring.compareTo(o.toString()) <= 0) {
                        break;
                    }
                }
                
                if (i == listmodel.getSize()) {
                    i--;    // the loop completed
                }

                setSelectedIndex(i);
                ensureIndexIsVisible(i);
            }
            
            super.processKeyEvent(evt);
        }
    }

    
    private class NewDirDialog extends JDialog implements ActionListener {
        
        private File            parentFile;
        private JButton         okButton;
        private JButton         cancelButton;
        private JTextField      dirnameField;
        private File            directory;
        
        
        NewDirDialog(JDialog owner, File parent) {
            super(owner);

            setTitle("Enter the new directory name");
            parentFile = parent;
            setSize(60, 10);

            JPanel midpan = new JPanel();
            midpan.setBorder(new EmptyBorder(2, 2, 2, 2));
            midpan.add(new JLabel("Directory name:"));
            dirnameField = new JTextField(35);
            dirnameField.setActionCommand("dirname");
            dirnameField.addActionListener(this);
            midpan.add(dirnameField);
            add(midpan, BorderLayout.CENTER);

            okButton = new JButton("OK");
            okButton.addActionListener(this);
            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(this);
            JPanel southpan = new JPanel();
            southpan.setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 1));
            southpan.add(okButton);
            southpan.add(cancelButton);
            add(southpan, BorderLayout.SOUTH);
            pack();
        }

        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd.equals("OK") || cmd.equals("dirname")) {
                if (parentFile.canWrite() == false) {
                    String[] msgs = {"Permission denied"};
                    JOptionPane.showMessageDialog(this, msgs, 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File newdir = new File(parentFile, dirnameField.getText());
                boolean ok = newdir.mkdir();
                if (ok == false) {
                    String[] msgs = {"Invalid directory"};
                    JOptionPane.showMessageDialog(this, msgs, 
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    directory = newdir;
                    hide();
                }
            } else if (cmd.equals("Cancel")) {
                directory = null;
                hide();
            }
        }

        File getDirectory() {
            return directory;
        }
    }

    private interface FileChooserListener extends EventListener {
        
        public void fileChanged(FileChooserEvent e);
    }

    private class FileChooserEvent extends java.util.EventObject {
        
        private static final long serialVersionUID = 5954095444121689518L;

        
        public FileChooserEvent(Object source) {
            super(source);
        }
    }

    private abstract class FileFilter {
        /**
         * Whether the given file is accepted by this filter.
         */
        public abstract boolean accept(File f);
    }
}
