package gtpay.gtronicspay.linksaver.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import gtpay.gtronicspay.linksaver.Const
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Const.TABLE_NAME)
data class MagicModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @ColumnInfo(name = "adb_status")
    val adbStatus: Boolean,
    @ColumnInfo(name = "link")
    val link: String
) : Parcelable