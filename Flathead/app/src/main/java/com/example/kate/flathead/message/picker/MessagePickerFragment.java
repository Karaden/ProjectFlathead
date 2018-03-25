package com.example.kate.flathead.message.picker;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kate.flathead.R;
import com.example.kate.flathead.message.FileManager;
import com.example.kate.flathead.message.types.ConversationMessage;
import com.example.kate.flathead.message.types.MoodPromptMessage;
import com.example.kate.flathead.message.types.ScreenMessage;
import com.example.kate.flathead.message.types.ScreenMessageAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kate on 25/03/18.
 * Message picker fragment-specific stuff
 */

public class MessagePickerFragment extends ListFragment {

    OnMessageSelectedListener mCallback;

    private Typeface moodPromptFont, conversationFont;

    ////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////The following is TEMPORARY FOR TESTING////////////////////////////
    //////////////////////it belongs in the controller/interface/////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

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


        //  mfc.initialisePicker(); //TODO: see https://developer.android.com/training/basics/fragments/communicating.html
        initialise();
    }

    private void initialise() {


        // initialiseDisplay();
        initialisePicker();

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

    public void initialisePicker() {
        listView = getView().findViewById(R.id.dynamicListView); //TODO: null check

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
                screenMessages.add(new MoodPromptMessage(m, moodPromptFont, messageSuffix, messageSubtitle, R.id.logo));
            }
        } catch (IOException e) {
            Log.e("tag", "Failed to read mood message file", e);
        }

        try {
            for (String m : FileManager.readArrayFromFile(getActivity().getExternalFilesDir(null), conversationFile)) {
                screenMessages.add(new ConversationMessage(m, conversationFont, messageSuffix, messageSubtitle, R.id.logo));
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


//        // ListView Item Click Listener
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // ListView Clicked item value
//                ScreenMessage sm = (ScreenMessage) listView.getItemAtPosition(position);
//                // TODO: reenable when DisplayFragment is ready
//                // sm.display();
//            }
//        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        // Notify the parent activity of selected item
        mCallback.onMessageSelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
        //  getListView().setItemChecked(position, true);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnMessageSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnMessageSelectedListener {
        /**
         * Called by MessagePickerFragment when a list item is selected
         */
        void onMessageSelected(int position);
    }

}
