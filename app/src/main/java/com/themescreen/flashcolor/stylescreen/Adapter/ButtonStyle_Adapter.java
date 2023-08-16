package com.themescreen.flashcolor.stylescreen.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.button_style.SP_Helper;

import java.io.InputStream;

public class ButtonStyle_Adapter extends RecyclerView.Adapter<ButtonStyle_Adapter.MyHolder> {
     String[] list;
     Context context;
     AssetManager manager;

    public ButtonStyle_Adapter(String[] list, Context context) {
        this.list = list;
        this.context = context;
        manager= context.getAssets();
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dialer_item,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        String image=list[position];
       try {
           InputStream is = manager.open("button_bg/"+image);
           Bitmap bitmap = BitmapFactory.decodeStream(is);
           holder.image_view.setImageBitmap(bitmap);
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

       holder.image_view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String str = list[position];
               Toast.makeText(context, "Change button style..", Toast.LENGTH_SHORT).show();
               SP_Helper.putValueInSharedpreference(context, SP_Helper.CONSOLE, str.substring(0, str.indexOf(".")));

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
            image_view=itemView.findViewById(R.id.image_view);
        }
    }
}
