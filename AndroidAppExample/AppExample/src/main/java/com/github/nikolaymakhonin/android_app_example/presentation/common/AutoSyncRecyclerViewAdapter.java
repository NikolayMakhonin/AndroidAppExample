package com.github.nikolaymakhonin.android_app_example.presentation.common;

import android.support.v7.widget.RecyclerView;

import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;
import com.github.nikolaymakhonin.utils.rx.ListObservableOptimizer;
import com.github.nikolaymakhonin.utils.lists.list.CollectionChangedEventArgs;
import com.github.nikolaymakhonin.utils.lists.list.ICollectionChangedList;
import com.github.nikolaymakhonin.utils.lists.list.SortedList;

import java.util.Collection;

import rx.Subscription;
import rx.functions.Action1;

public abstract class AutoSyncRecyclerViewAdapter<TViewHolder extends RecyclerView.ViewHolder, TItem extends ITreeModified, TList extends ICollectionChangedList<TItem>>
    extends RecyclerView.Adapter<TViewHolder>
{

    //region Subscribe CollectionChanged

    private Subscription _collectionChangedSubscription;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        _collectionChangedSubscription = _listObservableOptimizer.observable()
            .subscribe((Action1<CollectionChangedEventArgs>) e -> collectionChanged(e));
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        _collectionChangedSubscription.unsubscribe();
    }

    //endregion

    //region CollectionChanged

    private void collectionChanged(CollectionChangedEventArgs e) {

        int      count, oldIndex, newIndex;
        Object[] newItems, oldItems;

        switch (e.getChangedType()) {

            case Added:
                newIndex = e.getNewIndex();
                newItems = e.getNewItems();
                count = newItems.length;
                for (int i = 0; i < count; i++) {
                    TItem item = (TItem) newItems[i];
                    _showedItems.Insert(newIndex + i, item);
                }
                notifyItemRangeInserted(e.getNewIndex(), e.getNewItems().length);
                break;

            case Removed:
                oldIndex = e.getOldIndex();
                oldItems = e.getOldItems();
                count = oldItems.length;
                for (int i = count - 1; i >= 0; i--) {
                    _showedItems.RemoveAt(oldIndex + i);
                }
                notifyItemRangeRemoved(e.getOldIndex(), e.getOldItems().length);
                break;

            case Setted:
                _showedItems.set(e.getNewIndex(), (TItem) e.getOldItems()[0]);
                notifyItemChanged(e.getNewIndex());
                break;

            case Resorted:
                _showedItems.clear();
                _showedItems.addAll((Collection) e.getObject());
                notifyDataSetChanged();
                break;

            case Moved:
                _showedItems.Move(e.getOldIndex(), e.getNewIndex());
                notifyItemMoved(e.getOldIndex(), e.getNewIndex());
                break;
        }
    }

    //endregion

    //region Properties

    //region Items

    private TList _items;

    public TList getItems() {
        return _items;
    }

    public void setItems(TList value) {
        if (CompareUtils.EqualsObjects(_items, value)) {
            return;
        }
        _items = value;
        _listObservableOptimizer.setSource(_items);
    }

    private final ListObservableOptimizer<TItem> _listObservableOptimizer = new ListObservableOptimizer<>();

    //endregion

    //region ShowedItems

    protected final SortedList<TItem> _showedItems = new SortedList(false, false);

    public ICollectionChangedList<TItem> getShowedItems() {
        return _showedItems;
    }

    //endregion

    //endregion
}
