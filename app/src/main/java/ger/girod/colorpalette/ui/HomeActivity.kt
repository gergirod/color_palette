package ger.girod.colorpalette.ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import ger.girod.colorpalette.R
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        library_button.setOnClickListener {
            startActivity(PaletteActivity.getIntent(this, 1))
        }

        camera_button.setOnClickListener {
            startActivity(PaletteActivity.getIntent(this, 2))
        }
    }
}