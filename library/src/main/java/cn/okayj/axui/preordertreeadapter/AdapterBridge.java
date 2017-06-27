package cn.okayj.axui.preordertreeadapter;

/**
 * Created by jack on 2017/1/12.
 */

interface AdapterBridge<VH extends PreOrderTreeAdapter.ViewHolder> {
    void notifyDataSetChanged();

    void notifyItemRangeInserted(int positionStart, int itemCount);

    void notifyItemRangeRemoved(int positionStart, int itemCount);
}
