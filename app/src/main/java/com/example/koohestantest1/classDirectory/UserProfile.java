package com.example.koohestantest1.classDirectory;

public class UserProfile {

    private String MobilePhone;
    private String PID;
    private String FirstName;
    private String LastName;
    private String EmailAddress;
    private String HomePhone;
    private String BusinessPhone;
    private String NationalCode;
    private String CompanyID;
    private String JobTitle;
    private String FaxNumber;
    private String Country;
    private String City;
    private String State;
    private String PostalCode;
    private String Area;
    private String Location;
    private String Address;
    private String Bio;
    private String WebPage;
    private String Referredby;
    private String Hometown;
    private String Birthdate;
    private String MaritalStatus;
    private String ImageAttachments;
    private String IsOnline;
    private String LastSeen;
    private String Privasy;
    private String ErrorMsg;
    private String UserName;
    private boolean HasProfile;

    public UserProfile(String mobilePhone, String PID, String firstName, String lastName, String emailAddress, String homePhone, String businessPhone, String nationalCode, String companyID, String jobTitle, String faxNumber, String country, String city, String state, String postalCode, String area, String location, String address, String bio, String webPage, String referredby, String hometown, String birthdate, String maritalStatus, String imageAttachments, String isOnline,
                       String lastSeen, String privasy, String errorMsg, String userName, boolean hasProfile) {
        MobilePhone = mobilePhone;
        this.PID = PID;
        FirstName = firstName;
        LastName = lastName;
        EmailAddress = emailAddress;
        HomePhone = homePhone;
        BusinessPhone = businessPhone;
        NationalCode = nationalCode;
        CompanyID = companyID;
        JobTitle = jobTitle;
        FaxNumber = faxNumber;
        Country = country;
        City = city;
        State = state;
        PostalCode = postalCode;
        Area = area;
        Location = location;
        Address = address;
        Bio = bio;
        WebPage = webPage;
        Referredby = referredby;
        Hometown = hometown;
        Birthdate = birthdate;
        MaritalStatus = maritalStatus;
        ImageAttachments = imageAttachments;
        IsOnline = isOnline;
        LastSeen = lastSeen;
        Privasy = privasy;
        ErrorMsg = errorMsg;
        UserName = userName;
        HasProfile = hasProfile;
    }
    //    public UserProfile(String mobilePhone, String PID, String firstName, String lastName, String emailAddress, String homePhone, String businessPhone, String nationalCode, String companyID, String jobTitle, String faxNumber, String country, String city, String state, String postalCode, String area, String location, String address, String bio, String webPage, String referredby, String hometown, String birthdate, String maritalStatus, String imageAttachments, String isOnline, String lastSeen, String privasy, String errorMsg) {
//        MobilePhone = mobilePhone;
//        this.PID = PID;
//        FirstName = firstName;
//        LastName = lastName;
//        EmailAddress = emailAddress;
//        HomePhone = homePhone;
//        BusinessPhone = businessPhone;
//        NationalCode = nationalCode;
//        CompanyID = companyID;
//        JobTitle = jobTitle;
//        FaxNumber = faxNumber;
//        Country = country;
//        City = city;
//        State = state;
//        PostalCode = postalCode;
//        Area = area;
//        Location = location;
//        Address = address;
//        Bio = bio;
//        WebPage = webPage;
//        Referredby = referredby;
//        Hometown = hometown;
//        Birthdate = birthdate;
//        MaritalStatus = maritalStatus;
//        ImageAttachments = imageAttachments;
//        IsOnline = isOnline;
//        LastSeen = lastSeen;
//        Privasy = privasy;
//        ErrorMsg = errorMsg;
//    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getHomePhone() {
        return HomePhone;
    }

    public void setHomePhone(String homePhone) {
        HomePhone = homePhone;
    }

    public String getBusinessPhone() {
        return BusinessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        BusinessPhone = businessPhone;
    }

    public String getNationalCode() {
        return NationalCode;
    }

    public void setNationalCode(String nationalCode) {
        NationalCode = nationalCode;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getFaxNumber() {
        return FaxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        FaxNumber = faxNumber;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
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

    public String getHometown() {
        return Hometown;
    }

    public void setHometown(String hometown) {
        Hometown = hometown;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(String birthdate) {
        Birthdate = birthdate;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        MaritalStatus = maritalStatus;
    }

    public String getImageAttachments() {
        return ImageAttachments;
    }

    public void setImageAttachments(String imageAttachments) {
        ImageAttachments = imageAttachments;
    }

    public String getIsOnline() {
        return IsOnline;
    }

    public void setIsOnline(String isOnline) {
        IsOnline = isOnline;
    }

    public String getLastSeen() {
        return LastSeen;
    }

    public void setLastSeen(String lastSeen) {
        LastSeen = lastSeen;
    }

    public String getPrivasy() {
        return Privasy;
    }

    public void setPrivasy(String privasy) {
        Privasy = privasy;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public boolean isHasProfile() {
        return HasProfile;
    }

    public void setHasProfile(boolean hasProfile) {
        HasProfile = hasProfile;
    }
}
