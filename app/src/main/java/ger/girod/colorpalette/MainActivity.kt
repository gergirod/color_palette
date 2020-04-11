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
import androidx.palette.graphics.Palette
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

const val SELECT_IMAGE_REQUEST = 1
class MainActivity : AppCompatActivity(), MultiplePermissionsListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SELECT_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val images = Matisse.obtainResult(data)
           showImage(images[0])
        }
    }

    private fun showImage(uri : Uri ){
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    //image.setImageBitmap(resource)
                    val  palette = Palette.from(resource).generate()

                    image.setImageURI(uri)
                    val lighVibrant = palette.getLightVibrantColor(0)
                    val vibrant = palette.getVibrantColor(0)
                    val darkVibrant = palette.getDarkVibrantColor(0)
                    val lightMuted = palette.getLightMutedColor(0)
                    val muted = palette.getMutedColor(0)
                    val darkMuted = palette.getDarkMutedColor(0)


                    Log.e("mirar aca ","mirar aca $lighVibrant")
                    Log.e("mirar aca ","mirar aca $vibrant")
                    Log.e("mirar aca ","mirar aca $darkVibrant")
                    Log.e("mirar aca ","mirar aca $lightMuted")
                    Log.e("mirar aca ","mirar aca $muted")
                    Log.e("mirar aca ","mirar aca $darkMuted")

                    color_view_one.setBackgroundColor(lighVibrant)
                    color_view_two.setBackgroundColor(vibrant)
                    color_view_three.setBackgroundColor(darkVibrant)
                    color_view_four.setBackgroundColor(lightMuted)
                    color_view_five.setBackgroundColor(muted)
                    color_view_six.setBackgroundColor(darkMuted)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
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

    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?,
                                                    token: PermissionToken?) {

    }

        /*image.load("https://cdn.britannica.com/76/124976-050-E03E50CE/Diego-Maradona-1986.jpg") {
            transformations(object  : Transformation {
                override fun key() = "PaletteTransformer"

                override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
                    val  palette = Palette.from(input).generate()


                    val lighVibrant = palette.getLightVibrantColor(0)
                    val vibrant = palette.getVibrantColor(0)
                    val darkVibrant = palette.getDarkVibrantColor(0)
                    val lightMuted = palette.getLightMutedColor(0)
                    val muted = palette.getMutedColor(0)
                    val darkMuted = palette.getDarkMutedColor(0)


                    Log.e("mirar aca ","mirar aca $lighVibrant")
                    Log.e("mirar aca ","mirar aca $vibrant")
                    Log.e("mirar aca ","mirar aca $darkVibrant")
                    Log.e("mirar aca ","mirar aca $lightMuted")
                    Log.e("mirar aca ","mirar aca $muted")
                    Log.e("mirar aca ","mirar aca $darkMuted")

                    color_view.setBackgroundColor(darkMuted)
                    /*launch {
                        color_view.setBackgroundColor(darkMuted)
                    }*/
                    return input
                }

            })
        }

    }*/
}