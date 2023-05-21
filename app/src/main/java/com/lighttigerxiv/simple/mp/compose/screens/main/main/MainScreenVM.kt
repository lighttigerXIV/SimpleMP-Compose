package com.lighttigerxiv.simple.mp.compose.screens.main.main

import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainScreenVM : ViewModel() {

    private val _navbarOffset = MutableStateFlow(0.dp)
    val navbarOffset = _navbarOffset.asStateFlow()

    private var lastProgressFraction = 0f


    @OptIn(ExperimentalMaterialApi::class)
    fun changeNavbarOffset(progressFraction: Float, targetValue: BottomSheetValue) {

        if(progressFraction != lastProgressFraction){

            lastProgressFraction = progressFraction

            if (progressFraction in 0.2f..1f) {

                if (targetValue == BottomSheetValue.Expanded) {

                    _navbarOffset.update { (0 + (55 * progressFraction)).dp }

                } else {

                    _navbarOffset.update { (55 - (55 * progressFraction)).dp }
                }
            }
        }
    }
}