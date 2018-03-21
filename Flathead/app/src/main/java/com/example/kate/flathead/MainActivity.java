package com.example.kate.flathead;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FullscreenActivity {

    private TextView primaryLabel, secondaryLabel;
    private Typeface moodPromptFont, conversationFont;
    private ImageView logo;
    private List<ScreenMessage> screenMessages;
    private String messageSuffix, messageSubtitle;

    private ListView listView;
    private String moodFileName = "mood_messages.txt";
    private String conversationFileName = "conversation_messages.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialise();
    }



    private void initialise() {
        /*
        TODO: the following resources should be specific to class, not object.
         Can they be set in the class file, rather than on object instantiation?
        */

        moodPromptFont = Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf");
        conversationFont = Typeface.createFromAsset(getAssets(), "fonts/datacontrol.ttf");
        messageSuffix = getResources().getString(R.string.messageSuffix);
        messageSubtitle = getResources().getString(R.string.messageSubtitle);


        primaryLabel = findViewById(R.id.primaryLabel);
        secondaryLabel = findViewById(R.id.secondaryLabel);
        logo = findViewById(R.id.logo);
        listView = findViewById(R.id.dynamicListView);

        secondaryLabel.setVisibility(View.GONE);
        logo.setVisibility(View.INVISIBLE);

        checkAndLoadMessages();
        populateScreenMessages();
        populateListView();
    }

    private void checkAndLoadMessages() {

        File outFile = new File(getExternalFilesDir(null), moodFileName);

        //if it doesn't exist
        if (!outFile.isFile() && !outFile.isDirectory()) {


            AssetManager assetManager = getAssets();

            InputStream in = null;
            OutputStream out = null;

            try {

                in = assetManager.open("arrays.xml");
                out = new FileOutputStream(outFile);
                copyFile(in, out);

            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + moodFileName, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }



    // Create a list of all available messages from the two arrays in the resource file
    private void populateScreenMessages() {

        screenMessages = new ArrayList<>(0);

        //TODO: change source from Resources to custom location
        for (String m : getResources().getStringArray(R.array.moodPrompts)) {
            screenMessages.add(new MoodPromptMessage(m, moodPromptFont, primaryLabel,
                    secondaryLabel, logo, messageSuffix, messageSubtitle));
        }

        for (String m : getResources().getStringArray(R.array.conversations)) {
            screenMessages.add(new ConversationMessage(m, conversationFont, primaryLabel,
                    secondaryLabel, logo, messageSuffix, messageSubtitle));
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

