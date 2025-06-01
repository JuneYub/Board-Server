package com.campus.boardserver.boardserver.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    public enum Status {
        DEFAULT, ADMIN, DELETED
    }

    private int id;
    private String userId;
    private String password;
    private String nickName;
    private boolean isAdmin;
    private Date createTime;
    private boolean isWithDraw;
    private Status status;
    private Date updateTime;
}
