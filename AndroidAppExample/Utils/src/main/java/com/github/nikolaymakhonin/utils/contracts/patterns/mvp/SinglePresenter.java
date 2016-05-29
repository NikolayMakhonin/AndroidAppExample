package com.github.nikolaymakhonin.utils.contracts.patterns.mvp;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.patterns.BaseTreeModified;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;
import com.github.nikolaymakhonin.utils.rx.RxOperators;

import org.javatuples.Pair;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;

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

    private Subscription _doUpdateViewObservableReplaySubscription;

    private ConnectableObservable _doUpdateViewObservableReplay;

    private Observable<Pair<TView, TViewModel>> _doUpdateViewObservable;

    private boolean _doUpdateViewObservableInitialized;

    private void initDoUpdateViewObservable() {
        if (_doUpdateViewObservableInitialized) {
            return;
        }

        _doUpdateViewObservableInitialized = true;

        ConnectableObservable doUpdateViewObservableReplay = Observable.merge(
            Observable.just(null),
            TreeModified()
        )
            .filter(o -> isViewBind())
            .replay(1);

        //noinspection RedundantCast
        Observable<Pair<TView, TViewModel>> doUpdateViewObservable = doUpdateViewObservableReplay
            .lift(RxOperators.deferred(250, TimeUnit.MILLISECONDS))
            .filter(o -> isViewBind())
            .map(o -> new Pair<>(_view, _viewModel))
            .filter((Func1<Pair<TView, TViewModel>, Boolean>)p -> isViewBind(p.getValue0(), p.getValue1()));
        doUpdateViewObservable = preUpdateView(doUpdateViewObservable);
        doUpdateViewObservable.observeOn(AndroidSchedulers.mainThread());

        _doUpdateViewObservableReplay = doUpdateViewObservableReplay;
        _doUpdateViewObservable = doUpdateViewObservable;
    }

    private Subscription _doUpdateViewSubscription;

    private void subscribeDoUpdateView() {
        initDoUpdateViewObservable();
        _doUpdateViewObservableReplaySubscription = _doUpdateViewObservableReplay.connect();
        //noinspection RedundantCast
        _doUpdateViewSubscription = _doUpdateViewObservable
            .subscribe((Action1<Pair<TView, TViewModel>>)p -> {
                TView view = p.getValue0();
                TViewModel viewModel = p.getValue1();
                if (!isViewBind(view, viewModel)) {
                    return;
                }
                updateView(view, viewModel);
            });
    }

    private void unSubscribeDoUpdateView() {
        if (_doUpdateViewObservableReplaySubscription != null) {
            _doUpdateViewObservableReplaySubscription.unsubscribe();
        }
        if (_doUpdateViewSubscription != null) {
            _doUpdateViewSubscription.unsubscribe();
            _doUpdateViewSubscription = null;
        }
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

        //noinspection RedundantCast
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
