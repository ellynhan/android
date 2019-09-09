package com.example.memo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class actSplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.act_splash_screen)

        supportActionBar!!.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.act_splash_screen)
        createShortCut()
        Handler().postDelayed({
            startActivity(Intent(this,actMain::class.java))
            finish()},3000)
    }
    fun createShortCut(){
        val sp=getSharedPreferences("isAppInstalled", Context.MODE_PRIVATE)
        val varsp=sp.getBoolean("state",false)
        if(varsp === false){
            val shortcutIntent=Intent(applicationContext,actSplashScreen::class.java)
            shortcutIntent.action=Intent.ACTION_MAIN
            val intent = Intent()
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortcutIntent)
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,getString(R.string.app_name))
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(applicationContext,R.mipmap.ic_launcher_round))
            intent.action="com.android.launcher.action.INSTALL_SHORTCUT"
            applicationContext.sendBroadcast(intent)
            val editor=sp!!.edit()
            editor.putBoolean("state",true)
            editor.commit()
        }

    }

}
