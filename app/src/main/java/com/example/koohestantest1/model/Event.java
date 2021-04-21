package com.example.koohestantest1.model;

public class Event {
    private boolean UserMSG;
    private boolean CompanyMSG;
    private boolean CompanyOrder;
    private boolean UserOrder;
    private boolean Notify;
    private boolean calls;
    private boolean NewCompany;
    private boolean EmployeeUpdate;
    private boolean ErrorReport;
    private boolean ErrorReportAnswer;
    private boolean NewProduct;
    private boolean UserProfileChange;
    private boolean CompanyProfileChange;
    private boolean Comment;
    private boolean Address;
    private boolean NewVersion;
    private boolean OtherPersoLoginWhitthisUser;
    private boolean MustLogOut;
    private boolean AppMustUpdate;


    public void setUserMSG(boolean userMSG) {
        UserMSG = userMSG;
    }

    public void setCompanyMSG(boolean companyMSG) {
        CompanyMSG = companyMSG;
    }

    public void setCompanyOrder(boolean companyOrder) {
        CompanyOrder = companyOrder;
    }

    public void setUserOrder(boolean userOrder) {
        UserOrder = userOrder;
    }

    public void setNotify(boolean notify) {
        Notify = notify;
    }

    public void setCalls(boolean calls) {
        this.calls = calls;
    }

    public void setNewCompany(boolean newCompany) {
        NewCompany = newCompany;
    }

    public void setEmployeeUpdate(boolean employeeUpdate) {
        EmployeeUpdate = employeeUpdate;
    }

    public void setErrorReport(boolean errorReport) {
        ErrorReport = errorReport;
    }

    public void setErrorReportAnswer(boolean errorReportAnswer) {
        ErrorReportAnswer = errorReportAnswer;
    }

    public void setNewProduct(boolean newProduct) {
        NewProduct = newProduct;
    }

    public void setUserProfileChange(boolean userProfileChange) {
        UserProfileChange = userProfileChange;
    }

    public void setCompanyProfileChange(boolean companyProfileChange) {
        CompanyProfileChange = companyProfileChange;
    }

    public void setComment(boolean comment) {
        Comment = comment;
    }

    public void setAddress(boolean address) {
        Address = address;
    }

    public void setNewVersion(boolean newVersion) {
        NewVersion = newVersion;
    }

    public void setOtherPersoLoginWhitthisUser(boolean otherPersoLoginWhitthisUser) {
        OtherPersoLoginWhitthisUser = otherPersoLoginWhitthisUser;
    }

    public void setMustLogOut(boolean mustLogOut) {
        MustLogOut = mustLogOut;
    }

    public void setAppMustUpdate(boolean appMustUpdate) {
        AppMustUpdate = appMustUpdate;
    }

    public boolean isUserMSG() {
        return UserMSG;
    }

    public boolean isCompanyMSG() {
        return CompanyMSG;
    }

    public boolean isCompanyOrder() {
        return CompanyOrder;
    }

    public boolean isUserOrder() {
        return UserOrder;
    }

    public boolean isNotify() {
        return Notify;
    }

    public boolean isCalls() {
        return calls;
    }

    public boolean isNewCompany() {
        return NewCompany;
    }

    public boolean isEmployeeUpdate() {
        return EmployeeUpdate;
    }

    public boolean isErrorReport() {
        return ErrorReport;
    }

    public boolean isErrorReportAnswer() {
        return ErrorReportAnswer;
    }

    public boolean isNewProduct() {
        return NewProduct;
    }

    public boolean isUserProfileChange() {
        return UserProfileChange;
    }

    public boolean isCompanyProfileChange() {
        return CompanyProfileChange;
    }

    public boolean isComment() {
        return Comment;
    }

    public boolean isAddress() {
        return Address;
    }

    public boolean isNewVersion() {
        return NewVersion;
    }

    public boolean isOtherPersoLoginWhitthisUser() {
        return OtherPersoLoginWhitthisUser;
    }

    public boolean isMustLogOut() {
        return MustLogOut;
    }

    public boolean isAppMustUpdate() {
        return AppMustUpdate;
    }
}
