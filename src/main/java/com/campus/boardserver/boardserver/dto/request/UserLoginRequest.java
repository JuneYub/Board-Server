package com.campus.boardserver.boardserver.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginRequest {
    @NonNull
    private String userId;
    @NonNull
    private String password;
}