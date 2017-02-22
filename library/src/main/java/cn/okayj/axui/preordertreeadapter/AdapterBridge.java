package cn.okayj.axui.preordertreeadapter;

import android.view.ViewGroup;

import cn.okayj.axui.viewholder.ViewHolder;

/**
 * Created by jack on 2017/1/12.
 */

interface AdapterBridge<VH extends ViewHolder> {
    void notifyDataSetChanged();
}
