package com.cyberaka.cjpcs;

import java.applet.*;
import javax.swing.*;
import java.util.Random.*;
import java.util.*;
import java.lang.Math.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/*
 Project Code : CJPCS
 Project Name : Comprehensive Java Programmer Certification Simulator


 Expected layout model of the main window.

 /-CJPCS (Comprehensive Java Programmer Certification Simulator)----\
 |/----------------------------------------------------------------\|
 ||                                         /---------\  /--------\||
 || Time Left: 2:xx:xx                      | Log Off |  | Grade  |||
 ||                                         \---------/  \--------/||
 ||----------------------------------------------------------------||
 ||/--------------------------------------------------------------\||
 |||                                                            |Û|||
 ||| Question: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  |Û|||
 |||           xxxxxxxxxxxxxxx                                  |Û|||
 |||                                                            |Û|||
 ||| Options :                                                  | |||
 |||                                                            | |||
 |||   A; xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  | |||
 |||                                                            | |||
 |||   B: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  | |||
 |||                                                            | |||
 |||   C: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  | |||
 ||\--------------------------------------------------------------/||
 ||----------------------------------------------------------------||
 ||/--------------------------------------------------------------\||
 ||| [ ] A.                        [ ] E.                         |||
 ||| [ ] B.                        [ ] F.                         |||
 ||| [ ] C.                        [ ] G.                         |||
 ||| [ ] D.                        [ ] H.                         |||
 |||--------------------------------------------------------------|||
 ||| /--------\   /--------\  /--------\   /--------\  /--------\ |||
 ||| |Previous|   |  Next  |  |  MARK  |   |Previous|  |  Next  | |||
 ||| |M. Ques.|   |M. Ques.|  |Question|   |Question|  |Question| |||
 ||| \--------/   \--------/  \--------/   \--------/  \--------/ |||
 ||\--------------------------------------------------------------/||
 |\----------------------------------------------------------------/|
 \------------------------------------------------------------------/

 Border Layout no. 1

 1.1 Northern Container:
 Time Label
 Log Off button
 Grading Button

 1.2 Center Container  :
 Text Area

 1.3 Southern Container:
 Another Container with Border Layout no. 2

 Border Layout no. 2

 1.3.1 Northern Container:
 Another contianer with card layout.

 1.3.2 Southern Container:
 Another container with grid layout.

 1.3.1.1 Card Layout :
 A list of 59 cards with each corresponding questions.

 1.3.2.1 Grid layout :
 A list of 5 buttons for easy navigation among the questions.
 */

/*
 According to the java programmer specification their are
 11 sections of 

 */

public class CJPCS extends Frame implements ActionListener {

	// Create the main frame
	static Frame BaseFrame = new Frame(
			"CJPCS - Comprehensive Java Programmer Certification Simulator");

	// GUI grading or textual grading.
	static boolean guiGrading = false;

	// GUI grading windows column names.
	String[][] gradingData = {
			{ "Declaration & Access control", null, null, null, null },
			{ "Flow Control & Exception handling", null, null, null, null },
			{ "Garbage Collection", null, null, null, null },
			{ "Language Fundamentals", null, null, null, null },
			{ "Operators & Assignments", null, null, null, null },
			{ "OverLoading & Overridding", null, null, null, null },
			{ "Threads", null, null, null, null },
			{ "java.awt", null, null, null, null },
			{ "java.lang", null, null, null, null },
			{ "java.util", null, null, null, null },
			{ "java.io", null, null, null, null },
			{ "EXAM TOTAL", null, null, null, null } };

	String[] gradingColumnNames = { "Objective/Section", "Total Right",
			"Total Wrong", "Total Unattempted", "Percentage" };

	// Create an array reference to each of the section files
	// along with the no. of questions to be included from each section.
	static int questionCount = 0;
	static int currentQuestionNo = 1;
	static String[][] currentExamQuestionList = new String[59][8];
	// section no.
	// Question
	// Exhibit
	// Options
	// Answer key
	// Status - Answer given list (a,b,c,d / a / 0x6)
	// Marked - Marked
	// Correct - Correct/Wrong

	static String[][] sectionFileDescription = {
			{ "section1.txt", "6", "", "" }, { "section2.txt", "5", "", "" },
			{ "section3.txt", "5", "", "" }, { "section4.txt", "7", "", "" },
			{ "section5.txt", "5", "", "" }, { "section6.txt", "5", "", "" },
			{ "section7.txt", "5", "", "" }, { "section8.txt", "6", "", "" },
			{ "section9.txt", "5", "", "" }, { "section10.txt", "5", "", "" },
			{ "section11.txt", "5", "", "" } };

	// filename
	// questions from each file
	// questions available
	// question sequence read

	public static void main(String args[]) {

		// Display the conventions text & splash
		Frame f = new Frame();
		showSplash ss = new showSplash(f);
		new Conventions(f);

		// count the total no. of questions available
		countQuestions();

		for (int i = 0; i < sectionFileDescription.length; i++) {
			sectionFileDescription[i][3] = generateRandomQuestionList(
					getIntValue(sectionFileDescription[i][1]),
					getIntValue(sectionFileDescription[i][2]));
			// System.out.println(sectionFileDescription[i][0]+" -
			// "+sectionFileDescription[i][2]+" - " +
			// sectionFileDescription[i][3]);
		}

		// select the questions from each section.
		selectQuestions();

		// Initialize the main CJPCS screen
		CJPCS NewFrame = new CJPCS();
		new timer(timeLeft);
		// Activate the event control function of the instance
		// of CJPCS main window
		NewFrame.activateControls();
	}

	// ****************** Component Declaration Section *********************

	// For container no. 1.1
	Container BannerContainer = new Panel();
	GridLayout BannerLayout = new GridLayout(1, 5);
	Font BannerFont = new Font("SansSerif", Font.BOLD, 15);
	// Label timeLeft = new TextFieldLabel("Time left: 120 minutes",
	// Label.LEFT);
	static TextField timeLeft = new TextField("TIME: 00:00:00");
	Label questionNumber = new Label("Question no: 1");
	Label markLabel = new Label("Marked");
	Button Exhibit = new Button("View Exhibit ");
	Button Grade = new Button("Grade Session");

	// For container 1.2
	Container OptionContainer = new Panel();
	GridLayout OptionLayout = new GridLayout(1, 1);
	Font OptionBoardFont = new Font("Monospaced", Font.PLAIN, 18);
	TextArea OptionBoard = new TextArea("", 10, 50,
			TextArea.SCROLLBARS_VERTICAL_ONLY);

	// For container 1.3.1
	Container AnswerBoardContainer = new Panel();
	GridLayout AnswerBoardLayout = new GridLayout(3, 3);
	CheckboxGroup AnswerGroup = new CheckboxGroup();
	Font AnswerBoardFont = new Font("Dialog", Font.BOLD, 20);
	Checkbox AnswerCheckBox[] = new Checkbox[9];
	// Checkbox AnswerRadioBox[] = new Checkbox[9];

	/*
	 * ("A:"); Checkbox AnswerB = new Checkbox("B:"); Checkbox AnswerC = new
	 * Checkbox("C:"); Checkbox AnswerD = new Checkbox("D:"); Checkbox AnswerE =
	 * new Checkbox("E:"); Checkbox AnswerF = new Checkbox("F:"); Checkbox
	 * AnswerG = new Checkbox("G:"); Checkbox AnswerH = new Checkbox("H:");
	 * Checkbox AnswerI = new Checkbox("I:");
	 */
	TextField AnswerText = new TextField(10);
	// AnswerText.setFont(AnswerBoardFont);

	// For container 1.3.2
	Container ControlBoardContainer = new Panel();
	GridLayout ControlBoardLayout = new GridLayout(1, 5);
	Button PreviousMarkedQuestion = new Button("<< (Marked Question)");
	Button NextMarkedQuestion = new Button("(Marked Question) >>");
	Button MarkQuestion = new Button("Mark Question");
	Button PreviousQuestion = new Button("<< Question");
	Button NextQuestion = new Button("Question >>");

