package ger.girod.colorpalette

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_palette.*

const val SELECT_IMAGE_REQUEST = 1

class PaletteActivity : AppCompatActivity(), MultiplePermissionsListener {

    val adapter : ColorAdapter by lazy {
        ColorAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette)

        initList()
        checkPermissions()

    }

    fun initList() {

        color_list.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        color_list.layoutManager = layoutManager
        color_list.adapter = adapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val images = Matisse.obtainResult(data)
            showImage(images[0])
        }
    }

    private fun showImage(uri: Uri) {
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    image.setImageBitmap(resource)
                    getColorsfromPalette(getPalette(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })
    }

    private fun getColorsfromPalette(palette: Palette) {
        val newList = ArrayList<Palette.Swatch>(palette.swatches)

        newList.sortByDescending { it.population }

        val a : FloatArray = newList[0].hsl

        Log.e("mirar aca ", "mirar aca ${newList[0].rgb}")

        val h = a[0]*100
        val s = a[1]*100
        val l = a[2]*100

        val color = ColorUtils.HSLToColor(a);

        Log.e("mirar aca ","mirar aca "+color);

        adapter.setList(newList)
    }

    private fun getPalette(bitmap: Bitmap): Palette {
        return Palette.from(bitmap).generate()
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
        populateImageSelector()
    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: MutableList<PermissionRequest>?,
        token: PermissionToken?
    ) {

    }
}