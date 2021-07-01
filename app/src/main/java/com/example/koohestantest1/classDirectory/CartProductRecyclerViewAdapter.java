package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koohestantest1.CartRecyclerViewClickListener;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewProductActivity;
import com.example.koohestantest1.adapter.recyclerinterface.ICartEvents;
import com.example.koohestantest1.local_db.DBViewModel;
import com.example.koohestantest1.local_db.entity.Product;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;

import com.example.koohestantest1.ApiDirectory.AddressApi;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;

public class CartProductRecyclerViewAdapter extends RecyclerView.Adapter<CartProductRecyclerViewAdapter.ViewHolder> implements CartRecyclerViewClickListener {

    private ICartEvents iCartEvents;
    private Context mContext;
    private SendOrderClass mSendOrderClasses;
    private static AddressApi cartRecyclerViewClickListener;
    private boolean storeOrder;
    private final String TAG = CartProductRecyclerViewAdapter.class.getSimpleName() + "Debug";
    private BadgeSharedViewModel badgeSharedViewModel;
    private LocalCartViewModel localCartViewModel;
    private DBViewModel dbViewModel;
    private LifecycleOwner lifecycleOwner;
    BaseCodeClass baseCodeClass = new BaseCodeClass();

    private ManageOrderClass manageOrderClass;


    public CartProductRecyclerViewAdapter(Context mContext, SendOrderClass mSendOrderClasses, AddressApi clickListener, boolean storeOrder, BadgeSharedViewModel badgeSharedViewModel, LocalCartViewModel localCartViewModel, ICartEvents iCartEvents, LifecycleOwner lifecycleOwner, DBViewModel dbViewModel,Fragment fragment) {
        this.mContext = mContext;
        this.badgeSharedViewModel = badgeSharedViewModel;
        this.mSendOrderClasses = mSendOrderClasses;
        this.cartRecyclerViewClickListener = clickListener;
        this.storeOrder = storeOrder;
        this.localCartViewModel = localCartViewModel;
        this.iCartEvents = iCartEvents;
        this.lifecycleOwner = lifecycleOwner;
        this.dbViewModel = dbViewModel;

        manageOrderClass = new ManageOrderClass(fragment);
    }

