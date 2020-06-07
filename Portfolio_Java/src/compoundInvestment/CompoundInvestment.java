/**
 * @author John Wolf
 * Date March 2020
 * Course: ICS4U
 * CompoundInvestment.java
 * Calculates compound interest
 */
package compoundInvestment;

import java.util.Arrays;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import simpleIO.Console;

public class CompoundInvestment extends Application {
	// Formatting constants
	static final int GAP = 15;
	static double screenWidth;
	static double screenHeight;
	static final String LABEL_CSS = "-fx-font-family: Roboto; -fx-font-size: 24; -fx-text-fill: #12019d;";	
	
	// TextFields for user input
	JFXTextField yearlyPaymentField = new JFXTextField();
	JFXTextField interestField = new JFXTextField();
	JFXTextField yearsField = new JFXTextField();
	
	// Compounding Frequency Combo Box
	JFXComboBox<Label> compFreqCombo = new JFXComboBox<Label>();
	
	// Calculate button
	JFXButton btnCalculate = new JFXButton();
	
	// Return on Investment Label
	Label lblInvestmentReturn = new Label();

	// Stores the interest Rate (%)
	double interestRate = 0;
	
	// Stores the interest Rate ($)
	double yearlyPayment = 0;
	
	// Stores the number of years
	int years = 0;
	
	// Stores the compounding frequency choice
	String compFreq;

