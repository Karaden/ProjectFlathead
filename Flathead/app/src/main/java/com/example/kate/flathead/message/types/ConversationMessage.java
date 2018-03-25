package com.example.kate.flathead.message.types;

import android.graphics.Typeface;

public class ConversationMessage extends ScreenMessage {


    public ConversationMessage(String primaryText, int primaryColour, Typeface defaultTypeface, String messageSuffix,
                               String secondaryText, int secondaryColour, int logoImage) {

        super(primaryText, primaryColour, defaultTypeface, messageSuffix,
                secondaryText, secondaryColour, logoImage);

    }
}
