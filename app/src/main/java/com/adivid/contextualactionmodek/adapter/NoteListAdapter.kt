package com.adivid.contextualactionmodek.adapter

import android.graphics.Color
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adivid.contextualactionmodek.R
import com.adivid.contextualactionmodek.model.Note


class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private val TAG = "NoteListAdapter"
    var onItemClick: ((Note, Int) -> Unit)? = null
    var onItemLongClick: ((Note, Int) -> Unit)? = null

    private var selectedItemIds: SparseBooleanArray = SparseBooleanArray()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBody: TextView = itemView.findViewById(R.id.textView)
        val card :CardView = itemView.findViewById(R.id.card)

        init {
            itemView.setOnClickListener {
                val noteItem = differ.currentList[adapterPosition]
                noteItem?.let {
                    onItemClick?.invoke(noteItem, adapterPosition)
                }
            }

            itemView.setOnLongClickListener {
                val noteItem = differ.currentList[adapterPosition]
                noteItem?.let {
                    onItemLongClick?.invoke(noteItem, adapterPosition)
                }
                return@setOnLongClickListener true
            }

        }
    }

    val diffCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.msg == newItem.msg
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Note>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = differ.currentList[position]
        holder.textViewBody.text = note.msg

        /*if(selectedItemIds.get(position)){
            holder.itemView.setBackgroundColor(Color.RED)
        }*/

        holder.itemView
            .setBackgroundColor(
                if (selectedItemIds.get(position)) {
                    Color.RED
                } else {
                    Color.TRANSPARENT
                }
            )

    }

    override fun getItemCount(): Int = differ.currentList.size

    //functions relating to sparse boolean array

    fun toggleSelection(position: Int) {

        Log.d(TAG, "toggleSelection: " + selectedItemIds.get(position))

        selectView(position, selectedItemIds.get(position))


    }

    fun removeSelection() {
        selectedItemIds = SparseBooleanArray()
        notifyDataSetChanged()
    }

    private fun selectView(position: Int, value: Boolean) {
        if (value) {
            Log.d(TAG, "selectView: inside if")
            //selectedItemIds.put(position, value)

            selectedItemIds.delete(position)
        } else {
            Log.d(TAG, "selectView: inside else")
            //selectedItemIds.delete(position)

            selectedItemIds.put(position, !value)
        }
        notifyDataSetChanged()
    }

    fun getSelectedCount(): Int {
        return selectedItemIds.size()
    }

    fun getSelectedIds(): SparseBooleanArray {
        return selectedItemIds
    }


}
