package com.example.listacomponenteqr.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefence(val context : Context?) {

    private val PREFSH = "AlmacenEspaña"
    val sharedPref : SharedPreferences = context!!.getSharedPreferences(PREFSH, Context.MODE_PRIVATE)
    val editor : SharedPreferences.Editor = sharedPref.edit()

    val PREF_LOGIN_INICIO = "login_inicio_share"
    val PREF_LOGIN_USER = "login_user_share"
    val PREF_LOGIN_PASS = "login_pass_share"
    /**USER INFO LOGIN**/
    val PREF_USUID =        "user_id"
    val PREF_USU =          "user"
    val PREF_SALA =         "sala"
    val PREF_DEPARTAMENT =  "departament"
    val PREF_SUB_CENTRO =   "sub_centro"
    val PREF_VERSION =      "version"
    val PREF_APPNAME =      "app_name"
    val PREF_TIPO_ACCESO =  "tipo_acceso"

    fun saveinicio(inicio:Boolean?){
        if (inicio!!){
            editor.putString(PREF_LOGIN_INICIO, "true")
        }else{
            editor.putString(PREF_LOGIN_INICIO, "false")
        }
        editor.commit()
    }

    fun saveUserInfo(user: String, pwd: String, userID: Int, nombre: String, sala: String?){
        editor.putString(PREF_LOGIN_USER, user)
        editor.putString(PREF_LOGIN_PASS, pwd )
        editor.putInt(PREF_USUID, userID )
        editor.putString(PREF_USU, nombre)
        editor.putString(PREF_SALA,sala)
        editor.commit()
    }

    fun clearSharedPreference(){
        editor.clear()
        editor.commit()
    }

    fun getInicioLogin(): String? {return sharedPref.getString(PREF_LOGIN_INICIO,"false")}

    fun getUserLogin() : String?{ return sharedPref.getString(PREF_LOGIN_USER, "")}

    fun getPassLogin() : String?{ return sharedPref.getString(PREF_LOGIN_PASS, null)}

    fun getUsu(): String?{ return sharedPref.getString(PREF_USU, null)}

    fun getUsuID(): Int{ return sharedPref.getInt(PREF_USUID, 0)}

    fun getSala(): String? { return sharedPref.getString(PREF_SALA,null)}

    fun getDepartament() : String?{ return sharedPref.getString(PREF_DEPARTAMENT, null)}

    fun getSubCentro(): Int?{ return sharedPref.getInt(PREF_SUB_CENTRO, 0)}

    fun getVersion(): String?{ return sharedPref.getString(PREF_VERSION, null)}

    fun getAppName(): String?{ return sharedPref.getString(PREF_APPNAME, null)}

    fun getTipoAcesso(): String?{ return sharedPref.getString(PREF_TIPO_ACCESO, null)}

}