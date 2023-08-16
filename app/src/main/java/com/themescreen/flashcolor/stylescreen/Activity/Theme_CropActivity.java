package com.themescreen.flashcolor.stylescreen.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.admobmanager.Appmanage;
import com.themescreen.flashcolor.stylescreen.admobmanager.SharePreferencess;
import com.themescreen.flashcolor.stylescreen.utils.AppConst;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Theme_CropActivity extends AppCompatActivity {
    ImageView ivdone,ivback;
    RelativeLayout lytLeftRotate,lytRightRotate;
    public static Bitmap croppedImage;
    CropImageView cropImageView;
    Bitmap newp;
    SharePreferencess sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ivdone=findViewById(R.id.ivdone);
        ivback=findViewById(R.id.ivback);
        sp=new SharePreferencess(this);
        lytLeftRotate=findViewById(R.id.lytLeftRotate);
        lytRightRotate=findViewById(R.id.lytRightRotate);

        cropImageView=(CropImageView) findViewById(R.id.cropImageView);
        cropImageView.setAspectRatio(1, 1);
        if (AppConst.Main_Pic.getWidth() >= 1300 || AppConst.Main_Pic.getHeight() >= 1500) {
            float min = Math.min(1280.0f / ((float) AppConst.Main_Pic.getWidth()), 1280.0f / ((float) AppConst.Main_Pic.getHeight()));
            Matrix matrix = new Matrix();
            matrix.postScale(min, min);
            newp = Bitmap.createBitmap(AppConst.Main_Pic, 0, 0, AppConst.Main_Pic.getWidth(), AppConst.Main_Pic.getHeight(), matrix, true);
        } else {
            this.newp = AppConst.Main_Pic;
        }
        cropImageView.setImageBitmap(newp);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appmanage.getInstance(Theme_CropActivity.this).show_INTERSTIAL(Theme_CropActivity.this, new Appmanage.MyCallback() {
                    @Override
                    public void callbackCall() {
                        Theme_CropActivity.super.onBackPressed();
                    }
                },sp.getBackCountAds(),false);

            }
        });
        ivdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                croppedImage = cropImageView.getCroppedImage();
                startActivity(new Intent(Theme_CropActivity.this, Theme_Dialer_Menu_Activity.class));
                setResult(-1, new Intent());
                Toast.makeText(Theme_CropActivity.this, "dialer image changed", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        lytLeftRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(90);

            }
        });

        lytRightRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(-90);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Appmanage.getInstance(Theme_CropActivity.this).show_INTERSTIAL(Theme_CropActivity.this, new Appmanage.MyCallback() {
            @Override
            public void callbackCall() {
              Theme_CropActivity.super.onBackPressed();
            }
        },sp.getBackCountAds(),false);
    }
}