package com.example.koohestantest1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.AddProductActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.BadgeCounter;
import com.example.koohestantest1.ViewProductActivity;

import java.util.List;

import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.ManageOrderClass;
import com.example.koohestantest1.classDirectory.ReceiveProductClass;
import com.example.koohestantest1.fragments.bottomsheet.EditBottomSheet;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.badge;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

import static com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory;


public class ProductRecyclerViewAdapterV2 extends RecyclerView.Adapter<ProductRecyclerViewAdapterV2.MyViewHolder> {
    private Context mContext;
    public List<Product> showProductData;
    private final String TAG = ProductRecyclerViewAdapterV2.class.getSimpleName();
    BaseCodeClass baseCodeClass = new BaseCodeClass();

    private boolean editMode;
    private DBViewModel dbViewModel;
    private FragmentManager fragmentManager;

    private ManageOrderClass manageOrderClass;


 /*   public ProductRecyclerViewAdapterV2(Context mContext, boolean editMode) {
        this.mContext = mContext;
        this.editMode = editMode;
    }*/

    public ProductRecyclerViewAdapterV2(Context mContext, boolean editMode, FragmentManager fragmentManager, DBViewModel dbViewModel, AppCompatActivity fragment) {
        this.mContext = mContext;
        this.editMode = editMode;
        this.fragmentManager = fragmentManager;
        this.dbViewModel = dbViewModel;

        manageOrderClass = new ManageOrderClass(fragment);
    }

    public void setData(List<Product> _productData) {
        showProductData = _productData;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_listview, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (editMode) {
            holder.addToCart.setVisibility(View.GONE);
        }
        loadProduct(holder, position);

        holder.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return showProductData != null ? showProductData.size() : 0;
    }


