/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.test.bus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Bus {

    SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    /**
     * licence : BS33822D
     * beforeBaseIndex : -1
     * busBaseIndex : -1
     * syncTime : 8
     * busId : BS33822D
     * link :
     * state : 0
     * lng : 113.92167
     * distanceToSc : 913
     * acBus : 0
     * rType : 0
     * beforeLng : -1
     * shareId :
     * order : 6
     * travels : [{"order":35,"travelTime":3406,"pRate":-1,"arrivalTime":1501557692607}]
     * beforeLat : -1
     * delay : 0
     * mTicket : 0
     * lat : 22.6777
     */

    private String licence;
    private int beforeBaseIndex;
    private int busBaseIndex;
    private int syncTime;
    private String busId;
    private String link;
    private int state;
    private double lng;
    private int distanceToSc;
    private int acBus;
    private int rType;
    private int beforeLng;
    private String shareId;
    private int order;
    private int beforeLat;
    private int delay;
    private int mTicket;
    private double lat;
    private List<TravelsBean> travels;

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public int getBeforeBaseIndex() {
        return beforeBaseIndex;
    }

    public void setBeforeBaseIndex(int beforeBaseIndex) {
        this.beforeBaseIndex = beforeBaseIndex;
    }

    public int getBusBaseIndex() {
        return busBaseIndex;
    }

    public void setBusBaseIndex(int busBaseIndex) {
        this.busBaseIndex = busBaseIndex;
    }

    public int getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(int syncTime) {
        this.syncTime = syncTime;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getDistanceToSc() {
        return distanceToSc;
    }

    public void setDistanceToSc(int distanceToSc) {
        this.distanceToSc = distanceToSc;
    }

    public int getAcBus() {
        return acBus;
    }

    public void setAcBus(int acBus) {
        this.acBus = acBus;
    }

    public int getRType() {
        return rType;
    }

    public void setRType(int rType) {
        this.rType = rType;
    }

    public int getBeforeLng() {
        return beforeLng;
    }

    public void setBeforeLng(int beforeLng) {
        this.beforeLng = beforeLng;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getBeforeLat() {
        return beforeLat;
    }

    public void setBeforeLat(int beforeLat) {
        this.beforeLat = beforeLat;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getMTicket() {
        return mTicket;
    }

    public void setMTicket(int mTicket) {
        this.mTicket = mTicket;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public List<TravelsBean> getTravels() {
        return travels;
    }

    public void setTravels(List<TravelsBean> travels) {
        this.travels = travels;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        List<TravelsBean> travels = getTravels();
        for (TravelsBean travelsBean : travels) {
            long arrivalTime = travelsBean.arrivalTime;
            Date obj = new Date(arrivalTime);
            long interval = (arrivalTime - new Date().getTime()) / 1000 / 60;
            String time = SDF.format(obj) + " - " + interval + "分钟";
            info.append(String.valueOf(time));
        }

        return busId + "  " + info;
    }

    public static class TravelsBean {
        /**
         * order : 35
         * travelTime : 3406
         * pRate : -1
         * arrivalTime : 1501557692607
         */

        private int order;
        private int travelTime;
        private int pRate;
        private long arrivalTime;

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(int travelTime) {
            this.travelTime = travelTime;
        }

        public int getPRate() {
            return pRate;
        }

        public void setPRate(int pRate) {
            this.pRate = pRate;
        }

        public long getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(long arrivalTime) {
            this.arrivalTime = arrivalTime;
        }
    }
}


