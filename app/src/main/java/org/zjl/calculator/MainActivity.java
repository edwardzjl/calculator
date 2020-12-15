package org.zjl.calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * A stack of Reverse Polish Notation
     */
    private final Deque<String> outputs = new ArrayDeque<>();

    /**
     * A stack of operators whose later added elements has higher priority
     */
    private final Deque<String> operators = new ArrayDeque<>();

    /**
     * Current number
     */
    private String currentNumber = "";

    /**
     * Display text view
     */
    private TextView display;

    /**
     * When Calculate Btn was clicked, I calculate and display the result.
     * On the next click, just clear the result and display what was clicked.
     */
    private boolean clearOnNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        display.setText("");

        Button button0 = findViewById(R.id.button0);
        button0.setOnClickListener(v -> clickNumber("0"));
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> clickNumber("1"));
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> clickNumber("2"));
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> clickNumber("3"));
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> clickNumber("4"));
        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> clickNumber("5"));
        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(v -> clickNumber("6"));
        Button button7 = findViewById(R.id.button7);
        button7.setOnClickListener(v -> clickNumber("7"));
        Button button8 = findViewById(R.id.button8);
        button8.setOnClickListener(v -> clickNumber("8"));
        Button button9 = findViewById(R.id.button9);
        button9.setOnClickListener(v -> clickNumber("9"));
        Button buttonDot = findViewById(R.id.buttonDot);
        buttonDot.setOnClickListener(v -> clickNumber("."));

        Button btn_add = findViewById(R.id.buttonAdd);
        btn_add.setOnClickListener(v -> clickOperator("+"));
        Button btn_sub = findViewById(R.id.buttonSub);
        btn_sub.setOnClickListener(v -> clickOperator("-"));
        Button btn_mul = findViewById(R.id.buttonMul);
        btn_mul.setOnClickListener(v -> clickOperator("*"));
        Button btn_div = findViewById(R.id.buttonDiv);
        btn_div.setOnClickListener(v -> clickOperator("/"));

        Button btn_left_brace = findViewById(R.id.buttonLeftBrace);
        btn_left_brace.setOnClickListener(v -> clickLeftBrace());

        Button btn_right_brace = findViewById(R.id.buttonRightBrace);
        btn_right_brace.setOnClickListener(v -> clickRightBrace());

        Button btn_calc = findViewById(R.id.buttonCalc);
        btn_calc.setOnClickListener(v -> {
            display.setText(String.valueOf(calc()));
            outputs.clear();
            operators.clear();
        });

        Button btn_clear = findViewById(R.id.buttonClear);
        btn_clear.setOnClickListener(v -> clear());

    }


    private void clickNumber(String number) {
        if (clearOnNext) {
            clear();
        }
        display.append(number);
        currentNumber += number;
    }

    private void clickOperator(String operator) {
        if (clearOnNext) {
            clear();
        }
        display.append(operator);
        pushCurrentNumber();
        pushOperator(operator);
    }

    private void clickLeftBrace() {
        if (clearOnNext) {
            clear();
        }
        display.append("(");
        operators.push("(");
    }

    private void clickRightBrace() {
        if (clearOnNext) {
            clear();
        }
        display.append(")");
        while (!operators.peek().equals("(")) {
            outputs.add(operators.pop());
        }
        operators.pop();
    }

    /**
     * When an operator {@code (+, -, *, //, /(, /))} was clicked, check whether
     * {@link #currentNumber} is valid and push it into {@link #outputs}
     */
    private void pushCurrentNumber() {
        if (!TextUtils.isEmpty(currentNumber)) {
            outputs.add(currentNumber);
            currentNumber = "";
        }
    }

    /**
     * A unit processor for Shunting Yard Algorithm
     */
    private void pushOperator(String input) {
        // if there's higher priority elements in the operator stack
        // pop them and push into outputs
        while ((input.equals("-") || input.equals("+"))
                && !operators.isEmpty()
                && (!operators.peek().equals("(") && !operators.peek().equals(")"))) {
            outputs.add(operators.pop());
        }
        // push the operator into operator stack
        operators.push(input);
    }

    private double calc() {
        pushCurrentNumber();
        clearOnNext = true;
        while (!operators.isEmpty()
                && (OperatorUtil.isOperator(operators.peek()))) {
            outputs.add(operators.pop());
        }
        return calculate(outputs);
    }

    private void clear() {
        display.setText("");
        outputs.clear();
        operators.clear();
        currentNumber = "";
        clearOnNext = false;
    }

    /**
     * Shunting yard algorithm to parse an input list into a stack of Reverse Polish Notation
     *
     * @param inputs Input list
     * @return A stack of Reverse Polish Notation
     */
    public static Deque<String> shuntingYard(List<String> inputs) {
        // A stack of Reverse Polish Notation
        Deque<String> outputs = new ArrayDeque<>();
        // A stack of operators whose later added elements has higher priority
        Deque<String> operators = new ArrayDeque<>();
        for (String input : inputs) {
            if (OperatorUtil.isOperator(input)) {
                // if there's higher priority elements in the operator stack
                // pop them and push into outputs
                while ((input.equals("-") || input.equals("+"))
                        && !operators.isEmpty()
                        && (!operators.peek().equals("(") && !operators.peek().equals(")"))) {
                    outputs.add(operators.pop());
                }
                // push the operator into operator stack
                operators.push(input);
            } else if (input.equals("(")) {
                operators.push(input);
            } else if (input.equals(")")) {
                while (!operators.peek().equals("(")) {
                    outputs.add(operators.pop());
                }
                operators.pop();
            } else {
                outputs.add(input);
            }
        }
        // push all remaining operators into outputs
        while (!operators.isEmpty()
                && (OperatorUtil.isOperator(operators.peek()))) {
            outputs.add(operators.pop());
        }
        return outputs;
    }

    /**
     * Calculate a Reverse Polish Notation
     */
    public static double calculate(Deque<String> q) {
        String current = q.pollLast();
        if (OperatorUtil.isOperator(current)) {
            double rightOperation = calculate(q);
            double leftOperation = calculate(q);
            switch (current) {
                case "+":
                    return leftOperation + rightOperation;
                case "-":
                    return leftOperation - rightOperation;
                case "*":
                    return leftOperation * rightOperation;
                case "/":
                    return leftOperation / rightOperation;
                default:
                    return 0.0;
            }
        } else {
            return Double.parseDouble(current);
        }
    }

}