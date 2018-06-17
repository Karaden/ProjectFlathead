package com.karaden.flathead.message.types;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.karaden.flathead.R;

import java.util.List;
import java.util.Objects;

/**
 * Created by kate on 22/11/17.
 * Screen primaryText adapter
 */

public class ScreenMessageAdapter<T> extends ArrayAdapter {

    private Context mContext;

    public ScreenMessageAdapter(@NonNull Context context, @LayoutRes int resource,
                                @IdRes int textViewResourceId, @NonNull List<T> objects) {
        super(context, resource, textViewResourceId, objects); //TODO: unchecked call
        this.mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup container) {

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);

            convertView = inflater.inflate(R.layout.list_item, container, false);
        }


        ScreenMessage sm = (ScreenMessage) getItem(position);

        ((TextView) convertView.findViewById(android.R.id.text1)).setText(Objects.requireNonNull(sm).primaryText);
        ((TextView) convertView.findViewById(android.R.id.text1)).setTypeface(sm.typeface);
        ((TextView) convertView.findViewById(android.R.id.text1)).setPadding(20, 30, 20, 30);

        return convertView;
    }

}
