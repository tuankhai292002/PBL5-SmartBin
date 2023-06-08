package com.example.pbl5;

import java.io.Serializable;

public class Bin implements Serializable {
    public static Boolean isActivityActive=true;

    public String name;
    public int state;

    public Bin(String name, int state) {
        this.name = name;
        this.state = state;
    }
}
