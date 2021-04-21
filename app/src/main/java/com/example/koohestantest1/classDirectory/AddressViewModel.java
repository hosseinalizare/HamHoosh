package com.example.koohestantest1.classDirectory;

public class AddressViewModel {
    private String id;
    private  String UserId;
    private String Token;
    private String ObjectID;
    private String Country;
    private String City;
    private String State;
    private String PostalCode;
    private String Area;
    private String Location;
    private String Neighborhood;
    private String Pelak;
    private String Vahed;
    private String Address1;
    private String PhoneNumber;

    public AddressViewModel(String id, String userId, String token, String objectID, String country, String city,
                            String state, String postalCode, String area, String location, String neighborhood, String pelak,
                            String vahed, String address1, String phoneNumber) {
        this.id = id;
        UserId = userId;
        Token = token;
        ObjectID = objectID;
        Country = country;
        City = city;
        State = state;
        PostalCode = postalCode;
        Area = area;
        Location = location;
        Neighborhood = neighborhood;
        Pelak = pelak;
        Vahed = vahed;
        Address1 = address1;
        PhoneNumber = phoneNumber;
    }

    public AddressViewModel(String userId, String token, String objectID, String id) {
        UserId = userId;
        Token = token;
        ObjectID = objectID;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return UserId;
    }

    public String getToken() {
        return Token;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public String getCountry() {
        return Country;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public String getArea() {
        return Area;
    }

    public String getLocation() {
        return Location;
    }

    public String getNeighborhood() {
        return Neighborhood;
    }

    public String getPelak() {
        return Pelak;
    }

    public String getVahed() {
        return Vahed;
    }

    public String getAddress1() {
        return Address1;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }
}
