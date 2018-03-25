
package com.example.kate.flathead.message.types;


import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kate.flathead.R;

public class MoodPromptMessage extends ScreenMessage {

    /*
    String defaultSubtitle = Resources.getSystem().getString(R.string.messageSubtitle);
    //TODO: confirm whether the following fails on Not8 (7.1.1 OS)
    //8.0 expected early 2018? Beat release already available?
    Typeface typeface = Resources.getSystem().getFont(R.font.furmanite);
    */

    // Typeface mTypeface = Typeface.createFromFile("fonts/furmanite.otf"); //TODO: check this works, apply to Convo types
    // String mMessageSuffix = Resources.getSystem().getString(R.string.messageSuffix);


    public MoodPromptMessage(String message, Typeface defaultTypeface, TextView primaryLabel,
                             TextView secondaryLabel, ImageView logo, String messageSuffix,
                             String messageSubtitle) {
        super(message, defaultTypeface, primaryLabel, secondaryLabel, logo, messageSuffix,
                messageSubtitle);

    }


    public void display() {
        primaryLabel.setTypeface(typeface);
        primaryLabel.setText(message);
        primaryLabel.setTextColor(Color.WHITE);

        secondaryLabel.setText(messageSubtitle);
        secondaryLabel.setTypeface(typeface);
        secondaryLabel.setTextColor(Color.BLACK);
        secondaryLabel.setVisibility(View.VISIBLE);

        logo.setImageResource(R.drawable.functionistcouncilinsignia);
        logo.setVisibility(View.VISIBLE);


    }
}