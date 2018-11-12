package remi.scoreboard.data

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class PlayerScore(
    var player: Player? = null,
    var score: Int = 0
) : RealmObject()