package remi.scoreboard.data

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required
import kotlinx.android.parcel.Parcelize

@RealmClass(name = "games", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
@Parcelize
open class Game(
    @PrimaryKey var id: Long,
    @Required var name: String = "game_default",
    var thumbnail: String = "",
    var type: String = ""
) : RealmObject(), Parcelable