	// For container 1.3
	Container FooterContainer = new Panel();
	BorderLayout FooterLayout = new BorderLayout();

	// Create the base container
	BorderLayout BaseLayout = new BorderLayout();

	public CJPCS() {

		// Program components declaratin section
		// For container 1.1
		Exhibit.addActionListener(this);
		Grade.addActionListener(this);
		BannerContainer.setLayout(BannerLayout);
		timeLeft.setFont(new Font("Serif", Font.BOLD, 15));
		timeLeft.setForeground(Color.blue);
		timeLeft.setEditable(false);
		questionNumber.setFont(new Font("SansSerif", Font.BOLD, 15));
		markLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
		BannerContainer.setBackground(Color.cyan);
		BannerContainer.add(timeLeft);
		BannerContainer.add(questionNumber);
		BannerContainer.add(markLabel);
		BannerContainer.add(Exhibit);
		BannerContainer.add(Grade);

		// For container 1.2
		OptionBoard.setFont(OptionBoardFont);
		OptionContainer.setLayout(OptionLayout);
		OptionContainer.add(OptionBoard); // [1,1]
		OptionBoard.setBackground(Color.orange);
		OptionBoard.setForeground(Color.black);
		OptionBoard.setEditable(false);
		/*
		 * OptionBoard.setText( "Question:\n" + "Which of the following are
		 * valid definitions of an" + " application's main( ) method?\n\n" +
		 * "Select all correct answers:\n" + "A: public static void main();\n"+
		 * "B: public static void main( String args );\n"+ "C: public static
		 * void main( String args[] );\n"+ "D: public static void main( Graphics
		 * g );\n"+ "E: public static boolean main( String args[] );\n\n");
		 */
		OptionBoard.setText("");
		for (int i = 0; i < currentExamQuestionList.length; i++) {
			// OptionBoard.setText(OptionBoard.getText() +
			// currentExamQuestionList[i][0]);
			OptionBoard.setText(OptionBoard.getText()
					+ currentExamQuestionList[i][1] + '\r' + '\n');
			OptionBoard.setText(OptionBoard.getText()
					+ currentExamQuestionList[i][2] + '\r' + '\n');
			OptionBoard.setText(OptionBoard.getText()
					+ currentExamQuestionList[i][3] + '\r' + '\n');
			OptionBoard.setText(OptionBoard.getText()
					+ currentExamQuestionList[i][4] + '\r' + '\n');
			// OptionBoard.setText(OptionBoard.getText() +
			// currentExamQuestionList[i][5]);
			OptionBoard.setText(OptionBoard.getText() + '\r' + '\n'
					+ "------------" + '\r' + '\n' + '\r' + '\n');
		}

		// For container 1.3.1
		constructContainer();
		/*
		 * AnswerBoardContainer.setLayout(AnswerBoardLayout);
		 * AnswerBoardContainer.setFont(AnswerBoardFont);
		 * AnswerBoardContainer.setBackground(Color.lightGray);
		 * AnswerBoardContainer.setForeground(Color.red);
		 * AnswerBoardContainer.add(AnswerA); AnswerBoardContainer.add(AnswerD);
		 * AnswerBoardContainer.add(AnswerG); AnswerBoardContainer.add(AnswerB);
		 * AnswerBoardContainer.add(AnswerE); AnswerBoardContainer.add(AnswerH);
		 * AnswerBoardContainer.add(AnswerC); AnswerBoardContainer.add(AnswerF);
		 * AnswerBoardContainer.add(AnswerI);
		 */

		// Container 1.3.2
		PreviousMarkedQuestion.addActionListener(this);
		NextMarkedQuestion.addActionListener(this);
		MarkQuestion.addActionListener(this);
		PreviousQuestion.addActionListener(this);
		NextQuestion.addActionListener(this);

		ControlBoardContainer.setFont(new Font("Serif", Font.BOLD, 12));
		ControlBoardContainer.setLayout(ControlBoardLayout);
		ControlBoardContainer.add(PreviousMarkedQuestion);
		ControlBoardContainer.add(NextMarkedQuestion);
		ControlBoardContainer.add(MarkQuestion);
		ControlBoardContainer.add(PreviousQuestion);
		ControlBoardContainer.add(NextQuestion);

		// For container 1.3
		FooterContainer.setLayout(FooterLayout);
		FooterContainer.add(AnswerBoardContainer, BorderLayout.NORTH);
		FooterContainer.add(ControlBoardContainer, BorderLayout.SOUTH);

		// set the layout of the frame.
		BaseFrame.setLayout(BaseLayout);
		BaseFrame.add(BannerContainer, BorderLayout.NORTH);
		BaseFrame.add(OptionContainer, BorderLayout.CENTER);
		BaseFrame.add(FooterContainer, BorderLayout.SOUTH);

		// Activate the Layout manager & Show the window
		BaseFrame.pack();
		BaseFrame.setSize(800, 400);
		BaseFrame.setVisible(true);
	}

	public void activateControls() {

		BaseFrame.addWindowListener(new WindowAdapter() {
			// Invoked when the user clicks the close box.
			public void windowClosing(WindowEvent evt) {
				terminate();
			}
		});
	}

	private void terminate() {
		dispose();
		System.exit(0);
	}

	// Program to count questions
	private static void countQuestions() {

		File fileInstance = null;
		FileInputStream inputFile = null;
		InputStreamReader inputFileReader = null;
		BufferedReader fileReader[] = new BufferedReader[sectionFileDescription.length];
		// System.out.println(sectionFileDescription.length);
		// System.out.print("Pass: ");

		// Check the files in the file description
		try {
			for (int i = 0; i < sectionFileDescription.length; i++) {
				// Create an instance
				fileInstance = new File(sectionFileDescription[i][0]);
				if (!fileInstance.exists() || !fileInstance.isFile()) {
					new MessageBox(
							BaseFrame,
							"file named "
									+ sectionFileDescription[i][0]
									+ " not found.\n\n Please download the file once again...1");
					System.exit(0);
				}

				// Create the individual streams
				inputFile = new FileInputStream(fileInstance);
				inputFileReader = new InputStreamReader(inputFile);
				fileReader[i] = new BufferedReader(inputFileReader);
			}
			// System.out.print("[1]");
		} catch (FileNotFoundException e) {
			new MessageBox(BaseFrame, "File not found\n\nDescription " + e);
			System.exit(0);
		} catch (IOException e) {
			new MessageBox(BaseFrame, "Input/Output error\n\nDescription " + e);
			// System.out.println(e);
		}

		// Count the no. of available questions in the
		// section files.
		try {

			int totalSectionQuestions = 0;
			String temp = "";
			String temp2 = "";
			// System.out.print("[2]\n");

			for (int i = 0; i < fileReader.length; i++) {
				// System.out.print("["+i+"]");
				do {
					temp = fileReader[i].readLine();
					if (temp == null)
						break;
					// System.out.print(temp);
					temp2 = "";
					for (int j = 0; j < temp.length(); j++) {
						// Check whether we have got a question
						if (temp2.length() >= 3) {
							if (temp2.substring(j - 3, j).equals("Q. ")) {
								totalSectionQuestions++;
								// System.out.println(totalSectionQuestions);
							}
						}
						temp2 = temp2 + temp.charAt(j);
						// System.out.print(temp2);
					}
				} while (true);
				sectionFileDescription[i][2] = String
						.valueOf(totalSectionQuestions);
				totalSectionQuestions = 0;
			}
		} catch (FileNotFoundException e) {
			System.out.println("" + e);
		} catch (IOException e) {
			System.out.println(e);
		}

		try {
			for (int i = 0; i < fileReader.length; i++)
				fileReader[i].close();
		} catch (IOException e) {
			new MessageBox(BaseFrame, "Unable to close files\n\nDescription\n"
					+ e);
			System.exit(0);
		}
	}

