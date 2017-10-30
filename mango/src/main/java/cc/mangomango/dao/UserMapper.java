package cc.mangomango.dao;

import cc.mangomango.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by majunlei on 2017/10/30.
 */
public interface UserMapper {

    @Select("select id,`username`,`password`,ctime from `user` where username=#{username}")
    public User getByUsername(@Param("username") String username);

    @Select("insert into `user`(`username`,`password`,ctime,utime) values (#{username},#{password},unix_timestamp(),unix_timestamp())")
    public void save(@Param("username") String username, @Param("password") String password);

    @Insert("insert into user_token(user_id,token,status,ctime,utime) values(#{userId},#{token},0,unix_timestamp(),unix_timestamp())")
    public void saveToken(@Param("userId") long userId, @Param("token") String token);

    @Select("select count(*) from `user` u inner join `user_token` t on u.id=t.user_id where u.username=#{username} and t.token=#{token} and t.status=0")
    public int countToken(@Param("username") String username, @Param("token") String token);

}