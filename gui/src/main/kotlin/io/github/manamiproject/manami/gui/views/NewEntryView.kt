package io.github.manamiproject.manami.gui.views

import io.github.manamiproject.manami.common.exists
import io.github.manamiproject.manami.common.isRegularFile
import io.github.manamiproject.manami.core.Manami
import io.github.manamiproject.manami.entities.*
import io.github.manamiproject.manami.gui.components.FileChoosers.showBrowseForFolderDialog
import io.github.manamiproject.manami.gui.extensions.isValid
import javafx.application.Platform
import javafx.beans.property.SimpleIntegerProperty
import javafx.concurrent.WorkerStateEvent
import javafx.event.EventHandler
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
import java.net.UnknownHostException

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
        txtInfoLink.focusedProperty().addListener(ChangeListener<Boolean> { _, valueBefore, valueAfter ->
            run {
                val infoLink = InfoLink(txtInfoLink.text)
                val host = infoLink.url?.host

                if(infoLink.toString() != txtInfoLink.text) {
                    Platform.runLater { txtInfoLink.text = infoLink.toString() }
                }

                if(valueBefore && !valueAfter && SupportedInfoLinkDomains.values().map { it.value }.contains(host)) {
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
                    txtEpisodes.text = it.episodes.toString()
                    animeTypeIndex.value = it.type.ordinal
                    disableControls(false)
                }
            }
        }.onFailed = EventHandler<WorkerStateEvent> {
            disableControls(false)
            throw UnknownHostException(txtInfoLink.text)
        }
    }

    private fun disableControls(value: Boolean) {
        Platform.runLater {
            txtTitle.isDisable = value
            txtType.isDisable = value
            txtEpisodes.isDisable = value
            btnEpisodeUp.isDisable = value
            btnEpisodeDown.isDisable = value
            txtInfoLink.isDisable = value
            btnAdd.isDisable = value

            if(!value) {
                if(animeTypeIndex.value == 0) {
                    btnTypeUp.isDisable = value
                }

                if(animeTypeIndex.value == AnimeType.values().size - 1) {
                    btnTypeDown.isDisable = value
                }
            } else {
                btnTypeUp.isDisable = value
                btnTypeDown.isDisable = value
            }

        }
    }

    private fun initAnimeTypeControls() {
        Platform.runLater { txtType.text = AnimeType.values()[animeTypeIndex.value].toString() }

        animeTypeIndex.addListener(ChangeListener<Number> { _, _, valueAfter ->
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
        txtEpisodes.textProperty().addListener(ChangeListener<String> { _, _, valueAfter ->
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
            val configFile = Manami.getConfigFile()
            var folder = it.toAbsolutePath().toString()

            if(configFile.exists() && configFile.isRegularFile()) { //TODO: test this
               folder = configFile.parent.relativize(it).toString().replace("\\", "/")
            }

            Platform.runLater { txtLocation.text = folder }
        }
    }
}