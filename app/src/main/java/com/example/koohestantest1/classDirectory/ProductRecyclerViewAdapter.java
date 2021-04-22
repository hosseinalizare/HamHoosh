package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.BadgeCounter;
import com.example.koohestantest1.Utils.StringUtils;
import com.example.koohestantest1.Utils.TimeUtils;
import com.example.koohestantest1.ViewProductActivity;
import com.example.koohestantest1.constants.FilterOption;
import com.example.koohestantest1.fragments.bottomsheet.EditBottomSheet;
import com.example.koohestantest1.model.DeleteProduct;
import com.example.koohestantest1.model.UpdatedProductBody;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.LoadProductApi;
import com.example.koohestantest1.DB.MyDataBase;
import com.example.koohestantest1.ViewModels.BookMarkViewModel;
import com.example.koohestantest1.ViewModels.PostLikeViewModel;
import com.example.koohestantest1.ViewModels.PostViewViewModel;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.context;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.manageOrderClass;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.productDataList;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.sendOrderClass;
import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;

class itemViewHolder extends RecyclerView.ViewHolder {

    public ImageView pImageView;
    public TextView txtPName, txtDetail, txtPrice, cartQTY,txtPrice2;
    public CardView cardView, addToCart, increaseCart;
    public ImageView btnAddToCart, cartAdd, cartRemove, RaidIcon,imgEdit, imgDiscount;
    public ConstraintLayout layout;
    private boolean changePricePermission;
    public itemViewHolder(@NonNull View itemView) {
        super(itemView);

        pImageView = itemView.findViewById(R.id.ProductImage);
        txtPName = itemView.findViewById(R.id.productName);
        txtDetail = itemView.findViewById(R.id.txtProductDetail);
        txtPrice = itemView.findViewById(R.id.txtProductPrice);
        cardView = itemView.findViewById(R.id.cardViewProduct);
        btnAddToCart = itemView.findViewById(R.id.btmAddToCart);
        addToCart = itemView.findViewById(R.id.cardViewAddToCart);
        increaseCart = itemView.findViewById(R.id.cardViewIncreaseCart);
        cartAdd = itemView.findViewById(R.id.cartAdd);
        cartRemove = itemView.findViewById(R.id.cartRemove);
        cartQTY = itemView.findViewById(R.id.cartQuantity);
        txtPrice2=itemView.findViewById(R.id.txtProductFinalPrice);
        layout = itemView.findViewById(R.id.addToCartBack);
        RaidIcon = itemView.findViewById(R.id.rialIcon);
        imgEdit = itemView.findViewById(R.id.img_productItem_edit);
        imgDiscount =itemView.findViewById(R.id.img_priceDiscount);
    }

}

