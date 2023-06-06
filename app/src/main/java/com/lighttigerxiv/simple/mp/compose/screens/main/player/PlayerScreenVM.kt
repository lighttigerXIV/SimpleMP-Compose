package com.lighttigerxiv.simple.mp.compose.screens.main.player

import androidx.lifecycle.ViewModel
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayerScreenVM() : ViewModel() {

    //************************************************
    // Variables
    //************************************************

    var mainVM: MainVM? = null

    private val _showMenu = MutableStateFlow(false)
    val showMenu = _showMenu.asStateFlow()
    fun updateShowMenu(newValue:Boolean) {
        _showMenu.update { newValue }
    }

    private val _highlightSongTab = MutableStateFlow(true)
    val highlightSongTab = _highlightSongTab.asStateFlow()

    private val _highlightQueueTab = MutableStateFlow(false)
    val highlightQueueTab = _highlightQueueTab.asStateFlow()



    //************************************************
    // Functions
    //************************************************

    fun highlightTab(tab: String){

        if(tab == "song"){
            _highlightSongTab.update { true }
            _highlightQueueTab.update { false }
        }

        if(tab == "queue"){
            _highlightSongTab.update { false }
            _highlightQueueTab.update { true }
        }
    }

}