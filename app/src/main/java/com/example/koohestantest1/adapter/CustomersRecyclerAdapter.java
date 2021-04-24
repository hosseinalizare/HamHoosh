package com.example.koohestantest1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.koohestantest1.R;
import com.example.koohestantest1.databinding.RowItemCustomersListBinding;
import com.example.koohestantest1.fragments.transinterface.CustomerChatInterface;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomersRecyclerAdapter extends RecyclerView.Adapter<CustomersRecyclerAdapter.CustomersViewHolder> {
    private List<CompanyFollowerViewModel> list;
    private CustomerChatInterface anInterface;
    private Context context;
    private BaseCodeClass baseCodeClass ;

    public CustomersRecyclerAdapter(CustomerChatInterface anInterface, Context context) {
        this.anInterface = anInterface;
        this.context = context;
        baseCodeClass = new BaseCodeClass();
    }

    public void setData(List<CompanyFollowerViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void searchInCustomer(String word) {
        List<CompanyFollowerViewModel> searchedItem = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).getUserData().getFirstName() != null && list.get(i).getUserData().getFirstName().contains(word))
                    || (list.get(i).getUserData().getLastName() != null && list.get(i).getUserData().getLastName().contains(word))) {
                searchedItem.add(list.get(i));
            }
        }

        list = searchedItem;
        notifyDataSetChanged();
    }

    public void updateData(List<CompanyFollowerViewModel> companyFollowerViewModels) {
        this.list = companyFollowerViewModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowItemCustomersListBinding binding = RowItemCustomersListBinding.inflate(inflater, parent, false);
        return new CustomersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersViewHolder holder, int position) {
        holder.bindData(list.get(position));

        String url = baseCodeClass.pBASE_URL + "User/DownloadFile?UserID=" +list.get(position).getUserID()+ "&fileNumber=" + 1;
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_profile).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class CustomersViewHolder extends RecyclerView.ViewHolder {
        RowItemCustomersListBinding binding;
        CircleImageView circleImageView;

        public CustomersViewHolder(RowItemCustomersListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            circleImageView = binding.imgRowItemCustomerList;
        }

        void bindData(CompanyFollowerViewModel item) {
            String fName = item.getUserData().getFirstName();
            String lName = item.getUserData().getLastName();
            fName = fName != null ? fName : "";
            lName = lName != null ? lName : "";

            if (fName.equals("") && lName.equals("")) {
                BaseCodeClass baseCodeClass = new BaseCodeClass();
                boolean showNumberPermission = baseCodeClass.getPermissions().get(BaseCodeClass.EmploeeAccessLevel.ViewCustomerMobilePhone.getValue()).isState();
                if (!showNumberPermission) {
                    StringBuilder hidingNumber = new StringBuilder(item.getUserData().getMobilePhone());
                    for (int i = 7; i < hidingNumber.length(); i++) {
                        hidingNumber.setCharAt(i, 'x');
                    }
                    String hiddenNumber = hidingNumber.toString();
                    binding.tvCustomerTitle.setText(hidingNumber);
                } else
                    binding.tvCustomerTitle.setText(item.getUserData().getMobilePhone());
            } else
                binding.tvCustomerTitle.setText(fName + " " + lName);


            String homeTown = item.getUserData().getHometown();
            homeTown = homeTown != null ? homeTown : "";
            binding.tvCustomerLocation.setText(homeTown);

            binding.ivCustomerChat.setOnClickListener(v -> {
                BaseCodeClass baseCodeClass = new BaseCodeClass();
                boolean startChatPermission = baseCodeClass.getPermissions().get(BaseCodeClass.EmploeeAccessLevel.StartChatWhitCustmer.getValue()).isState();
                if (!startChatPermission) {
                    Toast.makeText(context, "اجازه شروع چت با مشتری را ندارید", Toast.LENGTH_SHORT).show();
                } else {
                    anInterface.onChatClicked(item.getUserID());
                }
            });
        }
    }
}
