package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.view_models.entities;

import com.github.nikolaymakhonin.android_app_example.ui.presentation.common.BaseViewModel;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.serialization.BinaryReader;
import com.github.nikolaymakhonin.utils.serialization.BinaryWriter;
import com.github.nikolaymakhonin.utils.time.DateTime;

import java.net.URI;

import rx.functions.Action0;

public class InstagramPost extends BaseViewModel {

    //region Properties

    //region CreatedTime

    private DateTime _createdTime;

    public DateTime getCreatedTime() {
        return _createdTime;
    }

    public void setCreatedTime(DateTime value) {
        if (CompareUtils.Equals(_createdTime, value)) {
            return;
        }
        _createdTime = value;
        Modified().onNext(null);
    }

    //endregion

    //region PostLink

    private URI _postLink;

    public URI getPostLink() {
        return _postLink;
    }

    public void setPostLink(URI value) {
        if (CompareUtils.Equals(_postLink, value)) {
            return;
        }
        _postLink = value;
        Modified().onNext(null);
    }

    //endregion

    //region PostType

    private int _postType;

    /**
     * see: {@link PostType}
     */
    public int getPostType() {
        return _postType;
    }

    /**
     * see: {@link PostType}
     */
    public void setPostType(int value) {
        if (CompareUtils.Equals(_postType, value)) {
            return;
        }
        _postType = value;
        Modified().onNext(null);
    }

    //endregion

    //region Title

    private String _title;

    public String getTitle() {
        return _title;
    }

    public void setTitle(String value) {
        if (CompareUtils.Equals(_title, value)) {
            return;
        }
        _title = value;
        Modified().onNext(null);
    }

    //endregion

    //region Media

    private Media _media;

    private Action0 _mediaUnBindFunc;

    public Media   getMedia() {
        return _media;
    }

    public void setMedia(Media value) {
        if (CompareUtils.EqualsObjects(_media, value)) {
            return;
        }
        synchronized (_propertySetLocker) {
            if (_mediaUnBindFunc != null) {
                _mediaUnBindFunc.call();
                _mediaUnBindFunc = null;
            }
            _media = value;
            if (_media != null) {
                _mediaUnBindFunc = _treeModifiedMerger.attach(_media.TreeModified());
            }
        }
        Modified().onNext(null);
    }

    //endregion

    //region User

    private User _user;

    private Action0 _userUnBindFunc;

    public User   getUser() {
        return _user;
    }

    public void setUser(User value) {
        if (CompareUtils.EqualsObjects(_user, value)) {
            return;
        }
        synchronized (_propertySetLocker) {
            if (_userUnBindFunc != null) {
                _userUnBindFunc.call();
                _userUnBindFunc = null;
            }
            _user = value;
            if (_user != null) {
                _userUnBindFunc = _treeModifiedMerger.attach(_user.TreeModified());
            }
        }
        Modified().onNext(null);
    }

    //endregion

    //endregion

    //region Serialization

    private static final int _currentVersion = 0;

    @Override
    public void Serialize(BinaryWriter writer) throws Exception {
        writer.write(_currentVersion);

        writer.WriteNullable(_createdTime);
        writer.WriteNullable(_postLink);
        writer.write(_postType);
        writer.WriteNullable(_title);

        writer.WriteNullable(_media, (w, o) -> o.Serialize(w));
        writer.WriteNullable(_user, (w, o) -> o.Serialize(w));
    }

    @Override
    public Object DeSerialize(BinaryReader reader) throws Exception {
        //noinspection UnusedAssignment
        int version = reader.readInt();

        setCreatedTime(reader.ReadNullableDateTime());
        setPostLink(reader.ReadNullableURI());
        setPostType(reader.readInt());
        setTitle(reader.ReadNullableString());

        setMedia(reader.ReadNullable(r -> (Media)new Media().DeSerialize(r)));
        setUser(reader.ReadNullable(r -> (User)new User().DeSerialize(r)));

        return this;
    }

    //endregion
}
