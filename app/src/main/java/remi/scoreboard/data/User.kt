package remi.scoreboard.data

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.*
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@RealmClass(name = "users", fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
@Parcelize
open class User(
    @PrimaryKey var id: Long = 0,
    @Required var firstName: String = "default_first_name",
    var lastName: String = "default_last_name",
    var email: String = "",
    var avatar: String = ""
) : Parcelable, RealmObject() {

    @Ignore
    @IgnoredOnParcel
    val displayName = "$firstName $lastName".trim()

    @Ignore
    var isSelected = false
}