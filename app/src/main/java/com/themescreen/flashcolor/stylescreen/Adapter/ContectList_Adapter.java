package com.themescreen.flashcolor.stylescreen.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.themescreen.flashcolor.stylescreen.Activity.Activity_SimSelection_Dailogs;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.custom_dailor.Model.ContactsListModeldailor;
import com.themescreen.flashcolor.stylescreen.utils.CallManager;
import com.themescreen.flashcolor.stylescreen.utils.Help;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContectList_Adapter extends RecyclerView.Adapter<ContectList_Adapter.MyHolder> implements Filterable {
    ArrayList<ContactsListModeldailor> contactsListModellers;
    ArrayList<ContactsListModeldailor> contactsListModels;
    Context context;
    public ContectList_Adapter(Context context2, ArrayList<ContactsListModeldailor> arrayList) {
        this.context = context2;
        this.contactsListModels = arrayList;
        ArrayList<ContactsListModeldailor> arrayList2 = new ArrayList<>();
        this.contactsListModellers = arrayList2;
        arrayList2.addAll(arrayList);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.raw_contacts,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imgSelectContact.setImageResource(R.drawable.accept_button_two);
        holder.contactName.setText(((ContactsListModeldailor) this.contactsListModels.get(position)).getName());
        holder.contactNumber.setText(((ContactsListModeldailor) this.contactsListModels.get(position)).getNumber());
        if (((ContactsListModeldailor) this.contactsListModels.get(position)).getPhotourl() != null) {
            Glide.with(this.context).load(((ContactsListModeldailor) this.contactsListModels.get(position)).getPhotourl()).into((ImageView) holder.contactImage);
            holder.textFirstName.setText(String.valueOf(((ContactsListModeldailor) this.contactsListModels.get(position)).getName().charAt(0)));
        } else {
            Glide.with(this.context).load(((ContactsListModeldailor) this.contactsListModels.get(position)).getPhotourl()).into((ImageView) holder.contactImage);
            holder.textFirstName.setText(String.valueOf(((ContactsListModeldailor) this.contactsListModels.get(position)).getName().charAt(0)));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CallManager.sCall == null) {
                    context.startActivity(new Intent(context, Activity_SimSelection_Dailogs.class).putExtra("num", ((ContactsListModeldailor) contactsListModels.get(position)).getNumber()).putExtra("checksim", true));
                    return;
                }
                Toast.makeText(context, "Another Call is Active", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return contactsListModels.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView contactImage;
        TextView contactName;
        TextView contactNumber;
        ImageView imgSelectContact;
        RelativeLayout relativeMain;
        TextView textFirstName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            this.contactName = itemView.findViewById(R.id.contect_name);
            this.contactNumber = itemView.findViewById(R.id.contect_number);
            this.textFirstName = itemView.findViewById(R.id.text_first_name);
            this.contactImage = itemView.findViewById(R.id.contect_image);
            this.relativeMain = itemView.findViewById(R.id.relatvive_main);
            this.imgSelectContact = itemView.findViewById(R.id.img_select_contect);
            Help.getheightandwidth(context);
            Help.setHeight(1080);
            Help.setSize(this.contactImage, 100, 100, true);
            Help.setSize(this.textFirstName, 100, 100, true);
            Help.setSize(this.imgSelectContact, 90, 90, true);
            Help.setSize(this.relativeMain, 983, 160, true);

        }
    }

    Filter filter = new Filter() {
        public FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                filterResults.count = contactsListModellers.size();
                filterResults.values = contactsListModellers;
            } else {
                ArrayList arrayList = new ArrayList();
                String lowerCase = charSequence.toString().toLowerCase();
                charSequence.toString();
                Iterator it = contactsListModellers.iterator();
                while (it.hasNext()) {
                    ContactsListModeldailor contactsListModeldailor = (ContactsListModeldailor) it.next();
                    String str = " ";
                    String str2 = "";
                    if (contactsListModeldailor.getNumber().toLowerCase().replace(str, str2).contains(lowerCase.toLowerCase()) || contactsListModeldailor.getName().toLowerCase().replace(str, str2).contains(lowerCase.toLowerCase())) {
                        arrayList.add(contactsListModeldailor);
                    }
                    filterResults.count = arrayList.size();
                    filterResults.values = arrayList;
                }
            }
            return filterResults;
        }

        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (contactsListModellers.size() != 0) {
                contactsListModels = (ArrayList) filterResults.values;
                notifyDataSetChanged();
            }
        }
    };
    public void addfiltervalue() {
        this.contactsListModellers.clear();
        this.contactsListModellers.addAll(this.contactsListModels);
    }


}

