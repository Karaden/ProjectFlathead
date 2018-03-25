package com.example.kate.flathead.Message;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kate on 22/11/17.
 * Screen message adapter
 */

public class ScreenMessageAdapter<T> extends ArrayAdapter {


    public ScreenMessageAdapter(@NonNull Context context, @LayoutRes int resource,
                                @IdRes int textViewResourceId, @NonNull List<T> objects) {
        super(context, resource, textViewResourceId, objects); //TODO: unchecked call
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView text;

        if (convertView == null) {
            text = new TextView(getContext());
        } else {
            text = (TextView) convertView;
        }

        ScreenMessage sm = (ScreenMessage) getItem(position);

        text.setText(sm.message); //TODO: null check
        text.setTypeface(sm.typeface);
        text.setPadding(20, 30, 20, 30);

        return text;

    }

}
