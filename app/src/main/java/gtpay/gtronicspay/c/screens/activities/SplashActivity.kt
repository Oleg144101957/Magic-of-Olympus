package gtpay.gtronicspay.c.screens.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.ActivitySplashBinding
import info.guardianproject.f5android.plugins.PluginNotificationListener
import info.guardianproject.f5android.plugins.f5.Extract
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(){


    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("123123", "onCreate SplashActivity")
        binding = ActivitySplashBinding.inflate(layoutInflater).also { setContentView(it.root) }
        configureWindow()

//
//        val file = assets.open("encoded_image.png")
//        val pass = "hello_world"
//        val extractor = Extract(this, file, pass.toByteArray())
//        extractor.start()

    }

    private fun configureWindow(){
        window.statusBarColor = getColor(R.color.black)
    }
}