package com.ECounselling.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String mailId;
    private String password;
}