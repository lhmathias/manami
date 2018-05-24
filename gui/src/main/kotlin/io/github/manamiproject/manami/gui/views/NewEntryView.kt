package io.github.manamiproject.manami.gui.views

import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.InfoLink
import io.github.manamiproject.manami.entities.UrlValidator
import io.github.manamiproject.manami.gui.components.FileChoosers
import io.github.manamiproject.manami.gui.components.FileChoosers.showBrowseForFolderDialog
import io.github.manamiproject.manami.gui.extensions.isValid
import javafx.application.Platform
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import org.controlsfx.validation.ValidationResult
import org.controlsfx.validation.ValidationSupport
import org.controlsfx.validation.Validator
import tornadofx.ChangeListener
import tornadofx.Fragment
import tornadofx.isInt

private const val DEFAULT_EPISODES = "1"

class NewEntryView : Fragment() {
    override val root: Parent by fxml()

    private var animeTypeIndex = SimpleIntegerProperty(0)

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
        initEpisodesControls()
        initAnimeTypeControls()
        initInfoLinkControls()
    }

    private fun initInfoLinkControls() {
        txtInfoLink.focusedProperty().addListener(ChangeListener<Boolean> { observable, valueBefore, valueAfter ->
            run {
                val normalizedInfoLink = InfoLink(txtInfoLink.text).url?.toString()

                if(normalizedInfoLink != null && normalizedInfoLink != txtInfoLink.text) {
                    Platform.runLater { txtInfoLink.text = normalizedInfoLink }
                }

                if(valueBefore && !valueAfter) {
                    autoFillForm()
                }
            }
        })

        val clipboardString: String = Clipboard.getSystemClipboard().string

        if (UrlValidator.isValid(clipboardString)) {
            Platform.runLater {
                txtInfoLink.text = clipboardString
                txtInfoLink.requestFocus()
            }
        }
    }

    private fun autoFillForm() {
        disableControls(true)
        runAsync {
            Manami.fetchAnime(InfoLink(txtInfoLink.text))?.let {
                Platform.runLater {
                    txtTitle.text = it.title
                    txtType.text
                    txtEpisodes.text = it.episodes.toString()
                    disableControls(false)
                }
            }
        }
    }

    private fun disableControls(value: Boolean) {
        Platform.runLater {
            txtTitle.isDisable = value
            txtType.isDisable = value
            btnTypeUp.isDisable = value
            btnTypeDown.isDisable = value
            txtEpisodes.isDisable = value
            btnEpisodeUp.isDisable = value
            btnEpisodeDown.isDisable = value
            txtInfoLink.isDisable = value
        }
    }

    private fun initAnimeTypeControls() {
        Platform.runLater { txtType.text = AnimeType.values()[animeTypeIndex.value].toString() }

        animeTypeIndex.addListener(ChangeListener<Number> { observable, valueBefore, valueAfter ->
            run {
                Platform.runLater { txtType.text = AnimeType.values()[valueAfter.toInt()].value }

                when (valueAfter) {
                    0 -> Platform.runLater { btnTypeDown.isDisable = true }
                    AnimeType.values().size - 1 -> Platform.runLater { btnTypeUp.isDisable = true }
                    else -> Platform.runLater {
                        btnTypeDown.isDisable = false
                        btnTypeUp.isDisable = false
                    }
                }
            }
        })
    }

    private fun initEpisodesControls() {
        txtEpisodes.textProperty().addListener(ChangeListener<String> { observable, valueBefore, valueAfter ->
            run {
                if (!valueAfter.isInt() || valueAfter.startsWith("-") || "0" == valueAfter) {
                    Platform.runLater { txtEpisodes.text = DEFAULT_EPISODES }
                }

                Platform.runLater { btnEpisodeDown.isDisable = txtEpisodes.text == DEFAULT_EPISODES }
            }
        })
    }

    fun add() {
        if(validationSupport.isValid()) {
            Manami.addAnime(Anime(txtTitle.text.trim(), InfoLink(txtInfoLink.text.trim())).apply {
                type = AnimeType.findByName(txtType.text.trim())!!
                episodes = txtEpisodes.text.trim().toInt()
                location = txtLocation.text.trim()
            })

            this.close()
        }
    }

    fun increaseEpisodes() {
        Platform.runLater { txtEpisodes.text = (txtEpisodes.text.toInt() + 1).toString() }
    }

    fun decreaseEpisodes() {
        Platform.runLater { txtEpisodes.text = (txtEpisodes.text.toInt() - 1).toString() }
    }

    fun typeUp() {
        if(animeTypeIndex.value < AnimeType.values().size - 1) {
            animeTypeIndex.value++
        }
    }

    fun typeDown() {
        if(animeTypeIndex.value > 0) {
            animeTypeIndex.value--
        }
    }

    fun browse() {
        showBrowseForFolderDialog(primaryStage)?.let {
            //TODO: add relative path handling
            Platform.runLater { txtLocation.text = it.toString() }
        }
    }
}