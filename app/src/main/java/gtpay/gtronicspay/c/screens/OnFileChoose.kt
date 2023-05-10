package gtpay.gtronicspay.c.screens

import android.net.Uri
import android.webkit.ValueCallback

interface OnFileChoose {
    fun onChooseCallbackActivated(paramChooseCallback: ValueCallback<Array<Uri?>>)
}