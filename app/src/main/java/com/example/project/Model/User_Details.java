package com.example.project.Model;

public class User_Details {

    String f_name,l_name,ID;
    Integer t_number,d_license;


    public User_Details(String ID,String f_name, String l_name, Integer t_number, Integer d_license) {
        this.ID = ID;
        this.f_name = f_name;
        this.l_name = l_name;
        this.t_number = t_number;
        this.d_license = d_license;
    }

    public User_Details() {
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public Integer getD_license() {
        return d_license;
    }

    public void setD_license(Integer d_license) {
        this.d_license = d_license;
    }

    public Integer getT_number() {
        return t_number;
    }

    public void setT_number(Integer t_number) {
        this.t_number = t_number;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    @Override
    public String toString() {
        return "User_Details{" +
                "f_name='" + f_name + '\'' +
                ", l_name='" + l_name + '\'' +
                ", t_number='" + t_number + '\'' +
                ", d_license='" + d_license + '\'' +
                '}';
    }
}
