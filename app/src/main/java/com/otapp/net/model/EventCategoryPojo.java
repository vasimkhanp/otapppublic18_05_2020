package com.otapp.net.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventCategoryPojo {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<EventCategory> data = null;

    public class EventCategory {

        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("category_name")
        @Expose
        public String categoryName;

    }

}
