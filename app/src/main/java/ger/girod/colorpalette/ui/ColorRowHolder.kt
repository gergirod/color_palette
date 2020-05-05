package ger.girod.colorpalette.ui

import android.view.View
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import ger.girod.colorpalette.utils.PaletteUtils
import kotlinx.android.synthetic.main.color_row.view.*
import android.content.ClipData
import android.content.Context.CLIPBOARD_SERVICE
import androidx.core.content.ContextCompat.getSystemService



class ColorRowHolder(itemView : View, private val onRowLongPress: ColorAdapter.OnRowLongPress) : RecyclerView.ViewHolder(itemView) {

    fun populateContent(swatch: Palette.Swatch) {
        itemView.row_background.setBackgroundColor(swatch.rgb)
        itemView.population_text.text = swatch.population.toString()
        itemView.population_text.setTextColor(swatch.titleTextColor)
        var color : String = PaletteUtils.rgbToHex(swatch.rgb)
        itemView.hex_color.text = color
        itemView.hex_color.setTextColor(swatch.bodyTextColor)

        itemView.setOnClickListener {
            onRowLongPress.onRowLongPressed(color)
        }
    }

}