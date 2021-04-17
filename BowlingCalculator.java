/*
 * Written by Rocky Peterson
 * JavaSE-13 & jdk-14.0.2
 * Last modified: 4/12/2021
*/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BowlingCalculator extends Frame {
	private String frameTitle;
	private Point frameOffset;
	private Color fontColor, backgroundColor;
	private Panel layoutBlock, instructionBlock, inputBlock, resultsBlock, buttonBlock;
	private Label calcResultLabel;
	private int calcResultValue = 0;
	private Button resultsButton;
	private char[] validInputCharacters = {'x','s','0','1','2','3','4','5','6','7','8','9'};
	private List<CustomTextField> scoreInputFields;
	
	public Font plainFont = new Font("Verdana",Font.PLAIN,14);
	public Font boldFont = new Font("Verdana",Font.BOLD,14);
	
	public BowlingCalculator(String inTitle, Point inOffset, Color inFontColor, Color inBackgroundColor) {
		this.frameTitle = inTitle;
		this.frameOffset = inOffset;
		this.fontColor = inFontColor;
		this.backgroundColor = inBackgroundColor;
		initialize();
	}
	
	public BowlingCalculator() {
		this.frameTitle = "Bowling Score Calculator";
		this.frameOffset = new Point(100,100);
		this.fontColor = Color.BLACK;
		this.backgroundColor = Color.WHITE;
		initialize();
	}
	
	private void initialize() {
		// Set Frame properties
		setLocation(frameOffset);
		setBackground(backgroundColor);
		setForeground(fontColor);
		setVisible(true);
		
		// Build Panels to display
		buildBlocks();
		
		// Set Frame extents to encapsulate child elements
		pack();
		
		// Add listener to close window
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	private void updateResults() {
		//System.out.println(resultsBlock.getComponent(1));
		
		// Set calculator results to 0, or previous calc results will accumulate
		calcResultValue = 0;
		
		// step through the values to calculate final score
		for(int i = 0; i < scoreInputFields.size(); i++) {
			if(scoreInputFields.get(i).isEnabled() && !scoreInputFields.get(i).getText().isBlank()) {
				calcResultValue += getTotalScore(scoreInputFields.get(i));
			}
		}
		
		//calcResultValue = getTotalScore(scoreInputFields.get(0));
		calcResultLabel.setText(Integer.toString(calcResultValue));
	}
	
	private int getTotalScore(CustomTextField traverseNode) {
		int points = 0;
		String str = traverseNode.getText();
		
		if(str.equals("x") && traverseNode.getStepsToLastNode() > 1) {
			points = 10;
			
			if(traverseNode.nextNode != null)
				points += getScoreStep(traverseNode.nextNode,2);
		}
		else if(str.equals("s")) {
			points = 10;
			
			if(traverseNode.nextNode != null)
				points += getScoreStep(traverseNode.nextNode,1);
		}
		else {
			if(Character.isDigit(str.charAt(0))) {
				if(traverseNode.nextNode != null && !traverseNode.nextNode.getText().isBlank() && traverseNode.nextNode.getText().equals("s"))
					points = 0;
				else
					points = Character.getNumericValue(str.charAt(0));
			}
		}
		
		return points;
	}
	
	private int getScoreStep(CustomTextField node, int numberOfRollsAhead) {
		if(numberOfRollsAhead > 0) {
			if(!node.isEnabled()) {
				if(node.nextNode != null)
					return getScoreStep(node.nextNode,numberOfRollsAhead);
				else
					return 0;
			}
			else {
				String str = node.getText();
				
				if(str.isBlank()) {
					return 0;
				}
				else {
					if(str.equals("x") || str.equals("s")) {
						if(node.nextNode != null)
							return 10 + getScoreStep(node.nextNode,numberOfRollsAhead - 1);
						else
							return 10;
					}
					else {
						if(node.nextNode != null)
							return Character.getNumericValue(str.charAt(0) + getScoreStep(node.nextNode,numberOfRollsAhead - 1));
						else
							return Character.getNumericValue(str.charAt(0));
					}
				}
			}
		}
		else {
			return 0;
		}
	}
	
	private void buildBlocks() {
		// Main Panel, for display organization
		layoutBlock = new Panel();
		layoutBlock.setLayout(new GridLayout(5,1));
		layoutBlock.setFont(plainFont);
		
		buildInstructionBlock();
		buildInputBlock();
		buildResultsBlock();
		buildButtonBlock();
		
		// Bottom cushion
		layoutBlock.add(new Label(""));
		
		add(layoutBlock);
	}
	
	private void buildInstructionBlock() {
		instructionBlock = new Panel();
		instructionBlock.setLayout(new GridLayout(5,1));
				
		// local variable created to set specific label font
		Label instructionLabelText = new Label("INSTRUCTIONS",Label.CENTER);
		instructionLabelText.setFont(boldFont);
		instructionBlock.add(instructionLabelText);
		instructionLabelText = new Label("To calculate your Total Score, enter the amount of pins knocked over per round for each frame played below.",Label.CENTER);
		instructionBlock.add(instructionLabelText);
		instructionBlock.add(new Label("X = Strike",Label.CENTER));
		instructionBlock.add(new Label("S = Spare",Label.CENTER));
		instructionBlock.add(new Label("0-9 = Pins knocked down",Label.CENTER));
		
		layoutBlock.add(instructionBlock);
	}
	
	private void buildInputBlock() {
		// local variable created to set specific label font
		Label inputLabelText;
		
		inputBlock = new Panel();
		inputBlock.setLayout(new GridLayout(2,22));
		
		inputLabelText = new Label("Frame: ",Label.RIGHT);
		inputLabelText.setFont(boldFont);
		inputBlock.add(inputLabelText);
		
		// add labels with text 1-10 to indicate bowling frame number
		for(int i=0; i<10; i++) {
			// space buffer to align layout
			inputBlock.add(new Label(""));
			// frame number
			inputBlock.add(new Label(Integer.toString(i+1),Label.CENTER));
		}
		// buffer for right side
		inputBlock.add(new Label(""));
		// extra label to create buffer on right end of frame
		inputBlock.add(new Label(""));
		
		inputLabelText = new Label("Round: ",Label.RIGHT);
		inputLabelText.setFont(boldFont);
		inputBlock.add(inputLabelText);
		
		// Create and Add Custom Text Fields
		scoreInputFields = new ArrayList<CustomTextField>();
		for(int i=0; i<21; i++) {
			scoreInputFields.add(new CustomTextField(validInputCharacters));
			if(i%2 == 0)
				scoreInputFields.get(i).isFirstInFrame = true;
			
			if(i > 0) {
				// set previous node to allow CustomTextField objs to let each other know when input should be enabled
				scoreInputFields.get(i).previousNode = scoreInputFields.get(i-1); 
				scoreInputFields.get(i).setEnabled(false);
				scoreInputFields.get(i).setText("<<");
			}
			scoreInputFields.get(i).setFont(boldFont);
			inputBlock.add(scoreInputFields.get(i));
		}
		
		// set frame 10 round 2 variable isFirstInFrame to true, is used for strike processing condition. All 3 rounds in frame 10 = true
		scoreInputFields.get(scoreInputFields.size()-1).isFirstInFrame = false;
		//System.out.println(scoreInputFields.get(scoreInputFields.size()-1).isFirstInFrame+"  <<should be false");
		
		// set next node to allow CustomTextField objs to let each other know when input should be enabled
		for(int i=19; i>=0; i--) {
			scoreInputFields.get(i).nextNode = scoreInputFields.get(i+1);
		}
		
		inputBlock.add(new Label(""));
		layoutBlock.add(inputBlock);
	}
	
	private void buildResultsBlock() {
		resultsBlock = new Panel();
		resultsBlock.setLayout(new GridLayout(1,2));
		
		// local variable created to set specific label font
		Label resultsLabelText = new Label("Final Score: ",Label.RIGHT);
		resultsLabelText.setFont(boldFont);
		resultsBlock.add(resultsLabelText);
		
		calcResultLabel = new Label(Integer.toString(calcResultValue),Label.LEFT);
		calcResultLabel.setFont(boldFont);
		resultsBlock.add(calcResultLabel);
		
		layoutBlock.add(resultsBlock);
	}
	
	private void buildButtonBlock() {
		buttonBlock = new Panel();
		buttonBlock.setLayout(new GridLayout(1,3));
		
		resultsButton = new Button("Click here to Calculate / Update your score");
		resultsButton.setFont(boldFont);
		
		resultsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("You pushed the BUTTON, thanks");
				updateResults();
			}
		});
		
		buttonBlock.add(new Label(""));
		buttonBlock.add(resultsButton);
		buttonBlock.add(new Label(""));
		layoutBlock.add(buttonBlock);
	}
	
}
