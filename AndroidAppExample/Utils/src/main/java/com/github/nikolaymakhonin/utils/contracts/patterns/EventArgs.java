package com.github.nikolaymakhonin.utils.contracts.patterns;

public class EventArgs {

    protected final Object _object;

    private Object getObject() {
        return _object;
    }

    public EventArgs(Object object) {
        _object = object;
    }

}
