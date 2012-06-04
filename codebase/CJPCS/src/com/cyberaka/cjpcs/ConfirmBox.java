package com.cyberaka.cjpcs;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ConfirmBox extends Dialog implements ActionListener,
		WindowListener {

	static boolean Confirmation;
	Button yes = new Button("Yes");
	Button no = new Button("No");

	ConfirmBox(Frame parent, String Message) {
		// Create the dialog box.
		super(parent, "Confirm", true);

		// Create the conventions dialog
		TextArea MsgTextArea = new TextArea("", 10, 50,
				TextArea.SCROLLBARS_VERTICAL_ONLY);
		MsgTextArea.setEditable(false);
		yes.addActionListener(this);
		no.addActionListener(this);
		addWindowListener(this);

		// Create the text
		String CT = Message;

		// Insert the text.
		MsgTextArea.setText(CT);

		// Add the components to the dialog
		setLayout(new BorderLayout());
		Container Control = new Panel();
		Control.setLayout(new GridLayout(1, 2));
		Control.add(yes);
		Control.add(no);
		add(MsgTextArea, BorderLayout.CENTER);
		add(Control, BorderLayout.SOUTH);

		// Make the dialog visible
		pack();
		setSize(200, 200);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == yes)
			Confirmation = true;

		if (evt.getSource() == no)
			Confirmation = false;

		dispose();
	}

	// Invoked when the user clicks the close-box
	public void windowClosing(WindowEvent evt) { // (6)
		dispose();
	}

	// unused methods of the WindowListener interface (7)
	public void windowOpened(WindowEvent evt) {
	}

	public void windowIconified(WindowEvent evt) {
	}

	public void windowDeiconified(WindowEvent evt) {
	}

	public void windowDeactivated(WindowEvent evt) {
	}

	public void windowClosed(WindowEvent evt) {
	}

	public void windowActivated(WindowEvent evt) {
	}
}
