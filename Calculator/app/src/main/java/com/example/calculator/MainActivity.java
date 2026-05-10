package com.example.calculator;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView expressionView;
    private TextView resultView;
    private String currentInput = "0";
    private double firstOperand = 0D;
    private String pendingOperator;
    private String expressionText = "0";
    private boolean shouldStartNewInput = true;
    private boolean lastActionWasEquals = false;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.##########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        expressionView = findViewById(R.id.tv_expression);
        resultView = findViewById(R.id.tv_result);
        bindDigitButtons();
        bindOperatorButtons();
        bindActionButtons();
        updateDisplay();
    }

    private void bindDigitButtons() {
        int[] digitButtonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        };

        for (int buttonId : digitButtonIds) {
            findViewById(buttonId).setOnClickListener(view -> appendDigit(((TextView) view).getText().toString()));
        }
        findViewById(R.id.btn_decimal).setOnClickListener(view -> appendDecimalPoint());
    }

    private void bindOperatorButtons() {
        findViewById(R.id.btn_add).setOnClickListener(view -> setOperator("+"));
        findViewById(R.id.btn_subtract).setOnClickListener(view -> setOperator("-"));
        findViewById(R.id.btn_multiply).setOnClickListener(view -> setOperator("×"));
        findViewById(R.id.btn_divide).setOnClickListener(view -> setOperator("÷"));
        findViewById(R.id.btn_equals).setOnClickListener(view -> evaluateResult());
    }

    private void bindActionButtons() {
        findViewById(R.id.btn_clear_entry).setOnClickListener(view -> clearEntry());
        findViewById(R.id.btn_clear_all).setOnClickListener(view -> clearAll());
        findViewById(R.id.btn_reciprocal).setOnClickListener(view -> applyReciprocal());

        ImageButton sqrtButton = findViewById(R.id.btn_sqrt);
        sqrtButton.setOnClickListener(view -> applySquareRoot());
    }

    private void appendDigit(String digit) {
        if (lastActionWasEquals && pendingOperator == null) {
            clearAll();
        }

        if (shouldStartNewInput || "0".equals(currentInput)) {
            currentInput = digit;
            shouldStartNewInput = false;
        } else {
            currentInput += digit;
        }
        lastActionWasEquals = false;
        updateDisplay();
    }

    private void appendDecimalPoint() {
        if (lastActionWasEquals && pendingOperator == null) {
            clearAll();
        }

        if (shouldStartNewInput) {
            currentInput = "0.";
            shouldStartNewInput = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        lastActionWasEquals = false;
        updateDisplay();
    }

    private void setOperator(String operator) {
        double inputValue = parseCurrentInput();
        if (pendingOperator != null && !shouldStartNewInput) {
            firstOperand = calculate(firstOperand, inputValue, pendingOperator);
            currentInput = formatNumber(firstOperand);
        } else {
            firstOperand = inputValue;
        }

        pendingOperator = operator;
        shouldStartNewInput = true;
        lastActionWasEquals = false;
        updateDisplay();
    }

    private void evaluateResult() {
        if (pendingOperator == null) {
            updateDisplay();
            return;
        }

        double secondOperand = parseCurrentInput();
        String expressionBeforeResult = formatNumber(firstOperand) + " " + pendingOperator + " " + formatNumber(secondOperand);
        if (("÷".equals(pendingOperator) || "%".equals(pendingOperator)) && secondOperand == 0D) {
            showError(expressionBeforeResult);
            return;
        }

        double result = calculate(firstOperand, secondOperand, pendingOperator);
        currentInput = formatNumber(result);
        firstOperand = result;
        pendingOperator = null;
        shouldStartNewInput = true;
        expressionText = expressionBeforeResult + " =";
        lastActionWasEquals = true;
        updateDisplay();
    }

    private void clearEntry() {
        currentInput = "0";
        shouldStartNewInput = true;
        lastActionWasEquals = false;
        updateDisplay();
    }

    private void clearAll() {
        currentInput = "0";
        firstOperand = 0D;
        pendingOperator = null;
        expressionText = "0";
        shouldStartNewInput = true;
        lastActionWasEquals = false;
        updateDisplay();
    }

    private void applyReciprocal() {
        double value = parseCurrentInput();
        String unaryExpression = "1/(" + formatNumber(value) + ")";
        if (value == 0D) {
            showError(unaryExpression);
            return;
        }

        expressionText = unaryExpression;
        currentInput = formatNumber(1D / value);
        shouldStartNewInput = true;
        pendingOperator = null;
        lastActionWasEquals = true;
        updateDisplay();
    }

    private void applySquareRoot() {
        double value = parseCurrentInput();
        String unaryExpression = "√(" + formatNumber(value) + ")";
        if (value < 0D) {
            showError(unaryExpression);
            return;
        }

        expressionText = unaryExpression;
        currentInput = formatNumber(Math.sqrt(value));
        shouldStartNewInput = true;
        pendingOperator = null;
        lastActionWasEquals = true;
        updateDisplay();
    }

    private double calculate(double left, double right, String operator) {
        switch (operator) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "×":
                return left * right;
            case "÷":
                return left / right;
            case "%":
                return left % right;
            default:
                return right;
        }
    }

    private double parseCurrentInput() {
        return Double.parseDouble(currentInput);
    }

    private void showError(String failedExpression) {
        currentInput = "0";
        firstOperand = 0D;
        pendingOperator = null;
        expressionText = failedExpression;
        shouldStartNewInput = true;
        lastActionWasEquals = true;
        expressionView.setText(expressionText);
        resultView.setText("Error");
    }

    private void updateDisplay() {
        expressionView.setText(buildExpressionText());
        resultView.setText(currentInput);
    }

    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return decimalFormat.format(value);
    }

    private String buildExpressionText() {
        if ("Error".equals(expressionText)) {
            return expressionText;
        }
        if (pendingOperator == null) {
            return lastActionWasEquals ? expressionText : currentInput;
        }
        if (shouldStartNewInput) {
            return formatNumber(firstOperand) + " " + pendingOperator;
        }
        return formatNumber(firstOperand) + " " + pendingOperator + " " + currentInput;
    }
}
