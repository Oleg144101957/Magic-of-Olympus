package gtpay.gtronicspay.c.screens.activities

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import gtpay.gtronicspay.c.Const
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.ActivityGoldBinding
import kotlin.random.Random

class GoldActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoldBinding
    private val listOfElements = listOf(
        R.drawable.element_1_939,
        R.drawable.element_2_939,
        R.drawable.element_3_939,
        R.drawable.element_4_939,
        R.drawable.element_5_939,
        R.drawable.element_6_939)
    private var flyingPosition: Float = 0.0F
    private var score = 0

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoldBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setAnimation()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.check.setOnClickListener {
            if (flyingPosition in 450.0F..600.0F){
                score++
                binding.scoretextview.text = "Score is $score"
                animateSuccess()
            }
        }

        binding.uncheck.setOnClickListener {
            startActivity(Intent(this, X200activity::class.java))
        }
    }

    private fun setAnimation() {
        setRoundAnimation()
        setGameAnimation()
    }


    private fun setGameAnimation() {
        val initialX = binding.rotatingElement.x

        // Calculate the target X position
        val targetX = initialX + 1500 // Adjust the value as needed

        // Create an ObjectAnimator to animate the X property of the element
        val animator = ObjectAnimator.ofFloat(binding.rotatingElement, "x", initialX, targetX)
        animator.duration = 3000 // Animation duration in milliseconds
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Float
            // Update the current x position of the element
            binding.rotatingElement.x = currentValue
            flyingPosition = currentValue
        }


        // Set an animation end listener to remove the element when animation completes
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // Remove the element from its parent view
                (binding.rotatingElement as? View)?.clearAnimation()
                flyingPosition = 0.0F
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {
                binding.rotatingElement.setImageResource(
                    listOfElements[Random.nextInt(0, 6)])
            }
        })

        // Start the animation
        animator.start()
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

    private fun animateSuccess(){
        val originalScaleX = binding.targetElement.scaleX
        val originalScaleY = binding.targetElement.scaleY

        val targetScaleX = originalScaleX * 1.5f
        val targetScaleY = originalScaleY * 1.5f

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000 // Animation duration in milliseconds

        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            val scaleX = originalScaleX + (targetScaleX - originalScaleX) * animatedValue
            val scaleY = originalScaleY + (targetScaleY - originalScaleY) * animatedValue
            binding.targetElement.scaleX = scaleX
            binding.targetElement.scaleY = scaleY
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                binding.targetElement.scaleX = originalScaleX
                binding.targetElement.scaleY = originalScaleY
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        animator.start()
    }
}