package com.quicsolv.rcity.mapping;

public class MappingPostBody {

    public MappingPostBody(String username, String password, String pois_from, String pois_to) {
        this.username = username;
        this.password = password;
        this.pois_from = pois_from;
        this.pois_to = pois_to;
    }

    String username,password,pois_from,pois_to;
}
