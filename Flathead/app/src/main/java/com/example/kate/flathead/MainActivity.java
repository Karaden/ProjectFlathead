package com.example.kate.flathead;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    private RadioButton message01, message02, message03;
    private Button btnDisplay;
    private TextView label;
    private HashMap<Integer, Function> radio_lookup = new HashMap();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.chosenLabel);
        addListenerOnButton();

        radio_lookup.put(R.id.message01, this::call_method1);
        radio_lookup.put(R.id.message02, this::call_method2);
        radio_lookup.put(R.id.message03, this::call_method3);
    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if(!checked) return;

        int id = view.getId();
        //label.setText(radio_lookup.get(id));
        radio_lookup.get(id).apply(null);
    }

    public Object call_method1(Object o){method1(); return null;}
    public Object call_method2(Object o){method2(7); return null;}
    public Object call_method3(Object o){method3(6,"st"); return null;}

    public void method1(){label.setText("No params");}
    public void method2(Integer i){label.setText(i.toString());}
    public void method3(Integer i, String j){label.setText(i.toString().concat(j));}

    public void addListenerOnButton() {

        message01 = findViewById(R.id.message01);
        message02 = findViewById(R.id.message02);
        message03 = findViewById(R.id.message03);
        btnDisplay = findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                StringBuffer result = new StringBuffer();
                result.append("Message 1 check : ").append(message01.isChecked());
                result.append("\nMessage 2 check : ").append(message02.isChecked());
                result.append("\nMessage 3 check : ").append(message03.isChecked());

                Toast.makeText(MainActivity.this, result.toString(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}
