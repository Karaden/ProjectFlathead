package com.example.kate.flathead;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private RadioButton message01, message02, message03;
    private Button btnDisplay;
    private TextView label;
    private Typeface furmanite, datacontrol;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.chosenLabel);
        addListenerOnButton();

        furmanite = Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf");
        datacontrol = Typeface.createFromAsset(getAssets(), "fonts/datacontrol.ttf");

        message01.setTypeface(furmanite);
        message02.setTypeface(datacontrol);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.message01:
                if (checked) {
                    label.setText(R.string.message01);
                    label.setTypeface(furmanite);
                }
                break;
            case R.id.message02:
                if (checked) {
                    label.setText(R.string.message02);
                    label.setTypeface(datacontrol);
                }
                break;
            case R.id.message03:
                if (checked) {
                    label.setText(R.string.message03);
                    label.setTypeface(Typeface.DEFAULT);
                }
                break;
        }
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

                String result = "Message 1 check : " + message01.isChecked() +
                        "\nMessage 2 check : " + message02.isChecked() +
                        "\nMessage 3 check : " + message03.isChecked() +
                        "\n Typeface is: " + label.getTypeface().toString();

                Toast.makeText(MainActivity.this, result,
                        Toast.LENGTH_LONG).show();


            }
        });

    }
}
