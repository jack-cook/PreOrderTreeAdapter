package cn.okayj.axui.preordertreeadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import cn.okayj.util.lineartree.DataNode;
import cn.okayj.util.lineartree.NodeFlatIndex;

/**
 * Created by jack on 2017/1/11.
 */

/**
 * @param <N>
 * @param <VH>
 */
public abstract class PreOrderTreeAdapter<N, VH extends PreOrderTreeAdapter.ViewHolder> implements LinearDataSource<VH> {
    private AdapterBridge<VH> realAdapter;

    private DataNode<N> rootNode;

    private NodeFlatIndex nodeFlatIndex;

    private N source;

    private boolean inited = false;

    /**
     * the root object is ignored in aspect of view
     * unless {@link #root()} return false
     *
     * @return
     */
    protected abstract N root();

    /**
     * whether root object is ignored in aspect of view
     *
     * @return
     */
    protected boolean ignoreRoot() {
        return true;
    }

    /**
     * child size of node n
     *
     * @param n
     * @return
     */
    protected abstract int getChildSize(N n);

    /**
     * retrieve a child from a node
     *
     * @param parent        witch node to retrieve child
     * @param childPosition position in parent
     * @return
     */
    protected abstract N getChild(N parent, int childPosition);

    protected abstract int getViewTypeCount();

    /**
     * @param n
     * @param depth node depth in the tree , begin from 1 (root node)
     * @return
     */
    protected abstract int getViewType(N n, int depth);

    protected abstract VH onCreateViewHolder(ViewGroup viewGroup, int viewType);

    protected abstract void onBindViewHolder(VH viewHolder, N data);

    protected int getItemId(N data) {
        return 0;
    }

    public ListAdapter asListAdapter() {
        if (realAdapter != null && realAdapter instanceof ListAdapter)
            return (ListAdapter) realAdapter;

        ListViewAdapter listAdapter = new ListViewAdapter<>(this);
        realAdapter = listAdapter;
        buildModel(false);
        return listAdapter;
    }

    public RecyclerView.Adapter asRecyclerAdapter() {
        if (realAdapter != null && realAdapter instanceof RecyclerView.Adapter)
            return (RecyclerView.Adapter) realAdapter;

        RecyclerViewAdapter adapter = new RecyclerViewAdapter<>(this);
        realAdapter = adapter;
        buildModel(false);
        return adapter;
    }

    public boolean notifyChildAdded(N parent, N child, int positionInParent) {
        DataNode node = findNode(parent);
        if (node == null)
            return false;

        DataNode newNode;
        newNode = buildSubTree(child);
        node.addChildNode(positionInParent, newNode);

        int startPosition = nodeFlatIndex.indexOf(newNode);
        int count = newNode.getFlatSize();
        realAdapter.notifyItemRangeInserted(startPosition, count);
        return true;
    }

    /**
     * notify child added to last position of parent
     *
     * @param parent
     * @param child
     * @return
     */
    public boolean notifyChildAdded(N parent, N child) {
        DataNode node = findNode(parent);
        if (node == null)
            return false;

        DataNode newNode = buildSubTree(child);
        node.addChildNode(newNode);
        int flatStartPosition = nodeFlatIndex.indexOf(newNode);
        realAdapter.notifyItemRangeInserted(flatStartPosition, newNode.getFlatSize());
        return true;
    }

    public boolean notifyChildRemoved(N child) {
        DataNode node = findNode(child);
        if (node == null)
            return false;

        int startPosition;
        int count;
        if (node == rootNode) {
            startPosition = 0;
            count = nodeFlatIndex.size();
            clearData();
        } else {
            startPosition = flatIndexOf(child);
            count = node.getFlatSize();
            node.removeFromParent();
        }

        if (count > 0) {
            realAdapter.notifyItemRangeRemoved(startPosition, count);
        } else {
            // not changed
        }

        return true;
    }

    /**
     * see also {@link #notifyDataSetChanged(Object)}
     */
    public void notifyDataSetChanged() {
        buildModel(true);
        realAdapter.notifyDataSetChanged();
    }

