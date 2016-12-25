package com.eventplanner.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ArjunHandeMac on 12/16/16.
 */

public class UserBookEvent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Address")
    private Address Address;
    @JsonProperty("date")
    private String date;
    @JsonProperty("usr")
    private String usr;
    @JsonProperty("vendor")
    private String vendor;

    /**
     *
     * @return
     * The address
     */
    @JsonProperty("address")
    public Object getAddress() {
        return Address;
    }

    /**
     *
     * @param address
     * The address
     */
    @JsonProperty("address")
    public void setAddress(Address address) {
        this.Address = address;
    }

    /**
     *
     * @return
     * The date
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The usr
     */
    @JsonProperty("usr")
    public String getUsr() {
        return usr;
    }

    /**
     *
     * @return
     * The usr
     */
    @JsonProperty("vendor")
    public String getVendor() {
        return vendor;
    }

    /**
     *
     * @param usr
     * The usr
     */
    @JsonProperty("usr")
    public void setUsr(String usr) {
        this.usr = usr;
    }

    /**
     *
     * @param vendor
     * The vendor
     */
    @JsonProperty("vendor")
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
