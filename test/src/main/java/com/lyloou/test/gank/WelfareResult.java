package com.lyloou.test.gank;

import java.io.Serializable;
import java.util.List;

public class WelfareResult implements Serializable {

    /**
     * error : false
     * results : [{"_id":"5c6a4ae99d212226776d3256","createdAt":"2019-02-18T06:04:25.571Z","desc":"2019-02-18","publishedAt":"2019-04-10T00:00:00.0Z","source":"web","type":"福利","url":"https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg","used":true,"who":"lijinshanmx"},{"_id":"5c2dabdb9d21226e068debf9","createdAt":"2019-01-03T06:29:47.895Z","desc":"2019-01-03","publishedAt":"2019-01-03T00:00:00.0Z","source":"web","type":"福利","url":"https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg","used":true,"who":"lijinshanmx"}]
     */

    private boolean error;
    private List<Welfare> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Welfare> getResults() {
        return results;
    }

    public void setResults(List<Welfare> results) {
        this.results = results;
    }
}
