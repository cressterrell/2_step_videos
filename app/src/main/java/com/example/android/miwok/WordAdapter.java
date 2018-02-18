/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * {@link WordAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link Word} objects.
 */
public class WordAdapter extends ArrayAdapter<Word>  {

    static class ViewHolderItem {
        TextView defaultTextView;
        TextView miWokTextView;
        CheckBox checkbox;
        View container;
    }

    /** Resource ID for the background color for this list of words */
    private int mColorResourceId;
    private Context mContext;
    private SharedPreferences mPref;

    /**
     * Create a new {@link WordAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param words is the list of {@link Word}s to be displayed.
     * @param colorResourceId is the resource ID for the background color for this list of words
     */
    public WordAdapter(Context context, ArrayList<Word> words, int colorResourceId) {
        super(context, 0, words);
        mColorResourceId = colorResourceId;
        mContext = context;
        SharedPreferences mPref = mContext.getApplicationContext().getSharedPreferences(MovesManager.PREF_FILE_KEY, MODE_PRIVATE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolderItem viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.miWokTextView = ((TextView) convertView.findViewById(R.id.miwok_text_view));
            viewHolder.defaultTextView = ((TextView) convertView.findViewById(R.id.default_text_view));
            viewHolder.checkbox = ((CheckBox) convertView.findViewById(R.id.check_box));
            viewHolder.container = ((View) convertView.findViewById(R.id.container));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }


        // Get the {@link Word} object located at this position in the list
        final Word currentWord = getItem(position);

        viewHolder.miWokTextView.setText(currentWord.getMiwokTranslation());

        viewHolder.defaultTextView.setText(currentWord.getDefaultTranslation());

        if (mPref == null) {
            mPref = mContext.getApplicationContext().getSharedPreferences(MovesManager.PREF_FILE_KEY, MODE_PRIVATE);
        }

        final boolean isCheckedFlag = mPref.getBoolean(String.valueOf(currentWord.getItemId()), false);

        viewHolder.checkbox.setChecked(isCheckedFlag);

        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mPref.edit();
                if (!isCheckedFlag) {
                    editor.putBoolean(String.valueOf(currentWord.getItemId()), true);
                } else {
                    editor.putBoolean(String.valueOf(currentWord.getItemId()), false);
                }
                editor.commit();
            }
        });

        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        // Set the background color of the text container View
        viewHolder.container.setBackgroundColor(color);

        if (currentWord.getItemId()>5) {
            viewHolder.container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.saved));
        }

        return convertView;
    }
}