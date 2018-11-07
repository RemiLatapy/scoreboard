package remi.scoreboard.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseConfig
import com.parse.ParseUser


class MainEmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Parse ping test
        ParseConfig.getInBackground { config, e ->
            if (e != null) {
                Toast.makeText(applicationContext, "Parse connection failed: ${e.message}", Toast.LENGTH_LONG).show()
            } else {
                val ping = config.getInt("ping")
                if (ping == 73) Toast.makeText(applicationContext, "Parse connection OK!", Toast.LENGTH_LONG).show()
                else Toast.makeText(applicationContext, "Parse connection failed: wrong config", Toast.LENGTH_LONG).show()
            }
        }

        val activityIntent = if (ParseUser.getCurrentUser()?.isAuthenticated == true) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginSignupActivity::class.java)
        }

        startActivity(activityIntent)
        finish()
    }
}