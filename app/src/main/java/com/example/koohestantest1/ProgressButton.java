package com.example.koohestantest1;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

class ProgressButton {

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;

    ProgressButton(Context ct, View view){
        cardView = view.findViewById(R.id.cardView);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textView);
    }
    void buttonActivated(String text){
        progressBar.setVisibility(View.VISIBLE);
        textView.setText(text);
    }
    void buttonFinished(String text){
        layout.setBackgroundColor(layout.getResources().getColor(R.color.okGreen));
        progressBar.setVisibility(View.GONE);
        textView.setText(text);

    }
}
