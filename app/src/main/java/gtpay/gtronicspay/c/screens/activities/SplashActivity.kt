package gtpay.gtronicspay.c.screens.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import gtpay.gtronicspay.c.Const
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.ActivitySplashBinding
import info.guardianproject.f5android.plugins.PluginNotificationListener
import info.guardianproject.f5android.plugins.f5.Extract
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(), Extract.ExtractionListener,
    PluginNotificationListener {

    private lateinit var olympusPref: SharedPreferences
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        olympusPref = getSharedPreferences(Const.OLYMPUS_PREF, Context.MODE_PRIVATE)
        binding = ActivitySplashBinding.inflate(layoutInflater).also { setContentView(it.root) }
        configureWindow()
        configOlympusKey()
    }

    private fun configureWindow(){
        window.statusBarColor = getColor(R.color.black)
    }

    private fun configOlympusKey(){
        val olympusKey = olympusPref.getInt(Const.OLYMPUS_KEY, 0)
        if (olympusKey == 0){
            val file = assets.open("image.png")
            val pass = "hello_world"
            val extractor = Extract(this, file, pass.toByteArray())
            extractor.start()
        }
    }
    override fun onExtractionResult(baos: ByteArrayOutputStream?) {
        val str = String(baos!!.toByteArray(), Charsets.UTF_8)
        val pair = Pair(str.substringBefore("|||"), str.substringAfter("|||"))
        olympusPref.edit().putInt(Const.OLYMPUS_KEY, pair.second.toInt()).apply()
    }

    override fun onUpdate(with_message: String?) {
    }

    override fun onFailure() {
    }
}