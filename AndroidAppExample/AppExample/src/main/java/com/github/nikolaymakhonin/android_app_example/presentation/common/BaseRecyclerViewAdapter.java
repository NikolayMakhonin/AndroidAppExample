package com.github.nikolaymakhonin.android_app_example.presentation.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.nikolaymakhonin.utils.contracts.patterns.ITreeModified;
import com.github.nikolaymakhonin.utils.lists.list.ICollectionChangedList;


public abstract class BaseRecyclerViewAdapter<
    TView extends IView,
    TItem extends ITreeModified,
    TList extends ICollectionChangedList<TItem>,
    TPresenter extends ISinglePresenter<TView, TItem>>
    extends AutoSyncRecyclerViewAdapter<BaseRecyclerViewAdapter.ViewHolder, TItem, TList>
{

    private final IRecyclerViewAdapterFactory<TView, TItem, TPresenter> _factory;

    public BaseRecyclerViewAdapter(IRecyclerViewAdapterFactory<TView, TItem, TPresenter> factory) {
        _factory = factory;
    }

    //region ViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TPresenter _itemPresenter;

        public ViewHolder(TPresenter itemPresenter) {
            super((View) itemPresenter.getView());
            _itemPresenter = itemPresenter;
        }

        public TPresenter getItemPresenter() {
            return _itemPresenter;
        }
    }

    //endregion

    //region Event handlers

    @Override
    public int getItemViewType(int position) {
        return _factory.getItemViewType(position, _showedItems.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TView      view      = _factory.createItemView(parent, viewType);
        TPresenter presenter = _factory.createItemPresenter(view, viewType);
        //        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.instagram_post, parent, false);

        return new ViewHolder(presenter);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.getItemPresenter().setViewModel(_showedItems.get(position));
    }

    @Override
    public int getItemCount() {
        return _showedItems.size();
    }

    //endregion
}


