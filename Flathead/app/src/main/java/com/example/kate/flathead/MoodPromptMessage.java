
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

        secondaryLabel.setText(messageSubtitle);
        secondaryLabel.setTypeface(defaultTypeface);
        secondaryLabel.setTextColor(Color.WHITE);
        secondaryLabel.setVisibility(View.VISIBLE);

        logo.setImageResource(R.drawable.functionistcouncilinsignia);
        logo.setVisibility(View.VISIBLE);
    }
}