package com.example.rentapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class UserPosts extends AppCompatActivity {
    private FirebaseFirestore mFirestoreReference;
    public static ArrayAdapter<PostDetails> userPosts;
    public static ArrayList<PostDetails> userList=new ArrayList<>();
//    private final UserPosts context=this;
    private static final String TAG="UserPosts";
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        userPosts=new UserPostAdapter(this,R.layout.post_in_list_card,userList);
//        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        ListView listView=(ListView)findViewById(R.id.postListView);
        listView.setAdapter(userPosts);

        // Get instance
        mFirestoreReference=FirebaseFirestore.getInstance();
        // Register Listener
        String username = "+917000614559";
        mFirestoreReference.collection("Post_with_number").document(
                "user_number").collection("posts").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null) {
                    Toast.makeText(UserPosts.this,"Error while listening on listener",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(DocumentChange dc:value.getDocumentChanges()) {
                    switch(dc.getType()) {
                        case ADDED:
                            if(userPosts.getCount()==0) {
                                ProgressBar progressBar=(ProgressBar) findViewById(R.id.myprogressBar);
                                progressBar.setVisibility(View.GONE);
                            }

                            PostDetails obj=dc.getDocument().toObject(PostDetails.class);
                            userPosts.add(obj);
                            Toast.makeText(UserPosts.this,"Id of doc is "+dc.getDocument().getId(),Toast.LENGTH_SHORT).show();
                            break;
                        case MODIFIED:
                            int index=binary_search(dc.getDocument().getId());
                            if(index==-1) break;
                            userPosts.remove(userPosts.getItem(index));
                            obj=dc.getDocument().toObject(PostDetails.class);
                            userPosts.insert(obj,index);
                            userPosts.notifyDataSetChanged();
                            Toast.makeText(UserPosts.this,"Modify performed Successfully at index is "+index,Toast.LENGTH_SHORT).show();
                            break;
                        case REMOVED:
                            index=binary_search(dc.getDocument().getId());
                            Toast.makeText(UserPosts.this,"index is "+index,Toast.LENGTH_SHORT).show();
                            if(index==-1) break;
                            userList.remove(index);
                            userPosts.notifyDataSetChanged();
                            Log.d("mytag","Delete performed Successfully at index "+index);
                            Toast.makeText(UserPosts.this,"Delete performed Successfully at index  "+index,Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + dc.getType());
                    }
                }
            }
        });

        // Register listener to list view item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(UserPosts.this,CardDetails.class);
                Toast.makeText(UserPosts.this,"Adapter index n id "+userPosts.getItem(i).getPostId(),Toast.LENGTH_SHORT).show();
                intent.putExtra("PostDetails", userPosts.getItem(i));
                startActivity(intent);


            }
        });

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                userPosts.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    int binary_search(String postId) {
        int index=-1;
        int postID=Integer.parseInt(postId);
        int low=0,high=userPosts.getCount()-1;
        while(low<=high) {
            int mid=low+(high-low)/2;
            int temp=Integer.parseInt(userList.get(mid).getPostId());
            if(temp>=postID) {
                index=mid;
                high=mid-1;
            }
            else low=mid+1;
        }
        return index;
    }
}
