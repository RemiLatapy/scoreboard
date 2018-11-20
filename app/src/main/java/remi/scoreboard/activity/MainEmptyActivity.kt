package remi.scoreboard.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser


class MainEmptyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityIntent: Intent = if (ParseUser.getCurrentUser()?.isAuthenticated == true) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginSignupActivity::class.java)
        }

        startActivity(activityIntent)
        finish()
    }
}