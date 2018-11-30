package remi.scoreboard.data

import com.parse.ParseObject
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required
import remi.scoreboard.remote.parse.ParseManager
import java.util.*

typealias PlayerScoreList = RealmList<PlayerScore>

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class Match(
    @PrimaryKey var id: String = "-1",
    var game: Game? = Game(),
    var scorePlayerList: PlayerScoreList = PlayerScoreList(), // TODO rename field... playerScoreList
    @Required var date: Date = Date()
) : RealmObject() {

    constructor(game: Game, playerList: List<Player>) : this() {
        this.game = game
        this.scorePlayerList.addAll(playerList.mapIndexed { idx, player -> PlayerScore(player = player, order = idx) })
    }

    constructor(parseMatch: ParseObject) : this() {
        this.id = parseMatch.objectId
        this.game = Game(parseMatch.getParseObject(ParseManager.FIELD_GAME)?.fetchIfNeeded<ParseObject>())
        parseMatch.getList<ParseObject>(ParseManager.FIELD_PLAYERSCORE_LIST)
            ?.map { PlayerScore(it.fetchIfNeeded<ParseObject>()) }?.let {
                this.scorePlayerList.addAll(it)
            }
        this.date = parseMatch.getDate(ParseManager.FIELD_DATE) ?: Date(0)
    }

    fun getParseMatchWithParsePlayerScores(parsePlayerScore: List<ParseObject>): ParseObject {
        return ParseObject(ParseManager.TABLE_MATCH).apply {
            game?.let { put(ParseManager.FIELD_GAME, ParseObject.createWithoutData(ParseManager.TABLE_GAME, it.id)) }
            put(ParseManager.FIELD_DATE, date)
            addAllUnique(ParseManager.FIELD_PLAYERSCORE_LIST, parsePlayerScore)
        }
    }
}
