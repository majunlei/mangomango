package cc.mangomango.dao;

import cc.mangomango.domain.Story;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by majunlei on 2017/10/15.
 */
public interface StoryMapper {

    @Select("select id,title,author,dt,ctime from story order by ctime desc limit ${offset},#{limits}")
    List<Story> getByPage(@Param("offset") int offset, @Param("limits") int limits);

    @Select("select s.id,s.title,s.author,s.dt,s.ctime,sc.content from story s inner join story_content sc on s.id=sc.story_id where s.id=#{id}")
    Story getDetailByStoryId(@Param("id") long id);

    @Select("select count(*) from story")
    int getCount();

    @Insert("insert into story(title,author,dt,ctime,utime) values (#{title},#{author},#{dt},unix_timestamp(),unix_timestamp())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(Story story);

    @Insert("insert into story_content(story_id,content,ctime,utime) values (#{storyId},#{content},unix_timestamp(),unix_timestamp())")
    void saveContent(@Param("storyId") long storyId, @Param("content") String content);

}