    public void toastMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public void loadProduct(final MyViewHolder mholder, final int position) {
        try {
            if (showProductData.get(position) == null) {
                return;
            }
            mholder.txtPName.setText(showProductData.get(position).ProductName);
            String pid = showProductData.get(position).ProductID;
            String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
            Glide.with(mContext).load(url).into(mholder.pImageView);

            String detail = showProductData.get(position).Description.replace("\n", " ");
            mholder.txtDetail.setText(detail);
            String dirtyPrice = showProductData.get(position).ShowStandardCost;
            float floatPrice = Float.parseFloat(dirtyPrice);
            int intPrice = (int) floatPrice;
            Log.d(TAG, "loadProduct: " + intPrice);
            mholder.txtPrice.setText(String.valueOf(intPrice));
            /**
             *
             */
            /*mholder.imgSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "HI", Toast.LENGTH_SHORT).show();
                }
            });*/
            mholder.cardView.setOnClickListener(v -> {
                if (editMode) {
                    startEditProduct(showProductData.get(position).ProductID);
                } else {
                    Intent intent = new Intent(mContext, ViewProductActivity.class);
                    intent.putExtra("PID", showProductData.get(position).ProductID);
                    mContext.startActivity(intent);
                }
            });

            //TODO(*) CHECK FOR CART
//            if (showProductData.get(position).isSelectedToCart()) {
//                mholder.btnAddToCart.setImageResource(R.drawable.ic_added_cart);
//                mholder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.okGreen));
//            } else {
//                mholder.btnAddToCart.setImageResource(R.drawable.ic_add_cart);
//                mholder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.purple1));
//            }

            /*
             Modifies Number of Items to Cart
             */

            mholder.addToCart.setOnClickListener(v -> {
                try {
                    // baseCodeClass.loadSelectedProduct(currentProductId, mContext);
                    if (showProductData.get(position).AddToCard) {
                        //removes Item in carts
                        manageOrderClass.RemoveProductFromCart(showProductData.get(position).ProductID);
                        //productDataList.get(productDataList.indexOf(selectedProduct)).setSelectedToCart(false);
                        showProductData.get(position).AddToCard = false;

                        //handle view:
                        mholder.btnAddToCart.setImageResource(R.drawable.ic_add_cart);
                        mholder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.purple1));
                        notifyItemChanged(position);
                        //badgeSharedViewModel.setCount(BadgeCounter.getCount() - 1);

                    } else {
                        /*if (manageOrderClass.addProductToCart(selectedProduct.getProductClass()))*/

                        //productDataList.get(productDataList.indexOf(selectedProduct)).setSelectedToCart(true);
                        showProductData.get(position).AddToCard = true;
                        //badgeSharedViewModel.setCount(sendOrderClass.Order_Details.size());
                    }

                    dbViewModel.updateProduct(showProductData.get(position));
                    notifyItemChanged(position);

                } catch (Exception e) {
                    logMessage("productAdapter add to cart : " + e.getMessage(), mContext);
                }
            });
        } catch (Exception e) {
            logMessage("ProductAdapter 500 >> " + e.getMessage(), mContext);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView pImageView;
        public TextView txtPName, txtDetail, txtPrice, cartQTY;
        public CardView cardView, addToCart, increaseCart;
        public ImageView btnAddToCart, cartAdd, cartRemove, imgSetting;
        public ConstraintLayout layout;

        public MyViewHolder(@NonNull View itemView) {
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
            layout = itemView.findViewById(R.id.addToCartBack);
            imgSetting = itemView.findViewById(R.id.img_productItem_edit);

        }
    }

    private void showPopup(View v, int position) {
        boolean showProduct = showProductData.get(position).Show;
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.edit_product_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean changePricePermission = baseCodeClass.getPermissions().get(BaseCodeClass.EmploeeAccessLevel.EditeProductPrice.getValue()).isState();
                boolean showProduct = showProductData.get(position).Show;

                switch (item.getItemId()) {
                    case R.id.editProductName_menu:
                        String productId = showProductData.get(position).ProductID;
                        EditBottomSheet editBottomSheet = EditBottomSheet.
                                onNewInstance(0, showProductData.get(position).ProductName, productId, BaseCodeClass.productFieldEnum.ProductName, null);
                        editBottomSheet.show(fragmentManager, "EDIT_PRICE_TAG");
                        //Toast.makeText(mContext, "Product name", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.editProductCount_menu:
                        productId = showProductData.get(position).ProductID;
                        editBottomSheet = EditBottomSheet.
                                onNewInstance(0, showProductData.get(position).Discontinued + "", productId, BaseCodeClass.productFieldEnum.Discontinued, null);
                        editBottomSheet.show(fragmentManager, "EDIT_PRICE_TAG");
                        break;
                    case R.id.editProductPrice_menu:
                        if (!changePricePermission) {
                            Toast.makeText(mContext, "اجازه ویرایش قیمت محصول را ندارید", Toast.LENGTH_SHORT).show();
                        } else {
                            productId = showProductData.get(position).ProductID;
                            editBottomSheet = EditBottomSheet.
                                    onNewInstance(0, showProductData.get(position).ShowPrice, productId, BaseCodeClass.productFieldEnum.StandardCost, null);
                            editBottomSheet.show(fragmentManager, "EDIT_PRICE_TAG");
                        }
                        break;
                    case R.id.manageShowProduct:
                        productId = showProductData.get(position).ProductID;
                        editBottomSheet = EditBottomSheet
                                .onNewInstance(0, String.valueOf(showProductData.get(position).Show), productId, BaseCodeClass.productFieldEnum.Show, null);
                        editBottomSheet.show(fragmentManager, "EDIT_SHOW_PRODUCT");
                        break;
                    case R.id.deleteProduct_menu:
                        productId = showProductData.get(position).ProductID;
/*
                        AllProductData productData = showProductData.get(position);
*/
                        // ReceiveProductClass productData = showProductData.get(position);
                        editBottomSheet = EditBottomSheet.
                                onNewInstance2(0, null, productId, BaseCodeClass.productFieldEnum.Deleted, showProductData.get(position));
                        editBottomSheet.show(fragmentManager, "DELETE_PRODUCT");

                }
                return false;
            }
        });

        popupMenu.show();

    }

    public void startEditProduct(String id) {
        Intent intent = new Intent(mContext, AddProductActivity.class);
        intent.putExtra("PID", id);

        mContext.startActivity(intent);
    }
}
