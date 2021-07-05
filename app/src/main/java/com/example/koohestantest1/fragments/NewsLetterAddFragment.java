package com.example.koohestantest1.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koohestantest1.ApiDirectory.JsonApi;
import com.example.koohestantest1.R;
import com.example.koohestantest1.Utils.Cache;
import com.example.koohestantest1.Utils.FileUtils;
import com.example.koohestantest1.classDirectory.BaseCodeClass;
import com.example.koohestantest1.classDirectory.GetResualt;
import com.example.koohestantest1.model.NewsLetterModel;
import com.example.koohestantest1.model.network.RetrofitInstance;
import com.iceteck.silicompressorr.SiliCompressor;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.koohestantest1.fragments.NewsLetterFragment.isGet;

public class NewsLetterAddFragment extends Fragment {

    private EditText edtTitle, edtDesc, edtCat;
    private AppCompatButton btnAdd;
    private int READ_REQUEST_CODE = 200;
    private List<Uri> receivedUriList;
    private List<Uri> videoUriList;
    private List<MultipartBody.Part> finalFiles;
    private List<Uri> imageUriList;
    private List<String> partNames;
    private String title;
    private String description;
    private String compressedVideoPath;
    private String category;
    private TextView txtSetting;
    private CheckBox chkPublish, chkComment, chkLike, chkSave;
    private ProgressBar pbLoading;
    private boolean isPublish = true,
            isComment = true,
            isLike = true,
            isSave = true;

