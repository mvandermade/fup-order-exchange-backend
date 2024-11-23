package com.example.stamp.service

import com.example.stamp.domain.Order
import com.example.stamp.entity.StampEntity
import com.example.stamp.provider.UUIDProvider
import com.example.stamp.repository.StampRepository
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val stampRepository: StampRepository,
    private val uuidProvider: UUIDProvider,
) {
    fun genAndCoupleOrder(): Order? {
        var postzegelOrder: Order? = null

        var didNotSucceed = 0

        while (didNotSucceed < 10) {
            val pendingEntity = stampRepository.getFirstByOrderIdIsNull()

            pendingEntity.ifPresent { postzegelOrder = genAndCoupleOrder(it) }

            if (null == postzegelOrder) {
                didNotSucceed++
                Thread.sleep(2000) // Time it takes to generate a new one
            } else {
                return postzegelOrder
            }
        }

        return null
    }

    private fun genAndCoupleOrder(stampEntity: StampEntity): Order? {
        val versionShouldBe1 = stampEntity.version
        if (versionShouldBe1 == null || versionShouldBe1 != 0L) {
            return null
        }
        // Version +1 "proves" right to the postzegel
        stampEntity.version = stampEntity.version?.plus(1)
        val orderId = uuidProvider.getUUID().toString()

        stampEntity.orderId = orderId
        stampRepository.saveAndFlush(stampEntity)

        return Order(stampEntity.code, orderId)
    }

    fun getOrderFor(orderId: String): Order {
        var postzegel: String? = null

        stampRepository.getByOrderIdEquals(orderId).ifPresent { postzegel = it.code }

        return if (null == postzegel) {
            throw NotFoundException()
        } else {
            Order(postzegel as String, orderId)
        }
    }
}
