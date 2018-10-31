package remi.scoreboard.data

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import kotlinx.android.parcel.Parcelize

//typealias PlayerScoreList = @RawValue RealmList<Pair<User, Int>>

// TODO deal with scorePlayerList

@RealmClass(name = "matches", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
@Parcelize
open class Match(
    var game: Game? = null,
    var scorePlayerList: String = ""
) : Parcelable, RealmObject()