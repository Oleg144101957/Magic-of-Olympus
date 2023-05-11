package gtpay.gtronicspay.c.screens.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also { setContentView(it.root) }
        configureWindow()
    }

    private fun configureWindow(){
        window.statusBarColor = getColor(R.color.black)
    }
}