package com.themescreen.flashcolor.stylescreen.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.themescreen.flashcolor.stylescreen.Model.ContactsListModel;
import com.themescreen.flashcolor.stylescreen.Model.ContectThemeModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.sqllite.SQLiteHelperClass;
import com.themescreen.flashcolor.stylescreen.utils.Help;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Contect_list extends RecyclerView.Adapter<Adapter_Contect_list.contectholder> implements Filterable {
    String TAG = "ContactslistAdapter";
    int callingreciveicon;
    int callingrejicon;
    ArrayList<ContactsListModel> contactsListModels;
    ArrayList<ContactsListModel> contactsListModelsfilter;
    ArrayList<ContectThemeModel> contectTheameModels;
    Context context;
    SQLiteDatabase database;
    Filter filter = new Filter() {

        public FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                filterResults.count = Adapter_Contect_list.this.contactsListModelsfilter.size();
                filterResults.values = Adapter_Contect_list.this.contactsListModelsfilter;
            } else {
                ArrayList arrayList = new ArrayList();
                String lowerCase = charSequence.toString().toLowerCase();
                charSequence.toString();
                Iterator<ContactsListModel> it = Adapter_Contect_list.this.contactsListModelsfilter.iterator();
                while (it.hasNext()) {
                    ContactsListModel next = it.next();
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
            if (Adapter_Contect_list.this.contactsListModelsfilter.size() != 0) {
                Adapter_Contect_list.this.contactsListModels = (ArrayList) filterResults.values;
                Adapter_Contect_list.this.notifyDataSetChanged();
            }
        }
    };
    SQLiteHelperClass sqlliteDemoClass;
    String theamdata;

    public Adapter_Contect_list(Context context2, ArrayList<ContactsListModel> arrayList, String str, ArrayList<ContectThemeModel> arrayList2, int i, int i2) {
        this.context = context2;
        this.contactsListModels = arrayList;
        this.theamdata = str;
        this.contectTheameModels = arrayList2;
        this.callingreciveicon = i;
        this.callingrejicon = i2;
        ArrayList<ContactsListModel> arrayList3 = new ArrayList<>();
        this.contactsListModelsfilter = arrayList3;
        arrayList3.addAll(arrayList);
    }

    public contectholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new contectholder(LayoutInflater.from(this.context).inflate(R.layout.raw_contacts, viewGroup, false));
    }

    public void onBindViewHolder(final contectholder contectholder2, final int i) {
        SQLiteHelperClass sQLiteHelperClass = new SQLiteHelperClass(this.context);
        this.sqlliteDemoClass = sQLiteHelperClass;
        this.database = sQLiteHelperClass.getReadableDatabase();
        contectholder2.contect_name.setText(this.contactsListModels.get(i).getName());
        contectholder2.contect_number.setText(this.contactsListModels.get(i).getNumber());
        if (this.contactsListModels.get(i).getPhotourl() != null) {
            Glide.with(this.context).load(this.contactsListModels.get(i).getPhotourl()).into(contectholder2.contect_image);
            contectholder2.text_first_name.setText(String.valueOf(this.contactsListModels.get(i).getName().charAt(0)));
        } else {
            Glide.with(this.context).load(this.contactsListModels.get(i).getPhotourl()).into(contectholder2.contect_image);
            contectholder2.text_first_name.setText(String.valueOf(this.contactsListModels.get(i).getName().charAt(0)));
        }
        Iterator<ContectThemeModel> it = this.contectTheameModels.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            } else if (it.next().getNumber().equals(this.contactsListModels.get(i).getNumber())) {
                contectholder2.img_select_contect.setImageResource(R.drawable.select_contact);
                Log.d(this.TAG, "onBindViewHolder: image");
                break;
            } else {
                contectholder2.img_select_contect.setImageResource(R.drawable.unselect_contact);
            }
        }
        contectholder2.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View r9) {
                Toast.makeText(context, "Akash1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    
    public int getItemCount() {
        Log.e(this.TAG, "getItemCount: " + this.contactsListModels.size());
        return this.contactsListModels.size();
    }

    public Filter getFilter() {
        return this.filter;
    }

    public class contectholder extends RecyclerView.ViewHolder {
        CircleImageView contect_image;
        TextView contect_name;
        TextView contect_number;
        ImageView img_select_contect;
        RelativeLayout relatvive_main;
        TextView text_first_name;

        public contectholder(View view) {
            super(view);
            this.contect_name = (TextView) view.findViewById(R.id.contect_name);
            this.contect_number = (TextView) view.findViewById(R.id.contect_number);
            this.text_first_name = (TextView) view.findViewById(R.id.text_first_name);
            this.contect_image = (CircleImageView) view.findViewById(R.id.contect_image);
            this.img_select_contect = (ImageView) view.findViewById(R.id.img_select_contect);
            this.relatvive_main = (RelativeLayout) view.findViewById(R.id.relatvive_main);
            Help.getheightandwidth(Adapter_Contect_list.this.context);
            Help.setHeight(1080);
            Help.setSize(this.contect_image, 100, 100, true);
            Help.setSize(this.text_first_name, 100, 100, true);
            Help.setSize(this.img_select_contect, 60, 60, true);
            Help.setSize(this.relatvive_main, 983, 160, true);
        }
    }

    public void addfiltervalue() {
        this.contactsListModelsfilter.clear();
        this.contactsListModelsfilter.addAll(this.contactsListModels);
    }
}
