package remi.scoreboard.realmdata

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import kotlinx.android.parcel.Parcelize

//typealias PlayerScoreList = @RawValue RealmList<Pair<UserRealm, Int>>

// TODO deal with scorePlayerList

@RealmClass(name = "matches", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
@Parcelize
open class MatchRealm(
    var game: GameRealm? = null,
    var scorePlayerList: String = ""
) : Parcelable, RealmObject()