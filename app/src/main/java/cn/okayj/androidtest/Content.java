package cn.okayj.androidtest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 2017/1/23.
 */

public class Content {
    private String content = "";

    private List<Content> subContentList = new ArrayList<>();

    public Content(String content) {
        if (content == null)
            this.content = "";
        else
            this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void addSubContent(Content subContent){
        subContentList.add(subContent);
    }

    public int subContentSize(){
        return subContentList.size();
    }

    public Content getSubContent(int position){
        return subContentList.get(position);
    }
}
