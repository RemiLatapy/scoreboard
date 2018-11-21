package remi.scoreboard.data

import com.parse.ParseObject
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required
import java.util.*

typealias PlayerScoreList = RealmList<PlayerScore>

@RealmClass(name = "matches", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class Match(
    @PrimaryKey var id: String = "-1",
    var game: Game? = Game(),
    var scorePlayerList: PlayerScoreList = PlayerScoreList(),
    @Required var date: Date = Date()
) : RealmObject() {

    constructor(game: Game, playerList: List<Player>) : this() {
        this.game = game
        this.scorePlayerList.addAll(playerList.map { PlayerScore(player = it) })
    }

    constructor(parseMatch: ParseObject) : this() {
        this.id = parseMatch.objectId
        this.game = Game(parseMatch.getParseObject("game")?.fetchIfNeeded<ParseObject>())
        parseMatch.getList<ParseObject>("playerScoreList")?.map { PlayerScore(it) }?.let {
            this.scorePlayerList.addAll(it)
        }
        this.date = parseMatch.getDate("date") ?: Date(0)
    }

    fun getParseMatchWithParsePlayerScores(parsePlayerScore: List<ParseObject>): ParseObject {
        return ParseObject("match").apply {
            game?.let { put("game", ParseObject.createWithoutData("games", it.id)) }
            put("date", date)
            addAllUnique("playerScoreList", parsePlayerScore)
        }
    }

}
