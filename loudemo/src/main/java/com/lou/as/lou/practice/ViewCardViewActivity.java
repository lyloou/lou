package com.lou.as.lou.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lou.as.lou.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewCardViewActivity extends AppCompatActivity {

    @BindView(R.id.practic_tv_card_view)
    TextView mPracticTvCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card_view);
        ButterKnife.bind(this);

        mPracticTvCardView.setText("Who are you?");
    }
}
