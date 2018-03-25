package com.example.kate.flathead.message;

import android.app.Fragment;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kate.flathead.R;
import com.example.kate.flathead.message.types.ScreenMessage;

import java.util.List;

public class MessageFragmentsController extends Fragment {

    private TextView primaryLabel, secondaryLabel;
    private Typeface moodPromptFont, conversationFont;
    private ImageView logo;
    private List<ScreenMessage> screenMessages;
    private String messageSuffix, messageSubtitle;

    private ListView listView;
    private String moodFile = "mood_messages.txt";
    private String conversationFile = "conversation_messages.txt";




    private void initialise() {
        /*
        TODO: the following resources should be specific to class, not object.
         Can they be set in the class file, rather than on object instantiation?
        */

        //Things to display
        moodPromptFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/furmanite.otf");
        conversationFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/datacontrol.ttf");
        messageSuffix = getResources().getString(R.string.messageSuffix);
        messageSubtitle = getResources().getString(R.string.messageSubtitle);


    }


}