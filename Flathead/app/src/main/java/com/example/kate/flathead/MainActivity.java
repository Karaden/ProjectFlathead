package com.example.kate.flathead;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

    private TextView primaryLabel, secondaryLabel;
    private Typeface moodPromptFont, conversationFont;
    private ImageView logo;
    private List<ScreenMessage> screenMessages;
    private String messageSuffix, messageSubtitle;

    private RadioGroup radioGroup;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
        // Defined Array values to show in ListView
        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View",
                "Android Example List View",
                "Android Example List View",
                "Android Example List View",
                "Android Example List View",
                "Android Example List View",
                "last",


        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

    }
}

