/**
 * 
 * This is the final project main class file it is a calculator
 * 
 * @author Albi Zhaku
 * @date 12/15/2022
 */

package application;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CalculatorProject extends Application {

	@FXML
	Text text;

	static String equation = "";
	static boolean equals = false;

	@Override
	/*
	 * start method
	 * 
	 * @param primaryStage - takes in the primary stage for the Calculator
	 * 
	 */
	public void start(Stage primaryStage) throws IOException {
		URL fmxLoader = getClass().getResource("CalculatorUI.fxml");
		FXMLLoader loader = new FXMLLoader(fmxLoader);

		Parent root = loader.load();
		primaryStage.setTitle("Calculator");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		primaryStage.setResizable(false);

	}

	/*
	 * Main method
	 */
	public static void main(String[] args) {
		launch(args);

	}

	/*
	 * onNumPad method
	 * 
	 * @param e - Action Event
	 * 
	 * It is the function that essentially does the work for the numbers
	 */
	public void onNumPad(ActionEvent e) {
		if (!equals == true) {

			if (text.getText().equals("Error")) {
				text.setText("");
			}

			if (text.getText().length() < 11) {

				if (text.getText().equals("0") && text.getText().length() == 1
						&& !((((Button) (e.getSource())).getText()).equals("."))) {
					text.setText("");
				}

				text.setText(text.getText() + (((Button) e.getSource()).getText()));
				equation += (((Button) e.getSource()).getText());

			}
		} else {
			text.setText((((Button) e.getSource()).getText()));
			equals = false;
		}
	}

	/*
	 * onEqualSign method
	 * 
	 * @param e - Action Event
	 * 
	 * It is the method that does the final calculation and presents it
	 */

	public void onEqualSign(ActionEvent e) {
		equals = true;
		try {
			String postfix = Postfix.convertToPostfix(equation + " ");

			StackInterface<Double> numbers = new LinkedStack<>();
			String number = "";

			for (int i = 0; i < postfix.length(); i++) {

				char nextCharacter = postfix.charAt(i);

				if (nextCharacter == '|') {
					if (!number.isEmpty()) {

						numbers.push(Double.valueOf(number));
						number = "";
					}
				} else {
					if (Character.isDigit(nextCharacter) || nextCharacter == '.') {
						number += nextCharacter;
					}
				}

				// If it is a digit we will push into the number stacks
				if (!Character.isDigit(nextCharacter) && nextCharacter != '|' && nextCharacter != '.') {
					Double operandTwo = numbers.pop();
					Double operandOne = numbers.pop();

					Double result = compute(operandOne, operandTwo, nextCharacter);

					numbers.push(result);

				}
			}

			Double result = numbers.pop();

			String resultTxt = String.valueOf(result);

			if (resultTxt.length() > 11) {
				text.setText("Error");
			} else {

				if (result % 1 > 0 || result % 1 < 0) {
					text.setText(resultTxt);
				} else {
					text.setText(String.valueOf(Math.round(result)));
				}
				equation = text.getText();
			}

		} catch (Exception e1) {
			text.setText("Error");
			equation = "";

		}

	}

	/*
	 * onFunctionPad method
	 * 
	 * @param e - Action Event
	 * 
	 * It is the method that will basically add + ^ - / * and C functions to the
	 * equation and program
	 */
	public void onFunctionPad(ActionEvent e) {
		equals = false;
		if ((((Button) e.getSource()).getText()).equals("C")) {
			text.setText("0");
			equation = "";
		} else {
			text.setText("0");

			equation += (((Button) e.getSource()).getText());
		}
	}

	/*
	 * compute method
	 * 
	 * @param e - Action Event
	 * 
	 * @return Double
	 * 
	 * Used it from the Postfix files essentially does the calculation and stores
	 * the final result in the numbers stacks
	 */

	public static Double compute(Double operandOne, Double operandTwo, char operator) {
		double result;

		switch (operator) {
		case '+':
			result = operandOne.doubleValue() + operandTwo.doubleValue();
			break;

		case '-':
			result = operandOne.doubleValue() - operandTwo.doubleValue();
			break;

		case '*':
			result = operandOne.doubleValue() * operandTwo.doubleValue();
			break;

		case '/':
			result = operandOne.doubleValue() / operandTwo.doubleValue();
			break;

		case '^':
			result = Math.pow(operandOne.doubleValue(), operandTwo.doubleValue());
			break;

		default: // Unexpected character
			result = 0;
			break;
		} // end switch

		return result;
	} // end compute
}
