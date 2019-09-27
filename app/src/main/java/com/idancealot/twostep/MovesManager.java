package com.idancealot.twostep;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by authorwjf on 2/17/18.
 */

public class MovesManager {

    private MovesManager(){}

        private static class SingletonHelper {
            private static final MovesManager INSTANCE = new MovesManager();
        }

        public static final String PREF_FILE_KEY = "DanceDancePref";

        public ArrayList<Word> getMoves() {
            // Create a list of words
            final ArrayList<Word> words = new ArrayList<Word>();
            words.add(new Word(1,"Basic", "Beginner", R.raw.number_one, R.raw.video_number_one));
            words.add(new Word(2,"Promenade", "Beginner", R.raw.number_two, R.raw.video_number_one));
            words.add(new Word(3,"Right Turning (Natural)", "Beginner",R.raw.number_three, R.raw.video_number_one));
            words.add(new Word(4,"Right Turning (Cross-Body)", "Beginner", R.raw.number_four, R.raw.video_number_one));
            words.add(new Word(5,"Promenade Pivot", "Beginner", R.raw.number_five, R.raw.video_number_one));
            words.add(new Word(6,"Underarm Turn (Left)", "Beginner", R.raw.number_five, R.raw.video_number_one));
            words.add(new Word(7,"Underarm Turn (Right)", "Beginner", R.raw.number_six, R.raw.video_number_one));
            words.add(new Word(8,"Wrap (Walkout)", "Beginner",R.raw.number_seven, R.raw.video_number_one));
            words.add(new Word(9,"Wrap (Check Turn)", "Beginner", R.raw.number_eight, R.raw.video_number_one));
            words.add(new Word(10,"Sweetheart (Check Turn Lt)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(11,"Sweetheart (Check Turn Rt)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(12,"Grapevine (Closed)", "Beginner", R.raw.number_nine, R.raw.video_number_one));
            words.add(new Word(13,"Grapevine (Bkd Hands)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(14,"Grapevine (Fwd Hands)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(15,"Basket Whip", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(16,"Shoulder Catch", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(17,"Weave (Inside)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(18,"Weave (Outside)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(19,"Weave (Outside/Inside)", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            words.add(new Word(20,"Side-by-Side Freespins", "Intermediate 1", R.raw.number_ten, R.raw.video_number_one));
            return words;
        }

        public static MovesManager getInstance(){
            return SingletonHelper.INSTANCE;
        }

        public int getTotalMoves() {
            return getMoves().size();
        }

        public int getTotalBeginnerMoves() {
            int count = 0;
            ArrayList<Word> words = getMoves();
            for (Word word : words) {
                if (word.getMiwokTranslation().toLowerCase().contains("beginner")) {
                    count++;
                }
            }
            return count;
        }

        public int getCompletedBeginnerMoves(Context c) {
            SharedPreferences prefs = c.getSharedPreferences(MovesManager.PREF_FILE_KEY, MODE_PRIVATE);
            int count = 0;
            ArrayList<Word> words = getMoves();
            for (Word word : words) {
                if (word.getMiwokTranslation().toLowerCase().contains("beginner")) {
                    boolean isCheckedFlag = prefs.getBoolean(String.valueOf(word.getItemId()), false);
                    if (isCheckedFlag) {
                        count++;
                    }
                }
            }
            return count;
        }

        public int getTotalInt1Moves() {
            int count = 0;
            ArrayList<Word> words = getMoves();
            for (Word word : words) {
                if (word.getMiwokTranslation().toLowerCase().contains("intermediate 1")) {
                    count++;
                }
            }
            return count;
        }

        public int getCompletedInt1Moves(Context c) {
            SharedPreferences prefs = c.getSharedPreferences(MovesManager.PREF_FILE_KEY, MODE_PRIVATE);
            int count = 0;
            ArrayList<Word> words = getMoves();
            for (Word word : words) {
                if (word.getMiwokTranslation().toLowerCase().contains("intermediate 1")) {
                    boolean isCheckedFlag = prefs.getBoolean(String.valueOf(word.getItemId()), false);
                    if (isCheckedFlag) {
                        count++;
                    }
                }
            }
            return count;
        }

        public int getTotalInt2Moves() {
            int count = 0;
            ArrayList<Word> words = getMoves();
            for (Word word : words) {
                if (word.getMiwokTranslation().toLowerCase().contains("intermediate 2")) {
                    count++;
                }
            }
            return count;
        }

        public int getCompletedInt2Moves(Context c) {
            SharedPreferences prefs = c.getSharedPreferences(MovesManager.PREF_FILE_KEY, MODE_PRIVATE);
            int count = 0;
            ArrayList<Word> words = getMoves();
            for (Word word : words) {
                if (word.getMiwokTranslation().toLowerCase().contains("intermediate 2")) {
                    boolean isCheckedFlag = prefs.getBoolean(String.valueOf(word.getItemId()), false);
                    if (isCheckedFlag) {
                        count++;
                    }
                }
            }
            return count;
        }

        public int getCompletedAdvMoves(Context c) {
            SharedPreferences prefs = c.getSharedPreferences(MovesManager.PREF_FILE_KEY, MODE_PRIVATE);
            int count = 0;
            ArrayList<Word> words = getMoves();
            for (Word word : words) {
                if (word.getMiwokTranslation().toLowerCase().contains("advance")) {
                    boolean isCheckedFlag = prefs.getBoolean(String.valueOf(word.getItemId()), false);
                    if (isCheckedFlag) {
                        count++;
                    }
                }
            }
            return count;
        }

        public int getTotalAdvMoves() {
            int count = 0;
            ArrayList<Word> words = getMoves();
            for (Word word : words) {
                if (word.getMiwokTranslation().toLowerCase().contains("advance")) {
                    count++;
                }
            }
            return count;
        }

        public String getAdvancedProgress(Context c) {
            try {
                float a = (float)getCompletedAdvMoves(c) / getTotalAdvMoves();
                a = a * 100;
                return Integer.toString((Math.round(a))) + "%";
            } catch (ArithmeticException e) {
                return "100%";
            }
        }

        public String getInt1Progress(Context c) {
            try {
                float a = (float)getCompletedInt1Moves(c) / getTotalInt1Moves();
                a = a * 100;
                return Integer.toString((Math.round(a))) + "%";
            } catch (ArithmeticException e) {
                return "100%";
            }
        }

        public String getInt2Progress(Context c) {
            try {
                float a = (float)getCompletedInt2Moves(c) / getTotalInt2Moves();
                a = a * 100;
                return Integer.toString((Math.round(a))) + "%";
            } catch (ArithmeticException e) {
                return "100%";
            }
        }

        public String getBeginnerProgress(Context c) {
            try {
                float a = (float)getCompletedBeginnerMoves(c) / getTotalBeginnerMoves();
                a = a * 100;
                return Integer.toString((Math.round(a))) + "%";
            }catch (ArithmeticException e) {
                return "100%";
            }
        }

}


