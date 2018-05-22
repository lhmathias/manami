package io.github.manamiproject.manami.gui.extensions

import org.controlsfx.validation.ValidationSupport

fun ValidationSupport.isValid(): Boolean {
    return !this.isInvalid
}