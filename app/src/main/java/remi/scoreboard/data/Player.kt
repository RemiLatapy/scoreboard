package remi.scoreboard.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class Player(
    @PrimaryKey var id: Long = 0,
    var name: String = "default_name",
    var avatar: String = "file:///android_asset/dafault_avatar.png"
) : RealmObject()