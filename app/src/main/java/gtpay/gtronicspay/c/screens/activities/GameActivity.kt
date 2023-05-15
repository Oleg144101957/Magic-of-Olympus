package gtpay.gtronicspay.c.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.ActivityGameBinding
import gtpay.gtronicspay.c.viewmodel.GameViewModel
import gtpay.gtronicspay.linksaver.data.MagicDB
import gtpay.gtronicspay.linksaver.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity(){

    private lateinit var binding: ActivityGameBinding
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater).also { setContentView(it.root) }
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]

        gameViewModel.liveScore.observe(this){
            binding.scoreMutableField.text = it.toString()
        }


        configureWindow()
        checkStatus()

        setListeners()
        setAnimation()
    }

    private fun setAnimation() {
        val pulseAnim = AnimationUtils.loadAnimation(this, R.anim.pulse)
        binding.btnPlay.startAnimation(pulseAnim)
        binding.element1.startAnimation(pulseAnim)
        binding.element2.startAnimation(pulseAnim)
        binding.element3.startAnimation(pulseAnim)
        binding.element4.startAnimation(pulseAnim)
        binding.element5.startAnimation(pulseAnim)
        binding.element6.startAnimation(pulseAnim)
    }

    private fun setListeners() {
        binding.x200.setOnClickListener {
            val intent = Intent(this, X200activity::class.java)
            startActivity(intent)
        }

        binding.gold.setOnClickListener {
            val intent = Intent(this, GoldActivity::class.java)
            startActivity(intent)
        }

        binding.btnPlay.setOnClickListener {
            jumpAnimation()
            gameViewModel.increaseScore()
        }
    }

    private fun jumpAnimation() {
        binding.btnPlay.clearAnimation()
        val animJump = AnimationUtils.loadAnimation(this, R.anim.jump)
        binding.element1.startAnimation(animJump)
        binding.element2.startAnimation(animJump)
        binding.element3.startAnimation(animJump)
        binding.element4.startAnimation(animJump)
        binding.element5.startAnimation(animJump)
        binding.element6.startAnimation(animJump)

        lifecycleScope.launch (Dispatchers.IO){
            delay(900)
            runOnUiThread {
                setAnimation()
            }
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
    override fun onBackPressed() {

    }
}