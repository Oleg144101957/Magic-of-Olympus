package gtpay.gtronicspay.c.viewmodel

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.installreferrer.api.InstallReferrerClient
import com.onesignal.OneSignal
import gtpay.gtronicspay.c.usecases.Encryptor
import gtpay.gtronicspay.c.usecases.GadidUsecase
import gtpay.gtronicspay.c.usecases.GetOsVe
import gtpay.gtronicspay.c.usecases.TimeUseCase
import gtpay.gtronicspay.c.usecases.VerCodeUseCase
import gtpay.gtronicspay.linksaver.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URLEncoder
import java.util.Locale

class ContainerViewModel(private val repository: Repository) : ViewModel() {

    private val _liveLink = MutableLiveData<String>()
    val liveLink : LiveData<String> = _liveLink

    fun initVM(){
        viewModelScope.launch (Dispatchers.IO){
            if (repository.readAllData().size>2){
                val linkFromRoom = repository.readAllData()[1].description
                _liveLink.postValue(linkFromRoom)
            }
        }
    }

    suspend fun createLink(referrerClient: InstallReferrerClient?, context: Context, key: Int){
        //Settings.Global.getString(context.contentResolver, Settings.Global.ADB_ENABLED)
        val encryptor = Encryptor(
            Settings.Global.getString(context.contentResolver, Settings.Global.ADB_ENABLED),
            key)

        val packageName = context.packageName
        val referrerUrl = referrerClient?.installReferrer?.installReferrer ?: ""
        val gInformation = getOlympusGid(context)
        val appVersion = VerCodeUseCase(context).getData()
        val osVe = GetOsVe().getData()
        val timestamp = TimeUseCase().getData()
        val userAgent = "Android ${Build.VERSION.RELEASE}; " +
                "${Locale.getDefault()}; " +
                "${Build.MODEL}; " +
                "Build/${Build.ID}"

        val jsonString = createJsonString(
            encryptor.getData("PACKAGE") to packageName,
            encryptor.getData("STRING_FROM_REFERER") to referrerUrl,
            encryptor.getData("GADID") to gInformation,
            encryptor.getData("APP_VERSION") to appVersion,
            encryptor.getData("OS_VERSION_KEY") to osVe,
            encryptor.getData("TIMESTAMP_KEY") to timestamp,
            encryptor.getData("USER_AGENT_KEY") to userAgent
        )

        val encodedString = withContext(Dispatchers.IO) {
            URLEncoder.encode(jsonString, "UTF-8")
        }

        val oneSig = encryptor.getData("ONE_SIGNAL_ID")
        OneSignal.setAppId(oneSig)

        val finalString = encryptor.getData("MAIN_LINK") + encodedString
        Log.d("123123", "Final link is $finalString")
        _liveLink.postValue(finalString)
    }
    private fun createJsonString(vararg params: Pair<String, Any>): String {
        val jsonObject = JSONObject()
        for ((key, value) in params) {
            jsonObject.put(key, value)
        }
        return jsonObject.toString()
    }
    private suspend fun getOlympusGid(context: Context) : String = withContext(Dispatchers.IO) {
        val gadidUsecase = GadidUsecase(context)
        gadidUsecase.getGadid()
    }
}