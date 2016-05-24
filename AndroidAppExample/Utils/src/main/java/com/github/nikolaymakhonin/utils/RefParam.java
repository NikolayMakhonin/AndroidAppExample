package com.github.nikolaymakhonin.utils;

@Deprecated
/** Use single array instead: int[] refParam = new int[] { 123 }; */
public class RefParam<TParam> {
    public RefParam() {}
    
    public RefParam(final TParam value) {
        this.value = value;
    }
    
    public TParam value;
}
