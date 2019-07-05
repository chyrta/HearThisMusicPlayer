package com.chyrta.mimi.artists

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chyrta.mimi.R
import com.chyrta.mimi.util.addFragmentToActivity

class ArtistsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artists)

        supportActionBar?.setTitle(R.string.top_artists)

        if (supportFragmentManager.findFragmentById(R.id.contentFrame) == null) {
            addFragmentToActivity(supportFragmentManager, ArtistsFragment(), R.id.contentFrame)
        }
    }
}
