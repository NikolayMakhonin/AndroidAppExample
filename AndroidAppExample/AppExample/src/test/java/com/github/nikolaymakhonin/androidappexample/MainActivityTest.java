package com.github.nikolaymakhonin.androidappexample;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/** Docs: http://robolectric.org/ */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.JELLY_BEAN)
public class MainActivityTest {

    private Activity _mainActivity;

    @Before
    public void setUp() {
        _mainActivity = Robolectric.buildActivity(MainActivity.class)
            .create()
            .start()
            .resume()
            .get();
    }

    @Test
    public void test() {
        assertEquals(4, 2 + 2);
        Drawable icon = ResourcesCompat.getDrawable(_mainActivity.getResources(), R.mipmap.ic_launcher, null);
        assertNotNull(icon);
    }
}