package com.otapp.net.home.core;

public class PagerModel {
    public String pagecount="0";
    public  boolean isSelectedPage=false;

    public PagerModel(String pagecount, boolean isSelectedPage) {
        this.pagecount = pagecount;
        this.isSelectedPage = isSelectedPage;
    }
}
