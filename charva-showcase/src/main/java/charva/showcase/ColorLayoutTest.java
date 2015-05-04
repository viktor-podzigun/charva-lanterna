
package charva.showcase;

import charva.awt.BorderLayout;
import charva.awt.Color;
import charva.awt.ColorPair;
import charva.awt.Container;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.awt.event.KeyEvent;
import charvax.swing.BoxLayout;
import charvax.swing.ButtonGroup;
import charvax.swing.JButton;
import charvax.swing.JDialog;
import charvax.swing.JFrame;
import charvax.swing.JLabel;
import charvax.swing.JPanel;
import charvax.swing.JRadioButton;
import charvax.swing.JTextField;
import charvax.swing.border.LineBorder;
import charvax.swing.border.TitledBorder;

/**
 * This class is based on the MiscellaneousLayoutTest but it
 * demonstrates how to set the foreground and background colors
 * of dialogs and components.
 */
class ColorLayoutTest extends JDialog implements ActionListener {
    
    public ColorLayoutTest(JFrame owner_) {
        super(owner_, 
                "Layout Test in Color (yellow foreground, green background)");
        setLocation(3, 3);
        setForeground(Color.YELLOW);
        setBackground(Color.GREEN);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout()); // default layout for JDialog

        JPanel toppan = new JPanel();
        toppan.setBorder(new TitledBorder(
                "North Panel (inherits green background)"));
        toppan.setForeground(Color.BLUE);
        contentPane.add(toppan, BorderLayout.NORTH);

        JRadioButton button1 = new JRadioButton("A JRadioButton...  ");
        JRadioButton button2 = new JRadioButton("And another JRadioButton");
        ButtonGroup buttons = new ButtonGroup();
        buttons.add(button1);
        buttons.add(button2);
        button1.setSelected(true);
        toppan.add(button1);
        toppan.add(button2);

        JPanel westpan = new JPanel();
        westpan.setBorder(new TitledBorder("West Panel"));
        westpan.setLayout(new BoxLayout(westpan, BoxLayout.Y_AXIS));
        JLabel label1 = new JLabel("Magenta label in west panel");
        westpan.add(label1);
        label1.setForeground(Color.MAGENTA);
        JTextField textfield1 = new JTextField("Cyan JTextField, red background");
        textfield1.setForeground(Color.CYAN);
        textfield1.setBackground(Color.RED);
        westpan.add(textfield1);

        JTextField whiteTextField = new JTextField("White JTextField");
        whiteTextField.setForeground(Color.WHITE);
        westpan.add(whiteTextField);
        JTextField blueTextField = new JTextField("Blue JTextField");
        blueTextField.setForeground(Color.BLUE);
        westpan.add(blueTextField);
        JTextField yellowTextField = new JTextField("Yellow JTextField");
        yellowTextField.setForeground(Color.YELLOW);
        westpan.add(yellowTextField);
        JTextField blackTextField = new JTextField("Black JTextField");
        blackTextField.setForeground(Color.BLACK);
        westpan.add(blackTextField);
        contentPane.add(westpan, BorderLayout.WEST);

        JPanel eastpan = new JPanel();
        eastpan.setForeground(Color.BLACK);
        eastpan.setBorder(new TitledBorder("East Panel"));
        eastpan.add(new JTextField("A JTextField"));
        contentPane.add(eastpan, BorderLayout.EAST);

        JPanel centerpan = new JPanel();
        centerpan.setForeground(Color.WHITE);
        centerpan.setBackground(Color.BLACK);
        centerpan.setLayout(new BorderLayout());
        LineBorder centerpan_lineborder = new LineBorder(
                ColorPair.create(Color.GREEN, Color.BLACK));
        TitledBorder centerpan_titledborder =
                new TitledBorder(centerpan_lineborder, 
                        "Green border, yellow title",
                        TitledBorder.LEFT, TitledBorder.TOP, 
                        ColorPair.create(Color.YELLOW, Color.BLACK));
        centerpan.setBorder(centerpan_titledborder);
        centerpan.add(new JLabel("A white label in the center"), 
                BorderLayout.CENTER);
        contentPane.add(centerpan, BorderLayout.CENTER);

        JPanel southpan = new JPanel();
        southpan.setBorder(new TitledBorder("South Panel (white foreground)"));
        southpan.setBackground(Color.BLUE);
        southpan.setForeground(Color.WHITE);
        JLabel labelsouth = new JLabel("A green label in the south panel ");
        labelsouth.setForeground(Color.GREEN);
        southpan.add(labelsouth);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(this);
        southpan.add(okButton);
        contentPane.add(southpan, BorderLayout.SOUTH);
        okButton.setMnemonic(KeyEvent.VK_F10);
        pack();
    }

    public void actionPerformed(ActionEvent ae_) {
        if (ae_.getActionCommand().equals("OK")) {
            hide();
        }
    }
}

