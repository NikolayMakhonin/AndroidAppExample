package com.github.nikolaymakhonin.android_app_example.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.utils.CompareUtils;

import java.util.Arrays;
import java.util.List;

public class InstagramListAdapter extends RecyclerView.Adapter<InstagramListAdapter.ViewHolder> {

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
            if (CompareUtils.equals(_item, item)) {
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

    private final List<Object> _items = createMockData();

    private static List<Object> createMockData() {
        return Arrays.asList(new Object[] { null, null, null });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.instagram_post, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(_items.get(position));
    }

    @Override
    public int getItemCount() {
        return _items.size();
    }
}
