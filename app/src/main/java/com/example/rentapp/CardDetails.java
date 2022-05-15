package com.example.rentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import static com.example.rentapp.UserPosts.userList;
import static com.example.rentapp.UserPosts.userPosts;

public class CardDetails extends AppCompatActivity {
    private CardDetails context=this;
    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<String> images_from_database=new ArrayList<>();
    ExtendedFloatingActionButton edit,delete,back_btn;
    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();

    SliderView sliderView;
    private String TAG="mytag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        edit=findViewById(R.id.edit_button);
        delete=findViewById(R.id.delete_button);
        back_btn=findViewById(R.id.back_btn);
        PostDetails postDetails=getIntent().getParcelableExtra("PostDetails");
        sliderView=findViewById(R.id.imageslider);
        images_from_database=postDetails.getUri_list();




        SliderAdpater sliderAdpater=new SliderAdpater(images_from_database);
        sliderView.setSliderAdapter(sliderAdpater);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
        Bundle bundle=new Bundle();
        bundle.putParcelable("PostDetails", postDetails);


        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter=new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new OverviewFragment(bundle),"Overview");
        vpAdapter.addFragment(new OtherDetailsFragment(bundle),"Other Details");
        viewPager.setAdapter(vpAdapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisPost(postDetails.postId);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void deleteThisPost( String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want delete this?");
        builder.setCancelable(false);


        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                mFirebaseFirestore.collection("Post_with_number").document("user_number").collection("posts").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                int index=binary_search(id);
                Toast.makeText(CardDetails.this,"index is "+index,Toast.LENGTH_SHORT).show();
                if(index==-1) return;
                userList.remove(index);
                userPosts.notifyDataSetChanged();
                Log.d("mytag","Delete performed Successfully at index "+index);
                Toast.makeText(CardDetails.this,"Delete performed Successfully at index  "+index,Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error deleting document", e);
            }
        });


                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        AlertDialog alertDialog=builder.create();
        alertDialog.show();


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