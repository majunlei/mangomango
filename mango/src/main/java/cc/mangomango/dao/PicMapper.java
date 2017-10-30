package cc.mangomango.dao;

import cc.mangomango.domain.Pic;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 作者:majunlei
 * 日期:2017/10/12 下午9:42
 */
public interface PicMapper {

    @Select("select seq from pic order by seq desc limit 1")
    public Integer getLatestSeq();

    @Select("select count(*) from pic")
    public int countTotal();

    @Insert("insert into pic(seq,url,thumb_url,title,ctime,utime) values(#{seq},#{url},#{thumbUrl},#{title},unix_timestamp(),unix_timestamp())")
    public int save(Pic pic);

    @Select("select id,seq,url,thumb_url,title,ctime,utime from pic order by seq asc limit #{offset},#{limits}")
    public List<Pic> getByPage(@Param("offset") int offset, @Param("limits") int limits);

    @Delete("delete from pic where id=#{id}")
    public int del(@Param("id") long id);

    @Select("select url from pic where id=#{id}")
    public String getUrlById(@Param("id") long id);

    @Update("update pic set seq=#{pos},utime=unix_timestamp() where id=#{id}")
    public int updatePos(@Param("id") long id, @Param("pos") int pos);
}
