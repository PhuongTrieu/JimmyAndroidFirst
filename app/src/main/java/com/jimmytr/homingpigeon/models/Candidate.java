package com.jimmytr.homingpigeon.models;

import java.io.Serializable;

/**
 * Created by FRAMGIA\trieu.duc.phuong on 18/08/2015.
 */
public class Candidate implements Serializable {
    private String id, apply_no, name, email, auth_token;

    public Candidate(String id, String name, String email, String auth_token, String apply_no) {
        this.id = id;
        this.apply_no = apply_no;
        this.name = name;
        this.email = email;
        this.auth_token = auth_token;
    }

    public String getId() {
        return this.id;
    }

    public String getApply_no() {
        return this.apply_no;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAuth_token() {
        return this.auth_token;
    }

}
