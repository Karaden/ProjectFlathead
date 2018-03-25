package com.example.kate.flathead.message.types;


import android.graphics.Color;
import android.graphics.Typeface;

import com.example.kate.flathead.message.display.MessageDisplayFragment;

public class ConversationMessage extends ScreenMessage {

    //String mMessageSuffix =

    public ConversationMessage(String message, Typeface defaultTypeface, String messageSuffix,
                               String messageSubtitle, int logoResourceID) {

        super(message, defaultTypeface, messageSuffix, messageSubtitle, logoResourceID);

    }


    public void display(MessageDisplayFragment mdf) {
        // send info only to DisplayFragment to decide how to display
        mdf.updateDisplay(
                (primaryText + messageSuffix),
                typeface,
                Color.BLACK);
    }

}
