package com.github.nikolaymakhonin.android_app_example.ui.presentation.instagram.presenters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.utils.CompareUtils;

public class InstagramPostViewHolder extends RecyclerView.ViewHolder {

    private Object _viewModel;

    private View     _itemView;
    private CardView _cardView;
    private TextView _titleView;

    public InstagramPostViewHolder(View itemView) {
        super(itemView);
        initControls(itemView);
    }

    private void initControls(View itemView) {
        _titleView = (TextView) itemView.findViewById(R.id.title);
        _cardView = (CardView) itemView.findViewById(R.id.cardView);
    }

    private Object getViewModel() {
        return _viewModel;
    }

    private void setViewModel(Object viewModel) {
        if (CompareUtils.EqualsObjects(_viewModel, viewModel)) {
            return;
        }
        _viewModel = viewModel;
        updateView();
    }

    private void updateView() {
        //TODO: fill view from _item
    }
}
