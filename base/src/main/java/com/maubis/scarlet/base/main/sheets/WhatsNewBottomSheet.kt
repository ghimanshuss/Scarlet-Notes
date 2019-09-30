package com.maubis.scarlet.base.main.sheets

import android.app.Dialog
import com.facebook.litho.Column
import com.facebook.litho.Component
import com.facebook.litho.ComponentContext
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge
import com.maubis.scarlet.base.R
import com.maubis.scarlet.base.config.ApplicationBase
import com.maubis.scarlet.base.config.CoreConfig.Companion.FONT_MONSERRAT
import com.maubis.scarlet.base.support.sheets.LithoBottomSheet
import com.maubis.scarlet.base.support.sheets.getLithoBottomSheetTitle
import com.maubis.scarlet.base.support.specs.BottomSheetBar
import com.maubis.scarlet.base.support.specs.GridSectionItem
import com.maubis.scarlet.base.support.specs.GridSectionOptionItem
import com.maubis.scarlet.base.support.specs.GridSectionView
import com.maubis.scarlet.base.support.ui.ThemeColorType
import com.maubis.scarlet.base.support.utils.FlavorUtils

class WhatsNewBottomSheet : LithoBottomSheet() {

  override fun getComponent(componentContext: ComponentContext, dialog: Dialog): Component {
    val options = listOf(
        if (FlavorUtils.isOpenSource()) null else GridSectionOptionItem(R.drawable.gdrive_icon, R.string.whats_new_sheet_google_drive, {}),
        if (FlavorUtils.isOpenSource()) null else GridSectionOptionItem(R.drawable.ic_image_gallery, R.string.whats_new_sheet_photo_sync, {}),
        GridSectionOptionItem(R.drawable.ic_action_lock, R.string.whats_new_sheet_app_lock, {}),
        GridSectionOptionItem(R.drawable.ic_action_select, R.string.whats_new_sheet_selection, {}),
        GridSectionOptionItem(R.drawable.icon_widget, R.string.whats_new_sheet_widget, {}),
        GridSectionOptionItem(R.drawable.ic_image_gallery, R.string.whats_new_sheet_more_languages, {}))

    val component = Column.create(componentContext)
        .widthPercent(100f)
        .paddingDip(YogaEdge.VERTICAL, 8f)
        .paddingDip(YogaEdge.HORIZONTAL, 20f)
        .child(getLithoBottomSheetTitle(componentContext)
            .textRes(R.string.whats_new_title)
            .marginDip(YogaEdge.HORIZONTAL, 0f))
        .child(Text.create(componentContext)
            .textSizeRes(R.dimen.font_size_large)
            .marginDip(YogaEdge.BOTTOM, 16f)
            .text(WHATS_NEW_DETAILS_SUBTITLE)
            .typeface(FONT_MONSERRAT)
            .textColor(ApplicationBase.instance.themeController().get(ThemeColorType.TERTIARY_TEXT)))
        .child(GridSectionView.create(componentContext)
            .maxLines(3)
            .numColumns(2)
            .iconSizeRes(R.dimen.ultra_large_round_icon_size)
            .section(GridSectionItem(options = options.filterNotNull()))
            .showSeparator(false))
        .child(BottomSheetBar.create(componentContext)
            .primaryActionRes(R.string.import_export_layout_exporting_done)
            .onPrimaryClick {
              dismiss()
            }
            .paddingDip(YogaEdge.VERTICAL, 8f))
    return component.build()
  }

  companion object {
    val WHATS_NEW_UID = 11

    val WHATS_NEW_DETAILS_SUBTITLE = "A lot has changed in this update, here is a summary of those changes."
    val WHATS_NEW_DETAILS_NEW_FEATURES_TITLE = "New Features"


  }
}