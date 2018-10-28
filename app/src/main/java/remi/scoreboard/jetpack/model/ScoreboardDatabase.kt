package remi.scoreboard.jetpack.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
        entities = [Game::class, Match::class, User::class],
        version = 1)
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
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.userDao(), database.gameDao(), database.matchDao())
                }
            }
        }

        // TODO this should be a unit test (?)
        private fun populateDatabase(userDao: UserDao, gameDao: GameDao, matchDao: MatchDao) {
            userDao.deleteAll()
            gameDao.deleteAll()
            matchDao.deleteAll()

            userDao.resetId() // TODO Not working

            val userA = User("User A")
            val userB = User("User B")

            val gameUno = Game("Uno", "file:///android_asset/thumbnail/uno.png", listOf("Rules 1 Uno", "Rules 2 Uno"))
            val gameScopa = Game("Scopa", "file:///android_asset/thumbnail/scopa.png", listOf("Rules 1 Scopa", "Rules 2 Scopa"))

//            val scoreA: Score = mapOf(Pair("score", 5))
//            val scoreB: Score = mapOf(Pair("phase", 9), Pair("points", 85))
//            val playerScoreList: MutablePlayerScoreList = ArrayList()
//            playerScoreList.add(Pair(userA.uid, scoreA))
//            playerScoreList.add(Pair(userB.uid, scoreB))
//            val match = Match(gameA.gid, playerScoreList)

            userDao.insert(userA)
            userDao.insert(userB)

//            val userList = userDao.loadAllUsers()
//            Log.d("DB", "userList = ${userList.value}")

            gameDao.insert(gameUno)
            gameDao.insert(gameScopa)

//            matchDao.insert(match)
        }
    }
}