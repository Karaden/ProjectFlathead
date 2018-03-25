package com.example.kate.flathead.message.types;

import android.graphics.Typeface;

import com.example.kate.flathead.message.display.MessageDisplayFragment;

/**
 * Created by kate on 20/11/17.
 * Representation of the kind of messages that can be displayed on the Flathead.
 * <p>
 * Conversational are the standard type; MoodPrompt are the Functionist propoganda types,
 * and Special are anything else unusual
 */

public abstract class ScreenMessage {
    String message;
    Typeface typeface;
    String messageSuffix;
    String messageSubtitle;
    int logoResourceID;


    ScreenMessage(String message, Typeface typeface, String messageSuffix, String messageSubtitle, int logoResourceID) {
        this.message = message;
        this.typeface = typeface;
        this.messageSuffix = messageSuffix;
        this.messageSubtitle = messageSubtitle;
        this.logoResourceID = logoResourceID;

    }

    public abstract void display(MessageDisplayFragment mdf);

    public String toString() {

        return this.message;
    }

}
