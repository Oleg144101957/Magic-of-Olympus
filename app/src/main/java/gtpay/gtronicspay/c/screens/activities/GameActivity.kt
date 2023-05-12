package gtpay.gtronicspay.c.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.ActivityGameBinding
import gtpay.gtronicspay.linksaver.data.MagicDB
import gtpay.gtronicspay.linksaver.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater).also { setContentView(it.root) }
        configureWindow()
        checkStatus()

        binding.x200.setOnClickListener {
            val intent = Intent(this, X200activity::class.java)
            startActivity(intent)
        }
    }

    private fun configureWindow(){
        window.statusBarColor = getColor(R.color.black)
    }

    private fun checkStatus(){
        lifecycleScope.launch(Dispatchers.IO) {
            val magicDao = MagicDB.getMagicDatabase(this@GameActivity).getGameDao()
            val repo = Repository(magicDao)
            val listMagicModels = repo.readAllData()
            val adbStatus = listMagicModels[0].description
            Log.d("123123", "Before delay adbStatus is $adbStatus")
            delay(500)
            if (adbStatus == "1"){
                runOnUiThread {
                    val intent = Intent(this@GameActivity, SplashActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}