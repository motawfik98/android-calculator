package com.motawfik.calculator.listeners;

import android.view.View;

import com.motawfik.calculator.R;

public class ClearListener implements View.OnClickListener {
    private NumberListener numberListener;
    private OperatorListener operatorListener;

    public ClearListener(NumberListener numberListener, OperatorListener operatorListener) {
        this.numberListener = numberListener;
        this.operatorListener = operatorListener;
    }

    @Override
    public void onClick(View v) {
        int currentId = v.getId();
        if (currentId == R.id.btn_clear_all) {
            numberListener.clearAll();
            operatorListener.clearAll();
        } else if (currentId == R.id.btn_clear_current) {
            if (numberListener.isEqualButtonPressed()) {
                numberListener.clearAll();
                operatorListener.clearAll();
            } else {
                numberListener.clearCurrent();
            }
        } else if (currentId == R.id.btn_backspace) {
            numberListener.clearLastDigit();
        }
    }
}
