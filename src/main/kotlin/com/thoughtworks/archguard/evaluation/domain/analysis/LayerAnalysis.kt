package com.thoughtworks.archguard.evaluation.domain.analysis

import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import org.springframework.stereotype.Service

@Service
class LayerAnalysis : Analysis {
    override fun getQualityReport(): Report? {
        return null
    }

    override fun getName(): String {
        return "分层架构"
    }

}
