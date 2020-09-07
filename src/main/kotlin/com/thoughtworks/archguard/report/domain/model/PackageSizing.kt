package com.thoughtworks.archguard.report.domain.model

data class PackageSizing(val systemId: Long,
                         val moduleName: String? = null,
                         val packageName: String,
                         val classCount: Int = 0,
                         val lines: Int = 0)
