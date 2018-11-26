package remi.scoreboard.data

import com.parse.ParseObject
import com.parse.ParseUser
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.*
import remi.scoreboard.remote.parse.ParseManager

typealias PlayerList = RealmList<Player>

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class User(
    @PrimaryKey var id: String = "",
    @Required var username: String = "default_username",
    @Required var email: String = "",
    var displayName: String = "",
    var avatar: String = "file:///android_asset/dafault_avatar.png",
    var playerList: PlayerList = PlayerList(),
    var isLocalUser: Boolean = false,
    @Ignore var password: String = ""
) : RealmObject() {

    init {
        if (displayName.isEmpty())
            displayName = username
    }

    constructor(user: ParseUser, parsePlayerList: List<ParseObject> = ArrayList()) : this() {
        id = user.objectId
        username = user.username
        displayName = user.getString(ParseManager.FIELD_DISPLAY_NAME) ?: displayName
        email = user.email
        avatar = user.getParseFile(ParseManager.FIELD_AVATAR)?.url ?: avatar
        parsePlayerList.mapTo(playerList) { Player(it) }
    }

    fun getParseUser(): ParseUser {
        val user = ParseUser()
        user.username = username
        user.email = email
        user.setPassword(password)
        user.put(ParseManager.FIELD_DISPLAY_NAME, displayName)
//        user.put("avatar", avatar) // TODO upload file...
        return user
    }
}