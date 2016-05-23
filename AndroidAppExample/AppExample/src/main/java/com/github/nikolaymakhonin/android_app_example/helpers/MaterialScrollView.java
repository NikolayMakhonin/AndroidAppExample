package com.github.nikolaymakhonin.android_app_example.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

public class MaterialScrollView extends ObservableScrollView {

    public MaterialScrollView(Context context) {
        super(context);
        init();
    }

    public MaterialScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }

    private MaterialViewPagerAnimator _pagerAnimator;
    private MaterialViewPagerAnimator getPagerAnimator() {
        if (_pagerAnimator == null) {
            _pagerAnimator = MaterialViewPagerHelper.getAnimator(getContext());
        }
        return _pagerAnimator;
    }

    private int getHeaderHeight() {
        //TODO: get current Header height
        return 110;
    }

    private int getMaxHeaderHeight() {
        return getPagerAnimator().getHeaderHeight();
    }

    private int getScrollMax() {
        return Math.round(getPagerAnimator().scrollMax - getHeaderHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return onMouseEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return onMouseEvent(event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return onMouseEvent(event);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);

        if (isMaxScroll()) {
            scrollVerticallyTo(getScrollMax());
        }
    }

    private boolean isMaxScroll() {
        int scrollY = getCurrentScrollY();
        int scrollMax = getScrollMax();

        return scrollY >= scrollMax;
    }

    private float _lastMotionX;
    private float _lastMotionY;

    private boolean onMouseEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();

                float dy = y - _lastMotionY;
                if (dy < 0) {
                    scrollVerticallyTo(Math.round(getCurrentScrollY() - dy));
                }

                _lastMotionX = x;
                _lastMotionY = y;
                break;
            case MotionEvent.ACTION_DOWN:
                _lastMotionX = event.getX();
                _lastMotionY = event.getY();
                break;
        }

        return false;
    }
}
