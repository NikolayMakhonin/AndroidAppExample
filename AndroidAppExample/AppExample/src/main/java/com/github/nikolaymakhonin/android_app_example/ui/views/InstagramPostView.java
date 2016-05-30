package com.github.nikolaymakhonin.android_app_example.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.di.components.AppComponent;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.data.InstagramPost;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.data.Media;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters.InstagramPostPresenter;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.views.IInstagramPostView;
import com.github.nikolaymakhonin.common_di.contracts.IHasAppComponentBase;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.strings.StringUtilsExt;

import java.net.URI;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class InstagramPostView extends RelativeLayout implements IInstagramPostView {

    private CardView     _cardView;
    private TextView     _titleTextView;
    private ImageView    _imageView;
    private URI          _currentImageUri;
    private AppComponent _appComponent;
    private int          _imageLayoutWidth;
    private int          _imageLayoutHeight;

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
        _appComponent = ((IHasAppComponentBase<AppComponent>) getContext().getApplicationContext()).getAppComponent();
        LayoutInflater.from(getContext()).inflate(R.layout.instagram_post, this, true);
        _cardView = (CardView) findViewById(R.id.cardView);
        _titleTextView = (TextView) findViewById(R.id.title);
        _imageView = (ImageView) findViewById(R.id.image);

        _imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                _imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                initImageView();
                return false;
            }
        });

        ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
    }

    //endregion

    private boolean _imageViewInitialized;

    private void initImageView() {
        if (_imageViewInitialized) {
            return;
        }
        _imageViewInitialized = true;
        _imageLayoutWidth = _imageView.getWidth();
        _imageLayoutHeight = _imageView.getHeight();
        loadImageByUri(true);
    }

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

    private void loadImageByUri(boolean force) {
        InstagramPost instagramPost = _instagramPost;
        if (_imageLayoutHeight <= 0 && _imageLayoutWidth <= 0 || instagramPost == null) {
            return;
        }
        Media media = instagramPost.getMedia();
        URI imageUri = media.getMediaLink();
        if (imageUri == null) {
            _imageView.setImageDrawable(null);
            _currentImageUri = null;
        } else if (force || !CompareUtils.EqualsObjects(imageUri, _currentImageUri)) {
            _currentImageUri = imageUri;
            int imageWidth = _imageLayoutWidth;
            int imageHeight = (media.getHeight() * imageWidth) / media.getWidth();
            _appComponent.getPicasso()
                .load(imageUri.toString())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading_animated)
                .resize(imageWidth, imageHeight)
                .into(_imageView);
        }
    }

    private InstagramPost _instagramPost;

    @Override
    public void updateView(InstagramPost instagramPost) {
        _instagramPost = instagramPost;
        String title = instagramPost.getTitle();
        _titleTextView.setText(title);
        _titleTextView.setVisibility(StringUtilsExt.isNullOrEmpty(title) ? GONE : VISIBLE);
        loadImageByUri(false);
    }

    //endregion
}
