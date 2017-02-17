package com.findr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by harshsetia on 14/02/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Venue {

    private String name;
    private String url;
    private String phoneNumber;
    private String fullAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                '}';
    }
}
