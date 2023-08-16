package com.themescreen.flashcolor.stylescreen.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Model.CallReceiveRejectCall;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.utils.Help;

import java.util.ArrayList;

public class Adapter_calling_icon_preview extends RecyclerView.Adapter<Adapter_calling_icon_preview.MyCallholder> {
    public static final String MyPREFERENCES = "myprefs";
    public static String ansvalue = "ansicon";
    public static String individualrecivevalue = "individualreciveicon";
    public static String individualrejectvalue = "individualrejecticon";
    public static String rejvalue = "reicon";
    String activitycheck;
    Adapterclick adapterclick;
    ArrayList<CallReceiveRejectCall> callReceiveRejectCalls;
    Context context;
    SharedPreferences sharedpreferences;

    public interface Adapterclick {
        String iconclick();
    }

    public Adapter_calling_icon_preview(ArrayList<CallReceiveRejectCall> arrayList, Context context2, Adapterclick adapterclick2, String str) {
        this.callReceiveRejectCalls = arrayList;
        this.context = context2;
        this.adapterclick = adapterclick2;
        this.activitycheck = str;
    }


    public MyCallholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyCallholder(LayoutInflater.from(this.context).inflate(R.layout.raw_callicon_preview, viewGroup, false));
    }

    public void onBindViewHolder(MyCallholder myCallholder, final int i) {
        Log.d("onBindViewHolder", "onBindViewHolder: " + this.callReceiveRejectCalls.get(i).getReceive());
        myCallholder.reccive_call.setImageResource(this.callReceiveRejectCalls.get(i).getReceive());
        myCallholder.reject_call.setImageResource(this.callReceiveRejectCalls.get(i).getReject());
        myCallholder.liner_main.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Adapter_calling_icon_preview adapter_calling_icon_preview = Adapter_calling_icon_preview.this;
                adapter_calling_icon_preview.sharedpreferences = adapter_calling_icon_preview.context.getSharedPreferences("myprefs", 0);
                if (Adapter_calling_icon_preview.this.activitycheck.equals("allcotacts")) {
                    SharedPreferences.Editor edit = Adapter_calling_icon_preview.this.sharedpreferences.edit();
                    edit.putInt(Adapter_calling_icon_preview.ansvalue, Adapter_calling_icon_preview.this.callReceiveRejectCalls.get(i).getReceive());
                    edit.putInt(Adapter_calling_icon_preview.rejvalue, Adapter_calling_icon_preview.this.callReceiveRejectCalls.get(i).getReject());
                    edit.apply();
                    Adapter_calling_icon_preview.this.adapterclick.iconclick();
                    return;
                }
                SharedPreferences.Editor edit2 = Adapter_calling_icon_preview.this.sharedpreferences.edit();
                edit2.putInt(Adapter_calling_icon_preview.individualrecivevalue, Adapter_calling_icon_preview.this.callReceiveRejectCalls.get(i).getReceive());
                edit2.putInt(Adapter_calling_icon_preview.individualrejectvalue, Adapter_calling_icon_preview.this.callReceiveRejectCalls.get(i).getReject());
                edit2.apply();
                Adapter_calling_icon_preview.this.adapterclick.iconclick();
            }
        });
    }


    public int getItemCount() {
        return this.callReceiveRejectCalls.size();
    }

    public class MyCallholder extends RecyclerView.ViewHolder {
        LinearLayout liner_main;
        ImageView reccive_call;
        ImageView reject_call;

        public MyCallholder(View view) {
            super(view);
            this.reccive_call = (ImageView) view.findViewById(R.id.reccive_call);
            this.reject_call = (ImageView) view.findViewById(R.id.reject_call);
            this.liner_main = (LinearLayout) view.findViewById(R.id.liner_main);
            Help.getheightandwidth(Adapter_calling_icon_preview.this.context);
            Help.setHeight(1080);
            Help.setSize(this.liner_main, 910, 170, true);
            Help.setSize(this.reccive_call, 160, 160, true);
            Help.setSize(this.reject_call, 160, 160, true);
        }
    }
}
