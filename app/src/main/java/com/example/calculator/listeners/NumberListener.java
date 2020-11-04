package com.example.calculator.listeners;

import android.view.View;
import android.widget.TextView;

import com.example.calculator.R;

public class NumberListener implements View.OnClickListener {
    private double currentValue = 0;
    private TextView display;

    private StringBuilder currentDisplayValue = new StringBuilder();
    private boolean isDecimal = false;
    private Operation lastOperation = Operation.NONE;
    private boolean hangingOperation = false;
    private boolean equalButtonPressed = false;
    private boolean clearHistory = false;

    public NumberListener(TextView display) {
        this.display = display;
    }

    @Override
    public void onClick(View v) {
        int currentId = v.getId();
        if (currentId == R.id.btn0)
            appendClickedNumber("0");
        else if (currentId == R.id.btn1)
            appendClickedNumber("1");
        else if (currentId == R.id.btn2)
            appendClickedNumber("2");
        else if (currentId == R.id.btn3)
            appendClickedNumber("3");
        else if (currentId == R.id.btn4)
            appendClickedNumber("4");
        else if (currentId == R.id.btn5)
            appendClickedNumber("5");
        else if (currentId == R.id.btn6)
            appendClickedNumber("6");
        else if (currentId == R.id.btn7)
            appendClickedNumber("7");
        else if (currentId == R.id.btn8)
            appendClickedNumber("8");
        else if (currentId == R.id.btn9)
            appendClickedNumber("9");
        else if (currentId == R.id.btn_dot)
            appendClickedNumber(".");
        else if (currentId == R.id.btn_change_sign)
            invertCurrentNumberSign();
    }

    private void appendClickedNumber(String clickedNumber) {
        if (hangingOperation) { // if the last clicked button was an operation button
            currentValue = 0; // remove the current value (as the user will start typing a new number)
            currentDisplayValue = new StringBuilder(); // clear display
            isDecimal = false; // clear the decimal flag
            hangingOperation = false; // update the flag that indicates that the user has clicked a number button
        }
        if (equalButtonPressed) { // if the user types a number after pressing on the "equals" button
            clearAll(); // delete all the data (including the history)
        }

        if (clickedNumber.equals(".")) // if the user clicked the 'dot' button
            if (isDecimal) // if the number already contains a decimal point
                return; // return without doing anything
            else { // the number doesn't contain a decimal point
                if (currentDisplayValue.length() == 0) { // if the length of the number is '0' (this is the first button that the user clicks)
                    currentDisplayValue.append("0"); // append a 'zero' to the beginning of the sequence to avoid crashing
                }
                isDecimal = true; // mark the number as decimal
            }

        currentDisplayValue.append(clickedNumber); // append the clicked number to the sequence
        currentValue = Double.parseDouble(currentDisplayValue.toString()); // parse the string to a double

        setFormattedText();
    }

    public void valueAfterOperation(String value) {
        if (equalButtonPressed) {
            equalButtonPressed = false;
        } else { // if any operation was clicked (except for the equals)
            currentDisplayValue = new StringBuilder(value); //change display value to the new value
            setFormattedText(); // update the textView
            hangingOperation = true; // disable any more operations to be performed until user types a number
        }
        clearHistory = false; // add flag to clear the history
    }

    private void setFormattedText() {
        // if the value of the number is equal to zero (and there's no decimal points in the sequence)
        // then delete the last appended number (it will definitely be an extra zero at the beginning)
        if (currentValue == 0 && !isDecimal && currentDisplayValue.charAt(currentDisplayValue.length() - 1) != '.')
            currentDisplayValue.deleteCharAt(currentDisplayValue.length() - 1);

        display.setText(currentDisplayValue); // set the text of the textView
    }

    private void invertCurrentNumberSign() {
        if (currentDisplayValue.length() == 0 || hangingOperation)
            return; // if there's no number written, or the calculator is in a hangingOperation state (no need to negate the number)
        if (currentDisplayValue.charAt(0) == '-') // if the first character is a negative sign
            currentDisplayValue.deleteCharAt(0); // remove the first character (to convert the number to positive)
        else // the first character is not the negative sign
            currentDisplayValue.insert(0, '-'); // add the negative sign to the beginning of the sequence
        currentValue = Double.parseDouble(currentDisplayValue.toString()); // parse the string number to a double
        setFormattedText(); // set the value of the string to the textView
    }

    public StringBuilder getCurrentDisplayValue() {
        return currentDisplayValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void clearAll() {
        currentDisplayValue = new StringBuilder();
        display.setText(currentDisplayValue);
        currentValue = 0;
        isDecimal = false;
        lastOperation = Operation.NONE;
        hangingOperation = false;
        equalButtonPressed = false;
        clearHistory = true;
    }

    public void clearCurrent() { // clear the current text that the user is typing
        currentDisplayValue = new StringBuilder(); // clear the string that contains the text
        display.setText(currentDisplayValue); // sets the display to the empty string
        currentValue = 0; // remove the current value to avoid miscalculations
        isDecimal = false; // remove the decimal flag
        hangingOperation = true; // make the calculator in the hangingOperation state
    }

    public void clearLastDigit() {
        if (hangingOperation || currentDisplayValue.length() < 1)
            return;

        if (currentDisplayValue.charAt(currentDisplayValue.length() - 1) == '.')
            isDecimal = false; // check if the deleted character was the decimal point, then remove the decimal flag

        currentDisplayValue.setLength(currentDisplayValue.length() - 1); // delete the last letter of the string
        if (currentDisplayValue.length() == 0) { // if the length of the sequence reached zero
            clearCurrent(); // perform the same operations for the clearCurrent
        } else if (currentDisplayValue.toString().equals("-")) {
            clearCurrent(); // check if the last digit is just the negative symbol
        } else {
            currentValue = Double.parseDouble(currentDisplayValue.toString()); // parse the string to a double
            setFormattedText(); // set the new text to the textView
        }
    }

    public void zeroDivisionError() {
        display.setText(R.string.zero_division_error);
    }

    public Operation getLastOperation() {
        return lastOperation;
    }

    public void setLastOperation(Operation lastOperation) {
        this.lastOperation = lastOperation;
    }

    public boolean isHangingOperation() {
        return hangingOperation;
    }

    public boolean isEqualButtonPressed() {
        return equalButtonPressed;
    }

    public void setEqualButtonPressed(boolean equalButtonPressed) {
        this.equalButtonPressed = equalButtonPressed;
    }

    public boolean isClearHistory() {
        return clearHistory;
    }
}
