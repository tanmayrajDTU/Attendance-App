package com.example.ATMS.models;


public class Student {
    String sname;
    String sid;
    String classes;
    String spass;
    String email;
    String phone;

  /*  public Student(String sname, String sid){

    }*/

    public Student(String sname, String sid,String classes,String spass,String email,String phone) {
        this.sname = sname;
        this.sid = sid;
        this.classes = classes;
        this.spass = spass;
        this.email=email;
        this.phone=phone;
    }

    public String getSname() { return sname; }

    public String getSid() {
        return sid;
    }
    public String getClasses() {
        return classes;
    }

    public String getspass() { return spass; }
    public String getEmail(){return email;}
    public String getPhone(){return  phone;}
}
