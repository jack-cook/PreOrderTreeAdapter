package cn.okayj.axui.preordertreeadapter;

import android.view.ViewGroup;

import cn.okayj.axui.viewholder.ViewHolder;

/**
 * Created by jack on 2017/1/12.
 */

public interface LinearDataSource<VH extends ViewHolder> {
    int count();
    Object item(int position);
    int itemId(int position);
    int viewType(int position);
    int viewTypeCount();
    VH createViewHolder(ViewGroup parent, int viewType);
    void bindViewHolder(VH viewHolder, int position);
}
