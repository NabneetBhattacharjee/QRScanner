package com.example.qrscanner.Model;

public class ListItem {
    int id;
    String code;
    String type;

    public ListItem(int id, String code, String type) {
        this.id = id;
        this.code = code;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    //public void setId(int id) {
       // this.id = id;
    //}

    public String getCode() {
        return code;
    }

    //public void setCode(String code) {
        //this.code = code;
   // }

    public String getType() {
        return type;
    }

    //public void setType(String type) {
        //this.type = type;
    //}
}
