
package com.example.kate.flathead;


import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class MoodPromptMessage extends ScreenMessage {

    MoodPromptMessage(String message, Typeface defaultTypeface, TextView primaryLabel,
                      TextView secondaryLabel, ImageView logo, String messageSuffix) {

        super(message, defaultTypeface, primaryLabel, secondaryLabel, logo, messageSuffix);
    }

    void display() {
        primaryLabel.setTypeface(defaultTypeface);

        primaryLabel.setText(message);
        secondaryLabel.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
    }
}