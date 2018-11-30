package remi.scoreboard.data

import android.os.Parcelable
import com.parse.ParseObject
import com.parse.ktx.getBooleanOrNull
import com.parse.ktx.getIntOrNull
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmNamingPolicy
import io.realm.annotations.Required
import kotlinx.android.parcel.Parcelize
import remi.scoreboard.remote.parse.ParseManager

@RealmClass(fieldNamingPolicy = RealmNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
@Parcelize
open class Game(
    @PrimaryKey var id: String = "-1",
    @Required var name: String = "game_default",
    var thumbnail: String = "",
    var order: Int = Int.MAX_VALUE,
    var deleted: Boolean = false
) : RealmObject(), Parcelable {

    constructor(parseGame: ParseObject?) : this() {
        parseGame?.let {
            this.id = parseGame.objectId
            this.name = parseGame.getString(ParseManager.FIELD_NAME) ?: name
            this.order = parseGame.getIntOrNull(ParseManager.FIELD_ORDER) ?: order
            this.deleted = parseGame.getBooleanOrNull(ParseManager.FIELD_DELETED) ?: deleted
            this.thumbnail = parseGame.getParseFile(ParseManager.FIELD_THUMBNAIL)?.url ?: thumbnail
        }
    }
}