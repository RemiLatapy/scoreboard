package remi.scoreboard.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_play.*
import remi.scoreboard.R

class PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = (nav_host_fragment_play as NavHostFragment).navController

        val actionBarConfig = AppBarConfiguration(setOf())
        setupActionBarWithNavController(navController, actionBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!Navigation.findNavController(this, R.id.nav_host_fragment_play).navigateUp())
            onBackPressed()
        return true
    }
}
