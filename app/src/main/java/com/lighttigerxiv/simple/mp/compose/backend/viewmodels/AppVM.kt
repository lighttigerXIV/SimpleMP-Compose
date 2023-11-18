package com.lighttigerxiv.simple.mp.compose.backend.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Artist
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import com.lighttigerxiv.simple.mp.compose.backend.utils.hasNotificationsPermission
import com.lighttigerxiv.simple.mp.compose.backend.utils.hasStoragePermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppVM(application: Application) : AndroidViewModel(application) {

}