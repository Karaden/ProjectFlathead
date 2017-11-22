package com.example.kate.flathead;

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

    OnClickListener radioButtonListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            ScreenMessage message;
            message = (ScreenMessage) view.getTag();

            message.display();
        }
    };

    private Button btnDisplay;
    private TextView primaryLabel, secondaryLabel;
    private Typeface moodPromptFont, conversationFont;
    private ImageView logo;
    private List<ScreenMessage> screenMessages;
    private RadioGroup radioGroup;
    private String messageSuffix, messageSubtitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDisplay = findViewById(R.id.btnDisplay);

        moodPromptFont = Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf");
        conversationFont = Typeface.createFromAsset(getAssets(), "fonts/datacontrol.ttf");

        primaryLabel = findViewById(R.id.primaryLabel);
        secondaryLabel = findViewById(R.id.secondaryLabel);

        secondaryLabel.setVisibility(View.GONE);

        messageSuffix = getResources().getString(R.string.messageSuffix);
        messageSubtitle = getResources().getString(R.string.messageSubtitle);

        logo = findViewById(R.id.logo);
        logo.setVisibility(View.INVISIBLE);

        radioGroup = findViewById(R.id.dynamicMessageList);

        addListenerOnButton();

        populateScreenMessages();
        populateRadioButtons();
    }

    public void addListenerOnButton() {

        btnDisplay.setOnClickListener(new OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                String result = "Typeface is: " + primaryLabel.getTypeface().toString();

                Toast.makeText(MainActivity.this, result,
                        Toast.LENGTH_LONG).show();


            }
        });

    }


    private void populateScreenMessages() {

        screenMessages = new ArrayList<>(0);

        for (String m : getResources().getStringArray(R.array.moodPrompts)) {
            screenMessages.add(new MoodPromptMessage(m, moodPromptFont, primaryLabel,
                    secondaryLabel, logo, messageSuffix, messageSubtitle));
        }

        for (String m : getResources().getStringArray(R.array.conversations)) {
            screenMessages.add(new ConversationMessage(m, conversationFont, primaryLabel,
                    secondaryLabel, logo, messageSuffix, messageSubtitle));
        }

    }


    private void populateRadioButtons() {

        radioGroup.setOrientation(LinearLayout.VERTICAL);

        for (ScreenMessage s : screenMessages) {

            RadioButton rb = new RadioButton(this);
            rb.setText(s.message);
            rb.setTag(s); // connects the rb to the message object itself
            rb.setTypeface(s.defaultTypeface);
            rb.setOnClickListener(radioButtonListener);

            radioGroup.addView(rb);

        }
    }


}

