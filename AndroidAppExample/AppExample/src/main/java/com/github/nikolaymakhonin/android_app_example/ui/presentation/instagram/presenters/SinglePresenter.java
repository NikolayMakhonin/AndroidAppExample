package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.presenters;

import com.github.nikolaymakhonin.android_app_example.ui.presentation.common.IView;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.patterns.BaseTreeModified;
import com.github.nikolaymakhonin.utils.contracts.patterns.IDisposable;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;
import com.github.nikolaymakhonin.utils.rx.RxOperators;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * One view and one viewModel
 */
public abstract class SinglePresenter<TView extends IView, TViewModel extends ITreeModified> extends BaseTreeModified implements IDisposable {

    //region Update view subscription

    private Observable<TView> createDoUpdateViewObservable() {
        return Observable.merge(
            Observable.just(null),
            TreeModified()
        )
            .filter(o -> isViewBind())
            .replay(1)
            .lift(RxOperators.deferred(250, TimeUnit.MILLISECONDS))
            .filter(o -> isViewBind())
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<TView> _doUpdateViewObservable;

    private Subscription _doUpdateViewSubscription;

    private void subscribeDoUpdateView() {
        if (_doUpdateViewObservable == null) {
            _doUpdateViewObservable = createDoUpdateViewObservable();
        }
        _doUpdateViewSubscription = _doUpdateViewObservable
            .subscribe(o -> {
                TView view = _view;
                if (!isViewBind(view)) {
                    return;
                }
                updateView(view);
            });
    }

    private void unSubscribeDoUpdateView() {
        if (_doUpdateViewSubscription != null) {
            _doUpdateViewSubscription.unsubscribe();
            _doUpdateViewSubscription = null;
        }
    }

    //endregion

    //region Bind View

    private Subscription _viewAttachedSubscription;

    private void bindView() {
        if (!_allowBindView || _view == null) {
            return;
        }

        _viewAttachedSubscription =
            Observable.merge(
                Observable.just(_view.isAttached()),
                _view.attachedObservable()
            )
            .subscribe(attached -> {
                synchronized (_propertySetLocker) {
                    if (attached) {
                        subscribeDoUpdateView();
                    } else {
                        unSubscribeDoUpdateView();
                    }
                }
            });
    }

    private void unBindView() {
        if (_viewAttachedSubscription != null) {
            _viewAttachedSubscription.unsubscribe();
            _viewAttachedSubscription = null;
        }
        unSubscribeDoUpdateView();
    }

    protected abstract void updateView(TView view);

    //endregion

    //region Properties

    //region View

    private TView _view;

    public TView getView() {
        return _view;
    }

    public void setView(TView value) {
        if (CompareUtils.EqualsObjects(_view, value)) {
            return;
        }
        synchronized (_propertySetLocker) {
            unBindView();
            _view = value;
            bindView();
        }
        Modified().onNext(null);
    }

    //endregion

    //region ViewModel

    private TViewModel _viewModel;

    private Action0 _viewModelUnBindFunc;

    public TViewModel getViewModel() {
        return _viewModel;
    }

    public void setViewModel(TViewModel value) {
        if (CompareUtils.EqualsObjects(_viewModel, value)) {
            return;
        }
        synchronized (_propertySetLocker) {
            if (_viewModelUnBindFunc != null) {
                _viewModelUnBindFunc.call();
                _viewModelUnBindFunc = null;
            }
            _viewModel = value;
            if (_viewModel != null) {
                _viewModelUnBindFunc = bindToTreeModified(_viewModel.TreeModified());
            }
        }
        Modified().onNext(null);
    }

    //endregion

    //region AllowBindView

    private boolean _allowBindView;

    public boolean isAllowBindView() {
        return _allowBindView;
    }

    public void setAllowBindView(boolean value) {
        if (CompareUtils.Equals(_allowBindView, value)) {
            return;
        }
        synchronized (_propertySetLocker) {
            unBindView();
            _allowBindView = value;
            bindView();
        }
        Modified().onNext(null);
    }

    //endregion

    //endregion

    //region State properties

    public boolean isViewBind() {
        return isViewBind(_view);
    }

    private boolean isViewBind(TView view) {
        return _allowBindView && (view != null) && view.isAttached();
    }

    //endregion

    @Override
    public void dispose() {
        setView(null);
    }
}