    /**
     * more efficient than {@link #notifyDataSetChanged()}
     *
     * @param subTree
     */
    public void notifyDataSetChanged(N subTree) {
        DataNode node = findNode(subTree);

        if (node == null)
            return;

        if (node == rootNode) {
            notifyDataSetChanged();
            return;
        } else {
            DataNode parent = node.getParentNode();
            int position = parent.removeChildNode(node);
            assert position >= 0;
            parent.addChildNode(position, buildSubTree(subTree));
        }

        realAdapter.notifyDataSetChanged();

    }

    /**
     * unlike {@link #notifyDataSetChanged()}, this method do not rebuild the internal tree model.
     * if your data changes properties but not the structure of tree, use this method.
     */
    public void notifyDataStateChanged() {
        buildModel(false);
        realAdapter.notifyDataSetChanged();
    }


    @Override
    public int count() {
        if (nodeFlatIndex == null)//root() 为 null 会出现这种情况
            return 0;
        return nodeFlatIndex.size();
    }

    @Override
    public Object item(int position) {
        return nodeFlatIndex.get(position).getSource();
    }

    @Override
    public int itemId(int position) {
        return getItemId((N) nodeFlatIndex.get(position).getSource());
    }

    @Override
    public int viewType(int position) {
        DataNode<N> node = (DataNode<N>) nodeFlatIndex.get(position);
        int depth = 1;//todo 动态求深度是否低效？
        DataNode ancestor = node;
        while ((ancestor = ancestor.getParentNode()) != null) {
            depth++;
        }

        return getViewType(node.getSource(), depth);
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
        DataNode<N> dataNode = (DataNode<N>) nodeFlatIndex.get(position);
        viewHolder.setNode(dataNode);
        onBindViewHolder(viewHolder, dataNode.getSource());
    }

    private DataNode<N> buildSubTree(N data) {
        DataNode node = new DataNode();
        node.setSource(data);
        int childSize = getChildSize(data);
        for (int i = 0; i < childSize; ++i) {
            node.addChildNode(buildSubTree(getChild(data, i)));
        }

        return node;
    }

    private void clearData() {
        rootNode = null;
        source = null;
        nodeFlatIndex = null;
        nodeFlatIndex = null;
    }

    /**
     * build or rebuild the internal tree model
     */
    public final PreOrderTreeAdapter buildModel() {
        return buildModel(true);
    }

    private PreOrderTreeAdapter buildModel(boolean forceRebuld) {
        if (forceRebuld || !inited) {
            clearData();
            source = root();
            if (source != null) {
                rootNode = buildSubTree(source);
                nodeFlatIndex = rootNode.getFlatIndex();
                if (ignoreRoot())
                    nodeFlatIndex.ignoreRoot(true);
            }

            inited = true;
        }

        return this;
    }

    private DataNode<N> findNode(N data) {
        if (source == data) {
            return rootNode;
        } else {
            for (int i = 0; i < nodeFlatIndex.size(); ++i) {
                DataNode node = nodeFlatIndex.get(i);
                if (node.getSource() == data) {
                    return node;
                }
            }
        }

        return null;
    }

    /**
     * @param data
     * @return -1 if not found
     */
    public int flatIndexOf(N data) {
        for (int i = 0; i < nodeFlatIndex.size(); ++i) {
            DataNode node = nodeFlatIndex.get(i);
            if (node.getSource() == data) {
                return i;
            }
        }

        return -1;
    }

    public N getParent(N data) {
        DataNode node = findNode(data);
        if (node != null) {
            DataNode parentNode = node.getParentNode();
            if (parentNode != null) {
                return (N) parentNode.getSource();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static class ViewHolder<I> extends cn.okayj.axui.viewholder.ViewHolder<I> {
        private RecyclerViewAdapter.RecyclerViewHolder recyclerViewHolder;

        private DataNode<I> node;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        void setRecyclerViewHolder(RecyclerViewAdapter.RecyclerViewHolder innerViewHolder) {
            this.recyclerViewHolder = innerViewHolder;
        }

        void setNode(DataNode<I> node) {
            this.node = node;
        }

        public RecyclerViewAdapter.RecyclerViewHolder asRecyclerViewHolder() {
            if (recyclerViewHolder == null)
                throw new IllegalStateException("this view holder is not bind to view holder of RecyclerView");

            return recyclerViewHolder;
        }
    }

}
