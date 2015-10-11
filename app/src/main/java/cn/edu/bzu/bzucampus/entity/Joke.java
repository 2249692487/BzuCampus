package cn.edu.bzu.bzucampus.entity;

/**
 * 笑话实体类
 * Created by monster on 2015/10/11.
 */
public class Joke {
    private String ct ; //时间
    private String text; //内容
    private String title; //标题
    private String type; //类型

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public Joke(String ct, String text, String title, String type) {
        this.ct = ct;
        this.text = text;
        this.title = title;
        this.type = type;
    }

    public  Joke(){
        super();
    }
}
