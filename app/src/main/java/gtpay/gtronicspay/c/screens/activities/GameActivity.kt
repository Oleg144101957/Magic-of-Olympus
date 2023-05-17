package gtpay.gtronicspay.c.screens.activities

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.lifecycle.lifecycleScope
import gtpay.gtronicspay.c.Const
import gtpay.gtronicspay.c.databinding.ActivityGameBinding
import gtpay.gtronicspay.linksaver.data.MagicDB
import gtpay.gtronicspay.linksaver.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity(){

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater).also { setContentView(it.root) }
        checkStatus()
        makeAnimation()
        binding.god.setOnClickListener {
            startActivity(Intent(this, GoldActivity::class.java))
        }
    }

    private fun makeAnimation() {
        // Create ObjectAnimator for vertical translation (Y-axis)
        val translationAnimator = ObjectAnimator.ofFloat(
            binding.god,
            "translationY",
            0f,
            200f // Adjust this value as per your desired vertical movement
        )

        translationAnimator.duration = 1000
        translationAnimator.interpolator = LinearInterpolator()
        translationAnimator.repeatCount = ObjectAnimator.INFINITE
        translationAnimator.repeatMode = ObjectAnimator.REVERSE
        translationAnimator.start()
    }

    private fun checkStatus(){
        lifecycleScope.launch(Dispatchers.IO) {

            val india = intent.getStringExtra(Const.INDIA_KEY)
            val magicDao = MagicDB.getMagicDatabase(this@GameActivity).getGameDao()
            val repo = Repository(magicDao)
            val listMagicModels = repo.readAllData()
            val adbStatus = listMagicModels[0].description
            delay(500)
            if (adbStatus == "0" && india == null){
                runOnUiThread {
                    val intent = Intent(this@GameActivity, SplashActivity::class.java)
                    //startActivity(intent)
                }
            }
        }
    }
    override fun onBackPressed() {

    }
}