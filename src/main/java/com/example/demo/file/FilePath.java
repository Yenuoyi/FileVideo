package com.example.demo.file;

public enum FilePath {
    //这里请改成自己的真实路径
    ONE(1,"tempFile\\"),TWO(2,"formalFile\\");
    private int num;
    private String name;
    private FilePath(int num,String name){
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
