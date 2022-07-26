package com.example.demoanalytic

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson


object UserZone {

    /**
     *是否已登录
     * */
    fun isLogin(context: Context): Boolean {
        return true
    }

    /**
     *获取当前登录的用户id
     * */
    fun getClientId(context: Context): String {
        /*return if (TextUtils.isEmpty(clientId)) {
            userId
        } else {
            clientId!!
        }*/
        return "minApp125106"
    }
}