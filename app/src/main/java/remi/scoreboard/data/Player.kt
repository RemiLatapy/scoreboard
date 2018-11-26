package remi.scoreboard.data

import com.parse.ParseObject
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required
import remi.scoreboard.remote.parse.ParseManager

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class Player(
    @PrimaryKey var id: String = "-1",
    @Required var username: String = "default_name",
    var avatar: String = "file:///android_asset/dafault_avatar.png"
) : RealmObject() {

    constructor(parsePlayer: ParseObject?) : this() {
        parsePlayer?.let {
            this.id = it.objectId
            this.username = it.fetchIfNeeded<ParseObject>().getString(ParseManager.FIELD_USERNAME) ?: username
            this.avatar = it.fetchIfNeeded<ParseObject>().getString(ParseManager.FIELD_AVATAR) ?: avatar
        }
    }

    fun getParsePlayer(): ParseObject {
        val parsePlayer = ParseObject(ParseManager.TABLE_PLAYER)
        parsePlayer.put(ParseManager.FIELD_USERNAME, username)
        parsePlayer.put(ParseManager.FIELD_AVATAR, avatar) // TODO upload file
        return parsePlayer
    }

    override fun toString(): String {
        return "player $username ($id)"
    }
}