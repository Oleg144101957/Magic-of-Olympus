package gtpay.gtronicspay.c.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MagicModel::class], exportSchema = true, version = 1)
abstract class MagicDB : RoomDatabase(){

    abstract fun getGameDao(): GameDao
    
    companion object{

        @Volatile
        var INSTANCE: MagicDB? = null

        fun getMagicDatabase(context: Context): MagicDB {
            val tmp = INSTANCE
            if (tmp != null) return tmp

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MagicDB::class.java,
                    "MAGIC_DB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}