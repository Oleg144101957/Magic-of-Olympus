package gtpay.gtronicspay.c.screens

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import gtpay.gtronicspay.c.R
import gtpay.gtronicspay.c.databinding.FragmentContainerBinding
import gtpay.gtronicspay.c.viewmodel.ContainerViewModel

class ContainerFragment : Fragment() {

    private lateinit var binding: FragmentContainerBinding
    private lateinit var vm: ContainerViewModel
    lateinit var chooseCallback: ValueCallback<Array<Uri?>>
    val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        chooseCallback.onReceiveValue(it.toTypedArray())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm = ViewModelProvider(this)[ContainerViewModel::class.java]
        binding = FragmentContainerBinding.inflate(layoutInflater, container, false)

        refInstaller(requireContext())

        val web = CustomView(requireContext(), object : OnFileChoose{
            override fun onChooseCallbackActivated(paramChooseCallback: ValueCallback<Array<Uri?>>) {
                chooseCallback = paramChooseCallback
            }
        })

        web.loadUrl("http://google.com")
        binding.frameLayoutContainer.addView(web)
        web.startWeb(getContent)



        //Build link
        // create WebView object
        //Put link in room
        //Observe Room
        // when link is ready load page, and show it
        
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    private fun refInstaller(context: Context){
        Log.d("123123", "refInstaller began")
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
                Log.d("123123", "onInstallReferrerServiceDisconnected method")
            }
        })
    }
}