package sco.carlukesoftware.composeextensions

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * MultiPreview annotation representing common device sizes, orientations, and configurations.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(
    name = "Small Phone",
    group = "Devices",
    device = Devices.NEXUS_5,
    showBackground = true
)
@Preview(
    name = "Standard Phone",
    group = "Devices",
    device = Devices.PIXEL_7,
    showBackground = true
)
@Preview(
    name = "Tablet Portrait",
    group = "Devices",
    device = Devices.TABLET,
    showBackground = true
)
@Preview(
    name = "Tablet Landscape",
    group = "Devices",
    device = "spec:width=1280dp,height=800dp,orientation=landscape",
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    group = "Themes",
    device = Devices.PIXEL_7,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    name = "Large Font (150%)",
    group = "Accessibility",
    device = Devices.PIXEL_7,
    fontScale = 1.5f,
    showBackground = true
)
@Preview(
    name = "RTL Layout",
    group = "Locales",
    device = Devices.PIXEL_7,
    locale = "ar",
    showBackground = true
)
annotation class MultiPreviews

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(
    name = "Phone - Small",
    group = "Devices",
    device = Devices.NEXUS_5,
    showBackground = true
)
@Preview(
    name = "Phone - Standard",
    group = "Devices",
    device = Devices.PIXEL_7,
    showBackground = true
)
@Preview(
    name = "Tablet - Portrait",
    group = "Devices",
    device = Devices.TABLET,
    showBackground = true
)
@Preview(
    name = "Tablet - Landscape",
    group = "Devices",
    device = "spec:width=1280dp,height=800dp,orientation=landscape",
    showBackground = true
)
annotation class DevicePreviews

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(
    name = "Light Mode",
    group = "Themes",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    group = "Themes",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
annotation class ThemePreviews

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
@Preview(
    name = "Font Scale 100%",
    group = "Accessibility",
    fontScale = 1.0f,
    showBackground = true
)
@Preview(
    name = "Font Scale 150%",
    group = "Accessibility",
    fontScale = 1.5f,
    showBackground = true
)
@Preview(
    name = "Font Scale 200%",
    group = "Accessibility",
    fontScale = 2.0f,
    showBackground = true
)
annotation class AccessibilityPreviews

/**
 * Composite MultiPreview combining device form factors, themes, and accessibility options.
 */
@DevicePreviews
@ThemePreviews
@AccessibilityPreviews
annotation class AllPreviews
