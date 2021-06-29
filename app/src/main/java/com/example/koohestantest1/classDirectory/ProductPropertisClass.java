package com.example.koohestantest1.classDirectory;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductPropertisClass implements Parcelable {
    private String ProductID;
    private String PropertisGroup;
    private String PropertisName;
    private String PropertisValue;
    private String UpdatTime;

    public ProductPropertisClass(String productID, String propertisGroup, String propertisName, String propertisValue, String updatTime) {
        ProductID = productID;
        PropertisGroup = propertisGroup;
        PropertisName = propertisName;
        PropertisValue = propertisValue;
        UpdatTime = updatTime;
    }

    protected ProductPropertisClass(Parcel in) {
        ProductID = in.readString();
        PropertisGroup = in.readString();
        PropertisName = in.readString();
        PropertisValue = in.readString();
        UpdatTime = in.readString();
    }

    public static final Creator<ProductPropertisClass> CREATOR = new Creator<ProductPropertisClass>() {
        @Override
        public ProductPropertisClass createFromParcel(Parcel in) {
            return new ProductPropertisClass(in);
        }

        @Override
        public ProductPropertisClass[] newArray(int size) {
            return new ProductPropertisClass[size];
        }
    };

    public String getProductID() {
        return ProductID;
    }

    public String getPropertisGroup() {
        return PropertisGroup;
    }

    public String getPropertisName() {
        return PropertisName;
    }

    public String getPropertisValue() {
        return PropertisValue;
    }

    public String getUpdatTime() {
        return UpdatTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ProductID);
        dest.writeString(PropertisGroup);
        dest.writeString(PropertisName);
        dest.writeString(PropertisValue);
        dest.writeString(UpdatTime);
    }
}
