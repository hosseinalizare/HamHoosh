package com.example.koohestantest1.fragments.bottomsheet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koohestantest1.adapter.CommentsRecyclerAdapter;
import com.example.koohestantest1.databinding.BottomSheetFragmentCommentsBinding;
import com.example.koohestantest1.model.GetComments;
import com.example.koohestantest1.model.PublishComment;
import com.example.koohestantest1.model.SaveCommentBody;
import com.example.koohestantest1.viewModel.CommentVM;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;

public class CommentsBottomSheet extends BottomSheetDialogFragment implements CommentsRecyclerAdapter.DeleteCommentClickListener {
    private BottomSheetFragmentCommentsBinding binding;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private CommentVM commentVM;
    private SaveCommentBody saveCommentBody;
    private String objectId,userId,token,cid;
    private EditText edtComment;
    private List<GetComments> commentsList;
    RecyclerView rclComments;
    public CommentsBottomSheet(String objectId, String userId, String token,String cid) {
        this.objectId = objectId;
        this.userId = userId;
        this.token = token;
        this.cid = cid;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentVM = new ViewModelProvider(requireActivity()).get(CommentVM.class);

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetFragmentCommentsBinding.inflate(inflater, container, false);

        edtComment =binding.edtBottomsheetSendComment;
        rclComments = binding.rvComments;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getComments(objectId,userId,cid);


        binding.ivCloseComments.setOnClickListener(v -> {
            dismiss();
        });

        binding.imgBottomsheetSendComment.setOnClickListener(v -> {
            String commentText = edtComment.getText().toString();
            if (!commentText.isEmpty()) {

                saveComment(new SaveCommentBody(objectId,userId,token,commentText));
            }

        });
    }

    public void saveComment(SaveCommentBody saveCommentBody){
        commentVM.saveComment(saveCommentBody).observe(requireActivity(), getResualt -> {
            if (getResualt.getResualt().equals("100")){
                edtComment.setText("");
                Toast.makeText(getContext(), "نظر شما ثبت و پس از تایید نمایش داده خواهد شد", Toast.LENGTH_SHORT).show();
                getComments(objectId,userId,cid);
            }else {
                Toast.makeText(getContext(), "خطای ناشناخته!", Toast.LENGTH_SHORT).show();

            }

        });

    }
    public void getComments(String objectId,String uid,String cid){

        commentVM.getComments(objectId,uid,cid).observe(requireActivity(), getComments -> {
            commentsList = getComments;
            rclComments.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false));
            rclComments.setHasFixedSize(true);
            commentsRecyclerAdapter = new CommentsRecyclerAdapter(getContext(),getComments,this);
            rclComments.setAdapter(commentsRecyclerAdapter);

        });


    }

    @Override
    public void onImgDeleteCommentListener(String objectId, String userId, String data, int position) {
        commentVM.deleteComment(new SaveCommentBody(objectId,userId,token,data)).observe(requireActivity(), new Observer<GetResualt>() {
            @Override
            public void onChanged(GetResualt getResualt) {
                if (getResualt.getResualt().equals("100")){
                    commentsList.remove(position);
                    commentsRecyclerAdapter. notifyItemRemoved(position);
                   commentsRecyclerAdapter. notifyItemRangeChanged(position, commentsList.size());
                    Toast.makeText(getContext(), "نظر مربوطه حذف شد", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "خطای ناشناخته!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onImgShareCommentListener(String objectId, String userId, String token, String data, ImageView imageView, CardView cardView) {
        commentVM.publishComment(new PublishComment(objectId,userId,token,data)).observe(requireActivity(), new Observer<GetResualt>() {
            @Override
            public void onChanged(GetResualt getResualt) {
                if (getResualt.getResualt().equals("100")){
                    Toast.makeText(getContext(), "نظر مربوطه منتشر شد", Toast.LENGTH_SHORT).show();
                    imageView.setVisibility(View.GONE);
                    cardView.setBackgroundColor(Color.WHITE);
                }else {
                    Toast.makeText(getContext(), "خطای ناشناخته", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


}
