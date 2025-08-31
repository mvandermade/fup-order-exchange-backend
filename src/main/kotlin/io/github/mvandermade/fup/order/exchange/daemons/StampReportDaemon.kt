package io.github.mvandermade.fup.order.exchange.daemons

import io.github.mvandermade.fup.order.exchange.providers.TransactionProvider
import io.github.mvandermade.fup.order.exchange.repositories.OrderStampRepository
import io.github.mvandermade.fup.order.exchange.repositories.StampReportRepository
import io.github.mvandermade.fup.order.exchange.repositories.StampRepository
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class StampReportDaemon(
    private val stampReportRepository: StampReportRepository,
    private val stampRepository: StampRepository,
    private val orderStampRepository: OrderStampRepository,
    private val transactionProvider: TransactionProvider,
) {
    @Scheduled(fixedDelay = 1_000, initialDelay = 2_000)
    fun findStampReportsAndDelete() {
        val page = Pageable.ofSize(50)
        val toDelete = stampReportRepository.findByDeletionIsDoneIsFalseAndComparisonIsErrorIsFalse(page)
        if (toDelete.isEmpty) return

        toDelete.forEach { stampReport ->
            val stamp = stampRepository.findByCode(stampReport.code)
            if (stamp == null) {
                stampReportRepository.save(stampReport.apply { deletionIsDone = true })
                return@forEach
            }

            val orderStampEntity = stamp.orderStampEntity
            if (orderStampEntity == null) {
                stampReportRepository.save(stampReport.apply { deletionIsDone = true })
                return@forEach
            }

            val orderStampTimestamp = orderStampEntity.createdAt
            if (orderStampTimestamp == null) {
                stampReportRepository.save(stampReport.apply { comparisonIsError = true })
                return@forEach
            }

            val createdAtTimeStamp = stampReport.createdAt
            if (createdAtTimeStamp == null) {
                stampReportRepository.save(stampReport.apply { comparisonIsError = true })
                return@forEach
            }

            if (orderStampTimestamp < createdAtTimeStamp) {
                stampReport.deletionIsDone = true
                transactionProvider.newReadWrite {
                    orderStampRepository.delete(orderStampEntity)
                    stampReportRepository.save(stampReport)
                }
            } else {
                // This could happen when the report daemon is behind
                stampReportRepository.save(stampReport.apply { comparisonIsError = true })
            }
        }
    }
}
