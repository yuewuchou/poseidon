package com.xw.poseidon.service.impl;

import com.xw.poseidon.dao.UserMapper;
import com.xw.poseidon.entity.PagedParameterEntity;
import com.xw.poseidon.entity.PagedResultEntity;
import com.xw.poseidon.model.User;
import com.xw.poseidon.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(User record) {
        int count = userMapper.insertSelective(record);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addBatch(List<User> list) {
        int count = userMapper.insertBatch(list);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int removeById(Integer id) {
        int count = userMapper.deleteByPrimaryKey(id);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int modifyById(User record) {
        int count = userMapper.updateByPrimaryKeySelective(record);
        return count;
    }

    @Override
    public List<User> queryByCondition(User record) {
        List<User> list = userMapper.selectByCondition(record);
        return list;
    }

    @Override
    public PagedResultEntity<User> queryPageByCondition(User record, PagedParameterEntity pagedParameter) {
        Integer pageNo = pagedParameter.getPageNo();
        Integer pageSize = pagedParameter.getPageSize();
        List<User> list = userMapper.selectPageByCondition(record, (pageNo - 1) * pageSize, pageSize);
        Integer rowCount = userMapper.selectPageByConditionCount(record);
        logger.info("queryPageByCondition-list:{}",list);
        logger.info("queryPageByCondition-rowCount:{}",rowCount);
        return new PagedResultEntity<User>(pageNo, pageSize, rowCount, list);
    }
}