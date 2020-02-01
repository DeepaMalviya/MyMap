package com.daffodil.mymap.other.dto;


public class Leaddto {
    String lead_id, lead_name, date, status, time, c_name, lat, log, parent_id, creater_id, name, phno, emailid, note, photo, leadClosingTime, leadBudget;
    private String compId, leadTitle, leadDetail, leadRemark, leadStatus, leadSource, compname, contFirstNAme, contLastName, contTitle, contJobTitle, contPhoneNo, contMobileNo, contEmailId, contNote, contProfilePic;
    private String selectedContactIds, compAdd, compCity, compState, compPincode, compCountry, compWebsite, compPhoneNumber, compEmailID, compIndustry, compLogo, compLat, compLong;

    public Leaddto() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Leaddto(String lead_id, String leadTitle) {
        this.lead_id = lead_id;
        this.leadTitle = leadTitle;
    }

    public Leaddto(String lead_id, String leadTitle, String leadDetail, String leadRemark, String leadClosingTime, String leadBudget, String leadSource, String leadStatus, String compname, String contLastName, String contFirstNAme, String contTitle, String contJobTitle, String contPhoneNo, String contMobileNo, String contEmailId, String contNote, String contProfilePic, String creater_id, String parent_id, String selectedContactIds, String user_id, String compId) {
        this.lead_id = lead_id;
        this.leadTitle = leadTitle;
        this.leadDetail = leadDetail;
        this.leadRemark = leadRemark;

        this.leadClosingTime = leadClosingTime;
        this.leadBudget = leadBudget;

        this.leadSource = leadSource;
        this.leadStatus = leadStatus;
        this.compname = compname;
        this.contLastName = contLastName;
        this.contFirstNAme = contFirstNAme;
        this.contTitle = contTitle;
        this.contJobTitle = contJobTitle;
        this.contPhoneNo = contPhoneNo;
        this.contMobileNo = contMobileNo;
        this.contEmailId = contEmailId;
        this.contNote = contNote;
        this.contProfilePic = contProfilePic;
        this.creater_id = creater_id;
        this.parent_id = parent_id;
        this.selectedContactIds = selectedContactIds;
        this.creater_id = user_id;
        this.compId = compId;
    }

    public String getSelectedContactIds() {
        return selectedContactIds;
    }

    public void setSelectedContactIds(String selectedContactIds) {
        this.selectedContactIds = selectedContactIds;
    }

    public String getLeadClosingTime() {
        return leadClosingTime;
    }

    public void setLeadClosingTime(String leadClosingTime) {
        this.leadClosingTime = leadClosingTime;
    }

    public String getLeadBudget() {
        return leadBudget;
    }

    public void setLeadBudget(String leadBudget) {
        this.leadBudget = leadBudget;
    }

    public String getCompCountry() {
        return compCountry;
    }

    public void setCompCountry(String compCountry) {
        this.compCountry = compCountry;
    }

    public String getCompAdd() {
        return compAdd;
    }

    public void setCompAdd(String compAdd) {
        this.compAdd = compAdd;
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

    public String getCompPincode() {
        return compPincode;
    }

    public void setCompPincode(String compPincode) {
        this.compPincode = compPincode;
    }

    public String getCompWebsite() {
        return compWebsite;
    }

    public void setCompWebsite(String compWebsite) {
        this.compWebsite = compWebsite;
    }

    public String getCompPhoneNumber() {
        return compPhoneNumber;
    }

    public void setCompPhoneNumber(String compPhoneNumber) {
        this.compPhoneNumber = compPhoneNumber;
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

    public String getCompLogo() {
        return compLogo;
    }

    public void setCompLogo(String compLogo) {
        this.compLogo = compLogo;
    }

    public String getCompLat() {
        return compLat;
    }

    public void setCompLat(String compLat) {
        this.compLat = compLat;
    }

    public String getCompLong() {
        return compLong;
    }

    public void setCompLong(String compLong) {
        this.compLong = compLong;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getCompname() {
        return compname;
    }

    public void setCompname(String compname) {
        this.compname = compname;
    }

    public String getLeadTitle() {
        return leadTitle;
    }

    public void setLeadTitle(String leadTitle) {
        this.leadTitle = leadTitle;
    }

    public String getLeadDetail() {
        return leadDetail;
    }

    public void setLeadDetail(String leadDetail) {
        this.leadDetail = leadDetail;
    }

    public String getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getLeadRemark() {
        return leadRemark;
    }

    public void setLeadRemark(String leadRemark) {
        this.leadRemark = leadRemark;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public String getContFirstNAme() {
        return contFirstNAme;
    }

    public void setContFirstNAme(String contFirstNAme) {
        this.contFirstNAme = contFirstNAme;
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

    public String getContEmailId() {
        return contEmailId;
    }

    public void setContEmailId(String contEmailId) {
        this.contEmailId = contEmailId;
    }

    public String getContNote() {
        return contNote;
    }

    public void setContNote(String contNote) {
        this.contNote = contNote;
    }

    public String getContProfilePic() {
        return contProfilePic;
    }

    public void setContProfilePic(String contProfilePic) {
        this.contProfilePic = contProfilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getCreater_id() {
        return creater_id;
    }

    public void setCreater_id(String creater_id) {
        this.creater_id = creater_id;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getLead_name() {
        return lead_name;
    }

    public void setLead_name(String lead_name) {
        this.lead_name = lead_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Leaddto(String lead_id, String lead_name, String date, String status) {
        super();
        this.lead_id = lead_id;
        this.lead_name = lead_name;
        this.date = date;
        this.leadStatus = status;
    }

    public Leaddto(String lead_id, String lead_name, String date, String status, String c_name) {
        super();
        this.lead_id = lead_id;
        this.leadTitle = lead_name;
        this.date = date;
        this.leadStatus = status;
        this.compname = c_name;
    }

    public Leaddto(String lead_id, String lead_name, String date, String status,
                   String time, String c_name, String lat, String log) {
        super();
        this.lead_id = lead_id;
        this.lead_name = lead_name;
        this.date = date;
        this.leadStatus = status;
        this.time = time;
        this.compname = c_name;
        this.lat = lat;
        this.log = log;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }


}
