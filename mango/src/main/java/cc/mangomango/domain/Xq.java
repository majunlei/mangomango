package cc.mangomango.domain;

import java.util.List;

/**
 * Created by majunlei on 2017/10/29.
 */
public class Xq {

    private long id;

    private String xq;

    private String author;

    private int stamp;

    private int view;

    private int like;

    private int ctime;

    private String timeDesc;

    private List<XqComment> comments;

    private List<PicUrl> urls;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getStamp() {
        return stamp;
    }

    public void setStamp(int stamp) {
        this.stamp = stamp;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public List<XqComment> getComments() {
        return comments;
    }

    public void setComments(List<XqComment> comments) {
        this.comments = comments;
    }

    public List<PicUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<PicUrl> urls) {
        this.urls = urls;
    }
}
