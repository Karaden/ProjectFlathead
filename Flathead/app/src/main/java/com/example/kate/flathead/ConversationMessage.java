package com.example.kate.flathead;


import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class ConversationMessage extends ScreenMessage {

    ConversationMessage(String message, Typeface defaultTypeface, TextView primaryLabel,
                        TextView secondaryLabel, ImageView logo, String messageSuffix,
                        String messageSubtitle) {

        super(message, defaultTypeface, primaryLabel, secondaryLabel, logo, messageSuffix,
                messageSubtitle);
    }

    void display() {
        primaryLabel.setTypeface(typeface);
        primaryLabel.setText(message + messageSuffix);
        primaryLabel.setTextColor(Color.BLACK);

        secondaryLabel.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
    }
}
