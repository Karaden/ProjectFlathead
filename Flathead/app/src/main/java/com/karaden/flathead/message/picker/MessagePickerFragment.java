package com.karaden.flathead.message.picker;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.karaden.flathead.R;
import com.karaden.flathead.message.types.MoodPromptMessage;
import com.karaden.flathead.message.types.ScreenMessage;
import com.karaden.flathead.message.types.ScreenMessageAdapter;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;

/**
 * Created by kate on 25/03/18.
 * Message picker fragment-specific stuff
 */

public class MessagePickerFragment extends ListFragment {

    private OnMessageSelectedListener mCallback;

    private ListView listView;

    private Random rand;

    // 3 mins = 180,000
    // 10s = 10,000
    CountDownTimer timer = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            // Do nothing
        }

        @Override
        public void onFinish() {

            //Pick a random message (of type MoodPrompt) from the list
            int n;

            do {
                rand = new Random();
                n = rand.nextInt(listView.getCount()); // Gives n such that 0 <= n < size of the listview
            } while ( !(listView.getItemAtPosition(n) instanceof MoodPromptMessage));

            // Display it
            onListItemClick(n);
        }
    };


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

        listView = Objects.requireNonNull(getView()).findViewById(android.R.id.list);
    }

    private void populateListView(List<ScreenMessage> screenMessages) {

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Fourth - the Array of data

        ScreenMessageAdapter<ScreenMessage> adapter = new ScreenMessageAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1,
                screenMessages);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        onListItemClick(position);
    }

    public void onListItemClick(int position) {
        // Restart the timer to display the next message
        timer.start();

        // Notify the parent activity of selected item
        ScreenMessage sm = (ScreenMessage) listView.getItemAtPosition(position);

        mCallback.onMessageSelected(sm);
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

    public void setMessages(List<ScreenMessage> screenMessages) {
        populateListView(screenMessages);
    }

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnMessageSelectedListener {
        /**
         * Called by MessagePickerFragment when a list item is selected
         */
        void onMessageSelected(ScreenMessage sm);
    }

}
