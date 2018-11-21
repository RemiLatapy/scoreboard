package remi.scoreboard.data

import com.parse.ParseObject
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class PlayerScore(
    @PrimaryKey var id: String = "-1",
    var player: Player? = Player(),
    var score: Int = 0
) : RealmObject() {

    constructor(parsePlayerScore: ParseObject) : this() {
        id = parsePlayerScore.objectId
        player = Player(parsePlayerScore.getParseObject("player"))
        score = parsePlayerScore.fetchIfNeeded<ParseObject>().getInt("score")
    }

    fun getParsePlayerScore(): ParseObject {
        return ParseObject("playerscore").apply {
            player?.let { put("player", ParseObject.createWithoutData("player", it.id)) }
            put("score", score)
        }
    }
}