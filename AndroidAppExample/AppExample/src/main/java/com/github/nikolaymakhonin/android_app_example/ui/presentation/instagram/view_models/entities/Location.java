package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.view_models.entities;

import com.github.nikolaymakhonin.android_app_example.ui.presentation.common.BaseViewModel;
import com.github.nikolaymakhonin.utils.serialization.BinaryReader;
import com.github.nikolaymakhonin.utils.serialization.BinaryWriter;

public class Location extends BaseViewModel {

    private double _latitude;
    private double _longitude;
    private String _name;

    //region Serialization

    private static final int _currentVersion = 0;

    @Override
    public void Serialize(BinaryWriter writer) throws Exception {
        writer.write(_currentVersion);
    }

    @Override
    public Object DeSerialize(BinaryReader reader) throws Exception {
        //noinspection UnusedAssignment
        int version = reader.readInt();

        return this;
    }

    //endregion
}