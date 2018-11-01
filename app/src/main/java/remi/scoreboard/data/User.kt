package remi.scoreboard.data

import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required

@RealmClass(name = "users", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class User(
    @Required var firstName: String = "default_first_name",
    var lastName: String = "default_last_name",
    var email: String = "",
    var avatar: String = "file:///android_asset/dafault_avatar.png"
) : RealmObject() {

    @Ignore
    val displayName = "$firstName $lastName".trim()
}