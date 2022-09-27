package com.persia.test.domain.use_case.validation

class ValidateNonEmptyNumberField {

    operator fun invoke(value: String?): ValidationResult {
        println("invoke: $value")

        if (value == null || value.isBlank()) {
            return ValidationResult(false, "this field should not be empty")
        }
        val containsNonDigit = value.any { !it.isDigit() }
        if (containsNonDigit) {
            return ValidationResult(false, "only number is acceptable")
        }
        return ValidationResult(true)
    }
}