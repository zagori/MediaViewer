package com.zagori.mediaviewer.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.zagori.mediaviewer.databinding.FragmentMediaBinding
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

private const val ARG_PARAM_LIST = "media_list"

class MediaFragment : DialogFragment() {
    private var medias: List<String>? = null

    private lateinit var binding: FragmentMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { medias = it.getStringArrayList(ARG_PARAM_LIST) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =  FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(MATCH_PARENT, MATCH_PARENT)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(medias: ArrayList<String>) = MediaFragment().apply {
            arguments = Bundle().apply {
                putStringArrayList(ARG_PARAM_LIST, medias)
            }
        }
    }
}