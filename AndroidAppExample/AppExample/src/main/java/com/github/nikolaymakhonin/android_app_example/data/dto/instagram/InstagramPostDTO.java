package com.github.nikolaymakhonin.android_app_example.data.dto.instagram;

import com.github.nikolaymakhonin.android_app_example.data.dto.BaseDTO;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.serialization.BinaryReader;
import com.github.nikolaymakhonin.utils.serialization.BinaryWriter;
import com.github.nikolaymakhonin.utils.time.DateTime;

import java.net.URI;

import rx.Subscription;

public class InstagramPostDTO extends BaseDTO {

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

    private MediaDTO _media;

    private Subscription _mediaSubscription;

    public MediaDTO getMedia() {
        return _media;
    }

    public void setMedia(MediaDTO value) {
        if (CompareUtils.EqualsObjects(_media, value)) {
            return;
        }
        if (_mediaSubscription != null) {
            _mediaSubscription.unsubscribe();
            _mediaSubscription = null;
        }
        _media = value;
        if (_media != null) {
            _mediaSubscription = _media.Modified().subscribe(Modified());
        }
        Modified().onNext(null);
    }

    //endregion

    //region User

    private UserDTO _user;

    private Subscription _userSubscription;

    public UserDTO getUser() {
        return _user;
    }

    public void setUser(UserDTO value) {
        if (CompareUtils.EqualsObjects(_user, value)) {
            return;
        }
        if (_userSubscription != null) {
            _userSubscription.unsubscribe();
            _userSubscription = null;
        }
        _user = value;
        if (_user != null) {
            _userSubscription = _user.Modified().subscribe(Modified());
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

        _createdTime = reader.ReadNullableDateTime();
        _postLink = reader.ReadNullableURI();
        _postType = reader.readInt();
        _title = reader.ReadNullableString();

        setMedia(reader.ReadNullable(r -> (MediaDTO) new MediaDTO().DeSerialize(r)));
        setUser(reader.ReadNullable(r -> (UserDTO) new UserDTO().DeSerialize(r)));

        return this;
    }

    //endregion
}
