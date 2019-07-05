package com.chyrta.mimi.artist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chyrta.mimi.R
import com.chyrta.mimi.util.addFragmentToActivity

class ArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val artistPermalink = intent.getStringExtra(EXTRA_ARTIST_PERMALINK)
        val artistName = intent.getStringExtra(EXTRA_ARTIST_NAME)

        supportActionBar?.title = artistName

        if (supportFragmentManager.findFragmentById(R.id.contentFrame) == null) {
            addFragmentToActivity(supportFragmentManager, ArtistFragment(artistPermalink), R.id.contentFrame)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ARTIST_PERMALINK = "ARTIST_PERMALINK"
        const val EXTRA_ARTIST_NAME = "ARTIST_NAME"
    }
}