class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.loadMoreProgressBar);
    }
}

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable, LoadProductApi {
    private Context mContext;
    List<AllProductData> productData;
    public List<AllProductData> showProductData = new ArrayList<>();
    public List<AllProductData> filteredProduct = new ArrayList<>();
    private FilterOption filterOption = FilterOption.NON;

    BaseCodeClass baseCodeClass = new BaseCodeClass();
    MyDataBase mydb;

    public final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    public RecyclerLoadMore loadMoreInterFace;
    public boolean isLoading;
    public int visibleThreshold = 9;
    public int lastVisibleItem, totalItemCount;
    private BadgeSharedViewModel badgeSharedViewModel;
    private LocalCartViewModel localCartViewModel;
    private FragmentManager fragmentManager;
    private String TAG = ProductRecyclerViewAdapter.class.getSimpleName();

//    public ProductRecyclerViewAdapter(Context mContext, List<AllProductData> _productData,
//                                      BadgeSharedViewModel badgeSharedViewModel, LocalCartViewModel localCartViewModel) {
//        this.mContext = mContext;
//        productData = _productData;
//        this.badgeSharedViewModel = badgeSharedViewModel;
//        this.localCartViewModel = localCartViewModel;
//        try {
//            this.filteredProduct.addAll(productData);
//            load10Data();
//
//            mydb = new MyDataBase(mContext);
//        } catch (Exception e) {
//            logMessage("ProductAdapter 100 >> " + e.getMessage(), mContext);
//        }
//
//    }

    public ProductRecyclerViewAdapter(Context mContext, List<AllProductData> _productData,
                                      BadgeSharedViewModel badgeSharedViewModel, LocalCartViewModel localCartViewModel, FragmentManager fragmentManager) {
        this.mContext = mContext;
        productData = _productData;
        this.fragmentManager = fragmentManager;
        this.badgeSharedViewModel = badgeSharedViewModel;
        this.localCartViewModel = localCartViewModel;
        try {
            this.filteredProduct.addAll(productData);
            load10Data();

            mydb = new MyDataBase(mContext);
        } catch (Exception e) {
            logMessage("ProductAdapter 100 >> " + e.getMessage(), mContext);
        }
    }

    @Override
    public Call<GetResualt> sendProductDetail(SendProductClass sendProductClass) {
        return null;
    }

    @Override
    public void onResponseSendProduct(GetResualt getResualt) {

    }

    @Override
    public Call<GetResualt> uploadProductImage(String prId, String coId, String uID, String token, MultipartBody.Part file) {
        return null;
    }

    @Override
    public Call<List<SendProductClass>> loadProduct(String companyId) {
        return null;
    }

    @Override
    public void onResponseLoadProduct(List<SendProductClass> sendProductClasses) {

    }

    @Override
    public Call<List<SendProductClass>> loadProduct(String companyId, String userID) {
        return null;
    }

    @Override
    public Call<GetResualt> deleteProduct(SendDeleteProduct sendDeleteProduct) {
        return null;
    }

    @Override
    public void onResponseDeleteProduct(GetResualt getResualt) {

    }

    @Override
    public Call<ResponseBody> downloadImage(String PId, String fileNumber) {
        return null;
    }

    @Override
    public void onResponseDownloadImage(ResponseBody responseBody, String pid) {
        notifyDataSetChanged();
    }

    @Override
    public Call<List<String>> getCategory(int companyType) {
        return null;
    }

    @Override
    public void onResponseGetCategory(List<String> cat) {

    }

    @Override
    public Call<List<String>> getSubCatOne(int companytype, String mainCat) {
        return null;
    }

    @Override
    public void onResponseGetSubCatOne(List<String> subCat1) {

    }

    @Override
    public Call<List<GetPropertisOfCompanyProducts>> getProperty(int companytype) {
        return null;
    }

    @Override
    public void onResponseGetProperty(List<GetPropertisOfCompanyProducts> propertisOfCompanyProducts) {

    }

    @Override
    public Call<GetResualt> viewProduct(PostViewViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> likeProduct(PostLikeViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> saveProduct(BookMarkViewModel postViewViewModel) {
        return null;
    }

    @Override
    public Call<GetResualt> editProductDetail(SendProductClass sendProductClass) {
        return null;
    }

    @Override
    public Call<List<SendProductClass>> getUpdatedData(UpdatedProductBody updatedProductBody) {
        return null;
    }

    @Override
    public void recyclerViewListClicked(View v, String value, boolean notify) {

    }

    @Override
    public void brandRecyclerViewListClicked(View v, String value, boolean notify) {

    }

    @Override
    public void recyclerViewCanUpdating() {

    }

    @Override
    public void imageAdapterCanUpdating(String imagePID) {
    }

    @Override
    public Call<GetResualt> deleteProduct(DeleteProduct deleteProduct) {
        return null;
    }




    /**
     * this was updateImage
     **/
    public void newDownloadImage(String pid) {
        String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
        Glide.with(mContext).load(url).into(holder.pImageView);
    }

    public void load10Data() {
        try {
            int i = 0;
            showProductData.clear();
            if (filteredProduct != null) {
                for (AllProductData b : filteredProduct
                ) {
                    this.showProductData.add(b);
                    i++;
                    if (i >= 10) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logMessage("ProductAdapter 200 >> " + e.getMessage(), mContext);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return showProductData.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMoreInterFace(RecyclerLoadMore loadMoreInterFace) {
        this.loadMoreInterFace = loadMoreInterFace;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_listview, parent, false);

            return new itemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_layout, parent, false);

            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof itemViewHolder) {
            loadProduct(holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return showProductData.size();
    }

    public int showSize() {
        return showProductData.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public Filter getFilter() {
        try {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredProduct = (List<AllProductData>) results.values;
                    load10Data();
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    ArrayList<AllProductData> FilteredArrayNames = new ArrayList<>();

                    String value = constraint.toString();
                    char mode = value.charAt(0);
                    value = value.substring(1);
                    constraint=value;
                    if (mode=='B')
                    {
                        for (int i = 0; i < productData.size(); i++) {
                            String dataNames =null;
                            for (ProductPropertisClass productPropertisClass : productData.get(i).getProductClass().getProductPropertis()) {
                                if (productPropertisClass.getPropertisName().equals("برند")){
                                    dataNames = productPropertisClass.getPropertisValue();
                                    break;
                                }
                            }
                           if (dataNames ==null){
                               continue;
                           }

                            if (dataNames.equals(constraint.toString()) ||
                                    constraint.toString().equals(mContext.getResources().getString(R.string.all))) {
                                FilteredArrayNames.add(productData.get(i));
                            }
                        }

                        results.count = FilteredArrayNames.size();
                        results.values = FilteredArrayNames;

                        return results;
                    }

                    // perform your search here using the searchConstraint String.

                    // constraint = constraint.toString().toLowerCase();
                    if (constraint.toString().equals("ترشی")) {
                        constraint = "ترشیجات";
                    }
                    for (int i = 0; i < productData.size(); i++) {
                        String dataNames = productData.get(i).getListCategory().get(1);
                        if (dataNames.equals(constraint.toString()) ||
                                constraint.toString().equals(mContext.getResources().getString(R.string.all))) {
                            FilteredArrayNames.add(productData.get(i));
                        }
                    }

                    results.count = FilteredArrayNames.size();
                    results.values = FilteredArrayNames;

                    return results;
                }
            };

            return filter;
        } catch (Exception e) {
            logMessage("ProductAdapter 300 >> " + e.getMessage(), mContext);
            return null;
        }
    }


    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public itemViewHolder holder;

    public void loadProduct(final RecyclerView.ViewHolder mholder, final int position) {


        try {

            holder = (itemViewHolder) mholder;
            if (baseCodeClass.getEmployeeID(baseCodeClass.getUserID()).equals("false")){
                holder.imgEdit.setVisibility(View.GONE);
            }
            if (showProductData.get(position) == null) {
                return;
            }


            holder.imgEdit.setOnClickListener(v -> {
                showPopup(v,position);
            });

            if (!showProductData.get(position).getProductClass().getListPrice().equals("0")){


                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.txtPrice.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.red_line) );
                } else {
                    holder.txtPrice.setBackground(ContextCompat.getDrawable(context, R.drawable.red_line));
                }
                holder.imgDiscount.setVisibility(View.VISIBLE);
                int firstPrice=Integer.parseInt(showProductData.get(position).getProductClass().getStandardCost());
                int discountPrice=Integer.parseInt(showProductData.get(position).getProductClass().getListPrice());
                int finalPrice = firstPrice-discountPrice;

                holder.txtPrice2.setText(StringUtils.getNumberWithoutDot(finalPrice));
                holder.txtPrice2.setVisibility(View.VISIBLE);
                BaseCodeClass.setMargins(holder.txtPrice,8,0,0,0);
                holder.txtPrice.setPadding(0,0,0,0);

            }else {
                holder.imgDiscount.setVisibility(View.GONE);

            }

            holder.txtPName.setText(showProductData.get(position).getProductClass().getProductName());

            Log.d(TAG, "loadProduct: " + showProductData.get(position).getProductClass().getProductID());
            newDownloadImage(showProductData.get(position).getProductClass().getProductID());

            String detail = showProductData.get(position).getProductClass().getDescription().replace("\n", " ");
            if (detail.length() >= 21) {
                holder.txtDetail.setText(detail.substring(0, Math.min(detail.length(), 20)) + "...");
            } else {
                holder.txtDetail.setText(detail);
            }
            holder.txtPrice.setText(StringUtils.getNumberWithoutDot(showProductData.get(position).getProductClass().getStandardCost()));

            if (!baseCodeClass.getEmployeeID(baseCodeClass.getUserID()).equals("false")) {
                boolean changePricePermission = baseCodeClass.getPermissions().get(BaseCodeClass.EmploeeAccessLevel.EditeProductPrice.getValue()).isState();
                holder.txtPrice.setOnLongClickListener(v -> {
                    if(!changePricePermission){
                        Toast.makeText(mContext, "اجازه ویرایش قیمت محصول را ندارید", Toast.LENGTH_SHORT).show();
                        return false;
                    }else {
                        String productId = showProductData.get(position).getProductClass().getProductID();
                        EditBottomSheet editBottomSheet = EditBottomSheet.onNewInstance(0, showProductData.get(position).getProductClass().getStandardCost(), productId, BaseCodeClass.productFieldEnum.StandardCost,null);
                        editBottomSheet.show(fragmentManager, "EDIT_PRICE_TAG");
                        return true;
                    }
                });

                holder.RaidIcon.setOnLongClickListener(view -> {
                    if(!changePricePermission){
                        Toast.makeText(mContext, "اجازه ویرایش قیمت محصول را ندارید", Toast.LENGTH_SHORT).show();
                        return false;
                    }else {
                        String productId = showProductData.get(position).getProductClass().getProductID();
                        EditBottomSheet editBottomSheet = EditBottomSheet.onNewInstance(0, showProductData.get(position).getProductClass().getStandardCost(), productId, BaseCodeClass.productFieldEnum.StandardCost,null);
                        editBottomSheet.show(fragmentManager, "EDIT_PRICE_TAG");
                        return true;
                    }
                });

            }

            holder.cardView.setOnClickListener(v -> {
                try {
//                selectedPID = PID.get(position);
                    if (baseCodeClass.loadSelectedProduct(showProductData.get(position).getProductClass().getProductID(), mContext)) {
                        Intent intent = new Intent(mContext, ViewProductActivity.class);
                        intent.putExtra("PID", showProductData.get(position).getProductClass().getProductID());
                        mContext.startActivity(intent);
                    } else {
//                    toastMessage("خطای درون برنامه ای");
                    }
                } catch (Exception e) {
                    logMessage("ProductAdapter 400 >> " + e.getMessage(), mContext);
                }

            });

            if (showProductData.get(position).isSelectedToCart()) {
                holder.btnAddToCart.setImageResource(R.drawable.ic_added_cart);
                holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.okGreen));
            } else {
                holder.btnAddToCart.setImageResource(R.drawable.ic_add_cart);
                holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.purple1));
            }

            /*
             Modifies Number of Items to Cart
             */
            //TODO Show non exist message inside invisible
            if (showProductData.get(position).getProductClass().getDiscontinued() == 0) {
                holder.addToCart.setVisibility(View.INVISIBLE);
            } else holder.addToCart.setVisibility(View.VISIBLE);

            holder.addToCart.setOnClickListener(v -> {
                try {
                    String currentProductId = showProductData.get(position).getProductClass().getProductID();
                    baseCodeClass.loadSelectedProduct(currentProductId, mContext);
                    if (selectedProduct.isSelectedToCart()) {
                        //removes Item in carts
                        manageOrderClass.RemoveProductFromCart(showProductData.get(position).getProductClass().getProductID());
                        productDataList.get(productDataList.indexOf(selectedProduct)).setSelectedToCart(false);

                        //handle view:
                        holder.btnAddToCart.setImageResource(R.drawable.ic_add_cart);
                        holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.purple1));
                        notifyItemChanged(position);
                        badgeSharedViewModel.setCount(BadgeCounter.getCount() - 1);

                    } else {
                        if (manageOrderClass.addProductToCart(selectedProduct.getProductClass())) {
                            productDataList.get(productDataList.indexOf(selectedProduct)).setSelectedToCart(true);

                            badgeSharedViewModel.setCount(sendOrderClass.Order_Details.size());


                        } else {
                            logMessage("productRec Adapter : 404", mContext);
                        }

                    }
                    localCartViewModel.updateCartInfo(sendOrderClass);

                    notifyItemChanged(position);

                } catch (Exception e) {
                    logMessage("productAdapter add to cart : " + e.getMessage(), mContext);
                }
            });
        } catch (Exception e) {
            logMessage("ProductAdapter 500 >> " + e.getMessage(), mContext);
        }
    }

    private void showPopup(View v,int position) {
        boolean showProduct = Boolean.parseBoolean(showProductData.get(position).getProductClass().getShow());
        PopupMenu popupMenu = new PopupMenu(mContext,v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.edit_product_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean changePricePermission = baseCodeClass.getPermissions().get(BaseCodeClass.EmploeeAccessLevel.EditeProductPrice.getValue()).isState();
                boolean showProduct = Boolean.parseBoolean(showProductData.get(position).getProductClass().getShow());

                switch (item.getItemId()){
                    case R.id.editProductName_menu:
                        String productId = showProductData.get(position).getProductClass().getProductID();
                        EditBottomSheet editBottomSheet = EditBottomSheet.
                                onNewInstance(0, showProductData.get(position).getProductClass().getProductName(), productId,BaseCodeClass.productFieldEnum.ProductName,null);
                        editBottomSheet.show(fragmentManager, "EDIT_PRICE_TAG");
                        //Toast.makeText(mContext, "Product name", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.editProductCount_menu:
                        productId = showProductData.get(position).getProductClass().getProductID();
                        editBottomSheet = EditBottomSheet.
                                onNewInstance(0, showProductData.get(position).getProductClass().getDiscontinued()+"", productId,BaseCodeClass.productFieldEnum.Discontinued,null);
                        editBottomSheet.show(fragmentManager, "EDIT_PRICE_TAG");
                        break;
                    case R.id.editProductPrice_menu:
                        if(!changePricePermission){
                            Toast.makeText(mContext, "اجازه ویرایش قیمت محصول را ندارید", Toast.LENGTH_SHORT).show();
                        }else {
                            productId = showProductData.get(position).getProductClass().getProductID();
                            editBottomSheet = EditBottomSheet.
                                    onNewInstance(0, showProductData.get(position).getProductClass().getStandardCost(), productId, BaseCodeClass.productFieldEnum.StandardCost,null);
                            editBottomSheet.show(fragmentManager, "EDIT_PRICE_TAG");
                        }
                        break;
                    case R.id.manageShowProduct:
                        productId = showProductData.get(position).getProductClass().getProductID();
                        editBottomSheet = EditBottomSheet
                                .onNewInstance(0,showProductData.get(position).getProductClass().getShow(),productId,BaseCodeClass.productFieldEnum.Show,null);
                        editBottomSheet.show(fragmentManager,"EDIT_SHOW_PRODUCT");
                        break;
                    case R.id.deleteProduct_menu:
                        productId = showProductData.get(position).getProductClass().getProductID();
                        AllProductData productData = showProductData.get(position);
                        editBottomSheet = EditBottomSheet.
                                onNewInstance(0,null,productId, BaseCodeClass.productFieldEnum.Deleted,productData);
                        editBottomSheet.show(fragmentManager,"DELETE_PRODUCT");

                }
                return false;
            }
        });

        popupMenu.show();

    }

    public void sortCheapData() {
        Collections.sort(filteredProduct, (o1, o2) -> {

            String itemPrice1 = o1.getProductClass().getStandardCost();
            String itemPrice2 = o2.getProductClass().getStandardCost();
            if (isFieldEmpty(itemPrice1, itemPrice2))
                return 1;

            String cleanPrice1 = StringUtils.getNumberFromStringV3(itemPrice1);
            String cleanPrice2 = StringUtils.getNumberFromStringV3(itemPrice2);
            int priceO1 = Integer.parseInt(cleanPrice1);
            int priceO2 = Integer.parseInt(cleanPrice2);
            return Integer.compare(priceO1, priceO2);
        });

        load10Data();
        notifyDataSetChanged();
    }

