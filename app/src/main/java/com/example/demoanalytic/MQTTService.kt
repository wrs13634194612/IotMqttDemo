package com.example.demoanalytic


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.ServiceUtils
import com.blankj.utilcode.util.TimeUtils
import com.rairmmd.andmqtt.*
import org.eclipse.paho.android.service.MqttTraceHandler
import org.eclipse.paho.client.mqttv3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MQTTService : Service() {
    private var mqttMsg: MqttMsg? = null
    private var isStop = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "faceId",
                    "mqtt",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
            val builder = NotificationCompat.Builder(this, "faceId")
            startForeground(1, builder.build())
        }
        EventBus.getDefault().register(this)
        if (UserZone.isLogin(this)) {
            Log.e("MQTTService", "onCreate 开始连接")
            initMqtt2()
        }
    }


    private fun initMqtt2() {
        AndMqtt.getInstance().init(applicationContext)
        AndMqtt.getInstance().connect(
            MqttConnect().setClientId(UserZone.getClientId(this))
                .setPort(MQTT_PORT).setAutoReconnect(true)
                .setCleanSession(true).setServer(MQTT_SERVICE)
                .setTraceCallback(object : MqttTraceHandler {
                    override fun traceDebug(tag: String, message: String) {
                        Log.e("MQTTService", "调试信息：$tag,$message")
                    }

                    override fun traceError(tag: String, message: String) {
                        Log.e("MQTTService", "错误信息：$tag,$message")
                    }

                    override fun traceException(
                        tag: String,
                        message: String,
                        e: java.lang.Exception
                    ) {
                        Log.e("MQTTService", "异常信息：$tag,$message,$e")
                    }
                }).setMessageListener(object : MqttCallbackExtended {
                    override fun connectComplete(
                        reconnect: Boolean,
                        serverURI: String
                    ) {
                        //reconnect 如果为真，则连接是自动重新连接的结果
                        Log.e("MQTTService", "连接完成：$reconnect,$serverURI")
                    }

                    override fun connectionLost(cause: Throwable) {
                        Log.e("MQTTService", "连接丢失：$cause")
                    }

                    @Throws(java.lang.Exception::class)
                    override fun messageArrived(
                        topic: String,
                        message: MqttMessage
                    ) {
                        Log.e("MQTTService", "接收到服务端的数据：$topic,$message")
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken) {
                        Log.e("MQTTService", "当消息的传递完成时调用：$token")

                    }
                }), object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.e("Rair", "(MainActivity.java:51)-onSuccess:->连接成功")
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    Log.e("Rair", "(MainActivity.java:56)-onFailure:->连接失败")
                }
            })
    }



    private fun writeLog(log: String) {
        // FileUtils.writeFileFromString("/sdcard/mindor.txt", log, true)
    }

    private fun publish(msg: String, topic: String) {
        if (!AndMqtt.getInstance().isConnect) {
            // Toaster.show("设备连接异常,请重启应用或稍后再试")
            Log.e("MQTTService", "publish 开始连接")
            initMqtt2()
            return
        }
        //发布
        AndMqtt.getInstance().publish(MqttPublish()
            .setMsg(msg)
            .setQos(0)
            .setTopic(topic), object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.e("publish onSuccess", topic + "发布成功" + msg + asyncActionToken.isComplete)
                val log = TimeUtils.getNowString() + "发送成功-->红外主题${topic}---消息${msg}\n\n"
                writeLog(log)
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.e("publish onSuccess", "发布失败onFailure")
            }
        })

    }

    private fun subscribe(topic: String) {
        if (!AndMqtt.getInstance().isConnect) {
            //Toaster.show("设备连接异常,请重启应用或稍后再试")
            Log.e("MQTTService", "subscribe 开始连接")
            initMqtt2()
            return
        }
        //订阅
        AndMqtt.getInstance().subscribe(MqttSubscribe()
            .setTopic(topic)
            .setQos(0), object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.e("MQTTService", "(MainActivity.java:63)-onSuccess:->订阅成功")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.e("MQTTService", "(MainActivity.java:68)-onFailure:->订阅失败")
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun publishMsg(mqtt: MqttBean) {
        Log.e("MQTTService", "发送指令publishMsg:${mqtt.topic}")
        if (mqtt.register) {
            subscribe(mqtt.topic)
            return
        }
        if (mqtt.isUnregister) {
            unSubscribe(mqtt.topic)
            return
        }
        publish(mqtt.message, mqtt.topic)
    }


    private fun unSubscribe(topic: String?) {
        try {
            if (!AndMqtt.getInstance().isConnect) {
                return
            }
            if (TextUtils.isEmpty(topic)) return
            AndMqtt.getInstance().unSubscribe(MqttUnSubscribe()
                .setTopic(topic), object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.e("MQTTService", topic + "(MainActivity.java:93)-onSuccess:->取消订阅成功")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.e("MQTTService", topic + "(MainActivity.java:98)-onFailure:->取消订阅失败")
                }
            })
        } catch (e: Exception) {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe()
    fun stopService(stopService: StopService) {
        this.isStop = stopService.isStop
    }

    @Subscribe
    fun loginSuccess(userInfo: UserInfo) {
        Log.e("MQTTService", "loginSuccess 开始连接")
        initMqtt2()
    }
}
