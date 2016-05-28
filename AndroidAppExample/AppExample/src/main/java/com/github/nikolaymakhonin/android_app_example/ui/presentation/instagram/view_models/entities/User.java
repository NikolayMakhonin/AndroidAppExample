package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.view_models.entities;

import com.github.nikolaymakhonin.android_app_example.ui.presentation.common.BaseViewModel;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.serialization.BinaryReader;
import com.github.nikolaymakhonin.utils.serialization.BinaryWriter;

import java.net.URI;

public class User extends BaseViewModel {

    //region Properties

    //region Avatar

    private URI _avatar;

    public URI getAvatar() {
        return _avatar;
    }

    public void setAvatar(URI value) {
        if (CompareUtils.Equals(_avatar, value)) {
            return;
        }
        _avatar = value;
        Modified().onNext(null);
    }

    //endregion

    //region DisplayName

    private String _displayName;

    public String getDisplayName() {
        return _displayName;
    }

    public void setDisplayName(String value) {
        if (CompareUtils.Equals(_displayName, value)) {
            return;
        }
        _displayName = value;
        Modified().onNext(null);
    }

    //endregion

    //region Id

    private long _id;

    public long getId() {
        return _id;
    }

    public void setId(long value) {
        if (CompareUtils.Equals(_id, value)) {
            return;
        }
        _id = value;
        Modified().onNext(null);
    }

    //endregion

    //region IdentityName

    private String _identityName;

    public String getIdentityName() {
        return _identityName;
    }

    public void setIdentityName(String value) {
        if (CompareUtils.Equals(_identityName, value)) {
            return;
        }
        _identityName = value;
        Modified().onNext(null);
    }

    //endregion

    //endregion

    //region Serialization

    private static final int _currentVersion = 0;

    @Override
    public void Serialize(BinaryWriter writer) throws Exception {
        writer.write(_currentVersion);

        writer.WriteNullable(_avatar);
        writer.WriteNullable(_displayName);
        writer.write(_id);
        writer.WriteNullable(_identityName);
    }

    @Override
    public Object DeSerialize(BinaryReader reader) throws Exception {
        //noinspection UnusedAssignment
        int version = reader.readInt();

        setAvatar(reader.ReadNullableURI());
        setDisplayName(reader.ReadNullableString());
        setId(reader.readLong());
        setIdentityName(reader.ReadNullableString());

        return this;
    }

    //endregion
}
