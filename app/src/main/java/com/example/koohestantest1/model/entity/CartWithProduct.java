package com.example.koohestantest1.model.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CartWithProduct {
    @Embedded
    public CartInformation cartInformation;
    @Relation(parentColumn = "cartId"  , entityColumn = "cartId")
    public List<CartProduct> cartProducts;
}
