package gtpay.gtronicspay.c.usecases

import android.content.Context
import android.provider.Settings

class AndroidChecker() {
    fun isAdb(context: Context) : Boolean{
        val status = Settings.Global.getString(context.contentResolver, Settings.Global.ADB_ENABLED)
        return status == "1"
    }
}