package remi.scoreboard.jetpack.model

import android.arch.persistence.room.*

@Entity(tableName = "matches", foreignKeys = [ForeignKey(entity = Game::class, parentColumns = ["game_id"], childColumns = ["game_id"])], indices = [Index("game_id")])
@TypeConverters(Converters::class)
data class Match(
        @ColumnInfo(name = "game_id") val gameId: Int,
        @ColumnInfo(name = "player_scores") val scorePlayers: List<Pair<Int, Int>>,
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "match_id") val mid: Int = 0) {
}