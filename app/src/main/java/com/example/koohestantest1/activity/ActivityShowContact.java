package com.example.koohestantest1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.MessageActivity;
import com.example.koohestantest1.R;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.adapter.AdapterContactList;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.model.ForwardMsgM;
import com.example.koohestantest1.viewModel.ContactVM;
import com.example.koohestantest1.viewModel.SendMessageVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityShowContact extends AppCompatActivity implements AdapterContactList.OnItemClickContactList {
    private RecyclerView recyclerView;
    private ImageView imgBack;
    private BaseCodeClass baseCodeClass;
    private AdapterContactList adapter;
    private int countSelect = 0;
    private LinearLayout linearForwarders;
    private TextView txtContactForwarderName;
    private FloatingActionButton fbForward;
    private List<String> forwardersName;
    private List<String> forwardersId;
    private List<ContactListViewModel> contactList = new ArrayList<>();
    private androidx.appcompat.widget.SearchView searchView;
    private ForwardMsgM forwardMsgM;
    private String msgId, senderMsgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        setupViews();
        msgId = getIntent().getStringExtra("msgId");
        senderMsgId = getIntent().getStringExtra("senderMsgId");
        imgBack.setOnClickListener(v -> {
            finish();
        });
        getContact();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.searchInList(newText);
                if (newText.equals("")) {
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
        contactVM.getContact(baseCodeClass.getToken(), baseCodeClass.getUserID(), senderMsgId).observe(this, new Observer<List<ContactListViewModel>>() {
            @Override
            public void onChanged(List<ContactListViewModel> contactListViewModels) {
                contactList = contactListViewModels;
                adapter = new AdapterContactList(ActivityShowContact.this, contactListViewModels, baseCodeClass.getUserID(), ActivityShowContact.this::onContactClicked);
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
        forwardersName = new ArrayList<>();
        forwardersId = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        baseCodeClass = new BaseCodeClass();
    }

    public void updateRecyclerView(List<ContactListViewModel> contactListViewModels) {
        contactList = contactListViewModels;
        adapter.updateData(contactList);
    }


    @Override
    public void onContactClicked(ImageView imageView, String contactName, String contactId) {
        if (countSelect < 5) {
            if (imageView.getVisibility() == View.GONE) {
                imageView.setVisibility(View.VISIBLE);
                countSelect++;
                forwardersName.add(contactName);
                forwardersId.add(contactId);
                txtContactForwarderName.setText(forwardersName.toString());
                if (linearForwarders.getVisibility() == View.GONE) {
                    linearForwarders.setVisibility(View.VISIBLE);
                }
            } else {
                imageView.setVisibility(View.GONE);
                countSelect--;
                forwardersName.remove(contactName);
                forwardersId.remove(contactId);
                txtContactForwarderName.setText(forwardersName.toString());

            }
        } else {
            if (imageView.getVisibility() == View.VISIBLE) {
                imageView.setVisibility(View.GONE);
                countSelect--;
                forwardersName.remove(contactName);
                forwardersId.remove(contactId);
                txtContactForwarderName.setText(forwardersName.toString());

            } else {
                Toast.makeText(ActivityShowContact.this, "تعداد افرادی که می توانید برای آن ها پیام ارسال کنید محدود است!", Toast.LENGTH_SHORT).show();

            }

        }

        if (countSelect == 0) {
            linearForwarders.setVisibility(View.GONE);
            forwardersName.clear();
            forwardersId.clear();
            txtContactForwarderName.setText("");
        }


        fbForward.setOnClickListener(v -> {
            forwardMsgM = new ForwardMsgM(baseCodeClass.getToken(), baseCodeClass.getUserID(), msgId, senderMsgId, forwardersId);

            SendMessageVM messageVM = new ViewModelProvider(this).get(SendMessageVM.class);
            messageVM.forwardMessage(forwardMsgM).observe(this, getResualt -> {
                if (getResualt.getResualt().equals("100")) {
                    Toast.makeText(ActivityShowContact.this, "پیام ارسال شد", Toast.LENGTH_SHORT).show();
                    if (forwardersId.size()==1){
                        Intent intent = new Intent(ActivityShowContact.this, MessageActivity.class);
                        intent.putExtra("getter", forwardersId.get(0));
                        // sender = Ourselves
                        intent.putExtra("sender", senderMsgId);
                        startActivity(intent);
                        finish();
                    }else {
                        finish();

                    }
                }else {
                    Toast.makeText(ActivityShowContact.this, "خطای ناشناخته!", Toast.LENGTH_SHORT).show();

                }


            });
        });

    }

}