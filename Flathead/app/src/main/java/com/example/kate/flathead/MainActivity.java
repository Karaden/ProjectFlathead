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

public class MainActivity extends AppCompatActivity {

    private RadioButton message01, message02, message03;
    private Button btnDisplay;
    private TextView label;
    private HashMap<Integer, Integer> radio_lookup = new HashMap();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.chosenLabel);
        addListenerOnButton();

        radio_lookup.put(R.id.message01, R.string.message01);
        radio_lookup.put(R.id.message02, R.string.message02);
        radio_lookup.put(R.id.message03, R.string.message03);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if(!checked) return;

        int id = view.getId();
        label.setText(radio_lookup.get(id));
    }


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
