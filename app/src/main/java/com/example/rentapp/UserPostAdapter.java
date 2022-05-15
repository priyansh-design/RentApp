package com.example.rentapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserPostAdapter extends ArrayAdapter<PostDetails> {
    public UserPostAdapter(Context context, int resource, ArrayList<PostDetails> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View listItemView, @NonNull ViewGroup parent) {
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.post_in_list_card, parent, false);
        }
        ImageView image=listItemView.findViewById(R.id.house_image);

        TextView addressView=(TextView) listItemView.findViewById(R.id.address);
        TextView rentAmoutView=(TextView) listItemView.findViewById(R.id.rent_amount);
        TextView bhkView=(TextView) listItemView.findViewById(R.id.bhk);
        TextView area=listItemView.findViewById(R.id.square_foot);
        PostDetails postDetail=getItem(position);
        addressView.setText(postDetail.getAddress());
        rentAmoutView.setText(postDetail.getRent_amount());
        area.setText(postDetail.getBuildup_area()+"sqft");

        bhkView.setText(postDetail.getBhk()+"bhk");
        if(postDetail.uri_list.size()!=0){
            Picasso.get().load(postDetail.uri_list.get(0)).into(image);
        }
        else{
            image.setImageResource(R.drawable.no_image);
        }

        return listItemView;
    }
}
