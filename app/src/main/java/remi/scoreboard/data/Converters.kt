package remi.scoreboard.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun playerScoreListToJson(list: PlayerScoreList): String = Gson().toJson(list)

    @TypeConverter
    fun jsonToPlayerScoreList(json: String): PlayerScoreList = Gson().fromJson(json, ArrayList<Pair<Int, Score>>().javaClass)

    @TypeConverter
    fun stringListToJson(list: List<String>): String = Gson().toJson(list)

    @TypeConverter
    fun jsonToStringList(json: String): List<String> = Gson().fromJson(json, ArrayList<String>().javaClass)
}