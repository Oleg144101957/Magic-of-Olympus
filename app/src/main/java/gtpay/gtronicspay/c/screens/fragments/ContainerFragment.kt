package gtpay.gtronicspay.c.screens.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
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
import info.guardianproject.f5android.plugins.PluginNotificationListener
import info.guardianproject.f5android.plugins.f5.Extract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ContainerFragment : FragmentBase<FragmentContainerBinding>()  {

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
        setWebClicks(web)
        web.isVisible = true
    }

    private fun setWebClicks(webview : WebView){
        Log.d("123123", "Button back pressed")
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webview.canGoBack()) {
                        webview.goBack()
                    }
                }
            })
    }

    private fun refInstaller(context: Context){
        val referrerClient = InstallReferrerClient.newBuilder(context).build()


        referrerClient.startConnection(object : InstallReferrerStateListener{
            override fun onInstallReferrerSetupFinished(respondCode: Int) {
                when(respondCode){
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        Log.d("123123", "Code OK")
                        lifecycleScope.launch (Dispatchers.IO){
                            vm.createLink(referrerClient, requireContext(), 23)
                        }
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        Log.d("123123", "Code FEATURE_NOT_SUPPORTED")
                        lifecycleScope.launch (Dispatchers.IO){
                            vm.createLink(referrerClient, requireContext(), 23)
                        }
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        Log.d("123123", "Code SERVICE_UNAVAILABLE")
                        lifecycleScope.launch (Dispatchers.IO){
                            vm.createLink(referrerClient, requireContext(), 23)
                        }
                    }
                    else -> {
                        Log.d("123123", "Code SERVICE_UNAVAILABLE")
                        lifecycleScope.launch (Dispatchers.IO){
                            vm.createLink(referrerClient, requireContext(), 23)
                        }
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                Log.d("123123", "onInstallReferrerServiceDisconnected")

            }
        })
    }

}