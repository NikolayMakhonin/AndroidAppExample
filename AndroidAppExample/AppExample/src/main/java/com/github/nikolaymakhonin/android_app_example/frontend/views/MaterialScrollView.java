package com.github.nikolaymakhonin.android_app_example.frontend.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerAnimator;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

public class MaterialScrollView extends ObservableScrollView {

    private static final String LOG_TAG = "MaterialScrollView";

    //region Init

    public MaterialScrollView(Context context) {
        super(context);
    }

    public MaterialScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean _initialized;

    private void init() {
        if (_initialized) {
            return;
        }
        _initialized = true;

        setSmoothScrollingEnabled(false);

        ViewGroup innerLayout = getInnerLayout();
        if (innerLayout == null) {
            return;
        }

        View contentView = getContentView();
        if (contentView == null) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.height = getContext().getResources().getDisplayMetrics().heightPixels;

        innerLayout.requestLayout();
        contentView.requestLayout();
    }

    //endregion

    //region MaterialViewPager properties

    private MaterialViewPagerAnimator _pagerAnimator;
    private MaterialViewPagerAnimator getPagerAnimator() {
        if (_pagerAnimator == null) {
            _pagerAnimator = MaterialViewPagerHelper.getAnimator(getContext());
        }
        return _pagerAnimator;
    }

    private int getMinHeaderHeight() {
        //TODO: get current Header height
        return 110;
    }

    private int getMaxHeaderHeight() {
        return getPagerAnimator().getHeaderHeight();
    }

    private int getHeaderHeight() {
        View holderView = getHolderView();
        if (holderView == null) {
            return 0;
        }
        return holderView.getBottom() - _currentScrollY;
    }

    private int getScrollMax() {
        return Math.round(getPagerAnimator().scrollMax - getMinHeaderHeight());
    }

    //endregion

    //region Scroll handlers

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

    private int _currentScrollY;

    @Override
    public int getCurrentScrollY() {
        return _currentScrollY;
    }

    @Override
    public void scrollTo(int x, int y) {
        int scrollMax = getScrollMax();

        if (y > scrollMax) {
            y = scrollMax;
        }
        if (y < 0) {
            y = 0;
        }
        if (y == _currentScrollY) {
            return;
        }

        //Fix render errors using Smooth scroll
        int dy = _currentScrollY - y;
        if (Math.abs(dy) > 10) {
            for (int smoothY = _currentScrollY; smoothY < y; smoothY += 10) {
                _currentScrollY = smoothY;
                Log.i(LOG_TAG, String.format("scrollTo(%s), scrollMax=%s", smoothY, scrollMax));
                super.scrollTo(x, _currentScrollY);
            }
        }

        Log.i(LOG_TAG, String.format("scrollTo(%s), scrollMax=%s", y, scrollMax));
        _currentScrollY = y;
        super.scrollTo(x, _currentScrollY);

        _contentHeightRequested = false;
        updateContentHeight();
    }

    private float _lastMotionX;
    private float _lastMotionY;

    private boolean onMouseEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int dy = Math.round(y - _lastMotionY);
        int headerHeight = getHeaderHeight();

//        Log.i(LOG_TAG, String.format("MouseEvent: Action=%s; [%s, %s] -> [%s, %s]; dy=%s; headerHeight=%s",
//            event.getAction(), _lastMotionX, _lastMotionY, x, y, dy, headerHeight));

        boolean lastMotionInHeader = dy < 0
            ? y <= headerHeight
            : _lastMotionY <= headerHeight - 10;

        int newScrollY = _currentScrollY - dy;

        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_MOVE:
            case MotionEvent.ACTION_MOVE:
                if (lastMotionInHeader) {
                    scrollVerticallyTo(newScrollY);
                }

                if (dy != 0) {
                    _lastMotionX = x;
                    _lastMotionY = y;
                }
                break;

            case MotionEvent.ACTION_HOVER_ENTER:
            case MotionEvent.ACTION_DOWN:
                if (dy != 0) {
                    _lastMotionX = x;
                    _lastMotionY = y;
                }
                break;
        }

        return false;
    }

    //endregion

    //region Get inner views

    private ViewGroup getInnerLayout() {
        if (getChildCount() == 0) {
            Log.e(LOG_TAG, "ScrollView inner layout not found (childCount == 0)");
            return null;
        }
        View view = getChildAt(0);

        if (!(view instanceof ViewGroup)) {
            Log.e(LOG_TAG, "ScrollView inner layout not found (child[0] not instance of ViewGroup)");
            return null;
        }

        return (ViewGroup) view;
    }

    private View getContentView() {
        return getContentView(getInnerLayout());
    }

    private View getContentView(ViewGroup innerLayout) {
        if (innerLayout.getChildCount() != 2) {
            Log.e(LOG_TAG, "ScrollView inner layout child count != 2. First and third must be '@layout/material_view_pager_placeholder'. Second must be content view.");
            return null;
        }
        return innerLayout.getChildAt(1);
    }

    private View getHolderView() {
        return getHolderView(getInnerLayout());
    }

    private View getHolderView(ViewGroup innerLayout) {
        if (innerLayout.getChildCount() != 2) {
            Log.e(LOG_TAG, "ScrollView inner layout child count != 2. First and third must be '@layout/material_view_pager_placeholder'. Second must be content view.");
            return null;
        }
        return innerLayout.getChildAt(0);
    }

    //endregion

    //region Update views

    private boolean _contentHeightRequested;

    private void updateContentHeight() {
        if (_contentHeightRequested) {
            return;
        }

        ViewGroup innerLayout = getInnerLayout();
        if (innerLayout == null) {
            return;
        }

        View holderView = getHolderView(innerLayout);
        if (holderView == null) {
            return;
        }

        View contentView = getContentView(innerLayout);
        if (contentView == null) {
            return;
        }

        int holderHeight = holderView.getHeight();
        if (holderHeight == 0) {
            return;
        }

        ViewGroup.MarginLayoutParams contentViewParams = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();

        contentViewParams.height = getHeight() - holderHeight + _currentScrollY;
        contentViewParams.bottomMargin = holderHeight;

        _contentHeightRequested = true;

        contentView.measure(
            MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentViewParams.height, MeasureSpec.EXACTLY));
        contentView.layout(0, holderHeight, getWidth(), contentViewParams.height + holderHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        init();
        updateContentHeight();
    }

    //endregion
}
