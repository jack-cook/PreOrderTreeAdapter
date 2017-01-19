package cn.okayj.axui.lineartreeadapter;

import android.view.ViewGroup;

import cn.okayj.axui.viewholder.ViewHolder;

/**
 * Created by jack on 2017/1/12.
 */

interface AdapterBridge<VH extends ViewHolder> {

    /*int createViewHolder(ViewGroup viewGroup, int viewType);

    void onBindViewHolder(VH viewHolder, int position);

    int viewTypeCount();

    int viewType(int position);*/

    void notifyDataSetChanged();
}
