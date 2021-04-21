package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

public class CompanySetting {
    @SerializedName("UserID")
    private String userId;

    @SerializedName("Token")
    private String token;

    @SerializedName("CompanyId")
    private String companyId;

    @SerializedName("Value")
    private String value;

    @SerializedName("Title")
    private String title;

    @SerializedName("Explain")
    private String explain;

    @SerializedName("EnumId")
    private int enumId;

    @SerializedName("valuType")
    private int valueType;

    @SerializedName("MustPayToActive")
    private boolean purchasableOption;

    @SerializedName("EditAble")
    private boolean editable;

    @SerializedName("Protect")
    private boolean protectedOption;

    @SerializedName("groupType")
    private int groupType;

    @SerializedName("AlowNull")
    private boolean nullable;

    public CompanySetting(String userId, String token, String companyId, String value, String title, String explain, int enumId, int valueType,
                          boolean purchasableOption, boolean editable, boolean protectedOption, int groupType, boolean nullable) {
        this.userId = userId;
        this.token = token;
        this.companyId = companyId;
        this.value = value;
        this.title = title;
        this.explain = explain;
        this.enumId = enumId;
        this.valueType = valueType;
        this.purchasableOption = purchasableOption;
        this.editable = editable;
        this.protectedOption = protectedOption;
        this.groupType = groupType;
        this.nullable = nullable;
    }

    public void setValue(String value){
        this.value = value;
    }
    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public String getExplain() {
        return explain;
    }

    public int getEnumId() {
        return enumId;
    }

    public int getValueType() {
        return valueType;
    }

    public boolean isPurchasableOption() {
        return purchasableOption;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isProtectedOption() {
        return protectedOption;
    }

    public int getGroupType() {
        return groupType;
    }

    public boolean isNullable() {
        return nullable;
    }

    @Override
    public String toString() {
        return "CompanySetting{" +
                "userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", companyId='" + companyId + '\'' +
                ", value='" + value + '\'' +
                ", title='" + title + '\'' +
                ", explain='" + explain + '\'' +
                ", enumId=" + enumId +
                ", valueType=" + valueType +
                ", purchasableOption=" + purchasableOption +
                ", editable=" + editable +
                ", protectedOption=" + protectedOption +
                ", groupType=" + groupType +
                ", nullable=" + nullable +
                '}';
    }
}
