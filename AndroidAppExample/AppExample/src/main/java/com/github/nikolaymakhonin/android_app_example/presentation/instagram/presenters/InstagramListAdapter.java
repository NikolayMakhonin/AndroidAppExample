package com.github.nikolaymakhonin.android_app_example.presentation.instagram.presenters;

import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.WhatsThereApi;
import com.github.nikolaymakhonin.android_app_example.data.apis.whats_there.dto.InstagramSearchResponse;
import com.github.nikolaymakhonin.android_app_example.di.factories.instagram.InstagramDataFactory;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.data.InstagramPost;
import com.github.nikolaymakhonin.android_app_example.presentation.instagram.views.IInstagramPostView;
import com.github.nikolaymakhonin.android_app_example.utils.patterns.mvp.BaseRecyclerViewAdapter;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.IRecyclerViewAdapterFactory;
import com.github.nikolaymakhonin.utils.contracts.patterns.mvp.ISinglePresenter;
import com.github.nikolaymakhonin.utils.lists.list.ICollectionChangedList;
import com.github.nikolaymakhonin.utils.lists.list.SortedList;
import com.github.nikolaymakhonin.utils.rx.RxOperators;

import org.javatuples.Triplet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class InstagramListAdapter extends BaseRecyclerViewAdapter<
    IInstagramPostView,
    InstagramPost,
    ICollectionChangedList<InstagramPost>,
    ISinglePresenter<IInstagramPostView, InstagramPost>>
{
    private final WhatsThereApi _whatsThereApi;

    public InstagramListAdapter(
        IRecyclerViewAdapterFactory<IInstagramPostView, InstagramPost, ISinglePresenter<IInstagramPostView, InstagramPost>> factory,
        WhatsThereApi whatsThereApi)
    {
        super(factory);
        _whatsThereApi = whatsThereApi;
        _loadByGeoRequests
            .lift(RxOperators.deferred(250, TimeUnit.MILLISECONDS))
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.io())
            .flatMap(args -> _whatsThereApi.getInstagramPostsByGeo(args.getValue0(), args.getValue1(), args.getValue2()))
            .first()
            .flatMapIterable(response -> Arrays.asList(response.data))
            .map(post -> InstagramDataFactory.fromWhatsThereDTO(post))
            .toList()
            .subscribe(posts -> setItems(new SortedList<>(false, false, posts)));
    }

    private final Subject<Triplet<Double, Double, Integer>, Triplet<Double, Double, Integer>> _loadByGeoRequests
        = PublishSubject.create();

    public void loadByGeo(double lat, double lng, int radius) {
        _loadByGeoRequests.onNext(new Triplet<>(lat, lng, radius));
    }
}
