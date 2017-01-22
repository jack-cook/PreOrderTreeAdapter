package cn.okayj.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import cn.okayj.axui.lineartreeadapter.LinearTreeAdapter;

/**
 * Created by jack on 2017/1/23.
 */

public class LinearTreeAdapterDemo extends Activity {
    private Content book;

    private ListView listView;

    {
        book = new Content("BOOK A");

        Content part1 = new Content("Part I");
            Content chapter1 = new Content("Chapter 1");
                chapter1.addSubContent(new Content("some topic 1.1"));
                chapter1.addSubContent(new Content("some topic 1.2"));
                chapter1.addSubContent(new Content("some topic 1.3"));
                chapter1.addSubContent(new Content("some topic 1.4"));
                chapter1.addSubContent(new Content("some topic 1.5"));
            Content chapter2 = new Content("Chapter 2");
                chapter2.addSubContent(new Content("some topic 2.1"));
                chapter2.addSubContent(new Content("some topic 2.2"));
                chapter2.addSubContent(new Content("some topic 2.3"));
            Content chapter3 = new Content("Chapter 3");
                chapter3.addSubContent(new Content("some topic 3.1"));
                chapter3.addSubContent(new Content("some topic 3.2"));

            part1.addSubContent(chapter1);
            part1.addSubContent(chapter2);
            part1.addSubContent(chapter3);

        Content part2 = new Content("Part II");
            Content chapter4 = new Content("Chapter 4");
                chapter4.addSubContent(new Content("some topic 4.1"));
                chapter4.addSubContent(new Content("some topic 4.2"));
                chapter4.addSubContent(new Content("some topic 4.3"));
                chapter4.addSubContent(new Content("some topic 4.4"));
            Content chapter5 = new Content("Chapter 5");
                chapter5.addSubContent(new Content("some topic 5.1"));
                chapter5.addSubContent(new Content("some topic 5.2"));
                chapter5.addSubContent(new Content("some topic 5.3"));
                chapter5.addSubContent(new Content("some topic 5.4"));
                chapter5.addSubContent(new Content("some topic 5.5"));

            part2.addSubContent(chapter4);
            part2.addSubContent(chapter5);

        book.addSubContent(part1);
        book.addSubContent(part2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_tree);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new TreeAdapter().asListAdapter());
    }

    class TreeAdapter extends LinearTreeAdapter<Content,ViewHolder> {
        private final int VIEW_TYPE_BOOK = 0;
        private final int VIEW_TYPE_PART = 1;
        private final int VIEW_TYPE_CHAPTER = 2;
        private final int VIEW_TYPE_CONTENT = 3;

        LayoutInflater layoutInflater = LayoutInflater.from(LinearTreeAdapterDemo.this);

        @Override
        protected Content root() {
            return book;
        }

        @Override
        protected boolean ignoreRoot() {
            return false;
        }

        @Override
        protected int getChildSize(Content content) {
            return content.subContentSize();
        }

        @Override
        protected Content getChild(Content parent, int childPosition) {
            return parent.getSubContent(childPosition);
        }

        @Override
        protected int getViewTypeCount() {
            return 4;
        }

        @Override
        protected int getViewType(Content content, int depth) {
            switch (depth){
                case 1:
                    return VIEW_TYPE_BOOK;
                case 2:
                    return VIEW_TYPE_PART;
                case 3:
                    return VIEW_TYPE_CHAPTER;
                case 4:
                    return VIEW_TYPE_CONTENT;
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        protected ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = null;
            switch (viewType){
                case VIEW_TYPE_BOOK:
                    view = layoutInflater.inflate(R.layout.item_book,viewGroup,false);
                    break;
                case VIEW_TYPE_PART:
                    view = layoutInflater.inflate(R.layout.item_part,viewGroup,false);
                    break;
                case VIEW_TYPE_CHAPTER:
                    view = layoutInflater.inflate(R.layout.item_chapter,viewGroup,false);
                    break;
                case VIEW_TYPE_CONTENT:
                    view = layoutInflater.inflate(R.layout.item_content,viewGroup,false);
                    break;
            }

            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.bindView();
            return viewHolder;
        }

        @Override
        protected void onBindViewHolder(ViewHolder viewHolder, Content data) {
            viewHolder.setItem(data);
        }

        @Override
        protected int getItemId(Content data) {
            return 0;
        }
    }

    class ViewHolder extends cn.okayj.axui.viewholder.ViewHolder<Content> {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onSetItem(Content item) {
            super.onSetItem(item);
            textView.setText(item.getContent());
        }

        @Override
        protected void onBindView(View itemView) {
            super.onBindView(itemView);
            textView = (TextView) itemView;
        }
    }
}
