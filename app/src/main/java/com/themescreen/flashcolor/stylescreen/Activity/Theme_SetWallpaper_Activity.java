package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.themescreen.flashcolor.stylescreen.R;

public class Theme_SetWallpaper_Activity extends AppCompatActivity {
    ImageView img,imgback;
    Button setwallpaper;
    int image;
     WallpaperManager instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);
        img=findViewById(R.id.img);
        imgback=findViewById(R.id.imgback);
        setwallpaper=findViewById(R.id.setwallpaper);
        image=getIntent().getIntExtra("img",0);
        img.setImageResource(image);
        instance= WallpaperManager.getInstance(getApplicationContext());

        setwallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    instance.setBitmap(getBitmapFromView(img));
                    Toast.makeText(getApplicationContext(), "Set Wallpaper", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Theme_SetWallpaper_Activity.super.onBackPressed();
            }
        });
    }

    public static Bitmap getBitmapFromView(ImageView imageView) {
        Bitmap createBitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Drawable background = imageView.getBackground();
        if (background != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        imageView.draw(canvas);
        return createBitmap;
    }

}