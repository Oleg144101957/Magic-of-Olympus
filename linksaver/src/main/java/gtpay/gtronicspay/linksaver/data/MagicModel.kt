package gtpay.gtronicspay.linksaver.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "my_table")
data class MagicModel (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "value")
    val description: String
    ) : Parcelable