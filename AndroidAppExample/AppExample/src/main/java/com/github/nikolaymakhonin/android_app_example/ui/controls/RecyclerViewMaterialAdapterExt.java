package com.github.nikolaymakhonin.android_app_example.ui.controls;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.R;

import java.security.InvalidParameterException;

/**
 * Created by florentchampigny on 24/04/15.
 * A RecyclerView.Adapter which inject a header to the actual RecyclerView.Adapter
 */
public class RecyclerViewMaterialAdapterExt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //the constants value of the header view
    static final int TYPE_PLACEHOLDER = Integer.MIN_VALUE;

    //the size taken by the header
    private int mPlaceholderSize = 1;

    //the actual RecyclerView.Adapter
    private final RecyclerView.Adapter mAdapter;

    /**
     * Construct the RecyclerViewMaterialAdapter, which inject a header into an actual RecyclerView.Adapter
     *
     * @param adapter The real RecyclerView.Adapter which displays content
     */
    public RecyclerViewMaterialAdapterExt(RecyclerView.Adapter adapter) {
        mAdapter = adapter;

        registerAdapterObservers();
    }

    /**
     * Construct the RecyclerViewMaterialAdapter, which inject a header into an actual RecyclerView.Adapter
     *
     * @param adapter         The real RecyclerView.Adapter which displays content
     * @param placeholderSize The number of placeholder items before real items, default is 1
     */
    public RecyclerViewMaterialAdapterExt(RecyclerView.Adapter adapter, int placeholderSize) {
        mAdapter = adapter;
        mPlaceholderSize = placeholderSize;

        registerAdapterObservers();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (mAdapter != null) {
            mAdapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mAdapter != null) {
            mAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    private RecyclerView.AdapterDataObserver _adapterObserver;
    private RecyclerView.AdapterDataObserver _innerAdapterObserver;

    protected void registerAdapterObservers() {
        if(mAdapter != null) {
            _adapterObserver = new RecyclerView.AdapterDataObserver() {

                @Override
                public void onChanged() {
                    super.onChanged();
                    mAdapter.unregisterAdapterDataObserver(_innerAdapterObserver);
                    mAdapter.notifyDataSetChanged();
                    mAdapter.registerAdapterDataObserver(_innerAdapterObserver);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    super.onItemRangeChanged(positionStart, itemCount);
                    if (positionStart < mPlaceholderSize) {
                        if (itemCount - (mPlaceholderSize - positionStart) <= 0) {
                            return;
                        }
                        throw new InvalidParameterException("positionStart < mPlaceholderSize");
                    }
                    positionStart -= mPlaceholderSize;
                    mAdapter.unregisterAdapterDataObserver(_innerAdapterObserver);
                    mAdapter.notifyItemRangeChanged(positionStart, itemCount);
                    mAdapter.registerAdapterDataObserver(_innerAdapterObserver);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    super.onItemRangeChanged(positionStart, itemCount, payload);
                    if (positionStart < mPlaceholderSize) {
                        if (itemCount - (mPlaceholderSize - positionStart) <= 0) {
                            return;
                        }
                        throw new InvalidParameterException("positionStart < mPlaceholderSize");
                    }
                    positionStart -= mPlaceholderSize;
                    mAdapter.unregisterAdapterDataObserver(_innerAdapterObserver);
                    mAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
                    mAdapter.registerAdapterDataObserver(_innerAdapterObserver);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    if (positionStart < mPlaceholderSize) {
                        if (itemCount - (mPlaceholderSize - positionStart) <= 0) {
                            return;
                        }
                        throw new InvalidParameterException("positionStart < mPlaceholderSize");
                    }
                    positionStart -= mPlaceholderSize;
                    mAdapter.unregisterAdapterDataObserver(_innerAdapterObserver);
                    mAdapter.notifyItemRangeInserted(positionStart, itemCount);
                    mAdapter.registerAdapterDataObserver(_innerAdapterObserver);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    if (itemCount != 1) {
                        throw new UnsupportedOperationException("onItemRangeMoved, itemCount != 1");
                    }
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                    if (fromPosition < mPlaceholderSize) {
                        if (itemCount - (mPlaceholderSize - fromPosition) <= 0) {
                            return;
                        }
                        throw new InvalidParameterException("fromPosition < mPlaceholderSize");
                    }
                    if (toPosition < mPlaceholderSize) {
                        throw new InvalidParameterException("toPosition < mPlaceholderSize");
                    }
                    fromPosition -= mPlaceholderSize;
                    toPosition -= mPlaceholderSize;
                    mAdapter.unregisterAdapterDataObserver(_innerAdapterObserver);
                    mAdapter.notifyItemMoved(fromPosition, toPosition);
                    mAdapter.registerAdapterDataObserver(_innerAdapterObserver);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    if (positionStart < mPlaceholderSize) {
                        if (itemCount - (mPlaceholderSize - positionStart) <= 0) {
                            return;
                        }
                        throw new InvalidParameterException("positionStart < mPlaceholderSize");
                    }
                    positionStart -= mPlaceholderSize;
                    mAdapter.unregisterAdapterDataObserver(_innerAdapterObserver);
                    mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
                    mAdapter.registerAdapterDataObserver(_innerAdapterObserver);
                }
            };

            _innerAdapterObserver = new RecyclerView.AdapterDataObserver() {

                @Override
                public void onChanged() {
                    super.onChanged();
                    unregisterAdapterDataObserver(_adapterObserver);
                    notifyDataSetChanged();
                    registerAdapterDataObserver(_adapterObserver);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    super.onItemRangeChanged(positionStart, itemCount);
                    positionStart += mPlaceholderSize;
                    unregisterAdapterDataObserver(_adapterObserver);
                    notifyItemRangeChanged(positionStart, itemCount);
                    registerAdapterDataObserver(_adapterObserver);
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                    super.onItemRangeChanged(positionStart, itemCount, payload);
                    positionStart += mPlaceholderSize;
                    unregisterAdapterDataObserver(_adapterObserver);
                    notifyItemRangeChanged(positionStart, itemCount, payload);
                    registerAdapterDataObserver(_adapterObserver);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    positionStart += mPlaceholderSize;
                    unregisterAdapterDataObserver(_adapterObserver);
                    notifyItemRangeInserted(positionStart, itemCount);
                    registerAdapterDataObserver(_adapterObserver);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    if (itemCount != 1) {
                        throw new UnsupportedOperationException("onItemRangeMoved, itemCount != 1");
                    }
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                    fromPosition += mPlaceholderSize;
                    toPosition += mPlaceholderSize;
                    unregisterAdapterDataObserver(_adapterObserver);
                    notifyItemMoved(fromPosition, toPosition);
                    registerAdapterDataObserver(_adapterObserver);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    positionStart += mPlaceholderSize;
                    unregisterAdapterDataObserver(_adapterObserver);
                    notifyItemRangeRemoved(positionStart, itemCount);
                    registerAdapterDataObserver(_adapterObserver);
                }
            };

            registerAdapterDataObserver(_adapterObserver);
            mAdapter.registerAdapterDataObserver(_innerAdapterObserver);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mPlaceholderSize)
            return TYPE_PLACEHOLDER;
        else
            return mAdapter.getItemViewType(position - mPlaceholderSize); //call getItemViewType on the adapter, less mPlaceholderSize
    }

    //dispatch getItemCount to the actual adapter, add mPlaceholderSize
    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mPlaceholderSize;
    }

    //add the header on first position, else display the true adapter's cells
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_PLACEHOLDER:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.material_view_pager_placeholder, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            default:
                return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    //dispatch onBindViewHolder on the actual mAdapter
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_PLACEHOLDER:
                break;
            default:
                mAdapter.onBindViewHolder(holder, position - mPlaceholderSize);
                break;
        }
    }
}