/*
    public void sortExpensiveData() {
        Collections.sort(filteredProduct, (o1, o2) -> {
            String itemPrice1 = o1.getProductClass().getStandardCost();
            String itemPrice2 = o2.getProductClass().getStandardCost();
            if (isFieldEmpty(itemPrice1, itemPrice2))
                return 0;
            String cleanPrice1 = StringUtils.getNumberFromStringV3(itemPrice1);
            String cleanPrice2 = StringUtils.getNumberFromStringV3(itemPrice2);
            int priceO1 = Integer.parseInt(cleanPrice1);
            int priceO2 = Integer.parseInt(cleanPrice2);

            return Integer.compare(priceO1, priceO2);
        });
        Collections.reverse(filteredProduct);
        load10Data();
        notifyDataSetChanged();
    }
*/

    public void sortExpensiveData() {
        Collections.sort(filteredProduct, (o1, o2) -> {

            String itemPrice1 = o1.getProductClass().getStandardCost();
            String itemPrice2 = o2.getProductClass().getStandardCost();
            if (isFieldEmpty(itemPrice1, itemPrice2))
                return 0;

            String cleanPrice1 = StringUtils.getNumberFromStringV3(itemPrice1);
            String cleanPrice2 = StringUtils.getNumberFromStringV3(itemPrice2);
            int priceO1 = Integer.parseInt(cleanPrice1);
            int priceO2 = Integer.parseInt(cleanPrice2);

            return Integer.compare(priceO1, priceO2);
        });
        Collections.reverse(filteredProduct);
        load10Data();
        notifyDataSetChanged();
    }




    public void sortByNewProduct(){
        Collections.sort(filteredProduct, (o1, o2) -> {
           Date date1 = TimeUtils.convertStrToDate(o1.getProductClass().getUpdateDate());
           Date date2 = TimeUtils.convertStrToDate(o2.getProductClass().getUpdateDate());
           long d1 = date1.getTime();
           long d2 = date2.getTime();

                return Long.compare(d1,d2);


        });

        Collections.reverse(filteredProduct);
        load10Data();
        notifyDataSetChanged();


    }


    public void sortMostSell() {
        Collections.sort(filteredProduct, (o1, o2) -> {
            int sellCount1 = o1.getProductClass().getSellCount();
            int sellCount2 = o2.getProductClass().getSellCount();
            return Integer.compare(sellCount1, sellCount2);
        });
        load10Data();
        notifyDataSetChanged();
    }

    public void sortMostView() {
        Collections.sort(filteredProduct, (o1, o2) -> Integer.compare(Integer.parseInt(o2.getProductClass().getViewedCount()),
                Integer.parseInt(o1.getProductClass().getViewedCount())));
        load10Data();
        notifyDataSetChanged();
    }

    public boolean isFieldEmpty(String one, String two) {
        if (one == null || one.equals(""))
            return true;
        if (two == null || two.equals(""))
            return true;

        return false;
    }
}
