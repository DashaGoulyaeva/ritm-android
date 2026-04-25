package com.dashagoulyaeva.ritm.feature.cycle.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayLog
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.FlowIntensity
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.MoodLevel
import com.dashagoulyaeva.ritm.feature.cycle.domain.usecase.GetCycleDayLog
import com.dashagoulyaeva.ritm.feature.cycle.domain.usecase.SaveCycleDayLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getCycleDayLog: GetCycleDayLog,
        private val saveCycleDayLog: SaveCycleDayLog,
    ) : ViewModel() {
        private val date: String = checkNotNull(savedStateHandle["date"])
        private val _uiState = MutableStateFlow(JournalUiState(date = date))
        val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                val existing = getCycleDayLog(date)
                _uiState.value =
                    if (existing != null) {
                        _uiState.value.copy(
                            flow = existing.flow,
                            mood = existing.mood,
                            symptoms = existing.symptoms.toSet(),
                            note = existing.note,
                            isLoading = false,
                        )
                    } else {
                        _uiState.value.copy(isLoading = false)
                    }
            }
        }

        fun setFlow(flow: FlowIntensity) {
            _uiState.value = _uiState.value.copy(flow = flow)
        }

        fun setMood(mood: MoodLevel) {
            _uiState.value = _uiState.value.copy(mood = mood)
        }

        fun toggleSymptom(key: String) {
            val s = _uiState.value.symptoms.toMutableSet()
            if (s.contains(key)) s.remove(key) else s.add(key)
            _uiState.value = _uiState.value.copy(symptoms = s)
        }

        fun setNote(note: String) {
            _uiState.value = _uiState.value.copy(note = note)
        }

        fun save() {
            viewModelScope.launch {
                saveCycleDayLog(
                    CycleDayLog(
                        date = date,
                        flow = _uiState.value.flow,
                        mood = _uiState.value.mood,
                        symptoms = _uiState.value.symptoms.toList(),
                        note = _uiState.value.note,
                    ),
                )
                _uiState.value = _uiState.value.copy(isSaved = true)
            }
        }
    }
