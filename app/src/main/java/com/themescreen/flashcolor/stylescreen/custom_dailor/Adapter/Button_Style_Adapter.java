package com.themescreen.flashcolor.stylescreen.custom_dailor.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.custom_dailor.utils.SP_Helper;
import com.themescreen.flashcolor.stylescreen.utils.Help;

import java.util.ArrayList;

public class Button_Style_Adapter extends RecyclerView.Adapter<Button_Style_Adapter.MyViewHolder> {
    static Context mContext;
    String folder;
    ArrayList<String> paths;

    public Button_Style_Adapter(Context context, ArrayList<String> arrayList, String str) {
        mContext = context;
        this.paths = arrayList;
        this.folder = str;
    }


    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shape_adapter, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        ((RequestBuilder) Glide.with(mContext).load(Uri.parse("file:///android_asset/" + this.folder + "/" + this.paths.get(i))).centerCrop()).into(myViewHolder.img);
        Log.d("StickerAdapter", "onBindViewHolder: " + Uri.parse("file:///android_asset/" + this.folder + "/" + this.paths.get(i)));
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String str = Button_Style_Adapter.this.paths.get(i);
                Toast.makeText(Button_Style_Adapter.mContext, "" + str.substring(0, str.indexOf(".")), Toast.LENGTH_SHORT).show();
                SP_Helper.put_value_in_sharedpreference(Button_Style_Adapter.mContext, "iconstyle", str.substring(0, str.indexOf(".")));
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            ImageView imageView = (ImageView) view.findViewById(R.id.img);
            this.img = imageView;
            Help.setSize(imageView, 480, 610, true);
            Help.setMargin(this.img, 0, 0, 0, 30, false);
        }
    }


    public int getItemCount() {
        return this.paths.size();
    }
}
