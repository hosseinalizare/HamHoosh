
package com.example.koohestantest1.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("AllFieldCount")
    private Long mAllFieldCount;
    @SerializedName("ClickAble")
    private Boolean mClickAble;
    @SerializedName("FieldList")
    private List<FieldList> mFieldList;
    @SerializedName("GropFieldByDate")
    private Boolean mGropFieldByDate;
    @SerializedName("GroupType")
    private Long mGroupType;
    @SerializedName("HasMoredata")
    private Boolean mHasMoredata;
    @SerializedName("ItemPreviewType")
    private Long mItemPreviewType;
    @SerializedName("ItemVerticalDirection")
    private Boolean mItemVerticalDirection;
    @SerializedName("Name")
    private String mName;
    @SerializedName("ShowItemPreview")
    private Boolean mShowItemPreview;

    public Long getAllFieldCount() {
        return mAllFieldCount;
    }

    public void setAllFieldCount(Long allFieldCount) {
        mAllFieldCount = allFieldCount;
    }

    public Boolean getClickAble() {
        return mClickAble;
    }

    public void setClickAble(Boolean clickAble) {
        mClickAble = clickAble;
    }

    public List<FieldList> getFieldList() {
        return mFieldList;
    }

    public void setFieldList(List<FieldList> fieldList) {
        mFieldList = fieldList;
    }

    public Boolean getGropFieldByDate() {
        return mGropFieldByDate;
    }

    public void setGropFieldByDate(Boolean gropFieldByDate) {
        mGropFieldByDate = gropFieldByDate;
    }

    public Long getGroupType() {
        return mGroupType;
    }

    public void setGroupType(Long groupType) {
        mGroupType = groupType;
    }

    public Boolean getHasMoredata() {
        return mHasMoredata;
    }

    public void setHasMoredata(Boolean hasMoredata) {
        mHasMoredata = hasMoredata;
    }

    public Long getItemPreviewType() {
        return mItemPreviewType;
    }

    public void setItemPreviewType(Long itemPreviewType) {
        mItemPreviewType = itemPreviewType;
    }

    public Boolean getItemVerticalDirection() {
        return mItemVerticalDirection;
    }

    public void setItemVerticalDirection(Boolean itemVerticalDirection) {
        mItemVerticalDirection = itemVerticalDirection;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Boolean getShowItemPreview() {
        return mShowItemPreview;
    }

    public void setShowItemPreview(Boolean showItemPreview) {
        mShowItemPreview = showItemPreview;
    }

}
