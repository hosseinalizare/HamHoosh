package com.example.koohestantest1.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsLetterModel {
    @SerializedName("Token")
    private String token;

    @SerializedName("UserID")
    private String userId;

    @SerializedName("CompanyID")
    private String companyId;

    @SerializedName("NewsTitle")
    private String newsTitle;

    @SerializedName("NewsDescription")
    private String newsDescription;

    @SerializedName("Category")
    private String category;

    @SerializedName("Show")
    private boolean show;

    @SerializedName("Spare1")
    private String spare1;

    @SerializedName("Spare2")
    private String spare2;

    @SerializedName("Spare3")
    private String spare3;

    @SerializedName("ActiveLike")
    private boolean activeLike;

    @SerializedName("ActiveComment")
    private boolean activeComment;

    @SerializedName("ActiveSave")
    private boolean activeSave;

    @SerializedName("CreatorUserID")
    private String creatorId;

    @SerializedName("LinkOut")
    private String linkOut;

    @SerializedName("LinkToInstagram")
    private String linkToInstagram;

    @SerializedName("src")
    private List<String> src;

    @SerializedName("ViewedCount")
    private int viewedCount;

    @SerializedName("LikeCount")
    private int likeCount;

    @SerializedName("SaveCount")
    private int saveCount;

    @SerializedName("Likeit")
    private boolean likeIt;

    @SerializedName("Saveit")
    private boolean saveIt;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getSpare1() {
        return spare1;
    }

    public void setSpare1(String spare1) {
        this.spare1 = spare1;
    }

    public String getSpare2() {
        return spare2;
    }

    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }

    public String getSpare3() {
        return spare3;
    }

    public void setSpare3(String spare3) {
        this.spare3 = spare3;
    }

    public boolean isActiveLike() {
        return activeLike;
    }

    public void setActiveLike(boolean activeLike) {
        this.activeLike = activeLike;
    }

    public boolean isActiveComment() {
        return activeComment;
    }

    public void setActiveComment(boolean activeComment) {
        this.activeComment = activeComment;
    }

    public boolean isActiveSave() {
        return activeSave;
    }

    public void setActiveSave(boolean activeSave) {
        this.activeSave = activeSave;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getLinkOut() {
        return linkOut;
    }

    public void setLinkOut(String linkOut) {
        this.linkOut = linkOut;
    }

    public String getLinkToInstagram() {
        return linkToInstagram;
    }

    public void setLinkToInstagram(String linkToInstagram) {
        this.linkToInstagram = linkToInstagram;
    }

    public List<String> getSrc() {
        return src;
    }

    public void setSrc(List<String> src) {
        this.src = src;
    }

    public int getViewedCount() {
        return viewedCount;
    }

    public void setViewedCount(int viewedCount) {
        this.viewedCount = viewedCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getSaveCount() {
        return saveCount;
    }

    public void setSaveCount(int saveCount) {
        this.saveCount = saveCount;
    }

    public boolean isLikeIt() {
        return likeIt;
    }

    public void setLikeIt(boolean likeIt) {
        this.likeIt = likeIt;
    }

    public boolean isSaveIt() {
        return saveIt;
    }

    public void setSaveIt(boolean saveIt) {
        this.saveIt = saveIt;
    }
}
