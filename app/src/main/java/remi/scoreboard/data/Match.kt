package remi.scoreboard.data

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

typealias Score = Map<String, Int>
typealias PlayerScoreList = List<Pair<Int, Score>>
typealias MutablePlayerScoreList = MutableList<Pair<Int, Score>>

@Entity(
    tableName = "matches",
    foreignKeys = [ForeignKey(entity = Game::class, parentColumns = ["game_id"], childColumns = ["game_id"])],
    indices = [Index("game_id")]
)
@TypeConverters(Converters::class)
@Parcelize
data class Match(
    @ColumnInfo(name = "game_id") val gameId: Int,
    @ColumnInfo(name = "player_scores") val scorePlayers: PlayerScoreList,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "match_id") val mid: Int = 0
) : Parcelable