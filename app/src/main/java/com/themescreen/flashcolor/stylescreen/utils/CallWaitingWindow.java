package com.themescreen.flashcolor.stylescreen.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.themescreen.flashcolor.stylescreen.R;


public class CallWaitingWindow {
    ImageView btnBack;
    Context context;
    ImageView imgDone;
    private LayoutInflater inflater;
    LinearLayout layAns;
    LinearLayout layCancelCall;
    onWaitingListner onWaitingListner;
    private WindowManager.LayoutParams params;
    RelativeLayout popLay;
    private TextView tv_title;
    View view;
    private WindowManager windowManager;

    public interface onWaitingListner {
        void onCall_Accept(Boolean bool, View view);

        void onCall_Decline();
    }

    public int animationStyle() {
        return -1;
    }

    public boolean dragEnable() {
        return true;
    }

    public int getWindowGravity() {
        return 48;
    }

    public void onWindowClick() {
    }

    public void onWindowCreated() {
    }

    public CallWaitingWindow(Context context2) {
        this.context = context2;
        this.onWaitingListner = (onWaitingListner) context2;
    }

    public void findViews(View view2) {
        this.popLay = (RelativeLayout) view2.findViewById(R.id.popLay);
        this.tv_title = (TextView) view2.findViewById(R.id.tv_title);
        this.layCancelCall = (LinearLayout) view2.findViewById(R.id.layCancelCall);
        this.btnBack = (ImageView) view2.findViewById(R.id.btnBack1);
        this.imgDone = (ImageView) view2.findViewById(R.id.imgDone);
        this.layAns = (LinearLayout) view2.findViewById(R.id.layAns);
        Help.getheightandwidth(this.context);
        Help.setSize(this.imgDone, 97, 97, true);
        Help.setSize(this.btnBack, 97, 97, true);
        Help.setSize(this.popLay, 1054, 184, true);
        Help.setSize(this.tv_title, 188, 47, true);
    }

    public void setName(String str) {
        ((TextView) this.view.findViewById(R.id.tv_title1)).setText(str);
        ((TextView) this.view.findViewById(R.id.tv_title)).setText("hold");
    }

    public View initView(LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R.layout.incoming_call_popup, (ViewGroup) null, false);
        this.view = inflate;
        return inflate;
    }

    public void setUpView() {
        LayoutInflater from = LayoutInflater.from(this.context);
        this.inflater = from;
        this.view = initView(from);
    }

    public void setAsWindow(String str) {
        showwaitingPopup(this.context, str);
    }

    private void showwaitingPopup(Context context2, String str) {
        if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(context2)) {
            if (!(this.windowManager == null && this.view == null)) {
                finishWindow();
            }
            this.windowManager = (WindowManager) context2.getSystemService(Context.WINDOW_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                this.params = new WindowManager.LayoutParams(-2, -2, 2038, 8, -3);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.params = new WindowManager.LayoutParams(-2, -2, 2002, 8, -3);
            } else {
                this.params = new WindowManager.LayoutParams(-2, -2, 2003, 8, -3);
            }
            this.params.gravity = 49;
            if (this.view == null) {
                setUpView();
                findViews(this.view);
            }
            this.view.findViewById(R.id.layAns).setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    boolean z;
                    Boolean.valueOf(false);
                    if (view.getTag().equals("0")) {
                        z = false;
                    } else {
                        z = true;
                    }
                    CallWaitingWindow.this.onWaitingListner.onCall_Accept(z, view);
                    ColorCallUtils.onClickEvent(view);
                    CallWaitingWindow.this.btnBack.setImageResource(R.drawable.swap_pressed);
                }
            });
            this.view.findViewById(R.id.layCancelCall).setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    CallWaitingWindow.this.onWaitingListner.onCall_Decline();
                    ColorCallUtils.onClickEvent(view);
                }
            });
            try {
                Log.i("theme head", " : incoming " + str);
                String contactNamebytherenumber = ColorCallUtils.getContactNamebytherenumber(context2, str);
                ((TextView) this.view.findViewById(R.id.tv_title1)).setText(str);
                if (!TextUtils.isEmpty(contactNamebytherenumber)) {
                    ((TextView) this.view.findViewById(R.id.tv_title1)).setText(contactNamebytherenumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (this.params != null) {
                    if (animationStyle() != -1) {
                        this.params.windowAnimations = animationStyle();
                    }
                    try {
                        WindowManager.LayoutParams layoutParams = this.params;
                        layoutParams.flags = 32;
                        layoutParams.gravity = getWindowGravity();
                        this.windowManager.addView(this.view, this.params);
                        dragView(this.params);
                        onWindowCreated();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    public void finishWindow() {
        View view2;
        try {
            WindowManager windowManager2 = this.windowManager;
            if (windowManager2 != null && (view2 = this.view) != null) {
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

    private void dragView(WindowManager.LayoutParams layoutParams) {
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
                    if (!(CallWaitingWindow.this.windowManager == null || view == null)) {
                        this.lastAction = motionEvent.getAction();
                    }
                    return true;
                } else if (action != 2) {
                    return CallWaitingWindow.this.dragEnable();
                } else {
                    if (view == null) {
                        return false;
                    }
                    try {
                        this.params.x = this.initialX + ((int) (motionEvent.getRawX() - this.initialTouchX));
                        this.params.y = this.initialY + ((int) (motionEvent.getRawY() - this.initialTouchY));
                        if (view != null) {
                            CallWaitingWindow.this.windowManager.updateViewLayout(view, this.params);
                        }
                        return true;
                    } catch (Exception unused) {
                        return false;
                    }
                }
            } else {
                CallWaitingWindow.this.onWindowClick();
                return true;
            }
        }
    }
}
