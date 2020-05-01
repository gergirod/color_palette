package ger.girod.colorpalette

import android.view.View
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.color_row.view.*

class ColorRowHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun populateContent(swatch: Palette.Swatch) {
        itemView.row_background.setBackgroundColor(swatch.rgb)
        itemView.population_text.text = swatch.population.toString()
        itemView.population_text.setTextColor(swatch.titleTextColor)
        itemView.hex_color.text = Integer.toHexString(swatch.rgb)
        itemView.hex_color.setTextColor(swatch.bodyTextColor)
    }

}