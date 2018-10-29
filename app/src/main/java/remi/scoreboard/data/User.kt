package remi.scoreboard.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(@ColumnInfo(name = "name") val name: String,
                @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val uid: Int = 0) {

    @Ignore
    var isSelected = false
}