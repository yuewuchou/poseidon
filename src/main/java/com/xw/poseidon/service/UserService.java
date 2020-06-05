package com.xw.poseidon.service;

import com.xw.poseidon.entity.PagedParameterEntity;
import com.xw.poseidon.entity.PagedResultEntity;
import com.xw.poseidon.model.User;
import java.util.List;

public interface UserService {
    int add(User record);

    int addBatch(List<User> list);

    int removeById(Integer id);

    int modifyById(User record);

    List<User> queryByCondition(User record);

    PagedResultEntity<User> queryPageByCondition(User record, PagedParameterEntity pagedParameter);
}