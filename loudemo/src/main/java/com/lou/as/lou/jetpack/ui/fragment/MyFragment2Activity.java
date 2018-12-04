package com.lou.as.lou.jetpack.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lou.as.lou.R;

public class MyFragment2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fragment2);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentTransaction != null) {
            fragmentTransaction.add(R.id.list, new ArticleListFragment());
            fragmentTransaction.add(R.id.reader, new ArticleReaderFragment());
            fragmentTransaction.commit();
        }
    }

    public void replaceReader(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.reader, new ArticleReaderFragment2());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        Fragment fragment = fragmentManager.findFragmentById(R.id.reader);
        TextView tv = fragment.getView().findViewById(R.id.tv_text);
        tv.postDelayed(() -> {
            tv.setText("asdfasdf");
        }, 2000);
    }
}
