package com.daffodil.mymap.other.dto;


public class Companydto {
    private String comp_name, c_add, c_city, c_state, c_pincode, c_country, lat, log, contact_no, comp_id, parent_id, creator_id, name, phno, mobno, emailid, note, photo;
    private String compLogo, compWebsite, compEmailId, compIndustry, compPhoneNo, contTitle, contFirstName, contLastName, contJobTitle, contProfilePic, contId;

    public Companydto(String comp_id, String comp_name) {
        this.comp_name = comp_name;
        this.comp_id = comp_id;
    }

    public String getCompLogo() {
        return compLogo;
    }

    public void setCompLogo(String compLogo) {
        this.compLogo = compLogo;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

    public String getContProfilePic() {
        return contProfilePic;
    }

    public void setContProfilePic(String contProfilePic) {
        this.contProfilePic = contProfilePic;
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

    public String getCompWebsite() {
        return compWebsite;
    }

    public void setCompWebsite(String compWebsite) {
        this.compWebsite = compWebsite;
    }

    public String getCompEmailId() {
        return compEmailId;
    }

    public void setCompEmailId(String compEmailId) {
        this.compEmailId = compEmailId;
    }

    public String getCompIndustry() {
        return compIndustry;
    }

    public void setCompIndustry(String compIndustry) {
        this.compIndustry = compIndustry;
    }

    public String getCompPhoneNo() {
        return compPhoneNo;
    }

    public void setCompPhoneNo(String compPhoneNo) {
        this.compPhoneNo = compPhoneNo;
    }

    public Companydto(String comp_name, String c_add, String c_city, String c_state, String c_pincode, String c_country, String photo, String note, String emailid, String mobno, String phno, String name, String creator_id, String parent_id, String comp_id, String contact_no, String log, String lat) {
        this.comp_name = comp_name;
        this.c_add = c_add;
        this.c_city = c_city;
        this.c_state = c_state;
        this.c_pincode = c_pincode;
        this.c_country = c_country;
        this.photo = photo;
        this.note = note;
        this.emailid = emailid;
        this.mobno = mobno;
        this.phno = phno;
        this.name = name;
        this.creator_id = creator_id;
        this.parent_id = parent_id;
        this.comp_id = comp_id;
        this.contact_no = contact_no;
        this.log = log;
        this.lat = lat;
    }

    //Used in Company activity,store information for edit company detail.
    public Companydto(String comp_name, String c_add, String c_city, String c_state, String c_pincode, String c_country, String compWebsite, String compPhoneNo, String compEmailId, String compIndustry, String contTitle, String contFirstName, String contLastName, String contJobTitle, String p_no, String m_no, String e_id, String cont_note, String lat, String log) {

        this.comp_name = comp_name;
        this.c_add = c_add;
        this.c_city = c_city;
        this.c_state = c_state;
        this.c_pincode = c_pincode;
        this.c_country = c_country;
        this.compWebsite = compWebsite;
        this.compPhoneNo = compPhoneNo;
        this.compEmailId = compEmailId;
        this.compIndustry = compIndustry;
        this.contTitle = contTitle;

        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contJobTitle = contJobTitle;

        this.mobno = m_no;
        this.phno = p_no;
        this.emailid = e_id;
        this.note = cont_note;

        this.log = log;
        this.lat = lat;


    }

    public Companydto(String compId, String comp_name, String c_add, String c_city, String c_state, String c_pincode, String c_country, String compWebsite, String compPhoneNo, String compEmailId, String compIndustry, String contTitle, String contFirstName, String contLastName, String contJobTitle, String p_no, String m_no, String e_id, String cont_note, String lat, String log) {
        this.comp_id = compId;
        this.comp_name = comp_name;
        this.c_add = c_add;
        this.c_city = c_city;
        this.c_state = c_state;
        this.c_pincode = c_pincode;
        this.c_country = c_country;
        this.compWebsite = compWebsite;
        this.compPhoneNo = compPhoneNo;
        this.compEmailId = compEmailId;
        this.compIndustry = compIndustry;
        this.contTitle = contTitle;

        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contJobTitle = contJobTitle;

        this.mobno = m_no;
        this.phno = p_no;
        this.emailid = e_id;
        this.note = cont_note;

        this.log = log;
        this.lat = lat;


    }

    public Companydto(String compId, String comp_name, String c_add, String c_city, String c_state, String c_pincode, String c_country, String compWebsite, String compPhoneNo, String compEmailId, String compIndustry, String contTitle, String contFirstName, String contLastName, String contJobTitle, String p_no, String m_no, String e_id, String cont_note, String lat, String log, String photo, String contId, String compLogo) {
        this.comp_id = compId;
        this.comp_name = comp_name;
        this.c_add = c_add;
        this.c_city = c_city;
        this.c_state = c_state;
        this.c_pincode = c_pincode;
        this.c_country = c_country;
        this.compWebsite = compWebsite;
        this.compPhoneNo = compPhoneNo;
        this.compEmailId = compEmailId;
        this.compIndustry = compIndustry;
        this.contTitle = contTitle;

        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contJobTitle = contJobTitle;

        this.mobno = m_no;
        this.phno = p_no;
        this.emailid = e_id;
        this.note = cont_note;

        this.log = log;
        this.lat = lat;
        this.photo = photo;
        this.contId = contId;
        this.compLogo = compLogo;


    }


    public Companydto() {
        super();
        // TODO Auto-generated constructor stub
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

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
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

    public Companydto(String comp_name, String c_add, String c_city,
                      String c_state, String c_pincode, String c_country, String lat,
                      String log) {
        super();
        this.comp_name = comp_name;
        this.c_add = c_add;
        this.c_city = c_city;
        this.c_state = c_state;
        this.c_pincode = c_pincode;
        this.c_country = c_country;
        this.lat = lat;
        this.log = log;
    }

    public String getC_add() {
        return c_add;
    }

    public void setC_add(String c_add) {
        this.c_add = c_add;
    }

    public String getC_city() {
        return c_city;
    }

    public void setC_city(String c_city) {
        this.c_city = c_city;
    }

    public String getC_state() {
        return c_state;
    }

    public void setC_state(String c_state) {
        this.c_state = c_state;
    }

    public String getC_pincode() {
        return c_pincode;
    }

    public void setC_pincode(String c_pincode) {
        this.c_pincode = c_pincode;
    }

    public String getC_country() {
        return c_country;
    }

    public void setC_country(String c_country) {
        this.c_country = c_country;
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

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public Companydto(String comp_name, String contact_no, String comp_id) {
        super();
        this.comp_id = comp_id;
        this.comp_name = comp_name;
        this.contact_no = contact_no;
    }

    public Companydto(String comp_name, String contact_no, String comp_id, String email_id, String compWebsite) {
        super();
        this.comp_id = comp_id;
        this.comp_name = comp_name;
        this.compPhoneNo = contact_no;
        this.compEmailId = email_id;
        this.compWebsite = compWebsite;
    }


}
