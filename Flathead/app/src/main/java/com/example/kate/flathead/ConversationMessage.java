package com.example.kate.flathead;


import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class ConversationMessage extends ScreenMessage {

    ConversationMessage(String message, Typeface defaultTypeface, TextView primaryLabel,
                        TextView secondaryLabel, ImageView logo, String messageSuffix) {

        super(message, defaultTypeface, primaryLabel, secondaryLabel, logo, messageSuffix);
    }

    void display() {
        primaryLabel.setTypeface(defaultTypeface);

        primaryLabel.setText(message + messageSuffix);
        secondaryLabel.setVisibility(View.GONE);

        logo.setVisibility(View.GONE);
    }
}
