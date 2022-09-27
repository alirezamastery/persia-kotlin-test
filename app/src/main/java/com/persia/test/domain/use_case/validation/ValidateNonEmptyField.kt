package com.persia.test.domain.use_case.validation

class ValidateNonEmptyField {

    // with invoke, you can call the instance of this class directly like: instance(value=123)
    operator fun invoke(value: Any?, errorMsg: String? = null): ValidationResult {
        val msg = errorMsg ?: "این فیلد الزامی هست"
        if (value == null || (value is String && value.isBlank())) {
            return ValidationResult(false, msg)
        }
        return ValidationResult(true)
    }
}