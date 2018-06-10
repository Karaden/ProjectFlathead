package com.karaden.flathead.message;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.karaden.flathead.R;
import com.karaden.flathead.message.types.ConversationMessage;
import com.karaden.flathead.message.types.MoodPromptMessage;
import com.karaden.flathead.message.types.ScreenMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {


    private Typeface moodPromptFont, conversationFont;

    private List<ScreenMessage> screenMessages;
    private String secondaryText;
    private int logo;

    private final String moodFile = "mood_messages.txt";
    private final String conversationFile = "conversation_messages.txt";

    private int defaultTextColour;
    private int moodPromptTextColour;


    private final Activity act;

    public MessageBuilder(Activity activity) {

        act = activity;
        initialise();
    }

    private void initialise() {
        /*
        TODO: the following resources should be specific to class, not object.
         Can they be set in the class file, rather than on object instantiation?
        */


        //Things to display
        moodPromptFont = Typeface.createFromAsset(act.getAssets(), "fonts/furmanite.otf");
        conversationFont = Typeface.createFromAsset(act.getAssets(), "fonts/datacontrol.ttf");
        secondaryText = act.getResources().getString(R.string.secondaryText);
        logo = R.drawable.ic_functionist_council_insignia;
        defaultTextColour = Color.BLACK;
        moodPromptTextColour = Color.WHITE;

        ensureMessagesAreAvailable();
        populateScreenMessages();
    }

    private void ensureMessagesAreAvailable() {
        FileManager.writeMessageFile(act.getExternalFilesDir(null),
                moodFile, act.getAssets(), moodFile);
        FileManager.writeMessageFile(act.getExternalFilesDir(null),
                conversationFile, act.getAssets(), conversationFile);
    }

    // Create a list of all available messages from the two arrays in the resource file
    private void populateScreenMessages() {

        screenMessages = new ArrayList<>(0);

        try {
            for (String m : FileManager.readArrayFromFile(act.getExternalFilesDir(null), moodFile)) {
                screenMessages.add(new MoodPromptMessage(m, moodPromptTextColour, moodPromptFont,
                        secondaryText, defaultTextColour, logo));
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to read mood primaryText file", e);
        }

        try {
            for (String m : FileManager.readArrayFromFile(act.getExternalFilesDir(null), conversationFile)) {
                screenMessages.add(new ConversationMessage(m, defaultTextColour, conversationFont,
                        secondaryText, defaultTextColour, logo));
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to read conversation primaryText file", e);
        }

        Log.i("tag", "Finished loading messages");

    }

    public List<ScreenMessage> getScreenMessages() {
        return screenMessages;
    }
}
