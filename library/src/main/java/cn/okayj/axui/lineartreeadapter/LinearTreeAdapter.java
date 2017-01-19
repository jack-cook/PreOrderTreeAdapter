package cn.okayj.axui.lineartreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import cn.okayj.axui.viewholder.ViewHolder;
import cn.okayj.util.lineartree.DataNode;
import cn.okayj.util.lineartree.NodeFlatIndex;

/**
 * Created by jack on 2017/1/11.
 */
public abstract class LinearTreeAdapter<N,VH extends ViewHolder> implements LinearDataSource<VH> {
    private AdapterBridge<VH> realAdapter;

    private DataNode<N> rootNode;

    private NodeFlatIndex nodeFlatIndex;

    private NodeFlatIndex.VisibleFlatIndex visibleFlatIndex;

    private N source;

    protected abstract N root();

    protected boolean ignoreRoot(){
        return true;
    }

    protected abstract int getChildSize(N n);

    protected abstract N getChild(N parent, int childIndex);

    protected abstract int getViewTypeCount();

    protected abstract int getViewType(N n);

    protected abstract VH onCreateViewHolder(ViewGroup viewGroup, int viewType);

    protected abstract void onBindViewHolder(VH viewHolder, N data);

    protected abstract int getItemId(N data);

    public ListAdapter asListAdapter() {
        ListViewAdapter listAdapter = new ListViewAdapter<>(this);
        realAdapter = listAdapter;
        buildModel();
        return listAdapter;
    }

    /*public RecyclerView.Adapter asRecyclerAdapter() {

    }*/

    public boolean notifyChildAdd(N parent, N child, int positionInParent){
        DataNode node = findNode(parent);
        if(node == null)
            return false;

        if(node == rootNode){
            DataNode newOne = buildSubTree(child);
            rootNode.addChildNode(positionInParent,newOne);
        }else {
            DataNode newOne = buildSubTree(child);
            node.addChildNode(positionInParent,newOne);
        }
        realAdapter.notifyDataSetChanged();
        return true;
    }

    public boolean notifyChildRemove(N child){
        DataNode node = findNode(child);
        if(node == null)
            return false;

        if(node == rootNode)
            clearData();
        else {
            node.removeFromParent();
        }
        realAdapter.notifyDataSetChanged();

        return true;
    }

    public void notifyDataSetChange(){
        buildModel();
        realAdapter.notifyDataSetChanged();
    }

    /*public void notifyDataSetChange(N subTree){
        DataNode node = findNode(subTree);

        if(node == null)
            return;

        if(node == rootNode){
            notifyDataSetChange();
            return;
        }else {
            DataNode parent = node.getParentNode();
            parent.

        }


    }*/



    @Override
    public int count(){
        return visibleFlatIndex.size();
    }

    @Override
    public Object item(int position){
        return visibleFlatIndex.get(position).getSource();
    }

    @Override
    public int itemId(int position){
        return getItemId((N)visibleFlatIndex.get(position).getSource());
    }

    @Override
    public int viewType(int position) {
        return getViewType((N)visibleFlatIndex.get(position).getSource());
    }

    @Override
    public int viewTypeCount() {
        return getViewTypeCount();
    }

    @Override
    public VH createViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent, viewType);
    }

    @Override
    public void bindViewHolder(VH viewHolder, int position) {
        N data = (N)visibleFlatIndex.get(position);
        onBindViewHolder(viewHolder,data);
    }

    private DataNode<N> buildSubTree(N data){
        DataNode node = new DataNode();
        node.setSource(data);
        int childSize = getChildSize(data);
        for (int i = 0; i < childSize; ++i){
            buildSubTree(getChild(data,i));
        }

        return node;
    }

    private void clearData(){
        rootNode = null;
        source = null;
        nodeFlatIndex = null;
        visibleFlatIndex = null;
    }

    /**
     * build or rebuild
     */
    private void buildModel(){
        clearData();
        source = root();
        if(source != null){
            rootNode = buildSubTree(source);
            nodeFlatIndex = rootNode.getFlatIndex();
            visibleFlatIndex = nodeFlatIndex.getVisibleIndex();
        }
    }

    private DataNode<N> findNode(N data){
        if(source == data){
            return rootNode;
        }else {
            for (int i = 0; i < nodeFlatIndex.size(); ++i){
                DataNode node = nodeFlatIndex.get(i);
                if(node.getSource() == data) {
                    return node;
                }
            }
        }

        return null;
    }

}
