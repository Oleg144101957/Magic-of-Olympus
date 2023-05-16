package gtpay.gtronicspay.c.usecases

import android.os.Build

class GetOsVe {
    fun getData() : String {
        return Build.VERSION.RELEASE
    }
}