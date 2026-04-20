package com.dashagoulyaeva.ritm.feature.water.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashagoulyaeva.ritm.feature.water.domain.model.WaterEntry
import com.dashagoulyaeva.ritm.feature.water.domain.usecase.GetWaterHistoryEntries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class WaterHistoryViewModel
    @Inject
    constructor(
        getWaterHistoryEntries: GetWaterHistoryEntries,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WaterHistoryUiState(isLoading = true))
        val uiState: StateFlow<WaterHistoryUiState> = _uiState.asStateFlow()

        init {
            getWaterHistoryEntries()
                .onEach { entries ->
                    _uiState.value =
                        WaterHistoryUiState(
                            days = entries.groupByDay(),
                            isLoading = false,
                            errorMessage = null,
                        )
                }
                .catch { error ->
                    _uiState.value =
                        WaterHistoryUiState(
                            isLoading = false,
                            errorMessage = error.message ?: "Не удалось загрузить историю воды",
                        )
                }
                .launchIn(viewModelScope)
        }

        private fun List<WaterEntry>.groupByDay(): List<WaterHistoryDayGroup> {
            val zoneId = ZoneId.systemDefault()
            return groupBy { entry ->
                Instant.ofEpochMilli(entry.timestamp).atZone(zoneId).toLocalDate()
            }
                .toList()
                .sortedByDescending { (date, _) -> date }
                .map { (date, entries) ->
                    WaterHistoryDayGroup(
                        date = date,
                        entries = entries.sortedByDescending { it.timestamp },
                    )
                }
        }
    }
