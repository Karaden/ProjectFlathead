package com.example.kate.flathead;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FullscreenActivity {

    View.OnClickListener radioButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ScreenMessage message;
            message = (ScreenMessage) view.getTag();

            message.display();
        }
    };


    private TextView primaryLabel, secondaryLabel;
    private Typeface moodPromptFont, conversationFont;
    private ImageView logo;
    private List<ScreenMessage> screenMessages;
    private String messageSuffix, messageSubtitle;

    private RadioGroup radioGroup;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialise();
    }

    private void initialise() {
        moodPromptFont = Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf");
        conversationFont = Typeface.createFromAsset(getAssets(), "fonts/datacontrol.ttf");

        primaryLabel = findViewById(R.id.primaryLabel);
        secondaryLabel = findViewById(R.id.secondaryLabel);

        secondaryLabel.setVisibility(View.GONE);

        messageSuffix = getResources().getString(R.string.messageSuffix);
        messageSubtitle = getResources().getString(R.string.messageSubtitle);

        logo = findViewById(R.id.logo);
        logo.setVisibility(View.INVISIBLE);

        radioGroup = findViewById(R.id.dynamicRadioGroup);
        listView = findViewById(R.id.dynamicListView);


        populateScreenMessages();
        populateRadioButtons();
        populateListView();
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


    private void populateListView() {

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Fourth - the Array of data

        ScreenMessageAdapter<ScreenMessage> adapter = new ScreenMessageAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                screenMessages);



        // Assign adapter to ListView
        listView.setAdapter(adapter);


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                ScreenMessage blah = (ScreenMessage) listView.getItemAtPosition(position);
                blah.display();


            }

        });

    }


}

