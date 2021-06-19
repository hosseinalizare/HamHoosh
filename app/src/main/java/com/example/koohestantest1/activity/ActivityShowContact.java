package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.adapter.AdapterContactList;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.viewModel.ContactVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityShowContact extends AppCompatActivity implements AdapterContactList.OnItemClickContactList{
    private RecyclerView recyclerView;
    private ImageView imgBack;
    private BaseCodeClass baseCodeClass;
    private AdapterContactList adapter;
    private int countSelect =0;
    private LinearLayout linearForwarders;
    private TextView txtContactForwarderName;
   private FloatingActionButton fbForward;
   private List<String> forwardersName;
    private List<ContactListViewModel> contactList = new ArrayList<>();
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        setupViews();
        imgBack.setOnClickListener(v -> {
            finish();
        });
        getContact();
        fbForward.setOnClickListener(v -> {
            Toast.makeText(ActivityShowContact.this, forwardersName.size()+"", Toast.LENGTH_SHORT).show();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.searchInList(newText);
                if (newText.equals("")){
                    updateRecyclerView(contactList);
                }
                return false;
            }

        });


        searchView.setOnCloseListener(() -> {
            searchView.setQuery("", false);

            if (!searchView.isIconfiedByDefault()) {
                searchView.setIconifiedByDefault(true);
            }

            updateRecyclerView(contactList);
            return false;
        });

    }

    private void getContact() {
        ContactVM contactVM = new ViewModelProvider(this).get(ContactVM.class);
        contactVM.getContact(baseCodeClass.getToken(), baseCodeClass.getUserID(),baseCodeClass.getUserID()).observe(this, new Observer<List<ContactListViewModel>>() {
            @Override
            public void onChanged(List<ContactListViewModel> contactListViewModels) {
                contactList = contactListViewModels;
                adapter = new AdapterContactList(ActivityShowContact.this,contactListViewModels,baseCodeClass.getUserID(),ActivityShowContact.this::onContactClicked);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void setupViews() {
        searchView = findViewById(R.id.sv_name_search);
        recyclerView = findViewById(R.id.listofContact);
        imgBack = findViewById(R.id.iv_back_contact_list);
        linearForwarders = findViewById(R.id.linear_showForwarders);
        txtContactForwarderName = findViewById(R.id.txt_activity_show_contact_txtContactName);
        fbForward = findViewById(R.id.fb_activityShowContact_fbMessage);
        forwardersName =  new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        baseCodeClass = new BaseCodeClass();
    }

    public void updateRecyclerView(List<ContactListViewModel> contactListViewModels) {
        contactList = contactListViewModels;
        adapter.updateData(contactList);
    }


    @Override
    public void onContactClicked(ImageView imageView,String contactName) {
        if (countSelect <5){
            if (imageView.getVisibility() == View.GONE){
                imageView.setVisibility(View.VISIBLE);
                countSelect++;
                forwardersName.add(contactName);
                txtContactForwarderName.setText(forwardersName.toString());
                if (linearForwarders.getVisibility() == View.GONE){
                    linearForwarders.setVisibility(View.VISIBLE);
                }
            }else {
                imageView.setVisibility(View.GONE);
                countSelect--;
                forwardersName.remove(contactName);
                txtContactForwarderName.setText(forwardersName.toString());

            }
        }else {
            if (imageView.getVisibility() == View.VISIBLE){
                imageView.setVisibility(View.GONE);
                countSelect--;
                forwardersName.remove(contactName);
                txtContactForwarderName.setText(forwardersName.toString());

            }else {
                Toast.makeText(ActivityShowContact.this, "تعداد افرادی که می توانید برای آن ها پیام ارسال کنید محدود است!", Toast.LENGTH_SHORT).show();

            }

        }

        if (countSelect ==0){
            linearForwarders.setVisibility(View.GONE);
            forwardersName.clear();
            txtContactForwarderName.setText("");
        }

    }

}