package dev.jorgeroldan.marsrover.ui.util

import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes

/**
 * Preview group for full screens, configured to show system ui in light and dark mode
 */
@PreviewScreenSizes
@PreviewLightDark
@PreviewDynamicColors
annotation class ScreenPreview

/**
 * Preview group for ui components, configured to show ui in light and dark mode
 */
@PreviewLightDark
@PreviewDynamicColors
annotation class ComponentPreview
