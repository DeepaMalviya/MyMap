package com.daffodil.mymap.other.dto;

/**
 * Created by roshni on 12/30/14.
 */
public class Event {
    private String eventId, eventTitle, eventDetail, eventStartTime, eventEndTime, eventLocation, eventRemarks, eventLatitude, eventLongitude, eventImage, compName, compAdd, compCity, compPin, compState, compCountry, compWeb, compphone, compEmail, contName, contJobTitle, contMob, contEmail;
    private String comp_id, compIndustry, contTitle, contFirstName, contLastName, contPhoneNumber, contNote;
    private String selectedContactIds, parentId, creatorId, contId, compLogo, contProfilePic, eventPhoto;

    public Event() {

    }

    public Event(String eventId, String eventTitle) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
    }

    public Event(String eventId, String eventTitle, String eventDetail, String eventStartTime, String eventEndTime, String eventLocation, String eventRemark, String eventLatitude, String eventLongitude, String eventImage, String contMob, String contJobTitle, String contEmail, String comp_id, String contTitle, String contFirstName, String contLastName, String contPhoneNumber, String contNote, String compName, String selectedContactIds) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventLocation = eventLocation;
        this.eventRemarks = eventRemark;
        this.eventLatitude = eventLatitude;
        this.eventLongitude = eventLongitude;
        this.eventImage = eventImage;
        this.contMob = contMob;
        this.contJobTitle = contJobTitle;
        this.contEmail = contEmail;
        this.comp_id = comp_id;
        this.compName = compName;

        this.contTitle = contTitle;
        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contPhoneNumber = contPhoneNumber;
        this.contNote = contNote;
        this.selectedContactIds = selectedContactIds;

    }

    public Event(String eventId, String compName, String eventTitle, String eventStartTime, String eventEndTime, String eventLocation, String eventLatitude, String eventLongitude, String date) {

        this.eventId = eventId;
        this.compName = compName;
        this.eventTitle = eventTitle;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventLocation = eventLocation;
        this.eventLatitude = eventLatitude;
        this.eventLongitude = eventLongitude;


    }

    public Event(String eventTitle, String eventDetail, String eventStartTime, String eventEndTime, String eventLocation, String eventRemarks, String eventImage, String comp_id) {
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventLocation = eventLocation;
        this.eventRemarks = eventRemarks;

        this.eventImage = eventImage;
        this.comp_id = comp_id;
    }

    public Event(String eventTitle, String eventDetail, String eventStartTime, String eventEndTime, String eventLocation, String eventRemarks, String eventLatitude, String eventLongitude, String eventImage, String compName, String compAdd, String compCity, String compPin, String compState, String compCountry, String compWeb, String compphone, String compEmail, String contName, String contJobTitle, String contMob, String contEmail) {
        this.eventTitle = eventTitle;
        this.eventDetail = eventDetail;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventLocation = eventLocation;
        this.eventRemarks = eventRemarks;
        this.eventLatitude = eventLatitude;
        this.eventLongitude = eventLongitude;
        this.eventImage = eventImage;
        this.compName = compName;
        this.compAdd = compAdd;
        this.compCity = compCity;
        this.compPin = compPin;
        this.compState = compState;
        this.compCountry = compCountry;
        this.compWeb = compWeb;
        this.compphone = compphone;
        this.compEmail = compEmail;
        this.contName = contName;
        this.contJobTitle = contJobTitle;
        this.contMob = contMob;
        this.contEmail = contEmail;
    }

    public String getSelectedContactIds() {
        return selectedContactIds;
    }

    public void setSelectedContactIds(String selectedContactIds) {
        this.selectedContactIds = selectedContactIds;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
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

    public String getEventPhoto() {
        return eventPhoto;
    }

    public void setEventPhoto(String eventPhoto) {
        this.eventPhoto = eventPhoto;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getCompIndustry() {
        return compIndustry;
    }

    public void setCompIndustry(String compIndustry) {
        this.compIndustry = compIndustry;
    }

    public String getContTitle() {
        return contTitle;
    }

    public void setContTitle(String contTitle) {
        this.contTitle = contTitle;
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

    public String getContPhoneNumber() {
        return contPhoneNumber;
    }

    public void setContPhoneNumber(String contPhoneNumber) {
        this.contPhoneNumber = contPhoneNumber;
    }

    public String getContNote() {
        return contNote;
    }

    public void setContNote(String contNote) {
        this.contNote = contNote;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventRemarks() {
        return eventRemarks;
    }

    public void setEventRemarks(String eventRemarks) {
        this.eventRemarks = eventRemarks;
    }

    public String getEventLatitude() {
        return eventLatitude;
    }

    public void setEventLatitude(String eventLatitude) {
        this.eventLatitude = eventLatitude;
    }

    public String getEventLongitude() {
        return eventLongitude;
    }

    public void setEventLongitude(String eventLongitude) {
        this.eventLongitude = eventLongitude;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getCompCity() {
        return compCity;
    }

    public void setCompCity(String compCity) {
        this.compCity = compCity;
    }

    public String getCompAdd() {
        return compAdd;
    }

    public void setCompAdd(String compAdd) {
        this.compAdd = compAdd;
    }

    public String getCompPin() {
        return compPin;
    }

    public void setCompPin(String compPin) {
        this.compPin = compPin;
    }

    public String getCompState() {
        return compState;
    }

    public void setCompState(String compState) {
        this.compState = compState;
    }

    public String getCompCountry() {
        return compCountry;
    }

    public void setCompCountry(String compCountry) {
        this.compCountry = compCountry;
    }

    public String getCompWeb() {
        return compWeb;
    }

    public void setCompWeb(String compWeb) {
        this.compWeb = compWeb;
    }

    public String getCompphone() {
        return compphone;
    }

    public void setCompphone(String compphone) {
        this.compphone = compphone;
    }

    public String getCompEmail() {
        return compEmail;
    }

    public void setCompEmail(String compEmail) {
        this.compEmail = compEmail;
    }

    public String getContJobTitle() {
        return contJobTitle;
    }

    public void setContJobTitle(String contJobTitle) {
        this.contJobTitle = contJobTitle;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getContMob() {
        return contMob;
    }

    public void setContMob(String contMob) {
        this.contMob = contMob;
    }

    public String getContEmail() {
        return contEmail;
    }

    public void setContEmail(String contEmail) {
        this.contEmail = contEmail;
    }
}
