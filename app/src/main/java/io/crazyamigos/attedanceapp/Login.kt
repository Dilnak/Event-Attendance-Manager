package io.crazyamigos.attedanceapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun doLogin(view:View){
        if(userName.text.toString().isEmpty()||password.text.toString().isEmpty())
            toast("Invalid username or password")
        else{
            login()

        }

    }
    fun login(){
        progress.visibility=View.VISIBLE
        doAsync {
            val body = FormBody.Builder()
                    .add("username", userName.text.toString())
                    .add("password", password.text.toString())
                    .build()
            val request = Request.Builder()
                    .url("https://test3.htycoons.in/api/login")
                    .post(body)
                    .build()
            val client = OkHttpClient()
            val response = client.newCall(request).execute()
            uiThread {
               when(response.code()){
             200->{
                 if(response.body()!=null) {
                     val jsonResponse = JSONObject(response.body()!!.string())
                     val accessToken=jsonResponse.getString("access_token")
                     Log.d("ACCESS",accessToken)
                     val pref=getSharedPreferences("event",0)
                     val editor=pref.edit()
                     editor.putString("access_tocken",accessToken)
                     editor.apply()
                     startActivity(intentFor<Events>())
                     finish()

                 }
             }
                   400->{}
                   404->{}
               }
            }
        }
    }
}
