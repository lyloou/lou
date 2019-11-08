package com.lyloou.test.flow;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FlowDay implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("id")
    private Integer id; // 20190921
    @SerializedName("day")
    private String day; // 20190921
    @SerializedName("is_disabled")
    private Integer isDisabled; // 20190921
    @SerializedName("items")
    private List<FlowItem> items;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<FlowItem> getItems() {
        return items;
    }

    public void setItems(List<FlowItem> items) {
        this.items = items;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Integer isDisabled) {
        this.isDisabled = isDisabled;
    }
}
