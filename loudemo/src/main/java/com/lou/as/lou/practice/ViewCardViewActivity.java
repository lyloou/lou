package com.lou.as.lou.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.lou.as.lou.R;
import com.lyloou.lou.util.Uview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewCardViewActivity extends AppCompatActivity {

    @BindView(R.id.practic_card_view_tv)
    TextView mPracticCardViewTv;
    @BindView(R.id.practice_card_view_cv)
    CardView mPracticeCardViewCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card_view);
        ButterKnife.bind(this);
        mPracticCardViewTv.setText("Who are you?");

        Uview.rippleItWithBorderless(mPracticCardViewTv);
    }
}
