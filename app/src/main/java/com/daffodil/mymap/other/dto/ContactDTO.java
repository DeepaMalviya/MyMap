package com.daffodil.mymap.other.dto;

public class ContactDTO {
    private String contNote, contId, contName, contMobileNum, compName, contTitle, contFirstName, contLastName, contJobTitle, contPhoneNum, contEmailId;
    private String compId, compAdd, compCity, compState, compPincode, compCountry, compWebsite, compPhoneNo, compEmailID, compIndustry, compLogo, contLatitude, contLongitude, contProfilePic, parent_id, creator_id, contPhotoId;

    public ContactDTO() {


    }

    public ContactDTO(String parent_id, String creator_id, String contId, String contTitle, String contFirstName, String contLastName, String contJobTitle, String contPhoneNum, String contMobileNum, String contEmailId, String contProfilePic, String compId, String compName, String contPhotoId) {
        this.parent_id = parent_id;
        this.creator_id = creator_id;
        this.contId = contId;
        this.contTitle = contTitle;
        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contJobTitle = contJobTitle;
        this.contPhoneNum = contPhoneNum;
        this.contMobileNum = contMobileNum;
        this.contEmailId = contEmailId;
        this.contProfilePic = contProfilePic;
        this.compId = compId;
        this.compName = compName;
        this.contPhotoId = contPhotoId;
    }

    public ContactDTO(String parent_id, String creator_id, String contId, String contTitle, String contFirstName, String contLastName, String contJobTitle, String contPhoneNum, String contMobileNum,
                      String contEmailId, String contNote, String compId, String compName,
                      String compAdd, String compCity,
                      String compState, String compPincode, String compCountry, String compWebsite,
                      String compPhoneNo, String compEmailID, String compIndustry, String compLogo,
                      String contProfilePic, String contLatitude, String contLongitude) {


        this.parent_id = parent_id;
        this.creator_id = creator_id;
        this.contId = contId;


        this.contTitle = contTitle;
        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contJobTitle = contJobTitle;
        this.contMobileNum = contMobileNum;
        this.contPhoneNum = contPhoneNum;
        this.contEmailId = contEmailId;
        this.contNote = contNote;
        this.compId = compId;
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
        this.compLogo = compLogo;
        this.contProfilePic = contProfilePic;
        this.contLatitude = contLatitude;
        this.contLongitude = contLongitude;


    }

    public String getContPhotoId() {
        return contPhotoId;
    }

    public void setContPhotoId(String contPhotoId) {
        this.contPhotoId = contPhotoId;
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

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
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

    public String getCompCountry() {
        return compCountry;
    }

    public void setCompCountry(String compCountry) {
        this.compCountry = compCountry;
    }

    public String getCompWebsite() {
        return compWebsite;
    }

    public void setCompWebsite(String compWebsite) {
        this.compWebsite = compWebsite;
    }

    public String getCompPhoneNo() {
        return compPhoneNo;
    }

    public void setCompPhoneNo(String compPhoneNo) {
        this.compPhoneNo = compPhoneNo;
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

    public String getContLatitude() {
        return contLatitude;
    }

    public void setContLatitude(String contLatitude) {
        this.contLatitude = contLatitude;
    }

    public String getContProfilePic() {
        return contProfilePic;
    }

    public void setContProfilePic(String contProfilePic) {
        this.contProfilePic = contProfilePic;
    }

    public String getContLongitude() {
        return contLongitude;
    }

    public void setContLongitude(String contLongitude) {
        this.contLongitude = contLongitude;
    }

    public String getContNote() {
        return contNote;
    }

    public void setContNote(String contNote) {
        this.contNote = contNote;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

    public String getName() {
        return contName;
    }

    public void setName(String name) {
        this.contName = name;
    }

    public String getCont_num() {
        return contMobileNum;
    }

    public void setCont_num(String cont_num) {
        this.contMobileNum = cont_num;
    }

    public String getComp_name() {
        return compName;
    }

    public void setComp_name(String comp_name) {
        this.compName = comp_name;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getContMobileNum() {
        return contMobileNum;
    }

    public void setContMobileNum(String contMobileNum) {
        this.contMobileNum = contMobileNum;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
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

    public String getContJobTitle() {
        return contJobTitle;
    }

    public void setContJobTitle(String contJobTitle) {
        this.contJobTitle = contJobTitle;
    }

    public String getContPhoneNum() {
        return contPhoneNum;
    }

    public void setContPhoneNum(String contPhoneNum) {
        this.contPhoneNum = contPhoneNum;
    }

    public String getContEmailId() {
        return contEmailId;
    }

    public void setContEmailId(String contEmailId) {
        this.contEmailId = contEmailId;
    }

    public ContactDTO(String contId, String compName, String contTitle, String contFirstName, String contLastName, String contJobTitle, String contPhoneNum, String contMobileNum, String contEmailId, String compId, String contProfilePic, String contPhotoId) {

        this.contId = contId;

        this.contMobileNum = contMobileNum;
        this.compName = compName;
        this.contTitle = contTitle;
        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contJobTitle = contJobTitle;
        this.contPhoneNum = contPhoneNum;
        this.contEmailId = contEmailId;
        this.compId = compId;
        this.contProfilePic = contProfilePic;
        this.contPhotoId = contPhotoId;
    }

    public ContactDTO(String contId, String name, String cont_num, String comp_name, String email_id) {
        super();
        this.contId = contId;
        this.contName = name;
        this.contMobileNum = cont_num;
        this.compName = comp_name;
        this.contEmailId = email_id;

    }


}
