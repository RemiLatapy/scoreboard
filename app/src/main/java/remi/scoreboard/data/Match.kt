package remi.scoreboard.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required
import kotlinx.android.parcel.RawValue
import java.util.*

typealias PlayerScoreList = @RawValue RealmList<PlayerScore>

@RealmClass(name = "matches", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class Match(
    var game: Game? = null,
    var scorePlayerList: PlayerScoreList,
    @Required var date: Date
) : RealmObject() {

    constructor() : this(null, PlayerScoreList(), Date())
}

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class PlayerScore(
    var user: User? = null,
    var score: Int = 0
) : RealmObject()