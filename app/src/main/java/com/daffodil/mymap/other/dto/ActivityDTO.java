package com.daffodil.mymap.other.dto;

/**
 * Created by root on 1/14/15.
 */
public class ActivityDTO {

    private String actId, actTitle, actDetail, actRemark, actDate, actTime, leadTitle, compName, contFirstName, contLastName, contTitle, contJobTitle, contPhoneNo, contMobileNo, contEmailID, latitude, longitude, actPhoto;
    private String selectedContactIds, compId, parent_id, creator_id, compAdd, compCity, compState, compPincode, compCountry, compWebsite, compPhoneNo, compEmailID, compIndustry, compLogo, contNote, contProfilePic;

    public ActivityDTO() {


    }

    public ActivityDTO(String actId, String actTitle) {
        this.actId = actId;
        this.actTitle = actTitle;

    }


    public ActivityDTO(String actId, String actTitle, String compName, String actPhoto) {
        this.actId = actId;
        this.actTitle = actTitle;
        this.compName = compName;
        this.actPhoto = actPhoto;
    }

    public ActivityDTO(String actId, String actTitle, String actDetail, String actRemark, String actDate, String actTime, String leadTitle, String compName, String compAdd, String compCity, String compCountry, String compState, String compPincode, String compWebsite, String compPhoneNo, String compEmailID, String compIndustry, String contFirstName, String contLastName, String contTitle, String contJobTitle, String contPhoneNo, String contMobileNo, String contEmailID, String contNote, String latitude, String longitude, String actPhoto) {
        this.actId = actId;
        this.actTitle = actTitle;
        this.actDetail = actDetail;
        this.actRemark = actRemark;
        this.actDate = actDate;
        this.actTime = actTime;
        this.leadTitle = leadTitle;
        this.compName = compName;
        this.compAdd = compAdd;
        this.compCity = compCity;
        this.compState = compState;
        this.compPincode = compPincode;
        this.compCountry = compCountry;
        this.compWebsite = compWebsite;
        this.compPhoneNo = compPhoneNo;
        this.compEmailID = compEmailID;
        this.compIndustry = compIndustry;
        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contTitle = contTitle;
        this.contJobTitle = contJobTitle;
        this.contPhoneNo = contPhoneNo;
        this.contMobileNo = contMobileNo;
        this.contEmailID = contEmailID;
        this.contNote = contNote;
        this.latitude = latitude;
        this.longitude = longitude;
        this.actPhoto = actPhoto;
    }


    public ActivityDTO(String actId, String actTitle, String actDetail, String actRemark, String actDate, String actTime, String leadTitle, String compName, String contFirstName, String contLastName, String contTitle, String contJobTitle, String contPhoneNo, String contMobileNo, String contEmailID, String contNote, String latitude, String longitude, String actPhoto, String compId, String selectedContactIds) {
        this.actId = actId;
        this.actTitle = actTitle;
        this.actDetail = actDetail;
        this.actRemark = actRemark;
        this.actDate = actDate;
        this.actTime = actTime;
        this.leadTitle = leadTitle;
        this.compName = compName;
        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contTitle = contTitle;
        this.contJobTitle = contJobTitle;
        this.contPhoneNo = contPhoneNo;
        this.contMobileNo = contMobileNo;
        this.contEmailID = contEmailID;
        this.contNote = contNote;
        this.latitude = latitude;
        this.longitude = longitude;
        this.actPhoto = actPhoto;
        this.compId = compId;
        this.selectedContactIds = selectedContactIds;
    }

    public String getSelectedContactIds() {
        return selectedContactIds;
    }

    public void setSelectedContactIds(String selectedContactIds) {
        this.selectedContactIds = selectedContactIds;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getCompPincode() {
        return compPincode;
    }

    public void setCompPincode(String compPincode) {
        this.compPincode = compPincode;
    }

    public String getCompLogo() {
        return compLogo;
    }

    public void setCompLogo(String compLogo) {
        this.compLogo = compLogo;
    }

    public String getContProfilePic() {
        return contProfilePic;
    }

    public void setContProfilePic(String contProfilePic) {
        this.contProfilePic = contProfilePic;
    }

    public String getCompAdd() {
        return compAdd;
    }

    public void setCompAdd(String compAdd) {
        this.compAdd = compAdd;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getCompCity() {
        return compCity;
    }

    public void setCompCity(String compCity) {
        this.compCity = compCity;
    }

    public String getCompState() {
        return compState;
    }

    public void setCompState(String compState) {
        this.compState = compState;
    }

    public String getCompPin() {
        return compPincode;
    }

    public void setCompPin(String compPin) {
        this.compPincode = compPin;
    }

    public String getCompCountry() {
        return compCountry;
    }

    public void setCompCountry(String compCountry) {
        this.compCountry = compCountry;
    }

    public String getCompPhoneNo() {
        return compPhoneNo;
    }

    public void setCompPhoneNo(String compPhoneNo) {
        this.compPhoneNo = compPhoneNo;
    }

    public String getCompWebsite() {
        return compWebsite;
    }

    public void setCompWebsite(String compWebsite) {
        this.compWebsite = compWebsite;
    }

    public String getCompEmailID() {
        return compEmailID;
    }

    public void setCompEmailID(String compEmailID) {
        this.compEmailID = compEmailID;
    }

    public String getCompIndustry() {
        return compIndustry;
    }

    public void setCompIndustry(String compIndustry) {
        this.compIndustry = compIndustry;
    }

    public String getActPhoto() {
        return actPhoto;
    }

    public void setActPhoto(String actPhoto) {
        this.actPhoto = actPhoto;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getActTitle() {
        return actTitle;
    }

    public void setActTitle(String actTitle) {
        this.actTitle = actTitle;
    }

    public String getActDetail() {
        return actDetail;
    }

    public void setActDetail(String actDetail) {
        this.actDetail = actDetail;
    }

    public String getActRemark() {
        return actRemark;
    }

    public void setActRemark(String actRemark) {
        this.actRemark = actRemark;
    }

    public String getActDate() {
        return actDate;
    }

    public void setActDate(String actDate) {
        this.actDate = actDate;
    }

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public String getLeadTitle() {
        return leadTitle;
    }

    public void setLeadTitle(String leadTitle) {
        this.leadTitle = leadTitle;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getContFirstName() {
        return contFirstName;
    }

    public void setContFirstName(String contFirstName) {
        this.contFirstName = contFirstName;
    }

    public String getContLastName() {
        return contLastName;
    }

    public void setContLastName(String contLastName) {
        this.contLastName = contLastName;
    }

    public String getContTitle() {
        return contTitle;
    }

    public void setContTitle(String contTitle) {
        this.contTitle = contTitle;
    }

    public String getContJobTitle() {
        return contJobTitle;
    }

    public void setContJobTitle(String contJobTitle) {
        this.contJobTitle = contJobTitle;
    }

    public String getContPhoneNo() {
        return contPhoneNo;
    }

    public void setContPhoneNo(String contPhoneNo) {
        this.contPhoneNo = contPhoneNo;
    }

    public String getContMobileNo() {
        return contMobileNo;
    }

    public void setContMobileNo(String contMobileNo) {
        this.contMobileNo = contMobileNo;
    }

    public String getContEmailID() {
        return contEmailID;
    }

    public void setContEmailID(String contEmailID) {
        this.contEmailID = contEmailID;
    }

    public String getContNote() {
        return contNote;
    }

    public void setContNote(String contNote) {
        this.contNote = contNote;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
