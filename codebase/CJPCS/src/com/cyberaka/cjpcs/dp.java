package com.cyberaka.cjpcs;

import java.io.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;

public class dp extends Frame implements ActionListener {

	static String questionList[];
	static String fileName = "";

	public static void main(String args[]) {
		if (args.length == 1) {
			fileName = args[0];
		} else {
			System.out.println("Usage: java dp <file name>");
			System.exit(0);
		}

		// Allocate the length of the array
		int totquestions = countQuestions();
		System.out.println(totquestions);
		questionList = new String[totquestions];

		// Read the questions
		readQuestions();

		// Output the contents of the array
		try {
			File newFile = new File("QQ.txt");
			FileOutputStream outputFile = new FileOutputStream(newFile);
			;
			OutputStreamWriter outputFileWriter = new OutputStreamWriter(
					outputFile);
			BufferedWriter fileWriter2 = new BufferedWriter(outputFileWriter);
			PrintWriter fileWriter = new PrintWriter(fileWriter2);

			for (int i = 0; i < questionList.length; i++)
				fileWriter.println(questionList[i] + "\r\n\r\n");
		} catch (Exception e) {
			System.out.println("Exception " + e);
		}

		new dp();
	}

	private static int countQuestions() {
		File fileInstance = null;
		FileInputStream inputFile = null;
		InputStreamReader inputFileReader = null;
		BufferedReader fileReader = null;

		try {
			fileInstance = new File(fileName);
			inputFile = new FileInputStream(fileInstance);
			inputFileReader = new InputStreamReader(inputFile);
			fileReader = new BufferedReader(inputFileReader);
		} catch (IOException e) {
			System.out.println("Error\n" + e);
		}
		// Check the files in the file description
		if (!fileInstance.exists() || !fileInstance.isFile()) {
			System.out.println("file named " + fileName
					+ " not found.\n\n Please download the file once again...");
			System.exit(0);
		}

		String temp = "";

		// Temporary variable
		int count = 0;

		try {
			do {
				temp = fileReader.readLine(); // Read a line
				if (temp == null) // If read line is
					break; // null, exit.

				// Check whether we have got a question
				// in the line currently read
				if (temp.indexOf("Q. ") != -1)
					count++;
			} while (true);
		} catch (IOException e) {
			System.out.println("Input Output Exception detected...\n\n" + e);
			System.exit(0);
		}
		return count;
	} // end count questions

	// Program to select questions
	private static void readQuestions() {

		// File fileInstance = new File(fileName);
		// FileInputStream inputFile = new FileInputStream(fileInstance);
		// InputStreamReader inputFileReader = new InputStreamReader(inputFile);
		// BufferedReader fileReader = new BufferedReader(inputFileReader);

		File fileInstance = null;
		FileInputStream inputFile = null;
		InputStreamReader inputFileReader = null;
		BufferedReader fileReader = null;

		try {
			fileInstance = new File(fileName);
			inputFile = new FileInputStream(fileInstance);
			inputFileReader = new InputStreamReader(inputFile);
			fileReader = new BufferedReader(inputFileReader);
		} catch (IOException e) {
			System.out.println("Error\n" + e);
		}

		// Check the files in the file description
		if (!fileInstance.exists() || !fileInstance.isFile()) {
			System.out.println("file named " + fileName
					+ " not found.\n\n Please download the file once again...");
			System.exit(0);
		}

		// Now we will read each and every line in the text file.
		// in the each line read we will search for "Q. " standing
		// for question. If no question is found then the data read
		// will be buffered in a temporary string variable named
		// tempBuffer.
		// If a "Q. " string is found then we will check the string in
		// tempBuffer for containing "Q. ". If the check is false then it
		// signifies that the string in tempBuffer is nothing and should be
		// discarded. Now if the string currently read and string in tempBuffer
		// contains a "Q. " then we will assign the tempBuffer value
		// into the questionList array. After doing this any string after
		// Q. will be assigned to the tempBuffer variable.

		String temp = "";
		String tempBuffer = "";
		int arrayIndex = 0;

		// Temporary variable
		int temp2 = 0;

		try {
			getOut: do {
				temp = fileReader.readLine(); // Read a line
				if (temp == null) // If read line is
					break; // null, exit.

				// Check whether we have got a question
				// in the line currently read
				temp2 = temp.indexOf("Q. ");
				if (temp2 != -1) {
					// Now extract any part of previous question from the
					// current question
					if (temp2 > 0)
						tempBuffer += "\r\n"
								+ temp.substring(0, temp.indexOf("Q. "));

					// Check whether the tempBuffer string contains a valid
					// "Q. " tag. If not it is a garbage.
					if (tempBuffer.indexOf("Q. ") == -1) {
						// Garbage
						tempBuffer = "";
					} else {
						// Now add the tempBuffer into the question array
						questionList[arrayIndex++] = tempBuffer.trim();
						tempBuffer = "";
					}

					// Now extract any remaining part of the temp variable
					if (temp2 < temp.length()) {
						tempBuffer = temp.substring(temp2, temp.length());
					}
				} else {
					tempBuffer += "\r\n" + temp;
				}
			} while (true);
		} catch (IOException e) {
			System.out.println("Input Output Exception detected...\n\n" + e);
			System.exit(0);
		}
	} // end read questions

