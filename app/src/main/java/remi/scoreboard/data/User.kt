package remi.scoreboard.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.*

typealias PlayerList = RealmList<Player>

@RealmClass(name = "users", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class User(
    @PrimaryKey var id: Long = 0,
    @Required var firstName: String = "default_first_name",
    var lastName: String = "default_last_name",
    var email: String = "",
    var avatar: String = "file:///android_asset/dafault_avatar.png",
    var playerList: PlayerList
) : RealmObject() {

    constructor() : this(playerList = PlayerList())

    @Ignore
    val displayName = "$firstName $lastName".trim()
}