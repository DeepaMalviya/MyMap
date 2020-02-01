package com.daffodil.mymap.other;

/**
 * Created by root on 1/17/15.
 */
public class TrackUser {
    private String parent_id, user_id, latitude, longitude, date, time, battery_persentage, network_type;
    private String cellIdShort, cellIdLong, lac, mnc, mcc, device_type, basestation_id, basestation_latitude, basestation_longitude, shutdownTime, restartTime, signalStrenght, networkSubType, sentStatus, profileType, operatorType;

    public TrackUser() {

    }

    public TrackUser(String parent_id, String user_id, String latitude, String longitude, String date, String time, String battery_persentage, String network_type) {
        this.parent_id = parent_id;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.battery_persentage = battery_persentage;
        this.network_type = network_type;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(String sentStatus) {
        this.sentStatus = sentStatus;
    }

    public String getShutdownTime() {
        return shutdownTime;
    }

    public void setShutdownTime(String shutdownTime) {
        this.shutdownTime = shutdownTime;
    }

    public String getRestartTime() {
        return restartTime;
    }

    public void setRestartTime(String restartTime) {
        this.restartTime = restartTime;
    }

    public String getSignalStrenght() {
        return signalStrenght;
    }

    public void setSignalStrenght(String signalStrenght) {
        this.signalStrenght = signalStrenght;
    }

    public String getNetworkSubType() {
        return networkSubType;
    }

    public void setNetworkSubType(String networkSubType) {
        this.networkSubType = networkSubType;
    }

    public String getCellIdShort() {
        return cellIdShort;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getBasestation_latitude() {
        return basestation_latitude;
    }

    public void setBasestation_latitude(String basestation_latitude) {
        this.basestation_latitude = basestation_latitude;
    }

    public String getBasestation_id() {
        return basestation_id;
    }

    public void setBasestation_id(String basestation_id) {
        this.basestation_id = basestation_id;
    }

    public String getBasestation_longitude() {
        return basestation_longitude;
    }

    public void setBasestation_longitude(String basestation_longitude) {
        this.basestation_longitude = basestation_longitude;
    }

    public void setCellIdShort(String cellIdShort) {
        this.cellIdShort = cellIdShort;
    }

    public String getCellIdLong() {
        return cellIdLong;
    }

    public void setCellIdLong(String cellIdLong) {
        this.cellIdLong = cellIdLong;
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBattery_persentage() {
        return battery_persentage;
    }

    public void setBattery_persentage(String battery_persentage) {
        this.battery_persentage = battery_persentage;
    }

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }
}
