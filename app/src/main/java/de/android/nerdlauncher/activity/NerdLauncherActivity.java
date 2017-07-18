package de.android.nerdlauncher.activity;

import android.support.v4.app.Fragment;

import de.android.nerdlauncher.fragment.NerdLauncherFragment;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
