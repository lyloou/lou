package com.lyloou.test.flow;

import com.lyloou.test.util.Utime;

public class FlowItem {
    private String timeStart;
    private String timeEnd;
    private String timeSep = "~";
    private String content;

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeSep() {
        return timeSep;
    }

    public void setTimeSep(String timeSep) {
        this.timeSep = timeSep;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSpend() {
        String spend = Utime.getInterval(getTimeStart(), getTimeEnd());
        if (spend == null) {
            return "--:--";
        }
        return spend;
    }
}
