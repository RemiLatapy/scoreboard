package remi.scoreboard.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_play.*
import remi.scoreboard.R
import remi.scoreboard.viewmodel.GamePlayViewModel

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

    override fun onBackPressed() {
        ViewModelProviders.of(this).get(GamePlayViewModel::class.java).showConfirmExitDialog(this)
    }
}