    public CartProductRecyclerViewAdapter(Context mContext, SendOrderClass mSendOrderClasses, AddressApi clickListener, boolean storeOrder, DBViewModel dbViewModel) {
        this.mContext = mContext;
        this.mSendOrderClasses = mSendOrderClasses;
        this.cartRecyclerViewClickListener = clickListener;
        this.storeOrder = storeOrder;
        this.dbViewModel = dbViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_list, parent, false);
        return new ViewHolder(view);
    }

    public void newDownloadImage(String pid, ImageView _imageView) {
        try {
            String url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
            Glide.with(mContext).load(url).into(_imageView);
        } catch (Exception e) {
            baseCodeClass.logMessage("ViewProduct glide :" + e.getMessage(), mContext);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        int a = mSendOrderClasses.Order_Details.size();
        int b = 0;
        try {

            newDownloadImage(mSendOrderClasses.Order_Details.get(position).getProductID(), holder.imageProduct);
            holder.productName.setText(mSendOrderClasses.getOrder_Details().get(position).getProductName());
            String companyId = mSendOrderClasses.getCompanyID();
            if (companyId != null) {
                if (companyId.equals(BaseCodeClass.CompanyID))
                    holder.companyName.setText(BaseCodeClass.CompanyName);
                else holder.companyName.setText(companyId);
            }
            holder.warranty.setText("تضمین کیفیت محصول");
            holder.shipperName.setText(mSendOrderClasses.getShipperID());
            holder.discount.setText(mSendOrderClasses.getOrder_Details().get(position).getDiscount());

            String price = mSendOrderClasses.getOrder_Details().get(position).getUnitPrice();
            int decimalPrice = price.indexOf(".");
            holder.price.setText(decimalPrice == -1 ? price : price.substring(0, decimalPrice));

            int quantity = mSendOrderClasses.getOrder_Details().get(position).getQuantity();


            // int decimalQuantity = quantity.indexOf(".");
            holder.quantity.setText(quantity + "");


            if (storeOrder) {
                holder.cartAdd.setVisibility(View.GONE);
                holder.cartRemove.setVisibility(View.GONE);

                holder.trash.setVisibility(View.GONE);
                holder.item.setOnClickListener(v -> {


                    Intent intent = new Intent(mContext, ViewProductActivity.class);
                    intent.putExtra("PID", mSendOrderClasses.Order_Details.get(position).getProductID());
                    mContext.startActivity(intent);

                });
            } else {
                          /*
             Remove whole Items from specific product in cart
             */
                holder.trash.setOnClickListener(v -> {
//                int itemCount = mSendOrderClasses.Order_Details.get(position).getQuantity();
                    String currentProductId = mSendOrderClasses.Order_Details.get(position).getProductID();
                    manageOrderClass.RemoveProductFromCart(currentProductId);
                    notifyDataSetChanged();

                    dbViewModel.getSpecificProduct(currentProductId).observe(lifecycleOwner, new Observer<Product>() {
                        @Override
                        public void onChanged(Product product) {
                            product.AddToCard = false;
                            dbViewModel.updateProduct(product);
                        }
                    });

                 //   localCartViewModel.updateCartInfo(mSendOrderClasses);

                    Toast.makeText(mContext, "حذف شد", Toast.LENGTH_SHORT).show();

//                    if (sendOrderClass.Order_Details.size() == 0) {
//                        sendOrderClass = new SendOrderClass();
//                    }

                    iCartEvents.onProductRemove();
                });
                holder.cartAdd.setOnClickListener(v -> {


                    SharedPreferences cardShared = mContext.getSharedPreferences("cardItem",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = cardShared.edit();
                    Set<String> itemId = new HashSet<>();

                    itemId.add(mSendOrderClasses.Order_Details.get(position).getProductID());
                    String pid = mSendOrderClasses.Order_Details.get(position).getProductID();
                    //int count = manageOrderClass.getProductQTY(pid);
                    int count = mSendOrderClasses.Order_Details.get(position).getQuantity();
                    //manageOrderClass.setProductQTY(pid, manageOrderClass.getProductQTY(pid) + 1);
                    count++;
                    dbViewModel.updateCardItemCount(count,pid);
                    holder.quantity.setText(String.valueOf(count));
                    /*Product product = new Product();
                    //product = addedProducts.get(position);
                    product.CartItemCount = count;
                    product.ProductName = mSendOrderClasses.Order_Details.get(position).getProductName();
                    product.UpdateDate = mSendOrderClasses.Order_Details.get(position).getUpdateDate();
                    product.ViewedCount = mSendOrderClasses.Order_Details.get(position).getViewedCount();
                    product.CompanyID = mSendOrderClasses.Order_Details.get(position).getCompanyID();
                    product.offPrice = mSendOrderClasses.Order_Details.get(position).getOffPrice();
                    product.Price = mSendOrderClasses.Order_Details.get(position).getPrice();
                    product.ShowStandardCost = mSendOrderClasses.Order_Details.get(position).getShowStandardCost();
                    product.ShowoffPrice = mSendOrderClasses.Order_Details.get(position).getShowoffPrice();
                    product.ShowPrice = mSendOrderClasses.Order_Details.get(position).getShowPrice();
                    product.Spare1 = mSendOrderClasses.Order_Details.get(position).getSpare1();
                    product.Spare2 = mSendOrderClasses.Order_Details.get(position).getSpare2();
                    product.Spare3 = mSendOrderClasses.Order_Details.get(position).getSpare3();
                    product.SubCat1 = mSendOrderClasses.Order_Details.get(position).getSubCat1();
                    product.SubCat2 = mSendOrderClasses.Order_Details.get(position).getSubCat2();
                    product.IsBulletin = mSendOrderClasses.Order_Details.get(position).isBulletin();
                    product.IsParticular = mSendOrderClasses.Order_Details.get(position).isParticular();
                    product.MainCategory = mSendOrderClasses.Order_Details.get(position).getMainCategory();
                    product.Brand = mSendOrderClasses.Order_Details.get(position).getBrand();
                    product.ProductID = mSendOrderClasses.Order_Details.get(position).getProductID();
                    product.CompanyName = mSendOrderClasses.Order_Details.get(position).getCompanyName();
                    product.StandardCost = mSendOrderClasses.Order_Details.get(position).getStandardCost();
                    product.LikeCount = mSendOrderClasses.Order_Details.get(position).getLikeCount();
                    product.Likeit = mSendOrderClasses.Order_Details.get(position).isLikeit();
                    product.Saveit = mSendOrderClasses.Order_Details.get(position).isSaveit();
                    product.AddToCard = mSendOrderClasses.Order_Details.get(position).isAddToCard();
                    product.Unit = mSendOrderClasses.Order_Details.get(position).getUnit();
                    product.TargetLevel = mSendOrderClasses.Order_Details.get(position).getTargetLevel();
                    product.SupplierID = mSendOrderClasses.Order_Details.get(position).getSupplierID();
                    product.SellCount = mSendOrderClasses.Order_Details.get(position).getSellCount();
                    product.SaveCount = mSendOrderClasses.Order_Details.get(position).getSaveCount();
                    product.ReorderLevel = mSendOrderClasses.Order_Details.get(position).getReorderLevel();
                    product.QuantityPerUnit = mSendOrderClasses.Order_Details.get(position).getQuantityPerUnit();
                    product.MinimumReorderQuantity = mSendOrderClasses.Order_Details.get(position).getMinimumReorderQuantity();
                    product.Discontinued = mSendOrderClasses.Order_Details.get(position).getDiscontinued();
                    product.Description = mSendOrderClasses.Order_Details.get(position).getDescription();
                    product.Category = mSendOrderClasses.Order_Details.get(position).getCategory();
                    product.Deleted = mSendOrderClasses.Order_Details.get(position).isDeleted();
                    product.ActiveComment = mSendOrderClasses.Order_Details.get(position).isActiveComment();
                    product.ActiveLike = mSendOrderClasses.Order_Details.get(position).isActiveLike();
                    product.ActiveSave = mSendOrderClasses.Order_Details.get(position).isActiveSave();
                    product.ChatWhitCreator = mSendOrderClasses.Order_Details.get(position).isChatWhitCreator();
                    product.CreatorUserID = mSendOrderClasses.Order_Details.get(position).getCreatorUserID();
                    product.Deleted1 = mSendOrderClasses.Order_Details.get(position).isDeleted1();
                    if (mSendOrderClasses.Order_Details.get(position).getID() != null)
                        product.id = Integer.parseInt(mSendOrderClasses.Order_Details.get(position).getID());
                    product.LinkOut = mSendOrderClasses.Order_Details.get(position).getLinkOut();
                    product.LinkToInstagram = mSendOrderClasses.Order_Details.get(position).getLinkToInstagram();
                    product.ProductType = mSendOrderClasses.Order_Details.get(position).getProductType();*/


                    //holder.quantity.setText(product.CartItemCount + "");
                    mSendOrderClasses.Order_Details.get(position).setQuantity(count);

                    //dbViewModel.updateProduct(product);

                    //notifyDataSetChanged();
                    cartRecyclerViewClickListener.CartRecyclerViewClickListener(v, false);

                });
                holder.cartRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pid = mSendOrderClasses.Order_Details.get(position).getProductID();
                        int qty = mSendOrderClasses.Order_Details.get(position).getQuantity() > 1 ? mSendOrderClasses.Order_Details.get(position).getQuantity() - 1 : 1;
                        mSendOrderClasses.Order_Details.get(position).setQuantity(qty);
                        dbViewModel.updateCardItemCount(qty,pid);
                        holder.quantity.setText(qty + "");
                        //notifyDataSetChanged();
                        cartRecyclerViewClickListener.CartRecyclerViewClickListener(v, false);

                    }
                });
            }
        } catch (Exception e) {
            logMessage("609 >> " + e.getMessage(), mContext);
            Log.d("Error", "609 is: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {

        if (mSendOrderClasses != null) {
            Log.d(TAG, "getItemCount: ");
            if (mSendOrderClasses.getOrder_Details() != null) {
                Log.d(TAG, "getItemCount: details " + mSendOrderClasses.getOrder_Details().size());
                return mSendOrderClasses.getOrder_Details().size();
            }
        }
        return 0;
    }

    @Override
    public void CartRecyclerViewClickListener(View v, boolean b) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct, cartAdd, cartRemove, trash;
        TextView productName, companyName, warranty, shipperName, discount, price, quantity;
        CardView cardView, item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProduct = (ImageView) itemView.findViewById(R.id.cartProductImage);
            productName = (TextView) itemView.findViewById(R.id.cartProductName);
            companyName = (TextView) itemView.findViewById(R.id.cartCompanyName);
            warranty = (TextView) itemView.findViewById(R.id.txtWarranty);
            shipperName = (TextView) itemView.findViewById(R.id.deliveryName);
            discount = (TextView) itemView.findViewById(R.id.carDiscountValue);
            price = (TextView) itemView.findViewById(R.id.cartPrice);
            quantity = (TextView) itemView.findViewById(R.id.cartQuantity);
            cartAdd = (ImageView) itemView.findViewById(R.id.cartAdd);
            cartRemove = (ImageView) itemView.findViewById(R.id.cartRemove);
            trash = (ImageView) itemView.findViewById(R.id.trashBtn);
            cardView = itemView.findViewById(R.id.cardView);
            item = itemView.findViewById(R.id.cardViewProduct);
        }
    }
}
