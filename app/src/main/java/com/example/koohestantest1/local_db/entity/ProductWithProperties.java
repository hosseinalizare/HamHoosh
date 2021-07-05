package com.example.koohestantest1.local_db.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ProductWithProperties {

    @Embedded
    Product product;

    @Relation(parentColumn = "product_id",entityColumn = "p_id")
    List<Properties> propertiesList;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Properties> getPropertiesList() {
        return propertiesList;
    }

    public void setPropertiesList(List<Properties> propertiesList) {
        this.propertiesList = propertiesList;
    }
}
