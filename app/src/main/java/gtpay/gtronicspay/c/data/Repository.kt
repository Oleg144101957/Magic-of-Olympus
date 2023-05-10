package gtpay.gtronicspay.c.data


class Repository(private val gameDao: GameDao) {

    fun readAllData(): List<MagicModel> {
        return gameDao.readAllData()
    }

    fun addData(magicModel: MagicModel){
        gameDao.addData(magicModel)
    }
}