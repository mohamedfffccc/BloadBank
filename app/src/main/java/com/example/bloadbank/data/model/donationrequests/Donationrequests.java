
package com.example.bloadbank.data.model.donationrequests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Donationrequests {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private DonationPagination data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DonationPagination getData() {
        return data;
    }

    public void setData(DonationPagination data) {
        this.data = data;
    }

}
