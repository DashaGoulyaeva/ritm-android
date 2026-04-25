package com.dashagoulyaeva.ritm.feature.cycle.domain.usecase

import com.dashagoulyaeva.ritm.feature.cycle.domain.repository.CycleRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

private const val RECENT_PERIODS_LIMIT = 7
private const val MIN_PERIODS_FOR_PREDICTION = 2

class PredictNextPeriod
    @Inject
    constructor(
        private val repository: CycleRepository,
    ) {
        /**
         * Returns predicted start date of next period as "yyyy-MM-dd", or null if not enough data.
         * Requires at least 2 completed periods to predict.
         */
        suspend operator fun invoke(): String? {
            val periods =
                repository.getRecentPeriods(RECENT_PERIODS_LIMIT)
                    .filter { it.endDate != null }
                    .sortedBy { it.startDate }

            if (periods.size < MIN_PERIODS_FOR_PREDICTION) return null

            // Calculate cycle lengths between consecutive periods
            val cycleLengths =
                periods.zipWithNext { a, b ->
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
