package com.github.nikolaymakhonin.android_app_example.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import com.github.florent37.materialviewpager.MaterialViewPagerHeader;
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
        if (isMaxScroll()) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isMaxScroll()) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (isMaxScroll()) {
            return false;
        }
        return super.onGenericMotionEvent(event);
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

//    @Override
//    public void scrollVerticallyTo(int y) {
//        int prevHeaderHeight = getHeaderHeight();
//        int prevScrollY = getScrollY();
//
//        super.scrollVerticallyTo(y);
//
//        int newHeaderHeight = getHeaderHeight();
//        int newScrollY = getScrollY();
//
//        int deltaHeight = newHeaderHeight - prevHeaderHeight;
//        int deltaScroll = newScrollY - prevScrollY;
//
//        if (deltaHeight != deltaScroll) {
//            super.scrollVerticallyTo(deltaScroll - deltaHeight);
//        }
//    }
}
