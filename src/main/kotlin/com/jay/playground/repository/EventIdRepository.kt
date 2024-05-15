package com.jay.playground.repository

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EventIdRepository : JpaRepository<EventId, Long>
