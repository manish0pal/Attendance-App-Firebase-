package com.example.adminzestech;

public class Meeting {
    private String time,code,venue,agenda,conclusion,uname,department;
    public Meeting(){
    }


    public Meeting(String time, String code, String venue, String agenda, String conclusion,String uname, String department) {
        this.time = time;
        this.code = code;
        this.venue = venue;
        this.agenda = agenda;
        this.conclusion = conclusion;
        this.uname = uname;
        this.department = department;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

}



