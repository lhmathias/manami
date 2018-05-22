package io.github.manamiproject.manami.gui.components

import javafx.scene.paint.Color
import javafx.scene.paint.Color.*
import org.controlsfx.glyphfont.FontAwesome
import org.controlsfx.glyphfont.Glyph
import org.controlsfx.glyphfont.GlyphFont
import org.controlsfx.glyphfont.GlyphFontRegistry


object Icons {

    private val DEFAULT_ICON_COLOR: Color = DARKGRAY
    private val ICON_COLOR_RED: Color = DARKRED
    private val ICON_COLOR_DIMGRAY: Color = DIMGREY
    private val FONT_AWESOME: GlyphFont = GlyphFontRegistry.font("FontAwesome")

    fun createIconThumbsUp(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.THUMBS_UP).color(DEFAULT_ICON_COLOR)
    }

    fun createIconTags(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.TAGS).color(DEFAULT_ICON_COLOR)
    }

    fun createIconBranchFork(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.CODE_FORK).color(DEFAULT_ICON_COLOR)
    }

    fun createIconClipboardCheck(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.CLIPBOARD).color(DEFAULT_ICON_COLOR)
    }

    fun createIconPlus(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.PLUS).color(DEFAULT_ICON_COLOR)
    }

    fun createIconFile(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.FILE_ALT).color(DEFAULT_ICON_COLOR)
    }

    fun createIconFolderOpen(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.FOLDER_OPEN_ALT).color(DEFAULT_ICON_COLOR)
    }

    fun createIconSave(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.SAVE).color(DEFAULT_ICON_COLOR)
    }

    fun createIconImport(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.SIGN_IN).color(DEFAULT_ICON_COLOR)
    }

    fun createIconExport(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.SIGN_OUT).color(DEFAULT_ICON_COLOR)
    }

    fun createIconExit(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.POWER_OFF).color(DEFAULT_ICON_COLOR)
    }

    fun createIconUndo(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.UNDO).color(DEFAULT_ICON_COLOR)
    }

    fun createIconRedo(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.REPEAT).color(DEFAULT_ICON_COLOR)
    }

    fun createIconDelete(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.TRASH_ALT).color(ICON_COLOR_DIMGRAY)
    }

    fun createIconQuestion(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.QUESTION).color(DEFAULT_ICON_COLOR)
    }

    fun createIconFilterList(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.BAN).color(ICON_COLOR_RED)
    }

    fun createIconWatchList(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.EYE).color(DARKBLUE)
    }

    fun createIconRemove(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.REMOVE).color(DEFAULT_ICON_COLOR)
    }

    fun createIconCancel(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.STOP).color(ICON_COLOR_RED)
    }

    fun createIconEdit(): Glyph {
        return FONT_AWESOME.create(FontAwesome.Glyph.EDIT).color(ICON_COLOR_DIMGRAY)
    }
}