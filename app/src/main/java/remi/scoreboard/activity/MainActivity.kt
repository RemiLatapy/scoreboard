package remi.scoreboard.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import remi.scoreboard.R
import remi.scoreboard.viewmodel.GameViewModel
import remi.scoreboard.viewmodel.UserViewModel

// TODO user merger to use is user finally log in and don't want to lose his local matches/players

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment? ?: return
        val navController = host.navController

        val actionBarConfig = AppBarConfiguration(setOf(R.id.game_list_dest, R.id.stats_dest, R.id.user_dest))
        setupActionBarWithNavController(navController, actionBarConfig)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)

        val gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        gameViewModel.updateGameList()

        val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.updateUser()

        // TODO is it the right place to prepopulate DB -- (temp) disable offline mode
//        val gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
//        val reader = InputStreamReader(assets.open("games.json"))
//        var gameList: List<Game> = Gson().fromJson(reader, object : TypeToken<List<Game>>() {}.type)
//        gameViewModel.insert(gameList)
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
}