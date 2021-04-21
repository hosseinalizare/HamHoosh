package com.example.koohestantest1.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.adapter.recyclerinterface.EmployeePermissionAdapter;
import com.example.koohestantest1.databinding.RowItemSpinnerPermissionBinding;
import com.example.koohestantest1.model.Permission;

import java.util.List;

public class PermissionSpinnerRecyclerAdapter extends RecyclerView.Adapter<PermissionSpinnerRecyclerAdapter.PermissionViewHolder> {

    private List<Permission> list;
    private EmployeePermissionAdapter permissionInterface;

    public PermissionSpinnerRecyclerAdapter(EmployeePermissionAdapter permissionInterface) {
        this.permissionInterface = permissionInterface;
    }

    public void setUpData(List<Permission> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PermissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowItemSpinnerPermissionBinding rowItemSpinnerPermissionBinding = RowItemSpinnerPermissionBinding.inflate(layoutInflater, parent, false);
        return new PermissionViewHolder(rowItemSpinnerPermissionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class PermissionViewHolder extends RecyclerView.ViewHolder {
        private RowItemSpinnerPermissionBinding binding;
        private SwitchCompat switchCompat;

        public PermissionViewHolder(@NonNull RowItemSpinnerPermissionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            switchCompat = binding.switchPermission;
        }

        void bindData(Permission permission) {
            binding.switchPermission.setChecked(permission.isState());
            binding.switchPermission.setText(permission.getDescription());

            switchCompat.setOnClickListener(v -> {
                if (!switchCompat.isChecked()) {
                    switchCompat.setChecked(false);
                    permission.setState(false);
                } else {
                    switchCompat.setChecked(true);
                    permission.setState(true);
                }
                permissionInterface.onSwitchClicked(permission);
            });
        }
    }
}
