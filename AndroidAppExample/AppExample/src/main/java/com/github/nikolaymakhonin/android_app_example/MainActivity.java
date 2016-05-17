package com.github.nikolaymakhonin.android_app_example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();
    }

    //region Init controls

    private void initControls() {
        initToolbar();
    }

    private void initToolbar() {
        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setTitle(R.string.app_name);
        _toolbar.setOnMenuItemClickListener(menuItem -> false);
        _toolbar.inflateMenu(R.menu.toolbar);
    }

    //endregion
}
