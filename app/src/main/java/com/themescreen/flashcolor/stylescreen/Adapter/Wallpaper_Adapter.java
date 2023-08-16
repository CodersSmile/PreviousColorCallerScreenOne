package com.themescreen.flashcolor.stylescreen.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Activity.Theme_SetWallpaper_Activity;
import com.themescreen.flashcolor.stylescreen.R;

public class Wallpaper_Adapter extends RecyclerView.Adapter<Wallpaper_Adapter.MyHolder> {
     int[] list;
     Context context;
     AssetManager manager;

    public Wallpaper_Adapter(int[] list, Context context) {
        this.list = list;
        this.context = context;
        manager= context.getAssets();
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.wallpaper_item,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        int image=list[position];

           holder.image_view.setImageResource(image);


       holder.image_view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(context, Theme_SetWallpaper_Activity.class);
               intent.putExtra("img",list[position]);
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView image_view;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image_view=itemView.findViewById(R.id.image);
        }
    }
}
