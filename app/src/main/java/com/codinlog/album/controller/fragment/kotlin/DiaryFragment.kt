package com.codinlog.album.controller.fragment.kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.codinlog.album.R
import com.codinlog.album.application.AlbumApplication
import com.codinlog.album.controller.BaseFragmentController
import com.codinlog.album.controller.activity.kotlin.DiaryPublishActivity
import com.codinlog.album.databinding.DiaryFragmentBinding
import com.codinlog.album.model.kotlin.DiaryViewModel
import com.codinlog.album.network.kotlin.model.UserModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DiaryFragment : BaseFragmentController<DiaryViewModel, DiaryFragmentBinding>() {
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var ivHeader: ImageView
    private lateinit var tvName: TextView
    private lateinit var layout: ConstraintLayout
    private lateinit var rvBottom: RecyclerView
    private val handler = Handler()
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
        layout.setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.user_login,null)
            val etName = view.findViewById<EditText>(R.id.etName)
            val etPassword = view.findViewById<EditText>(R.id.etPassword)
            val builder = AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle(R.string.user_login)
                    .setView(view)
                    .setNegativeButton(R.string.btn_cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(R.string.btn_ok,null)
                    .create()
            builder.setOnShowListener {
                val button = builder.getButton(AlertDialog.BUTTON_POSITIVE)
                button.setOnClickListener {
                    val username = etName.text.toString()
                    val password = etPassword.text.toString()
                    viewModel.Login(UserModel(username,null,password,null))
                }
                etName.requestFocus()
                val inputManager = AlbumApplication.context
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                handler.postDelayed(Runnable { inputManager.showSoftInput(etName, 0) }, 200)
            }
            builder.show()
        }
        activity?.let {
            viewModel.user.observe(it, Observer { userInfo ->
                userInfo?.let {
                    tvName.text = userInfo.username
                    Toast.makeText(context,R.string.user_login_success,Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.floatBtn.setOnClickListener {
            val intent = Intent(context,DiaryPublishActivity::class.java)
            startActivity(intent)
        }
    }

    override fun doInitDisplayData() {
        viewModel.Login(null)
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return DiaryFragment()
        }
    }
}