	// program to generate a random list of questions to be selected from
	// each section.
	private static String generateRandomQuestionList(int totalItems, int upto) {
		int num1 = upto;
		int totalchars = 0;

		// Count the number of characters in the number
		do {
			num1 = (int) num1 / 10;
			totalchars++;
		} while (num1 > 0);

		Date d = new Date();
		Random r = new Random(d.getTime());
		int dummyRange = (int) Math.pow(10, totalchars); // This variable
															// will be used
		// to blanket the random no.
		int j = 0;
		int proceed = 0;
		String returntext = "";
		int dummynum1 = 0;

		// System.out.println("[1]");
		for (int i = 0; i < totalItems; i++) {
			// System.out.println("====>" + i + " ====>" + returntext);
			do // Proceed only when the
			{ // output no. is in the
				j = (int) (r.nextFloat() * dummyRange);

				if (j >= 1 & j <= upto) // Check whether j is in
				{ // range or not.
					proceed = 1; // Set escape to true
					dummynum1 = 0;
					for (int k = 0; k < returntext.length(); k++) { // 11,4,
						if (returntext.charAt(k) == ',') { // If the current
															// index is at a
															// comma
							// System.out.print(">" + returntext + ":" +
							// dummynum1 + ":" + k + ":");
							// System.out.println(returntext.substring(dummynum1,k)
							// + ">");
							if (getIntValue(returntext.substring(dummynum1, k)) == j)
								proceed = 0; // if the number already exists
							// then don't exit
							dummynum1 = k + 1;
						}
					}
				} else {
					proceed = 0;
				}
			} while (proceed == 0);
			returntext = returntext + j + ",";
			proceed = 0;
		}

		return returntext;
	}

