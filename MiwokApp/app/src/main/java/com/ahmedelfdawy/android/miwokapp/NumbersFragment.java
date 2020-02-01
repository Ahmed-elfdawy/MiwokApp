package com.ahmedelfdawy.android.miwokapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class NumbersFragment extends Fragment {

    private ArrayList<Word> mWords;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mMediaPlayer.stop();
                mMediaPlayer.seekTo(0);
            }else if (focusChange == mAudioManager.AUDIOFOCUS_GAIN) mMediaPlayer.start();
            else if (focusChange == mAudioManager.AUDIOFOCUS_LOSS) releaseMediaPlayer();
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mWords = new ArrayList<Word>();

        mWords.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        mWords.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        mWords.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        mWords.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        mWords.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        mWords.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        mWords.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        mWords.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        mWords.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        mWords.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), mWords, R.color.category_numbers);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word currentWord = mWords.get(position);
                releaseMediaPlayer();
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                mMediaPlayer = MediaPlayer.create(getActivity(), currentWord.getAudioResourceId());
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    public void releaseMediaPlayer(){
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
