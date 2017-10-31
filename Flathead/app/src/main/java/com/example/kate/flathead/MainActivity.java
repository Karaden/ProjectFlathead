package com.example.kate.flathead;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CheckBox chkmessage01, chkmessage02, chkmessage03;
    private Button btnDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnChkmessage01();
        addListenerOnButton();
    }

    public void addListenerOnChkmessage01() {

        chkmessage01 = findViewById(R.id.chkmessage01);

        chkmessage01.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkmessage01 checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(MainActivity.this,
                            "Tomorrow will be even better", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void addListenerOnButton() {

        chkmessage01 = findViewById(R.id.chkmessage01);
        chkmessage02 = findViewById(R.id.chkmessage02);
        chkmessage03 = findViewById(R.id.chkmessage03);
        btnDisplay = findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                StringBuffer result = new StringBuffer();
                result.append("Message 1 check : ").append(chkmessage01.isChecked());
                result.append("\nMessage 2 check : ").append(chkmessage02.isChecked());
                result.append("\nMessage 3 check : ").append(chkmessage03.isChecked());

                Toast.makeText(MainActivity.this, result.toString(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}