    @Override
    public void start(Stage myStage) throws Exception {
		// Get screen dimensions
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		screenWidth = primaryScreenBounds.getWidth();
		screenHeight = primaryScreenBounds.getHeight();
        
        /********* ROOT ********/
		// Set root formatting options
		VBox root = new VBox(3*GAP);
		root.setPadding(new Insets(GAP, GAP, GAP, GAP));
		root.setAlignment(Pos.TOP_CENTER);
		root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		/********* LOGO IMAGE ********/
		// Set logo imageview formatting options
		ImageView logoImg = new ImageView(new Image("/images/investoCalcLogo.png", screenWidth / 8, screenWidth / 8, true, true));
		root.getChildren().add(logoImg);
		
		/********* PRINCIPAL TEXTFIELD ********/
		// Use a regex validator to determine valid payment entries
		RegexValidator dollarAmountValid = new RegexValidator();
        
        // Format $XX.XX (dollar sign and decimal places (max 2) optional)
        dollarAmountValid.setRegexPattern("^(\\$)?(([0-9]+)(\\.[0-9]{2})?)$");
        dollarAmountValid.setMessage("Must be a valid dollar amount, format $XX.XX (dollar sign and decimal places (max 2) optional)");

		yearlyPaymentField.setPromptText("Regular Payment($)...");

        // Add validator
        yearlyPaymentField.getValidators().add(dollarAmountValid);

        yearlyPaymentField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
            	if(yearlyPaymentField.validate() == true) {
	            	String principalStr = yearlyPaymentField.getText();
	            	// If a valid entry has been entered, retrieve the yearly payment
	            	if(principalStr.startsWith("$") == true) {
	            		principalStr = principalStr.substring(1);
	            	}
	            	try {
	            		yearlyPayment = Double.valueOf(principalStr);
	            	} catch (NumberFormatException e) {
	            		// Do nothing
	            	}
            	}
            	checkEnableButton();
            }
        });

        yearlyPaymentField.setMaxWidth(screenWidth / 3);
        root.getChildren().add(yearlyPaymentField);

		/********* INTEREST TEXTFIELD ********/
        interestField.setPromptText("Interest Rate (%)...");
        
        // Use a regex validator to determine valid interest entries
        RegexValidator percentValid = new RegexValidator();

        // Format XX.XX% (percent sign and decimal places optional)
        percentValid.setRegexPattern("(^100(\\.0*)?%?$)|(^([1-9]([0-9])?|0)(\\.[0-9]*)?%?$)");
        percentValid.setMessage("Must be a valid percentage, format: XX.XX% (percent sign and decimal places optional)");

        // Add validator
        interestField.getValidators().add(percentValid);

        // Validate entries on the fly
        interestField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal == false) {
            	if(interestField.validate() == true) {
            		// If a valid entry has been entered, retrieve the interest rate
	            	String interestStr = interestField.getText();
	            	if(interestStr.endsWith("%") == true) {
	            		interestStr = interestStr.substring(0, interestStr.length() - 1);
	            	}
	            	try {
	            		interestRate = Double.valueOf(interestStr);
	            	} catch (NumberFormatException e) {
	            		// Do nothing
	            	}
            	}
            	checkEnableButton();
            }
        });
        interestField.setMaxWidth(screenWidth / 3);
        root.getChildren().add(interestField);
        
        /********* YEARS TEXTFIELD ********/
        yearsField.setPromptText("Years Invested...");
        
        // Use a regex validator to determine valid year entries
        RegexValidator yearsValid = new RegexValidator();

        yearsValid.setRegexPattern("^[0-9]+$");
        yearsValid.setMessage("Must be a valid number of years");

        // Add validator
        yearsField.getValidators().add(yearsValid);

        // Validate entries on the fly
        yearsField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal == false) {
            	if (yearsField.validate() == true) {
            		// If a valid entry has been entered, retrieve the number of years
	            	String yearsStr = yearsField.getText();
	            	try {
	            		years = Integer.valueOf(yearsStr);
	            	} catch (NumberFormatException e) {
	            		// Should never happen
	            	}
            	}
            	checkEnableButton();
            }
        });
        yearsField.setMaxWidth(screenWidth / 3);
        root.getChildren().add(yearsField);
        
        /********* COMPOUNDING FREQUENCY COMBOBOX ********/
        // Add options to comboBox
        compFreqCombo.getItems().add(new Label("Annually"));
        compFreqCombo.getItems().add(new Label("Semi-annually"));
        compFreqCombo.getItems().add(new Label("Quarterly"));
        compFreqCombo.getItems().add(new Label("Monthly"));
        compFreqCombo.getItems().add(new Label("Biweekly"));
        compFreqCombo.getItems().add(new Label("Weekly"));
         
        // Set combo box properties
        compFreqCombo.setPromptText("Select Compounding Frequency");
        compFreqCombo.setOnAction(event -> getCompoundingFrequency());
        compFreqCombo.setMaxWidth(screenWidth / 3);
        root.getChildren().add(compFreqCombo);
        
        /********* CALCULATE BUTTON ********/
		// Set the calculate button's formatting options
        btnCalculate.setText("CALCULATE!");
		btnCalculate.setRipplerFill(Color.WHITE);
		btnCalculate.setOnAction(event -> calculateTotal());
		btnCalculate.setDisable(true);
		
		root.getChildren().add(btnCalculate);
		
		/********* INVESTMENT RETURN LABEL ********/
		// Set Return on Investment Label formatting options
		lblInvestmentReturn.setStyle(LABEL_CSS);
		root.getChildren().add(lblInvestmentReturn);

		/********* GENERATE SCENE AND ADD IT TO THE STAGE ********/
		Scene scene = new Scene(root, screenWidth, screenHeight, false, SceneAntialiasing.BALANCED);

        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto:400,500,900&display=swap");
		
		// Load the application's CSS
		scene.getStylesheets().add("/CSS/compoundInvestment.css");

        myStage.setTitle("InvestoCalc");
        myStage.setScene(scene);
        Image applicationIcon = new Image("/images/investoCalcLogoNoText.png");
        myStage.getIcons().add(applicationIcon);
        myStage.setMaximized(true);
        myStage.show();


    }

    /**
     * Retrieves the compounding Frequency from compFreqCombo
     */
    public void getCompoundingFrequency() {
    	compFreq = compFreqCombo.getValue().getText();
    	checkEnableButton();
	}
    
    /**
     * Ensures all entered data is valid before enabling the calculateButton
     */
    public void checkEnableButton() {
    	if (yearlyPaymentField.validate() && interestField.validate() && yearsField.validate() && compFreq != null) {
    		btnCalculate.setDisable(false);
    	}
    	else {
    		btnCalculate.setDisable(true);
    	}
    }

    /**
	 * This method launches the UI
	 * @param args command-line arguments
	 */ 
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Calculates the final investment return based on the yearly payment,
     * years invested, interest rate and compounding frequency
     */
    public void calculateTotal() {
    	// Set the compFreqPeriod based on the user's compFreq choice
    	int compFreqPeriod = 0;
    	if (compFreq.equals("Annually")) {
    		compFreqPeriod = 1;
    	}
    	else if (compFreq.equals("Semi-annually")) {
    		compFreqPeriod = 2;
    	}
    	else if (compFreq.equals("Quarterly")) {
    		compFreqPeriod = 4;
    	}
    	else if (compFreq.equals("Monthly")) {
    		compFreqPeriod = 12;
    	}
    	else if (compFreq.equals("Biweekly")) {
    		compFreqPeriod = 26;
    	}
    	else if (compFreq.equals("Weekly")) {
    		compFreqPeriod = 52;
    	}
    	
    	// Create a 2D array to store calculation data
    	double[][] investment = new double[years][3];
    	
    	// Store investment total
    	double investmentTotal = 0;
    	for (int year = 0; year < years; year++) {
    		// Store the principal for each year
    		investment[year][0] = yearlyPayment + investmentTotal;
    		
    		// Use the compound interest formula to determine the amount made each year
    		investment[year][2] = investment[year][0] * Math.pow(1 + (interestRate/100)/compFreqPeriod, compFreqPeriod);
    		
    		// The interest is the difference between the two other values
    		investment[year][1] = investment[year][2] - investment[year][0];
    		
    		// Update the investmentTotal
    		investmentTotal = investment[year][2];
    	}
    	// Update label
    	lblInvestmentReturn.setText("Return on investment: $" + Console.roundDouble(investmentTotal, 2) + "");
    }

}
