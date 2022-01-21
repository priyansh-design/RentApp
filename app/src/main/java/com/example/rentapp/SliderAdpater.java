package com.example.rentapp;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdpater  extends SliderViewAdapter<SliderAdpater.Holder> {

    ArrayList<Uri> arrayList;

    public SliderAdpater(ArrayList<Uri> arrayList) {
        this.arrayList = arrayList;
    }

    public class Holder extends SliderViewAdapter.ViewHolder{
        ImageView image;

        public Holder(View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.imageofslider);
        }
    }
    @Override
    public  Holder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.image.setImageURI(arrayList.get(position));

    }



    @Override
    public int getCount() {
        return arrayList.size();
    }
}
