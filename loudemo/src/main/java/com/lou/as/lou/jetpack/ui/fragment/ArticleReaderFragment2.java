package com.lou.as.lou.jetpack.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lou.as.lou.R;

public class ArticleReaderFragment2 extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_reader, container, false);
        TextView tvText = view.findViewById(R.id.tv_text);
        tvText.setText("Reader2");
        return view;
    }

}
