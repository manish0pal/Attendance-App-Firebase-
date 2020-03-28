package com.example.adminzestech.MeetingList1;

public class Student {
    String name,std,rollno;
    public Student(){
    }

    public Student(String name, String std, String rollno) {
        this.name = name;
        this.std = std;
        this.rollno = rollno;
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
}