	// Program to select questions
	private static void selectQuestions() {

		File fileInstance = null;
		FileInputStream inputFile = null;
		InputStreamReader inputFileReader = null;
		BufferedReader fileReader[] = new BufferedReader[sectionFileDescription.length];

		File newFile = new File("SampleQuestion.txt");
		FileOutputStream outputFile = null;
		OutputStreamWriter outputFileWriter = null;
		BufferedWriter fileWriter2 = null;
		PrintWriter fileWriter = null;

		// System.out.println(sectionFileDescription.length);
		// System.out.print("Pass: ");

		// Check the files in the file description
		try {
			for (int i = 0; i < sectionFileDescription.length; i++) {
				// Create an instance
				fileInstance = new File(sectionFileDescription[i][0]);
				if (!fileInstance.exists() || !fileInstance.isFile()) {
					new MessageBox(
							BaseFrame,
							"file named "
									+ sectionFileDescription[i][0]
									+ " not found.\n\n Please download the file once again...2");
					System.exit(0);
				}

				// Create the individual streams
				inputFile = new FileInputStream(fileInstance);
				inputFileReader = new InputStreamReader(inputFile);
				fileReader[i] = new BufferedReader(inputFileReader);
			}

			// Create a stream into the output file
			outputFile = new FileOutputStream(newFile);
			outputFileWriter = new OutputStreamWriter(outputFile);
			fileWriter2 = new BufferedWriter(outputFileWriter);
			fileWriter = new PrintWriter(fileWriter2);
			// System.out.print("[1]");
		} catch (FileNotFoundException e) {
			new MessageBox(BaseFrame, "File not found\n\nDescription " + e);
			System.exit(0);
		} catch (IOException e) {
			new MessageBox(BaseFrame, "Input/Output error\n\nDescription " + e);
			System.out.println(e);
		}

		// Now open each and every streams and count the no. of
		// questions one by one. If any number in the section decription
		// matches the question number in the section text file then
		// induct it into another file.

		// Count the no. of available questions in the
		// section files.
		try {

			int totalSectionQuestions = 0; // Total questions in this section
			int nextQuestionNo = 0; // The question no. to be found out.
			int presentQuestionNo = 0; // The current question no. index of
										// file
			int dummynum1 = 0;
			int questionSequence[] = null; // Contains the sequence of randomly
											// generated question sequence
			int questionSequenceIndex = 0;
			String randomQuestionList = ""; // contains the question no. list of
											// this section
			String temp = "";
			String temp2 = "";
			String temp4 = "";
			boolean check = false;
			boolean exitInner = false;
			Date d = new Date();

			// System.out.print("[2]\n");

			for (int i = 0; i < fileReader.length; i++) {
				// System.out.println();
				// Find out the random question list.
				randomQuestionList = sectionFileDescription[i][3].trim();
				questionSequenceIndex = 0;
				presentQuestionNo = 0;
				nextQuestionNo = 0;

				// Find out the total no. of questions
				for (int k = 0; k < randomQuestionList.length(); k++)
					if (randomQuestionList.charAt(k) == ',')
						questionSequenceIndex++;

				questionSequence = new int[questionSequenceIndex];
				questionSequenceIndex = 0;

				// Find out all the question no. in the list and
				// store them in an array
				dummynum1 = 0;
				for (int k = 0; k < randomQuestionList.length(); k++) {
					if (randomQuestionList.charAt(k) == ',') {
						questionSequence[questionSequenceIndex++] = getIntValue(randomQuestionList
								.substring(dummynum1, k));
						dummynum1 = k + 1;
					}
				}

				// Now Arrange the question in ascending order
				for (int k1 = 0; k1 < questionSequenceIndex - 1; k1++)
					for (int k2 = k1 + 1; k2 < questionSequenceIndex; k2++)
						if (questionSequence[k1] > questionSequence[k2]) {
							int temp3 = questionSequence[k1];
							questionSequence[k1] = questionSequence[k2];
							questionSequence[k2] = temp3;
						}
				// System.out.print("==> ");
				for (int k1 = 0; k1 < questionSequenceIndex; k1++) {
					// System.out.print("[" + questionSequence[k1] + "] ");
				}
				// System.out.println("");

				// The next question no. to be recorded by cjpcs
				nextQuestionNo = questionSequence[0];
				questionSequenceIndex = 0;

				// System.out.print("["+i+"]");
				getOut: do {
					temp = fileReader[i].readLine();
					temp4 = "";
					if (temp == null)
						break;
					// System.out.print(temp);
					temp2 = "";
					for (int j = 0; j < temp.length(); j++) {
						// Check whether we have got a question
						if (temp2.length() >= 3) {
							if (temp2.substring(j - 3, j).equals("Q. ")) {
								presentQuestionNo++;
								// System.out.print("/"+presentQuestionNo);

								// Check whether the question no. is
								// the one we were looking for
								check = false;
								for (int zz = 0; zz < questionSequence.length; zz++)
									if (questionSequence[zz] == presentQuestionNo) {
										check = true;
										break;
									}

								if (presentQuestionNo == nextQuestionNo | check) {

									// System.out.print("[" + presentQuestionNo
									// + "]");
									// If we have to record this question
									// then first of all store those part of
									// the current question that has already
									// been read.
									temp4 = temp
											.substring(j - 3, temp.length());

									// System.out.print("{3}");
									exitInner = false;
									do {
										temp4 = temp4 + '\r' + '\n';
										if (exitInner)
											break;

										// System.out.println(fileReader[i].readLine());
										// System.out.println("--"+i+"--");
										temp = fileReader[i].readLine();
										// System.out.println(temp);
										// fileWriter.println("<!" + temp);
										// if (temp == null) {
										// System.out.print("[DARN}");
										// break getOut;
										// }
										// System.out.println(temp4+"#");

										for (int jj = 0; jj < temp.length(); jj++) {
											// Check whether we have got a
											// answer
											if (temp4.length() >= 7) {
												// System.out.println("{-4-}"+temp4+"\n");
												// //.substring(temp4.length()-7,temp4.length()));
												// long fuck1 = d.getTime();
												if (temp4.substring(
														temp4.length() - 7,
														temp4.length()).equals(
														"ANSWER:")) {
													// System.out.print("{2}");

													// Now we are in the answer
													// section
													// Since we are using the
													// readline method.
													// so the full statement
													// will definitely contain
													// the complete set of
													// answer. So we
													// will end our task here by
													// writing the data
													// collected so far into the
													// output file.
													if (jj + 1 <= temp.length())
														temp4 = temp4
																+ temp
																		.substring(
																				jj + 1,
																				temp
																						.length());

													fileWriter
															.println(temp4 + '\r' + '\n');

													// Now that the file writing
													// part is over
													// we can extract portions
													// out of the question read
													// and store them in an
													// array
													// We will pass the section
													// number as an argument.
													storeQuestioninMemory(i,
															temp4);
													questionCount++;

													// nextQuestionNo =
													// questionSequence[++questionSequenceIndex];
													temp4 = "";
													exitInner = true;
													break;
												}
												// else {
												// temp4 = temp4 +
												// temp.charAt(jj);
												// }
											}
											temp4 = temp4 + temp.charAt(jj);
										}
									} while (true);
								}
							}
						}
						temp2 = temp2 + temp.charAt(j);
						// System.out.print(temp2);
					}
				} while (true);

				sectionFileDescription[i][2] = String
						.valueOf(totalSectionQuestions);
				totalSectionQuestions = 0;
				// */
			}
		} catch (FileNotFoundException e) {
			System.out.println("" + e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println("error!@#$\n" + e);
		}

		try {
			for (int i = 0; i < fileReader.length; i++)
				fileReader[i].close();
			fileWriter.close();
		} catch (IOException e) {
			new MessageBox(BaseFrame, "Unable to close files\n\nDescription\n"
					+ e);
			System.exit(0);
		}
	}

	private static int getIntValue(String num) {
		// System.out.println("Got = [" + num + "]");
		return Integer.valueOf(num).intValue();
	}

	// This function reads the complete mixed up question/exhibit/answer
	// text and assigns them into the currentExamQuestionList array
	private static void storeQuestioninMemory(int sectionNo, String mixedUp) {

		boolean isExhibit = false;
		int exhibitPos = 0;
		boolean isOptions = false;
		int optionsPos = 0;
		boolean isAnswer = false;
		int answerPos = 0;
		String tempQuestion = "";
		String tempExhibit = "";
		String tempOptions = "";
		String tempAnswer = "";
		currentExamQuestionList[questionCount][0] = "";
		currentExamQuestionList[questionCount][1] = "";
		currentExamQuestionList[questionCount][2] = "";
		currentExamQuestionList[questionCount][3] = "";
		currentExamQuestionList[questionCount][4] = "";
		currentExamQuestionList[questionCount][5] = "";
		currentExamQuestionList[questionCount][6] = "";
		currentExamQuestionList[questionCount][7] = "";

		exhibitPos = mixedUp.indexOf("<EXHIBIT>");
		if (exhibitPos != -1)
			isExhibit = true;

		optionsPos = mixedUp.indexOf("A. ");
		if (optionsPos != -1)
			isOptions = true;

		answerPos = mixedUp.indexOf("ANSWER:");
		if (answerPos != -1)
			isAnswer = true;

		// Set the section no.
		currentExamQuestionList[questionCount][0] = (new Integer(sectionNo))
				.toString();

		// Now we will extract the question
		if (isExhibit) {
			tempQuestion = mixedUp.substring(0, exhibitPos - 1);
		} else if (isOptions) {
			tempQuestion = mixedUp.substring(0, optionsPos - 1);
		} else if (isAnswer) {
			tempQuestion = mixedUp.substring(0, answerPos - 1);
		}
		// Set the question
		currentExamQuestionList[questionCount][1] = tempQuestion.substring(3,
				tempQuestion.length()).trim();

		// Now we will extract the exhibit
		if (isExhibit) {
			if (isOptions) {
				tempExhibit = mixedUp.substring(exhibitPos, optionsPos - 1);
			} else if (isAnswer) {
				tempExhibit = mixedUp.substring(exhibitPos, answerPos - 1);
			}
		}
		// Set the exhibit
		currentExamQuestionList[questionCount][2] = tempExhibit.trim();

		// Now we will extract the options
		if (isOptions) {
			if (isAnswer) {
				tempOptions = mixedUp.substring(optionsPos, answerPos - 1);
			}
		}
		// Set the options
		currentExamQuestionList[questionCount][3] = tempOptions.trim();

		// Now we will extract the answer
		if (isAnswer) {
			tempAnswer = mixedUp.substring(answerPos, mixedUp.length());
		}
		// Set the options
		currentExamQuestionList[questionCount][4] = tempAnswer.substring(7,
				tempAnswer.length()).trim();

		currentExamQuestionList[questionCount][5] = "";
		currentExamQuestionList[questionCount][6] = "";

		// currentExamQuestionList
		// section no.
		// Question
		// Exhibit
		// Options
		// Answer key
		// Status - Answer given list (a,b,c,d / a / 0x6)
		// Marked - Marked
	}

	// Program to track the user movement.
	public void actionPerformed(ActionEvent evt) {

		// Check if the user has pressed the grade button
		if (evt.getSource() == Grade) {
			saveAnswerState();
			if ((new ConfirmBox(
					BaseFrame,
					"This will end your exam session and will initiate the grading process \n\n You won't be able to modify your answers then \n\n Are you sure ?")).Confirmation) {
				gradeSession();
			}
		}

		// Check if the user has pressed the exhibit button
		if (evt.getSource() == Exhibit) {
			if (currentExamQuestionList[currentQuestionNo - 1][2].length() > 0) {
				new MessageBox(BaseFrame,
						currentExamQuestionList[currentQuestionNo - 1][2]);
			}
		}

		// Check if the user has pressed previous marked question
		if (evt.getSource() == PreviousMarkedQuestion & currentQuestionNo > 1) {
			for (int i = currentQuestionNo - 1; i > 0; i--) {
				if (currentExamQuestionList[i - 1][6].equals("Marked")) {
					/*
					 * //BaseFrame.pack(); //BaseFrame.setSize(800,400);
					 * Dimension d = BaseFrame.getSize();
					 * //BaseFrame.setVisible(false); constructContainer();
					 * //BaseFrame.pack(); BaseFrame.doLayout();
					 * BaseFrame.setSize(d); //BaseFrame.setVisible(true);
					 */
					saveAnswerState();
					currentQuestionNo = i;
					makeLayout();
					break;
				}
			}
		}

		// Check if the user has pressed next marked question
		if (evt.getSource() == NextMarkedQuestion
				& currentQuestionNo < (currentExamQuestionList.length)) {
			for (int i = currentQuestionNo + 1; i < 59; i++) {
				if (currentExamQuestionList[i - 1][6].equals("Marked")) {
					/*
					 * constructContainer(); //BaseFrame.pack();
					 * //BaseFrame.setSize(800,400); Dimension d =
					 * BaseFrame.getSize(); //BaseFrame.setVisible(false);
					 * constructContainer(); //BaseFrame.pack();
					 * BaseFrame.doLayout(); BaseFrame.setSize(d);
					 * //BaseFrame.setVisible(true);
					 */
					saveAnswerState();
					currentQuestionNo = i;
					makeLayout();
					break;
				}
			}

		}

		// Check if the user has pressed MarkQuestion button
		if (evt.getSource() == MarkQuestion) {
			// System.out.println("Mark Question");
			if (currentExamQuestionList[currentQuestionNo - 1][6]
					.equals("Marked"))
				currentExamQuestionList[currentQuestionNo - 1][6] = "     ";
			else
				currentExamQuestionList[currentQuestionNo - 1][6] = "Marked";
			/*
			 * //BaseFrame.pack(); //BaseFrame.setSize(800,400); Dimension d =
			 * BaseFrame.getSize(); //BaseFrame.setVisible(false);
			 * constructContainer(); //BaseFrame.pack(); BaseFrame.doLayout();
			 * BaseFrame.setSize(d); //BaseFrame.setVisible(true);
			 */
			saveAnswerState();
			makeLayout();
		}

		// Check if the user has pressed the previous button
		if (evt.getSource() == PreviousQuestion) {
			if (currentQuestionNo <= 1)
				return;
			/*
			 * saveAnswerState(); currentQuestionNo--; Dimension d =
			 * BaseFrame.getSize(); //BaseFrame.setVisible(false);
			 * constructContainer(); //BaseFrame.pack(); BaseFrame.doLayout();
			 * BaseFrame.setSize(d); //BaseFrame.setVisible(true);
			 */
			saveAnswerState();
			currentQuestionNo--;
			makeLayout();
		}

		// Check if the user has pressed the next button
		if (evt.getSource() == NextQuestion) {
			if (currentQuestionNo >= 59)
				return;
			/*
			 * saveAnswerState(); currentQuestionNo++; Dimension d =
			 * BaseFrame.getSize(); //BaseFrame.setVisible(false);
			 * constructContainer(); //BaseFrame.pack(); BaseFrame.doLayout();
			 * BaseFrame.setSize(d); //BaseFrame.setVisible(true);
			 */
			saveAnswerState();
			currentQuestionNo++;
			makeLayout();
		}
	}

	// Function to do the task of making the layout visible
	private void makeLayout() {
		Dimension d = BaseFrame.getSize();
		constructContainer();
		// BaseFrame.pack();
		BannerContainer.doLayout();
		AnswerBoardContainer.doLayout();
		BaseFrame.doLayout();
		BaseFrame.setSize(d);
	}

	// function to construct containers
	private void constructContainer() {

		// Manage the exhibit button
		// System.out.println("["+currentExamQuestionList[currentQuestionNo-1][2]+"]");
		if (currentExamQuestionList[currentQuestionNo - 1][2].length() > 0) {
			Exhibit.setForeground(Color.black);
			// System.out.println("black");
		} else {
			Exhibit.setForeground(Color.gray);
			// System.out.println("lightgray");
		}

		// Manage the question number
		questionNumber = new Label("Question no: "
				+ (new Integer(currentQuestionNo)).toString());

		// Manage the mark/unmarked label
		if (currentExamQuestionList[currentQuestionNo - 1][6].equals("Marked")) {
			markLabel = new Label("Marked");
			// System.out.println("Marked");
		} else {
			markLabel = new Label("      ");
			// System.out.println("*******");
		}

		BannerContainer.removeAll();
		BannerContainer.setLayout(BannerLayout);
		timeLeft.setFont(new Font("Serif", Font.BOLD, 15));
		timeLeft.setForeground(Color.blue);
		questionNumber.setFont(new Font("SansSerif", Font.BOLD, 15));
		markLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
		BannerContainer.setBackground(Color.cyan);
		BannerContainer.add(timeLeft);
		BannerContainer.add(questionNumber);
		BannerContainer.add(markLabel);
		BannerContainer.add(Exhibit);
		BannerContainer.add(Grade);

		// Clear the current text of the option board and
		// set the question and the options on the
		// option board
		OptionBoard.setText("");
		OptionBoard.setText(currentExamQuestionList[currentQuestionNo - 1][1]
				+ '\r' + '\n' + '\r' + '\n');
		OptionBoard.setText(OptionBoard.getText()
				+ currentExamQuestionList[currentQuestionNo - 1][3] + '\r'
				+ '\n');

		// Count the number of options available
		int totalOptions = 0;
		char optionChar = 65;
		// do {
		// totalOptions++;
		// } while
		// ((currentExamQuestionList[currentQuestionNo-1][3].indexOf(optionChar++
		// + ": ")) != -1);
		// System.out.println(currentExamQuestionList[currentQuestionNo-1][3]);
		// System.out.println("[pass1]");
		for (int i = 0; i < currentExamQuestionList[currentQuestionNo - 1][3]
				.length(); i++) {
			if (currentExamQuestionList[currentQuestionNo - 1][3].charAt(i) == '.') {
				if (currentExamQuestionList[currentQuestionNo - 1][3]
						.charAt(i - 1) >= 'A'
						& currentExamQuestionList[currentQuestionNo - 1][3]
								.charAt(i - 1) <= 'I') {
					totalOptions++;
				}
			}
		}

		int totalAnswer = 1;
		// System.out.println("[pass2]");
		for (int i = 0; i < currentExamQuestionList[currentQuestionNo - 1][4]
				.length(); i++)
			if (currentExamQuestionList[currentQuestionNo - 1][4].charAt(i) == ',')
				totalAnswer++;
		// System.out.print("<"+ totalAnswer +">");

		/*
		 * //System.out.println("===============================");
		 * //System.out.println("Question no. : " + (currentQuestionNo));
		 * //System.out.println("Total options: " + totalOptions);
		 * //System.out.println("Total answers: " + totalAnswer);
		 * //System.out.println("-------------------------------\n");
		 */

		// Reset the answer board
		// System.out.println("[pass3]");
		AnswerBoardContainer.removeAll();
		AnswerBoardContainer.setFont(AnswerBoardFont);
		AnswerBoardContainer.setLayout(AnswerBoardLayout);
		AnswerBoardContainer.setFont(AnswerBoardFont);
		AnswerBoardContainer.setBackground(Color.lightGray);
		AnswerBoardContainer.setForeground(Color.red);

		// System.out.println("[pass4]");
		// Now assign null values to all the checkbox objects
		for (int i = 0; i < 9; i++)
			AnswerCheckBox[i] = null;

		// for(int i=0; i<9; i++)
		// AnswerRadioBox[i] = null;

		if (totalOptions == 0) {
			// Textfield case

			// Reset the answer board
			AnswerBoardContainer.setLayout(new GridLayout(3, 1));

			// Reset the answertext text field
			AnswerText
					.setText(currentExamQuestionList[currentQuestionNo - 1][5]);

			// Add the components
			AnswerBoardContainer.add(new Label(""));
			AnswerBoardContainer.add(AnswerText);
			AnswerBoardContainer.add(new Label(""));

		} else if (totalAnswer > 1) {
			// Multiselect case

			// Initialize the checkboxes
			optionChar = 'A';
			for (int i = 0; i < totalOptions; i++) {
				// System.out.print("=");
				AnswerCheckBox[i] = new Checkbox(optionChar++ + ": ");
				AnswerBoardContainer.add(AnswerCheckBox[i]);
			}
			for (int i = totalOptions; i < 9; i++) {
				// System.out.print("[["+i+"]]");
				AnswerBoardContainer.add(new Label(""));
			}
		} else {
			// Single Select case

			// Initialize the checkboxes
			optionChar = 'A';
			for (int i = 0; i < totalOptions; i++) {
				AnswerCheckBox[i] = new Checkbox(optionChar++ + ": ",
						AnswerGroup, false);
				AnswerBoardContainer.add(AnswerCheckBox[i]);
			}
			for (int i = totalOptions; i < 9; i++) {
				// System.out.print("["+i+"]");
				AnswerBoardContainer.add(new Label(""));
			}
		}
		if (totalOptions == 0) {
			// System.out.println("[Textfield]");
			// Textfield case
			AnswerText
					.setText(currentExamQuestionList[currentQuestionNo - 1][5]);
		} else {
			// System.out.println("[Select]");
			int temp1 = 0;
			int temp2 = currentExamQuestionList[currentQuestionNo - 1][5]
					.indexOf(",", temp1);
			temp1 = temp2 + 1;
			int temp3 = 0;

			// System.out.print("\n\nSearching: ["+temp2+"]");
			if (temp2 != -1) {
				// System.out.print("[InsideSelect]");
				do {
					selectOuter: for (optionChar = 65; optionChar < (totalOptions + 65); optionChar++) {
						// System.out.print("["+optionChar+"]["+currentExamQuestionList[currentQuestionNo-1][5].substring(temp3,temp2)+"]");
						if (currentExamQuestionList[currentQuestionNo - 1][5]
								.substring(temp3, temp2)
								.equals("" + optionChar)) {
							AnswerCheckBox[optionChar - 65].setState(true);
							temp3 = temp2 + 1;
							break selectOuter;
						}
					}
					temp2 = currentExamQuestionList[currentQuestionNo - 1][5]
							.indexOf(",", temp1);
					temp1 = temp2 + 1;
					// System.out.print("["+temp2+"]");
				} while (temp2 != -1);
			}
		}
		// System.out.println('\n');
	}

	private void saveAnswerState() {
		int totalOptions = 0;
		char optionChar = 65;
		for (int i = 0; i < currentExamQuestionList[currentQuestionNo - 1][3]
				.length(); i++) {
			if (currentExamQuestionList[currentQuestionNo - 1][3].charAt(i) == '.') {
				if (currentExamQuestionList[currentQuestionNo - 1][3]
						.charAt(i - 1) >= 'A'
						& currentExamQuestionList[currentQuestionNo - 1][3]
								.charAt(i - 1) <= 'I') {
					totalOptions++;
				}
			}
		}

		currentExamQuestionList[currentQuestionNo - 1][5] = "";
		if (totalOptions == 0) {
			// Textfield case
			currentExamQuestionList[currentQuestionNo - 1][5] = AnswerText
					.getText();
		} else {
			optionChar = 64;
			for (int i = 0; i < totalOptions; i++) {
				optionChar++;
				if (AnswerCheckBox[i].getState())
					currentExamQuestionList[currentQuestionNo - 1][5] = currentExamQuestionList[currentQuestionNo - 1][5]
							+ optionChar + ",";
			}
		}
		// System.out.println("{" + currentQuestionNo +
		// currentExamQuestionList[currentQuestionNo-1][5] + "}");
	}

	// Function to find out the overall result of the user.
	// After the over all performance has been graded
	// the user should be given the option to view as to which questions
	// he did wrong.
	private void gradeSession() {

		// Initialize the answer given by the user & the answer prescribed
		// by the section database into a variable
		String answerKey = "";
		String answerGiven = "";
		String msg = "";
		int temp1 = 0;
		int temp2 = 0;
		int numberOfAnswerKey = 0;
		int numberOfAnswerGiven = 0;
		int totalAnswer = 0;
		int totalOptions = 0;
		char optionChar = 65;

		// Cycle through each and every element
		for (int i = 0; i < currentExamQuestionList.length; i++) {
			answerKey = currentExamQuestionList[i][4] + ",";
			answerGiven = currentExamQuestionList[i][5];
			numberOfAnswerKey = 0;
			numberOfAnswerGiven = 0;

			// totalAnswer = 1;
			// for (int ii=0; ii < currentExamQuestionList[i][4].length(); ii++)
			// if (currentExamQuestionList[i][4].charAt(ii) == ',')
			// totalAnswer++;

			// Count the total number of options
			// [for text field business]
			totalOptions = 0;
			optionChar = 65;
			for (int ii = 0; ii < currentExamQuestionList[i][3].length(); ii++) {
				if (currentExamQuestionList[i][3].charAt(ii) == '.') {
					if (currentExamQuestionList[i][3].charAt(ii - 1) >= 'A'
							& currentExamQuestionList[i][3].charAt(ii - 1) <= 'I') {
						totalOptions++;
					}
				}
			}

			msg = msg + "\n";
			if (answerGiven.length() == 0) {
				// since the length of the answergiven text is empty
				// so the answer has been unattempted
				msg = msg + "Question no. " + (i + 1) + ": [Unattempted] "
						+ answerGiven + "[" + numberOfAnswerKey + "]["
						+ numberOfAnswerGiven + "]";
				currentExamQuestionList[i][7] = "Unattempted";
			} else {
				temp2 = 0;
				for (int j = 0; j < answerKey.length(); j++)
					if (answerKey.indexOf(",", temp2) != -1) {
						numberOfAnswerKey++;
						temp2 = answerKey.indexOf(",", temp2) + 1;
					}
				temp2 = 0;
				for (int j = 0; j < answerGiven.length(); j++)
					if (answerGiven.indexOf(",", temp2) != -1) {
						numberOfAnswerGiven++;
						temp2 = answerGiven.indexOf(",", temp2) + 1;
					}

				// Check whether the number of answer keys and number of
				// answer is equal or not. If not equal then check
				// whether the total options defined is greater than 0 or not
				// As because in case of text field the total number of
				// answers in answer key and total number of answers in the
				// answer given can be different.
				if ((numberOfAnswerKey != numberOfAnswerGiven)
						& (totalOptions > 0)) {
					// Since the number of commas in answerkey is not equal
					// to the number of commas in the answer given so the
					// answer itself is wrong
					msg = msg + "\nQuestion no. " + (i + 1)
							+ ": [Wrong - commas do not tally]["
							+ numberOfAnswerKey + "][" + numberOfAnswerGiven
							+ "]";
					currentExamQuestionList[i][7] = "Wrong";
				} else {
					// Since the number of commas in answerkey is equal
					// to the number of commas in the answer given so there
					// is a possibility of question being right

					// Extract one comma at a time from the answer key
					// and then check the answer in the answer key for
					// its existence in the answer key
					if (numberOfAnswerKey == 1 & numberOfAnswerGiven == 1
							& totalOptions > 0) {

						// Remove any blank spaces from the begining
						// and ending of the answers
						answerKey.trim();
						answerGiven.trim();

						// Remove the "," from the end of the answers
						answerKey = answerKey.substring(0,
								answerKey.length() - 1);
						answerGiven = answerKey.substring(0, answerGiven
								.length() - 1);

						// System.out.println("[baba]" + (i+1));
						if (answerKey.equals(answerGiven)) {
							msg = msg + "\nQuestion no. " + (i + 1)
									+ ": [Right - single select****]";
							currentExamQuestionList[i][7] = "Right";
						} else {
							msg = msg + "\nQuestion no. " + (i + 1)
									+ ": [Wrong - single select]";
							currentExamQuestionList[i][7] = "Wrong";
						}
					} else if (numberOfAnswerKey > 1 & numberOfAnswerGiven > 1
							& totalOptions > 0) {
						// Case of multiselect answers

						// Now extract each and every answer from the answer key
						// and check whether the answer is present in the answer
						// given
						int temp3 = 0;
						int temp4 = answerKey.indexOf(",");
						int temp5 = 0;
						int temp6 = 0;
						int temp7 = 0;
						int temp8 = 0;
						do {
							temp5 = 0;
							temp6 = answerGiven.indexOf(",", temp5);
							temp7 = 0;
							do {
								if (answerKey.substring(temp3, temp4 - 1)
										.equals(
												answerGiven.substring(temp5,
														temp6 - 1))) {
									temp7 = 1;
									break;
								}
								temp5 = temp6 + 1;
								temp6 = answerGiven.indexOf(",", temp5);
							} while (temp6 != -1);

							// If any key not found then the answer is wrong
							if (temp7 == 0) {
								msg = msg
										+ "\nQuestion no. "
										+ (i + 1)
										+ ": [Wrong - multi select option not found]"
										+ answerKey.substring(temp3, temp4 - 1)
										+ "[" + answerKey + "][" + answerGiven
										+ "]";
								currentExamQuestionList[i][7] = "Wrong";
								break;
							} else if (temp7 == 1) {
								temp8++;
							}

							temp3 = temp4 + 1;
							temp4 = answerKey.indexOf(",", temp3);
						} while (temp4 != -1);

						if (temp8 == numberOfAnswerKey) {
							msg = msg + "\nQuestion no. " + (i + 1)
									+ ": [Right - multi select *****]";
							currentExamQuestionList[i][7] = "Right";
						} else {
							msg = msg + "\nQuestion no. " + (i + 1)
									+ ": [Wrong - multi select]";
							currentExamQuestionList[i][7] = "Wrong";
						}

					} else {
						// System.out.println("[Textfield]["+numberOfAnswerKey+"]["+numberOfAnswerGiven+"]["+totalOptions+"]["+answerKey+"]["+answerGiven+"]");
						if (numberOfAnswerKey >= 1 & numberOfAnswerGiven == 0
								& totalOptions == 0) {
							// Case of text field answer

							// Now check whether the answer given exists in the
							// answer key
							int temp3 = 0;
							answerGiven = answerGiven + ",";
							int temp4 = answerKey.indexOf(",", temp3);

							do {
								// System.out.print("["+answerKey.substring(temp3,
								// temp4)+"]");
								// System.out.println("["+answerGiven+"]");
								if (answerKey.substring(temp3, temp4).equals(
										answerGiven.substring(0, answerGiven
												.length() - 1))) {
									msg = msg + "\nQuestion no. " + (i + 1)
											+ ": [Right - text field *****]";
									currentExamQuestionList[i][7] = "Right";
									break;
								}
								temp3 = temp4 + 1;
								temp4 = answerKey.indexOf(",", temp3);
							} while (temp4 != -1);

							if (!currentExamQuestionList[i][7].equals("Right")) {
								msg = msg + "\nQuestion no. " + (i + 1)
										+ ": [Wrong - text field]";
								currentExamQuestionList[i][7] = "Wrong";
							}
						}
					}
				}
			}
		}

		// Define the section dimensions.
		String sectionList[][] = new String[11][5];
		// name
		// total right
		// total wrong
		// total unattempted
		// total percentage right

		sectionList[0][0] = "Declaration & Access control       ";
		sectionList[1][0] = "Flow Control & Exception handling  ";
		sectionList[2][0] = "Garbage Collection                 ";
		sectionList[3][0] = "Language Fundamentals              ";
		sectionList[4][0] = "Operators & Assignments            ";
		sectionList[5][0] = "OverLoading & Overridding          ";
		sectionList[6][0] = "Threads                            ";
		sectionList[7][0] = "java.awt                           ";
		sectionList[8][0] = "java.lang                          ";
		sectionList[9][0] = "java.util                          ";
		sectionList[10][0] = "java.io                            ";

		for (int i = 0; i < 11; i++) {
			sectionList[i][1] = "0";
			sectionList[i][2] = "0";
			sectionList[i][3] = "0";
			sectionList[i][4] = "0";
		}

		// System.out.println("Grading in process...");
		String gradeMsg = "";
		int sectionTotal = 0;
		int correctTotal = 0;
		int totalCorrectTotal = 0;
		int totalSectionTotal = 0;
		int percentagetotal = 0;
		// Now grade the correct answers.
		for (int i = 0; i < 11; i++) {
			// Count the total number of right and wrong questions
			correctTotal = 0;
			sectionTotal = 0;
			// System.out.print("\n[["+i+"]]");
			for (int j = 0; j < currentExamQuestionList.length; j++) {
				// System.out.print("["+j+"]" + currentExamQuestionList[j][0]);
				if (getIntValue(currentExamQuestionList[j][0]) == i) {
					// System.out.print("[.]");
					sectionTotal++;
					if (currentExamQuestionList[j][7].equals("Right")) {
						// System.out.print("[.."+sectionList[i][1]+"]");
						correctTotal++;
						sectionList[i][1] = ""
								+ (getIntValue(sectionList[i][1]) + 1);
					} else if (currentExamQuestionList[j][7].equals("Wrong")) {
						// System.out.print("[..."+sectionList[i][2]+"]");
						sectionList[i][2] = ""
								+ (getIntValue(sectionList[i][2]) + 1);
					} else if (currentExamQuestionList[j][7]
							.equals("Unattempted")) {
						// System.out.print("[...."+sectionList[i][3]+"]");
						sectionList[i][3] = ""
								+ (getIntValue(sectionList[i][3]) + 1);
					}
				}
			}
			percentagetotal = (int) ((((float) correctTotal) / ((float) sectionTotal)) * 100);
			// System.out.print("correctTotal/sectionTotal = " + (((float)
			// correctTotal)/((float) sectionTotal))*100 + " ");
			// System.out.print("("+percentagetotal+")");
			sectionList[i][4] = "" + percentagetotal;
			totalCorrectTotal += correctTotal;
			totalSectionTotal += sectionTotal;
			// System.out.print("["+sectionList[i][4]+"]");
			// System.out.print("["+correctTotal+"]");
			// System.out.println("["+sectionTotal+"]");
			gradeMsg += '\n' + sectionList[i][0] + " ====> "
					+ sectionList[i][1] + " ====> " + sectionList[i][2]
					+ " ====> " + sectionList[i][3];

			// Now store the result in the array
			gradingData[i][0] = sectionList[i][0];
			gradingData[i][1] = sectionList[i][1];
			gradingData[i][2] = sectionList[i][2];
			gradingData[i][3] = sectionList[i][3];
			gradingData[i][4] = ""
					+ ((int) ((((float) getIntValue(sectionList[i][1])) / (float) (getIntValue(sectionList[i][1])
							+ getIntValue(sectionList[i][2]) + getIntValue(sectionList[i][3]))) * 10000) / 100)
					+ "%";
			// 1 2 3 4 43 3 4 4 4 4 4 4 3 2 1
		}
		gradeMsg += "\n\nTOTAL PERCENTAGE = "
				+ ((int) ((((float) totalCorrectTotal) / ((float) totalSectionTotal)) * 100));

		// Find out the total percentage and the sum total
		float rt = 0, wt = 0, ut = 0;
		for (int i = 0; i < 11; i++) {
			rt = rt + getIntValue(sectionList[i][1]);
			wt = wt + getIntValue(sectionList[i][2]);
			ut = ut + getIntValue(sectionList[i][3]);
		}
		gradingData[11][1] = "" + (int) rt;
		gradingData[11][2] = "" + (int) wt;
		gradingData[11][3] = "" + (int) ut;
		gradingData[11][4] = ""
				+ (((int) ((rt / (rt + wt + ut)) * 10000)) / 100) + "%";
		// 1 2 2 23 4 43 2 1

		// System.out.println(rt);
		// System.out.println(rt + wt+ ut);
		// System.out.println(rt/(rt + wt+ ut));
		// System.out.println(((rt/(rt + wt+ ut)) * 100));

		// Ask the user if he wants to see his report in the form of a
		// textual listing or by way of swing.
		if ((new ConfirmBox(
				BaseFrame,
				"Do you want to see a textual listing of report, so that you can copy & paste it into another file for any future reference ?")).Confirmation) {
			new MessageBox(BaseFrame, "" + gradeMsg);
			new MessageBox(BaseFrame, "" + msg);
		} else {
			JTable table = new JTable(gradingData, gradingColumnNames);
			JScrollPane scrollableTable = new JScrollPane(table);
			JFrame mainWindow = new JFrame("Exam Statistics");
			mainWindow.setContentPane(scrollableTable);
			mainWindow.addWindowListener(new WindowAdapter() {
				// Invoked when the user clicks the close box.
				public void windowClosing(WindowEvent evt) {
					dispose();
				}
			});
			mainWindow.pack();
			mainWindow.setSize(300, 200);
			mainWindow.setVisible(true);
		}

		// new MessageBox(BaseFrame, "file named " +
		// sectionFileDescription[i][0] + " not found.\n\n Please download the
		// file once again...3");
	}
	// public void paint(Graphics g) {}
	// public void repaint(Graphics g) {}
	// public void update(Graphics g) {}
}

class Conventions extends Dialog implements ActionListener, WindowListener {

