package com.xw.poseidon.controller;

import com.xw.poseidon.entity.PagedParameterEntity;
import com.xw.poseidon.entity.PagedResultEntity;
import com.xw.poseidon.entity.ResultEntity;
import com.xw.poseidon.entity.ResultEnum;
import com.xw.poseidon.model.User;
import com.xw.poseidon.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @ResponseBody
    public ResultEntity<User> add(User record) {
        try {
            userService.add(record);
            return new ResultEntity<User>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), record);
        } catch (Exception e) {
            logger.error("新增用户失败", e);
            return new ResultEntity<User>(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());
        }
    }

    @PostMapping("/addBatch")
    @ResponseBody
    public ResultEntity<User> addBatch(@RequestBody List<User> list) {
        try {
            userService.addBatch(list);
            return new ResultEntity<User>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("批量新增用户失败", e);
            return new ResultEntity<User>(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());
        }
    }

    @PostMapping("/removeById")
    @ResponseBody
    public ResultEntity<User> addBatch(int id) {
        try {
            userService.removeById(id);
            return new ResultEntity<User>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("删除用户失败", e);
            return new ResultEntity<User>(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());
        }
    }

    @PostMapping("/modifyById")
    @ResponseBody
    public ResultEntity<User> modifyById(User record) {
        try {
            userService.modifyById(record);
            return new ResultEntity<User>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            logger.error("修改用户失败", e);
            return new ResultEntity<User>(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());
        }
    }

    @PostMapping("/queryByCondition")
    @ResponseBody
    public ResultEntity<List<User>> queryByCondition(User record) {
        try {
            List<User> list = userService.queryByCondition(record);
            return new ResultEntity<List<User>>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), list);
        } catch (Exception e) {
            logger.error("查询用户失败", e);
            return new ResultEntity<List<User>>(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());
        }
    }

    @PostMapping("/queryPageByCondition")
    @ResponseBody
    public ResultEntity<PagedResultEntity<User>> queryPageByCondition(User record, PagedParameterEntity pagedParameter) {
        try {
            PagedResultEntity<User> pagedList = userService.queryPageByCondition(record, pagedParameter);
            return new ResultEntity<PagedResultEntity<User>>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), pagedList);
        } catch (Exception e) {
            logger.error("分页查询用户失败", e);
            return new ResultEntity<PagedResultEntity<User>>(ResultEnum.FAILURE.getCode(), ResultEnum.FAILURE.getMessage());
        }
    }
}