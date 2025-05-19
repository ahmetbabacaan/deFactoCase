package com.babacan.defactocase.domain.model

enum class PasswordConstraintType(val predicate: (String) -> Boolean) {
    MAIL({ it.contains(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) }),
    LENGTH({ it.length >= 8 }),
    UPPER_CASE({ it.contains(Regex("[A-Z]")) }),
    LOWER_CASE({
        it.contains(
            Regex("[a-z]")
        )
    }),
    NUMBER_SPECIAL_CHARACTER({ it.contains(Regex("[0-9]")) || it.contains(Regex("[^A-Za-z0-9]")) }),
    MATCH_WITH_FIRST({ true })
}

data class PasswordConstraint(
    val id: PasswordConstraintType,
    val text: String,
    val isValid: Boolean,
)