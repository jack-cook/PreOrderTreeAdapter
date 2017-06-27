package cn.okayj.axui.preordertreeadapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * Created by jack on 2017/1/12.
 */

class ListViewAdapter<VH extends PreOrderTreeAdapter.ViewHolder> extends BaseAdapter implements AdapterBridge<VH> {
    private LinearDataSource<VH> source;

    ListViewAdapter(LinearDataSource<VH> source) {
        this.source = source;
    }

    @Override
    public int getCount() {
        return source.count();
    }

    @Override
    public Object getItem(int position) {
        return source.item(position);
    }

    @Override
    public long getItemId(int position) {
        return source.itemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            vh = source.createViewHolder(parent, getItemViewType(position));//todo 以此获取viewtype是否低效？
        } else {
            vh = (VH) PreOrderTreeAdapter.ViewHolder.getHolder(convertView);
        }

        source.bindViewHolder(vh, position);

        return vh.itemView;
    }

    @Override
    public int getItemViewType(int position) {
        return source.viewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return source.viewTypeCount();
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        notifyDataSetChanged();
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        notifyDataSetChanged();
    }
}
