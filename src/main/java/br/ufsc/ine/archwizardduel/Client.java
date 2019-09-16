package br.ufsc.ine.archwizardduel;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class Client extends JFrame {

	private JLabel label1, label2, label3;
	private JTextField textField1, textField2, textField3;
	private JPasswordField pass;
	private JButton button;
	private JCheckBox bold, italic;


	public Client() {
		super("Testing JLabel");
		setLayout(new FlowLayout());

		Font font = new Font("Arial", Font.PLAIN, 14);

		label1 = new JLabel("JLabel text");
		label1.setFont(font);
		label1.setToolTipText("L1 Tooltip");
		this.add(label1);

		Icon bug = new ImageIcon(getClass().getClassLoader().getResource("bug.png"));
		label2 = new JLabel("JLabel with Icon", bug, SwingConstants.LEFT);
		label2.setToolTipText("Another JLabel tooltip");
		this.add(label2);

		label3 = new JLabel("JLabel with added stuff");
		label3.setIcon(bug);
		label3.setHorizontalTextPosition(SwingConstants.CENTER);
		label3.setVerticalTextPosition(SwingConstants.BOTTOM);
		label3.setToolTipText("Yet another tip");
		this.add(label3);

		textField1 = new JTextField(10);
		this.add(textField1);

		textField2 = new JTextField("Default text");
		this.add(textField2);

		textField3 =  new JTextField("Non-editable", 12);
		textField3.setEditable(false);
		this.add(textField3);

		pass = new JPasswordField("Hidden text");
		this.add(pass);

		button = new JButton("MyButton");
		this.add(button);

		ActionListener observer = new MyHandler();
		textField1.addActionListener(observer);
		textField2.addActionListener(observer);
		textField3.addActionListener(observer);
		pass.addActionListener(observer);
		button.addActionListener(observer);


		bold = new JCheckBox("Bold");
		this.add(bold);

		italic = new JCheckBox("Italic");
		this.add(italic);

		ItemListener listener = new MyListener();
		bold.addItemListener(listener);
		italic.addItemListener(listener);
	}


	private class MyHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String str = "";

			if (event.getSource() == textField1) {
				str = String.format("Field 1: %s", event.getActionCommand());
			} else if (event.getSource() == textField2) {
				str = String.format("Field 2: %s", event.getActionCommand());
			} else if (event.getSource() == textField3) {
				str = String.format("Field 3: %s", event.getActionCommand());
			} else if (event.getSource() == pass) {
				str = String.format("Password field: %s", event.getActionCommand());
			} else {
				str = String.format("Event activated by %s", event.getActionCommand());
			}

			JOptionPane.showMessageDialog(Client.this, str);
		}
	}

	private class MyListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent ev) {
			Font font = null;

			if (bold.isSelected() && italic.isSelected()) {
				font = new Font("Arial", Font.BOLD + Font.ITALIC, 14);
			} else if (bold.isSelected()) {
				font = new Font("Serif", Font.BOLD, 14);
			} else if (italic.isSelected()) {
				font = new Font("Courier New", Font.ITALIC, 14);
			} else {
				font = new Font("Serif", Font.PLAIN, 14);
			}

			label1.setFont(font);
		}
	}

}

