package com.zeew.ui

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.zeew.util.Resource

@BindingAdapter("progressStatus")
fun bindProgressStatus(view: View, status: Resource<*>?) {
    status?.let {
        view.isVisible = it is Resource.Loading
    }
}

@BindingAdapter("idleStatus")
fun bindIdleStatus(view: View, status: Resource<*>?) {
    status?.let {
        view.isVisible = it is Resource.Success || it is Resource.Error
    }
}