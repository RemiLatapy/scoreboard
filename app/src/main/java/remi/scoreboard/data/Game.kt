package remi.scoreboard.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "games",
        indices = [Index(value = ["name"], unique = true)])
@TypeConverters(Converters::class)
@Parcelize
data class Game(val name: String,
                val thumbnail: String,
                val rules: List<String>,
                @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "game_id") val gid: Int = 0) : Parcelable