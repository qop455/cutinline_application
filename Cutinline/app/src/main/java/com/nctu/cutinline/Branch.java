package com.nctu.cutinline;

import android.util.Log;

/**
 * Created by jason on 2016/5/17.
 */
public class Branch {

    private int branch_id;
    private String branch_name;
    private String branch_city;
    private String branch_district;
    private String branch_village;
    private String branch_address;


    public Branch() {
    }

    public Branch(int branch_id, String branch_name, String branch_city, String branch_district, String branch_village, String branch_address) {
        this.branch_id = branch_id;
        this.branch_name = branch_name;
        this.branch_city = branch_city;
        this.branch_district = branch_district;
        this.branch_village = branch_village;
        this.branch_address = branch_address;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_city() {
        return branch_city;
    }

    public void setBranch_city(String branch_city) {
        this.branch_city = branch_city;
    }

    public String getBranch_district() {
        return branch_district;
    }

    public void setBranch_district(String branch_district) {
        this.branch_district = branch_district;
    }

    public String getBranch_village() {
        return branch_village;
    }

    public void setBranch_village(String branch_village) {
        this.branch_village = branch_village;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }
}
