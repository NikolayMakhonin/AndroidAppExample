package com.github.nikolaymakhonin.utils.contracts.patterns.mvp;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.patterns.BaseTreeModified;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;
import com.github.nikolaymakhonin.utils.rx.RxOperators;
import com.github.nikolaymakhonin.utils.threads.ThreadUtils;

import org.javatuples.Pair;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * One view and one viewModel
 */
public abstract class SinglePresenter<TView extends IView, TViewModel extends ITreeModified> extends BaseTreeModified implements ISinglePresenter<TView,TViewModel> {

    //region Data stream transformation

    protected Observable<Pair<TView, TViewModel>> preUpdateView(Observable<Pair<TView, TViewModel>> dataStream) {
        return dataStream;
    }

    //endregion

    //region Update view subscription

    private Observable<Pair<TView, TViewModel>> _doUpdateViewObservable;

    private boolean _doUpdateViewObservableInitialized;

    private void initDoUpdateViewObservable() {
        if (_doUpdateViewObservableInitialized) {
            return;
        }

        _doUpdateViewObservableInitialized = true;

        //noinspection RedundantCast
        Observable<Pair<TView, TViewModel>> doUpdateViewObservable = TreeModified()
            .filter(o -> isViewBind())
            .lift(RxOperators.deferred(250, TimeUnit.MILLISECONDS))
            .filter(o -> isViewBind());
        doUpdateViewObservable = preUpdateView(doUpdateViewObservable);
        doUpdateViewObservable.observeOn(AndroidSchedulers.mainThread());

        _doUpdateViewObservable = doUpdateViewObservable;
    }

    private Subscription _doUpdateViewSubscription;

    private void subscribeDoUpdateView() {
        initDoUpdateViewObservable();
        //noinspection RedundantCast
        _doUpdateViewSubscription = _doUpdateViewObservable
            .subscribe(o -> updateView());
    }

    private void unSubscribeDoUpdateView() {
        if (_doUpdateViewSubscription != null) {
            _doUpdateViewSubscription.unsubscribe();
            _doUpdateViewSubscription = null;
        }
    }

    private void updateView() {
        TView view = _view;
        TViewModel viewModel = _viewModel;
        if (!isViewBind(view, viewModel)) {
            return;
        }
        updateView(view, viewModel);
    }

    protected void updateView(TView view, TViewModel viewModel) {
        view.updateView(viewModel);
    }

    //endregion

    //region Bind View

    private Subscription _viewAttachedSubscription;

    private void bindView() {
        if (!_allowBindView || _view == null || _viewModel == null) {
            return;
        }

        boolean isMainThread = ThreadUtils.isMainThread();
        Observable viewAttachedObservable = _view.attachedObservable();
        if (!isMainThread) {
            viewAttachedObservable = Observable
                .concatEager(
                    Observable.just(_view.isAttached())
                        .observeOn(AndroidSchedulers.mainThread()),
                    viewAttachedObservable);
        }

        //noinspection RedundantCast
        _viewAttachedSubscription = viewAttachedObservable
            .map((Func1<Boolean, Boolean>) attached -> {
                updateView();
                return attached;
            })
            .observeOn(Schedulers.computation())
            .subscribe((Action1<Boolean>) attached -> {
                if (attached && _viewAttachedSubscription != null) {
                    subscribeDoUpdateView();
                } else {
                    unSubscribeDoUpdateView();
                }
            });

        if (isMainThread) {
            updateView();
        }
    }

    private void unBindView() {
        if (_viewAttachedSubscription != null) {
            _viewAttachedSubscription.unsubscribe();
            _viewAttachedSubscription = null;
        }
        unSubscribeDoUpdateView();
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

    private boolean _allowBindView = true;

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
