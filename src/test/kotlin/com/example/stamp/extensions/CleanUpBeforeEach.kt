package com.example.stamp.extensions

import com.example.stamp.repositories.OrderRepository
import com.example.stamp.repositories.OrderStampRepository
import com.example.stamp.repositories.StampReportRepository
import com.example.stamp.repositories.StampRepository
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class CleanUpBeforeEach : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        val orderStampRepository = SpringExtension.getApplicationContext(context).getBean(OrderStampRepository::class.java)
        val stampReportRepository = SpringExtension.getApplicationContext(context).getBean(StampReportRepository::class.java)
        val orderRepository = SpringExtension.getApplicationContext(context).getBean(OrderRepository::class.java)
        val stampRepository = SpringExtension.getApplicationContext(context).getBean(StampRepository::class.java)

        // Using deleteAllInBatch here because of errors: org.hibernate.TransientObjectException persistent instance references an unsaved transient instance
        // First the FK relations gone...
        stampReportRepository.deleteAllInBatch()
        orderStampRepository.deleteAllInBatch()

        // Then the entities:
        orderRepository.deleteAllInBatch()
        stampRepository.deleteAllInBatch()
    }
}
