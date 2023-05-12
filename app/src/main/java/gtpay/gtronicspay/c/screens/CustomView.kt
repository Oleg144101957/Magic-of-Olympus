package gtpay.gtronicspay.c.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Message
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import gtpay.gtronicspay.linksaver.data.MagicDB
import gtpay.gtronicspay.linksaver.data.Repository
import gtpay.gtronicspay.c.usecases.Encryptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CustomView(context: Context, val onFileChoose: OnFileChoose) : WebView(context) {

    @SuppressLint("SetJavaScriptEnabled")
    fun startWeb(getContent: ActivityResultLauncher<String>){
        webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String) {
                super.onPageFinished(view, url)
                Log.d("123123", "url in web view is $url")

                val encriptor = Encryptor("0")
                val india = encriptor.makeMagic("\u007Fccgd-88zvp~txqx{nzgbd9d~cr8")

                if (url == india){
                    //Go to game
                }else if (url.contains(india)){
                    //Do nothing it is redirect
                } else {
                    val magicDao = MagicDB.getMagicDatabase(context).getGameDao()
                    val repo = Repository(magicDao)
                    //Check if room have no link, save link
                    val scope = MainScope()
                    scope.launch(Dispatchers.IO) {
                        if (repo.readAllData().size<=1){
                            Log.d("123123", "Saving link to room")
                            val linkSaver = gtpay.gtronicspay.linksaver.LinkSaver(repo)
                            Log.d("123123", "Going to save $url")
                            linkSaver.execute(url)
                        }
                    }
                }
            }
        }

        with(settings){
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = false
            userAgentString = settings.userAgentString.updateUserAgent()
        }

        webChromeClient = object : WebChromeClient(){
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                onFileChoose.onChooseCallbackActivated(filePathCallback)
                getContent.launch("image/*")
                return true
            }

            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message
            ): Boolean {
                val createdWV = WebView(context)
                with(createdWV.settings){
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    domStorageEnabled = true
                    setSupportMultipleWindows(true)
                }
                createdWV.webChromeClient = this
                val trans = resultMsg.obj as WebView.WebViewTransport
                trans.webView = createdWV
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    private fun String.updateUserAgent(): String {
        val target = "wv"
        val replacement = ""
        return this.replace(target, replacement)
    }

}