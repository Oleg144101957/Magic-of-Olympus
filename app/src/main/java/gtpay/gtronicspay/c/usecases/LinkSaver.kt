package gtpay.gtronicspay.c.usecases

import gtpay.gtronicspay.c.data.MagicModel
import gtpay.gtronicspay.c.data.Repository

class LinkSaver(private val repo: Repository) {
    fun execute(link: String){
        val magicModel = MagicModel(0, false, link)
        repo.addData(magicModel)
    }
}