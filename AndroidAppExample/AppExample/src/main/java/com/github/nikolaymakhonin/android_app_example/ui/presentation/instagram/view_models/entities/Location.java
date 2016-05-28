package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.view_models.entities;

import com.github.nikolaymakhonin.android_app_example.ui.presentation.common.BaseViewModel;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.serialization.BinaryReader;
import com.github.nikolaymakhonin.utils.serialization.BinaryWriter;

public class Location extends BaseViewModel {

    //region Properties

    //region Latitude

    private double _latitude;

    public double   getLatitude() {
        return _latitude;
    }

    public void setLatitude(double value) {
        if (CompareUtils.Equals(_latitude, value)) {
            return;
        }
        _latitude = value;
        Modified().onNext(null);
    }

    //endregion

    //region Longitude

    private double _longitude;

    public double   getLongitude() {
        return _longitude;
    }

    public void setLongitude(double value) {
        if (CompareUtils.Equals(_longitude, value)) {
            return;
        }
        _longitude = value;
        Modified().onNext(null);
    }

    //endregion

    //region Name

    private String _name;

    public String   getName() {
        return _name;
    }

    public void setName(String value) {
        if (CompareUtils.Equals(_name, value)) {
            return;
        }
        _name = value;
        Modified().onNext(null);
    }

    //endregion

    //endregion

    //region Serialization

    private static final int _currentVersion = 0;

    @Override
    public void Serialize(BinaryWriter writer) throws Exception {
        writer.write(_currentVersion);

        writer.write(_latitude);
        writer.write(_longitude);
        writer.WriteNullable(_name);
    }

    @Override
    public Object DeSerialize(BinaryReader reader) throws Exception {
        //noinspection UnusedAssignment
        int version = reader.readInt();

        setLatitude(reader.readDouble());
        setLongitude(reader.readDouble());
        setName(reader.ReadNullableString());

        return this;
    }

    //endregion
}
