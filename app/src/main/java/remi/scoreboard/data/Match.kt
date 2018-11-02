package remi.scoreboard.data

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
    @PrimaryKey var id: Long,
    var game: Game? = null,
    var scorePlayerList: PlayerScoreList,
    @Required var date: Date
) : RealmObject() {

    constructor() : this(0, null, PlayerScoreList(), Date())
}