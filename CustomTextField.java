/*
 * Written by Rocky Peterson
 * JavaSE-13 & jdk-14.0.2
 * Last modified: 4/13/2021
*/

import java.lang.*;
import java.awt.*;
import java.awt.event.*;

public class CustomTextField extends TextField {
	public CustomTextField previousNode;
	public CustomTextField nextNode;
	public boolean isFirstInFrame = false;
	private char[] validCharacters;
	
	public CustomTextField (char[] acceptedInputCharacters) {
		super(1);
		this.validCharacters = acceptedInputCharacters;
		initialize();
	}
	
	private void initialize() {
		// make sure "valid" character array characters exist in lowercase format
		ensureCharacterArrayIsLowercase();
		
		// Check incoming character and disregard if not a valid character
		KeyListener kListener = new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				char keyChar = ke.getKeyChar();
				int keyInt = ke.getKeyCode();
				
				if (((TextField)(ke.getComponent())).getText().length() < 1) {
					// flag to determine if key in valid character array
					boolean isCharacterAllowed = false;
					
					// Validate key input, is in validCharacters array?
					for(char vc : validCharacters) {
						if(keyChar == vc)
							isCharacterAllowed = true;
					}
					
					if(isCharacterAllowed) {
						if(((CustomTextField)(ke.getComponent())).isFirstInFrame) {
							if(keyChar == 's')
								ke.consume();
							else {
								onTextEntered(keyChar);
								return;
							}
						}
						else {
							if(keyChar == 'x') {
								if(getStepsToLastNode() > 2)
									ke.consume();
								else
									onTextEntered(keyChar);
									return;
							}
							else if(keyChar == 's') {
								if(!Character.isDigit(((CustomTextField)(ke.getComponent())).previousNode.getText().charAt(0)))
									ke.consume();
								else {
									onTextEntered(keyChar);
									return;
								}
							}
							else {
								if(Character.isDigit(((CustomTextField)(ke.getComponent())).previousNode.getText().charAt(0))) {
									if(Character.getNumericValue(keyChar) > 9-Integer.parseInt(((CustomTextField)(ke.getComponent())).previousNode.getText()))
										ke.consume();
									else
										onTextEntered(keyChar);
										return;
								}
								else {
									onTextEntered(keyChar);
									return;
								}
							}
						}
					}
					else
						ke.consume();
				}
				else {
					// Check for backspace / delete / tab -- let these pass through
					switch (keyInt) {
						case KeyEvent.VK_BACK_SPACE:
							/*
							if(((TextField)(ke.getComponent())).getCaretPosition() == 1) {
								onTextDeleted();
							}
							*/
							onTextDeleted(keyChar);
							return;
						case KeyEvent.VK_DELETE:
							/*
							if(((TextField)(ke.getComponent())).getCaretPosition() == 0) {
								onTextDeleted();
							}
							*/
							return;
						case KeyEvent.VK_TAB:
							return;
						case KeyEvent.VK_ENTER:
							System.out.println("ENTER does nothing, try TAB");
							return;
						default: ke.consume();
					}
				}
			}
		};
		
		addKeyListener(kListener);
	}
	
	private void onTextEntered(char cKey) {
		//System.out.println("Text Entered = "+ckey);
		
		// unlock next CustomTextField to be accessed by user
		if(this.nextNode != null) {
			if(cKey == 'x') {
				// determine CustomTextField action for last frame, check 1,2,3 rounds ahead, only a concern for strikes (x) in frame 10 round 1
				if(this.nextNode.nextNode == null || this.nextNode.nextNode.nextNode == null) {
					this.nextNode.setEnabled(true);
					this.nextNode.setText("");
				}
				else {
					// this frame has closed, enable CustomTextField 2 steps ahead
					this.nextNode.nextNode.setEnabled(true);
					this.nextNode.nextNode.setText("");
				}
			}
			else {
				this.nextNode.setEnabled(true);
				this.nextNode.setText("");
			}
		}
	}
	
	private void onTextDeleted(char cKey) {
		//System.out.println("Text Deleted = "+cKey);
		
		// lock CustomTextFields to the right
		CustomTextField ctf = this.nextNode;
		while(ctf != null) {
			ctf.setText("<<");
			ctf.setEnabled(false);
			ctf = ctf.nextNode;
		}
	}
	
	public int getStepsToLastNode() {
		int tally = 0;
		
		if(this.nextNode != null) {
			CustomTextField nodeHolder = this.nextNode;
			while(nodeHolder != null) {
				tally += 1;
				nodeHolder = nodeHolder.nextNode; 
			}
		}
		else
			return 0;
		
		return tally;
	}
	
	private void ensureCharacterArrayIsLowercase() {
		// check for uppercase letters and update to lowercase
		for(int i=0; i<validCharacters.length; i++) {
			char ch = validCharacters[i];
			
			if(Character.isLetterOrDigit(ch)) {
				// set chars to lowercase
				if(Character.isUpperCase(ch))
					validCharacters[i] = Character.toLowerCase(ch);
			}
		}
	}
}
