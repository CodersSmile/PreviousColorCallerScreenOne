package com.themescreen.flashcolor.stylescreen.Activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.themescreen.flashcolor.stylescreen.R;
import com.themescreen.flashcolor.stylescreen.Service.CallService;
import com.themescreen.flashcolor.stylescreen.phone_reciever.PhoneCallReceiver;
import com.themescreen.flashcolor.stylescreen.sqllite.SQLiteHelperClass;
import com.themescreen.flashcolor.stylescreen.utils.CallManager;
import com.themescreen.flashcolor.stylescreen.utils.Call_Detector;
import com.themescreen.flashcolor.stylescreen.utils.Calling_Floating_Icon;
import com.themescreen.flashcolor.stylescreen.utils.ColorCallUtils;
import com.themescreen.flashcolor.stylescreen.utils.Help;
import com.themescreen.flashcolor.stylescreen.utils.Utils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Calling_InComing_Outgoing extends AppCompatActivity implements View.OnClickListener, Calling_Floating_Icon.OnTapListener {
    public static String ACTIONC_CHANGE = "ACTIONCHANGE";
    public static String ActionCallAnswerStop = "actioncallend";
    public static String CUTMAIN = "CUTMAIN";
    public static String HIDE_WaitCallAction = "HIDE_WaitCallAction";
    public static final String MyPREFERENCES = "myprefs";
    public static String SHOW_WaitCallAction = "SHOW_WaitCallAction";
    public static String UPDATE_WaitCallAction = "UPDATE_WaitCallAction";
    public static String actionUpdate = "ACTIONUPDATE";
    public static String ansvalue = "ansicon";
    public static boolean from_call_or_not = false;
    private static boolean isincoming = false;
    public static String rejvalue = "reicon";
    public static int sim_id = 1;
    public static final String value = "theamvideo";
    String TAG = "CALL_OUTGOING_ACTIVITY";
    int ans;
    private AudioManager audioManager;
    LinearLayout btn0;
    LinearLayout btn1;
    LinearLayout btn2;
    LinearLayout btn3;
    LinearLayout btn4;
    LinearLayout btn5;
    LinearLayout btn6;
    LinearLayout btn7;
    LinearLayout btn8;
    LinearLayout btn9;
    ImageView btnBack1;
    ImageView btnCall;
    ImageView btnCancel;
    LinearLayout btnHash;
    LinearLayout btnStar;
    private Calling_Floating_Icon callingFloatingIcon;
    private String cameraId;
    Context cn = this;
    Context context;
    String displayTxt = "";
    EditText edPad;
    long endTime = 0;
    private int field;
    Handler handler;
    private CircleImageView imageViewProfile;
    private CircleImageView imageViewProfile1;
    ImageView img0;
    ImageView img1;
    ImageView img10;
    ImageView img11;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;
    ImageView img7;
    ImageView img8;
    ImageView img9;
    ImageView imgAdd;
    private ImageView imgAns1;
    ImageView imgDone;
    private ImageView imgHangUp1;
    ImageView imgHangup;
    ImageView imgHold;
    ImageView imgKeypad;
    ImageView imgMute;
    ImageView imgProfile;
    ImageView imgProfile1;
    ImageView imgSpeaker;
    ImageView img_Keypad_new;
    ImageView img_speker_new;
    private VideoView imgvideo;
    private Call_Detector inComingCallPresenter;
    private boolean isHold = false;
    Boolean isfromHold = false;
    LinearLayout layAns;
    private RelativeLayout layAns1;
    LinearLayout layCancel;
    LinearLayout layCancelCall;
    public LinearLayout layDialpad;
    private RelativeLayout layHangUp1;
    LinearLayout layNumberDial;
    private RelativeLayout layPRof1;
    LinearLayout layincoming;
    LinearLayout layoutgoing;
    private Callback mCallback = new Callback();
    private Camera mCamera;
    private Camera.Parameters mParams;
    PowerManager.WakeLock mProximityWakeLock;
    private int mState;
    MediaPlayer mp = new MediaPlayer();
    private String number;
    private boolean on;
    private long pauseOffset = 0;
    private VideoView playerManager;
    RelativeLayout popLay;
    private PowerManager powerManager;
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.hello.action1")) {
                Activity_Calling_InComing_Outgoing.this.finishAndRemoveTask();
            } else if (intent.getAction().equals(Activity_Calling_InComing_Outgoing.actionUpdate)) {
                Activity_Calling_InComing_Outgoing.this.userEndCall = false;
                if (CallManager.sCall != null) {
                    Activity_Calling_InComing_Outgoing.this.setup();
                    if (CallManager.callWaiting != null) {
                        CallManager.callWaiting.unregisterCallback(Activity_Calling_InComing_Outgoing.this.mCallback);
                    }
                    Activity_Calling_InComing_Outgoing.this.updateCallBackObjject(true);
                    Activity_Calling_InComing_Outgoing.this.updateUI(CallManager.sCall.getState());
                }
            } else if (intent.getAction().equals(Activity_Calling_InComing_Outgoing.CUTMAIN)) {
                Activity_Calling_InComing_Outgoing.this.userEndCall = false;
                if (CallManager.sCall != null) {
                    CallManager.sCall.unregisterCallback(Activity_Calling_InComing_Outgoing.this.mCallback);
                }
            } else if (intent.getAction().equals(Activity_Calling_InComing_Outgoing.SHOW_WaitCallAction)) {
                Activity_Calling_InComing_Outgoing.this.show_waitingcall_Pop();
            } else if (intent.getAction().equals(Activity_Calling_InComing_Outgoing.HIDE_WaitCallAction)) {
                Activity_Calling_InComing_Outgoing.this.hide_waitingcall_Pop();
            } else if (intent.getAction().equals(Activity_Calling_InComing_Outgoing.UPDATE_WaitCallAction)) {
                Activity_Calling_InComing_Outgoing.this.show_waitingcall_Pop();
            }
        }
    };
    BroadcastReceiver receiver1 = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Log.e("ppp", " Slot Number " + Activity_Calling_InComing_Outgoing.this.capturedSimSlot(extras));
            }
            if (intent.getAction().equals(Activity_Calling_InComing_Outgoing.ActionCallAnswerStop)) {
                Activity_Calling_InComing_Outgoing.this.incommingDestroy();
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            Activity_Calling_InComing_Outgoing.this.destroyCallHead(true);
                            if (Activity_Calling_InComing_Outgoing.this.callingFloatingIcon != null) {
                                Activity_Calling_InComing_Outgoing.this.callingFloatingIcon.cleanUp();
                            }
                        } catch (Exception unused) {
                        }
                    }
                }).start();
                Activity_Calling_InComing_Outgoing.this.finishAndRemoveTask();
            } else if (intent.getAction().equals(Activity_Calling_InComing_Outgoing.ACTIONC_CHANGE)) {
                Activity_Calling_InComing_Outgoing.this.layincoming.setVisibility(View.GONE);
                Activity_Calling_InComing_Outgoing.this.layoutgoing.setVisibility(View.VISIBLE);
                boolean unused = Activity_Calling_InComing_Outgoing.isincoming = false;
                Activity_Calling_InComing_Outgoing.this.incommingDestroy();
                Activity_Calling_InComing_Outgoing.this.outgoingCreate();
            }
        }
    };
    int rej;
    Runnable runnable = new Runnable() {

        public void run() {
            if (Build.VERSION.SDK_INT > 22 && CallManager.sCall != null) {
                Utils.callStartTime = CallManager.sCall.getDetails().getConnectTimeMillis();
            }
            Activity_Calling_InComing_Outgoing.this.pauseOffset = (System.currentTimeMillis() - Utils.callStartTime) / 1000;
            Utils.callEndTime = System.currentTimeMillis();
            Activity_Calling_InComing_Outgoing activity_Calling_InComing_Outgoing = Activity_Calling_InComing_Outgoing.this;
            String timeFromSeconds = activity_Calling_InComing_Outgoing.getTimeFromSeconds(activity_Calling_InComing_Outgoing.pauseOffset);
            if (timeFromSeconds.length() > 9) {
                timeFromSeconds = "00:00";
            }
            Activity_Calling_InComing_Outgoing.this.textTiming.setText("" + timeFromSeconds);
            Activity_Calling_InComing_Outgoing.this.handler.postDelayed(Activity_Calling_InComing_Outgoing.this.runnable, 100);
        }
    };
    SharedPreferences sharedpreferences;
    long startTime = 0;
    private boolean stateMic = false;
    private boolean stateSpeaker = false;
    TextView textTiming;
    private TextView textViewCallerName;
    private TextView textViewCallerName1;
    private TextView textViewNumber1;
    String theam;
    private Thread thread;
    private TextView tv1;
    private TextView tv2;
    private TextView tvText;
    private TextView tv_title;
    TextView tv_title1;
    private boolean userEndCall;
    private PowerManager.WakeLock wakeLock;



    private void hide_waitingcall_Pop() {
        this.popLay.setVisibility(View.GONE);
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void show_waitingcall_Pop() {
        if (CallManager.callWaiting != null) {
            String str = null;
            try {
                str = URLDecoder.decode(CallManager.callWaiting.getDetails().getHandle().toString(), "utf-8").replace("tel:", "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (CallManager.callWaiting.getState() == Call.STATE_HOLDING) {
                ((ImageView) findViewById(R.id.btnBack1)).setImageResource(R.drawable.swap_pressed);
            } else {
                ((ImageView) findViewById(R.id.btnBack1)).setImageResource(R.drawable.accept_button);
            }
            this.layAns.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    boolean z;
                    Boolean.valueOf(false);
                    if (view.getTag().equals("0")) {
                        z = false;
                    } else {
                        z = true;
                    }
                    Activity_Calling_InComing_Outgoing.this.btnBack1.setImageResource(R.drawable.swap_pressed);
                    if (CallService.callService != null) {
                        CallService.callService.onCall_Accept(z, view);
                    }
                    Utils.onClickEvent(view);
                }
            });
            this.layCancelCall.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    if (CallService.callService != null) {
                        CallService.callService.onCall_Decline();
                    }
                    Utils.onClickEvent(view);
                }
            });
            try {
                Log.e("OOO", " : incoming " + str);
                this.tv_title1.setText(str);
                String contactName = Utils.getContactName(this.context, str);
                if (!TextUtils.isEmpty(contactName)) {
                    this.tv_title.setText(contactName);
                }
                this.tv_title.setText("hold");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            this.popLay.setVisibility(View.VISIBLE);
        }
    }



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.e("AAA", "Outgoing Activity Created");
        requestWindowFeature(1);
        getWindow().setFlags(512, 512);
        setRequestedOrientation(1);
        try {
            if (Build.VERSION.SDK_INT >= 27) {
                setShowWhenLocked(true);
                setTurnScreenOn(true);
            }
            getWindow().addFlags(524288);
            getWindow().addFlags(4194304);
            getWindow().addFlags(2097152);
            getWindow().addFlags(128);
            getWindow().addFlags(67108864);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_call_outgoing);
        startProximitySensor();
        this.context = this;
        isincoming = getIntent().getBooleanExtra(Utils.ISINCOMING, false);
        outgoingCreate();
    }



    private void outgoingCreate() {
        UI();
        resize();
        setup();
        clickPart();
        updateUI(CallManager.getState());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.hello.action1");
        intentFilter.addAction(actionUpdate);
        intentFilter.addAction(CUTMAIN);
        intentFilter.addAction(SHOW_WaitCallAction);
        intentFilter.addAction(HIDE_WaitCallAction);
        intentFilter.addAction(UPDATE_WaitCallAction);
        registerReceiver(this.receiver, intentFilter);
        Intent intent = new Intent();
        intent.setAction("com.hello.action");
        sendBroadcast(intent);
    }

    private void resize() {
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.btnCancel, 60, 45, true);
        Help.setSize(this.layNumberDial, 700, 110, true);
        Help.setSize(findViewById(R.id.iv_divide), 700, 6, true);
        Help.setSize(this.btnCall, 180, 180, true);
        Help.setSize(this.imgMute, 164, 224, true);
        Help.setSize(this.imgHold, 164, 224, true);
        Help.setSize(this.imgKeypad, 164, 224, true);
        Help.setSize(this.imgSpeaker, 164, 224, true);
        Help.setSize(this.imgHangup, 180, 180, true);
        Help.setSize(this.imgProfile, 300, 300, true);
        Help.setSize(this.imageViewProfile, 300, 300, true);
        Help.setSize(this.img0, 80, 105, true);
        Help.setSize(this.img1, 80, 105, true);
        Help.setSize(this.img2, 80, 105, true);
        Help.setSize(this.img3, 80, 105, true);
        Help.setSize(this.img4, 80, 105, true);
        Help.setSize(this.img5, 80, 105, true);
        Help.setSize(this.img6, 80, 105, true);
        Help.setSize(this.img7, 80, 105, true);
        Help.setSize(this.img8, 80, 105, true);
        Help.setSize(this.img9, 80, 105, true);
        Help.setSize(this.img10, 80, 105, true);
        Help.setSize(this.img11, 80, 105, true);
        Help.setSize(this.img_Keypad_new, 62, 70, true);
        Help.setSize(this.img_speker_new, 62, 70, true);
    }



    private void setup() {
        String str = "";
        if (CallManager.sCall != null) {
            try {
                str = URLDecoder.decode(CallManager.sCall.getDetails().getHandle().toString(), "utf-8").replace("tel:", str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.tvText.setText(str);
            String finalStr = str;
            new Thread(new Runnable() {

                public void run() {
                    String contactName = Activity_Calling_InComing_Outgoing.getContactName(Activity_Calling_InComing_Outgoing.this.context, finalStr);
                    if (TextUtils.isEmpty(contactName) || contactName.equals("")) {
                        Activity_Calling_InComing_Outgoing.this.displayTxt = "Unknown";
                    } else {
                        Activity_Calling_InComing_Outgoing.this.displayTxt = contactName;
                    }
                    Activity_Calling_InComing_Outgoing.this.runOnUiThread(new Runnable() {

                        public void run() {
                            Activity_Calling_InComing_Outgoing.this.textViewCallerName.setText(Activity_Calling_InComing_Outgoing.this.displayTxt);
                            Activity_Calling_InComing_Outgoing.this.setimage(Activity_Calling_InComing_Outgoing.this);
                        }
                    });
                }
            }).start();
        }
    }

    public void setimage(Context context2) {
        if (CallManager.sCall == null || CallManager.callWaiting == null) {
            if (CallManager.sCall != null) {
                if (CallManager.sCall.getState() == 4) {
                    try {
                        Bitmap retrieveContactPhoto = Utils.retrieveContactPhoto(context2, URLDecoder.decode(CallManager.sCall.getDetails().getHandle().toString(), "utf-8").replace("tel:", ""));
                        if (retrieveContactPhoto != null) {
                            Glide.with(getApplicationContext()).load(retrieveContactPhoto).into(this.imageViewProfile);
                            return;
                        }
                        Glide.with(getApplicationContext()).load(BitmapFactory.decodeResource(getResources(), R.drawable.avtar)).into(this.imageViewProfile);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            } else if (CallManager.callWaiting != null && CallManager.callWaiting.getState() == 4) {
                try {
                    Bitmap retrieveContactPhoto2 = Utils.retrieveContactPhoto(context2, URLDecoder.decode(CallManager.callWaiting.getDetails().getHandle().toString(), "utf-8").replace("tel:", ""));
                    if (retrieveContactPhoto2 != null) {
                        Glide.with(getApplicationContext()).load(retrieveContactPhoto2).into(this.imageViewProfile);
                        return;
                    }
                    try {
                        Glide.with(this.context).load(BitmapFactory.decodeResource(getResources(), R.drawable.avtar)).into(this.imageViewProfile);
                    } catch (Exception e2) {
                        Log.e(this.TAG, "setimage: " + e2.getMessage());
                    }
                } catch (UnsupportedEncodingException e3) {
                    e3.printStackTrace();
                }
            }
        } else if (CallManager.sCall.getState() == 4) {
            try {
                Bitmap retrieveContactPhoto3 = Utils.retrieveContactPhoto(context2, URLDecoder.decode(CallManager.sCall.getDetails().getHandle().toString(), "utf-8").replace("tel:", ""));
                if (retrieveContactPhoto3 != null) {
                    Glide.with(this.context).load(retrieveContactPhoto3).into(this.imageViewProfile);
                    return;
                }
                Glide.with(this.context).load(BitmapFactory.decodeResource(getResources(), R.drawable.avtar)).into(this.imageViewProfile);
            } catch (UnsupportedEncodingException e4) {
                e4.printStackTrace();
            }
        } else if (CallManager.callWaiting.getState() == 4) {
            try {
                Bitmap retrieveContactPhoto4 = Utils.retrieveContactPhoto(context2, URLDecoder.decode(CallManager.callWaiting.getDetails().getHandle().toString(), "utf-8").replace("tel:", ""));
                if (retrieveContactPhoto4 != null) {
                    Glide.with(this.context).load(retrieveContactPhoto4).into(this.imageViewProfile);
                    return;
                }
                try {
                    Glide.with(this.context).load(BitmapFactory.decodeResource(getResources(), R.drawable.avtar)).into(this.imageViewProfile);
                } catch (Exception e5) {
                    Log.e(this.TAG, "setimage: " + e5.getMessage());
                }
            } catch (UnsupportedEncodingException e6) {
                e6.printStackTrace();
            }
        }
    }

    public static String getContactName(Context context2, String str) {
        Cursor query = context2.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)), new String[]{"display_name"}, null, null, null);
        String str2 = null;
        if (query == null) {
            return null;
        }
        if (query.moveToFirst()) {
            str2 = query.getString(query.getColumnIndex("display_name"));
        }
        if (query != null && !query.isClosed()) {
            query.close();
        }
        return str2;
    }

    private void clickPart() {
        this.btn0.setOnClickListener(this);
        this.btn1.setOnClickListener(this);
        this.btn2.setOnClickListener(this);
        this.btn3.setOnClickListener(this);
        this.btn4.setOnClickListener(this);
        this.btn5.setOnClickListener(this);
        this.btn6.setOnClickListener(this);
        this.btn7.setOnClickListener(this);
        this.btn8.setOnClickListener(this);
        this.btn9.setOnClickListener(this);
        this.btnHash.setOnClickListener(this);
        this.layCancel.setOnClickListener(this);
        this.btnStar.setOnClickListener(this);
        this.imgMute.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Activity_Calling_InComing_Outgoing.this.muteProcess();
                Utils.onClickEvent(view);
            }
        });
        this.imgSpeaker.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Activity_Calling_InComing_Outgoing.this.speakerProcess();
                Utils.onClickEvent(view);
            }
        });
        this.imgHold.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Activity_Calling_InComing_Outgoing.this.holdingProcess();
                Utils.onClickEvent(view);
            }
        });
        this.imgHangup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Activity_Calling_InComing_Outgoing.this.userEndCall = true;
                Activity_Calling_InComing_Outgoing.this.endCallProcess();
                Utils.onClickEvent(view);
            }
        });
        this.imgKeypad.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (Activity_Calling_InComing_Outgoing.this.layDialpad.getVisibility() == 0) {
                    Activity_Calling_InComing_Outgoing.this.imgKeypad.setImageResource(R.drawable.keypad_event);
                    Activity_Calling_InComing_Outgoing.this.imgMute.setVisibility(View.VISIBLE);
                    Activity_Calling_InComing_Outgoing.this.imgSpeaker.setVisibility(View.VISIBLE);
                    Activity_Calling_InComing_Outgoing.this.imgHold.setVisibility(View.VISIBLE);
                    Activity_Calling_InComing_Outgoing.this.img_speker_new.setVisibility(View.GONE);
                    Activity_Calling_InComing_Outgoing.this.img_Keypad_new.setVisibility(View.GONE);
                    Activity_Calling_InComing_Outgoing.this.layDialpad.setVisibility(View.INVISIBLE);
                    Utils.onClickEvent(view);
                    return;
                }
                Activity_Calling_InComing_Outgoing.this.imgKeypad.setImageResource(R.drawable.keypad_presss);
                Activity_Calling_InComing_Outgoing.this.imgMute.setVisibility(View.INVISIBLE);
                Activity_Calling_InComing_Outgoing.this.imgSpeaker.setVisibility(View.INVISIBLE);
                Activity_Calling_InComing_Outgoing.this.imgHold.setVisibility(View.INVISIBLE);
                Activity_Calling_InComing_Outgoing.this.imgKeypad.setVisibility(View.INVISIBLE);
                Activity_Calling_InComing_Outgoing.this.img_speker_new.setVisibility(View.VISIBLE);
                Activity_Calling_InComing_Outgoing.this.img_Keypad_new.setVisibility(View.VISIBLE);
                Activity_Calling_InComing_Outgoing.this.layDialpad.setVisibility(View.VISIBLE);
                Utils.onClickEvent(view);
            }
        });
        this.btnCall.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + Activity_Calling_InComing_Outgoing.this.edPad.getText().toString().trim()));
                Activity_Calling_InComing_Outgoing.this.context.startActivity(intent);
                Utils.onClickEvent(view);
            }
        });
    }

    public void endCallProcess() {
        endCall();
    }



    private void holdingProcess() {
        try {
            if (this.isHold) {
                CallManager.sCall.unhold();
                this.isHold = false;
                this.imgHold.setSelected(false);
                return;
            }
            CallManager.sCall.hold();
            this.isHold = true;
            this.imgHold.setSelected(true);
        } catch (Exception unused) {
            Log.e("AAAA", "Hold issue");
        }
    }



    private void speakerProcess() {
        if (Build.VERSION.SDK_INT > 22) {
            if (CallService.callService != null && CallService.callService.getCallAudioState() != null) {
                if (CallService.callService.getCallAudioState().getRoute() == 8) {
                    CallService.callService.setAudioRoute(5);
                    this.stateSpeaker = false;
                    this.imgSpeaker.setSelected(false);
                    this.img_speker_new.setSelected(false);
                    return;
                }
                CallService.callService.setAudioRoute(8);
                this.stateSpeaker = true;
                this.imgSpeaker.setSelected(true);
                this.img_speker_new.setSelected(true);
            }
        } else if (this.stateSpeaker) {
            this.audioManager.setMode(2);
            this.audioManager.setSpeakerphoneOn(false);
            this.stateSpeaker = false;
            this.imgSpeaker.setSelected(false);
            this.img_speker_new.setSelected(false);
        } else {
            this.audioManager.setMode(2);
            this.audioManager.setMicrophoneMute(false);
            this.audioManager.setSpeakerphoneOn(true);
            this.stateSpeaker = true;
            this.imgSpeaker.setSelected(true);
            this.img_speker_new.setSelected(true);
        }
    }



    private void muteProcess() {
        if (this.stateMic) {
            this.audioManager.setMicrophoneMute(false);
            this.audioManager.setMode(2);
            this.stateMic = false;
            this.imgMute.setSelected(false);
            return;
        }
        this.audioManager.setMode(2);
        this.audioManager.setMicrophoneMute(true);
        this.imgMute.setSelected(true);
        this.stateMic = true;
    }



    public void onStop() {
        super.onStop();
    }



    public void onDestroy() {
        unregisterReceiver(this.receiver);
        cleanCallHead();
        Utils.isOutgoingScreenActive = false;
        turnOff();
        removeProxmitySensor();
        super.onDestroy();
    }



    public void onPause() {
        super.onPause();
        if (!Utils.isPhoneLocked(this.context)) {
            try {
                if (!isFinishing()) {
                    showCallHead(this.context);
                }
            } catch (Exception e) {
                Log.e(NotificationCompat.CATEGORY_MESSAGE, "" + e.toString());
            }
        } else if (!isFinishing()) {
            startParentCallActivity(this.context, true);
        }
    }


    public void onResume() {
        String str;
        super.onResume();
        new Thread(new Runnable() {

            public void run() {
                try {
                    Activity_Calling_InComing_Outgoing.this.destroyCallHead(true);
                    if (Activity_Calling_InComing_Outgoing.this.callingFloatingIcon != null) {
                        Activity_Calling_InComing_Outgoing.this.callingFloatingIcon.cleanUp();
                    }
                } catch (Exception unused) {
                }
            }
        }).start();
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(5894);
        }
        if (this.layincoming.getVisibility() == 0 && (str = this.theam) != null && !str.isEmpty() && !this.theam.equals("")) {
            this.imgvideo.setVideoURI(Uri.parse(this.theam));
            this.imgvideo.start();
        }
    }



    public void onStart() {
        super.onStart();
        Utils.isOutgoingScreenActive = true;
    }

    public Calling_Floating_Icon getCallHead() {
        if (this.callingFloatingIcon == null) {
            Calling_Floating_Icon calling_Floating_Icon = new Calling_Floating_Icon(this.context);
            this.callingFloatingIcon = calling_Floating_Icon;
            calling_Floating_Icon.setTapListener(this);
        }
        return this.callingFloatingIcon;
    }

    public void showCallHead(Context context2) {
        if (!isFinishing()) {
            new Thread(new Runnable() {

                public void run() {
                    Activity_Calling_InComing_Outgoing.this.getCallHead().setAsWindow();
                }
            }).run();
        }
    }

    public void destroyCallHead(boolean z) {
        if (!z || this.callingFloatingIcon == null) {
            Calling_Floating_Icon calling_Floating_Icon = this.callingFloatingIcon;
            if (calling_Floating_Icon != null && !isFinishing()) {
                calling_Floating_Icon.finishWindow();
                return;
            }
            return;
        }
        new Thread(new Runnable() {

            public void run() {
                if (Activity_Calling_InComing_Outgoing.this.callingFloatingIcon != null && !Activity_Calling_InComing_Outgoing.this.isFinishing()) {
                    Activity_Calling_InComing_Outgoing.this.callingFloatingIcon.finishWindow();
                }
            }
        }).start();
    }

    public static void startParentCallActivity(Context context2, boolean z) {
        Intent intent = new Intent(context2, Activity_Calling_InComing_Outgoing.class);
        intent.putExtra(Utils.ISINCOMING, isincoming);
        PendingIntent activity = PendingIntent.getActivity(context2, 0, intent, 134217728);
        if (activity == null) {
            Intent intent2 = new Intent(context2, Activity_Calling_InComing_Outgoing.class);
            intent2.putExtra(Utils.ISINCOMING, isincoming);
            activity = PendingIntent.getActivity(context2, 0, intent2, 268435456);
        }
        try {
            activity.send();
        } catch (PendingIntent.CanceledException unused) {
            context2.startActivity(new Intent(context2, Activity_Calling_InComing_Outgoing.class).putExtra(Utils.ISINCOMING, isincoming));
        }
    }

    public void onReturnToCall() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            returnToCall();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                public void run() {
                    Activity_Calling_InComing_Outgoing.this.returnToCall();
                }
            });
        }
    }



    private void returnToCall() {
        try {
            destroyCallHead(true);
            Calling_Floating_Icon calling_Floating_Icon = this.callingFloatingIcon;
            if (calling_Floating_Icon != null) {
                calling_Floating_Icon.cleanUp();
            }
        } catch (Exception unused) {
        }
        startParentCallActivity(this, false);
    }

    private void cleanCallHead() {
        if (this.callingFloatingIcon != null) {
            destroyCallHead(false);
            this.callingFloatingIcon.cleanUp();
            this.callingFloatingIcon = null;
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= 16) {
            finishAffinity();
        } else {
            finish();
        }
    }

    private void UI() {
        this.textViewCallerName = (TextView) findViewById(R.id.textViewCallerName);
        this.textTiming = (TextView) findViewById(R.id.textViewNumber);
        this.tvText = (TextView) findViewById(R.id.tvText);
        this.imageViewProfile = (CircleImageView) findViewById(R.id.imageViewProfile);
        this.imgProfile = (ImageView) findViewById(R.id.imgProfile);
        this.imgMute = (ImageView) findViewById(R.id.imgMute);
        this.imgSpeaker = (ImageView) findViewById(R.id.imgVolume);
        this.img_speker_new = (ImageView) findViewById(R.id.img_speker_new);
        this.img_Keypad_new = (ImageView) findViewById(R.id.img_Keypad_new);
        this.imgKeypad = (ImageView) findViewById(R.id.imgKeypad);
        this.imgAdd = (ImageView) findViewById(R.id.imgAdd);
        this.imgHold = (ImageView) findViewById(R.id.imgHold);
        this.imgHangup = (ImageView) findViewById(R.id.imgHangup);
        this.layincoming = (LinearLayout) findViewById(R.id.layincoming);
        this.layoutgoing = (LinearLayout) findViewById(R.id.layoutgoing);
        this.popLay = (RelativeLayout) findViewById(R.id.popLay);
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.tv_title1 = (TextView) findViewById(R.id.tv_title1);
        this.layCancelCall = (LinearLayout) findViewById(R.id.layCancelCall);
        this.btnBack1 = (ImageView) findViewById(R.id.btnBack1);
        this.imgDone = (ImageView) findViewById(R.id.imgDone);
        this.layAns = (LinearLayout) findViewById(R.id.layAns);
        this.popLay.setVisibility(View.GONE);
        Help.getheightandwidth(this);
        Help.setHeight(1080);
        Help.setSize(this.popLay, 1054, 184, true);
        Help.setSize(this.btnBack1, 97, 97, true);
        Help.setSize(this.imgDone, 97, 97, true);
        if (CallManager.callWaiting != null && CallManager.callWaiting.getState() == 3) {
            ((ImageView) findViewById(R.id.btnBack1)).setImageResource(R.drawable.swap_pressed);
        }
        if (isincoming) {
            Log.e("AAA", "UI: Showing Incoming Display");
            this.layincoming.setVisibility(View.VISIBLE);
            this.layoutgoing.setVisibility(View.GONE);
            incomingcreate();
        } else {
            Log.e("AAA", "UI: Showing Outgoing Display");
            this.layincoming.setVisibility(View.GONE);
            this.layoutgoing.setVisibility(View.VISIBLE);
        }
        this.layNumberDial = (LinearLayout) findViewById(R.id.layNumberDial);
        this.edPad = (EditText) findViewById(R.id.edPad);
        this.layDialpad = (LinearLayout) findViewById(R.id.layDialpad);
        this.btn1 = (LinearLayout) findViewById(R.id.btn1);
        this.btn2 = (LinearLayout) findViewById(R.id.btn2);
        this.btn3 = (LinearLayout) findViewById(R.id.btn3);
        this.btn4 = (LinearLayout) findViewById(R.id.btn4);
        this.btn5 = (LinearLayout) findViewById(R.id.btn5);
        this.btn6 = (LinearLayout) findViewById(R.id.btn6);
        this.btn7 = (LinearLayout) findViewById(R.id.btn7);
        this.btn8 = (LinearLayout) findViewById(R.id.btn8);
        this.btn9 = (LinearLayout) findViewById(R.id.btn9);
        this.btn0 = (LinearLayout) findViewById(R.id.btn0);
        this.btnHash = (LinearLayout) findViewById(R.id.btnHash);
        this.btnCancel = (ImageView) findViewById(R.id.btnCancel);
        this.btnCall = (ImageView) findViewById(R.id.btnCall);
        this.btnStar = (LinearLayout) findViewById(R.id.btnStar);
        this.img1 = (ImageView) findViewById(R.id.img1);
        this.img2 = (ImageView) findViewById(R.id.img2);
        this.img3 = (ImageView) findViewById(R.id.img3);
        this.img4 = (ImageView) findViewById(R.id.img4);
        this.img5 = (ImageView) findViewById(R.id.img5);
        this.img6 = (ImageView) findViewById(R.id.img6);
        this.img7 = (ImageView) findViewById(R.id.img7);
        this.img8 = (ImageView) findViewById(R.id.img8);
        this.img9 = (ImageView) findViewById(R.id.img9);
        this.img0 = (ImageView) findViewById(R.id.img0);
        this.img10 = (ImageView) findViewById(R.id.img10);
        this.img11 = (ImageView) findViewById(R.id.img11);
        this.layCancel = (LinearLayout) findViewById(R.id.layCancel);
        this.isHold = false;
        if (Build.VERSION.SDK_INT >= 21) {
            this.edPad.setShowSoftInputOnFocus(false);
        }
        this.powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        try {
            this.field = PowerManager.class.getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable unused) {
        }
        this.wakeLock = this.powerManager.newWakeLock(this.field, getLocalClassName());
        AudioManager audioManager2 = (AudioManager) this.context.getSystemService("audio");
        this.audioManager = audioManager2;
        try {
            audioManager2.setMode(0);
        } catch (Exception e) {
            Log.e(this.TAG, "UI:setmode:= " + e.getMessage());
        }
    }



    private String getTimeFromSeconds(long j) {
        try {
            int i = (int) (j / 3600);
            int i2 = (int) ((j % 3600) / 60);
            int i3 = (int) (j % 60);
            if (i > 0) {
                return String.format("%02d:%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
            }
            return String.format("%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3));
        } catch (Exception unused) {
            return "00:00";
        }
    }


    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        updateCallBackObjject(false);
    }


    public void updateCallBackObjject(Boolean bool) {
        CallManager.sCall.registerCallback(this.mCallback);
        updateUI(CallManager.getState());
    }

    public void onClick(View view) {
        String obj = view.getTag().toString();
        obj.hashCode();
        char c = 65535;
        switch (obj.hashCode()) {
            case -1367724422:
                if (obj.equals("cancel")) {
                    c = 0;
                    break;
                }
                break;
            case 35:
                if (obj.equals("#")) {
                    c = 1;
                    break;
                }
                break;
            case 42:
                if (obj.equals("*")) {
                    c = 2;
                    break;
                }
                break;
            case 48:
                if (obj.equals("0")) {
                    c = 3;
                    break;
                }
                break;
            case 49:
                if (obj.equals("1")) {
                    c = 4;
                    break;
                }
                break;
            case 50:
                if (obj.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
                    c = 5;
                    break;
                }
                break;
            case 51:
                if (obj.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                    c = 6;
                    break;
                }
                break;
            case 52:
                if (obj.equals("4")) {
                    c = 7;
                    break;
                }
                break;
            case 53:
                if (obj.equals("5")) {
                    c = '\b';
                    break;
                }
                break;
            case 54:
                if (obj.equals("6")) {
                    c = '\t';
                    break;
                }
                break;
            case 55:
                if (obj.equals("7")) {
                    c = '\n';
                    break;
                }
                break;
            case 56:
                if (obj.equals("8")) {
                    c = 11;
                    break;
                }
                break;
            case 57:
                if (obj.equals("9")) {
                    c = '\f';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                String obj2 = this.edPad.getText().toString();
                if (obj2.length() > 0) {
                    obj2 = obj2.substring(0, obj2.length() - 1);
                }
                this.edPad.setText(obj2.toString());
                return;
            case 1:
                playMyBit("#");
                this.edPad.setText(this.edPad.getText().toString() + "#");
                return;
            case 2:
                playMyBit("*");
                this.edPad.setText(this.edPad.getText().toString() + "*");
                return;
            case 3:
                playMyBit("0");
                this.edPad.setText(this.edPad.getText().toString() + "0");
                return;
            case 4:
                playMyBit("1");
                this.edPad.setText(this.edPad.getText().toString() + "1");
                return;
            case 5:
                playMyBit(ExifInterface.GPS_MEASUREMENT_2D);
                this.edPad.setText(this.edPad.getText().toString() + ExifInterface.GPS_MEASUREMENT_2D);
                return;
            case 6:
                playMyBit(ExifInterface.GPS_MEASUREMENT_3D);
                this.edPad.setText(this.edPad.getText().toString() + ExifInterface.GPS_MEASUREMENT_3D);
                return;
            case 7:
                playMyBit("4");
                this.edPad.setText(this.edPad.getText().toString() + "4");
                return;
            case '\b':
                playMyBit("5");
                this.edPad.setText(this.edPad.getText().toString() + "5");
                return;
            case '\t':
                playMyBit("6");
                this.edPad.setText(this.edPad.getText().toString() + "6");
                return;
            case '\n':
                playMyBit("7");
                this.edPad.setText(this.edPad.getText().toString() + "7");
                return;
            case 11:
                playMyBit("8");
                this.edPad.setText(this.edPad.getText().toString() + "8");
                return;
            case '\f':
                playMyBit("9");
                this.edPad.setText(this.edPad.getText().toString() + "9");
                return;
            default:
                return;
        }
    }

    private void playMyBit(String str) {
        CallManager.keypad(str.charAt(str.length() - 1));
    }

    public void keypad_click_new(View view) {
        if (this.layDialpad.getVisibility() == 0) {
            this.imgKeypad.setImageResource(R.drawable.keypad_event);
            this.imgMute.setVisibility(View.VISIBLE);
            this.imgSpeaker.setVisibility(View.VISIBLE);
            this.imgHold.setVisibility(View.VISIBLE);
            this.imgKeypad.setVisibility(View.VISIBLE);
            this.img_Keypad_new.setVisibility(View.GONE);
            this.img_speker_new.setVisibility(View.GONE);
            this.layDialpad.setVisibility(View.INVISIBLE);
            Utils.onClickEvent(view);
            return;
        }
        this.imgKeypad.setImageResource(R.drawable.keypad_presss);
        this.imgMute.setVisibility(View.INVISIBLE);
        this.imgSpeaker.setVisibility(View.INVISIBLE);
        this.imgHold.setVisibility(View.INVISIBLE);
        this.imgKeypad.setVisibility(View.INVISIBLE);
        this.img_Keypad_new.setVisibility(View.VISIBLE);
        this.img_speker_new.setVisibility(View.VISIBLE);
        this.layDialpad.setVisibility(View.VISIBLE);
        Utils.onClickEvent(view);
    }

    public void speacker_click_new(View view) {
        speakerProcess();
        Utils.onClickEvent(view);
    }

    public class Callback extends Call.Callback {
        public Callback() {
        }

        public void onStateChanged(Call call, int i) {
            super.onStateChanged(call, i);
            if (i != 7) {
                Activity_Calling_InComing_Outgoing.this.updateUI(i);
            } else if (CallManager.sCall == null) {
                Activity_Calling_InComing_Outgoing.this.dimissScreen();
            } else if (CallManager.callWaiting == null && CallManager.sCall.getState() == 7) {
                Activity_Calling_InComing_Outgoing.this.dimissScreen();
            }
        }

        public void onDetailsChanged(Call call, Call.Details details) {
            super.onDetailsChanged(call, details);
        }
    }



    private void updateUI(int i) {
        Runnable runnable2;
        this.mState = i;
        if (i == 1) {
            this.imgKeypad.setEnabled(false);
            this.imgHold.setEnabled(false);
            this.imgMute.setEnabled(false);
            this.imgKeypad.setSelected(false);
            this.textTiming.setText(R.string.status_call_connexting);
            Log.e("AAA", "update UIr --> STATE_DIALING ");
            Log.e("AAA", "Number --> " + Utils.numberCall);
        } else if (i == 2) {
            Log.e("AAA", "update UI --> STATE_RINGING ");
            this.textTiming.setText(R.string.status_call_incoming);
        } else if (i == 3) {
            this.isfromHold = true;
            this.imgHold.setSelected(true);
            Log.e("AAA", "update UI --> STATE_HOLDING ");
        } else if (i == 4) {
            try {
                if (!this.isfromHold.booleanValue()) {
                    Utils.callStartTime = System.currentTimeMillis();
                    this.isfromHold = false;
                }
                this.isHold = CallManager.sCall.getState() == 3;
            } catch (Exception unused) {
            }
            if (this.isHold) {
                this.imgHold.setSelected(true);
            } else {
                this.imgHold.setEnabled(true);
                this.imgHold.setSelected(false);
            }
            this.imgKeypad.setEnabled(true);
            this.imgMute.setEnabled(true);
            Handler handler2 = this.handler;
            if (handler2 == null || (runnable2 = this.runnable) == null) {
                this.handler = new Handler();
            } else {
                handler2.removeCallbacks(runnable2);
            }
            this.handler.postDelayed(this.runnable, 100);
            Log.e("AAA", "update UI --> STATE_ACTIVE ");
        } else if (i == 7) {
            Handler handler3 = this.handler;
            if (handler3 != null) {
                this.isHold = true;
                handler3.removeCallbacks(this.runnable);
                this.handler = null;
            }
            Utils.callEndTime = this.pauseOffset;
            this.textTiming.setText(R.string.status_call_disconnected);
            Log.e("AAA", "update UI --> STATE_DISCONNECTED ");
        } else if (i != 9) {
            Log.e("AAA", "update UI --> Default Ui Change ");
        } else {
            this.imgKeypad.setEnabled(false);
            this.imgHold.setEnabled(false);
            this.imgMute.setEnabled(false);
            destroyCallHead(true);
            onReturnToCall();
            Log.e("AAA", "update UI --> STATE_CONNECTING ");
            this.textTiming.setText(R.string.status_call_connexting);
        }
        if (i == 7 && !this.userEndCall) {
            endCallProcess();
        }
    }

    private void switchToCallingUI() {
        this.audioManager.setMode(2);
        acquireWakeLock();
        Handler handler2 = new Handler();
        this.handler = handler2;
        handler2.postDelayed(this.runnable, 100);
    }

    private void endCall() {
        CallManager.reject(CallManager.sCall);
        if (CallManager.callWaiting == null) {
            dimissScreen();
        } else if (CallManager.callWaiting.getState() == 7) {
            dimissScreen();
        }
    }


    public void dimissScreen() {
        Handler handler2 = this.handler;
        if (handler2 != null) {
            handler2.removeCallbacks(this.runnable);
        }
        releaseWakeLock();
        cleanCallHead();
        if (CallManager.sCall != null) {
            CallManager.sCall.unregisterCallback(this.mCallback);
        }
        closeNow();
    }

    private void acquireWakeLock() {
        if (!this.wakeLock.isHeld()) {
            this.wakeLock.acquire(600000);
        }
    }

    private void releaseWakeLock() {
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
    }


    public void onBackPressed() {
        if (this.layDialpad.getVisibility() == 0) {
            this.layDialpad.setVisibility(View.GONE);
            this.imgKeypad.setImageResource(R.drawable.keypad_disable);
            this.imgMute.setVisibility(View.VISIBLE);
            this.imgSpeaker.setVisibility(View.VISIBLE);
            this.imgHold.setVisibility(View.VISIBLE);
        }
    }

    public int capturedSimSlot(Bundle bundle) {
        int i = bundle.containsKey("subscription") ? bundle.getInt("subscription") : -1;
        if (i >= 0 && i < 5) {
            return i;
        }
        if (bundle.containsKey("simId")) {
            return bundle.getInt("simId");
        }
        if (bundle.containsKey("com.android.phone.extra.slot")) {
            return bundle.getInt("com.android.phone.extra.slot");
        }
        String str = "";
        for (String str2 : bundle.keySet()) {
            if (str2.contains("sim")) {
                str = str2;
            }
        }
        return bundle.containsKey(str) ? bundle.getInt(str) : i;
    }


    public void incomingcreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionCallAnswerStop);
        intentFilter.addAction(ACTIONC_CHANGE);
        registerReceiver(this.receiver1, intentFilter);
        this.startTime = System.currentTimeMillis();
        this.inComingCallPresenter = new Call_Detector(this);
        ui1();
        resize1();
        click();
        sticky();
        YoYo.with(Techniques.Bounce).duration(700).repeat(-1).playOn(this.layAns1);
        YoYo.with(Techniques.Bounce).duration(500).repeat(-1).playOn(this.layHangUp1);
    }

    private void click() {
        this.layAns1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("com.hello.action");
                Activity_Calling_InComing_Outgoing.this.sendBroadcast(intent);
                Activity_Calling_InComing_Outgoing.this.answerCall();
            }
        });
        this.layHangUp1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Activity_Calling_InComing_Outgoing.this.endCallinc();
            }
        });
    }

    private void dismissPage() {
        unregisterReceiver(this.receiver1);
        finish();
    }

    private void ui1() {
        this.imgvideo = (VideoView) findViewById(R.id.videoView);
        this.imgHangUp1 = (ImageView) findViewById(R.id.imgHangUp1);
        this.imgAns1 = (ImageView) findViewById(R.id.imgAns1);
        this.textViewCallerName1 = (TextView) findViewById(R.id.textViewCallerName1);
        this.textViewNumber1 = (TextView) findViewById(R.id.textViewNumber1);
        this.imageViewProfile1 = (CircleImageView) findViewById(R.id.imageViewProfile1);
        this.imgProfile1 = (ImageView) findViewById(R.id.imgProfile1);
        this.layHangUp1 = (RelativeLayout) findViewById(R.id.layHangUp1);
        this.layAns1 = (RelativeLayout) findViewById(R.id.layAns1);
        this.layPRof1 = (RelativeLayout) findViewById(R.id.layPRof1);
    }

    private void resize1() {
        Help.getheightandwidth(this);
        Help.setSize(this.imgHangUp1, 210, 210, true);
        Help.setSize(this.imgAns1, 210, 210, true);
        Help.setSize(this.layPRof1, 300, 300, true);
        Help.setSize(this.imageViewProfile1, 300, 300, true);
        Help.setSize(this.imgProfile1, 300, 300, true);
    }

    public void turnOn() {
        if (Build.VERSION.SDK_INT <= 23) {
            Camera camera = this.mCamera;
            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                this.mParams = parameters;
                parameters.setFlashMode("torch");
                this.mCamera.setParameters(this.mParams);
                this.on = true;
                return;
            }
            return;
        }
        try {
            this.cameraId = CallManager.camManager.getCameraIdList()[0];
            CallManager.camManager.setTorchMode(this.cameraId, true);
            this.on = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e("AAA", " falsh error on" + e.toString());
        }
    }

    public void turnOff() {
        if (Build.VERSION.SDK_INT <= 23) {
            Camera camera = this.mCamera;
            if (camera != null && this.mParams != null) {
                if (camera != null) {
                    Camera.Parameters parameters = camera.getParameters();
                    this.mParams = parameters;
                    if (parameters.getFlashMode().equals("torch")) {
                        this.mParams.setFlashMode("off");
                        this.mCamera.setParameters(this.mParams);
                    }
                }
                this.on = false;
                return;
            }
            return;
        }
        try {
            if (CallManager.camManager != null) {
                try {
                    CallManager.camManager.setTorchMode(this.cameraId, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                this.on = false;
            }
        } catch (Exception e2) {
            Log.e("AAA", "flash off error   " + e2.toString());
        }
    }

    public void toggleFlashLight() {
        try {
            if (!this.on) {
                turnOn();
            } else {
                turnOff();
            }
        } catch (Exception e) {
            Log.e("flashing", "" + e.toString());
        }
    }

    private Thread blinkThread() {
        Thread r0 = new Thread() {

            public void run() {
                try {
                    Activity_Calling_InComing_Outgoing.this.initializeCamera();
                    while (!Activity_Calling_InComing_Outgoing.this.isFinishing() && !Activity_Calling_InComing_Outgoing.this.thread.isInterrupted()) {
                        Activity_Calling_InComing_Outgoing.this.toggleFlashLight();
                        sleep(50);
                    }
                    Activity_Calling_InComing_Outgoing.this.turnOff();
                    if (Build.VERSION.SDK_INT <= 23 && Activity_Calling_InComing_Outgoing.this.mCamera != null) {
                        Activity_Calling_InComing_Outgoing.this.mCamera.stopPreview();
                        Activity_Calling_InComing_Outgoing.this.mCamera.release();
                        Activity_Calling_InComing_Outgoing.this.mCamera = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.thread = r0;
        return r0;
    }



    private void initializeCamera() {
        if (Build.VERSION.SDK_INT > 23) {
            CallManager.camManager = (CameraManager) getSystemService("camera");
            this.cameraId = null;
            try {
                this.cameraId = CallManager.camManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else if (this.mCamera == null) {
            Camera open = Camera.open();
            this.mCamera = open;
            try {
                open.setPreviewDisplay(null);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.mCamera.startPreview();
        }
    }


    public void sticky() {
        try {
            this.number = getIntent().getStringExtra(PhoneCallReceiver.NUMBER_STATE);
        } catch (Exception e) {
            e.getMessage();
        }
        this.textViewNumber1.setText(this.number);
        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", 0);
        this.sharedpreferences = sharedPreferences;
        if (sharedPreferences.getBoolean("flashvalue", false)) {
            Thread blinkThread = blinkThread();
            this.thread = blinkThread;
            blinkThread.start();
        }
        String contactNamebytherenumber = ColorCallUtils.getContactNamebytherenumber(this, this.number);
        if (contactNamebytherenumber == null || contactNamebytherenumber.equals("")) {
            this.textViewCallerName1.setText("Unkown Number");
        } else {
            this.textViewCallerName1.setText(contactNamebytherenumber);
        }
        Bitmap retrieveContactPhoto = Utils.retrieveContactPhoto(this, this.number);
        if (retrieveContactPhoto != null) {
            this.imgProfile1.setVisibility(View.GONE);
            this.imageViewProfile1.setVisibility(View.VISIBLE);
            this.imageViewProfile1.setImageBitmap(retrieveContactPhoto);
        } else {
            this.imgProfile1.setVisibility(View.GONE);
            this.imageViewProfile1.setVisibility(View.VISIBLE);
            this.imageViewProfile1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.avtar));
        }
        SQLiteDatabase readableDatabase = new SQLiteHelperClass(this).getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT NUMBER,THEMEVALUE  FROM contect_theme", new String[0]);
        int i = 3;
        if (rawQuery == null || !rawQuery.moveToFirst()) {
            this.theam = this.sharedpreferences.getString("theamvideo", "android.resource://" + getPackageName() + "/" + R.raw.asset_theme1);
        } else {
            while (true) {
                Log.d(this.TAG, "onCreateView:2 " + this.number.length());
                int length = this.number.length();
                int length2 = rawQuery.getString(0).length();
                if (length == length2) {
                    if (this.number.equals(rawQuery.getString(0))) {
                        this.theam = rawQuery.getString(1);
                    }
                } else if (length2 == 10) {
                    String substring = this.number.substring(i, length);
                    Log.d(this.TAG, "onCreateView:3 " + this.number.length());
                    if (substring.equals(rawQuery.getString(0))) {
                        this.theam = rawQuery.getString(1);
                    }
                }
                if (!rawQuery.moveToNext()) {
                    break;
                }
                i = 3;
            }
            rawQuery.close();
        }
        Log.d(this.TAG, "onStartCommand:theam" + this.theam);
        if (this.theam == null) {
            this.theam = this.sharedpreferences.getString("theamvideo", "android.resource://" + getPackageName() + "/" + R.raw.asset_theme1);
        }
        if (!this.theam.equals("android.resource://" + getPackageName() + "/" + R.raw.asset_theme1)) {
            Log.e(this.TAG, "onCreate:callingnumber " + this.number);
        } else {
            Log.e(this.TAG, "onCreate:asset asset");
        }
        this.imgvideo.setVideoURI(Uri.parse(this.theam));
        this.imgvideo.start();
        this.imgvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0.0f, 0.0f);
            }
        });
        this.imgvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mediaPlayer) {
                Activity_Calling_InComing_Outgoing.this.imgvideo.start();
            }
        });
        Cursor rawQuery2 = readableDatabase.rawQuery("SELECT NUMBER,RECEVIEICON,REJECTICON   FROM calling_icon", new String[0]);
        if (rawQuery2 == null || !rawQuery2.moveToFirst()) {
            this.ans = this.sharedpreferences.getInt(ansvalue, R.drawable.accept_button_three);
            this.rej = this.sharedpreferences.getInt(rejvalue, R.drawable.decline_button_three);
        } else {
            do {
                Log.d(this.TAG, "onCreateView:1 " + rawQuery2.getString(0));
                Log.d(this.TAG, "onCreateView:2 " + rawQuery2.getString(1));
                Log.d(this.TAG, "onCreateView:2 " + rawQuery2.getString(2));
                Log.d(this.TAG, "onStartCommand: " + this.number);
                int length3 = this.number.length();
                int length4 = rawQuery2.getString(0).length();
                if (length3 == length4) {
                    if (this.number.equals(rawQuery2.getString(0))) {
                        this.ans = rawQuery2.getInt(1);
                        this.rej = rawQuery2.getInt(2);
                    }
                } else if (length4 == 10) {
                    String substring2 = this.number.substring(3, length3);
                    Log.d(this.TAG, "onCreateView:3 " + this.number.length());
                    if (substring2.equals(rawQuery2.getString(0))) {
                        this.ans = rawQuery2.getInt(1);
                        this.rej = rawQuery2.getInt(2);
                    }
                }
            } while (rawQuery2.moveToNext());
            rawQuery2.close();
        }
        if (this.ans == 0 && this.rej == 0) {
            this.ans = this.sharedpreferences.getInt(ansvalue, R.drawable.accept_button_three);
            this.rej = this.sharedpreferences.getInt(rejvalue, R.drawable.decline_button_three);
        }
        Log.e(this.TAG, "onCreate:theam" + this.ans);
        Log.e(this.TAG, "onCreate:theam" + this.rej);
        this.imgHangUp1.setImageResource(this.rej);
        this.imgAns1.setImageResource(this.ans);
    }

    public void answerCall() {
        CallManager.answer();
    }

    public void endCallinc() {
        dismissPage();
        try {
            CallManager.reject();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            if (CallManager.sCall != null) {
                CallManager.sCall.reject(false, "");
            }
        }
    }


    public void incommingDestroy() {
        unregisterReceiver(this.receiver1);
        Thread thread2 = this.thread;
        if (thread2 != null && thread2.isAlive()) {
            this.thread.interrupt();
            this.thread = null;
        }
        turnOff();
        Log.e("SSS", "destory incomming theme");
    }

    public void startProximitySensor() {
        try {
            PowerManager powerManager2 = (PowerManager) this.cn.getSystemService(POWER_SERVICE);
            if (this.mProximityWakeLock == null && powerManager2 != null) {
                this.mProximityWakeLock = powerManager2.newWakeLock(32, "color:Salut_ddd");
            }
            if (!this.mProximityWakeLock.isHeld()) {
                this.mProximityWakeLock.acquire();
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    public void removeProxmitySensor() {
        try {
            PowerManager.WakeLock wakeLock2 = this.mProximityWakeLock;
            if (wakeLock2 != null) {
                if (wakeLock2.isHeld()) {
                    this.mProximityWakeLock.release();
                }
                this.mProximityWakeLock = null;
            }
        } catch (Exception unused) {
        }
    }
}
