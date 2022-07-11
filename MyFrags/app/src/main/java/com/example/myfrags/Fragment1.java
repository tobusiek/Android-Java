package com.example.myfrags;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Fragment1 extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        Button buttonShuffle = (Button) view.findViewById(R.id.button_shuffle);
        buttonShuffle.setOnClickListener(v -> {
            if (callback != null) callback.onButtonClickShuffle();
        });

        Button buttonClockwise = (Button) view.findViewById(R.id.button_clockwise);
        buttonClockwise.setOnClickListener(v -> {
            if (callback != null) callback.onButtonClickClockwise();
        });

        Button buttonHide = (Button) view.findViewById(R.id.button_hide);
        buttonHide.setOnClickListener(v -> {
            if (callback != null) callback.onButtonClickHide();
        });

        Button buttonRestore = (Button) view.findViewById(R.id.button_restore);
        buttonRestore.setOnClickListener(v -> {
            if (callback != null) callback.onButtonClickRestore();
        });

        return view;
    }


    public interface OnButtonClickListener {
        void onButtonClickShuffle();
        void onButtonClickClockwise();
        void onButtonClickHide();
        void onButtonClickRestore();
    }

    private OnButtonClickListener callback = null;

    public void setOnButtonClickListener(OnButtonClickListener callback) {
        this.callback = callback;
    }
}