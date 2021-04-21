package com.example.koohestantest1.ApiDirectory;

import com.example.koohestantest1.model.GetComments;
import com.example.koohestantest1.model.PublishComment;
import com.example.koohestantest1.model.SaveCommentBody;

import java.util.List;

import com.example.koohestantest1.classDirectory.GetResualt;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentApi {

      @POST("Products/SaveComment")
      Single<GetResualt> saveComment(@Body SaveCommentBody saveCommentBody);

      @POST("Products/GetComment")
      Single<List<GetComments>> getComments(@Query("ObjectID") String ObjectID,@Query("UID") String uid,@Query("CID") String cid);

      @POST("Products/DeletComment")
      Single<GetResualt> deleteComment(@Body SaveCommentBody saveCommentBody);

      @POST("Products/PulishComment")
      Single<GetResualt> publishComment(@Body PublishComment publishComment);



}
