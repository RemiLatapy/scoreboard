package remi.scoreboard.data

import android.os.Parcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

typealias PlayerScoreList = @RawValue RealmList<PlayerScore>

@RealmClass(name = "matches", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
@Parcelize
open class Match(
    var game: Game? = null,
    var scorePlayerList: PlayerScoreList,
    @Required var date: Date,
    @PrimaryKey var id: Long = 0
) : Parcelable, RealmObject() {

    constructor() : this(null, PlayerScoreList(), Date(), 0)
}

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
@Parcelize
open class PlayerScore(
    var User: User? = null,
    var score: Int = 0
) : Parcelable, RealmObject()