package remi.scoreboard.data

import com.parse.ParseObject
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import remi.scoreboard.remote.parse.ParseManager

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class PlayerScore(
    @PrimaryKey var id: String = "-1",
    var player: Player? = Player(),
    var score: Int = 0,
    var order: Int = 0
) : RealmObject() {

    constructor(parsePlayerScore: ParseObject) : this() {
        id = parsePlayerScore.objectId
        player = Player(parsePlayerScore.getParseObject(ParseManager.FIELD_PLAYER))
        score = parsePlayerScore.fetchIfNeeded<ParseObject>().getInt(ParseManager.FIELD_SCORE)
        order = parsePlayerScore.fetchIfNeeded<ParseObject>().getInt(ParseManager.FIELD_ORDER)
    }

    fun getParsePlayerScore(): ParseObject {
        return ParseObject(ParseManager.TABLE_PLAYERSCORE).apply {
            player?.let {
                put(
                    ParseManager.FIELD_PLAYER,
                    ParseObject.createWithoutData(ParseManager.TABLE_PLAYER, it.id)
                )
            }
            put(ParseManager.FIELD_SCORE, score)
            put(ParseManager.FIELD_ORDER, order)
        }
    }
}