package gtpay.gtronicspay.linksaver

import gtpay.gtronicspay.linksaver.data.MagicModel
import gtpay.gtronicspay.linksaver.data.Repository


class LinkSaver(private val repo: Repository) {
    suspend fun execute(link: String){
        val magicModel = MagicModel(1, link)
        repo.addData(magicModel)
    }
}