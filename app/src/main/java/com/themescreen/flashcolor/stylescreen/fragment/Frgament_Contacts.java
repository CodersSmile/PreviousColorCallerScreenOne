package com.themescreen.flashcolor.stylescreen.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.themescreen.flashcolor.stylescreen.Adapter.ContectList_Adapter;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.custom_dailor.Model.ContactsListModeldailor;
import com.themescreen.flashcolor.stylescreen.utils.Help;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class Frgament_Contacts extends Fragment {
    public static final String[] PROJECTION = {"contact_id", "display_name", "data1", "photo_thumb_uri"};
    ArrayList<ContactsListModeldailor> contactsListModels = new ArrayList<>();
    ContectList_Adapter adapter;
    View view;
    RecyclerView recycle_view;
    EditText edit_search;
    ImageView img_back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frgament__contacts, container, false);
        edit_search = view.findViewById(R.id.edit_search);
        recycle_view = view.findViewById(R.id.recycle_view);
        img_back=view.findViewById(R.id.img_back);

        Help.getheightandwidth(getContext());
        contactsListModels.clear();
        recycle_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContectList_Adapter(getContext(), this.contactsListModels);
        recycle_view.setAdapter(adapter);
        rx();

        edit_search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                Log.e("d", "");
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.e("d", "");
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                adapter.getFilter().filter(charSequence);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Frgament_Contacts.this.getActivity().onBackPressed();
            }
        });


        return view;
    }

    public void rx() {
        Observable.just("").flatMap(new Function<String, ObservableSource<ContactsListModeldailor>>() {
            public ObservableSource<ContactsListModeldailor> apply(String str) throws Exception {
                ArrayList arrayList = new ArrayList();
                Cursor query = Frgament_Contacts.this.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, Frgament_Contacts.PROJECTION, null, null, "display_name ASC");
                if (query != null) {
                    ArrayList arrayList2 = new ArrayList();
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
                            if (!arrayList2.contains(replace)) {
                                arrayList.add(new ContactsListModeldailor(string4, string, replace, string3));
                                arrayList2.add(replace);
                            }
                        }
                    } finally {
                        query.close();
                    }
                }
                return Observable.fromIterable(arrayList);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ContactsListModeldailor>() {
            public void onComplete() {
                Log.e("d", "");
            }

            public void onSubscribe(Disposable disposable) {
                Log.e("d", "");
            }

            public void onNext(ContactsListModeldailor contactsListModeldailor) {
                contactsListModels.add(contactsListModeldailor);
                Collections.sort(Frgament_Contacts.this.contactsListModels, ContactsListModeldailor.contactsname);
               adapter.notifyItemChanged(Frgament_Contacts.this.contactsListModels.size() - 1);
                adapter.addfiltervalue();
            }

            public void onError(Throwable th) {
                Log.e("Erreo",th.getMessage());
            }
        });
    }

}