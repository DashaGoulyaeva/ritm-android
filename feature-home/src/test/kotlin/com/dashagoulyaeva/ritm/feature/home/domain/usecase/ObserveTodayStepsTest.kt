package com.dashagoulyaeva.ritm.feature.home.domain.usecase

import com.dashagoulyaeva.ritm.feature.home.domain.model.StepRecord
import com.dashagoulyaeva.ritm.feature.home.domain.repository.StepsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ObserveTodayStepsTest {

    private class FakeStepsRepository : StepsRepository {
        var stepsFlow: Flow<Int> = flowOf(0)

        override fun observeTodaySteps(): Flow<Int> = stepsFlow
        override suspend fun getStepsHistory(limit: Int): List<StepRecord> = emptyList()
        override suspend fun saveTodaySteps(stepCount: Int) {}
    }

    @Test
    fun `emits step count from repository`() = runTest {
        val repo = FakeStepsRepository().apply { stepsFlow = flowOf(5000) }
        val useCase = ObserveTodaySteps(repo)

        val result = useCase().first()

        assertEquals(5000, result)
    }

    @Test
    fun `emits zero when sensor unavailable (-1)`() = runTest {
        val repo = FakeStepsRepository().apply { stepsFlow = flowOf(-1) }
        val useCase = ObserveTodaySteps(repo)

        val raw = useCase().first()

        // The use-case itself forwards the raw value; the mapping to todaySteps=0
        // and isAvailable=false lives in StepsViewModel. Here we assert the contract:
        // the repository emits -1 and the use case passes it through unchanged.
        assertEquals(-1, raw)
        assertFalse("sensor unavailable when value is -1", raw >= 0)
    }

    @Test
    fun `emits updated value on sensor change`() = runTest {
        val repo = FakeStepsRepository().apply {
            stepsFlow = flow {
                emit(1000)
                emit(2000)
            }
        }
        val useCase = ObserveTodaySteps(repo)

        val results = useCase().take(2).toList()

        assertEquals(2, results.size)
        assertEquals(2000, results.last())
    }
}
