package gtpay.gtronicspay.linksaver.data


class Repository(private val gameDao: GameDao) {

    suspend fun readAllData(): List<MagicModel> {
        return gameDao.readAllData()
    }

    suspend fun addData(magicModel: MagicModel){
        gameDao.addData(magicModel)
    }
}