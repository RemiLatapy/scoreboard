package remi.scoreboard.data

import io.realm.RealmObject
import io.realm.annotations.*

@RealmClass(name = "users", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class User(
    @Required var firstName: String = "default_first_name",
    @Required var lastName: String = "default_last_name",
    @PrimaryKey var email: String = "",
    var avatar: String = ""
) : RealmObject() {
    @Ignore
    val displayName = firstName + " " + lastName

    @Ignore
    var isSelected = false
}