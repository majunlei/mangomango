package cc.mangomango.dao;

import cc.mangomango.domain.TimeLine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TimeLineMapper {

    @Select("select id,`date`,description,ctime from time_line order by id desc limit #{offset},#{limits}")
    List<TimeLine> getByPage(@Param("offset") int offset, @Param("limits") int limits);

    @Select("select count(*) from time_line")
    int getCount();

    @Insert("insert into time_line(`date`,description,ctime,utime) values (#{date},#{description},unix_timestamp(),unix_timestamp())")
    void save(TimeLine timeLine);
}
