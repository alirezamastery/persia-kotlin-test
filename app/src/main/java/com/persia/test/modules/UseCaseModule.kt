package com.persia.test.modules

import com.persia.test.domain.use_case.validation.ValidateNonEmptyField
import com.persia.test.domain.use_case.validation.ValidateNonEmptyNumberField
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideNonEmptyFieldValidator(): ValidateNonEmptyField {
        return ValidateNonEmptyField()
    }

    @Singleton
    @Provides
    fun provideNonEmptyNumberFieldValidator(): ValidateNonEmptyNumberField {
        return ValidateNonEmptyNumberField()
    }
}