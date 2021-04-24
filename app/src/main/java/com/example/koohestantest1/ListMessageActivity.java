package com.example.koohestantest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.koohestantest1.ApiDirectory.MessageApi;
import com.example.koohestantest1.InfoDirectory.MessageManagerClass;
import com.example.koohestantest1.ViewModels.ContactListViewModel;
import com.example.koohestantest1.ViewModels.SendMessageViewModel;
import com.example.koohestantest1.ViewModels.SendReportViewModel;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.classDirectory.ListMessageRecyclerViewAdapter;

import io.reactivex.Single;
import retrofit2.Call;

public class ListMessageActivity extends AppCompatActivity implements MessageApi {

    private RecyclerView recyclerView;
    private Context mContext;
    private ListMessageRecyclerViewAdapter adapter = null;
    private List<ContactListViewModel> contactListViewModelss = new ArrayList<>();
    private BaseCodeClass baseCodeClass;
    private ImageView ivBack;

    private final Handler handler = new Handler();
    private final int timeInterval = 10 * 1000; // 10 sec
    private Toolbar toolbar;
    private MessageApi messageApi = this;
    private ImageView ivSearch;
    private TextView tvCompanyName;
    private androidx.appcompat.widget.SearchView searchView;
    private String userIdStr;

    private boolean searchMode = false;

    private final String TAG = ListMessageActivity.class.getSimpleName() + "Debug";

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new MessageManagerClass(mContext, messageApi).getContact(userIdStr);
            handler.postDelayed(runnable, timeInterval);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_message);
        userIdStr =getIntent().getStringExtra("id");

        recyclerView = findViewById(R.id.listofSender);
        toolbar = findViewById(R.id.toolbar_list_message);
        ivBack = findViewById(R.id.iv_back_messages_list);
        mContext = ListMessageActivity.this;
        baseCodeClass = new BaseCodeClass();
        tvCompanyName = findViewById(R.id.tv_company_name);
        searchView = findViewById(R.id.sv_name_search);

        tvCompanyName.setText(BaseCodeClass.CompanyName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListMessageRecyclerViewAdapter(this, contactListViewModelss, userIdStr);
        recyclerView.setAdapter(adapter);

        ivBack.setOnClickListener(v -> {
            finish();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.searchInList(newText);
                searchMode = true;

                return false;
            }
        });

        searchView.setOnSearchClickListener(v -> {
            searchMode = true;
            Toast.makeText(this, "cl2", Toast.LENGTH_SHORT).show();

        });
        searchView.setOnCloseListener(() -> {
            Toast.makeText(this, "cl", Toast.LENGTH_SHORT).show();
            searchView.setQuery("", false);

            if (!searchView.isIconfiedByDefault()) {
                searchView.setIconifiedByDefault(true);
            }

            updateRecyclerView(contactListViewModelss);
            searchMode = false;
            return false;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //update view each 30 second
        startRepeatingTask();
    }

    void startRepeatingTask() {
        runnable.run();
    }

    void stopRepeatingTask() {
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRepeatingTask();
        Log.d(TAG, "onStop: ");
    }

    public void updateRecyclerView(List<ContactListViewModel> contactListViewModels) {
        contactListViewModelss = contactListViewModels;
        adapter.updateData(contactListViewModelss);
    }

    @Override
    public Call<GetResualt> sendMessage(SendMessageViewModel sendMessage) {
        return null;
    }

    @Override
    public void onResponseSendMessage(GetResualt getResualt) {

    }

    @Override
    public Single<GetResualt> sendAMessage(SendMessageViewModel sendMessage) {
        return null;
    }

    @Override
    public Call<GetResualt> sendReport(SendReportViewModel sendReportViewModel) {
        return null;
    }

    @Override
    public void onResponseSendReport(GetResualt getResualt) {

    }

    @Override
    public Call<List<SendMessageViewModel>> getMessage(SendMessageViewModel sendMessageViewModel) {
        return null;
    }

    @Override
    public void onResponseGetMessage(List<SendMessageViewModel> sendMessageViewModels) {

    }

    @Override
    public Call<List<ContactListViewModel>> getContact(String token, String userID, String objectID) {
        return null;
    }

    @Override
    public void onResponseGetContact(List<ContactListViewModel> contactListViewModels) {
        if (contactListViewModels != null) {
            if (contactListViewModels.size() > 0) {
                if (!searchMode)
                    updateRecyclerView(contactListViewModels);
            }
        }
    }

    @Override
    public Call<List<ContactListViewModel>> getContactV2(String token, String userID, String objectID) {
        return null;
    }

    @Override
    public void onResponseGetContactV2(List<ContactListViewModel> contactListViewModels) {

    }
}
