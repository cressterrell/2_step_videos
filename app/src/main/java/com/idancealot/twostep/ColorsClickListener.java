package com.idancealot.twostep;

import android.view.View;
import android.widget.Toast;

/**
 * Created by Cress on 8/3/2017.
 */

public class ColorsClickListener implements View.OnClickListener{
    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
}}
