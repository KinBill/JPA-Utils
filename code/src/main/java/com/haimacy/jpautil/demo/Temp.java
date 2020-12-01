package com.haimacy.jpautil.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;

@Data
//@Entity
@AllArgsConstructor
public class Temp {
    private String id;

    private String mobile;

    private String a;

    public Temp(String id, String mobile) {
        this.id = id;
        this.mobile = mobile;
    }
}
