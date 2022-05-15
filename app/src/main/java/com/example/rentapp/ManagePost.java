package com.example.rentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ManagePost extends AppCompatActivity {
    PostDetails postDetails;
    private String TAG="ManagePost";
    private FirebaseFirestore mFirebaseFirestore;
    private String username="+917000614559";
    private ManagePost context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_post);
        postDetails= getIntent().getParcelableExtra("PostDetails");
        String idOfPost=postDetails.getPostId();
        Toast.makeText(this,"id received in manage post "+idOfPost,Toast.LENGTH_SHORT).show();
        Toast.makeText(context,"Adapter index n id "+postDetails.getName(),Toast.LENGTH_SHORT).show();
        PostDetails postDetailsUpdated=new PostDetails("1","jgf","jgf","jgf","jgf","RS 550000","jgf","jgf","jgf","jgf",new ArrayList<String>(),new ArrayList<String>(),"1");
        mFirebaseFirestore=FirebaseFirestore.getInstance();
//        mFirebaseFirestore.collection("Users").document(username).collection("posts").document(idOfPost).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.v(TAG, "DocumentSnapshot successfully deleted!");
//                context.finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.v(TAG, "Error deleting document", e);
//            }
//        });
        try{
            mFirebaseFirestore.collection("Post_with_number").document("user_number").collection("posts").document(idOfPost).set(postDetailsUpdated).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Data is modified", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "something fishy", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Log.d("mytag",e.getMessage());}
//        context.finish();
    }
}