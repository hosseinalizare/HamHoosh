package com.example.koohestantest1.classDirectory;

import android.content.Context;

import java.util.List;

import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

public class AllProductData {

    public Context mContext;
    private String companyName;
    private boolean selectedToCart;
    private boolean like;
    private List<String> Comment;
    private boolean bookMark;
    private int likeCount;
    private int viewCount;

    public boolean imageDled = false;

    List<String> listCategory;

    public List<String> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<String> listCategory) {
        this.listCategory = listCategory;
    }

    private ReceiveProductClass productClass;
    BaseCodeClass baseCodeClass = new BaseCodeClass();


    public AllProductData(Context mContext, String companyName, boolean selectedToCart, boolean like,
                          List<String> comment, boolean bookMark, int likeCount, int viewCount, List<String> listCategory,
                          ReceiveProductClass productClass) {
        this.mContext = mContext;
        this.companyName = companyName;
        this.selectedToCart = selectedToCart;
        this.like = like;
        Comment = comment;
        this.bookMark = bookMark;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.listCategory = listCategory;
        this.productClass = productClass;

    }

    public AllProductData(AllProductData data) {
        this.mContext = data.mContext;
        this.companyName = data.companyName;
        this.selectedToCart = data.selectedToCart;
        this.like = data.like;
        this.Comment = data.getComment();
        this.bookMark = data.bookMark;
        this.likeCount = data.likeCount;
        this.viewCount = data.viewCount;
        this.listCategory = data.listCategory;
        this.productClass = data.productClass;
    }

//    public Bitmap getImageFromUrl(){
//        return baseCodeClass.ProductImage(productClass.getProductID(), mContext);
//    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isSelectedToCart() {
        return selectedToCart;
    }

    public void setSelectedToCart(boolean selectedToCart) {
        this.selectedToCart = selectedToCart;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public List<String> getComment() {
        return Comment;
    }

    public void setComment(List<String> comment) {
        Comment = comment;
    }

    public boolean isBookMark() {
        return bookMark;
    }

    public void setBookMark(boolean bookMark) {
        this.bookMark = bookMark;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public ReceiveProductClass getProductClass() {
        return productClass;
    }

    public void setProductClass(ReceiveProductClass productClass) {
        this.productClass = productClass;
    }

}
