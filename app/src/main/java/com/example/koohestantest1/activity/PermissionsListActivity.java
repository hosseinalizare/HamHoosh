package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.koohestantest1.adapter.PermissionSpinnerRecyclerAdapter;
import com.example.koohestantest1.adapter.recyclerinterface.EmployeePermissionAdapter;
import com.example.koohestantest1.constants.EmployeeStatus;
import com.example.koohestantest1.databinding.ActivityPermissionsListBinding;
import com.example.koohestantest1.model.EditPermission;
import com.example.koohestantest1.model.Permission;
import com.example.koohestantest1.viewModel.PermissionViewModel;

import java.util.List;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

public class PermissionsListActivity extends AppCompatActivity implements EmployeePermissionAdapter {
    public static String INTENT_KEY_EMPLOYEE_ID = "intent_key_employee_id";
    private ActivityPermissionsListBinding binding;
    private PermissionSpinnerRecyclerAdapter recyclerAdapter;
    private String employeeId;
    private PermissionViewModel permissionViewModel;
    private String TAG = PermissionsListActivity.class.getSimpleName();
    private BaseCodeClass baseCodeClass = new BaseCodeClass();
    private List<Permission> loadedPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        employeeId = getIntent().getStringExtra(INTENT_KEY_EMPLOYEE_ID);

        permissionViewModel = new ViewModelProvider(this).get(PermissionViewModel.class);

        if (employeeId != null)
            permissionViewModel.callForPermissionList(employeeId);
        else
            Toast.makeText(this, "خطای شناسه کارمند", Toast.LENGTH_SHORT).show();

        recyclerAdapter = new PermissionSpinnerRecyclerAdapter(this::onSwitchClicked);
        setUpRecycler();

        binding.ivBackEmployee.setOnClickListener(v -> {
            finish();
        });
        permissionViewModel.getLiveListPermissions().observe(this, permissions -> {
            loadedPermissions = permissions;
            recyclerAdapter.setUpData(permissions);
        });

        permissionViewModel.getLiveEditPermissionRes().observe(this, resualt -> {
            if (resualt.getResualt().equals("100")) {
                Toast.makeText(this, "موفقیت آمیز بود", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCreate: " + resualt.getMsg());
            } else
                Toast.makeText(this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();

        });

        permissionViewModel.getLiveErrorEditPermission().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        });

        permissionViewModel.getLiveErrorPermissions().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        });

        binding.cbEnableAllPermission.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "درحال فعال سازی همه", Toast.LENGTH_SHORT).show();
                for (Permission permission :
                        loadedPermissions) {
                    if (!permission.isState())
                        permission.setState(true);
                }
                permissionViewModel.callForEditEmployeePermission(new EditPermission(baseCodeClass.getToken(), baseCodeClass.getUserID(), employeeId, EmployeeStatus.FULL_PERMISSION, true, 1));
            } else {
                Toast.makeText(this, "در حال غیرفعال سازی", Toast.LENGTH_SHORT).show();
                for (Permission permission :
                        loadedPermissions) {
                    if (permission.isState())
                        permission.setState(false);
                }
                permissionViewModel.callForEditEmployeePermission(new EditPermission(baseCodeClass.getToken(), baseCodeClass.getUserID(), employeeId, EmployeeStatus.DROP_PERMISSIONS, false, 1));

            }
            recyclerAdapter.setUpData(loadedPermissions);
        });
    }

    private void setUpRecycler() {
        binding.rvPermissionsSpinner.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPermissionsSpinner.setAdapter(recyclerAdapter);
    }

    @Override
    public void onSwitchClicked(Permission permission) {
        permissionViewModel.callForEditEmployeePermission(new EditPermission(baseCodeClass.getToken(), baseCodeClass.getUserID(), employeeId, permission.getPos(), permission.isState(), 0));
    }
}