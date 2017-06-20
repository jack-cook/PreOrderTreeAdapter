package cn.okayj.axui.preordertreeadapter;

import cn.okayj.axui.viewholder.ViewHolder;

/**
 * Created by jack on 2017/1/12.
 */

interface AdapterBridge<VH extends ViewHolder> {
    void notifyDataSetChanged();

    void notifyItemRangeInserted(int positionStart, int itemCount);

    void notifyItemRangeRemoved(int positionStart, int itemCount);
}
