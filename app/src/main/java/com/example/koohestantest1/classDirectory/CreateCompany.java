package com.example.koohestantest1.classDirectory;

public class CreateCompany {
    public String Token;
    public String UserID ;
    public String CompanyID ;
    public String CompanyName ;
    public String Notes ;
    public String UserName ;
    public String PassWord ;
    public String Position;
    public short AccessLlevel ;

    public CreateCompany(String token, String userID, String companyID, String companyName,
                         String notes, String userName, String passWord, String position,
                         short accessLlevel) {
        Token = token;
        UserID = userID;
        CompanyID = companyID;
        CompanyName = companyName;
        Notes = notes;
        UserName = userName;
        PassWord = passWord;
        Position = position;
        AccessLlevel = accessLlevel;
    }
}
