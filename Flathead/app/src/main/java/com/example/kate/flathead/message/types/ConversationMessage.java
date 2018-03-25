package com.example.kate.flathead.message.types;


import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationMessage extends ScreenMessage {

    public ConversationMessage(String message, Typeface defaultTypeface, TextView primaryLabel,
                               TextView secondaryLabel, ImageView logo, String messageSuffix,
                               String messageSubtitle) {

        super(message, defaultTypeface, primaryLabel, secondaryLabel, logo, messageSuffix,
                messageSubtitle);
    }

    public void display() {
        primaryLabel.setTypeface(typeface);
        primaryLabel.setText(message + messageSuffix);
        primaryLabel.setTextColor(Color.BLACK);

        secondaryLabel.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
    }
}
