package com.example.kate.flathead.message.display;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kate.flathead.R;


/**
 * Created by kate on 25/03/18.
 * Message display-specific fragment stuff
 */

public class MessageDisplayFragment extends Fragment {
    private TextView mPrimaryLabel, mSecondaryLabel;
    private ImageView mLogo;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_messagedisplay, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialiseDisplay();
    }


    public void initialiseDisplay() {
        // Locations to send things to
        mPrimaryLabel = getView().findViewById(R.id.primaryLabel);
        mSecondaryLabel = getView().findViewById(R.id.secondaryLabel);
        mLogo = getView().findViewById(R.id.logo);


    }

    public void updateDisplay(String primaryText, Typeface primaryTypeface, int primaryTextColour,
                              String secondaryText, Typeface secondaryTypeface, int secondaryTextColour, int logo) {

        mPrimaryLabel.setVisibility(View.VISIBLE);
        mPrimaryLabel.setText(primaryText);
        mPrimaryLabel.setTypeface(primaryTypeface);
        mPrimaryLabel.setTextColor(primaryTextColour);

        mSecondaryLabel.setVisibility(View.VISIBLE);
        mSecondaryLabel.setText(secondaryText);
        mSecondaryLabel.setTypeface(secondaryTypeface);
        mSecondaryLabel.setTextColor(secondaryTextColour);

        mLogo.setVisibility(View.VISIBLE);
        mLogo.setImageResource(logo);

    }

    public void updateDisplay(String primaryText, Typeface primaryTypeface,
                              int primaryTextColour) {

        mPrimaryLabel.setVisibility(View.VISIBLE);
        mPrimaryLabel.setText(primaryText);
        mPrimaryLabel.setTypeface(primaryTypeface);
        mPrimaryLabel.setTextColor(primaryTextColour);

        mSecondaryLabel.setVisibility(View.GONE);

        mLogo.setVisibility(View.GONE);

    }

    public void updateColourOnly(int colour) {
        mPrimaryLabel.setTextColor(colour);
    }

}
