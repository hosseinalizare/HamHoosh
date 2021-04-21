package com.example.koohestantest1.classDirectory;

public class CompanyEmployeesClass {

    private String EmployeeID;
    private String UserID;
    private String Position;
    private String AccessLlevel;
    private String Notes;

    public CompanyEmployeesClass(String employeeID, String userID, String position, String accessLlevel, String notes) {
        EmployeeID = employeeID;
        UserID = userID;
        Position = position;
        AccessLlevel = accessLlevel;
        Notes = notes;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getAccessLlevel() {
        return AccessLlevel;
    }

    public void setAccessLlevel(String accessLlevel) {
        AccessLlevel = accessLlevel;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}
