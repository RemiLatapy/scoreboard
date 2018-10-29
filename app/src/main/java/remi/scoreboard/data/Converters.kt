package remi.scoreboard.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun playerScoreListToJson(list: PlayerScoreList): String = Gson().toJson(list)

    @TypeConverter
    fun jsonToPlayerScoreList(json: String): PlayerScoreList = Gson().fromJson(json, object : TypeToken<PlayerScoreList>() {}.type)

    @TypeConverter
    fun stringListToJson(list: List<String>): String = Gson().toJson(list)

    @TypeConverter
    fun jsonToStringList(json: String): List<String> = Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
}