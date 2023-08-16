package com.themescreen.flashcolor.stylescreen.custom_dailor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ContactslistAdapterdailor extends RecyclerView.Adapter<ContactslistAdapterdailor.contectholder> implements Filterable {
    String TAG = "ContactslistAdapter";
    ArrayList<ContactsListModeldailor> contactsListModels;
    ArrayList<ContactsListModeldailor> contactsListModelsfilter;
    Context context;
    Filter filter = new Filter() {


        public FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                filterResults.count = ContactslistAdapterdailor.this.contactsListModelsfilter.size();
                filterResults.values = ContactslistAdapterdailor.this.contactsListModelsfilter;
            } else {
                ArrayList arrayList = new ArrayList();
                String lowerCase = charSequence.toString().toLowerCase();
                charSequence.toString();
                Iterator<ContactsListModeldailor> it = ContactslistAdapterdailor.this.contactsListModelsfilter.iterator();
                while (it.hasNext()) {
                    ContactsListModeldailor next = it.next();
                    if (next.getNumber().toLowerCase().replace(" ", "").contains(lowerCase.toLowerCase()) || next.getName().toLowerCase().replace(" ", "").contains(lowerCase.toLowerCase())) {
                        arrayList.add(next);
                    }
                    filterResults.count = arrayList.size();
                    filterResults.values = arrayList;
                }
            }
            return filterResults;
        }


        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (ContactslistAdapterdailor.this.contactsListModelsfilter.size() != 0) {
                ContactslistAdapterdailor.this.contactsListModels = (ArrayList) filterResults.values;
                ContactslistAdapterdailor.this.notifyDataSetChanged();
            }
        }
    };

    public ContactslistAdapterdailor(Context context2, ArrayList<ContactsListModeldailor> arrayList) {
        this.context = context2;
        this.contactsListModels = arrayList;
        ArrayList<ContactsListModeldailor> arrayList2 = new ArrayList<>();
        this.contactsListModelsfilter = arrayList2;
        arrayList2.addAll(arrayList);
    }


    public contectholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new contectholder(LayoutInflater.from(this.context).inflate(R.layout.raw_contacts, viewGroup, false));
    }

    public void onBindViewHolder(contectholder contectholder2, final int i) {
        contectholder2.img_select_contect.setImageResource(R.drawable.accept_button_two);
        contectholder2.contect_name.setText(this.contactsListModels.get(i).getName());
        contectholder2.contect_number.setText(this.contactsListModels.get(i).getNumber());
        if (this.contactsListModels.get(i).getPhotourl() != null) {
            Glide.with(this.context).load(this.contactsListModels.get(i).getPhotourl()).into(contectholder2.contect_image);
            contectholder2.text_first_name.setText(String.valueOf(this.contactsListModels.get(i).getName().charAt(0)));
        } else {
            Glide.with(this.context).load(this.contactsListModels.get(i).getPhotourl()).into(contectholder2.contect_image);
            contectholder2.text_first_name.setText(String.valueOf(this.contactsListModels.get(i).getName().charAt(0)));
        }
        contectholder2.itemView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (CallManager.sCall == null) {
                    ContactslistAdapterdailor.this.context.startActivity(new Intent(ContactslistAdapterdailor.this.context, Activity_SimSelection_Dailogs.class).putExtra("num", ContactslistAdapterdailor.this.contactsListModels.get(i).getNumber()).putExtra("checksim", true));
                } else {
                    Toast.makeText(ContactslistAdapterdailor.this.context, "Another Call is Active", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public int getItemCount() {
        return this.contactsListModels.size();
    }

    public Filter getFilter() {
        return this.filter;
    }

    public class contectholder extends RecyclerView.ViewHolder {
        CircleImageView contect_image;
        TextView contect_name;
        TextView contect_number;
        LinearLayout dividerrr;
        ImageView img_select_contect;
        RelativeLayout relatvive_main;
        TextView text_first_name;

        public contectholder(View view) {
            super(view);
            this.contect_name = (TextView) view.findViewById(R.id.contect_name);
            this.contect_number = (TextView) view.findViewById(R.id.contect_number);
            this.text_first_name = (TextView) view.findViewById(R.id.text_first_name);
            this.contect_image = (CircleImageView) view.findViewById(R.id.contect_image);
            this.relatvive_main = (RelativeLayout) view.findViewById(R.id.relatvive_main);
            this.img_select_contect = (ImageView) view.findViewById(R.id.img_select_contect);
            Help.getheightandwidth(ContactslistAdapterdailor.this.context);
            Help.setHeight(1080);
            Help.setSize(this.contect_image, 100, 100, true);
            Help.setSize(this.text_first_name, 100, 100, true);
            Help.setSize(this.img_select_contect, 90, 90, true);
            Help.setSize(this.relatvive_main, 983, 160, true);
        }
    }

    public void addfiltervalue() {
        this.contactsListModelsfilter.clear();
        this.contactsListModelsfilter.addAll(this.contactsListModels);
    }
}
