package com.thoughtworks.archguard.report.domain.redundancy

import com.thoughtworks.archguard.report.domain.module.ClassVO

interface RedundancyRepository {
    fun getOneMethodClassCount(systemId: Long,  limit: Long, offset: Long): Long
    fun getOneMethodClass(systemId: Long, limit: Long, offset: Long): List<ClassVO>

    fun getOneFieldClassCount(systemId: Long,  limit: Long, offset: Long): Long
    fun getOneFieldClass(systemId: Long, limit: Long, offset: Long): List<ClassVO>
}