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
import com.example.koohestantest1.local_db.entity.ProductWithProperties;
import com.example.koohestantest1.viewModel.BadgeSharedViewModel;
import com.example.koohestantest1.viewModel.LocalCartViewModel;

import com.example.koohestantest1.ApiDirectory.AddressApi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;
import static com.example.koohestantest1.classDirectory.BaseCodeClass.selectedProduct;

public class CartProductRecyclerViewAdapter extends RecyclerView.Adapter<CartProductRecyclerViewAdapter.ViewHolder> implements CartRecyclerViewClickListener {

    private ICartEvents iCartEvents;
    private Context mContext;
    //private SendOrderClass mSendOrderClasses;
    private List<ProductWithProperties> productWithProperties;
    private static AddressApi cartRecyclerViewClickListener;
    private boolean storeOrder;
    private final String TAG = CartProductRecyclerViewAdapter.class.getSimpleName() + "Debug";
    private BadgeSharedViewModel badgeSharedViewModel;
    private LocalCartViewModel localCartViewModel;
    private DBViewModel dbViewModel;
    private LifecycleOwner lifecycleOwner;
    private String url;
    BaseCodeClass baseCodeClass = new BaseCodeClass();
    private Product prod;
    //private ManageOrderClass manageOrderClass;


    public CartProductRecyclerViewAdapter(Context mContext, List<ProductWithProperties> productWithProperties, AddressApi clickListener, boolean storeOrder, BadgeSharedViewModel badgeSharedViewModel, LocalCartViewModel localCartViewModel, ICartEvents iCartEvents, LifecycleOwner lifecycleOwner, DBViewModel dbViewModel,Fragment fragment) {
        this.mContext = mContext;
        this.badgeSharedViewModel = badgeSharedViewModel;
        this.productWithProperties = productWithProperties;
        this.cartRecyclerViewClickListener = clickListener;
        this.storeOrder = storeOrder;
        this.localCartViewModel = localCartViewModel;
        this.iCartEvents = iCartEvents;
        this.lifecycleOwner = lifecycleOwner;
        this.dbViewModel = dbViewModel;

        //manageOrderClass = new ManageOrderClass(fragment);
    }

    public CartProductRecyclerViewAdapter(Context mContext, List<ProductWithProperties> productWithProperties, AddressApi clickListener, boolean storeOrder, DBViewModel dbViewModel) {
        this.mContext = mContext;
        this.productWithProperties = productWithProperties;
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
            url = baseCodeClass.BASE_URL + "Products/DownloadFile?ProductID=" + pid + "&fileNumber=1";
            dbViewModel.getProductImages(pid).observe(lifecycleOwner, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    url = s;
                }
            });

            Glide.with(mContext).load(url).into(_imageView);
        } catch (Exception e) {
            baseCodeClass.logMessage("ViewProduct glide :" + e.getMessage(), mContext);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        prod = new Product();
        int a = productWithProperties.size();
        int b = 0;
        try {

            newDownloadImage(productWithProperties.get(position).getProduct().ProductID, holder.imageProduct);
            holder.productName.setText(productWithProperties.get(position).getProduct().ProductName);
            String companyId = productWithProperties.get(position).getProduct().CompanyID;
            if (companyId != null) {
                if (companyId.equals(BaseCodeClass.CompanyID))
                    holder.companyName.setText(BaseCodeClass.CompanyName);
                else holder.companyName.setText(companyId);
            }
            int quantity = productWithProperties.get(position).getProduct().CartItemCount;
            holder.warranty.setText("تضمین کیفیت محصول");
            holder.shipperName.setText(""+productWithProperties.get(position).getProduct().CompanyName);
            holder.discount.setText("" + (productWithProperties.get(position).getProduct().offPrice * quantity));

            /*String price = mSendOrderClasses.getOrder_Details().get(position).getSumPrice()+"";
            int decimalPrice = price.indexOf(".");*/
            holder.price.setText("" + (productWithProperties.get(position).getProduct().Price * quantity));




            // int decimalQuantity = quantity.indexOf(".");
            holder.quantity.setText(quantity + "");


            if (storeOrder) {
                holder.cartAdd.setVisibility(View.GONE);
                holder.cartRemove.setVisibility(View.GONE);

                holder.trash.setVisibility(View.GONE);
                holder.item.setOnClickListener(v -> {


                    Intent intent = new Intent(mContext, ViewProductActivity.class);
                    intent.putExtra("PID", productWithProperties.get(position).getProduct().ProductID);
                    mContext.startActivity(intent);

                });
            } else {
                          /*
             Remove whole Items from specific product in cart
             */
                holder.trash.setOnClickListener(v -> {
//                int itemCount = mSendOrderClasses.Order_Details.get(position).getQuantity();
                    String currentProductId = productWithProperties.get(position).getProduct().ProductID;
//                    manageOrderClass.RemoveProductFromCart(currentProductId);
                    /*notifyDataSetChanged();*/
                    productWithProperties.get(position).getProduct().AddToCard = false;
                    productWithProperties.get(position).getProduct().CartItemCount = 0;

                    dbViewModel.updateProduct(productWithProperties.get(position).getProduct());

                    Toast.makeText(mContext, "حذف شد", Toast.LENGTH_SHORT).show();
                    /*dbViewModel.getSpecificProduct(currentProductId).observe(lifecycleOwner, new Observer<Product>() {
                        @Override
                        public void onChanged(Product product) {
                            prod = product;
                            if(prod != null) {

                            }
                        }
                    });*/


                 //   localCartViewModel.updateCartInfo(mSendOrderClasses);



//                    if (sendOrderClass.Order_Details.size() == 0) {
//                        sendOrderClass = new SendOrderClass();
//                    }

                   // iCartEvents.onProductRemove();
                });
                holder.cartAdd.setOnClickListener(v -> {


                    SharedPreferences cardShared = mContext.getSharedPreferences("cardItem",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = cardShared.edit();
                    Set<String> itemId = new HashSet<>();

                    itemId.add(productWithProperties.get(position).getProduct().ProductID);
                    String pid = productWithProperties.get(position).getProduct().ProductID;
                    //int count = manageOrderClass.getProductQTY(pid);
                    int count = productWithProperties.get(position).getProduct().CartItemCount;
                    //manageOrderClass.setProductQTY(pid, manageOrderClass.getProductQTY(pid) + 1);
                    count++;
                    productWithProperties.get(position).getProduct().CartItemCount = count;
                    dbViewModel.updateCardItemCount(count,pid);
                    holder.quantity.setText(String.valueOf(count));

                    //mSendOrderClasses.Order_Details.get(position).setQuantity(count);

                    cartRecyclerViewClickListener.CartRecyclerViewClickListener(v, false);

                });
                holder.cartRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pid = productWithProperties.get(position).getProduct().ProductID;
                        int qty = productWithProperties.get(position).getProduct().CartItemCount > 1 ? productWithProperties.get(position).getProduct().CartItemCount - 1 : 1;
                        productWithProperties.get(position).getProduct().CartItemCount = qty;
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

        /*if (mSendOrderClasses != null) {
            Log.d(TAG, "getItemCount: ");
            if (mSendOrderClasses.getOrder_Details() != null) {
                Log.d(TAG, "getItemCount: details " + mSendOrderClasses.getOrder_Details().size());
                return mSendOrderClasses.getOrder_Details().size();
            }
        }*/
        return productWithProperties.size();
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
