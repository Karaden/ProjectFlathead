package com.example.kate.flathead.message.types;

import android.graphics.Typeface;

public class MoodPromptMessage extends ScreenMessage {


    public MoodPromptMessage(String primaryText, int primaryColour, Typeface defaultTypeface,
                             String messageSuffix, String secondaryText, int secondaryColour,
                             int logoResourceID) {

        super(primaryText, primaryColour, defaultTypeface, messageSuffix,
                secondaryText, secondaryColour, logoResourceID);

    }
}