package com.deo.activitimook.mapper;

import com.deo.activitimook.pojo.UserInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/23 上午2:13
 * @since TODO
 */
@Mapper
@Component
public interface UserInfoBeanMapper {
    // generator 插件，实际开发中使用
    // 这里为了方便，直接通过 @Select 编写 SQL 语句
    @Select("SELECT * FROM user WHERE username = #{username}")
    UserInfoBean selectByUsername(@Param("username") String username);
}
