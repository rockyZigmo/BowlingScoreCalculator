/*
 * Written by Rocky Peterson
 * JavaSE-13 & jdk-14.0.2
 * Last modified: 4/12/2021
*/

import java.awt.*;
import java.awt.event.*;

public class CustomPanel extends Panel{
	private Point grid; 
	//private char[] approvedCharacters;
	//private final CustomTextField[] scoreInputFields;
	
	public CustomPanel(Point gridArrangement) {
		super();
		this.grid = gridArrangement;
		//this.approvedCharacters = charactersAllowedForInput;
		//this.scoreInputFields = new CustomTextField[numberOfRounds];
		initialize();
	}
	
	private void initialize() {
		setLayout(new GridLayout(grid.x,grid.y));
		/*
		for(TextField inputField : scoreInputFields) {
			inputField = new CustomTextField(approvedCharacters);
			add(inputField);
		}
		*/
	}
}



/*  This has been abandoned for now  */