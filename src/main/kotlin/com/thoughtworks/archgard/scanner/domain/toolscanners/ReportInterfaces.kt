package com.thoughtworks.archgard.scanner.domain.toolscanners

interface BadSmellReport {
    fun getBadSmellReport(): String
}

interface TestBadSmellReport {
    fun getTestBadSmellReport(): String
}