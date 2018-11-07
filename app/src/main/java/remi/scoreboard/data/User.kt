package remi.scoreboard.data

import com.parse.ParseUser
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.*

typealias PlayerList = RealmList<Player>

@RealmClass(name = "users", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class User(
    @PrimaryKey var id: String = "",
    @Required var username: String = "default_username",
    @Required var email: String = "",
    var avatar: String = "file:///android_asset/dafault_avatar.png",
    var playerList: PlayerList = PlayerList(),
    @Ignore var password: String = ""
) : RealmObject() {

//    constructor() : this(playerList = PlayerList())

    constructor(user: ParseUser) : this(playerList = PlayerList()) {
        id = user.objectId
        username = user.username
        email = user.email
    }

    fun getParseUser(): ParseUser {
        val user = ParseUser()
        user.username = username
        user.email = email
        user.setPassword(password)
        return user
    }
}