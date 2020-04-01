package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ScannerManager(@Autowired private val scanners: List<Scanner>) {

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    private val log = LoggerFactory.getLogger(ScannerManager::class.java)

    fun execute(context: ScanContext) {
//        val WORKER_THREAD_POOL = Executors.newFixedThreadPool(4)


//        val callables: List<Callable<Unit>> = scanners.map { s ->
//            Callable {
//                try {
//                    s.scan(context)
//                } catch (e: Exception) {
//                    log.error("failed to scan {}", s.javaClass.simpleName, e)
//                }
//            }
//        }
//
//        WORKER_THREAD_POOL.invokeAll(callables)

        scanners.forEach {
            try {
                log.info("start to scan {}", it.javaClass.simpleName)
                it.scan(context)
            } catch (e: Exception) {
                log.error("failed to scan {}", it.javaClass.simpleName, e)
            }
        }
    }

    fun register() {
        val toRegister = scanners.map { it.toolList }.flatten().map { it.getConfigNames() }.flatten()
        val registered = configureRepository.getConfigures().map { it.getConfigNames() }.flatten()

        configureRepository.register(toRegister.filter { !registered.contains(it) })
        configureRepository.cleanRegistered(registered.filter { !toRegister.contains(it) })
    }
}