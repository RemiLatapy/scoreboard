package remi.scoreboard.jetpack.model

import android.arch.persistence.room.*

@Entity(tableName = "games",
        indices = [Index(value = ["name"], unique = true)])
@TypeConverters(Converters::class)
data class Game(val name: String,
                val thumbnail: String,
                val rules: List<String>,
                @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "game_id") val gid: Int = 0) {
}