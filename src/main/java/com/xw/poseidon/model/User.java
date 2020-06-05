package com.xw.poseidon.model;

import java.util.Date;

/**
 *   用户表
 */
public class User {
    /**
     *   用户表id
     */
    private Integer id;

    /**
     *   用户名
     */
    private String username;

    /**
     *   密码
     */
    private String password;

    /**
     *   创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}