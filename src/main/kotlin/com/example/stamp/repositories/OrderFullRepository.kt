package com.example.stamp.repositories

import com.example.stamp.entities.Order
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface OrderFullRepository : JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = ["stamp"], type = EntityGraph.EntityGraphType.LOAD)
    override fun findById(id: Long): Optional<Order>
}
