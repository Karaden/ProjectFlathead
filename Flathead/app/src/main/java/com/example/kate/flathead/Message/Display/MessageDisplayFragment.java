package com.example.kate.flathead.Message.Display;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kate.flathead.R;

/**
 * Created by kate on 25/03/18.
 * Fragment to display the list of messages to choose from.
 */

public class MessageDisplayFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_messagedisplay, container, false);
    }

}
