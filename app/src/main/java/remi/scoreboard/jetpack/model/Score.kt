package remi.scoreboard.jetpack.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(val score: Int,
                 @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "score_id") val sid: Int = 0) {
}