	Conventions(Frame parent) {

		super(parent, "Java Programmer Exam Conventions", true);

		// Create the conventions dialog
		TextArea ConventionTextArea = new TextArea("", 10, 50,
				TextArea.SCROLLBARS_VERTICAL_ONLY);
		ConventionTextArea.setEditable(false);
		Button close = new Button("Close");
		close.addActionListener(this);
		addWindowListener(this);

		// Create the text
		String ConventionText[] = {
				"Conventions Used in the Sun Certified Java Programmer Examination\n",
				"The Java Programmer Examination includes the conventions described below. Some of them are intended to shorten the text that is displayed, therefore reducing the amount of reading required for each question. Other Conventions help to provide consistency through the examination.\n\n",
				"Conventions for Code\n",
				"The code samples that are presented for you to study include line numbers. You should assume that the line numbers are not part of the source files, and therefore will not cause compilation errors.\n\n",
				"Line numbers that begin with 1 indicate that a complete source file is shown. In contrast, if line numbers start with some other value, you should assume that the omitted code would cause the code sample to compile correctly, and that it does not have any unexpected effects on the code in the question. So, for example, you should not choose an answer that says \"The code does not compile\" based only on the fact that you do not see a required import statement in the code sample that is presented.\n\n",
				"In general, code of eight lines or less will be presented in the body of the question. If more than eight lines are needed, the code will be presented in an exhibit, and you will need to click the \"Exhibit\" button to see the code. Since you generally cannot see both the exhibited code and the question simultaneously due to limited screen size, you should read the question first, and use notepaper as necessary when considering an answer.\n\n",
				"Conventions for Questions\n",
				"When a question includes a code sample and asks \"What is the result?\" or something similar, you should consider what happens if you attempt to compile the code and then run it. This type of question admits the possibility that the code might not compile, or if it compiles, that it might not run. You should assume that all necessary support is given to the compilation and run phases (for example, that the CLASSPATH variable is appropriately set). Therefore, you should only examine possible causes of error in the information that is presented to you, and ignore information that is omitted.\n\n",
				"Some of the possible answers use a form like this: \"An error at line 5 causes compilation to fail.\" If you see this, you should consider whether the line in question is either a syntax error, or if it is inconsistent with some other part of the program and therefore misrepresents the program's clear intent. You should choose an answer of this type if the root of the problem is at the specified line, regardless of where any particular compiler might actually report an error.\n\n",
				"Some questions might ask \"Which answers are true?\" or something similar. If an answer is worded like \"An exception can be thrown,\" or \"An exception may be thrown,\" then you should choose this answer if what it describes is possible, rather than disregarding it because the situation does not always occur. In contrast, if an option discusses something that \"must\" occur, then you should choose it only if there are no conditions under which the observation is untrue.\n\n",
				"In multiple choice questions that require you to pick more than one answer, you will be told how many options to choose, and the options will be presented as checkboxes. In questions that require you to pick only one answer, the possible answers will be presented with radio buttons that effectively prevent you from selecting more than one answer.\n\n" };

		String CT = "";
		for (int i = 0; i < ConventionText.length; i++)
			CT = CT + ConventionText[i];

		// Insert the text.
		ConventionTextArea.setText(CT);

		// Add the components to the dialog
		setLayout(new BorderLayout());
		add(ConventionTextArea, BorderLayout.CENTER);
		add(close, BorderLayout.SOUTH);

		// Make the dialog visible
		pack();
		setSize(800, 400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		dispose();
	}

	// Invoked when the user clicks the close-box
	public void windowClosing(WindowEvent evt) { // (6)
		dispose();
		System.exit(0);
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

class MessageBox extends Dialog implements ActionListener, WindowListener {

	// Create a dummy frame
	MessageBox(Frame parent, String Message) {
		// Create the dialog box.
		super(parent, "Message", true);

		// Create the conventions dialog
		TextArea MsgTextArea = new TextArea("", 10, 50,
				TextArea.SCROLLBARS_VERTICAL_ONLY);
		MsgTextArea.setEditable(false);
		MsgTextArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
		Button close = new Button("Close");
		close.addActionListener(this);
		addWindowListener(this);

		// Create the text
		String CT = Message;

		// Insert the text.
		MsgTextArea.setText(CT);

		// Add the components to the dialog
		setLayout(new BorderLayout());
		add(MsgTextArea, BorderLayout.CENTER);
		add(close, BorderLayout.SOUTH);

		// Make the dialog visible
		pack();
		setSize(200, 200);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
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

class timer implements Runnable {
	static TextField tf;
	long currentMillis = System.currentTimeMillis(); // starting time
	String currentTime = "TIME: 00:00:00";
	String lastTime = currentTime;
	static Thread th;
	long startMillis = System.currentTimeMillis();

	public timer(TextField tf2) {
		th = new Thread(this);
		tf = tf2;
		th.start();
	}

	public synchronized void run() {
		long hours = 0;
		long minutes = 0;
		long seconds = 0;
		tf.setText(currentTime);

		while (true) {
			if (!lastTime.equals(currentTime)) {
				tf.setText(currentTime);
				lastTime = currentTime;
			}
			currentMillis = System.currentTimeMillis();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			currentMillis = System.currentTimeMillis() - startMillis;
			seconds = (int) currentMillis / 1000;
			minutes = (int) (seconds / 60);
			seconds = seconds - (minutes * 60);
			hours = (int) (minutes / 60);
			minutes = minutes - (hours * 60);
			while (true) {
				if (seconds < 60)
					break;

				minutes++;
				seconds -= 60;
			}

			while (true) {
				if (minutes < 60)
					break;

				hours++;
				minutes -= 60;
			}
			currentTime = "TIME: " + (hours < 10 ? "0" : "") + hours + ":"
					+ (minutes < 10 ? "0" : "") + minutes + ":"
					+ (seconds < 10 ? "0" : "") + seconds;
			// System.out.println("["+hours+"]["+minutes+"]["+seconds+"]["+currentTime+"]["+currentMillis+"]");
		}
	}
}

class showSplash extends Applet implements ActionListener, WindowListener {

	// Frame splashFrame = new Frame("Comprehensive Java Programmer
	// Certification Simulator");
	Dialog splashDialog;
	Button close = new Button("Close");
	Toolkit currentTK;
	Image image;
	LoadedImage lim;

	// ****************** Splash showing section ******************
	showSplash(Frame f) {
		splashDialog = new Dialog(f,
				"Comprehensive Java Programmer Certification Simulator", true);
		splashDialog.setLayout(new BorderLayout());
		splashDialog.add(close, BorderLayout.SOUTH);
		close.addActionListener(this);
		splashDialog.addWindowListener(this);

		// image handling
		currentTK = Toolkit.getDefaultToolkit();
		image = currentTK.getImage("Logo.jpg");
		lim = new LoadedImage(image);
		Panel comp = new Panel();
		comp.setBackground(Color.black);
		comp.add(lim);
		splashDialog.add(comp, BorderLayout.CENTER);

		splashDialog.pack();
		splashDialog.setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		splashDialog.dispose();
		return;
	}

	// Invoked when the user clicks the close-box
	public void windowClosing(WindowEvent evt) { // (6)
		splashDialog.dispose();
		System.exit(0);
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

class LoadedImage extends Canvas {
	Image img;

	public LoadedImage(Image i) {
		set(i);
	}

	void set(Image i) {
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(i, 0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e) {
		}
		;
		img = i;
		repaint();
	}

	public void paint(Graphics g) {
		if (img == null) {
			g.drawString("no image", 10, 30);
		} else {
			g.drawImage(img, 0, 0, this);
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(img.getWidth(this), img.getHeight(this));
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
}
