package com.example.koohestantest1.classDirectory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.fragments.transinterface.EmployeeDeletingInterface;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;

public class ManageEmployeeRecyclerViewAdapter extends RecyclerView.Adapter<ManageEmployeeRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    List<CompanyFollowerViewModel> employee;
    private EmployeeDeletingInterface anInterface;
    BaseCodeClass baseCodeClass = new BaseCodeClass();

    public ManageEmployeeRecyclerViewAdapter(Context mContext, List<CompanyFollowerViewModel> employee, EmployeeDeletingInterface anInterface) {
        this.mContext = mContext;
        this.employee = employee;
        this.anInterface = anInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_manage_employee, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(employee.get(position).getUserData().getFirstName() + " " + employee.get(position).getUserData().getLastName());
        holder.phone.setText(employee.get(position).getUserData().getMobilePhone());
        holder.bindData(employee.get(position));
    }

    @Override
    public int getItemCount() {
        return employee.size();
    }

    public void removeItem(int pos){
        employee.remove(pos);
        notifyItemRemoved(pos);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, phone;
        Button button;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.EmployeeImage);
            name = itemView.findViewById(R.id.employeeName);
            phone = itemView.findViewById(R.id.employeeUserName);
            button = itemView.findViewById(R.id.btn_setting);
            cardView = itemView.findViewById(R.id.cv_employee);
        }

        void bindData(CompanyFollowerViewModel item) {

            String url = baseCodeClass.pBASE_URL + "User/DownloadFile?UserID=" + item.getUserID() + "&fileNumber=" + 1;
            Glide.with(imageView.getContext()).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_profile).into(imageView);


            button.setOnClickListener(v -> {
//                anInterface.onDelete(item.getUserData().getErrorMsg());
                anInterface.onSettingClicked(item, getAdapterPosition());
            });

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, MessageActivity.class);
                // sender = Another user
                intent.putExtra("getter", item.getUserID());
                // getter = Ourselves
                intent.putExtra("sender", baseCodeClass.getCompanyID());
                mContext.startActivity(intent);
            });
        }

    }

    public void searchInEmployees(String word){
        List<CompanyFollowerViewModel> searchItems = new ArrayList<>();
        for(int i = 0; i < employee.size(); i++){
            if((employee.get(i).getUserData().getFirstName() != null && employee.get(i).getUserData().getFirstName().contains(word)) ||
                    (employee.get(i).getUserData().getLastName() != null && employee.get(i).getUserData().getLastName().contains(word))){
                searchItems.add(employee.get(i));
            }
        }

        employee = searchItems;
        notifyDataSetChanged();
    }

    public void updateData(List<CompanyFollowerViewModel> companyFollowers){
        employee = companyFollowers;
        notifyDataSetChanged();
    }
}
