
package com.example.kate.flathead.message.types;


import android.graphics.Color;
import android.graphics.Typeface;

import com.example.kate.flathead.R;
import com.example.kate.flathead.message.display.MessageDisplayFragment;

public class MoodPromptMessage extends ScreenMessage {

    /*
    String defaultSubtitle = Resources.getSystem().getString(R.string.secondaryText);
    //TODO: confirm whether the following fails on Not8 (7.1.1 OS)
    //8.0 expected early 2018? Beat release already available?
    Typeface typeface = Resources.getSystem().getFont(R.font.furmanite);
    */

    // Typeface mTypeface = Typeface.createFromFile("fonts/furmanite.otf"); //TODO: check this works, apply to Convo types
    // String mMessageSuffix = Resources.getSystem().getString(R.string.messageSuffix);


    public MoodPromptMessage(String message, Typeface defaultTypeface, String messageSuffix,
                             String messageSubtitle, int logoResourceID) {

        super(message, defaultTypeface, messageSuffix, messageSubtitle, logoResourceID);

    }


    public void display(MessageDisplayFragment mdf) {
        // send info only to DisplayFragment to decide how to display
        mdf.updateDisplay(
                primaryText,
                typeface,
                Color.WHITE,
                secondaryText,
                typeface,
                Color.BLACK,
                R.drawable.functionistcouncilinsignia);

    }
}