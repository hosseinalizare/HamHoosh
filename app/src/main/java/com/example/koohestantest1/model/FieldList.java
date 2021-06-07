
package com.example.koohestantest1.model;
import com.google.gson.annotations.SerializedName;

public class FieldList {

    @SerializedName("ClickActionType")
    private int mClickActionType;
    @SerializedName("EXtraData")
    private String mEXtraData;
    @SerializedName("Explain")
    private String mExplain;
    @SerializedName("FieldDate")
    private String mFieldDate;
    @SerializedName("id")
    private String mId;
    @SerializedName("Title")
    private String mTitle;
    @SerializedName("valuType")
    private int mValuType;
    @SerializedName("Value")
    private String mValue;

    public int getClickActionType() {
        return mClickActionType;
    }

    public void setClickActionType(int clickActionType) {
        mClickActionType = clickActionType;
    }

    public String getEXtraData() {
        return mEXtraData;
    }

    public void setEXtraData(String eXtraData) {
        mEXtraData = eXtraData;
    }

    public String getExplain() {
        return mExplain;
    }

    public void setExplain(String explain) {
        mExplain = explain;
    }

    public String getFieldDate() {
        return mFieldDate;
    }

    public void setFieldDate(String fieldDate) {
        mFieldDate = fieldDate;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getValuType() {
        return mValuType;
    }

    public void setValuType(int valuType) {
        mValuType = valuType;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}
