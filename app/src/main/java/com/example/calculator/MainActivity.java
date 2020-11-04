package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.example.calculator.databinding.ActivityMainBinding;
import com.example.calculator.listeners.ClearListener;
import com.example.calculator.listeners.NumberListener;
import com.example.calculator.listeners.OperatorListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot(); // gets the root of the layout (LinearLayout)
        setContentView(view);

        NumberListener numberListener = new NumberListener(binding.txtDisplay);
        OperatorListener operatorListener = new OperatorListener(numberListener, binding.txtHistory);
        ClearListener clearListener = new ClearListener(numberListener, operatorListener);

        binding.btn0.setOnClickListener(numberListener);
        binding.btn1.setOnClickListener(numberListener);
        binding.btn2.setOnClickListener(numberListener);
        binding.btn3.setOnClickListener(numberListener);
        binding.btn4.setOnClickListener(numberListener);
        binding.btn5.setOnClickListener(numberListener);
        binding.btn6.setOnClickListener(numberListener);
        binding.btn7.setOnClickListener(numberListener);
        binding.btn8.setOnClickListener(numberListener);
        binding.btn9.setOnClickListener(numberListener);
        binding.btnDot.setOnClickListener(numberListener);
        binding.btnChangeSign.setOnClickListener(numberListener);

        binding.btnPlus.setOnClickListener(operatorListener);
        binding.btnMinus.setOnClickListener(operatorListener);
        binding.btnMultiply.setOnClickListener(operatorListener);
        binding.btnDivide.setOnClickListener(operatorListener);
        binding.btnEquals.setOnClickListener(operatorListener);

        binding.btnClearAll.setOnClickListener(clearListener);
        binding.btnClearCurrent.setOnClickListener(clearListener);
        binding.btnBackspace.setOnClickListener(clearListener);

        binding.txtHistory.setMovementMethod(new ScrollingMovementMethod());
        binding.txtDisplay.setMovementMethod(new ScrollingMovementMethod());
    }
}