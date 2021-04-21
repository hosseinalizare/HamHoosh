package com.example.koohestantest1.classDirectory;

import java.sql.Date;

public class CompanyProfile {
    private String UserID;
    private String Token;
    private String CompanyID;
    private String CompanyName;
    private String Bio;
    private String EmailAddress;
    private String MobilePhone;
    private String BusinessPhone;
    private String FaxNumber;
    private String NationalCode;
    private String RegistrationNumber;
    private Date DateOfRegistration;
    private String EconomicaNumber;
    private Date CreationDate;
    private String WebPage;
    private String Referredby;
    private String Privasy;
    private String Union;
    private String ObjectID;
    private String Country;
    private String City ;
    private String State;
    private String PostalCode;
    private String Area;
    private String Location ;
    private String Neighborhood ;
    private String Address;
    public String CompanyType;
    public String UpdateDate;
    private String Deleted;
    private String HasCourier;
    private String StartTime;
    private String EndTime;
    private String ActiveDayID;
    private String Spare1;
    private String Spare2;
    private String Spare3;
    private String CompanyOwnerID;
    private String TimeToCourier;
    private String FollowingCount;
    private String ProductCount;
    private String OrderCount;

    public CompanyProfile(String userID, String token, String companyID, String companyName, String bio, String emailAddress, String mobilePhone, String businessPhone, String faxNumber, String nationalCode, String registrationNumber, Date dateOfRegistration, String economicaNumber, Date creationDate, String webPage, String referredby, String privasy, String union, String objectID, String country, String city, String state, String postalCode, String area, String location, String neighborhood, String address) {
        UserID = userID;
        Token = token;
        CompanyID = companyID;
        CompanyName = companyName;
        Bio = bio;
        EmailAddress = emailAddress;
        MobilePhone = mobilePhone;
        BusinessPhone = businessPhone;
        FaxNumber = faxNumber;
        NationalCode = nationalCode;
        RegistrationNumber = registrationNumber;
        DateOfRegistration = dateOfRegistration;
        EconomicaNumber = economicaNumber;
        CreationDate = creationDate;
        WebPage = webPage;
        Referredby = referredby;
        Privasy = privasy;
        Union = union;
        ObjectID = objectID;
        Country = country;
        City = city;
        State = state;
        PostalCode = postalCode;
        Area = area;
        Location = location;
        Neighborhood = neighborhood;
        Address = address;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getBusinessPhone() {
        return BusinessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        BusinessPhone = businessPhone;
    }

    public String getFaxNumber() {
        return FaxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        FaxNumber = faxNumber;
    }

    public String getNationalCode() {
        return NationalCode;
    }

    public void setNationalCode(String nationalCode) {
        NationalCode = nationalCode;
    }

    public String getRegistrationNumber() {
        return RegistrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        RegistrationNumber = registrationNumber;
    }

    public Date getDateOfRegistration() {
        return DateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        DateOfRegistration = dateOfRegistration;
    }

    public String getEconomicaNumber() {
        return EconomicaNumber;
    }

    public void setEconomicaNumber(String economicaNumber) {
        EconomicaNumber = economicaNumber;
    }

    public Date getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(Date creationDate) {
        CreationDate = creationDate;
    }

    public String getWebPage() {
        return WebPage;
    }

    public void setWebPage(String webPage) {
        WebPage = webPage;
    }

    public String getReferredby() {
        return Referredby;
    }

    public void setReferredby(String referredby) {
        Referredby = referredby;
    }

    public String getPrivasy() {
        return Privasy;
    }

    public void setPrivasy(String privasy) {
        Privasy = privasy;
    }

    public String getUnion() {
        return Union;
    }

    public void setUnion(String union) {
        Union = union;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String objectID) {
        ObjectID = objectID;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getNeighborhood() {
        return Neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        Neighborhood = neighborhood;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCompanyType() {
        return CompanyType;
    }

    public void setCompanyType(String companyType) {
        CompanyType = companyType;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getDeleted() {
        return Deleted;
    }

    public void setDeleted(String deleted) {
        Deleted = deleted;
    }

    public String getHasCourier() {
        return HasCourier;
    }

    public void setHasCourier(String hasCourier) {
        HasCourier = hasCourier;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getActiveDayID() {
        return ActiveDayID;
    }

    public void setActiveDayID(String activeDayID) {
        ActiveDayID = activeDayID;
    }

    public String getSpare1() {
        return Spare1;
    }

    public void setSpare1(String spare1) {
        Spare1 = spare1;
    }

    public String getSpare2() {
        return Spare2;
    }

    public void setSpare2(String spare2) {
        Spare2 = spare2;
    }

    public String getSpare3() {
        return Spare3;
    }

    public void setSpare3(String spare3) {
        Spare3 = spare3;
    }

    public String getCompanyOwnerID() {
        return CompanyOwnerID;
    }

    public void setCompanyOwnerID(String companyOwnerID) {
        CompanyOwnerID = companyOwnerID;
    }

    public String getTimeToCourier() {
        return TimeToCourier;
    }

    public void setTimeToCourier(String timeToCourier) {
        TimeToCourier = timeToCourier;
    }

    public String getFollowingCount() {
        return FollowingCount;
    }

    public void setFollowingCount(String followingCount) {
        FollowingCount = followingCount;
    }

    public String getProductCount() {
        return ProductCount;
    }

    public void setProductCount(String productCount) {
        ProductCount = productCount;
    }

    public String getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(String orderCount) {
        OrderCount = orderCount;
    }
}
