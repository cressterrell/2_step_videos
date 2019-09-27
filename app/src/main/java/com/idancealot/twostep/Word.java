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
package com.idancealot.twostep;

/**
 * {@link Word} represents a vocabulary word that the user wants to learn.
 * It contains a default translation, a Miwok translation, and an image for that word.
 */
public class Word {

    /** Default translation for the word */
    private String mDefaultTranslation;

    /** Miwok translation for the word */
    private String mMiwokTranslation;

    /** Audio resource ID for the word */
    private int mAudioResourceId;

    /** ID used for uniquely identifying the item */
    private int mItemId;

    /** Image resource ID for the word */
    //private int mImageResourceId = NO_RESOUCE_PROVIDED;

    /** the video in the raw file */
    private int mVideoResourceId = NO_RESOUCE_PROVIDED;

    /** Constant value that represents no image was provided for this word */
    public static final int NO_RESOUCE_PROVIDED = -1;

    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation is the word in the Miwok language
     * @param audioResourceId is the resource ID for the audio file associated with this word
     */
    public Word(int itemId, String defaultTranslation, String miwokTranslation, int audioResourceId) {
        mItemId = itemId;
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
        mVideoResourceId = NO_RESOUCE_PROVIDED;
    }

    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation is the word in the Miwok language
     * @param audioResourceId is the resource ID for the audio file associated with this word
     * @param videoResourceId is the resource ID for the video file associated with this word
     */
    public Word(int itemId, String defaultTranslation, String miwokTranslation, int audioResourceId, int videoResourceId) {
        mItemId = itemId;
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
        mVideoResourceId = videoResourceId;
    }

    /**
     * Get the unique item id
     */
    public int getItemId() {
        return mItemId;
    }

    /**
     * Get the default translation of the word.
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the Miwok translation of the word.
     */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     * Return the audio resource ID of the word.
     */
    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    /**
     * Return the audio resource ID of the word.
     */
    public int getVideoResourceId() {
        return mVideoResourceId;
    }
}