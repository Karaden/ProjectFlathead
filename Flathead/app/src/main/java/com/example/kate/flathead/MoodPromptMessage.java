
package com.example.kate.flathead;


import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class MoodPromptMessage extends ScreenMessage {


    MoodPromptMessage(String message, Typeface defaultTypeface, TextView primaryLabel,
                      TextView secondaryLabel, ImageView logo, String messageSuffix,
                      String messageSubtitle) {

        super(message, defaultTypeface, primaryLabel, secondaryLabel, logo, messageSuffix,
                messageSubtitle);
    }

    void display() {
        primaryLabel.setTypeface(defaultTypeface);
        primaryLabel.setText(message);
        primaryLabel.setTextColor(Color.WHITE);

        secondaryLabel.setText(messageSubtitle);
        secondaryLabel.setTypeface(defaultTypeface);
        secondaryLabel.setTextColor(Color.BLACK);
        secondaryLabel.setVisibility(View.VISIBLE);

        logo.setImageResource(R.drawable.functionistcouncilinsignia);
        logo.setVisibility(View.VISIBLE);

        /*
        TODO: things like the default font, colour, logo etc should be specific to class, not object.
         Can they be set in the class file, rather than on object instantiation?
        */
    }
}