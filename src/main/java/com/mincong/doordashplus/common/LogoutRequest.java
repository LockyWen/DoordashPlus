package com.mincong.doordashplus.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class LogoutRequest implements Serializable {
    private String id;
}
