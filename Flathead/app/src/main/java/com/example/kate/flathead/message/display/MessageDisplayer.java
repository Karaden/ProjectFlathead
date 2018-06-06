package com.example.kate.flathead.message.display;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kate.flathead.R;
import com.example.kate.flathead.message.types.ConversationMessage;
import com.example.kate.flathead.message.types.MoodPromptMessage;
import com.example.kate.flathead.message.types.ScreenMessage;

public class MessageDisplayer {

    private TextView mPrimaryLabel;
    private TextView mSecondaryLabel;
    private ImageView mLogo;
    private View mView;

    public MessageDisplayer(View view) {

        mView = view;
        initialiseDisplay();
    }

    private View getView() {
        return mView;
    }

    public void initialiseDisplay() {
        // Locations to send things to
        mPrimaryLabel = getView().findViewById(R.id.primaryLabel);
        mSecondaryLabel = getView().findViewById(R.id.secondaryLabel);
        mLogo = getView().findViewById(R.id.logo);


    }

    public void updateDisplay(String primaryText, Typeface primaryTypeface, int primaryTextColour,
                              String secondaryText, Typeface secondaryTypeface, int secondaryTextColour,
                              int logo) {

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

    public void updateDisplay(ScreenMessage sm) {

        if (sm instanceof MoodPromptMessage) {
            updateDisplay(sm.primaryText, sm.typeface, sm.primaryColour,
                    sm.secondaryText, sm.typeface, sm.secondaryColour,
                    sm.logoResourceID);
        } else if (sm instanceof ConversationMessage) {
            updateDisplay(sm.primaryText, sm.typeface, sm.primaryColour);
        }


    }

}
