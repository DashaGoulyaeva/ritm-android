package com.dashagoulyaeva.ritm.feature.cycle.domain.usecase

import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class PredictNextPeriod @Inject constructor(
    private val repository: CycleRepository,
) {
    /**
     * Returns predicted start date of next period as "yyyy-MM-dd", or null if not enough data.
     * Requires at least 2 completed periods to predict.
     */
    suspend operator fun invoke(): String? {
        val periods = repository.getRecentPeriods(7)
            .filter { it.endDate != null }
            .sortedBy { it.startDate }

        if (periods.size < 2) return null

        // Calculate cycle lengths between consecutive periods
        val cycleLengths = periods.zipWithNext { a, b ->
            ChronoUnit.DAYS.between(
                LocalDate.parse(a.startDate),
                LocalDate.parse(b.startDate),
            )
        }

        val avgLength = cycleLengths.average().toLong()
        val lastStart = LocalDate.parse(periods.last().startDate)
        return lastStart.plusDays(avgLength).toString()
    }
}
