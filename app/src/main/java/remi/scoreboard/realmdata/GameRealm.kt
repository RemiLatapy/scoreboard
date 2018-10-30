package remi.scoreboard.realmdata

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import kotlinx.android.parcel.Parcelize

@RealmClass(name = "games", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
@Parcelize
open class GameRealm(
    @PrimaryKey var name: String = "game_default",
    var thumbnail: String = "",
    var type: String = ""
) : RealmObject(), Parcelable