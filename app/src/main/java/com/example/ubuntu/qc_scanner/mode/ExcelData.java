package com.example.ubuntu.qc_scanner.mode;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ubuntu on 17-8-15.
 */

public class ExcelData implements Serializable {
    public String id;
    public String groupID;
    public String numberID;
    public String dateTime;
    public String valleyValue;
    public String peakValue;
    public String totalValue;

    public ExcelData(String id, String groupID, String numberID, String dateTime,String valleyValue,String peakValue, String totalValue) {
        this.id = id;
        this.groupID = groupID;
        this.numberID = numberID;
        this.dateTime = dateTime;
        this.valleyValue = valleyValue;
        this.peakValue = peakValue;
        this.totalValue = totalValue;
    }

    public ExcelData(JSONObject obj) {
        this.id = obj.optString("ID");
        this.groupID = obj.optString("groupID");
        this.numberID = obj.optString("numberID");
        this.dateTime = obj.optString("dateTime");
        this.valleyValue = obj.optString("valleyValue");
        this.peakValue = obj.optString("peakValue");
        this.totalValue = obj.optString("totalValue");
    }
}
