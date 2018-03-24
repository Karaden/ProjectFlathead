package com.example.kate.flathead;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kate.flathead.immersivemode.BasicImmersiveModeFragment;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView primaryLabel, secondaryLabel;
    private Typeface moodPromptFont, conversationFont;
    private ImageView logo;
    private List<ScreenMessage> screenMessages;
    private String messageSuffix, messageSubtitle;

    private ListView listView;
    private String moodFile = "mood_messages.txt";
    private String conversationFile = "conversation_messages.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentByTag("BasicImmersiveModeFragment") == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BasicImmersiveModeFragment fragment = new BasicImmersiveModeFragment();
            transaction.add(fragment, "BasicImmersiveModeFragment");
            transaction.commit();
        }

        setContentView(R.layout.activity_main);

        //initialise();
    }
//
//    private void initialise() {
//        /*
//        TODO: the following resources should be specific to class, not object.
//         Can they be set in the class file, rather than on object instantiation?
//        */
//
//        moodPromptFont = Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf");
//        conversationFont = Typeface.createFromAsset(getAssets(), "fonts/datacontrol.ttf");
//        messageSuffix = getResources().getString(R.string.messageSuffix);
//        messageSubtitle = getResources().getString(R.string.messageSubtitle);
//
//
//        primaryLabel = findViewById(R.id.primaryLabel);
//        secondaryLabel = findViewById(R.id.secondaryLabel);
//        logo = findViewById(R.id.logo);
//        listView = findViewById(R.id.dynamicListView);
//
//        secondaryLabel.setVisibility(View.GONE);
//        logo.setVisibility(View.INVISIBLE);
//
//        ensureMessagesAreAvailable();
//        populateScreenMessages();
//        populateListView();
//    }
//
//    private void ensureMessagesAreAvailable() {
//        FileManager.writeMessageFile(getExternalFilesDir(null), moodFile, getAssets(), moodFile);
//        FileManager.writeMessageFile(getExternalFilesDir(null), conversationFile, getAssets(), conversationFile);
//    }
//
//
//    // Create a list of all available messages from the two arrays in the resource file
//    private void populateScreenMessages() {
//
//        screenMessages = new ArrayList<>(0);
//
//        try {
//            for (String m : FileManager.readArrayFromFile(getExternalFilesDir(null), moodFile)) {
//                screenMessages.add(new MoodPromptMessage(m, moodPromptFont, primaryLabel,
//                        secondaryLabel, logo, messageSuffix, messageSubtitle));
//            }
//        } catch (IOException e) {
//            Log.e("tag", "Failed to read mood message file", e);
//        }
//
//        try {
//            for (String m : FileManager.readArrayFromFile(getExternalFilesDir(null), conversationFile)) {
//                screenMessages.add(new ConversationMessage(m, conversationFont, primaryLabel,
//                        secondaryLabel, logo, messageSuffix, messageSubtitle));
//            }
//        } catch (IOException e) {
//            Log.e("tag", "Failed to read conversation message file", e);
//        }
//
//    }
//
//    private void populateListView() {
//
//        // Define a new Adapter
//        // First parameter - Context
//        // Second parameter - Layout for the row
//        // Third parameter - ID of the TextView to which the data is written
//        // Fourth - the Array of data
//
//        ScreenMessageAdapter<ScreenMessage> adapter = new ScreenMessageAdapter<>(
//                this,
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1,
//                screenMessages);
//
//        // Assign adapter to ListView
//        listView.setAdapter(adapter);
//
//
//        // ListView Item Click Listener
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // ListView Clicked item value
//                ScreenMessage blah = (ScreenMessage) listView.getItemAtPosition(position);
//                blah.display();
//            }
//        });
//    }

}

