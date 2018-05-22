package io.github.manamiproject.manami.entities

import org.apache.commons.validator.routines.UrlValidator

object UrlValidator {

    private val urlValidator = UrlValidator(arrayOf("HTTP", "HTTPS"))

    fun isValid(url: String) = urlValidator.isValid(url)

    fun isNotValid(url: String) = !isValid(url)
}