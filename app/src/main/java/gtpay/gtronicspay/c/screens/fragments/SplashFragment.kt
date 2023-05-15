package gtpay.gtronicspay.c.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.linksaver.data.MagicDB
import gtpay.gtronicspay.linksaver.data.MagicModel
import gtpay.gtronicspay.linksaver.data.Repository
import gtpay.gtronicspay.c.databinding.FragmentSplashBinding
import gtpay.gtronicspay.c.usecases.AndroidChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : FragmentBase<FragmentSplashBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkStatus()
    }

    private fun checkStatus(){
        lifecycleScope.launch(Dispatchers.IO) {
            val magicDao = MagicDB.getMagicDatabase(requireContext()).getGameDao()
            val repo = Repository(magicDao)

            if (repo.readAllData().isNotEmpty() && repo.readAllData()[0].description == "0"){
                Log.d("123123", "Status adb is not true room is not empty, is not first time")
                navigateToNextActivityWithDelay(R.id.action_splashFragment_to_containerFragment, 0)
            } else if (repo.readAllData().isNotEmpty() && repo.readAllData()[0].description == "1"){
                Log.d("123123", "Status adb is TRUE in room it is Moderator again")
                //Moderator -> Go directly to the game
                navigateToNextActivityWithDelay(R.id.action_splashFragment_to_gameActivity, 500)
            }else{
                //First time
                val checker = AndroidChecker()
                val status = checker.isAdb(requireContext())
                Log.d("123123", "The room is empty Status adb is ${status} going to Game first time")
                val magicModel = MagicModel(0, status)
                repo.addData(magicModel)
                navigateToNextActivityWithDelay(R.id.action_splashFragment_to_gameActivity, 3500)
            }
        }
    }
    private fun navigateToNextActivityWithDelay(action: Int, delay: Long){
        lifecycleScope.launch {
            delay(delay)
            findNavController().navigate(action)
        }
    }
}