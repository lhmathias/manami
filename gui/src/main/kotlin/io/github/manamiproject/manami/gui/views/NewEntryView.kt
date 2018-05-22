package io.github.manamiproject.manami.gui.views

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.UrlValidator
import io.github.manamiproject.manami.gui.extensions.isValid
import javafx.application.Platform
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import org.controlsfx.validation.ValidationResult
import org.controlsfx.validation.ValidationSupport
import org.controlsfx.validation.Validator
import tornadofx.Fragment

class NewEntryView : Fragment() {
    override val root: Parent by fxml()

    private val txtTitle: TextField by fxid()
    private val txtType: TextField by fxid()
    private val txtEpisodes: TextField by fxid()
    private val txtInfoLink: TextField by fxid()
    private val txtLocation: TextField by fxid()
    private val btnTypeUp: Button by fxid()
    private val btnTypeDown: Button by fxid()
    private val btnEpisodeUp: Button by fxid()
    private val btnEpisodeDown: Button by fxid()
    private val btnAdd: Button by fxid()
    private val btnBrowse: Button by fxid()

    private val validationSupport: ValidationSupport = ValidationSupport().apply {
        registerValidator(txtTitle, Validator.createEmptyValidator<TextField>("Title is required"))
        registerValidator(txtLocation, Validator.createEmptyValidator<TextField>("Location is required"))
        registerValidator(txtInfoLink, Validator<String> { _, value ->
            return@Validator when {
                value.isNullOrEmpty() || UrlValidator.isNotValid(value) -> ValidationResult.fromError(txtInfoLink, "Info link must be a valid URL")
                else -> ValidationResult()
            }
        })
    }

    init {
        val clipboardString: String = Clipboard.getSystemClipboard().string

        if(UrlValidator.isValid(clipboardString)) {
            txtInfoLink.text = clipboardString
            Platform.runLater { txtInfoLink.requestFocus() }
        }
    }

    fun add() {
        if(validationSupport.isValid()) {
            Manami.addAnime(Anime(txtTitle.text.trim(), InfoLink(txtInfoLink.text.trim())).apply {
                type = AnimeType.findByName(txtType.text.trim())!!
                episodes = txtEpisodes.text.trim().toInt()
                location = txtLocation.text.trim()
            })
        }
    }

    fun increaseEpisodes() {}
    fun decreaseEpisodes() {}

    fun typeUp() {}
    fun typeDown() {}
    fun browse() {}
}