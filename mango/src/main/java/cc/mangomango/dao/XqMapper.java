package cc.mangomango.dao;

import cc.mangomango.domain.PicUrl;
import cc.mangomango.domain.Xq;
import cc.mangomango.domain.XqComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by majunlei on 2017/10/29.
 */
public interface XqMapper {

    @Select("select count(*) from xq")
    int getCount();

    @Select("select id,xq,author,stamp,view,`like`,ctime from xq order by id desc limit #{offset},#{limits}")
    List<Xq> getByPage(@Param("offset") int offset, @Param("limits") int limits);

    @Select("select url,thumb_url from xq_pic where stamp=#{stamp}")
    List<PicUrl> getUrls(@Param("stamp") int stamp);

    @Select("select xq_id,`comment`,ctime from xq_comment where xq_id=#{id}")
    List<XqComment> getComments(@Param("id") long id);

    @Insert("insert into xq(xq,author,stamp,view,`like`,ctime,utime) values (#{xq},#{author},#{stamp},#{view},#{like},unix_timestamp(),unix_timestamp())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(Xq Xq);

    @Insert("insert into xq_pic(stamp,url,thumb_url,ctime,utime) values (#{stamp},#{url},#{thumbUrl},unix_timestamp(),unix_timestamp())")
    int savePic(@Param("stamp") int stamp, @Param("url") String url, @Param("thumbUrl") String thumbUrl);

    @Update("update xq set `like`=`like`+1,utime=unix_timestamp() where id=#{id}")
    int likeAdd(@Param("id") long id);

    @Update("update xq set view=view+1,utime=unix_timestamp() where id=#{id}")
    int viewAdd(@Param("id") long id);

    @Insert("insert into xq_comment(xq_id,comment,ctime,utime) values (#{xqId},#{comment},unix_timestamp(),unix_timestamp())")
    int saveComment(@Param("xqId") long xqId, @Param("comment") String comment);

}
