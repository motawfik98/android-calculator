package com.example.calculator.listeners;

import android.view.View;
import android.widget.TextView;

import com.example.calculator.R;

import java.util.Locale;

public class OperatorListener implements View.OnClickListener {
    private NumberListener numberListener;
    private TextView history;


    private StringBuilder currentHistory = new StringBuilder();
    private double currentValue = 0;
    private boolean divisionByZero = false;

    public OperatorListener(NumberListener numberListener, TextView history) {
        this.numberListener = numberListener;
        this.history = history;
    }

    @Override
    public void onClick(View v) {
        int currentId = v.getId();
        performOperation();
        if (numberListener.getLastOperation() == Operation.NONE) {
            currentValue = numberListener.getCurrentValue(); // if it's the first time the user opens the app, then set the newValue to be the currentValue
        }
        if (divisionByZero && numberListener.getCurrentValue() == 0) {
            numberListener.zeroDivisionError();
            return;
        } else {
            divisionByZero = false;
        }

        if (currentId == R.id.btn_plus) { // the plus button was clicked
            // if there's an unfinished operation
            if (numberListener.isHangingOperation() && !numberListener.isEqualButtonPressed())
                replaceLastOperation(Operation.ADD, "+"); // change the operation to be add operation
            else
                add(); // perform add logic
        } else if (currentId == R.id.btn_minus) { // the minus button was clicked
            // if there's an unfinished operation, and this operation is not a subtract operation
            if (numberListener.isHangingOperation() && !numberListener.isEqualButtonPressed())
                replaceLastOperation(Operation.SUBTRACT, "-"); // change the operation to be subtract operation
            else
                subtract(); // perform subtract logic
        } else if (currentId == R.id.btn_multiply) { // the multiply button was clicked
            // if there's an unfinished operation, and this operation is not a multiply operation
            if (numberListener.isHangingOperation() && !numberListener.isEqualButtonPressed())
                replaceLastOperation(Operation.MULTIPLY, "X"); // change the operation to be multiply operation
            else
                multiply(); // perform multiply logic
        } else if (currentId == R.id.btn_divide) { // the divide button was clicked
            // if there's an unfinished operation, and this operation is not a divide operation
            if (numberListener.isHangingOperation() && !numberListener.isEqualButtonPressed())
                replaceLastOperation(Operation.DIVIDE, "/"); // change the operation to be division operation
            else
                divide(); // perform division logic
        } else if (currentId == R.id.btn_equals) { // equals button was clicked
            if (!numberListener.isHangingOperation() && !numberListener.isEqualButtonPressed())
                finishCalculating();
        }
    }

    private void performOperation() {
        double newValue = numberListener.getCurrentValue(); // gets the new value that the user entered
        if (numberListener.getLastOperation() == Operation.ADD && !numberListener.isHangingOperation()) {
            currentValue += newValue; // if the last operation button the user entered was "ADD" and he entered any number after it, add the 2 numbers
        } else if (numberListener.getLastOperation() == Operation.SUBTRACT && !numberListener.isHangingOperation()) {
            currentValue -= newValue; // if the last operation button the user entered was "SUBTRACT" and he entered any number after it, subtract the 2 numbers
        } else if (numberListener.getLastOperation() == Operation.MULTIPLY && !numberListener.isHangingOperation()) {
            currentValue *= newValue; // if the last operation button the user entered was "MULTIPLY" and he entered any number after it, add multiply 2 numbers
        } else if (numberListener.getLastOperation() == Operation.DIVIDE && !numberListener.isHangingOperation()) {
            if (newValue == 0)
                divisionByZero = true;
            else
                currentValue /= newValue; // if the last operation button the user entered was "DIVIDE" and he entered any number after it, divide the 2 numbers
        } else if (numberListener.getLastOperation() == Operation.NONE) {
            currentValue = newValue; // if it's the first time the user opens the app, then set the newValue to be the currentValue
        }
    }

    private void add() {
        updateDisplayAndHistory(" + "); // update the display and history to show the result
        numberListener.setLastOperation(Operation.ADD); // sets the last operation to "ADD"
    }

    private void subtract() {
        updateDisplayAndHistory(" - "); // update the display and history to show the result
        numberListener.setLastOperation(Operation.SUBTRACT); // sets the last operation to "SUBTRACT"
    }

    private void multiply() {
        updateDisplayAndHistory(" X "); // update the display and history to show the result
        numberListener.setLastOperation(Operation.MULTIPLY); // sets the last operation to "MULTIPLY"
    }

    private void divide() {
        updateDisplayAndHistory(" / "); // update the display and history to show the result
        numberListener.setLastOperation(Operation.DIVIDE); // sets the last operation to "DIVIDE"

    }

    private void finishCalculating() {
        updateDisplayAndHistory(" = ");
        numberListener.setLastOperation(Operation.EQUALS);
        numberListener.setEqualButtonPressed(true);
    }

    private void updateDisplayAndHistory(String operationUsed) {
        String newStringValue = numberListener.getCurrentDisplayValue().toString(); // gets the new value that the user entered
        if (newStringValue.equals(""))
            newStringValue = "0";
        String currentStringValue;

        if (numberListener.isClearHistory()) { // check if the history should be deleted
            currentHistory = new StringBuilder(); // reset the history to empty string
            history.setText(currentHistory); // reset the textView to make it empty
        }
        if (numberListener.isEqualButtonPressed()) { // check if the "equal" button was pressed prior to this click
            currentHistory = new StringBuilder(newStringValue).append(operationUsed); // add the current number with the equal sign in the history
            currentStringValue = newStringValue;
        } else {
            if (currentValue % 1 != 0) // check if the number contains decimal places
                currentStringValue = String.format(Locale.getDefault(), "%.4f", currentValue); // format the number to only have 4 decimal places
            else
                currentStringValue = currentValue + "";
            currentHistory.append(newStringValue).append(operationUsed); // add the value and the new operation that the user wants to execute
        }

        numberListener.valueAfterOperation(currentStringValue); // send the result to be displayed to the user
        history.setText(currentHistory); // update the history
    }

    public void clearAll() {
        currentHistory = new StringBuilder(); // reset the history to empty string
        history.setText(currentHistory); // reset the textView to make it empty
        currentValue = 0; // reset the current value to zero
        divisionByZero = false;
    }

    private void replaceLastOperation(Operation newOperation, String symbol) {
        currentHistory.replace(currentHistory.length() - 2, currentHistory.length() - 1, symbol); // replace the character (before the last one) with the new operator
        history.setText(currentHistory); // update the text view
        numberListener.setLastOperation(newOperation); // update the lastOperation variable
    }
}

enum Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE, EQUALS, NONE;
}
