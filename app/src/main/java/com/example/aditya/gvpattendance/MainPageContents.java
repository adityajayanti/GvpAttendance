package com.example.aditya.gvpattendance;

import android.util.Log;

/**
 * Created by Aditya on 28-02-2018.
 */

public class MainPageContents {

    private String title ;
    private String desc ;
    private String timehour;
    private int sem;
    private int image;
    private String stu_id;
    private String stu_name;
    private String stu_dept;
    private int stu_sem;
    private long stu_mob;
    private String stu_email;
    private boolean PA;
    private String total_att;
    private  String total_held;
    private String total_per;

    public MainPageContents(String stu_date , String stu_hour , String stu_topic , String stu_ab){
        this.title = stu_date;
        this.desc = stu_hour;
        this.timehour = stu_topic;
        this.stu_id = stu_ab;
    }

    public MainPageContents(String sub , String dept , String sem){
        this.title = sub;
        this.desc = dept;
        this.timehour = sem;
    }

    public MainPageContents(String stu_id , String stu_name , String stu_total_att , String stu_total_held , String stu_total_per){
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.total_att = stu_total_att;
        this.total_held = stu_total_held;
        this.total_per = stu_total_per;
    }

    public MainPageContents(String stu_id, String stu_name , String stu_dept , int stu_sem , long stu_mob , String stu_email) {

        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.stu_dept = stu_dept;
        this.stu_sem = stu_sem;
        this.stu_mob = stu_mob;
        this.stu_email = stu_email;

    }

    public MainPageContents(String stu_id , String stu_name , Boolean PA){
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.PA = PA;
    }

    public MainPageContents(String title, String desc, int image) {

        this.title = title;
        this.desc = desc;
        this.image = image;

    }

    public MainPageContents(String title, String desc, String timehour,int sem ) {

        this.title = title;
        this.desc = desc;
        this.timehour = timehour;
        this.sem = sem;

    }

    public String getTitle() {return title;}

    public String getDesc() {
        return desc;
    }

    public String getTimehour() {
        return timehour;
    }

    public int getSem() {
        return sem;
    }

    public int getImage() { return image; }


    public String getStu_id() {
        return stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public String getStu_dept() {
        return stu_dept;
    }

    public int getStu_sem() {
        return stu_sem;
    }

    public long getStu_mob() {
        return stu_mob;
    }

    public String getStu_email() {
        return stu_email;
    }

    public Boolean get_PA() { return PA;}

    public void set_PA(Boolean selected){ PA = selected; }


    public String getTotal_att() {
        return total_att;
    }

    public String getTotal_held() {
        return total_held;
    }

    public String getTotal_per() {
        return total_per;
    }

}
