package ger.girod.colorpalette.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import ger.girod.colorpalette.R

class ColorAdapter(val onRowLongPress: OnRowLongPress) : RecyclerView.Adapter<ColorRowHolder>() {

    var colorList : ArrayList<Palette.Swatch> = ArrayList()

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun onBindViewHolder(holder: ColorRowHolder, position: Int) {
        holder.populateContent(colorList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorRowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.color_row, parent, false)
        return ColorRowHolder(view, onRowLongPress)
    }

    fun setList(list : ArrayList<Palette.Swatch>) {
        colorList.clear()
        colorList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnRowLongPress {

        fun onRowLongPressed(hexColor : String)

    }
}