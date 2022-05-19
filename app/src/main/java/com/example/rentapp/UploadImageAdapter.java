package com.example.rentapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.UploadImageHolder> {

    ArrayList<Uri> post_images;
    public UploadImageAdapter(ArrayList<Uri> img){
        this.post_images=img;

    }
    @NonNull
    @Override
    public UploadImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_image_item,parent,false);
        return new UploadImageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UploadImageHolder holder, int position) {
        Glide.with(holder.image.getContext()).load(post_images.get(position)).fitCenter().into(holder.image);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_images.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,post_images.size());
            }
        });

    }



    @Override
    public int getItemCount() {
        return post_images.size();
    }
    public static class UploadImageHolder extends RecyclerView.ViewHolder {
        ImageView image;
        Button delete;
        public UploadImageHolder(@NonNull View itemView) {
            super(itemView);
            this.image=itemView.findViewById(R.id.image);
            this.delete=itemView.findViewById(R.id.delete);
        }



    }


}

