package com.example.kate.flathead;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RadioButton message01, message02, message03, message04;
    private Button btnDisplay;


    private TextView label, subLabel;
    private Typeface moodPromptFont, conversationFont;
    private ImageView logo;
    private List<ScreenMessage> screenMessages;
    private RadioGroup radioGroup;
    private String messageSuffix;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moodPromptFont = Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf");
        conversationFont = Typeface.createFromAsset(getAssets(), "fonts/datacontrol.ttf");

        message01 = findViewById(R.id.message01);
        message02 = findViewById(R.id.message02);
        message03 = findViewById(R.id.message03);
        message04 = findViewById(R.id.message04);
        btnDisplay = findViewById(R.id.btnDisplay);

        label = findViewById(R.id.chosenLabel);

        subLabel = findViewById(R.id.subtitleLabel);
        subLabel.setText(R.string.moodSubtitle);
        subLabel.setTypeface(moodPromptFont);
        subLabel.setTextColor(Color.WHITE);
        subLabel.setVisibility(View.GONE);

        logo = findViewById(R.id.logo);
        logo.setImageResource(R.drawable.functionistcouncilinsignia);
        logo.setVisibility(View.INVISIBLE);

        message01.setTypeface(moodPromptFont);
        message02.setTypeface(conversationFont);

        radioGroup = findViewById(R.id.dynamicMessageList);
        messageSuffix = getResources().getString(R.string.conversationSuffix);

        addListenerOnButton();


        populateScreenMessages();
        populateRadioButtons();
    }

    private void displayMoodPrompt(int message) {
        label.setTypeface(moodPromptFont);

        label.setText(message);
        subLabel.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
    }

    private void displayConversation(int message) {
        label.setTypeface(conversationFont);

        label.setText(getResources().getString(message) + getResources().getString(R.string.conversationSuffix));
        subLabel.setVisibility(View.GONE);

        logo.setVisibility(View.GONE);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.message01:
                if (checked) {
                    displayMoodPrompt(R.string.mood01);
                }
                break;
            case R.id.message02:
                if (checked) {
                    displayConversation(R.string.conversation03);
                }
                break;
            case R.id.message03:
                if (checked) {
                    displayMoodPrompt(R.string.mood03);

                }
                break;
            case R.id.message04:
                if (checked) {
                    displayConversation(R.string.conversation02);
                }
                break;
        }
    }


    public void addListenerOnButton() {



        btnDisplay.setOnClickListener(new OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                String result = "Message 1 check : " + message01.isChecked() +
                        "\nMessage 2 check : " + message02.isChecked() +
                        "\nMessage 3 check : " + message03.isChecked() +
                        "\nMessage 4 check : " + message04.isChecked() +
                        "\n Typeface is: " + label.getTypeface().toString();

                Toast.makeText(MainActivity.this, result,
                        Toast.LENGTH_LONG).show();


            }
        });

    }

    private void populateScreenMessages() {

        screenMessages = new ArrayList<>(0);

        for (String m : getResources().getStringArray(R.array.moodPrompts)) {
            screenMessages.add(new MoodPromptMessage(m, moodPromptFont, label, subLabel, logo, messageSuffix));
        }

        for (String m : getResources().getStringArray(R.array.conversations)) {
            screenMessages.add(new ConversationMessage(m, conversationFont, label, subLabel, logo, messageSuffix));
        }

    }


    private void populateRadioButtons() {

        radioGroup.setOrientation(LinearLayout.VERTICAL);

        for (ScreenMessage s : screenMessages) {

            RadioButton rb = new RadioButton(this);
            rb.setText(s.message);
            rb.setTypeface(s.defaultTypeface);

            radioGroup.addView(rb);

        }
    }

}
