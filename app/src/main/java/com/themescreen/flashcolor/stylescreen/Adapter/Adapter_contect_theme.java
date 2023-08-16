package com.themescreen.flashcolor.stylescreen.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.themescreen.flashcolor.stylescreen.Model.ContectIconModel;
import com.themescreen.flashcolor.stylescreen.Model.ContectThemeModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.sqllite.SQLiteHelperClass;
import com.themescreen.flashcolor.stylescreen.utils.ColorCallUtils;
import com.themescreen.flashcolor.stylescreen.utils.Help;

import java.util.ArrayList;
import java.util.Iterator;

public class Adapter_contect_theme extends RecyclerView.Adapter<Adapter_contect_theme.contecthemeholder> {
    String TAG = "CONTECT_THEAM_ADAPTER";
    int ansicon;
    ArrayList<ContectIconModel> contectIconModels;
    ArrayList<ContectThemeModel> contectTheameModels;
    Context context;
    int rejicon;

    public Adapter_contect_theme(Context context2, ArrayList<ContectThemeModel> arrayList, ArrayList<ContectIconModel> arrayList2) {
        this.context = context2;
        this.contectTheameModels = arrayList;
        this.contectIconModels = arrayList2;
    }


    public contecthemeholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new contecthemeholder(LayoutInflater.from(this.context).inflate(R.layout.raw_contect_theme, viewGroup, false));
    }

    public void onBindViewHolder(contecthemeholder contecthemeholder2, @SuppressLint("RecyclerView") final int i) {
        Log.e("CONTECTTHEAMADAPTER", "onBindViewHolder: " + contectTheameModels.size());
        Glide.with(this.context).load(this.contectTheameModels.get(i).getTheamevalue()).into(contecthemeholder2.raw_online_theam_videoview);
        contecthemeholder2.text_number.setText(this.contectTheameModels.get(i).getNumber());
        contecthemeholder2.text_name.setText(ColorCallUtils.getContactNamebytherenumber(this.context, this.contectTheameModels.get(i).getNumber()));
        contecthemeholder2.img_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Adapter_contect_theme.this.deletedailog(i);
            }
        });
        Iterator<ContectIconModel> it = this.contectIconModels.iterator();
        while (it.hasNext()) {
            ContectIconModel next = it.next();
            if (next.getNumber().equals(this.contectTheameModels.get(i).getNumber())) {
                contecthemeholder2.img_recive.setImageResource(next.getRecicon());
                contecthemeholder2.img_rej.setImageResource(next.getRejicon());
                return;
            }
            contecthemeholder2.img_recive.setImageResource(R.drawable.noti_receive_btn_unpress);
            contecthemeholder2.img_rej.setImageResource(R.drawable.noti_decline_btn_unpress);
        }
    }


    public int getItemCount() {
        return this.contectTheameModels.size();
    }

    public class contecthemeholder extends RecyclerView.ViewHolder {
        ImageView img_delete;
        ImageView img_recive;
        ImageView img_rej;
        ImageView img_user;
        ImageView img_user_pic_border;
        ImageView raw_online_theam_videoview;
        RelativeLayout relative_main;
        TextView text_name;
        TextView text_number;

        public contecthemeholder(View view) {
            super(view);
            this.raw_online_theam_videoview = (ImageView) view.findViewById(R.id.raw_online_theam_videoview);
            this.img_recive = (ImageView) view.findViewById(R.id.img_recive);
            this.img_rej = (ImageView) view.findViewById(R.id.img_rej);
            this.relative_main = (RelativeLayout) view.findViewById(R.id.relative_main);
            this.img_user_pic_border = (ImageView) view.findViewById(R.id.img_user_pic_border);
            this.img_user = (ImageView) view.findViewById(R.id.img_user);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_number = (TextView) view.findViewById(R.id.text_number);
            this.img_delete = (ImageView) view.findViewById(R.id.img_delete);
            Help.getheightandwidth(Adapter_contect_theme.this.context);
            Help.setHeight(1080);
            Help.setSize(this.raw_online_theam_videoview, 510, 830, true);
            Help.setSize(this.relative_main, 550, 830, true);
            Help.setSize(this.img_user_pic_border, 170, 170, true);
            Help.setSize(this.img_user, 170, 170, true);
            Help.setSize(this.img_recive, 140, 140, true);
            Help.setSize(this.img_rej, 140, 140, true);
            Help.setSize(this.img_delete, 90, 90, true);
        }
    }

    public void deletedailog(final int i) {
        final Dialog dialog = new Dialog(this.context);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.custom_delete_dailog);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.img_no);
        ImageView imageView2 = (ImageView) dialog.findViewById(R.id.img_yes);
        Help.getheightandwidth(this.context);
        Help.setHeight(1080);
        Help.setSize((LinearLayout) dialog.findViewById(R.id.liner_main_delete_dailog), 868, 663, true);
        Help.setSize(imageView, 210, 80, true);
        Help.setSize(imageView2, 210, 80, true);
        dialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SQLiteHelperClass sQLiteHelperClass = new SQLiteHelperClass(Adapter_contect_theme.this.context);
                sQLiteHelperClass.deletedata(Adapter_contect_theme.this.contectTheameModels.get(i).getNumber(), sQLiteHelperClass.getReadableDatabase());
                Adapter_contect_theme.this.contectTheameModels.remove(i);
                Adapter_contect_theme.this.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }
}
