package com.yida.modules.yde.api.command;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginCommand implements Serializable {
    private String username;

    private String password;
}
