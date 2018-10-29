package remi.scoreboard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [Game::class, Match::class, User::class],
    version = 1
)
abstract class ScoreboardDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gameDao(): GameDao
    abstract fun matchDao(): MatchDao


    companion object {
        @Volatile
        private var INSTANCE: ScoreboardDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ScoreboardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreboardDatabase::class.java,
                    "Scoreboard_database"
                ).addCallback(ScoreboardDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class ScoreboardDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
//        override fun onOpen(db: SupportSQLiteDatabase) {
//            super.onOpen(db)
//            INSTANCE?.let { database ->
//                scope.launch(Dispatchers.IO) {
//                    scope.coroutineContext.
//                }
//            }
//        }
    }
}