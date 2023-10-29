package com.abogomazov.merchant.guide.rest

import arrow.core.Either
import com.abogomazov.merchant.guide.cli.getResourceError
import com.abogomazov.merchant.guide.cli.getResourceResult
import com.abogomazov.merchant.guide.cli.getTranslationError
import com.abogomazov.merchant.guide.cli.getTranslationResult
import com.abogomazov.merchant.guide.cli.setResourceError
import com.abogomazov.merchant.guide.cli.setResourceResult
import com.abogomazov.merchant.guide.cli.setTranslationError
import com.abogomazov.merchant.guide.cli.setTranslationResult
import com.abogomazov.merchant.guide.domain.amount
import com.abogomazov.merchant.guide.domain.credits
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceError
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import io.mockk.MockKAdditionalAnswerScope
import io.mockk.every
import io.mockk.mockk

typealias GetResourceResult = Either<GetResourceMarketPriceError, Credits>
typealias SetResourceResult = Either<SetResourceMarketPriceError, Unit>
typealias GetTranslationResult = Either<GetTranslationError, Amount>
typealias SetTranslationResult = Either<SetTranslationError, Unit>
typealias Stub<Usecase, Result> = (Usecase) -> MockKAdditionalAnswerScope<Result, Result>

fun mockAppServer(
    getResourcePriceStub: Stub<GetResourceMarketPriceUseCase, GetResourceResult>,
    setResourcePriceStub: Stub<SetResourceMarketPriceUseCase, SetResourceResult>,
    setTranslationStub: Stub<SetTranslationUseCase, SetTranslationResult>,
    getTranslationStub: Stub<GetTranslationUseCase, GetTranslationResult>,
): ApplicationServer {
    val getResourcePriceUseCaseMock = mockk<GetResourceMarketPriceUseCase>().also { getResourcePriceStub(it) }
    val setResourcePriceUseCaseMock = mockk<SetResourceMarketPriceUseCase>().also { setResourcePriceStub(it) }
    val getTranslationUseCaseMock = mockk<GetTranslationUseCase>().also { getTranslationStub(it) }
    val setTranslationUseCaseMock = mockk<SetTranslationUseCase>().also { setTranslationStub(it) }

    return ApplicationServer(
        setTranslationUseCase = setTranslationUseCaseMock,
        getTranslationUseCase = getTranslationUseCaseMock,
        setResourcePriceUseCase = setResourcePriceUseCaseMock,
        getResourcePriceUseCase = getResourcePriceUseCaseMock,
    )
}

fun errorAppServer() =
    mockAppServer(
        getResourcePriceStub = {
            every { it.execute(any(), any()) } returns
                    getResourceError(GetResourceMarketPriceError.PriceNotFound)
        },
        setResourcePriceStub = {
            every { it.execute(any(), any(), any()) } returns
                    setResourceError(SetResourceMarketPriceError.RomanNotationRulesViolated)
        },
        getTranslationStub = {
            every { it.execute(any()) } returns
                    getTranslationError(GetTranslationError.RomanNotationRulesViolated)
        },
        setTranslationStub = {
            every { it.execute(any(), any()) } returns
                    setTranslationError(SetTranslationError.GalaxyNumeralAlreadyAssociated(RomanNumeral.D))
        }
    )

fun resultAppServer() =
    mockAppServer(
        getResourcePriceStub = { every { it.execute(any(), any()) } returns getResourceResult(credits(34)) },
        setResourcePriceStub = { every { it.execute(any(), any(), any()) } returns setResourceResult() },
        getTranslationStub = { every { it.execute(any()) } returns getTranslationResult(amount(10)) },
        setTranslationStub = { every { it.execute(any(), any()) } returns setTranslationResult() }
    )
