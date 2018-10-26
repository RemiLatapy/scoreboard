package remi.scoreboard.jetpack.model

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
        entities = [Game::class, Match::class, Score::class, User::class],
        version = 1)
public abstract class ScoreboardDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao


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
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database -> scope.launch(Dispatchers.IO) {
                populateDatabase(database.userDao())
            }}
        }

        private fun populateDatabase(userDao: UserDao) {
            userDao.deleteAll()
            userDao.resetId()

            userDao.insert(User("User A"))
            userDao.insert(User("User B"))
        }
    }
}