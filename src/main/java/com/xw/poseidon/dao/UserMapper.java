package com.xw.poseidon.dao;

import com.xw.poseidon.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Integer insertBatch(List<User> list);

    List<User> selectByCondition(User record);

    List<User> selectPageByCondition(@Param("record") User record, @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

    Integer selectPageByConditionCount(User record);
}