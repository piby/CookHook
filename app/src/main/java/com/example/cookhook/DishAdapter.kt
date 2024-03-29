package com.example.cookhook

import android.graphics.Color
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View

class DishAdapter(
    private val dishList: ArrayList<String>,
    private val listener: ListActivity
        ) : RecyclerView.Adapter<DishAdapter.ViewHolder>() {

    // holder class that holds reference
    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = bindingAdapterPosition
            listener.onItemClick(position)
        }

        var textView: TextView = itemView.findViewById(R.id.dish_item_name) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dish_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dishList[position]

        // set different background for every other line
        //val color = if (position % 2 == 1) "#ffffffff" else "#ccccccff"
        //holder.textView.setBackgroundColor(Color.parseColor(color));
    }

    override fun getItemCount() = dishList.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}