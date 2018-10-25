package remi.scoreboard.jetpack.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users")
data class User(@ColumnInfo(name = "name") val name: String,
                @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val uid: Int = 0) {
}