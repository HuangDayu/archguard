package com.thoughtworks.archguard.evaluation.infrastructure

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.archguard.evaluation.domain.Dimension
import com.thoughtworks.archguard.evaluation.domain.EvaluationReport
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EvaluationRepository(@Autowired private val jdbi: Jdbi) {

    private val mapper = ObjectMapper()

    fun save(evaluationReport: EvaluationReport): String {
        val dimensions = mapper.writeValueAsString(evaluationReport.dimensions)
        val uuid = UUID.randomUUID().toString()
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("insert into evaluationReport(id, name, dimensions, comment, improvements, createdDate) " +
                    "values ('${uuid}', '${evaluationReport.name}', '${dimensions}'," +
                    " '${evaluationReport.comment}', '${evaluationReport.improvements.joinToString(",")}'," +
                    "'${evaluationReport.createdDate}')")
                    .execute()
        }
        return uuid
    }

    fun findAll(): List<EvaluationReport> {
        return jdbi.withHandle<List<EvaluationReport>, Nothing> {
            it
                    .createQuery("select id, name, dimensions, comment, improvements, createdDate from evaluationReport order by createdDate desc")
                    .map { rs, _ ->
                        EvaluationReport(rs.getString("id"),
                                rs.getTimestamp("createdDate").toLocalDateTime(),
                                rs.getString("name"),
                                mapper.readValue(rs.getString("dimensions"), object : TypeReference<List<Dimension>>() {}),
                                rs.getString("comment"),
                                rs.getString("improvements").split(","))
                    }.list()
        }

    }

    fun findById(id: String): EvaluationReport? {
        return jdbi.withHandle<EvaluationReport?, Nothing> {
            it
                    .createQuery("select id, name, dimensions, comment, improvements, createdDate from evaluationReport where id='${id}'")
                    .map { rs, _ ->
                        EvaluationReport(rs.getString("id"),
                                rs.getTimestamp("createdDate").toLocalDateTime(),
                                rs.getString("name"),
                                mapper.readValue(rs.getString("dimensions"), object : TypeReference<List<Dimension>>() {}),
                                rs.getString("comment"),
                                rs.getString("improvements").split(","))
                    }.firstOrNull()
        }
    }
}