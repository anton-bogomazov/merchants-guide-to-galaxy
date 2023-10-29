package com.abogomazov.merchant.guide.usecase.contracts

import com.abogomazov.merchant.guide.usecase.common.TranslationProvider
import com.abogomazov.merchant.guide.usecase.translator.TranslationPersister
import com.abogomazov.merchant.guide.usecase.translator.TranslationRemover

interface TranslationRepository : TranslationProvider, TranslationPersister, TranslationRemover
