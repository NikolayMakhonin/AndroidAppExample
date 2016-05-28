package com.github.nikolaymakhonin.android_app_example.ui.helpers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

public final class AnimationHelper {

    /** Source: <a href="https://developer.android.com/training/material/animations.html">developer.android.com</a>
      * See also: <a href="https://github.com/ozodrukh/CircularReveal">github.com</a>*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Animator createCircularReveal(View view) {
        // get the center for the clipping circle
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);

        return anim;
    }

    /** Source: <a href="https://developer.android.com/training/material/animations.html">developer.android.com</a>
     * See also: <a href="https://github.com/ozodrukh/CircularReveal">github.com</a>*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Animator createCircularHide(View view) {
        // get the center for the clipping circle
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        return anim;
    }

}
