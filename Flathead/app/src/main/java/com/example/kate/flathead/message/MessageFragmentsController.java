package com.example.kate.flathead.message;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kate.flathead.R;
import com.example.kate.flathead.message.types.ConversationMessage;
import com.example.kate.flathead.message.types.MoodPromptMessage;
import com.example.kate.flathead.message.types.ScreenMessage;
import com.example.kate.flathead.message.types.ScreenMessageAdapter;

import java.io.IOException;
import java.util.ArrayList;
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

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_messagepicker, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialise();
    }

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

        //Locations to send things to
//        primaryLabel = getView().findViewById(R.id.primaryLabel);
//        secondaryLabel = getView().findViewById(R.id.secondaryLabel);
//        logo = getView().findViewById(R.id.logo);


        listView = getView().findViewById(R.id.dynamicListView); //TODO: null check


//        secondaryLabel.setVisibility(View.GONE);
//        logo.setVisibility(View.INVISIBLE);

        ensureMessagesAreAvailable();
        populateScreenMessages();
        populateListView();
    }

    private void ensureMessagesAreAvailable() {
        FileManager.writeMessageFile(getActivity().getExternalFilesDir(null), moodFile, getActivity().getAssets(), moodFile);
        FileManager.writeMessageFile(getActivity().getExternalFilesDir(null), conversationFile, getActivity().getAssets(), conversationFile);
    }


    // Create a list of all available messages from the two arrays in the resource file
    private void populateScreenMessages() {

        screenMessages = new ArrayList<>(0);

        try {
            for (String m : FileManager.readArrayFromFile(getActivity().getExternalFilesDir(null), moodFile)) {
                screenMessages.add(new MoodPromptMessage(m, moodPromptFont, primaryLabel,
                        secondaryLabel, logo, messageSuffix, messageSubtitle));
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to read mood message file", e);
        }

        try {
            for (String m : FileManager.readArrayFromFile(getActivity().getExternalFilesDir(null), conversationFile)) {
                screenMessages.add(new ConversationMessage(m, conversationFont, primaryLabel,
                        secondaryLabel, logo, messageSuffix, messageSubtitle));
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to read conversation message file", e);
        }

    }

    private void populateListView() {

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Fourth - the Array of data

        ScreenMessageAdapter<ScreenMessage> adapter = new ScreenMessageAdapter<>(
                getContext(),
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
                ScreenMessage sm = (ScreenMessage) listView.getItemAtPosition(position);
                // TODO: reenable when DisplayFragment is ready
                // sm.display();
            }
        });
    }


}