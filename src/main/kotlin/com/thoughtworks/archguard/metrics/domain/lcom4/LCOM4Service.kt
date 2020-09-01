package com.thoughtworks.archguard.metrics.domain.lcom4

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.method.domain.JMethodRepository
import com.thoughtworks.archguard.metrics.domain.ClassLCOM4
import com.thoughtworks.archguard.module.domain.graph.GraphStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LCOM4Service(val jClassRepository: JClassRepository,
                   val jMethodRepository: JMethodRepository,
                   val jClassLCOM4Repository: JClassLCOM4Repository) {

    fun calculateAllLCOM4(systemId: Long): List<ClassLCOM4> {
        val jClasses = jClassRepository.getJClassesHasModules(systemId)
        jClasses.forEach { prepareJClassBasicDataForLCOM4(systemId, it) }

        val classLCOM4List = mutableListOf<ClassLCOM4>()
        jClasses.forEach { classLCOM4List.add(ClassLCOM4(it.toVO(), getLCOM4Graph(it).getConnectivityCount())) }
        return classLCOM4List
    }

    private fun prepareJClassBasicDataForLCOM4(systemId: Long, jClass: JClass) {
        jClass.fields = jClassRepository.findFields(jClass.id)
        val methods = jMethodRepository.findMethodsByModuleAndClass(systemId, jClass.module, jClass.name)
        methods.forEach { it.fields = jMethodRepository.findMethodFields(it.id) }
        methods.forEach { it.callees = jMethodRepository.findMethodCallees(it.id) }
        jClass.methods = methods
    }

    fun getLCOM4Graph(jClass: JClass): GraphStore {
        val graphStore: GraphStore = GraphStore()
        val methods = jClass.methods
        methods.forEach { method ->
            method.fields.forEach { graphStore.addEdge(method.toVO(), it) }
            val methodsCallBySelfOtherMethod = method.callees.filter { jMethod -> methods.map { it.id }.contains(jMethod.id) }
            methodsCallBySelfOtherMethod.forEach { graphStore.addEdge(method.toVO(), it.toVO()) }
        }
        return graphStore
    }

    @Transactional
    fun getClassLCOM4ExceedThreshold(systemId: Long, threshold: Integer): List<ClassLCOM4> {
        return jClassLCOM4Repository.getClassLCOM4ExceedThreshold(systemId, threshold);
    }
}