package com.fan.gradleplugin.reflectasm;

public class UserService {

    private String name;

    public String ttt = "33";

    int i = 4;

    public UserService() {
        this("default");
    }

    public UserService(String name) {
        this.name = name;
    }

    public void showName() {
        System.out.println("Name is: " + name);
    }

}
