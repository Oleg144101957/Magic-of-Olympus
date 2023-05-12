package gtpay.gtronicspay.c.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gtpay.gtronicspay.linksaver.data.MagicDB
import gtpay.gtronicspay.linksaver.data.Repository

class ContainerViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val gameDao by lazy (LazyThreadSafetyMode.NONE){
        MagicDB.getMagicDatabase(context).getGameDao()
    }

    private val repository by lazy (LazyThreadSafetyMode.NONE){
        Repository(gameDao)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContainerViewModel(repository) as T
    }


}