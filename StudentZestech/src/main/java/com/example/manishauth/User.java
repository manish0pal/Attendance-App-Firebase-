package com.example.manishauth;

public class User {
    private String name;
    private String std;
    private String grno;
    private String rollno;
    private String phoneno;
    private String dob;
    private String email;
    private String uid;



    public User(){
    }


    public User(String name, String std, String grno, String rollno, String phoneno, String dob, String email, String uid){
        this.name=name;
        this.std=std;
        this.grno=grno;
        this.rollno=rollno;
        this.phoneno=phoneno;
        this.dob=dob;
        this.email=email;
        this.uid = uid;
    }

    //name1
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    //class2
    public String getStd(){
        return std;
    }
    public void setStd(String std){
        this.std=std;
    }
    //grno3
    public String getGrno(){
        return grno;
    }
    public void setGrno(String grno){
        this.grno = grno;
    }
    //roll no 4
    public String getRollno(){
        return rollno;
    }
    public void setRollno(String rollno){
        this.rollno = rollno;
    }
    //phone no5
    public String getPhoneno(){
        return phoneno;
    }
    public void set(String phoneno){
        this.phoneno=phoneno;
    }
    //date of birth6
    public String getDob(){
        return dob;
    }
    public void setDob(String dob){
        this.dob=dob;
    }
    //email7
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    //uid
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
}
