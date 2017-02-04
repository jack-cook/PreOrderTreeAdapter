package cn.okayj.axui.preordertreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import cn.okayj.axui.viewholder.ViewHolder;
import cn.okayj.util.lineartree.DataNode;
import cn.okayj.util.lineartree.NodeFlatIndex;

/**
 * Created by jack on 2017/1/11.
 */

/**
 * @param <N>
 * @param <VH>
 */
public abstract class PreOrderTreeAdapter<N,VH extends ViewHolder> implements LinearDataSource<VH> {
    private AdapterBridge<VH> realAdapter;

    private DataNode<N> rootNode;

    private NodeFlatIndex nodeFlatIndex;

    private N source;

    /**
     * the root object is ignored in aspect of view
     * unless {@link #root()} return false
     * @return
     */
    protected abstract N root();

    /**
     * whether root object is ignored in aspect of view
     * @return
     */
    protected boolean ignoreRoot(){
        return true;
    }

    /**
     * child size of node n
     * @param n
     * @return
     */
    protected abstract int getChildSize(N n);

    /**
     * retrieve a child from a node
     * @param parent witch node to retrieve child
     * @param childPosition position in parent
     * @return
     */
    protected abstract N getChild(N parent, int childPosition);

    protected abstract int getViewTypeCount();

    /**
     *
     * @param n
     * @param depth node depth in the tree , begin from 1 (root node)
     * @return
     */
    protected abstract int getViewType(N n, int depth);

    protected abstract VH onCreateViewHolder(ViewGroup viewGroup, int viewType);

    protected abstract void onBindViewHolder(VH viewHolder, N data);

    protected int getItemId(N data){
        return 0;
    }

    public ListAdapter asListAdapter() {
        if(realAdapter != null && realAdapter instanceof ListAdapter)
            return (ListAdapter) realAdapter;

        ListViewAdapter listAdapter = new ListViewAdapter<>(this);
        realAdapter = listAdapter;
        buildModel();
        return listAdapter;
    }

    public RecyclerView.Adapter asRecyclerAdapter() {
        if(realAdapter != null && realAdapter instanceof RecyclerView.Adapter)
            return (RecyclerView.Adapter) realAdapter;

        RecyclerViewAdapter adapter = new RecyclerViewAdapter<>(this);
        realAdapter = adapter;
        buildModel();
        return adapter;
    }

    public boolean notifyChildAdded(N parent, N child, int positionInParent){
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

    public boolean notifyChildRemoved(N child){
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

    /**
     * see also {@link #notifyDataSetChanged(Object)}
     */
    public void notifyDataSetChanged(){
        buildModel();
        realAdapter.notifyDataSetChanged();
    }

    /**
     * more efficient than {@link #notifyDataSetChanged()}
     * @param subTree
     */
    public void notifyDataSetChanged(N subTree){
        DataNode node = findNode(subTree);

        if(node == null)
            return;

        if(node == rootNode){
            notifyDataSetChanged();
            return;
        }else {
            DataNode parent = node.getParentNode();
            int position = parent.removeChildNode(node);
            assert position >= 0;
            parent.addChildNode(position,buildSubTree(subTree));
        }

        realAdapter.notifyDataSetChanged();

    }



    @Override
    public int count(){
        if(nodeFlatIndex == null)//root() 为 null 会出现这种情况
            return 0;
        return nodeFlatIndex.size();
    }

    @Override
    public Object item(int position){
        return nodeFlatIndex.get(position).getSource();
    }

    @Override
    public int itemId(int position){
        return getItemId((N) nodeFlatIndex.get(position).getSource());
    }

    @Override
    public int viewType(int position) {
        DataNode<N> node = (DataNode<N>) nodeFlatIndex.get(position);
        int depth = 1;//todo 动态求深度是否低效？
        DataNode ancestor = node;
        while ((ancestor = ancestor.getParentNode()) != null){
            depth ++;
        }

        return getViewType(node.getSource(),depth);
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
        DataNode<N> dataNode= (DataNode<N>) nodeFlatIndex.get(position);
        onBindViewHolder(viewHolder,dataNode.getSource());
    }

    private DataNode<N> buildSubTree(N data){
        DataNode node = new DataNode();
        node.setSource(data);
        int childSize = getChildSize(data);
        for (int i = 0; i < childSize; ++i){
            node.addChildNode(buildSubTree(getChild(data,i)));
        }

        return node;
    }

    private void clearData(){
        rootNode = null;
        source = null;
        nodeFlatIndex = null;
        nodeFlatIndex = null;
    }

    /**
     * build or rebuild the internal tree model
     */
    public final PreOrderTreeAdapter buildModel(){
        clearData();
        source = root();
        if(source != null){
            rootNode = buildSubTree(source);
            nodeFlatIndex = rootNode.getFlatIndex();
            if(ignoreRoot())
                nodeFlatIndex.ignoreRoot(true);
        }

        return this;
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
