package com.example.koohestantest1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.activity.PermissionsListActivity;
import com.example.koohestantest1.constants.EmployeeStatus;
import com.example.koohestantest1.fragments.transinterface.EmployeeDeletingInterface;
import com.example.koohestantest1.model.EditPermission;
import com.example.koohestantest1.model.EmployeeDeleting;
import com.example.koohestantest1.viewModel.CompanyViewModel;
import com.example.koohestantest1.viewModel.PermissionViewModel;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.InfoDirectory.GetOnlineInformationClass;
import com.example.koohestantest1.ViewModels.CompanyFollowerViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.CompanyEmployeesClass;
import com.example.koohestantest1.classDirectory.ManageEmployeeRecyclerViewAdapter;

import static com.example.koohestantest1.classDirectory.BaseCodeClass.logMessage;

public class ManageEmployeeActivity extends AppCompatActivity implements EmployeeDeletingInterface {

    private String TAG = ManageEmployeeActivity.class.getSimpleName();
    ManageEmployeeRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    private SearchView svEmployeeSearch;
    private SwipeRefreshLayout swpRefresh;
    List<CompanyFollowerViewModel> employee;
    BaseCodeClass baseCodeClass;
    private CompanyViewModel companyViewModel;
    private PermissionViewModel permissionViewModel;
    private TextView txtTitle;
    private boolean searchMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employee);
        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        permissionViewModel = new ViewModelProvider(this).get(PermissionViewModel.class);


        baseCodeClass = new BaseCodeClass();
        employee = new ArrayList<>();
        swpRefresh = findViewById(R.id.swp_manageEmployee);
        recyclerView = findViewById(R.id.myEmployeeRecycler);
        svEmployeeSearch = findViewById(R.id.sv_manageEmployeeActivity_name);
        txtTitle = findViewById(R.id.tv_manageEmployeeActivity_title);
        fillRecycler();

        companyViewModel.getDeletingRes().observe(this, resualt -> {
            Log.d(TAG, "onCreate: " + resualt.toString());
            if (resualt.getResualt().equals("100")) {
                Toast.makeText(this, "از لیست کارمندان خارج شد", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
        });

        permissionViewModel.getLiveErrorEditPermission().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        });

        permissionViewModel.getLiveEditPermissionRes().observe(this, resualt -> {
            if (resualt.getResualt().equals("100"))
                Toast.makeText(this, "موفقیت آمیز بود", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "خطایی رخ داد", Toast.LENGTH_SHORT).show();
        });

        svEmployeeSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.searchInEmployees(newText);
                searchMode = true;
                return false;
            }
        });

        svEmployeeSearch.setOnSearchClickListener(v -> {
            txtTitle.setVisibility(View.GONE);
            searchMode = true;
        });

        svEmployeeSearch.setOnCloseListener(() -> {
            svEmployeeSearch.setQuery("",false);
            if(!svEmployeeSearch.isIconfiedByDefault()){
                svEmployeeSearch.setIconifiedByDefault(true);
            }

            updateRecyclerView(employee);
            searchMode = false;
            txtTitle.setVisibility(View.VISIBLE);
            return false;
        });

        swpRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillRecycler();
            }
        });

    }

    private void updateRecyclerView(List<CompanyFollowerViewModel> employees) {
        employee = employees;
        adapter.updateData(employee);
    }

    public void fillRecycler() {
        try {
            employee.clear();
            for (CompanyFollowerViewModel cfv : baseCodeClass.getCompanyFollower()
            ) {
                String currentId = getEmployeeId(cfv.getUserID());
                if (currentId != null) {
                    cfv.getUserData().setErrorMsg(currentId);
                    employee.add(cfv);
                }
            }
        } catch (Exception e) {
            logMessage("ManageEmployee 100 >> " + e.getMessage(), this);
        }

        initRecyclerView();
    }

    public String getEmployeeId(String userID) {
        for (CompanyEmployeesClass employees : baseCodeClass.companyEmployees
        ) {
            if (userID.equals(employees.getUserID())) {
                Log.d(TAG, "fillRecycler: " + employees.getEmployeeID());
                return employees.getEmployeeID();
            }
        }
        return null;
    }

    public void initRecyclerView() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ManageEmployeeRecyclerViewAdapter(this, employee, this);
            recyclerView.setAdapter(adapter);
            if(swpRefresh.isRefreshing()){
                swpRefresh.setRefreshing(false);
            }
        } catch (Exception e) {
            logMessage("ManageEmployee 100 >> " + e.getMessage(), this);
        }
    }

    public void btnBack(View view) {
        finish();
    }

    private void onDelete(String employeeId, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("حذف کارمند");
        builder.setMessage("با تایید حذف، این کاربر از لیست کارمندان شما حذف خواهد شد.");
        builder.setPositiveButton("تایید", (dialog, which) -> {
            companyViewModel.callForDeletingEmployee(new EmployeeDeleting(baseCodeClass.getUserID(), baseCodeClass.getToken(), employeeId));
            adapter.removeItem(pos);
            dialog.dismiss();
            new GetOnlineInformationClass(this).companyEmployee(baseCodeClass.getCompanyID());

        });
        builder.setNeutralButton("لغو", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onSettingClicked(CompanyFollowerViewModel item, int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_employee_access, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        view.findViewById(R.id.btn_edit_permision).setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent intent = new Intent(this, PermissionsListActivity.class);
            intent.putExtra(PermissionsListActivity.INTENT_KEY_EMPLOYEE_ID, item.getUserData().getErrorMsg());
            startActivity(intent);
        });
        view.findViewById(R.id.btn_promote_to_admin).setOnClickListener(v -> {
            permissionViewModel.callForEditEmployeePermission(new EditPermission(baseCodeClass.getToken(), baseCodeClass.getUserID(), item.getUserData().getErrorMsg(), EmployeeStatus.ADMIN, false, 1));
        });

        view.findViewById(R.id.btn_promote_to_operator).setOnClickListener(v ->
        {
            permissionViewModel.callForEditEmployeePermission(new EditPermission(baseCodeClass.getToken(), baseCodeClass.getUserID(), item.getUserData().getErrorMsg(), EmployeeStatus.OPERATOR, false, 1));
        });
        view.findViewById(R.id.btn_delete).setOnClickListener(view1 -> {
            alertDialog.dismiss();
            onDelete(item.getUserData().getErrorMsg(), position);
        });
    }
}
