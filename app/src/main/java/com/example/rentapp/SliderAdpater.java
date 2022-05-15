package com.example.rentapp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdpater  extends SliderViewAdapter<SliderAdpater.Holder> {

    ArrayList<String> arrayList;

    public SliderAdpater(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public class Holder extends SliderViewAdapter.ViewHolder{
        ImageView image;
        ProgressBar progressBar;

        public Holder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.imageofslider);
            progressBar=itemView.findViewById(R.id.progress_in_slide);
        }
    }
    @Override
    public  Holder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {

        Picasso.get().load(arrayList.get(position)).into(viewHolder.image, new Callback() {
            @Override
            public void onSuccess() {
                viewHolder.progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }



    @Override
    public int getCount() {
        return arrayList.size();
    }
}
