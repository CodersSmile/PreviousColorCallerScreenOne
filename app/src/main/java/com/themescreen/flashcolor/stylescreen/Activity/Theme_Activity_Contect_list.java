package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Adapter.Adapter_Contect_list;
import com.themescreen.flashcolor.stylescreen.Model.ContactsListModel;
import com.themescreen.flashcolor.stylescreen.Model.ContectThemeModel;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.fragment.Theme_Activity_Calling_Main;
import com.themescreen.flashcolor.stylescreen.sqllite.SQLiteHelperClass;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Theme_Activity_Contect_list extends AppCompatActivity {
    public static final String MyPREFERENCES = "myprefs";
    private static final String[] PROJECTION = {"contact_id", "display_name", "data1", "photo_thumb_uri"};
    String TAG = "AllContactsListActivity";
    TextView action_bar_header;
    Adapter_Contect_list adapterContectlist;
    ArrayList<ContactsListModel> contactsListModels = new ArrayList<>();
    RecyclerView contacts_list_recycleview;
    ArrayList<ContectThemeModel> contectTheameModels = new ArrayList<>();
    EditText edit_search;
    ImageView img_action_done;
    ImageView img_back;
    SharedPreferences sharedPreferences;
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<String> temp = new ArrayList<>();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_contacts_list);
        Help.FS(this);
        ui();
        this.sharedPreferences = getSharedPreferences("myprefs", 0);
        this.shimmerFrameLayout.startShimmer();
        this.contactsListModels.clear();
        this.img_action_done.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Theme_Activity_Contect_list.this.startActivity(new Intent(Theme_Activity_Contect_list.this, Theme_Activity_Calling_Main.class));
            }
        });
        this.edit_search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Theme_Activity_Contect_list.this.adapterContectlist.getFilter().filter(charSequence);
            }
        });
        this.img_back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Theme_Activity_Contect_list.this.onBackPressed();
            }
        });
        Cursor rawQuery = new SQLiteHelperClass(this).getReadableDatabase().rawQuery("SELECT NUMBER,THEMEVALUE  FROM contect_theme", new String[0]);
        if (rawQuery == null || !rawQuery.moveToFirst()) {
            this.shimmerFrameLayout.stopShimmer();
            this.shimmerFrameLayout.setVisibility(View.GONE);
            this.contacts_list_recycleview.setLayoutManager(new LinearLayoutManager(this));
            Adapter_Contect_list adapter_Contect_list = new Adapter_Contect_list(this, this.contactsListModels, getIntent().getStringExtra("theamsvalue"), this.contectTheameModels, getIntent().getIntExtra("ansicon", R.drawable.accept_button), getIntent().getIntExtra("rejicon", R.drawable.decline_button));
            this.adapterContectlist = adapter_Contect_list;
            this.contacts_list_recycleview.setAdapter(adapter_Contect_list);
            rx();
        }
        do {
            this.contectTheameModels.add(new ContectThemeModel(rawQuery.getString(0), rawQuery.getString(1)));
        } while (rawQuery.moveToNext());
        this.shimmerFrameLayout.stopShimmer();
        this.shimmerFrameLayout.setVisibility(View.GONE);
        this.contacts_list_recycleview.setLayoutManager(new LinearLayoutManager(this));
        Adapter_Contect_list adapter_Contect_list2 = new Adapter_Contect_list(this, this.contactsListModels, getIntent().getStringExtra("theamsvalue"), this.contectTheameModels, getIntent().getIntExtra("ansicon", R.drawable.accept_button), getIntent().getIntExtra("rejicon", R.drawable.decline_button));
        this.adapterContectlist = adapter_Contect_list2;
        this.contacts_list_recycleview.setAdapter(adapter_Contect_list2);
        rx();
    }

    public void rx() {
        Observable.just("").flatMap(new Function<String, ObservableSource<ContactsListModel>>() {

            public ObservableSource<ContactsListModel> apply(String str) throws Exception {
                ArrayList arrayList = new ArrayList();
                Cursor query = Theme_Activity_Contect_list.this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, Theme_Activity_Contect_list.PROJECTION, null, null, "display_name ASC");
                if (query != null) {
                    HashSet hashSet = new HashSet();
                    try {
                        int columnIndex = query.getColumnIndex("display_name");
                        int columnIndex2 = query.getColumnIndex("data1");
                        int columnIndex3 = query.getColumnIndex("photo_thumb_uri");
                        int columnIndex4 = query.getColumnIndex("contact_id");
                        while (query.moveToNext()) {
                            String string = query.getString(columnIndex);
                            String string2 = query.getString(columnIndex2);
                            String string3 = query.getString(columnIndex3);
                            String string4 = query.getString(columnIndex4);
                            String replace = string2.replace(" ", "");
                            if (!hashSet.contains(replace)) {
                                arrayList.add(new ContactsListModel(string4, string, replace, string3));
                                hashSet.add(replace);
                            }
                        }
                    } finally {
                        query.close();
                    }
                }
                return Observable.fromIterable(arrayList);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ContactsListModel>() {

            public void onComplete() {
            }

            public void onSubscribe(Disposable disposable) {
            }

            public void onNext(ContactsListModel contactsListModel) {
                Theme_Activity_Contect_list.this.contactsListModels.add(contactsListModel);
                Collections.sort(Theme_Activity_Contect_list.this.contactsListModels, ContactsListModel.contactsname);
                Theme_Activity_Contect_list.this.adapterContectlist.notifyItemChanged(Theme_Activity_Contect_list.this.contactsListModels.size() - 1);
                Theme_Activity_Contect_list.this.adapterContectlist.addfiltervalue();
            }

            public void onError(Throwable th) {
                Log.e(Theme_Activity_Contect_list.this.TAG, "RX_EXCEPTION:= " + th.getMessage());
            }
        });
    }

    public void ui() {
        this.contacts_list_recycleview = (RecyclerView) findViewById(R.id.contacts_list_recycleview);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        this.action_bar_header = (TextView) findViewById(R.id.action_bar_header);
        this.img_action_done = (ImageView) findViewById(R.id.img_action_done);
        this.edit_search = (EditText) findViewById(R.id.edit_search);
        this.action_bar_header.setText("Contact List");
        this.shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer);
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.img_back, 80, 80, true);
        Help.setSize(this.img_action_done, 80, 80, true);
        Help.setSize(this.edit_search, 901, 101, true);
    }

    public void onBackPressed() {
        finish();
    }

}
