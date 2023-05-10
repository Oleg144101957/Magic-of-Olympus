package gtpay.gtronicspay.c.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "my_table")
data class MagicModel (
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @ColumnInfo(name = "adb_status")
    val adbStatus: Boolean,
    @ColumnInfo(name = "link")
    val link: String
    ) : Parcelable