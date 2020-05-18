package com.otapp.net.model;

public class HomeServicePojo {

    String service;
    int id;

    public HomeServicePojo(String service, int id) {
        this.service = service;
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
