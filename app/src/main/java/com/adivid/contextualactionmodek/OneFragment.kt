package com.adivid.contextualactionmodek

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adivid.contextualactionmodek.adapter.NoteListAdapter
import com.adivid.contextualactionmodek.databinding.FragmentOneBinding
import com.adivid.contextualactionmodek.model.Note

class OneFragment : Fragment() {

    private val TAG = "OneFragment"
    private var _binding: FragmentOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteListAdapter: NoteListAdapter
    private var isSelectionMode = false

    private var noteList: List<Note> = mutableListOf(
        Note("abcd"),
        Note("pqrs"),
        Note("fdfs"),
        Note("sdfs"),
        Note("cvbc"),
        Note("erte"),
        Note("uiouo"),
        Note("mnmnf"),

        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        setUpList()

    }

    private fun setUpList() {
        val note = Note("abcd")
        noteList.toMutableList().add(note)
        val note1 = Note("abcd1")
        noteList.toMutableList().add(note1)
        val note2 = Note("abcd2")
        noteList.toMutableList().add(note2)
    }


    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            noteListAdapter = NoteListAdapter()
            adapter = noteListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        noteListAdapter.submitList(noteList)
        noteListAdapter.notifyDataSetChanged()

        binding.recyclerView.adapter?.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.recyclerView.smoothScrollToPosition(0)
            }
        })

        noteListAdapter.onItemClick = { _, i ->
            /*Log.d(TAG, "setUpRecyclerView: onClick $i")*/
            val hasCheckedItems = noteListAdapter.getSelectedCount() > 0

            if(hasCheckedItems && isSelectionMode){
                noteListAdapter.toggleSelection(i)
                if(noteListAdapter.getSelectedCount() > 0){
                    binding.tvHidden.text = noteListAdapter.getSelectedCount().toString()
                }else{
                    binding.tvHidden.isVisible = false
                    isSelectionMode = false
                }
            }else{
                // do other
            }
        }

        noteListAdapter.onItemLongClick = { _, i ->
            Log.d(TAG, "setUpRecyclerView: onLongClicked $i")

            isSelectionMode = true
            noteListAdapter.toggleSelection(i)

            val hasCheckedItems = noteListAdapter.getSelectedCount() > 0
            binding.tvHidden.isVisible = hasCheckedItems
            binding.tvHidden.text = noteListAdapter.getSelectedCount().toString()

        }
    }

    companion object
}