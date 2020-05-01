package ger.girod.colorpalette

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView

class ColorAdapter : RecyclerView.Adapter<ColorRowHolder>() {

    var colorList : ArrayList<Palette.Swatch> = ArrayList()

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onBindViewHolder(holder: ColorRowHolder, position: Int) {
        holder.populateContent(colorList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorRowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.color_row, parent, false)
        return ColorRowHolder(view)
    }

    fun setList(list : ArrayList<Palette.Swatch>) {
        colorList.clear()
        colorList.addAll(list)
        notifyDataSetChanged()
    }
}