package com.themescreen.flashcolor.stylescreen.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.themescreen.flashcolor.stylescreen.R;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Calling_Floating_Icon {
    public Context context;
    private ImageView imageViewProfile;
    private ImageView imageViewProfiledefault;
    private LayoutInflater inflater;
    private RelativeLayout layPRof;
    public OnTapListener onTapListener;
    public WindowManager.LayoutParams params;
    private LinearLayout revealEffectLayout;
    private View view;
    private WindowManager windowManager;

    public interface OnTapListener {
        void onReturnToCall();
    }



    private void onWindowClick() {
    }

    public int animationStyle() {
        return -1;
    }

    public boolean dragEnable() {
        return false;
    }

    public void onWindowCreated() {
    }


    public class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

        private SingleTapConfirm() {
        }
    }

    public void setUpView() {
        LayoutInflater from = LayoutInflater.from(this.context);
        this.inflater = from;
        this.view = initView(from);
    }

    public Calling_Floating_Icon(Context context2) {
        this.context = context2;
    }

    public View initView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.row_folating_icon, (ViewGroup) null);
    }

    public void findViews(View view2) {
        this.layPRof = (RelativeLayout) view2.findViewById(R.id.layPRof);
        this.imageViewProfile = (ImageView) view2.findViewById(R.id.imageViewProfile);
        this.imageViewProfiledefault = (ImageView) view2.findViewById(R.id.imageViewProfiledefault);
        this.revealEffectLayout = (LinearLayout) view2.findViewById(R.id.revealEffectLayout);
        Help.getheightandwidth(this.context);
        Help.setSize(this.imageViewProfile, 200, 200, true);
        Help.setSize(this.imageViewProfiledefault, 200, 200, true);
    }

    public void setAsWindow() {
        showCallHead(this.context);
    }

    public void finishWindow() {
        View view2 = getView();
        try {
            WindowManager windowManager2 = this.windowManager;
            if (windowManager2 != null && view2 != null) {
                windowManager2.removeView(view2);
                this.windowManager = null;
                this.view = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() {
        finishWindow();
        this.windowManager = null;
        this.view = null;
    }

    public void setTapListener(OnTapListener onTapListener2) {
        this.onTapListener = onTapListener2;
    }

    public Bitmap getContactsPhoto(String str) {
        Cursor query;
        Bitmap bitmap = null;
        try {
            Uri withAppendedPath = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str));
            if (!(withAppendedPath == null || (query = this.context.getContentResolver().query(withAppendedPath, null, null, null, null)) == null)) {
                while (query.moveToNext()) {
                    String string = query.getString(query.getColumnIndex("photo_uri"));
                    if (string != null) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.context.getContentResolver(), Uri.parse(string));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception unused) {
        }
        return bitmap;
    }

    public View getView() {
        return this.view;
    }

    private void showCallHead(Context context2) {
        if (Settings.canDrawOverlays(this.context)) {
            if (!(this.windowManager == null && getView() == null)) {
                finishWindow();
            }
            this.windowManager = (WindowManager) context2.getSystemService("window");
            if (Build.VERSION.SDK_INT >= 26) {
                this.params = new WindowManager.LayoutParams(-2, -2, 2038, 8, -3);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.params = new WindowManager.LayoutParams(-2, -2, 2002, 8, -3);
            } else {
                this.params = new WindowManager.LayoutParams(-2, -2, 2003, 8, -3);
            }
            this.params.gravity = 21;
            this.params.x = 0;
            this.params.y = 100;
            final GestureDetector gestureDetector = new GestureDetector(context2, new SingleTapConfirm());
            if (getView() == null) {
                setUpView();
                findViews(getView());
            }
            try {
                Log.i("theme head", " : incoming " + Utils.numberCall);
                Bitmap contactsPhoto = getContactsPhoto(Utils.numberCall);
                if (contactsPhoto == null) {
                    this.imageViewProfiledefault.setImageResource(R.drawable.rounded_icon);
                    this.imageViewProfiledefault.setVisibility(View.VISIBLE);
                    this.imageViewProfile.setVisibility(View.INVISIBLE);
                } else if (CallManager.callWaiting != null) {
                    this.imageViewProfiledefault.setImageResource(R.drawable.rounded_icon);
                    this.imageViewProfiledefault.setVisibility(View.VISIBLE);
                    this.imageViewProfile.setVisibility(View.INVISIBLE);
                } else {
                    this.imageViewProfile.setImageBitmap(contactsPhoto);
                    this.imageViewProfiledefault.setVisibility(View.INVISIBLE);
                    this.imageViewProfile.setVisibility(View.VISIBLE);
                }
                Help.getheightandwidth(this.context);
                Help.setSize(getView().findViewById(R.id.layPRof), 200, 200, true);
                Help.setSize(getView().findViewById(R.id.imageViewProfile), 200, 200, true);
                Help.setSize(getView().findViewById(R.id.imageViewProfiledefault), 200, 200, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (getView() != null) {
                getView().setOnTouchListener(new View.OnTouchListener() {
                    private float initialTouchX;
                    private float initialTouchY;
                    private int initialX;
                    private int initialY;
                    private int lastAction;

                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (gestureDetector.onTouchEvent(motionEvent)) {
                            if (Calling_Floating_Icon.this.onTapListener != null) {
                                Calling_Floating_Icon.this.onTapListener.onReturnToCall();
                            }
                            Calling_Floating_Icon.this.moveWithReveal(new AnimatorListenerAdapter() {

                                public void onAnimationEnd(Animator animator) {
                                }
                            });
                            return true;
                        }
                        int action = motionEvent.getAction();
                        if (action == 0) {
                            this.initialX = Calling_Floating_Icon.this.params.x;
                            this.initialY = Calling_Floating_Icon.this.params.y;
                            this.initialTouchX = motionEvent.getRawX();
                            this.initialTouchY = motionEvent.getRawY();
                            this.lastAction = motionEvent.getAction();
                            return true;
                        } else if (action == 1) {
                            if (!(Calling_Floating_Icon.this.windowManager == null || Calling_Floating_Icon.this.getView() == null)) {
                                this.lastAction = motionEvent.getAction();
                            }
                            return true;
                        } else if (action != 2) {
                            return false;
                        } else {
                            if (!(Calling_Floating_Icon.this.windowManager == null || Calling_Floating_Icon.this.getView() == null)) {
                                Calling_Floating_Icon.this.params.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                                Calling_Floating_Icon.this.windowManager.updateViewLayout(Calling_Floating_Icon.this.getView(), Calling_Floating_Icon.this.params);
                                this.lastAction = motionEvent.getAction();
                            }
                            return true;
                        }
                    }
                });
            }
            try {
                if (this.params != null) {
                    this.windowManager.addView(getView(), this.params);
                    dragView(this.params);
                    onWindowCreated();
                }
            } catch (Exception unused) {
            }
        }
    }

    public void moveWithReveal(final AnimatorListenerAdapter animatorListenerAdapter) {
        WindowManager.LayoutParams layoutParams;
        try {
            final LinearLayout linearLayout = this.revealEffectLayout;
            if (linearLayout != null && (layoutParams = this.params) != null) {
                layoutParams.height = -1;
                this.params.width = -1;
                this.windowManager.updateViewLayout(this.view, this.params);
                linearLayout.post(new Runnable() {

                    public void run() {
                        Calling_Floating_Icon.this.layPRof.setVisibility(View.GONE);
                        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(linearLayout, (linearLayout.getLeft() + linearLayout.getRight()) / 2, linearLayout.getTop(), 0.0f, (float) Math.max(linearLayout.getWidth(), linearLayout.getHeight()));
                        createCircularReveal.addListener(animatorListenerAdapter);
                        createCircularReveal.start();
                    }
                });
            }
        } catch (Exception unused) {
        }
    }

    public void dragView(WindowManager.LayoutParams layoutParams) {
        if (dragEnable()) {
            this.view.setOnTouchListener(new Dragable(new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

                public boolean onSingleTapUp(MotionEvent motionEvent) {
                    return true;
                }
            }), layoutParams));
        }
    }


    public class Dragable implements View.OnTouchListener {
        private GestureDetector gestureDetector;
        private float initialTouchX;
        private float initialTouchY;
        private int initialX;
        private int initialY;
        private int lastAction;
        private WindowManager.LayoutParams params;

        public Dragable(GestureDetector gestureDetector2, WindowManager.LayoutParams layoutParams) {
            this.gestureDetector = gestureDetector2;
            this.params = layoutParams;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            GestureDetector gestureDetector2 = this.gestureDetector;
            if (gestureDetector2 == null || !gestureDetector2.onTouchEvent(motionEvent)) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    this.initialX = this.params.x;
                    this.initialY = this.params.y;
                    this.initialTouchX = motionEvent.getRawX();
                    this.initialTouchY = motionEvent.getRawY();
                    this.lastAction = motionEvent.getAction();
                    return true;
                } else if (action == 1) {
                    if (!(Calling_Floating_Icon.this.windowManager == null || view == null)) {
                        this.lastAction = motionEvent.getAction();
                    }
                    return true;
                } else if (action != 2) {
                    return Calling_Floating_Icon.this.dragEnable();
                } else {
                    if (view == null) {
                        return false;
                    }
                    try {
                        this.params.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                        this.params.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                        if (view != null) {
                            Calling_Floating_Icon.this.windowManager.updateViewLayout(view, this.params);
                        }
                        return true;
                    } catch (Exception unused) {
                        return false;
                    }
                }
            } else {
                Calling_Floating_Icon.this.onWindowClick();
                return true;
            }
        }
    }
}
