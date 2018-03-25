package com.example.kate.flathead.message.types;

import android.graphics.Typeface;

/**
 * Created by kate on 20/11/17.
 * Representation of the kind of messages that can be displayed on the Flathead.
 * <p>
 * Conversational are the standard type; MoodPrompt are the Functionist propaganda types,
 * and Special are anything else unusual
 */

public abstract class ScreenMessage {
    public String primaryText;
    public Typeface typeface;
    public String messageSuffix;
    public String secondaryText;
    public int logoResourceID;


    ScreenMessage(String primaryText, Typeface typeface, String messageSuffix, String secondaryText, int logoImage) {
        this.primaryText = primaryText;
        this.typeface = typeface;
        this.messageSuffix = messageSuffix;
        this.secondaryText = secondaryText;
        this.logoResourceID = logoImage;
    }

    public String toString() {
        return this.primaryText;
    }

}
