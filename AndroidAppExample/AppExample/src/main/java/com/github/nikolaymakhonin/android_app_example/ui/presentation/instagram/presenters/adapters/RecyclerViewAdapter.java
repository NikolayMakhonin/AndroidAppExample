package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.presenters.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.ui.presentation.common.ListObservableOptimizer;
import com.github.nikolaymakhonin.utils.CompareUtils;
import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;
import com.github.nikolaymakhonin.utils.lists.list.CollectionChangedEventArgs;
import com.github.nikolaymakhonin.utils.lists.list.ICollectionChangedList;
import com.github.nikolaymakhonin.utils.lists.list.SortedList;

import java.util.Collection;

import rx.Subscription;
import rx.functions.Action1;

public class RecyclerViewAdapter<TItem extends ITreeModified, TList extends ICollectionChangedList<TItem>>
    extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //region Subscribe CollectionChanged

    private Subscription _collectionChangedSubscription;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        _collectionChangedSubscription = _listObservableOptimizer
            .observable()
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

        int count, oldIndex, newIndex;
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
                _showedItems.set(e.getNewIndex(), e.getOldItems()[0]);
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

    //region Classes

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView _cardView;
        private TextView _titleView;
        private Object _item;

        public ViewHolder(View itemView) {
            super(itemView);

            initControls(itemView);
        }

        private void initControls(View itemView) {
            _titleView = (TextView) itemView.findViewById(R.id.title);
            _cardView = (CardView) itemView.findViewById(R.id.cardView);
        }

        private Object getItem() {
            return _item;
        }

        private void setItem(Object item) {
            if (CompareUtils.EqualsObjects(_item, item)) {
                return;
            }
            _item = item;
            updateView();
        }

        private void updateView() {
            //TODO: fill view from _item
        }
    }

    //endregion

    //region Properties

    //region Items

    private TList _items;

    public TList   getItems() {
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

    private final SortedList _showedItems = new SortedList(false, false);

    public ICollectionChangedList<TItem> getShowedItems() {
        return _showedItems;
    }

    //endregion

    //endregion

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.instagram_post, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(_showedItems.get(position));
    }

    @Override
    public int getItemCount() {
            return _showedItems.size();
        }
}
