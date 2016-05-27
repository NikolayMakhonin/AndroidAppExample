package com.github.nikolaymakhonin.utils.lists.map;


public enum DictionaryChangedType {
    /** установлены свойства Index, OldValue */
    Removed,
    /** установлены свойства Index, NewValue */
    Added,
    /** установлены свойства Index, OldValue, NewValue, */
    Setted
}
