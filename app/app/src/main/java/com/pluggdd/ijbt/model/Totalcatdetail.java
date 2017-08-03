package com.pluggdd.ijbt.model;



import java.util.HashMap;
import java.util.Map;

public class Totalcatdetail {

    private String categoryid;
    private String Categorynam;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Totalcatdetail(String categoryid, String categorynam) {
        this.categoryid = categoryid;
        this.Categorynam = categorynam;
    }

    /**
     * @return The categoryid
     */
    public String getCategoryid() {
        return categoryid;
    }

    /**
     * @param categoryid The Categoryid
     */
    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    /**
     * @return The categorynam
     */
    public String getCategorynam() {
        return Categorynam;
    }

    /**
     * @param categorynam The Categorynam
     */
    public void setCategorynam(String categorynam) {
        this.Categorynam = categorynam;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}