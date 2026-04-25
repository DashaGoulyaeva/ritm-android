package com.dashagoulyaeva.ritm.feature.cycle.domain.usecase

import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CycleDayInfo
import com.dashagoulyaeva.ritm.feature.cycle.domain.model.CyclePhase
import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

private const val MENSTRUAL_PHASE_LAST_DAY = 5
private const val FOLLICULAR_PHASE_LAST_DAY = 13
private const val OVULATION_PHASE_LAST_DAY = 16
private const val LUTEAL_PHASE_LAST_DAY = 28

class GetCurrentCycleDay
    @Inject
    constructor(
        private val repository: CycleRepository,
    ) {
        operator fun invoke(): Flow<CycleDayInfo> =
            repository.getAllPeriods().map { periods ->
                val lastPeriod =
                    periods.maxByOrNull { it.startDate }
                        ?: return@map CycleDayInfo(0, CyclePhase.UNKNOWN, false)

                val today = LocalDate.now()
                val start = LocalDate.parse(lastPeriod.startDate)
                val dayOfCycle = (ChronoUnit.DAYS.between(start, today) + 1).toInt()
                val isInPeriod =
                    lastPeriod.endDate == null ||
                        today <= LocalDate.parse(lastPeriod.endDate!!)

                val phase =
                    when {
                        dayOfCycle <= MENSTRUAL_PHASE_LAST_DAY -> CyclePhase.MENSTRUAL
                        dayOfCycle <= FOLLICULAR_PHASE_LAST_DAY -> CyclePhase.FOLLICULAR
                        dayOfCycle <= OVULATION_PHASE_LAST_DAY -> CyclePhase.OVULATION
                        dayOfCycle <= LUTEAL_PHASE_LAST_DAY -> CyclePhase.LUTEAL
                        else -> CyclePhase.LUTEAL
                    }

                CycleDayInfo(dayOfCycle, phase, isInPeriod)
            }
    }
