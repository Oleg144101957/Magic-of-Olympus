package gtpay.gtronicspay.c.viewmodel

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.installreferrer.api.InstallReferrerClient
import gtpay.gtronicspay.c.data.MagicModel
import gtpay.gtronicspay.c.usecases.Encryptor
import org.json.JSONObject
import java.lang.Exception
import java.net.URLEncoder
import java.util.Locale

class ContainerViewModel : ViewModel() {

    private val _roomMutableData = MutableLiveData<List<MagicModel>>()
    val roomLiveData : LiveData<List<MagicModel>> = _roomMutableData
    private val _stateOfWeb = MutableLiveData<String>("not_ready")
    val stateOfWeb: LiveData<String> = _stateOfWeb

    val tmpLiveLink = MutableLiveData<String>()

    fun createLink(referrerClient: InstallReferrerClient?, context: Context){
        val encryptor = Encryptor("0")

        val packageName = context.packageName
        val referrerUrl = referrerClient?.installReferrer?.installReferrer ?: ""
        val gadid = "4f4c00a2-0698-48d2-8525-9e1984b84225" // BULD THIS VALUE BEFORE RELEASE !!!
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

        val encodedString = URLEncoder.encode(jsonString, "UTF-8")

        val finalString = encryptor.getData("MAIN_LINK") + encodedString

        Log.d("123123", "Final link is $finalString")

        tmpLiveLink.value = finalString
        //post link to Room
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
}