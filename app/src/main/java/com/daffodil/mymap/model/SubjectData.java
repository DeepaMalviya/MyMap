package com.daffodil.mymap.model;

/**
 * Created by Daffodil on 11/7/2019.
 */

 public class SubjectData {
    String SubjectName;
    String Link;
    int Image;
    public SubjectData(String subjectName, String link, int image) {
        this.SubjectName = subjectName;
        this.Link = link;
        this.Image = image;
    }

    public SubjectData(String subjectName, String link) {
        SubjectName = subjectName;
        Link = link;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}