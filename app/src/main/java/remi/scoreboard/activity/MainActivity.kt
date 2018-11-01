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
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import remi.scoreboard.R
import remi.scoreboard.data.Game
import remi.scoreboard.viewmodel.GameViewModel
import java.io.InputStreamReader


class MainActivity : AppCompatActivity(), TempToolbarTitleListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController

        setupActionBarWithNavController(navController)

        // TODO is it the right place to prepopulate DB
        val gameViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        val reader = InputStreamReader(assets.open("games.json"))
        var gameList: List<Game> = Gson().fromJson(reader, object : TypeToken<List<Game>>() {}.type)
        gameViewModel.insert(gameList)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    // https://stackoverflow.com/questions/50599238/dynamic-actionbar-title-from-a-fragment-using-androidx-navigation
    override fun updateTitle(title: String) {
        findViewById<Toolbar>(R.id.toolbar).title = title
    }
}

interface TempToolbarTitleListener {
    fun updateTitle(title: String)
}