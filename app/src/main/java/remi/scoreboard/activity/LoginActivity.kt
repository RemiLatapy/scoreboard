package remi.scoreboard.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseACL
import com.parse.ParseUser
import remi.scoreboard.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupSkip()
        setupSignup()
        setupLogging()
    }

    private fun setupSkip() {
        findViewById<Button>(R.id.skip_btn)?.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
            finish()
        }
    }

    private fun setupSignup() {

        findViewById<Button>(R.id.signup_btn)?.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val pwd = findViewById<EditText>(R.id.password).text.toString()
            val user = ParseUser()
            user.username = username
            user.email = email
            user.setPassword(pwd)
            user.signUpInBackground { e ->
                if (e == null) {
                    val newUser = ParseUser.getCurrentUser()
                    newUser.acl = ParseACL(user)
                    newUser.saveInBackground()

                    Toast.makeText(
                        this,
                        "Signup succeed for ${ParseUser.getCurrentUser().username}",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else
                    Toast.makeText(this, "Signup failed, ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupLogging() {
        findViewById<Button>(R.id.login_btn)?.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val pwd = findViewById<EditText>(R.id.password).text.toString()
            ParseUser.logInInBackground(username, pwd) { loggedUser, e ->
                if (loggedUser != null) {
                    Toast.makeText(
                        this,
                        "Logging succeed for ${ParseUser.getCurrentUser().username}",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else
                    Toast.makeText(this, "Logging failed, ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
