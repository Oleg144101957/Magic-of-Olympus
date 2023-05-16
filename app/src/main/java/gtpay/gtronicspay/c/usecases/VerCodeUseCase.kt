package gtpay.gtronicspay.c.usecases

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

class VerCodeUseCase(private val context: Context) {

    fun getData() = try{
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

}