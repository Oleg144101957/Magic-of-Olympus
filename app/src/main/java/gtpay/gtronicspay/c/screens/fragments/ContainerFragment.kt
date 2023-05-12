package gtpay.gtronicspay.c.screens.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import gtpay.gtronicspay.c.databinding.FragmentContainerBinding
import gtpay.gtronicspay.c.screens.CustomView
import gtpay.gtronicspay.c.screens.OnFileChoose
import gtpay.gtronicspay.c.viewmodel.ContainerViewModel
import gtpay.gtronicspay.c.viewmodel.ContainerViewModelFactory
import gtpay.gtronicspay.linksaver.data.MagicDB
import gtpay.gtronicspay.linksaver.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContainerFragment : FragmentBase<FragmentContainerBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentContainerBinding
        get() = FragmentContainerBinding::inflate
    private lateinit var vm: ContainerViewModel
    lateinit var chooseCallback: ValueCallback<Array<Uri?>>
    val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        chooseCallback.onReceiveValue(it.toTypedArray())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vm = ViewModelProvider(this, ContainerViewModelFactory(requireContext()))[ContainerViewModel::class.java]
        vm.initVM()
        val magicDao = MagicDB.getMagicDatabase(requireContext()).getGameDao()
        val repo = Repository(magicDao)

        lifecycleScope.launch(Dispatchers.IO) {
            if (repo.readAllData().size==1){
                Log.d("123123", "No data in Room, building link, observer started")
                refInstaller(requireContext())
                launch(Dispatchers.Main) {
                    vm.liveLink.observe(viewLifecycleOwner){
                        setView(url = it)
                    }
                }

            } else {
                Log.d("123123", "There is data in Room, go to setView method directly ")
                val linkFromRoom = repo.readAllData()[1].description
                launch(Dispatchers.Main) {
                    setView(linkFromRoom)
                }
            }
        }
    }


    private fun setView(url: String) {
        val web = CustomView(requireContext(), object : OnFileChoose {
            override fun onChooseCallbackActivated(paramChooseCallback: ValueCallback<Array<Uri?>>) {
                chooseCallback = paramChooseCallback
            }
        })
        web.loadUrl(url)
        web.startWeb(getContent)
        binding.root.addView(web)
        web.isVisible = true
    }

    private fun refInstaller(context: Context){
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener{
            override fun onInstallReferrerSetupFinished(respondCode: Int) {
                when(respondCode){
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        Log.d("123123", "Code OK")
                        vm.createLink(referrerClient, requireContext())

                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        Log.d("123123", "Code FEATURE_NOT_SUPPORTED")
                        vm.createLink(referrerClient, requireContext())

                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        Log.d("123123", "Code SERVICE_UNAVAILABLE")
                        vm.createLink(referrerClient, requireContext())

                    }
                    else -> {
                        Log.d("123123", "Code SERVICE_UNAVAILABLE")
                        vm.createLink(referrerClient, requireContext())
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {

            }
        })
    }
}