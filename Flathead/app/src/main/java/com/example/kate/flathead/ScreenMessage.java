package com.example.kate.flathead;

import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kate on 20/11/17.
 * Representation of the kind of messages that can be displayed on the Flathead.
 * <p>
 * Conversational are the standard type; MoodPrompt are the Functionist propoganda types,
 * and Special are anything else unusual
 */

abstract class ScreenMessage {
    String message;
    Typeface defaultTypeface;
    TextView primaryLabel;
    TextView secondaryLabel;
    ImageView logo;
    String messageSuffix;
    String messageSubtitle;


    ScreenMessage(String message, Typeface defaultTypeface, TextView primaryLabel,
                  TextView secondaryLabel, ImageView logo, String messageSuffix,
                  String messageSubtitle) {
        this.message = message;
        this.defaultTypeface = defaultTypeface;
        this.primaryLabel = primaryLabel;
        this.secondaryLabel = secondaryLabel;
        this.logo = logo;
        this.messageSuffix = messageSuffix;
        this.messageSubtitle = messageSubtitle;

    }

    abstract void display();

    public String toString() {

        return this.message;
    }

}
