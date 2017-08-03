package com.pluggdd.ijbt.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TotalCatDetails{

    private List<Totalcatdetail> totalcatdetails = new ArrayList<Totalcatdetail>();
    private List<Currency> currencies = new ArrayList<Currency>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The totalcatdetails
     */
    public List<Totalcatdetail> getTotalcatdetails() {
        return totalcatdetails;
    }

    /**
     *
     * @param totalcatdetails
     * The totalcatdetails
     */
    public void setTotalcatdetails(List<Totalcatdetail> totalcatdetails) {
        this.totalcatdetails = totalcatdetails;
    }

    /**
     *
     * @return
     * The currencies
     */
    public List<Currency> getCurrencies() {
        return currencies;
    }

    /**
     *
     * @param currencies
     * The currencies
     */
    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}