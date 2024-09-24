package com.dopaminedefense.dodiserver.system.service.Impl

import com.dopaminedefense.dodiserver.system.dto.Category
import com.dopaminedefense.dodiserver.system.dto.Language
import com.dopaminedefense.dodiserver.system.dto.OperationReq
import com.dopaminedefense.dodiserver.system.dto.OperationRes
import com.dopaminedefense.dodiserver.system.entity.Operation
import com.dopaminedefense.dodiserver.system.repository.OperationRepository
import com.dopaminedefense.dodiserver.system.service.OperationService
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class OperationServiceImpl (
    val operationRepository: OperationRepository
) : OperationService {

    @PostConstruct
    fun init() {
        operationRepository.findByContextAndLanguageCode("https://forms.gle/eHsj8ffZ9WeYogWr5", "KR") ?:
        operationRepository.save(Operation(
            type = "interview",
            context = "https://forms.gle/eHsj8ffZ9WeYogWr5",
            category = Category.LINK,
            languageCode = "KR"
        ))

        operationRepository.findByContextAndLanguageCode("{{minutes}}분 휴식하면\\n블록 {{blocks}}개 완성 가능!", "KR") ?:
        operationRepository.save(Operation(
            type = "promote-action",
            context = "{{minutes}}분 휴식하면\\n블록 {{blocks}}개 완성 가능!",
            category = Category.WELCOME_MSG,
            languageCode = "KR"
        ))

        operationRepository.findByContextAndLanguageCode("어제보다 스크린타임\\n{{percentage}}% {{delta}}어요!", "KR") ?:
        operationRepository.save(Operation(
            type = "compare-to-yesterday",
            context = "어제보다 스크린타임\\n{{percentage}}% {{delta}}어요!",
            category = Category.WELCOME_MSG,
            languageCode = "KR"
        ))

        operationRepository.findByContextAndLanguageCode("https://docs.google.com/forms/d/1O0JU5mHxZvQJbhl_uTrh2tsVHowvgqUXi4SxsijklnA/edit", "US,DE,FR") ?:
        operationRepository.save(Operation(
            type = "interview",
            context = "https://docs.google.com/forms/d/1O0JU5mHxZvQJbhl_uTrh2tsVHowvgqUXi4SxsijklnA/edit",
            category = Category.LINK,
            languageCode = "US,DE,FR"
        ))

        operationRepository.findByContextAndLanguageCode("Screen time\\n{{percentage}}% {{delta}}!", "US,DE,FR") ?:
        operationRepository.save(Operation(
            type = "compare-to-yesterday",
            context = "Screen time\\n{{percentage}}% {{delta}}!",
            category = Category.WELCOME_MSG,
            languageCode = "US,DE,FR"
        ))

        operationRepository.findByContextAndLanguageCode("{{minutes}}-minute break,\\n{{blocks}} blocks done!", "US,DE,FR") ?:
        operationRepository.save(Operation(
            type = "promote-action",
            context = "{{minutes}}-minute break,\\n{{blocks}} blocks done!",
            category = Category.WELCOME_MSG,
            languageCode = "US,DE,FR"
        ))
    }

    override fun saveOperation(operationReq: OperationReq) : OperationRes {
        val operation = operationRepository.save(operationReq.toEntity())
        return OperationRes(
            type = operation.type,
            context = operation.context,
            category = operation.category,
            languageCode = operation.languageCode
        )
    }


}