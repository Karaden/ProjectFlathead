package com.example.kate.flathead.message;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ListView;

import com.example.kate.flathead.R;
import com.example.kate.flathead.message.types.ConversationMessage;
import com.example.kate.flathead.message.types.MoodPromptMessage;
import com.example.kate.flathead.message.types.ScreenMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {


    private Typeface moodPromptFont, conversationFont;

    private List<ScreenMessage> screenMessages;
    private String messageSuffix, messageSubtitle;

    private ListView listView;
    private String moodFile = "mood_messages.txt";
    private String conversationFile = "conversation_messages.txt";

    private Activity act;

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
        messageSuffix = act.getResources().getString(R.string.messageSuffix);
        messageSubtitle = act.getResources().getString(R.string.secondaryText);

        ensureMessagesAreAvailable();
        populateScreenMessages();
    }

    private void ensureMessagesAreAvailable() {
        FileManager.writeMessageFile(act.getExternalFilesDir(null), moodFile, act.getAssets(), moodFile);
        FileManager.writeMessageFile(act.getExternalFilesDir(null), conversationFile, act.getAssets(), conversationFile);
    }

    // Create a list of all available messages from the two arrays in the resource file
    private void populateScreenMessages() {

        screenMessages = new ArrayList<>(0);

        try {
            for (String m : FileManager.readArrayFromFile(act.getExternalFilesDir(null), moodFile)) {
                screenMessages.add(new MoodPromptMessage(m, moodPromptFont, messageSuffix, messageSubtitle, R.id.logo));
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to read mood primaryText file", e);
        }

        try {
            for (String m : FileManager.readArrayFromFile(act.getExternalFilesDir(null), conversationFile)) {
                screenMessages.add(new ConversationMessage(m, conversationFont, messageSuffix, messageSubtitle, R.id.logo));
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
