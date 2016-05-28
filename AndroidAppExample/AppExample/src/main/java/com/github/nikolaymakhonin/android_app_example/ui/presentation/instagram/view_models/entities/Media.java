package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.view_models.entities;

import com.github.nikolaymakhonin.android_app_example.ui.presentation.common.BaseViewModel;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.serialization.BinaryReader;
import com.github.nikolaymakhonin.utils.serialization.BinaryWriter;

import java.net.URI;

public class Media extends BaseViewModel {

    //region Properties

    //region Height

    private int _height;

    public int getHeight() {
        return _height;
    }

    public void setHeight(int value) {
        if (CompareUtils.Equals(_height, value)) {
            return;
        }
        _height = value;
        Modified().onNext(null);
    }

    //endregion

    //region MediaLink

    private URI _mediaLink;

    public URI getMediaLink() {
        return _mediaLink;
    }

    public void setMediaLink(URI value) {
        if (CompareUtils.Equals(_mediaLink, value)) {
            return;
        }
        _mediaLink = value;
        Modified().onNext(null);
    }

    //endregion

    //region Width

    private int _width;

    public int getWidth() {
        return _width;
    }

    public void setWidth(int value) {
        if (CompareUtils.Equals(_width, value)) {
            return;
        }
        _width = value;
        Modified().onNext(null);
    }

    //endregion

    //endregion

    //region Serialization

    private static final int _currentVersion = 0;

    @Override
    public void Serialize(BinaryWriter writer) throws Exception {
        writer.write(_currentVersion);

        writer.write(_height);
        writer.WriteNullable(_mediaLink);
        writer.write(_width);
    }

    @Override
    public Object DeSerialize(BinaryReader reader) throws Exception {
        //noinspection UnusedAssignment
        int version = reader.readInt();

        setHeight(reader.readInt());
        setMediaLink(reader.ReadNullableURI());
        setWidth(reader.readInt());

        return this;
    }

    //endregion
}
