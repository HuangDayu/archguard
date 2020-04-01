package com.thoughtworks.archgard.scanner.domain.scanner.dependencies

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.JavaByteCodeTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JavaDependencyScanner(@Value("spring.datasource.url") val dbUrl: String) : Scanner {
    private val log = LoggerFactory.getLogger(JavaDependencyScanner::class.java)
    override fun toolListGenerator(): List<ToolConfigure> {
        val result = ArrayList<ToolConfigure>()
        val config = HashMap<String, String>()
        config["available"] = "false"
        result.add(ToolConfigure("JavaDependency", config))
        return result
    }

    override fun scan(context: ScanContext) {
        log.info("start scan java dependency")
        val javaByteCodeTool = JavaByteCodeTool(context.workspace, dbUrl)
        javaByteCodeTool.getDependencyReport()
        log.info("finished scan java dependency")
    }

}