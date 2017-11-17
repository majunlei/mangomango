package cc.mangomango.domain;

/**
 * Created by majunlei on 2017/10/29.
 */
public class XqComment {

    private long xqId;

    private String comment;

    private int ctime;

    public long getXqId() {
        return xqId;
    }

    public void setXqId(long xqId) {
        this.xqId = xqId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

}
