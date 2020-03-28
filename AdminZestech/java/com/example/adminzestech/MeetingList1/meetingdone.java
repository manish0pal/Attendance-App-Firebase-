
package com.example.adminzestech.MeetingList1;

public class meetingdone {
    String time,venue,agenda,department,uname;
    public  meetingdone(){

    }

    public meetingdone(String time, String venue, String agenda,String department, String uname) {
        this.time = time;
        this.venue = venue;
        this.agenda = agenda;
        this.department = department;
        this.uname = uname;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
