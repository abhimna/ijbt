package com.pluggdd.ijbt.model;


import java.util.HashMap;
import java.util.Map;

public class Currency{

    private String iso_code;
    private String symbol;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Currency(String isoCode,String symbol)
    {
        this.iso_code = isoCode;
        this.symbol = symbol;
    }
    /**
     *
     * @return
     * The isoCode
     */
    public String getIsoCode() {
        return iso_code;
    }

    /**
     *
     * @param isoCode
     * The iso_code
     */
    public void setIsoCode(String isoCode) {
        this.iso_code = isoCode;
    }

    /**
     *
     * @return
     * The symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @param symbol
     * The symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}