# PreOrderTreeAdapter
An adapter which can be used for ListView or RecyclerView. It traverses data in pre-order,than builds index internal.
It can be used to implement second-level list like ExpandableListView, but not limited to hierarchy depth.

高效先序遍历嵌套的数据的Adapter(内部构建索引)，
配合ListView和RecyclerView可方便展示嵌套的数据（不用特地编写相应的数据结构去配合ListView和RecyclerView）.
可以用其实现伪二级列表（类似ExpandableListView，但层级数不限）。

## Demo
<p>
   <img src="https://github.com/jack-cook/PreOrderTreeAdapter/blob/master/display/device-2017-01-23-021752.png" width="320" />
</p>

## Usage

#### dependency

**Gradle**：
compile 'cn.okayj:preorder-tree-adapter:1.3.0'


#### code

    class MyAdapter extends PreOrderTreeAdapter<Object,ViewHolder> {

        @Override
        protected Object root() {
            return someObject;// return the root of data
        }

        @Override
        protected int getChildSize(Object someNode) {
            // return the size of children of someNode
        }

        @Override
        protected Object getChild(Object parent, int childPosition) {
            // return the child node of parent
        }



        @Override
        protected int getViewTypeCount() {
            //..
        }

        @Override
        protected int getViewType(Content content, int depth) {
            //..
        }

        @Override
        protected ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //..
        }

        @Override
        protected void onBindViewHolder(ViewHolder viewHolder, Content data) {
            //..
        }

    }

    listAdapter = new MyAdapter().asListAdapter();
    // or
    // listAdapter = new ListViewAdapter(new MyAdapter().buildModel()); // you can extend ListViewAdapter to add your feature for ListView

    listView.setAdapter(listAdapter);


## License
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.