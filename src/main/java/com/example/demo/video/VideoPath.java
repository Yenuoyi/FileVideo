package com.example.demo.video;

public enum  VideoPath {
    ONE(1,"C:\\Users\\yebing\\Desktop\\video1\\"),
    TWO(2,"C:\\Users\\yebing\\Desktop\\video2\\"),
    THREE(3,"C:\\Users\\yebing\\Desktop\\video3\\");
    private int num;
    private String name;
    private VideoPath(int num, String name){
        this.num = num;
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
