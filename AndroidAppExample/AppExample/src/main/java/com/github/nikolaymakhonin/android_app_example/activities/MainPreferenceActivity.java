package com.github.nikolaymakhonin.android_app_example.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.fragments.MainPreferenceFragment;
import com.yalantis.starwars.TilesFrameLayout;

import net.xpece.android.support.preference.ColorPreference;
import net.xpece.android.support.preference.PreferenceScreenNavigationStrategy;
import net.xpece.android.support.preference.Util;
import net.xpece.android.support.preference.XpColorPreferenceDialogFragment;

public class MainPreferenceActivity extends AppCompatActivity implements
    PreferenceFragmentCompat.OnPreferenceStartScreenCallback,
    PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback,
    PreferenceScreenNavigationStrategy.ReplaceFragment.Callbacks,
    FragmentManager.OnBackStackChangedListener {

    private MainPreferenceFragment                             _mainPreferenceFragment;
    private PreferenceScreenNavigationStrategy.ReplaceFragment _replaceFragmentStrategy;
    private Toolbar                                            _toolbar;
    private TextSwitcher                                       _titleSwitcher;
    private CharSequence                                       _title;
    private TilesFrameLayout                                   _tilesFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_main);

        //region Init preference fragment

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("MainPreference");

        if (fragment == null) {
            _mainPreferenceFragment = MainPreferenceFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction().add(R.id.content, _mainPreferenceFragment, "MainPreference").commit();
        } else {
            _mainPreferenceFragment = (MainPreferenceFragment) fragment;
        }

        //endregion

        //region Switch screens

        _replaceFragmentStrategy = new PreferenceScreenNavigationStrategy.ReplaceFragment(
            this, R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out);

        //endregion

        //region Toolbar

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Cross-fading title setup.
        _title = getTitle();

        _titleSwitcher = new TextSwitcher(_toolbar.getContext());
        _titleSwitcher.setFactory(() -> {
            TextView tv = new AppCompatTextView(_toolbar.getContext());
            TextViewCompat.setTextAppearance(tv, R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);
            return tv;
        });
        _titleSwitcher.setCurrentText(_title);

        actionBar.setCustomView(_titleSwitcher);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        // Add to hierarchy before accessing layout params.
        int margin = Util.dpToPxOffset(this, 16);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) _titleSwitcher.getLayoutParams();
        lp.leftMargin = margin;
        lp.rightMargin = margin;

        _titleSwitcher.setInAnimation(this, R.anim.abc_fade_in);
        _titleSwitcher.setOutAnimation(this, R.anim.abc_fade_out);

        //endregion

        //region Close animation

        _tilesFrameLayout = (TilesFrameLayout) findViewById(R.id.tilesFrameLayout);

        //endregion
    }

    //region Close animation

    @Override
    protected void onResume() {
        super.onResume();
        _tilesFrameLayout.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _tilesFrameLayout.onPause();
    }

    @Override
    public void finish() {
        startFinishAnimation();
    }

    private boolean _finishAnimationStarted;

    private void startFinishAnimation() {
        if (_finishAnimationStarted) {
            return;
        }
        _finishAnimationStarted = true;
        _tilesFrameLayout.setOnAnimationFinishedListener(() -> {
            super.finish();
            overridePendingTransition(0, 0);
        });

        //Start destroy animation
        _tilesFrameLayout.startAnimation();
    }

    //endregion

    //region ToolBar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_pref, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);

        if (!_title.equals(title)) {
            _title = title;

            // Only switch if the title differs. Used for the first hook.
            _titleSwitcher.setText(title);
        }
    }

    //endregion

    //region Switch screens

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen) {
        _replaceFragmentStrategy.onPreferenceStartScreen(getSupportFragmentManager(), preferenceFragmentCompat, preferenceScreen);
        return true;
    }

    @Override
    public PreferenceFragmentCompat onBuildPreferenceFragment(String rootKey) {
        return MainPreferenceFragment.newInstance(rootKey);
    }

    @Override
    public void onBackStackChanged() {
        _mainPreferenceFragment = (MainPreferenceFragment) getSupportFragmentManager().findFragmentByTag("MainPreference");
    }

    //endregion

    //region Color dialog

    @Override
    public boolean onPreferenceDisplayDialog(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference) {
        String   key = preference.getKey();
        DialogFragment dialogFragment;
        if (preference instanceof ColorPreference) {
            dialogFragment = XpColorPreferenceDialogFragment.newInstance(key);
        } else {
            return false;
        }

        dialogFragment.setTargetFragment(preferenceFragmentCompat, 0);
        dialogFragment.show(getSupportFragmentManager(), key);
        return true;
    }

    //endregion
}
