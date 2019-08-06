package com.yida.modules.yde.api.command;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenCommand implements Serializable {
    private String token;
}
