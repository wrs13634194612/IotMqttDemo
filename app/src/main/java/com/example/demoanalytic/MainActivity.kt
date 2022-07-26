package com.example.demoanalytic

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ServiceUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    private val productId = "zcz004"
    private val equipmentId = "zcz004100629"
    // https://github.com/Rairmmd/AndMqtt.git 参考网址，需要lib的自行去下载

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        EventBus.getDefault().postSticky(MqttMsg(true))
        if (ServiceUtils.isServiceRunning(MQTTService::class.java.name)) {
            ServiceUtils.stopService(MQTTService::class.java.name)
        }
        val intent = Intent(this@MainActivity, MQTTService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this@MainActivity.startForegroundService(intent)
        } else {
            startService(intent)
        }

        btn_pub_d.setOnClickListener {
            EventBus.getDefault().post(
                MqttBean(
                    String.format(
                        PLUG_TOPIC,
                        productId,
                        equipmentId
                    ), true
                )
            )
        }


        btn_open.setOnClickListener {
            EventBus.getDefault().post(
                MqttBean(
                    String.format(PLUG_TOPIC_REC, productId, equipmentId),
                    String.format(SET_SWITCH, "1")
                )
            )
        }

        btn_close.setOnClickListener {
            EventBus.getDefault().post(
                MqttBean(
                    String.format(PLUG_TOPIC_REC, productId, equipmentId),
                    String.format(SET_SWITCH, "0")
                )
            )
        }


        btn_smart_sure.setOnClickListener {


            /*
            * 连接肯定是正常的   我是不是没订阅d 端的数据  所以收不到呢  那么问题来了  应该怎么订阅呢
            * */



            EventBus.getDefault().post(
                MqttBean(
                    String.format(
                        PLUG_TOPIC_REC,
                        productId,
                        equipmentId
                    ), SET_IR_SMART
                )
            )
        }

        btn_smart_cancel.setOnClickListener {
            EventBus.getDefault().post(
                MqttBean(
                    String.format(
                        PLUG_TOPIC_REC,
                        productId,
                        equipmentId
                    ), SET_IR_SMART_END
                )
            )
            EventBus.getDefault().post(StopService(true))
            EventBus.getDefault().post(
                MqttBean(
                    String.format(
                        PLUG_TOPIC,
                        productId,
                        equipmentId
                    ), "", true
                )
            )
        }

    }

    override fun onDestroy() {
        try {
            Log.e("TAG", "onDestroy: " + ServiceUtils.stopService(MQTTService::class.java.name))
            Log.e(
                "TAG",
                "onDestroy: " + ServiceUtils.stopService("org.eclipse.paho.android.service.MqttService")
            )
        } catch (e: java.lang.Exception) {
            e.toString()
        }
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showMsgDialog(showMsg: ShowMsg) {

    }
}
