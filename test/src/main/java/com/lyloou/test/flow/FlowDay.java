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
    private boolean isDisabled; // 20190921
    @SerializedName("is_synced")
    private boolean isSynced;
    @SerializedName("is_archived")
    private boolean isArchived;
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

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
