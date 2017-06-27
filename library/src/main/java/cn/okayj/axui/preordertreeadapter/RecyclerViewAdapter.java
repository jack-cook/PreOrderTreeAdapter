package cn.okayj.axui.preordertreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by jack on 2017/2/4.
 */

public class RecyclerViewAdapter<VH extends PreOrderTreeAdapter.ViewHolder> extends RecyclerView.Adapter implements AdapterBridge<VH> {
    private LinearDataSource<VH> source;

    public RecyclerViewAdapter(LinearDataSource<VH> source) {
        this.source = source;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new RecyclerViewHolder(source.createViewHolder(parent,viewType));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        source.bindViewHolder((VH) ((RecyclerViewHolder) holder).asNormalViewHolder(),position);
    }

    @Override
    public int getItemCount() {
        return source.count();
    }

    @Override
    public long getItemId(int position) {
        return source.itemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return source.viewType(position);
    }

    public static class RecyclerViewHolder extends cn.okayj.axui.recyclerview.RecyclerViewHolder {
        public RecyclerViewHolder(PreOrderTreeAdapter.ViewHolder viewHolder) {
            super(viewHolder);
            viewHolder.setRecyclerViewHolder(this);
        }
    }
}
