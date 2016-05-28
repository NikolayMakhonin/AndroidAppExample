package com.github.nikolaymakhonin.android_app_example.ui.presentation.common;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.patterns.BaseTreeModified;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;
import com.github.nikolaymakhonin.utils.rx.RxOperators;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * One view and one viewModel
 */
public abstract class SinglePresenter<TView extends IView, TViewModel extends ITreeModified> extends BaseTreeModified implements ISinglePresenter<TView,TViewModel> {

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
                TViewModel viewModel = _viewModel;
                if (!isViewBind(view, viewModel)) {
                    return;
                }
                updateView(view, viewModel);
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
        if (!_allowBindView || _view == null || _viewModel == null) {
            return;
        }

        _viewAttachedSubscription =
            Observable.merge(
                Observable.just(_view.isAttached()),
                _view.attachedObservable()
            )
            .subscribe((Action1<Boolean>) attached -> {
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

    protected void updateView(TView view, TViewModel viewModel) {
        view.updateView(viewModel);
    }

    //endregion

    //region Properties

    //region View

    private TView _view;

    @Override
    public TView getView() {
        return _view;
    }

    @Override
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

    @Override
    public TViewModel getViewModel() {
        return _viewModel;
    }

    @Override
    public void setViewModel(TViewModel value) {
        if (CompareUtils.EqualsObjects(_viewModel, value)) {
            return;
        }
        synchronized (_propertySetLocker) {
            boolean mustReBindView = _viewModel == null || value == null;
            if (mustReBindView) {
                unBindView();
            }
            if (_viewModelUnBindFunc != null) {
                _viewModelUnBindFunc.call();
                _viewModelUnBindFunc = null;
            }
            _viewModel = value;
            if (_viewModel != null) {
                _viewModelUnBindFunc = _treeModifiedMerger.attach(_viewModel.TreeModified());
            }
            if (mustReBindView) {
                bindView();
            }
        }
        Modified().onNext(null);
    }

    //endregion

    //region AllowBindView

    private boolean _allowBindView;

    @Override
    public boolean isAllowBindView() {
        return _allowBindView;
    }

    @Override
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

    @Override
    public boolean isViewBind() {
        return isViewBind(_view, _viewModel);
    }

    private boolean isViewBind(TView view, TViewModel viewModel) {
        return _allowBindView && viewModel != null && (view != null) && view.isAttached();
    }

    //endregion

    @Override
    public void dispose() {
        setView(null);
    }
}
