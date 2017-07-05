package com.toong.recyclerwithloadmore.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.toong.recyclerwithloadmore.R;

/**
 * Created by PhanVanLinh on 05/07/2017.
 * phanvanlinh.94vn@gmail.com
 *
 * Any class extend this class
 * + should implement getCustomItemViewType
 * + should call super.onBindViewHolder(holder, position); inside onBindViewHolder
 */

public abstract class LoadMoreRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<T> {
    private static final int TYPE_PROGRESS = 0xFFFF;
    private RetryLoadMoreListener mRetryLoadMoreListener;
    private boolean mOnLoadMoreFailed;
    private boolean mIsReachEnd;

    protected LoadMoreRecyclerViewAdapter(@NonNull Context context,
            ItemClickListener itemClickListener,
            @NonNull RetryLoadMoreListener retryLoadMoreListener) {
        super(context, itemClickListener);
        mRetryLoadMoreListener = retryLoadMoreListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PROGRESS:
                View view = mInflater.inflate(R.layout.item_recyclerview_bottom, parent, false);
                return new BottomViewHolder(view, mRetryLoadMoreListener);
        }
        throw new RuntimeException("LoadMoreRecyclerViewAdapter: ViewHolder = null");
    }

    @Override
    public int getItemViewType(int position) {
        if (position == bottomItemPosition()) {
            return TYPE_PROGRESS;
        }
        return getCustomItemViewType(position);
    }

    private int bottomItemPosition() {
        return getItemCount() - 1;
    }

    protected abstract int getCustomItemViewType(int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder) holder).mTvNoMoreItem.setVisibility(
                    mIsReachEnd ? View.VISIBLE : View.GONE);

            ((BottomViewHolder) holder).mProgressBar.setVisibility(
                    mIsReachEnd ? View.GONE : mOnLoadMoreFailed ? View.GONE : View.VISIBLE);
            ((BottomViewHolder) holder).layoutRetry.setVisibility(
                    mIsReachEnd ? View.GONE : mOnLoadMoreFailed ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 1; // +1 for progress
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private TextView mTvNoMoreItem;
        private Button mBtnRetry;
        private View layoutRetry;
        private RetryLoadMoreListener mRetryLoadMoreListener;

        BottomViewHolder(View itemView, RetryLoadMoreListener retryLoadMoreListener) {
            super(itemView);
            mRetryLoadMoreListener = retryLoadMoreListener;
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            layoutRetry = itemView.findViewById(R.id.layout_retry);
            mBtnRetry = (Button) itemView.findViewById(R.id.button_retry);
            mTvNoMoreItem = (TextView) itemView.findViewById(R.id.text_no_more_item);

            layoutRetry.setVisibility(View.GONE); // gone layout retry as default
            mTvNoMoreItem.setVisibility(View.GONE); // gone text view no more item as default

            mBtnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRetryLoadMoreListener.onRetryLoadMore();
                }
            });
        }
    }

    /**
     * It help visible progress when load more
     */
    public void startLoadMore() {
        mOnLoadMoreFailed = false;
        notifyDataSetChanged();
    }

    /**
     * It help visible layout retry when load more failed
     */
    public void onLoadMoreFailed() {
        mOnLoadMoreFailed = true;
        notifyItemChanged(bottomItemPosition());
    }

    public void onReachEnd() {
        mIsReachEnd = true;
        notifyItemChanged(bottomItemPosition());
    }

    public interface RetryLoadMoreListener {
        void onRetryLoadMore();
    }
}
