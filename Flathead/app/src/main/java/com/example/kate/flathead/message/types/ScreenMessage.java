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
    public String primaryText;
    public Typeface typeface;
    public String messageSuffix;
    public String secondaryText;
    public int logoResourceID;


    ScreenMessage(String primaryText, Typeface typeface, String messageSuffix, String secondaryText, int logoResourceID) {
        this.primaryText = primaryText;
        this.typeface = typeface;
        this.messageSuffix = messageSuffix;
        this.secondaryText = secondaryText;
        this.logoResourceID = logoResourceID;

    }

    public abstract void display(MessageDisplayFragment mdf);

    public String toString() {

        return this.primaryText;
    }

}
