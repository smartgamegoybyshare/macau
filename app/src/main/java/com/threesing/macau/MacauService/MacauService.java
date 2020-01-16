package com.threesing.macau.MacauService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.threesing.macau.Activity.MainActivity_fix;
import com.threesing.macau.R;
import com.threesing.macau.Support.StringRandom;
import com.threesing.macau.Support.Value;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static java.lang.Thread.sleep;

public class MacauService extends Service {

    private static String TAG = "MacauService";
    private LocalBinder binder = new LocalBinder();
    private MqttConnectOptions connectOptions;
    private String broker = "tcp://210.244.56.216:1883";
    private String clientId;   //唯一字串
    private MemoryPersistence persistence;
    private MqttAndroidClient androidClient;
    private String username = "mqtt", userpassword = "123qwe.com";
    private final int PID = android.os.Process.myPid();
    private boolean connectflag = false;

    public class LocalBinder extends Binder {
        // 声明一个方法，getService。（提供给客户端调用）
        public MacauService getService() {
            // 返回当前对象LocalService,这样我们就可在客户端端调用Service的公共方法了
            return MacauService.this;
        }
    }

    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "IBinder onBind");
        return binder;
    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
        setstartForeground();
        setMQTTnofication();
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        connectivityManager.requestNetwork(new NetworkRequest.Builder().build(),
                new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        handler.sendEmptyMessage(1);
                    }
                });
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        Log.e(TAG, "onStartCommand = " + flags);
        return START_STICKY;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            switch (i) {
                case 1:
                    Log.e(TAG, "網路斷開");
                    connectflag = true;
                    break;
                case 0:
                    Log.e(TAG, "網路已連線");
                    try {
                        if(connectflag) {
                            androidClient.isConnected();
                            sleep(100);
                            setMQTTnofication();
                            connectflag = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void setstartForeground() {
        String textmessage = null;
        if (Value.language_flag == 0) {  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
            textmessage = "3singmacau is availd";
        } else if (Value.language_flag == 1) {
            textmessage = "三昇澳門正在執行";
        } else if (Value.language_flag == 2) {
            textmessage = "三昇澳门正在运行";
        }
        String id = "my_channel";   // 通知渠道的id
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_MIN;
            assert mNotificationManager != null;
            NotificationChannel mChannel = mNotificationManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, "三昇澳門服務", importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);
            intent = new Intent(this, MainActivity_fix.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle("三昇澳門")                            // required
                    .setSmallIcon(R.drawable.appicon_notification)   // required
                    .setContentText(textmessage) // required
                    .setDefaults(Notification.FLAG_FOREGROUND_SERVICE)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(textmessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(this, id);
            intent = new Intent(this, MainActivity_fix.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle("三昇澳門")                            // required
                    .setSmallIcon(R.drawable.appicon_notification)   // required
                    .setContentText(textmessage) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(textmessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_MIN);
        }
        startForeground(PID, builder.build());
    }

    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        //MacauService2.enqueueWork(this, new Intent());
        androidClient.unregisterResources();
        androidClient.close();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(new Intent(this, MacauService.class));
        } else {
            this.startService(new Intent(this, MacauService.class));
        }
    }

    /**
     * 解除绑定时调用
     *
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "Service is invoke onUnbind");
        return super.onUnbind(intent);
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public void setMQTTnofication() {
        connectOptions = new MqttConnectOptions();   //MQTT的Broker連線相關設定都是透過MqttConnectOptions設定
        connectOptions.setCleanSession(true);   //設定為true，表示當連線中斷後，mqtt paho client 會嘗試重新連線，false則否
        connectOptions.setUserName(username);
        char[] password = userpassword.toCharArray();
        connectOptions.setPassword(password);
        persistence = new MemoryPersistence();
        //MemoryPersistence物件用來在App如果被清除或終止時，可以存下重要資料，這裡並不需要特別處理他，只需new即可
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        assert TelephonyMgr != null;
        clientId = TelephonyMgr.getDeviceId();
        Log.e(TAG, "clientId = " + clientId);
        androidClient = new MqttAndroidClient(this, broker, clientId, persistence);
        MqttCallback mqttCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                Log.e(TAG, "已斷線");
                connectflag = true;
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                Log.e(TAG, "收到訊息");
                Log.e(TAG, "mqttMessage = " + mqttMessage.toString());
                sendNotification(getApplicationContext(), "三昇澳門", mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.e(TAG, "發送成功");
            }
        };
        androidClient.setCallback(mqttCallback);
        try {
            androidClient.connect(connectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.e(TAG, "連線成功");
                    try {
                        androidClient.subscribe("三昇澳門", 2);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e(TAG, "連線失敗");
                    Log.e(TAG, "clientId = " + clientId);
                    Log.e(TAG, "username = " + username);
                    Log.e(TAG, "userpassword = " + userpassword);
                    Log.e(TAG, "iMqttToken = " + iMqttToken);
                    Log.e(TAG, "Message = " + throwable.getMessage());
                    Log.e(TAG, "Cause = " + throwable.getCause());
                    Log.e(TAG, "throwable = " + throwable);
                    try {
                        androidClient.disconnect();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "異常");
            e.printStackTrace();
        }
    }

    public static void sendLocalNotification(Context context, int notifyID,
                                             int drawableSmallIcon, String title, String msg,
                                             boolean autoCancel, PendingIntent pendingIntent) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context, notifyID + "").setSmallIcon(drawableSmallIcon).setContentTitle(title)
                .setContentText(msg).setAutoCancel(autoCancel)
                .setDefaults(Notification.DEFAULT_ALL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    notifyID + "",
                    title,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyID, mBuilder.build());
    }

    public static void sendNotification(Context context, String messageTitle, String messageBody) {
        /*Intent intent = new Intent(context, MainActivity_fix.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        sendLocalNotification(context, 1, R.drawable.appicon_notification, messageTitle,
                messageBody, true, pendingIntent);*/
        final int NOTIFY_ID = 1; // ID of notification
        String channelId = "macaunotification";
        //String channelId = new StringRandom().getStringRandom(5);    // default_channel_id
        Log.e(TAG, "channelId = " + channelId);
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        NotificationManager notifManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notifManager = context.getSystemService(NotificationManager.class);
        } else {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bitmap rawBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon);
        builder = new NotificationCompat.Builder(context, channelId)
                //channelId => NotificationCompat.Builder與NotificationChannel 務必一致
                .setContentTitle(messageTitle)// required
                .setSmallIcon(R.drawable.appicon_notification)   // required
                .setLargeIcon(rawBitmap)
                .setContentText(messageBody) // required
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);
        assert notifManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, messageTitle, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("");
            mChannel.enableVibration(true);
            mChannel.enableLights(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notifManager.createNotificationChannel(mChannel);
            intent = new Intent(context, MainActivity_fix.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent);
        } else {
            intent = new Intent(context, MainActivity_fix.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle(messageTitle)  // required
                    .setSmallIcon(R.drawable.appicon_notification)   // required
                    .setLargeIcon(rawBitmap)
                    .setContentText(messageBody) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(messageBody)
                    .setWhen(System.currentTimeMillis())
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }
        notifManager.notify(NOTIFY_ID, builder.build());
        Log.e(TAG, "顯示通知");
    }
}
