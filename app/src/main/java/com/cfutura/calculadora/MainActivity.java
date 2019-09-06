package com.cfutura.calculadora;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.mariuszgromada.math.mxparser.*;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private TextView tvExpression;
    private Boolean edit = false;
    private String dec = String.valueOf(DecimalFormatSymbols.getInstance().getDecimalSeparator());
    private String grp = String.valueOf(DecimalFormatSymbols.getInstance().getGroupingSeparator());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        tvExpression = findViewById(R.id.tvExpression);
        Button decimal = findViewById(R.id.Decimal);
        decimal.setText(dec);
    }

    public void clickNumber(View view) {
        String digit = (String) ((Button) view).getText();
        String display = (String) tvResult.getText();

        if (!display.contains(dec)) display = display.replace(grp,"");

        display = edit ? display+digit : digit;
        edit = true;

        display = display.contains(dec) ? display : NumberFormat.getInstance().format(Float.parseFloat(display));
        tvResult.setText(display);
    }

    public void clickDecimal(View view) {
        String digit = (String) ((Button) view).getText();
        String display = (String) tvResult.getText();

        if (display.contains(dec)) return;
        display = edit ? display + digit : "0" + dec;
        edit = true;

        tvResult.setText(display);
    }

    public void clickBack(View view) {
        String display = (String) tvResult.getText();

        if (!edit) return;

        if (display.length() < 2) {
            display = "0";
        } else if (!display.contains(dec)) {
            display = display.replace(grp,"");
            display = display.substring(0, display.length() - 1);
            display = NumberFormat.getInstance().format(Float.parseFloat(display));
        } else {
            display = display.substring(0, display.length() - 1);
        }

        tvResult.setText(display);
    }

    public void clickOperator(View view) {
        Button button = (Button) view;
        String display = (String) tvResult.getText();
        String expression = (String) tvExpression.getText();

        if (display.substring(display.length() -1).equals(dec)) display = display.substring(0,display.length()-1);

        if (edit) {
            edit = false;
            expression += " " + display.replace(grp,"") + " " + button.getText();
        } else {
            expression = expression.replaceAll(".$", (String) button.getText());
        }
        tvExpression.setText(expression);
        calc();
    }

    public void clickTool(View view) {
        Button button = (Button) view;
        String display = (String) tvResult.getText();
        switch (button.getText().toString()) {
            case "C":
                tvExpression.setText("");
                tvResult.setText("0");
                edit = false;
                break;
            case "%":
                Float percent = Float.parseFloat(display.replace(grp,"").replace(dec,"."))/100;
                tvResult.setText(NumberFormat.getInstance().format(percent));
                break;
            case "=":
                calc();
                edit = true;
                tvExpression.setText("");
                break;
        }
    }

    public void calc() {
        String display = (String) tvResult.getText();
        String expression = (String) tvExpression.getText();
        if (expression.isEmpty()) return;

        expression = edit ? expression + display.replace(grp,"") : expression.substring(0,expression.length() - 1);

        expression = expression.replace("÷","/")
                               .replace("x","*")
                               .replace(dec,".");
        String e = String.valueOf(new Expression(expression).calculate());

        e = NumberFormat.getInstance().format(Float.parseFloat(e));
        tvResult.setText(e);
    }

}
