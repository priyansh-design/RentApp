package com.example.rentapp.Add_Post.Model;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rentapp.Add_Post.View.AddPost;
import com.example.rentapp.PostDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddPostModel {
    private int countUploadedPhotos;
    private ArrayList<String> fireuri=new ArrayList<String>();
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private String username,postId=null;
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private ArrayList<Object> receivedData;
    private PostDetails postDetails;
    private ArrayList<Uri> uriList;
    private AddPost context;

    public AddPostModel(ArrayList<Object> dataToModel) {
        receivedData=dataToModel;
        context=(AddPost) receivedData.get(0);
        uriList=(ArrayList<Uri>) receivedData.get(1);
        postDetails=(PostDetails) receivedData.get(2);
        ArrayList<String> facilities=postDetails.getAmmenities();
        username= facilities.get(facilities.size() - 1);
        facilities.remove(facilities.size() - 1);
    }
    public boolean uploadOnFirebase() {
        uploadPostDetailFollowedByImages();
        return true;
    }

    void uploadPostDetailFollowedByImages() {
        firebaseFirestore.collection("Users").document(username).collection("Posts").add(postDetails).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    DocumentReference docRef = task.getResult();
                    postId = docRef.getId();
                    Log.v("post id", postId);
                    addInPostByCity(postId);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailedUpload(e.getMessage());
            }
        });
    }

    private void addInPostByCity(String postId) {
        Map<String,Object> map=new HashMap<>();
        firebaseFirestore.collection("PostByCity").document(postDetails.getCity()).collection(username).document(postId).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(uriList.size()==0) {
                    onSuccessfulUpload();
                }
                else {
                    uploadImagestoStorage(uriList,postId);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailedUpload(e.getMessage());
            }
        });

    }

    private  synchronized void uploadImagestoStorage(ArrayList<Uri> images,String postId){
        Log.d("mytag","upload wale function ke andar hai apun");

        try {
            StorageReference fileref=reference.child(username).child(postId);
            for(int i=0;i<images.size();i++){
                Log.d("mytag","for loop ke andar hai apun");
                try {
                    int index=i;
                    fileref.child(Integer.toString(i)).putFile(images.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                Log.d("mytag","get download ke andar hai apun");
                                fileref.child(Integer.toString(index)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        fireuri.add(uri.toString());

                                        Log.d("mytag",uri.toString());

                                        firebaseFirestore.collection("Users").document(username).collection("Posts").document(postId).update("uri_list",fireuri).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                countUploadedPhotos++;
                                                if(countUploadedPhotos==uriList.size()) {
                                                    onSuccessfulUpload();
                                                }
//                                                Toast.makeText(AddPost.this, "uri added to firestore", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(AddPost.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                        onFailedUpload(e.getMessage());
                                    }
                                });

                            } catch (Exception e){
                                Log.d("mytag-during",e.getMessage());
                                onFailedUpload(e.getMessage());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(AddPost.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            onFailedUpload(e.getMessage());
                        }
                    });

                } catch (Exception e) {
                    Log.d("mytag-upload",e.getMessage());
                    onFailedUpload(e.getMessage());
                }

            }

        } catch (Exception e){
            Log.d("mytag-upload",e.getMessage());
            onFailedUpload(e.getMessage());
        }
    }
    void onFailedUpload(String error) {
        Log.d("My-tag","Nhi ho paya");
        // In order to make operation atomic, delete data from firestore if exists
        if(postId!=null) {
            // delete documen from Users/username/ if exists
            firebaseFirestore.collection("Users").document(username).collection("Posts").document(postId).delete();
            //delete document from PostById/ if exists
            firebaseFirestore.collection("PostByCity").document(postDetails.getCity()).collection(username).document(postId).delete();
        }
        context.onFailedUpload();
    }
    void onSuccessfulUpload() {
        Log.d("My-tag","Krr dala mene");
        context.onSuccessfulUpload();
    }

}
