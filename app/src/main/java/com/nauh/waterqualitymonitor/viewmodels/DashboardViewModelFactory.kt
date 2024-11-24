package com.nauh.waterqualitymonitor.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nauh.waterqualitymonitor.utils.DataSaver

class DashboardViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(DataSaver(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}