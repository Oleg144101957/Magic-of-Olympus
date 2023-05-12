package gtpay.gtronicspay.linksaver.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {

    @Query("SELECT * FROM my_table")
    fun readAllData(): List<MagicModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(magicModel: MagicModel)

}