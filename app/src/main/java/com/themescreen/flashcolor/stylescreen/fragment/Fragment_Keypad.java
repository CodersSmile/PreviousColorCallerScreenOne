package com.themescreen.flashcolor.stylescreen.fragment;

import static com.themescreen.flashcolor.stylescreen.Activity.Theme_CropActivity.croppedImage;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.themescreen.flashcolor.stylescreen.Activity.Activity_SimSelection_Dailogs;
import com.themescreen.flashcolor.stylescreen.Adapter.ContectList_Adapter;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.button_style.SP_Helper;
import com.themescreen.flashcolor.stylescreen.custom_dailor.Model.ContactsListModeldailor;
import com.themescreen.flashcolor.stylescreen.utils.AppConst;
import com.themescreen.flashcolor.stylescreen.utils.CallManager;
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


public class Fragment_Keypad extends Fragment implements View.OnClickListener {
    public static final String[] PROJECTION = {"contact_id", "display_name", "data1", "photo_thumb_uri"};
    ArrayList<ContactsListModeldailor> contactsListModels = new ArrayList<>();
    RecyclerView contacts_recycleview;
    EditText edPad;
    ImageView layCancel,dailor_bg;
    View view;
    ImageView btn_calling;
    TextView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnStar, btn0, btnHash;
    ContectList_Adapter adapter;
    public Fragment_Keypad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__keypad, container, false);
        initView();
        contacts_recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContectList_Adapter(getContext(), contactsListModels);
        contacts_recycleview.setAdapter(adapter);
        rx();
        Help.getheightandwidth(getContext());
        iconstyle();
        if (AppConst.idiom == 1) {
            dailor_bg.setImageBitmap(croppedImage);
        } else if (AppConst.idiom == 2) {
            String str = "";
            String valueFromSharedprefrence = SP_Helper.getValueFromSharedprefrence(getContext(), SP_Helper.IMAGER, str);

            if (valueFromSharedprefrence != str) {
                ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) Glide.with(getContext()).load(valueFromSharedprefrence).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).centerCrop()).into(dailor_bg);
            }
        }
        btn_calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String obj = edPad.getText().toString();
                if (CallManager.sCall != null) {
                    Toast.makeText(getContext(), "Another Call is Active", Toast.LENGTH_SHORT).show();
                } else if (!obj.matches("")) {
                    Fragment_Keypad.this.startActivity(new Intent(Fragment_Keypad.this.getContext(), Activity_SimSelection_Dailogs.class).putExtra("num", obj).putExtra("checksim", true));
                }

            }
        });
        edPad.addTextChangedListener(new TextWatcher() {
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

        return view;
    }

    public void initView() {
        dailor_bg=view.findViewById(R.id.dailor_bg);
        contacts_recycleview = view.findViewById(R.id.contacts_recycleview);
        layCancel = view.findViewById(R.id.layCancel);
        layCancel.setOnClickListener(this);
        edPad = view.findViewById(R.id.edPad);
        btn_calling=view.findViewById(R.id.btn_calling);

        btn1 = view.findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        btn2 = view.findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        btn3 = view.findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        btn4 = view.findViewById(R.id.btn4);
        btn4.setOnClickListener(this);

        btn5 = view.findViewById(R.id.btn5);
        btn5.setOnClickListener(this);

        btn6 = view.findViewById(R.id.btn6);
        btn6.setOnClickListener(this);

        btn7 = view.findViewById(R.id.btn7);
        btn7.setOnClickListener(this);

        btn8 = view.findViewById(R.id.btn8);
        btn8.setOnClickListener(this);

        btn9 = view.findViewById(R.id.btn9);
        btn9.setOnClickListener(this);

        btn0 = view.findViewById(R.id.btn0);
        btn0.setOnClickListener(this);

        btnHash = view.findViewById(R.id.btnHash);
        btnHash.setOnClickListener(this);

        btnStar = view.findViewById(R.id.btnStar);
        btnStar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String obj = v.getTag().toString();
        obj.hashCode();
        obj.hashCode();
        obj.hashCode();
        obj.hashCode();
        int hashCode = obj.hashCode();
        Log.e("HAs_Code",""+hashCode);
        String str = "9";
        String str2 = "8";
        String str3 = "7";
        String str4 = "6";
        String str5 = "5";
        String str6 = "4";
        String str7 = ExifInterface.GPS_MEASUREMENT_3D;
        String str8 = ExifInterface.GPS_MEASUREMENT_2D;
        String str9 = "1";
        String str10 = "0";
        String str11 = "*";
        String str12 = "#";
        char c = 65535;
        switch (hashCode) {
            case -1367724422:
                if (obj.equals("cancel")) {
                    c = 0;
                    break;
                }
                break;
            case 35:
                if (obj.equals(str12)) {
                    c = 1;
                    break;
                }
                break;
            case 42:
                if (obj.equals(str11)) {
                    c = 2;
                    break;
                }
                break;
            case 48:
                if (obj.equals(str10)) {
                    c = 3;
                    break;
                }
                break;
            case 49:
                if (obj.equals(str9)) {
                    c = 4;
                    break;
                }
                break;
            case 50:
                if (obj.equals(str8)) {
                    c = 5;
                    break;
                }
                break;
            case 51:
                if (obj.equals(str7)) {
                    c = 6;
                    break;
                }
                break;
            case 52:
                if (obj.equals(str6)) {
                    c = 7;
                    break;
                }
                break;
            case 53:
                if (obj.equals(str5)) {
                    c = 8;
                    break;
                }
                break;
            case 54:
                if (obj.equals(str4)) {
                    c = 9;
                    break;
                }
                break;
            case 55:
                if (obj.equals(str3)) {
                    c = 10;
                    break;
                }
                break;
            case 56:
                if (obj.equals(str2)) {
                    c = 11;
                    break;
                }
                break;
            case 57:
                if (obj.equals(str)) {
                    c = 12;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                String obj2 = edPad.getText().toString();
                if (obj2.length() > 0) {
                    obj2 = obj2.substring(0, obj2.length() - 1);
                }
                edPad.setText(obj2.toString());
                return;
            case 1:
                EditText editText = edPad;
                StringBuilder sb = new StringBuilder();
                sb.append(edPad.getText().toString());
                sb.append(str12);
                editText.setText(sb.toString());
                return;
            case 2:
                EditText editText2 = edPad;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(edPad.getText().toString());
                sb2.append(str11);
                editText2.setText(sb2.toString());
                return;
            case 3:
                EditText editText3 = edPad;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(edPad.getText().toString());
                sb3.append(str10);
                editText3.setText(sb3.toString());
                return;
            case 4:
                EditText editText4 = edPad;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(edPad.getText().toString());
                sb4.append(str9);
                editText4.setText(sb4.toString());
                return;
            case 5:
                EditText editText5 = edPad;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(edPad.getText().toString());
                sb5.append(str8);
                editText5.setText(sb5.toString());
                return;
            case 6:
                EditText editText6 = edPad;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(edPad.getText().toString());
                sb6.append(str7);
                editText6.setText(sb6.toString());
                return;
            case 7:
                EditText editText7 = edPad;
                StringBuilder sb7 = new StringBuilder();
                sb7.append(edPad.getText().toString());
                sb7.append(str6);
                editText7.setText(sb7.toString());
                return;
            case 8:
                EditText editText8 = edPad;
                StringBuilder sb8 = new StringBuilder();
                sb8.append(edPad.getText().toString());
                sb8.append(str5);
                editText8.setText(sb8.toString());
                return;
            case 9:
                EditText editText9 = edPad;
                StringBuilder sb9 = new StringBuilder();
                sb9.append(edPad.getText().toString());
                sb9.append(str4);
                editText9.setText(sb9.toString());
                return;
            case 10:
                EditText editText10 = edPad;
                StringBuilder sb10 = new StringBuilder();
                sb10.append(edPad.getText().toString());
                sb10.append(str3);
                editText10.setText(sb10.toString());
                return;
            case 11:
                EditText editText11 = edPad;
                StringBuilder sb11 = new StringBuilder();
                sb11.append(edPad.getText().toString());
                sb11.append(str2);
                editText11.setText(sb11.toString());
                return;
            case 12:
                EditText editText12 = edPad;
                StringBuilder sb12 = new StringBuilder();
                sb12.append(edPad.getText().toString());
                sb12.append(str);
                editText12.setText(sb12.toString());
                return;
            default:
                return;
        }

    }

    public void iconstyle() {
        String str = "4";
        String valueFromSharedprefrence = SP_Helper.getValueFromSharedprefrence(getContext(), SP_Helper.CONSOLE, str);
        if (valueFromSharedprefrence.equals("1")) {
            btn1.setBackgroundResource(R.drawable.th_1);
            btn2.setBackgroundResource(R.drawable.th_12);
            btn3.setBackgroundResource(R.drawable.th_13);
            btn4.setBackgroundResource(R.drawable.th_14);
            btn5.setBackgroundResource(R.drawable.th_15);
            btn6.setBackgroundResource(R.drawable.th_16);
            btn7.setBackgroundResource(R.drawable.th_17);
            btn8.setBackgroundResource(R.drawable.th_18);
            btn9.setBackgroundResource(R.drawable.th_19);
            btnStar.setBackgroundResource(R.drawable.th_110);
            btn0.setBackgroundResource(R.drawable.th_111);
            btnHash.setBackgroundResource(R.drawable.th_18);
        } else if (valueFromSharedprefrence.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
            btn1.setBackgroundResource(R.drawable.th_2);
            btn2.setBackgroundResource(R.drawable.th_2);
            btn3.setBackgroundResource(R.drawable.th_2);
            btn4.setBackgroundResource(R.drawable.th_2);
            btn5.setBackgroundResource(R.drawable.th_2);
            btn6.setBackgroundResource(R.drawable.th_2);
            btn7.setBackgroundResource(R.drawable.th_2);
            btn8.setBackgroundResource(R.drawable.th_2);
            btn9.setBackgroundResource(R.drawable.th_2);
            btnStar.setBackgroundResource(R.drawable.th_2);
            btn0.setBackgroundResource(R.drawable.th_2);
            btnHash.setBackgroundResource(R.drawable.th_2);
        } else if (valueFromSharedprefrence.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
            btn1.setBackgroundResource(R.drawable.th_31);
            btn2.setBackgroundResource(R.drawable.th_32);
            btn3.setBackgroundResource(R.drawable.th_33);
            btn4.setBackgroundResource(R.drawable.th_34);
            btn5.setBackgroundResource(R.drawable.th_35);
            btn6.setBackgroundResource(R.drawable.th_36);
            btn7.setBackgroundResource(R.drawable.th_37);
            btn8.setBackgroundResource(R.drawable.th_38);
            btn9.setBackgroundResource(R.drawable.th_39);
            btnStar.setBackgroundResource(R.drawable.th_310);
            btn0.setBackgroundResource(R.drawable.th_311);
            btnHash.setBackgroundResource(R.drawable.th_312);
        } else if (valueFromSharedprefrence.equals(str)) {
            btn1.setBackgroundResource(R.drawable.th_51);
            btn2.setBackgroundResource(R.drawable.th_52);
            btn3.setBackgroundResource(R.drawable.th_53);
            btn4.setBackgroundResource(R.drawable.th_54);
            btn5.setBackgroundResource(R.drawable.th_55);
            btn6.setBackgroundResource(R.drawable.th_56);
            btn7.setBackgroundResource(R.drawable.th_57);
            btn8.setBackgroundResource(R.drawable.th_58);
            btn9.setBackgroundResource(R.drawable.th_59);
            btnStar.setBackgroundResource(R.drawable.th_510);
            btn0.setBackgroundResource(R.drawable.th_511);
            btnHash.setBackgroundResource(R.drawable.th_512);
        } else if (valueFromSharedprefrence.equals("5")) {
            btn1.setBackgroundResource(R.drawable.th_6);
            btn2.setBackgroundResource(R.drawable.th_6);
            btn3.setBackgroundResource(R.drawable.th_6);
            btn4.setBackgroundResource(R.drawable.th_6);
            btn5.setBackgroundResource(R.drawable.th_6);
            btn6.setBackgroundResource(R.drawable.th_6);
            btn7.setBackgroundResource(R.drawable.th_6);
            btn8.setBackgroundResource(R.drawable.th_6);
            btn9.setBackgroundResource(R.drawable.th_6);
            btnStar.setBackgroundResource(R.drawable.th_6);
            btn0.setBackgroundResource(R.drawable.th_6);
            btnHash.setBackgroundResource(R.drawable.th_6);
        } else if (valueFromSharedprefrence.equals("6")) {
            btn1.setBackgroundResource(R.drawable.th7_1);
            btn2.setBackgroundResource(R.drawable.th7_2);
            btn3.setBackgroundResource(R.drawable.th7_3);
            btn4.setBackgroundResource(R.drawable.th7_4);
            btn5.setBackgroundResource(R.drawable.th7_5);
            btn6.setBackgroundResource(R.drawable.th7_6);
            btn7.setBackgroundResource(R.drawable.th7_7);
            btn8.setBackgroundResource(R.drawable.th7_8);
            btn9.setBackgroundResource(R.drawable.th7_9);
            btnStar.setBackgroundResource(R.drawable.th7_10);
            btn0.setBackgroundResource(R.drawable.th7_11);
            btnHash.setBackgroundResource(R.drawable.th7_1);
        } else if (valueFromSharedprefrence.equals("7")) {
            btn1.setBackgroundResource(R.drawable.th9_1);
            btn2.setBackgroundResource(R.drawable.th9_2);
            btn3.setBackgroundResource(R.drawable.th9_3);
            btn4.setBackgroundResource(R.drawable.th9_4);
            btn5.setBackgroundResource(R.drawable.th9_5);
            btn6.setBackgroundResource(R.drawable.th9_6);
            btn7.setBackgroundResource(R.drawable.th9_7);
            btn8.setBackgroundResource(R.drawable.th9_8);
            btn9.setBackgroundResource(R.drawable.th9_10);
            btnStar.setBackgroundResource(R.drawable.th9_11);
            btn0.setBackgroundResource(R.drawable.th9_1);
            btnHash.setBackgroundResource(R.drawable.th9_2);
        } else if (valueFromSharedprefrence.equals("8")) {
            btn1.setBackgroundResource(R.drawable.th10_1);
            btn2.setBackgroundResource(R.drawable.th10_2);
            btn3.setBackgroundResource(R.drawable.th10_3);
            btn4.setBackgroundResource(R.drawable.th10_4);
            btn5.setBackgroundResource(R.drawable.th10_5);
            btn6.setBackgroundResource(R.drawable.th10_6);
            btn7.setBackgroundResource(R.drawable.th10_7);
            btn8.setBackgroundResource(R.drawable.th10_8);
            btn9.setBackgroundResource(R.drawable.th10_9);
            btnStar.setBackgroundResource(R.drawable.th10_10);
            btn0.setBackgroundResource(R.drawable.th10_11);
            btnHash.setBackgroundResource(R.drawable.th10_12);
        } else if (valueFromSharedprefrence.equals("9")) {
            btn1.setBackgroundResource(R.drawable.btn_them9);
            btn2.setBackgroundResource(R.drawable.btn_them9);
            btn3.setBackgroundResource(R.drawable.btn_them9);
            btn4.setBackgroundResource(R.drawable.btn_them9);
            btn5.setBackgroundResource(R.drawable.btn_them9);
            btn6.setBackgroundResource(R.drawable.btn_them9);
            btn7.setBackgroundResource(R.drawable.btn_them9);
            btn8.setBackgroundResource(R.drawable.btn_them9);
            btn9.setBackgroundResource(R.drawable.btn_them9);
            btnStar.setBackgroundResource(R.drawable.btn_them9);
            btn0.setBackgroundResource(R.drawable.btn_them9);
            btnHash.setBackgroundResource(R.drawable.btn_them9);
        } else if (valueFromSharedprefrence.equals("10")) {
            btn1.setBackgroundResource(R.drawable.btn_them10);
            btn2.setBackgroundResource(R.drawable.btn_them10);
            btn3.setBackgroundResource(R.drawable.btn_them10);
            btn4.setBackgroundResource(R.drawable.btn_them10);
            btn5.setBackgroundResource(R.drawable.btn_them10);
            btn6.setBackgroundResource(R.drawable.btn_them10);
            btn7.setBackgroundResource(R.drawable.btn_them10);
            btn8.setBackgroundResource(R.drawable.btn_them10);
            btn9.setBackgroundResource(R.drawable.btn_them10);
            btnStar.setBackgroundResource(R.drawable.btn_them10);
            btn0.setBackgroundResource(R.drawable.btn_them10);
            btnHash.setBackgroundResource(R.drawable.btn_them10);
        } else if (valueFromSharedprefrence.equals("11")) {
            btn1.setBackgroundResource(R.drawable.btn_them11);
            btn2.setBackgroundResource(R.drawable.btn_them11);
            btn3.setBackgroundResource(R.drawable.btn_them11);
            btn4.setBackgroundResource(R.drawable.btn_them11);
            btn5.setBackgroundResource(R.drawable.btn_them11);
            btn6.setBackgroundResource(R.drawable.btn_them11);
            btn7.setBackgroundResource(R.drawable.btn_them11);
            btn8.setBackgroundResource(R.drawable.btn_them11);
            btn9.setBackgroundResource(R.drawable.btn_them11);
            btnStar.setBackgroundResource(R.drawable.btn_them11);
            btn0.setBackgroundResource(R.drawable.btn_them11);
            btnHash.setBackgroundResource(R.drawable.btn_them11);
        } else if (valueFromSharedprefrence.equals("12")) {
            btn1.setBackgroundResource(R.drawable.btn_them12);
            btn2.setBackgroundResource(R.drawable.btn_them12);
            btn3.setBackgroundResource(R.drawable.btn_them12);
            btn4.setBackgroundResource(R.drawable.btn_them12);
            btn5.setBackgroundResource(R.drawable.btn_them12);
            btn6.setBackgroundResource(R.drawable.btn_them12);
            btn7.setBackgroundResource(R.drawable.btn_them12);
            btn8.setBackgroundResource(R.drawable.btn_them12);
            btn9.setBackgroundResource(R.drawable.btn_them12);
            btnStar.setBackgroundResource(R.drawable.btn_them12);
            btn0.setBackgroundResource(R.drawable.btn_them12);
            btnHash.setBackgroundResource(R.drawable.btn_them12);
        } else if (valueFromSharedprefrence.equals("12")) {
            btn1.setBackgroundResource(R.drawable.btn_them12);
            btn2.setBackgroundResource(R.drawable.btn_them12);
            btn3.setBackgroundResource(R.drawable.btn_them12);
            btn4.setBackgroundResource(R.drawable.btn_them12);
            btn5.setBackgroundResource(R.drawable.btn_them12);
            btn6.setBackgroundResource(R.drawable.btn_them12);
            btn7.setBackgroundResource(R.drawable.btn_them12);
            btn8.setBackgroundResource(R.drawable.btn_them12);
            btn9.setBackgroundResource(R.drawable.btn_them12);
            btnStar.setBackgroundResource(R.drawable.btn_them12);
            btn0.setBackgroundResource(R.drawable.btn_them12);
            btnHash.setBackgroundResource(R.drawable.btn_them12);
        } else if (valueFromSharedprefrence.equals("13")) {
            btn1.setBackgroundResource(R.drawable.btn_them13);
            btn2.setBackgroundResource(R.drawable.btn_them13);
            btn3.setBackgroundResource(R.drawable.btn_them13);
            btn4.setBackgroundResource(R.drawable.btn_them13);
            btn5.setBackgroundResource(R.drawable.btn_them13);
            btn6.setBackgroundResource(R.drawable.btn_them13);
            btn7.setBackgroundResource(R.drawable.btn_them13);
            btn8.setBackgroundResource(R.drawable.btn_them13);
            btn9.setBackgroundResource(R.drawable.btn_them13);
            btnStar.setBackgroundResource(R.drawable.btn_them13);
            btn0.setBackgroundResource(R.drawable.btn_them13);
            btnHash.setBackgroundResource(R.drawable.btn_them13);
        } else if (valueFromSharedprefrence.equals("14")) {
            btn1.setBackgroundResource(R.drawable.btn_them14);
            btn2.setBackgroundResource(R.drawable.btn_them14);
            btn3.setBackgroundResource(R.drawable.btn_them14);
            btn4.setBackgroundResource(R.drawable.btn_them14);
            btn5.setBackgroundResource(R.drawable.btn_them14);
            btn6.setBackgroundResource(R.drawable.btn_them14);
            btn7.setBackgroundResource(R.drawable.btn_them14);
            btn8.setBackgroundResource(R.drawable.btn_them14);
            btn9.setBackgroundResource(R.drawable.btn_them14);
            btnStar.setBackgroundResource(R.drawable.btn_them14);
            btn0.setBackgroundResource(R.drawable.btn_them14);
            btnHash.setBackgroundResource(R.drawable.btn_them14);
        } else if (valueFromSharedprefrence.equals("15")) {
            btn1.setBackgroundResource(R.drawable.btn_them15);
            btn2.setBackgroundResource(R.drawable.btn_them15);
            btn3.setBackgroundResource(R.drawable.btn_them15);
            btn4.setBackgroundResource(R.drawable.btn_them15);
            btn5.setBackgroundResource(R.drawable.btn_them15);
            btn6.setBackgroundResource(R.drawable.btn_them15);
            btn7.setBackgroundResource(R.drawable.btn_them15);
            btn8.setBackgroundResource(R.drawable.btn_them15);
            btn9.setBackgroundResource(R.drawable.btn_them15);
            btnStar.setBackgroundResource(R.drawable.btn_them15);
            btn0.setBackgroundResource(R.drawable.btn_them15);
            btnHash.setBackgroundResource(R.drawable.btn_them15);
        } else if (valueFromSharedprefrence.equals("16")) {
            btn1.setBackgroundResource(R.drawable.btn_them16);
            btn2.setBackgroundResource(R.drawable.btn_them16);
            btn3.setBackgroundResource(R.drawable.btn_them16);
            btn4.setBackgroundResource(R.drawable.btn_them16);
            btn5.setBackgroundResource(R.drawable.btn_them16);
            btn6.setBackgroundResource(R.drawable.btn_them16);
            btn7.setBackgroundResource(R.drawable.btn_them16);
            btn8.setBackgroundResource(R.drawable.btn_them16);
            btn9.setBackgroundResource(R.drawable.btn_them16);
            btnStar.setBackgroundResource(R.drawable.btn_them16);
            btn0.setBackgroundResource(R.drawable.btn_them16);
            btnHash.setBackgroundResource(R.drawable.btn_them16);
        } else if (valueFromSharedprefrence.equals("17")) {
            btn1.setBackgroundResource(R.drawable.btn_them17);
            btn2.setBackgroundResource(R.drawable.btn_them17);
            btn3.setBackgroundResource(R.drawable.btn_them17);
            btn4.setBackgroundResource(R.drawable.btn_them17);
            btn5.setBackgroundResource(R.drawable.btn_them17);
            btn6.setBackgroundResource(R.drawable.btn_them17);
            btn7.setBackgroundResource(R.drawable.btn_them17);
            btn8.setBackgroundResource(R.drawable.btn_them17);
            btn9.setBackgroundResource(R.drawable.btn_them17);
            btnStar.setBackgroundResource(R.drawable.btn_them17);
            btn0.setBackgroundResource(R.drawable.btn_them17);
            btnHash.setBackgroundResource(R.drawable.btn_them17);
        } else if (valueFromSharedprefrence.equals("18")) {
            btn1.setBackgroundResource(R.drawable.btn_them18);
            btn2.setBackgroundResource(R.drawable.btn_them18);
            btn3.setBackgroundResource(R.drawable.btn_them18);
            btn4.setBackgroundResource(R.drawable.btn_them18);
            btn5.setBackgroundResource(R.drawable.btn_them18);
            btn6.setBackgroundResource(R.drawable.btn_them18);
            btn7.setBackgroundResource(R.drawable.btn_them18);
            btn8.setBackgroundResource(R.drawable.btn_them18);
            btn9.setBackgroundResource(R.drawable.btn_them18);
            btnStar.setBackgroundResource(R.drawable.btn_them18);
            btn0.setBackgroundResource(R.drawable.btn_them18);
            btnHash.setBackgroundResource(R.drawable.btn_them18);
        } else {
            btn1.setBackgroundResource(R.drawable.th_1);
            btn2.setBackgroundResource(R.drawable.th_12);
            btn3.setBackgroundResource(R.drawable.th_13);
            btn4.setBackgroundResource(R.drawable.th_14);
            btn5.setBackgroundResource(R.drawable.th_15);
            btn6.setBackgroundResource(R.drawable.th_16);
            btn7.setBackgroundResource(R.drawable.th_17);
            btn8.setBackgroundResource(R.drawable.th_18);
            btn9.setBackgroundResource(R.drawable.th_19);
            btnStar.setBackgroundResource(R.drawable.th_110);
            btn0.setBackgroundResource(R.drawable.th_111);
            btnHash.setBackgroundResource(R.drawable.th_1);
        }
    }

    public void rx() {
        Observable.just("").flatMap(new Function<String, ObservableSource<ContactsListModeldailor>>() {
            public ObservableSource<ContactsListModeldailor> apply(String str) throws Exception {
                ArrayList arrayList = new ArrayList();
                Cursor query = Fragment_Keypad.this.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, Fragment_Keypad.PROJECTION, null, null, "display_name ASC");
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
                Fragment_Keypad.this.contactsListModels.add(contactsListModeldailor);
                Collections.sort(Fragment_Keypad.this.contactsListModels, ContactsListModeldailor.contactsname);
                adapter.notifyItemChanged(Fragment_Keypad.this.contactsListModels.size() - 1);
                adapter.addfiltervalue();
            }

            public void onError(Throwable th) {

            }
        });
    }




}