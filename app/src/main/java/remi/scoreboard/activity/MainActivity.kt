package remi.scoreboard.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import remi.scoreboard.R
import remi.scoreboard.viewmodel.MainActivityViewModel

// TODO user merger to use is user finally log in and don't want to lose his local matches/players

class MainActivity : AppCompatActivity() {

    companion object {
        val navigateIntentFilter = "navigate-to-dest"
    }

    var fragmentDest = -1

    private val fragmentNavReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val dest = it.getIntExtra("dest", -1)
//                TODO Navigation using nav component cannot be done while activity is paused
//                https://stackoverflow.com/questions/51895243/ignoring-navigate-call-fragmentmanager-has-already-saved-its-state?noredirect=1&lq=1
                fragmentDest = dest
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment? ?: return
        val navController = host.navController

        val actionBarConfig = AppBarConfiguration(setOf(R.id.game_list_dest, R.id.stats_dest, R.id.user_dest))
        setupActionBarWithNavController(navController, actionBarConfig)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)

        showTempMatchRecoverDialogIfNeeded()

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(fragmentNavReceiver, IntentFilter(navigateIntentFilter))

        // TODO is it the right place to prepopulate DB -- (temp) disable offline mode
//        val gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
//        val reader = InputStreamReader(assets.open("games.json"))
//        var gameList: List<Game> = Gson().fromJson(reader, object : TypeToken<List<Game>>() {}.type)
//        gameViewModel.insert(gameList)
    }

    private fun showTempMatchRecoverDialogIfNeeded() {
        val viewmodel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        viewmodel.tempMatch.observe(this, Observer { match ->
            match?.let {
                AlertDialog.Builder(this).apply {
                    setTitle("Game in progress")
                    setMessage("Last game was not properly ended, do you want to continue or finish it?")
                    setPositiveButton("Continue") { _, _ ->
                        viewmodel.refreshUser() // TODO start activity on refresh finish
                        startActivity(Intent(context, PlayActivity::class.java))
                    }
                    setNegativeButton("Finish") { _, _ ->
                        viewmodel.saveAndDeleteTempMatch()
                    }
                    setCancelable(false)
                    show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (fragmentDest != -1) {
            findNavController(R.id.nav_host_fragment_main).navigate(fragmentDest)
            fragmentDest = -1
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment_main).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
// TODO this way lead to fragment stacking instead of replacing (= back button navigate back instead of exit)
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment_main))
                || super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fragmentNavReceiver)
    }
}