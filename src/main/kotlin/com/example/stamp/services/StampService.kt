package com.example.stamp.services

import com.example.stamp.entities.StampEntity
import com.example.stamp.providers.RandomProvider
import com.example.stamp.repositories.StampRepository
import org.springframework.stereotype.Service

@Service
class StampService(
    private val stampRepository: StampRepository,
    private val randomProvider: RandomProvider,
) {
    fun persistRandomStamp() {
        val code = randomProvider.randomString(1)

        // Prevent unneeded attempts cluttering the logs
        if (stampRepository.findByCode(code) != null) return

        val entity =
            StampEntity(
                code = code,
            )
        stampRepository.save(entity)
    }
}
