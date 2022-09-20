package com.persia.test.domain.use_case.validation

class ValidateNonEmptyField {

    operator fun invoke(value: Long?): ValidationResult {
        if (value == null) {
            return ValidationResult(false, "this field should not be empty")
        }
        return ValidationResult(true)
    }
}