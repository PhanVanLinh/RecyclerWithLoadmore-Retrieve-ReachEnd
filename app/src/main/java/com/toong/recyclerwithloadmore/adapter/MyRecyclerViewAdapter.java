package com.toong.recyclerwithloadmore.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.toong.recyclerwithloadmore.R;
import com.toong.recyclerwithloadmore.base.LoadMoreRecyclerViewAdapter;
import com.toong.recyclerwithloadmore.model.Item;

public class MyRecyclerViewAdapter extends LoadMoreRecyclerViewAdapter<Item> {
    private static final int TYPE_ITEM = 1;

    public MyRecyclerViewAdapter(Context context, ItemClickListener itemClickListener,
            RetryLoadMoreListener retryLoadMoreListener) {
        super(context, itemClickListener, retryLoadMoreListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
            return new ItemViewHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Item item = mDataList.get(position);
            ((ItemViewHolder) holder).myTextView.setText(item.getTitle());
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected int getCustomItemViewType(int position) {
        return TYPE_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myTextView;

        ItemViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}