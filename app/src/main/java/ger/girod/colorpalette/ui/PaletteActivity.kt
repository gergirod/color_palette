package ger.girod.colorpalette.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import ger.girod.colorpalette.R
import ger.girod.colorpalette.utils.PaletteUtils
import kotlinx.android.synthetic.main.activity_palette.*

const val SELECT_IMAGE_REQUEST = 1
const val REQUEST_IMAGE_CAPTURE = 2

class PaletteActivity : AppCompatActivity(), MultiplePermissionsListener,
    ColorAdapter.OnRowLongPress {

    private var type: Int = 0

    companion object {

        fun getIntent(context: Context, type: Int): Intent {
            return Intent(context, PaletteActivity::class.java).apply {
                putExtra("action_type", type)
            }
        }
    }

    private val colorAdapter: ColorAdapter by lazy {
        ColorAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        type = intent.getIntExtra("action_type", -1)

        initList()
        checkPermissions()

    }

    private fun initList() {
        val listLayoutManager = LinearLayoutManager(this)

        color_list.apply {
            adapter = colorAdapter
            layoutManager = listLayoutManager
            setHasFixedSize(true)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED -> finish()
            Activity.RESULT_OK -> {
                when (requestCode) {
                    SELECT_IMAGE_REQUEST -> {
                        val images = Matisse.obtainResult(data)
                        showImage(images[0])
                    }
                    REQUEST_IMAGE_CAPTURE -> {
                        val image = data?.extras?.get("data") as Bitmap
                        showCameraImage(image)
                    }
                }
            }

        }
    }

    private fun showCameraImage(bitmap: Bitmap) {

        Glide.with(this)
            .asBitmap()
            .load(bitmap)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    image.setImageBitmap(resource)
                    setPaletteColorList(getColorsFromPalette(PaletteUtils.getPalette(resource)))
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })
    }

    fun setPaletteColorList(list : List<Palette.Swatch>) {
        val newList = ArrayList<Palette.Swatch>(list)
        newList.sortByDescending { it.population }
        colorAdapter.setList(newList)
    }

    private fun showImage(uri: Uri) {
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    image.setImageBitmap(resource)
                    setPaletteColorList(getColorsFromPalette(PaletteUtils.getPalette(resource)))
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })
    }

    private fun getColorsFromPalette(palette: Palette) : List<Palette.Swatch> {
        return  palette.swatches
    }

    private fun checkPermissions() {
        val permissions = listOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        Dexter.withActivity(this)
            .withPermissions(permissions)
            .withListener(this)
            .check()
    }

    private fun populateCameraApp() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun populateImageSelector() {
        Matisse.from(this)
            .choose(MimeType.ofImage())
            .countable(true)
            .maxSelectable(9)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showPreview(false) // Default is `true`
            .forResult(1)
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        when (type) {
            1 -> populateImageSelector()
            2 -> populateCameraApp()
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: MutableList<PermissionRequest>?, token: PermissionToken
    ) {
    }

    override fun onRowLongPressed(hexColor: String) {
        Toast.makeText(this, "$hexColor color added to the clipBoard", Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}