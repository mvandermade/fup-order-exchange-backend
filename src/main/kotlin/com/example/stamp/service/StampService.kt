package com.example.stamp.service

import com.example.stamp.domain.OrderDTO
import com.example.stamp.domain.StampResponse
import com.example.stamp.entity.Stamp
import com.example.stamp.mapper.StampMapper
import com.example.stamp.repository.OrderRepository
import com.example.stamp.repository.StampRepository
import org.springframework.stereotype.Service

@Service
class StampService(
    private val stampRepository: StampRepository,
    private val stampMapper: StampMapper,
    private val orderRepository: OrderRepository,
) {
    fun attemptStampCollection(orderDTO: OrderDTO): StampResponse? {
        var tries = 0
        while (tries < 10) {
            tries++
            val stamp = stampRepository.getFirstByOrderIdIsNull()
            if (stamp == null) {
                Thread.sleep(2000) // Time it takes to generate a new one
                continue
            }
            val claimedStamp = tryPopulate(stamp, orderDTO)
            if (claimedStamp == null) {
                Thread.sleep(2000)
                continue
            }
            return stampMapper.toResponse(claimedStamp)
        }
        return null
    }

    private fun tryPopulate(stamp: Stamp, orderDTO: OrderDTO): Stamp? {
        val order = orderRepository.getReferenceById(orderDTO.orderId)
        // Version 0 proves right to the postzegel
        stamp.version = 0
        stamp.order = order
        val newStampEntity = try {
            // Flush to expect DataIntegrityViolation
            stampRepository.saveAndFlush(stamp)
        } catch (e: Exception) {
            return null
        }
        return newStampEntity
    }
}
