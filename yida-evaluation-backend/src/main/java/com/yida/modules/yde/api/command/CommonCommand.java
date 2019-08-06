package com.yida.modules.yde.api.command;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonCommand implements Serializable {
    private int pageNum;
    private int pageSize;
}
