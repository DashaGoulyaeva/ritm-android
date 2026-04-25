package com.dashagoulyaeva.ritm.feature.home.data

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StepsSensorDataSource
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        /** Returns true if ACTIVITY_RECOGNITION permission is granted. */
        fun hasPermission(): Boolean =
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION,
            ) == PERMISSION_GRANTED

        /**
         * Returns a [Flow] that emits the cumulative step count from [Sensor.TYPE_STEP_COUNTER].
         * Emits -1 if the sensor is unavailable or the permission is not granted, then closes.
         */
        fun observeSteps(): Flow<Int> =
            callbackFlow {
                if (!hasPermission()) {
                    trySend(-1)
                    close()
                    return@callbackFlow
                }

                val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                val stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

                if (stepCounter == null) {
                    trySend(-1)
                    close()
                    return@callbackFlow
                }

                val listener =
                    object : SensorEventListener {
                        override fun onSensorChanged(event: SensorEvent) {
                            trySend(event.values[0].toInt())
                        }

                        override fun onAccuracyChanged(
                            sensor: Sensor,
                            accuracy: Int,
                        ) = Unit
                    }

                sensorManager.registerListener(
                    listener,
                    stepCounter,
                    SensorManager.SENSOR_DELAY_NORMAL,
                )

                awaitClose {
                    sensorManager.unregisterListener(listener)
                }
            }
    }
