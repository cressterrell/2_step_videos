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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class ProgressLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress);
        updateProgress();
    }

    private void updateProgress() {

        int completed=0;
        String x, y;
        x= Integer.toString(MovesManager.getInstance().getCompletedBeginnerMoves(this));
        y= Integer.toString(MovesManager.getInstance().getTotalBeginnerMoves());
        ((TextView)findViewById(R.id.beginner_x_of_y)).setText(x+" of "+y);
        completed+=MovesManager.getInstance().getCompletedBeginnerMoves(this);
        ((TextView)findViewById(R.id.beginner_complete)).setText(MovesManager.getInstance().getBeginnerProgress(this));

        x= Integer.toString(MovesManager.getInstance().getCompletedInt1Moves(this));
        y= Integer.toString(MovesManager.getInstance().getTotalInt1Moves());
        ((TextView)findViewById(R.id.int1_x_of_y)).setText(x+" of "+y);
        completed+=MovesManager.getInstance().getCompletedInt1Moves(this);
        ((TextView)findViewById(R.id.int1_complete)).setText(MovesManager.getInstance().getInt1Progress(this));

        x= Integer.toString(MovesManager.getInstance().getCompletedInt2Moves(this));
        y= Integer.toString(MovesManager.getInstance().getTotalInt2Moves());
        ((TextView)findViewById(R.id.int2_x_of_y)).setText(x+" of "+y);
        completed+=MovesManager.getInstance().getCompletedInt2Moves(this);
        ((TextView)findViewById(R.id.int2_complete)).setText(MovesManager.getInstance().getInt2Progress(this));

        x= Integer.toString(MovesManager.getInstance().getCompletedAdvMoves(this));
        y= Integer.toString(MovesManager.getInstance().getTotalAdvMoves());
        ((TextView)findViewById(R.id.adv_x_of_y)).setText(x+" of "+y);
        completed+=MovesManager.getInstance().getCompletedAdvMoves(this);
        ((TextView)findViewById(R.id.adv_complete)).setText(MovesManager.getInstance().getAdvancedProgress(this));

        x = Integer.toString(completed);
        y = Integer.toString(MovesManager.getInstance().getTotalMoves());
        ((TextView)findViewById(R.id.tot_x_of_y)).setText(x+" of "+y);

        try {
            float a = (float) completed / MovesManager.getInstance().getTotalMoves();
            a = a * 100;
            ((TextView)findViewById(R.id.tot_complete)).setText(Integer.toString((Math.round(a))) + "%");
        } catch (ArithmeticException e) {
            ((TextView)findViewById(R.id.tot_complete)).setText("100%");
        }
    }

}
