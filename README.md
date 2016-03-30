charva-lanterna
===============
[![Build Status](https://travis-ci.org/viktor-podzigun/charva-lanterna.svg?branch=master)](https://travis-ci.org/viktor-podzigun/charva-lanterna)

Making [CHARVA UI](http://sourceforge.net/projects/charva/) 100% Java by using [lanterna terminal](https://github.com/mabe02/lanterna) library

### Build
```bash
mvn clean package
```

### Run Demo 
```bash
java -Djava.awt.headless=true -jar ./charva-demo-app/target/charva-demo.jar
```
![](charva-demo-app/doc/screenshot.png)

### List of supported components

* [JButton](charva/src/main/java/charvax/swing/JButton.java)
* [JCheckBox](charva/src/main/java/charvax/swing/JCheckBox.java)
* [JCheckBoxMenuItem](charva/src/main/java/charvax/swing/JCheckBoxMenuItem.java)
* [JComboBox](charva/src/main/java/charvax/swing/JComboBox.java)
* [JComponent](charva/src/main/java/charvax/swing/JComponent.java)
* [JDialog](charva/src/main/java/charvax/swing/JDialog.java)
* JFileChooser //TODO
* [JFrame](charva/src/main/java/charvax/swing/JFrame.java)
* [JLabel](charva/src/main/java/charvax/swing/JLabel.java)
* [JList](charva/src/main/java/charvax/swing/JList.java)
* [JMenu](charva/src/main/java/charvax/swing/JMenu.java)
* [JMenuBar](charva/src/main/java/charvax/swing/JMenuBar.java)
* [JMenuItem](charva/src/main/java/charvax/swing/JMenuItem.java)
* [JOptionPane](charva/src/main/java/charvax/swing/JOptionPane.java)
* [JPanel](charva/src/main/java/charvax/swing/JPanel.java)
* [JPasswordField](charva/src/main/java/charvax/swing/JPasswordField.java)
* [JPopupMenu](charva/src/main/java/charvax/swing/JPopupMenu.java)
* [JProgressBar](charva/src/main/java/charvax/swing/JProgressBar.java)
* [JRadioButton](charva/src/main/java/charvax/swing/JRadioButton.java)
* [JScrollBar](charva/src/main/java/charvax/swing/JScrollBar.java)
* [JScrollPane](charva/src/main/java/charvax/swing/JScrollPane.java)
* [JSeparator](charva/src/main/java/charvax/swing/JSeparator.java)
* [JTabbedPane](charva/src/main/java/charvax/swing/JTabbedPane.java)
* JTable //TODO
* JTextArea //TODO
* [JTextField](charva/src/main/java/charvax/swing/JTextField.java)
* JTextComponent //TODO

### Changes to the original CHARVA UI project

There was some refactoring done of original Charva code. Most of the changes were done to the
internal code and should not break client/API interface much.

The idea of these changes is to make the Charva as close to Swing as possible. From both,
the client/API side, and the internal side, and to make it easier to support/migrate the components
in the future.

Here is the short summary of the changes:

#### Structure/modules changes

#### Code changes

#### New/Added staff

    draw method for each component
    renaming it to paint
    adding Graphics class
    adding plaf package to follow javax.swing structure
    making other changes


### How to track the beginning of charva-lanterna back to CHARVA UI

As of 20th March 2016, the original repository of [CHARVA
UI](http://sourceforge.net/projects/charva/) hosts two branches:

* `trunk` r95 - last updated in 2007. A copy of that branch is stored
  in `refs/tags/sourceforge-svn-trunk-r95`;

* Branch `andxor` r116 - last updated in 2009. A copy of that branch
  is stored in tag `refs/tags/sourceforge-svn-branch-andxor-r116`.

charva-lanterna started in 2015, importing a modified version of
CHARVA UI `trunk` r95 - see charva-lanterna commit f5d7b75 `Added
charva, charva-showcase, charva-lanterna modules`.  In order to enrich
the charva-lanterna repository history with CHARVA UI's one, you can
use `git replace`:
```bash
git checkout refs/tags/sourceforge-svn-trunk-r95
git cherry-pick $(git rev-list master | tail -1)
git replace $(git rev-list master | tail -1) HEAD
```
