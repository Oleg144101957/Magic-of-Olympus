package gtpay.gtronicspay.c.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.ActivityX200activityBinding

class X200activity : AppCompatActivity() {

    private lateinit var binding: ActivityX200activityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityX200activityBinding.inflate(layoutInflater).also { setContentView(it.root) }


        binding.btnBack.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}