    private LinearLayout linSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_letter_add, container, false);
        edtTitle = view.findViewById(R.id.edt_addNewsLetterFragment_title);
        edtDesc = view.findViewById(R.id.edt_addNewsLetterFragment_desc);
        edtCat = view.findViewById(R.id.edt_addNewsLetterFragment_category);
        btnAdd = view.findViewById(R.id.btn_addNewsLetterFragment_add);
        chkPublish = view.findViewById(R.id.chk_addNewsLetterFragment_showNewsLetter);
        chkComment = view.findViewById(R.id.chk_addNewsLetterFragment_comment);
        chkLike = view.findViewById(R.id.chk_addNewsLetterFragment_like);
        chkSave = view.findViewById(R.id.chk_addNewsLetterFragment_save);
        linSetting = view.findViewById(R.id.lin_addNewsLetterFragment_setting);
        txtSetting = view.findViewById(R.id.txt_addNewsLetterFragment_setting);
        pbLoading = view.findViewById(R.id.pb_addNewsLetterFragment_loading);
        receivedUriList = new ArrayList<>();
        videoUriList = new ArrayList<>();
        finalFiles = new ArrayList<>();
        partNames = new ArrayList<>();
        imageUriList = new ArrayList<>();
        receivedUriList = getArguments().getParcelableArrayList("uriList");

        for (int i = 0; i < receivedUriList.size(); i++) {
            partNames.add(i + "");
        }
        /*ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            if (result.getData().getClipData() != null) {
                                int count = result.getData().getClipData().getItemCount();
                                int currentItem = 0;
                                while (currentItem < count) {
                                    Uri imageUri = result.getData().getClipData().getItemAt(currentItem).getUri();
                                    imageUriList.add(imageUri);
                                    partNames.add(currentItem + "");
                                    currentItem++;
                                }
                            } else if (result.getData().getData() != null) {
                                imageUriList.add(result.getData().getData());
                            }
                        }


                        NewsLetterModel newsLetter = new NewsLetterModel();
                        newsLetter.setShow(isPublish);
                        newsLetter.setActiveComment(isComment);
                        newsLetter.setActiveLike(isLike);
                        newsLetter.setActiveSave(isSave);
                        newsLetter.setCategory(category);
                        newsLetter.setCompanyId(BaseCodeClass.CompanyID);
                        newsLetter.setCreatorId(BaseCodeClass.userID);
                        newsLetter.setLinkOut("");
                        newsLetter.setLinkToInstagram("");
                        newsLetter.setNewsDescription(description);
                        newsLetter.setNewsTitle(title);
                        newsLetter.setSpare1("#FFFFFF");
                        newsLetter.setSpare2("#FFFFFF");
                        newsLetter.setSpare3("#FFFFFF");
                        newsLetter.setToken(BaseCodeClass.token);
                        newsLetter.setUserId(BaseCodeClass.userID);
                        uploadNewsLetter(newsLetter);

                    }
                }
        );*/

        /*ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                result -> {
                    if (result) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        Intent chooserIntent = Intent.createChooser(intent, "Open Gallery");

                        mLauncher.launch(chooserIntent);
                    } else {
                        Toast.makeText(getContext(), "FAIL", Toast.LENGTH_SHORT).show();
                    }
                }
        );*/

        btnAdd.setOnClickListener(v -> {
            title = edtTitle.getText().toString();
            description = edtDesc.getText().toString();
            category = edtCat.getText().toString();
            if ((title.isEmpty() || title.equals("")) || (description.isEmpty() || description.equals(""))
                    || (category.isEmpty() || category.equals(""))) {
                Toast.makeText(getContext(), "همه فیلدها را پر کنید", Toast.LENGTH_SHORT).show();
            } else {
//                mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                pbLoading.setVisibility(View.VISIBLE);
                NewsLetterModel newsLetter = new NewsLetterModel();
                newsLetter.setShow(isPublish);
                newsLetter.setActiveComment(isComment);
                newsLetter.setActiveLike(isLike);
                newsLetter.setActiveSave(isSave);
                newsLetter.setCategory(category);
                newsLetter.setCompanyId(BaseCodeClass.CompanyID);
                newsLetter.setCreatorId(BaseCodeClass.userID);
                newsLetter.setLinkOut("");
                newsLetter.setLinkToInstagram("");
                newsLetter.setNewsDescription(description);
                newsLetter.setNewsTitle(title);
                newsLetter.setSpare1("#FFFFFF");
                newsLetter.setSpare2("#FFFFFF");
                newsLetter.setSpare3("#FFFFFF");
                newsLetter.setToken(BaseCodeClass.token);
                newsLetter.setUserId(BaseCodeClass.userID);
                newsLetter.setLikeIt(false);
                newsLetter.setSaveIt(false);
                newsLetter.setLikeCount(0);
                newsLetter.setSaveCount(0);
                newsLetter.setViewedCount(0);
                uploadNewsLetter(newsLetter);
            }
        });

        chkLike.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isLike = isChecked;
        });

        chkComment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isComment = isChecked;
        });

        chkPublish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isPublish = isChecked;
        });

        chkSave.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSave = isChecked;
        });

        txtSetting.setOnClickListener(v -> {
            if (linSetting.getVisibility() == View.VISIBLE)
                linSetting.setVisibility(View.GONE);
            else
                linSetting.setVisibility(View.VISIBLE);
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void uploadNewsLetter(NewsLetterModel newsLetter) {
        boolean isLarge = checkVideoSize(receivedUriList);
        if (isLarge) {
            Toast.makeText(getContext(), "حجم فایل تصویری باید کمتر از 50 مگابایت باشد", Toast.LENGTH_SHORT).show();
            pbLoading.setVisibility(View.GONE);
            return;
        }

        Retrofit retrofit;
        JsonApi api;
        retrofit = RetrofitInstance.getRetrofit();
        api = retrofit.create(JsonApi.class);
        Call<GetResualt> call = api.setNewsLetter(newsLetter);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                if (response.body().getResualt().equals("100")) {
                    String newsId = response.body().getMsg();
                    List<MultipartBody.Part> files = new ArrayList<>();
                    List<MultipartBody.Part> videoFiles = new ArrayList<>();
                    if (partNames.size() == 0) {
                        partNames.add("0");
                    }
                    for (Uri fileUri : receivedUriList) {
//                        String filePath = fileUri.getPath();
//                        String fileExtension = filePath.substring(filePath.lastIndexOf("."));
                        String fileExtension = getMimeType(getContext(), fileUri);
                        if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png"))
                            imageUriList.add(fileUri);
                        else if (fileExtension.equals("mp4")) {
                            videoUriList.add(fileUri);
                        }

                    }
                    if (videoUriList != null && videoUriList.size() > 0) {
                        for (Uri videoUri : videoUriList) {
                            File file = FileUtils.getFile(getContext(), videoUri);
                            RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_VIDEO), file);
                            videoFiles.add(MultipartBody.Part.createFormData(file.getName(), file.getName(), requestFile));
                        }
                    }
                    if (imageUriList != null && imageUriList.size() > 0)
                        files = convertUriToFIle(partNames, imageUriList, newsId);
                    finalFiles.addAll(files);
                    finalFiles.addAll(videoFiles);

                    uploadNewsImages(newsId, finalFiles);

                }
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
                Log.e("Error", t.getMessage());
            }
        });

    }

    private boolean checkVideoSize(List<Uri> uri) {
        boolean result = false;
        for (Uri fileUri : uri) {
            String fileExtension = getMimeType(getContext(), fileUri);
            if (fileExtension.equals("mp4")) {
                File file = FileUtils.getFile(getContext(), fileUri);
                RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_VIDEO), file);
                try {
                    long fileSize = requestFile.contentLength();
                    if ((fileSize / 1000000) > 50) {
                        result = true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    private List<MultipartBody.Part> convertUriToFIle(List<String> partNames, List<Uri> imageUriList, String newsId) {
        List<MultipartBody.Part> files = new ArrayList<>();
        for (int i = 0; i < imageUriList.size(); i++) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUriList.get(i));
                Cache cache = new Cache(getContext());
                File imageFile = cache.saveToCacheAndGetFile2(bitmap, newsId);
                long fileSize = imageFile.length() / 1024;
                int quality = (int) Math.abs((fileSize - 3775) / 61.25);
                if (quality < 20)
                    quality = 20;
                else if (quality > 100)
                    quality = 100;

                Bitmap imageBitmap = new Compressor(getContext())
                        .setMaxWidth(1080)
                        .setMaxHeight(1080)
                        .setQuality(quality)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .compressToBitmap(imageFile);
                Cache cacheCompressed = new Cache(getContext());
                File compressedFile = cacheCompressed.saveToCacheAndGetFile2(imageBitmap, newsId + i);
                //File file = FileUtils.getFile(getContext(),imageUriList.get(i));
                RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_IMAGE), compressedFile);
                files.add(MultipartBody.Part.createFormData(partNames.get(i), compressedFile.getName(), requestFile));
            } catch (IOException e) {
                pbLoading.setVisibility(View.GONE);
                e.printStackTrace();
            }

        }
        return files;
    }

    private String getMimeType(Context context, Uri uri) {
        String extension;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }

        return extension;
    }

    private void uploadNewsImages(String newsLetterId, List<MultipartBody.Part> files) {
        Retrofit retrofit;
        JsonApi api;
        retrofit = RetrofitInstance.getRetrofit();
        api = retrofit.create(JsonApi.class);
        Call<GetResualt> call = api.uploadNewsLetterImage(newsLetterId, BaseCodeClass.CompanyID, BaseCodeClass.userID, BaseCodeClass.token, files);
        call.enqueue(new Callback<GetResualt>() {
            @Override
            public void onResponse(Call<GetResualt> call, Response<GetResualt> response) {
                if (response.body().getResualt().equals("100")) {
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "خبر با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                    isGet = false;
                    Fragment newsFragment = new NewsLetterFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frm_newsLetterActivity_layout, newsFragment);
                    transaction.commit();
                } else {
                    pbLoading.setVisibility(View.GONE);
                    Log.e("Error", response.body().getResualt() + " " + response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GetResualt> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }


}