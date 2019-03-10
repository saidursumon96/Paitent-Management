package com.jarvis.patientmanagement.recycler;

public class MyList {
    private String head;
    private String desc;

    public MyList(String head, String desc) {
        this.head = head;
        this.desc = desc;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }
}