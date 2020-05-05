package ger.girod.colorpalette.utils

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

object PaletteUtils {

    fun getPalette(bitmap: Bitmap) : Palette {
        return Palette.from(bitmap).generate()
    }

    fun rgbToHex(rgb : Int) : String {
        return "#${Integer.toHexString(rgb)}"
    }

}