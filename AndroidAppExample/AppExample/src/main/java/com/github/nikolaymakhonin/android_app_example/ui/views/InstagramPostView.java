package com.github.nikolaymakhonin.android_app_example.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.di.components.AppComponent;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.data.InstagramPost;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters.InstagramPostPresenter;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.views.IInstagramPostView;
import com.github.nikolaymakhonin.common_di.contracts.IHasAppComponentBase;
import com.github.nikolaymakhonin.utils.CompareUtils;

import java.net.URI;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class InstagramPostView extends RelativeLayout implements IInstagramPostView {

    private CardView     _cardView;
    private TextView     _titleTextView;
    private ImageView    _imageView;
    private URI          _currentMediaLink;
    private AppComponent _appComponent;

    //region Constructors

    public InstagramPostView(Context context) {
        super(context);
        initControls();
    }

    public InstagramPostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControls();
    }

    public InstagramPostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControls();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InstagramPostView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initControls();
    }

    //endregion

    //region Init Controls

    private void initControls() {
        _appComponent = ((IHasAppComponentBase<AppComponent>)getContext().getApplicationContext()).getAppComponent();
        LayoutInflater.from(getContext()).inflate(R.layout.instagram_post, this, true);
        _cardView = (CardView) findViewById(R.id.cardView);
        _titleTextView = (TextView) findViewById(R.id.title);
        _imageView = (ImageView) findViewById(R.id.image);
    }

    //endregion

    //region Attached

    private boolean _attached;

    @Override
    public boolean isAttached() {
        return _attached;
    }

    private final Subject<Boolean, Boolean> _attachedSubject = PublishSubject.create();

    @Override
    public Observable<Boolean> attachedObservable() {
        return _attachedSubject;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        _attachedSubject.onNext(_attached = true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        _attachedSubject.onNext(_attached = false);
    }

    //endregion

    //region Presenter property

    private InstagramPostPresenter _presenter;

    @Override
    public InstagramPostPresenter getPresenter() {
        return _presenter;
    }

    @Override
    public void setPresenter(InstagramPostPresenter value) {
        _presenter = value;
    }

    //endregion

    //region Update View

    @Override
    public void updateView(InstagramPost instagramPost) {
        _titleTextView.setText(instagramPost.getTitle());
        URI mediaLink = instagramPost.getMedia().getMediaLink();
        if (mediaLink == null) {
            _imageView.setImageDrawable(null);
        } else if (!CompareUtils.EqualsObjects(mediaLink, _currentMediaLink)) {
            _currentMediaLink = mediaLink;
            _appComponent.getPicasso().load(mediaLink.toString()).into(_imageView);
        }
    }

    //endregion
}
