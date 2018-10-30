package remi.scoreboard.realmdata

import io.realm.RealmObject
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required

@RealmClass(name = "users", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
open class UserRealm(
    @Required var firstName: String = "default_first_name",
    @Required var lastName: String = "default_last_name",
    var email: String = "",
    var avatar: String = ""
) : RealmObject()