package com.github.nikolaymakhonin.utils;

/** You can use single array instead: int[] refParam = new int[] { 123 }; */
public class RefParam<TParam> {
    public RefParam() {}
    
    public RefParam(final TParam value) {
        this.value = value;
    }
    
    public TParam value;
}
