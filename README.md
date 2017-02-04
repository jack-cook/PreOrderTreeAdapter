# PreOrderTreeAdapter
An adapter which can be used for ListView or RecyclerView. It traverse data in pre-order.

一个可以先序遍历数据的 adapter， 可以用于ListView和RecyclerView， 也可以自己扩展。

## Demo
[Book A][1]

## Usage
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

[1]: https://github.com/jack-cook/PreOrderTreeAdapter/blob/master/desplay/device-2017-01-23-021752.png