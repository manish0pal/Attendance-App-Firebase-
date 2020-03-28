package com.example.adminzestech.MessageToAll;

public class messages_gone {
    public String msg,department,name,time;

    public messages_gone(){
    }

    public messages_gone(String department, String name, String time,String msg) {
        this.department = department;
        this.name = name;
        this.time = time;
        this.msg = msg;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
