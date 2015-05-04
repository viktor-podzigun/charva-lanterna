
package charva.showcase;

import charva.awt.BorderLayout;
import charva.awt.Component;
import charva.awt.Container;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.awt.event.ItemEvent;
import charva.awt.event.ItemListener;
import charva.awt.event.KeyEvent;
import charvax.swing.BoxLayout;
import charvax.swing.ButtonGroup;
import charvax.swing.JButton;
import charvax.swing.JCheckBox;
import charvax.swing.JDialog;
import charvax.swing.JFrame;
import charvax.swing.JLabel;
import charvax.swing.JPanel;
import charvax.swing.JRadioButton;
import charvax.swing.JTextField;
import charvax.swing.border.EmptyBorder;
import charvax.swing.border.TitledBorder;


/**
 * This class demonstrates how to use the various types of Buttons.
 */
class ButtonTest extends JDialog implements ActionListener, ItemListener {
    
    private ButtonGroup     _buttons  = new ButtonGroup();
    private JRadioButton    _strawberry;
    private JRadioButton    _chocolate;
    private JRadioButton    _vanilla;
    private JRadioButton    _pistachio;
    private JRadioButton    _lime;
    private JTextField      _selectedFlavor = new JTextField(15);
    private JCheckBox       _nutTopping;
    private JCheckBox       _syrupTopping;
    private JCheckBox       _candyTopping;
    private JCheckBox       _waferTopping;

    
    ButtonTest(JFrame owner_) {
        super(owner_, "Button Test");
        
        _strawberry   = new JRadioButton("Strawberry");
        _strawberry.setMnemonic('s');
        _chocolate    = new JRadioButton("Chocolate");
        _chocolate.setMnemonic('c');
        _vanilla      = new JRadioButton("Vanilla");
        _vanilla.setMnemonic('v');
        _pistachio    = new JRadioButton("Pistachio");
        _pistachio.setMnemonic('p');
        _lime         = new JRadioButton("Lime");
        _lime.setMnemonic('l');
        _nutTopping   = new JCheckBox("Nuts");
        _nutTopping.setMnemonic('n');
        _syrupTopping = new JCheckBox("Syrup");
        _syrupTopping.setMnemonic('y');
        _candyTopping = new JCheckBox("Candy");
        _candyTopping.setMnemonic('a');
        _waferTopping = new JCheckBox("Wafer");
        _waferTopping.setMnemonic('w');
        
        Container contentPane = getContentPane();
        contentPane.add(makeNorthPanel(), BorderLayout.NORTH);
        contentPane.add(makeCenterPanel(), BorderLayout.CENTER);

        JPanel southpan = new JPanel();
        JButton okButton = new JButton("OK (F10)");
        okButton.setMnemonic(KeyEvent.VK_F10);
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        southpan.add(okButton);

        contentPane.add(southpan, BorderLayout.SOUTH);

        //pack();
    }

    /**
     * Implements the ActionListener interface.
     */
    public void actionPerformed(ActionEvent e_) {
        String cmd = e_.getActionCommand();
        if (cmd.equals("OK")) {
            hide();
        }
    }

    /**
     * Implements ItemListener interface
     */
    public void itemStateChanged(ItemEvent e_) {
        int statechange = e_.getStateChange();
        Component source = (Component) e_.getSource();
        if (statechange == ItemEvent.SELECTED) {
            JRadioButton button = (JRadioButton) source;
            _selectedFlavor.setText(button.getText());
        }
    }

    private JPanel makeNorthPanel() {
        JPanel northpan = new JPanel();
        northpan.setBorder(new TitledBorder("Select a flavor"));
        northpan.setLayout(new BoxLayout(northpan, BoxLayout.Y_AXIS));

        northpan.add(_strawberry);
        northpan.add(_chocolate);
        northpan.add(_vanilla);
        northpan.add(_pistachio);
        northpan.add(_lime);

        _strawberry.addItemListener(this);
        _strawberry.setActionCommand("Strawberry");
        _chocolate.addItemListener(this);
        _chocolate.setActionCommand("Chocolate");
        _vanilla.addItemListener(this);
        _vanilla.setActionCommand("Vanilla");
        _pistachio.addItemListener(this);
        _pistachio.setActionCommand("Pistachio");
        _lime.addItemListener(this);
        _lime.setActionCommand("Lime");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Selected flavor: "));
        panel.add(_selectedFlavor);
        _selectedFlavor.setEnabled(false);
        panel.setBorder(new EmptyBorder(1, 1, 1, 1));
        northpan.add(panel);

        _buttons.add(_strawberry);
        _strawberry.setSelected(true);    // select one button in the group
        _buttons.add(_chocolate);
        _buttons.add(_vanilla);
        _buttons.add(_pistachio);
        _buttons.add(_lime);

        return northpan;
    }

    private JPanel makeCenterPanel() {
        JPanel centerpan = new JPanel();
        centerpan.setBorder(new TitledBorder("Select one or more toppings"));
        centerpan.add(_nutTopping);
        centerpan.add(_syrupTopping);
        centerpan.add(_candyTopping);
        centerpan.add(_waferTopping);

        return centerpan;
    }
}