	Frame dpWin = new Frame("CJPCS - Data entry program");

	// Text Area
	TextArea displayArea = new TextArea("", 5, 40,
			TextArea.SCROLLBARS_VERTICAL_ONLY);
	Container control = new Panel();
	Container radioControl = new Panel();

	// Define the radio buttons
	CheckboxGroup radioGroup = new CheckboxGroup();
	Checkbox section1 = new Checkbox("Section 1", radioGroup, true);
	Checkbox section2 = new Checkbox("Section 2", radioGroup, false);
	Checkbox section3 = new Checkbox("Section 3", radioGroup, false);
	Checkbox section4 = new Checkbox("Section 4", radioGroup, false);
	Checkbox section5 = new Checkbox("Section 5", radioGroup, false);
	Checkbox section6 = new Checkbox("Section 6", radioGroup, false);
	Checkbox section7 = new Checkbox("Section 7", radioGroup, false);
	Checkbox section8 = new Checkbox("Section 8", radioGroup, false);
	Checkbox section9 = new Checkbox("Section 9", radioGroup, false);
	Checkbox section10 = new Checkbox("Section 10", radioGroup, false);
	Checkbox section11 = new Checkbox("Section 11", radioGroup, false);

	// Define the submit button
	Container buttonControl = new Panel();
	Button submit = new Button("Submit");
	Button previous = new Button("Previous");
	Button next = new Button("Next");
	int currentArrayIndex = 0;

	dp() {
		// Construct the radio contral panel
		radioControl.setLayout(new GridLayout(3, 5));
		radioControl.add(section1);
		radioControl.add(section2);
		radioControl.add(section3);
		radioControl.add(section4);
		radioControl.add(section5);
		radioControl.add(section6);
		radioControl.add(section7);
		radioControl.add(section8);
		radioControl.add(section9);
		radioControl.add(section10);
		radioControl.add(section11);
		radioControl.add(new Label(""));
		radioControl.add(new Label(""));
		radioControl.add(new Label(""));
		radioControl.add(new Label(""));

		// create the button control
		submit.addActionListener(this);
		previous.addActionListener(this);
		next.addActionListener(this);
		buttonControl.setLayout(new GridLayout(1, 3));
		buttonControl.add(previous);
		buttonControl.add(next);
		buttonControl.add(submit);

		// construct the controls container.
		control.setLayout(new BorderLayout());
		control.add(radioControl, BorderLayout.NORTH);
		control.add(buttonControl, BorderLayout.SOUTH);

		dpWin.add(displayArea, BorderLayout.CENTER);
		dpWin.add(control, BorderLayout.SOUTH);

		// set the text in the display area text.
		displayArea.setText(questionList[currentArrayIndex]);

		dpWin.pack();
		dpWin.setVisible(true);

		dpWin.addWindowListener(new WindowAdapter() {
			// Invoked when the user clicks the close box.
			public void windowClosing(WindowEvent evt) {
				dispose();
				System.exit(0);
			}
		});
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == submit) {
			if ((new ConfirmBox(dpWin,
					"Are sure you want to submit this question into section no. "
							+ getSectionNo())).Confirmation) {
				if (submitQuestion()) {
					displayArea.setText(questionList[++currentArrayIndex]);
				} else {
					System.out.println("Unable to submit question no. "
							+ ++currentArrayIndex);
					System.exit(0);
				}
			}
		}

		if (evt.getSource() == previous & currentArrayIndex > 0) {
			displayArea.setText(questionList[--currentArrayIndex]);
		}

		if (evt.getSource() == next & currentArrayIndex < questionList.length) {
			displayArea.setText(questionList[++currentArrayIndex]);
		}

	}

	private boolean submitQuestion() {
		// Output the contents of the array at the current index.
		try {
			File newFile = new File("section" + getSectionNo() + ".txt");
			FileOutputStream outputFile = new FileOutputStream("section"
					+ getSectionNo() + ".txt", true);
			;
			OutputStreamWriter outputFileWriter = new OutputStreamWriter(
					outputFile);
			BufferedWriter fileWriter2 = new BufferedWriter(outputFileWriter);
			PrintWriter fileWriter = new PrintWriter(fileWriter2);

			fileWriter.println(questionList[currentArrayIndex] + "\r\n\r\n");

			fileWriter.close();
			fileWriter2.close();
			outputFileWriter.close();
			outputFile.close();
		} catch (Exception e) {
			System.out.println("Exception " + e);
			return false;
		}
		return true;
	}

	private int getSectionNo() {
		if (section1.getState())
			return 1;
		else if (section2.getState())
			return 2;
		else if (section3.getState())
			return 3;
		else if (section4.getState())
			return 4;
		else if (section5.getState())
			return 5;
		else if (section6.getState())
			return 6;
		else if (section7.getState())
			return 7;
		else if (section8.getState())
			return 8;
		else if (section9.getState())
			return 9;
		else if (section10.getState())
			return 10;
		else if (section11.getState())
			return 11;
		return 1;
	}
} // end dp class
