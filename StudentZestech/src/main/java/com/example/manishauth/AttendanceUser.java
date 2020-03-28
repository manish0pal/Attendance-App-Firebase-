package com.example.manishauth;

public class AttendanceUser {
    private String name,std,rollno;

    public AttendanceUser(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public AttendanceUser(String name, String std, String rollno) {
        this.name = name;
        this.std = std;
        this.rollno = rollno;
    }
}
