package remi.scoreboard.jetpack.model

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson

object Converters {

    @TypeConverter
    fun playerScoreListToJson(list: List<Pair<Int, Int>>): String = Gson().toJson(list)

    @TypeConverter
    fun jsonToPlayerScoreList(json: String): List<Pair<Int, Int>> = Gson().fromJson(json, ArrayList<Pair<Int, Int>>().javaClass)

    @TypeConverter
    fun stringListToJson(list: List<String>): String = Gson().toJson(list)

    @TypeConverter
    fun jsonToStringList(json: String): List<String> = Gson().fromJson(json, ArrayList<String>().javaClass)
}