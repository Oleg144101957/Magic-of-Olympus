package gtpay.gtronicspay.c.screens.activities

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.ActivityGoldBinding

class GoldActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoldBinding

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoldBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setRoundAnimation()
    }



    private fun setRoundAnimation() {
        val rotationAnimator = ObjectAnimator.ofFloat(
            binding.goldenSun,
            "rotation",
            0f,
            360f // One complete rotation (adjust as needed)
        )
        // Set the duration of the animation (in milliseconds)
        rotationAnimator.duration = 6000 // Adjust the duration as desired

        // Set the interpolator for smooth linear animation
        rotationAnimator.interpolator = LinearInterpolator()

        // Set the repeat count to indicate infinite animation
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE

        // Set the repeat mode to ensure seamless rotation
        rotationAnimator.repeatMode = ObjectAnimator.RESTART

        // Start the animation
        rotationAnimator.start()
    }
}