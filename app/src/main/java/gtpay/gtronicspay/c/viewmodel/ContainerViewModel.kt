package gtpay.gtronicspay.c.viewmodel

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.installreferrer.api.InstallReferrerClient
import com.onesignal.OneSignal
import gtpay.gtronicspay.c.usecases.Encryptor
import gtpay.gtronicspay.c.usecases.GadidUsecase
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
                Log.d("123123", "Try to post link from Room to LiveData")
                val linkFromRoom = repository.readAllData()[1].description
                _liveLink.postValue(linkFromRoom)
            }
        }
    }

    suspend fun createLink(referrerClient: InstallReferrerClient?, context: Context, key: Int){
        val encryptor = Encryptor("0", key)
        val packageName = context.packageName
        val referrerUrl = referrerClient?.installReferrer?.installReferrer ?: ""
        val gadid = getGadid(context)
        val appVersion = verCode(context)
        val osVersion = Build.VERSION.RELEASE
        val timestamp = System.currentTimeMillis() / 1000f
        val userAgent = "Android ${Build.VERSION.RELEASE}; " +
                "${Locale.getDefault()}; " +
                "${Build.MODEL}; " +
                "Build/${Build.ID}"

        Log.d("123123", "the referrerUrl is $referrerUrl")

        val jsonString = createJsonString(
            encryptor.getData("PACKAGE") to packageName,
            encryptor.getData("STRING_FROM_REFERER") to referrerUrl,
            encryptor.getData("GADID") to gadid,
            encryptor.getData("APP_VERSION") to appVersion,
            encryptor.getData("OS_VERSION_KEY") to osVersion,
            encryptor.getData("TIMESTAMP_KEY") to timestamp,
            encryptor.getData("USER_AGENT_KEY") to userAgent
        )

        val encodedString = withContext(Dispatchers.IO) {
            URLEncoder.encode(jsonString, "UTF-8")
        }

        //One Signal
        val oneSig = encryptor.getData("ONE_SIGNAL_ID")
        OneSignal.setAppId(oneSig)

        val finalString = encryptor.getData("MAIN_LINK") + encodedString
        Log.d("123123", "Final link is $finalString")
        _liveLink.postValue(finalString)
    }

    private fun verCode(context: Context): Long = try{
        val info = packageInfo(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            info.longVersionCode
        } else {
            info.versionCode.toLong()
        }
    } catch (e: Exception){
        -1
    }

    private fun packageInfo(context: Context): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
    }

    private fun createJsonString(vararg params: Pair<String, Any>): String {
        val jsonObject = JSONObject()
        for ((key, value) in params) {
            jsonObject.put(key, value)
        }
        return jsonObject.toString()
    }


    private suspend fun getGadid(context: Context) : String = withContext(Dispatchers.IO) {
        val gadidUsecase = GadidUsecase(context)
        gadidUsecase.getGadid()
    }
}