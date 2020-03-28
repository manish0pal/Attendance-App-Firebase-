package com.example.adminzestech.StudentRecords;

public class userinmeeting {
    public String name;
    public String std;
    public String rollno;
    public String department;
    public String uid;
    public userinmeeting(){

    }

    public userinmeeting(String uid) {
        this.uid = uid;
    }

    public userinmeeting(String name, String std, String rollno, String department ) {
        this.name = name;
        this.std = std;
        this.rollno = rollno;

        this.department = department;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
