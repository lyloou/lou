package com.lyloou.lou.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.lyloou.lou.R;


/**
 * @author Lyloou
 *         A common Activity class with FragmentManager.
 */
public abstract class SingleFragmentActivity extends LouActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        addFragmentToActivity();
    }

    /**
     * Add Fragment to Activity
     */
    private void addFragmentToActivity() {
        FragmentManager fm = getFragmentManager();

        // Try to get fragment from FragmentManager's Queue.
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // Create a fragment if not find fragment in FragmentManager's Queue
        if (fragment == null) {
            // get fragment, the sub class will do it.
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    /**
     * @return create a fragment, let sub class implement the method.
     */
    public abstract Fragment createFragment();
}
