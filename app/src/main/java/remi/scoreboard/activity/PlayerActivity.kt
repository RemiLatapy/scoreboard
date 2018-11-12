package remi.scoreboard.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_player.*
import remi.scoreboard.R

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = (nav_host_fragment_player as NavHostFragment).navController

        val actionBarConfig = AppBarConfiguration(setOf())
        setupActionBarWithNavController(navController, actionBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (nav_host_fragment_player as NavHostFragment).navController.navigateUp()
    }
}
