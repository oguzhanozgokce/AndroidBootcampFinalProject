package com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.MVI
import com.oguzhanozgokce.androidbootcampfinalproject.delegation.mvi
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiAction
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiEffect
import com.oguzhanozgokce.androidbootcampfinalproject.ui.gamesetup.GameSetupContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor() : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
        }
    }
}