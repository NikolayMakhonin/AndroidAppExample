package com.github.nikolaymakhonin.utils.contracts.patterns;

import com.github.nikolaymakhonin.utils.rx.DynamicObservablesMerger;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public abstract class BaseTreeModified implements ITreeModified {

    protected final Object _propertySetLocker = new Object();

    //region Modified events

    private final Subject _modifiedSubject = PublishSubject.create();

    @Override
    public Subject Modified() {
        return _modifiedSubject;
    }

    protected final DynamicObservablesMerger _treeModifiedMerger = new DynamicObservablesMerger(_modifiedSubject);

    @Override
    public Observable TreeModified() {
        return _treeModifiedMerger.observable();
    }

    //endregion

}
