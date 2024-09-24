package com.dopaminedefense.dodiserver.block.repository

import com.dopaminedefense.dodiserver.block.entity.Block
import com.dopaminedefense.dodiserver.block.entity.QBlock.block
import com.dopaminedefense.dodiserver.system.dto.Category
import com.dopaminedefense.dodiserver.system.entity.Operation
import com.dopaminedefense.dodiserver.system.entity.QOperation.operation
import com.dopaminedefense.dodiserver.users.entity.QUsers.users
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository


@Repository
class BlockDslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : BlockDslRepository {

    override fun getWelcomeMessage(email: String, category: Category) : Operation {
        return queryFactory.select(operation)
            .from(operation)
            .leftJoin(users).on(users.email.eq(email))
            .where(operation.category.eq(category)
                .and(operation.languageCode.contains(users.countryCode.stringValue())))
            .orderBy(Expressions.numberTemplate(Double::class.java, "function('RAND')").asc())
            .limit(1)
            .fetchOne()!!
    }

    override fun getBlock(email: String, date: String) : Block? {
        return queryFactory.select(block)
            .from(block)
            .leftJoin(users).on(users.id.eq(block.user().id))
            .where(users.email.eq(email).and(block.localDate.contains(date)))
            .orderBy(block.createdDate.desc())
            .fetchOne()
    }

    override fun getBlockOfMonthByCalculatedIsFalse(email: String, date: String) : List<Block> {
        return queryFactory.select(block)
            .from(block)
            .leftJoin(users).on(users.id.eq(block.user().id))
            .where(users.email.eq(email).and(block.createdDate.contains(date)).and(block.calculated.eq(false)))
            .orderBy(block.createdDate.asc())
            .fetch()
    }

    override fun getBlockOfMonth(email: String, date: String): List<Block> {
        return queryFactory.select(block)
            .from(block)
            .leftJoin(users).on(users.id.eq(block.user().id))
            .where(users.email.eq(email).and(block.createdDate.contains(date)))
            .orderBy(block.createdDate.asc())
            .fetch()
    }

    override fun getBlockCountDifference(currentDate: String, previousDate: String): Double {
        val result = queryFactory
            .select(
                block.createdDate.substring(0, 7),
                block.blockCount.sum(),
                block.blockCount.count()
            )
            .from(block)
            .where(block.createdDate.contains(currentDate).or(block.createdDate.contains(previousDate)))
            .groupBy(block.createdDate.substring(0, 7))
            .fetch()

        val resultMap : Map<String, Double> = result.map { tuple ->
            val date = tuple.get(block.createdDate.substring(0, 7))!!
            val rate = tuple.get(block.blockCount.sum())!! / tuple.get(block.blockCount.count())!!

            date to String.format("%.2f", rate.toDouble()).toDouble()
        }.toMap()

        return (resultMap.get(currentDate) ?: 0.0) - (resultMap.get(previousDate) ?: 0.0)
    }


}