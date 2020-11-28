package com.example.ATMS.models;


public class Teacher {
    String tname;
    String tid;
    String subject;
    String classes;
    String tpass;
    String email;
    String phone;

  /*  public Teacher(String tname, String tid, EditText subject, Spinner classes){

    }*/

    public Teacher(String tname, String tid, String subject, String classes, String tpass,String email,String phone) {
        this.tname = tname;
        this.tid = tid;
        this.subject = subject;
        this.classes = classes;
        this.tpass = tpass;
        this.email=email;
        this.phone=phone;
    }

    public String getTname() {
        return tname;
    }

    public String getTid() {
        return tid;
    }

    public String getSubject() {
        return subject;
    }

    public String getClasses() {
        return classes;
    }

    public String gettpass() {
        return tpass;
    }

    public String getEmail(){return email;}
    public String getPhone(){return  phone;}

}
