package com.codinlog.album.controller.Fragment.kotlin

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.codinlog.album.R
import com.codinlog.album.controller.BaseFragmentController
import com.codinlog.album.databinding.DiaryFragmentBinding
import com.codinlog.album.model.kotlin.DiaryViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DiaryFragment : BaseFragmentController<DiaryViewModel, DiaryFragmentBinding>() {
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var ivHeader: ImageView
    private lateinit var tvName: TextView
    private lateinit var layout: ConstraintLayout
    private lateinit var rvBottom: RecyclerView
    override fun getLayoutId(): Int {
        return R.layout.diary_fragment
    }

    override fun doInitViewData() {
        viewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        ivHeader = binding.root.findViewById(R.id.ivHeader)
        tvName = binding.root.findViewById(R.id.tvName)
        rvBottom = binding.root.findViewById(R.id.rvBottom)
        layout = binding.root.findViewById(R.id.layout)
    }

    override fun doInitListener() {
        layout.setOnClickListener { Toast.makeText(context, "click", Toast.LENGTH_SHORT).show() }
    }

    override fun doInitDisplayData() {

    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return DiaryFragment()
        }
    }
}