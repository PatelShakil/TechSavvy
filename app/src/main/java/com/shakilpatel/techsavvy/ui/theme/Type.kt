package com.shakilpatel.techsavvy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.shakilpatel.techsavvy.R

val poppins_regular_font = Font(R.font.p_regular).toFontFamily()

object CustomTypographyTokens {
    val BodyLarge =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.BodyLargeWeight,
            fontSize = TypeScaleTokens.BodyLargeSize,
            lineHeight = TypeScaleTokens.BodyLargeLineHeight,
            letterSpacing = TypeScaleTokens.BodyLargeTracking,
        )

    val BodyMedium =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.BodyMediumWeight,
            fontSize = TypeScaleTokens.BodyMediumSize,
            lineHeight = TypeScaleTokens.BodyMediumLineHeight,
            letterSpacing = TypeScaleTokens.BodyMediumTracking,
        )

    val BodySmall =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.BodySmallWeight,
            fontSize = TypeScaleTokens.BodySmallSize,
            lineHeight = TypeScaleTokens.BodySmallLineHeight,
            letterSpacing = TypeScaleTokens.BodySmallTracking,
        )

    val DisplayLarge =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.DisplayLargeWeight,
            fontSize = TypeScaleTokens.DisplayLargeSize,
            lineHeight = TypeScaleTokens.DisplayLargeLineHeight,
            letterSpacing = TypeScaleTokens.DisplayLargeTracking,
        )

    val DisplayMedium =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.DisplayMediumWeight,
            fontSize = TypeScaleTokens.DisplayMediumSize,
            lineHeight = TypeScaleTokens.DisplayMediumLineHeight,
            letterSpacing = TypeScaleTokens.DisplayMediumTracking,
        )

    val DisplaySmall =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.DisplaySmallWeight,
            fontSize = TypeScaleTokens.DisplaySmallSize,
            lineHeight = TypeScaleTokens.DisplaySmallLineHeight,
            letterSpacing = TypeScaleTokens.DisplaySmallTracking,
        )

    val HeadlineLarge =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.HeadlineLargeWeight,
            fontSize = TypeScaleTokens.HeadlineLargeSize,
            lineHeight = TypeScaleTokens.HeadlineLargeLineHeight,
            letterSpacing = TypeScaleTokens.HeadlineLargeTracking,
        )

    val HeadlineMedium =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.HeadlineMediumWeight,
            fontSize = TypeScaleTokens.HeadlineMediumSize,
            lineHeight = TypeScaleTokens.HeadlineMediumLineHeight,
            letterSpacing = TypeScaleTokens.HeadlineMediumTracking,
        )

    val HeadlineSmall =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.HeadlineSmallWeight,
            fontSize = TypeScaleTokens.HeadlineSmallSize,
            lineHeight = TypeScaleTokens.HeadlineSmallLineHeight,
            letterSpacing = TypeScaleTokens.HeadlineSmallTracking,
        )

    val LabelLarge =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.LabelLargeWeight,
            fontSize = TypeScaleTokens.LabelLargeSize,
            lineHeight = TypeScaleTokens.LabelLargeLineHeight,
            letterSpacing = TypeScaleTokens.LabelLargeTracking,
        )

    val LabelMedium =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.LabelMediumWeight,
            fontSize = TypeScaleTokens.LabelMediumSize,
            lineHeight = TypeScaleTokens.LabelMediumLineHeight,
            letterSpacing = TypeScaleTokens.LabelMediumTracking,
        )

    val LabelSmall =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.LabelSmallWeight,
            fontSize = TypeScaleTokens.LabelSmallSize,
            lineHeight = TypeScaleTokens.LabelSmallLineHeight,
            letterSpacing = TypeScaleTokens.LabelSmallTracking,
        )

    val TitleLarge =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.TitleLargeWeight,
            fontSize = TypeScaleTokens.TitleLargeSize,
            lineHeight = TypeScaleTokens.TitleLargeLineHeight,
            letterSpacing = TypeScaleTokens.TitleLargeTracking,
        )

    val TitleMedium =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.TitleMediumWeight,
            fontSize = TypeScaleTokens.TitleMediumSize,
            lineHeight = TypeScaleTokens.TitleMediumLineHeight,
            letterSpacing = TypeScaleTokens.TitleMediumTracking,
        )

    val TitleSmall =
        TextStyle(
            fontFamily = poppins_regular_font,
            fontWeight = TypeScaleTokens.TitleSmallWeight,
            fontSize = TypeScaleTokens.TitleSmallSize,
            lineHeight = TypeScaleTokens.TitleSmallLineHeight,
            letterSpacing = TypeScaleTokens.TitleSmallTracking,
        )

}

object TypeScaleTokens {
    val BodyLargeFont = TypefaceTokens.Plain
    val BodyLargeLineHeight = 24.0.sp
    val BodyLargeSize = 16.sp
    val BodyLargeTracking = 0.5.sp
    val BodyLargeWeight = TypefaceTokens.WeightRegular
    val BodyMediumFont = TypefaceTokens.Plain
    val BodyMediumLineHeight = 20.0.sp
    val BodyMediumSize = 14.sp
    val BodyMediumTracking = 0.2.sp
    val BodyMediumWeight = TypefaceTokens.WeightRegular
    val BodySmallFont = TypefaceTokens.Plain
    val BodySmallLineHeight = 16.0.sp
    val BodySmallSize = 12.sp
    val BodySmallTracking = 0.4.sp
    val BodySmallWeight = TypefaceTokens.WeightRegular
    val DisplayLargeFont = TypefaceTokens.Brand
    val DisplayLargeLineHeight = 64.0.sp
    val DisplayLargeSize = 57.sp
    val DisplayLargeTracking = -0.2.sp
    val DisplayLargeWeight = TypefaceTokens.WeightRegular
    val DisplayMediumFont = TypefaceTokens.Brand
    val DisplayMediumLineHeight = 52.0.sp
    val DisplayMediumSize = 45.sp
    val DisplayMediumTracking = 0.0.sp
    val DisplayMediumWeight = TypefaceTokens.WeightRegular
    val DisplaySmallFont = TypefaceTokens.Brand
    val DisplaySmallLineHeight = 44.0.sp
    val DisplaySmallSize = 36.sp
    val DisplaySmallTracking = 0.0.sp
    val DisplaySmallWeight = TypefaceTokens.WeightRegular
    val HeadlineLargeFont = TypefaceTokens.Brand
    val HeadlineLargeLineHeight = 40.0.sp
    val HeadlineLargeSize = 32.sp
    val HeadlineLargeTracking = 0.0.sp
    val HeadlineLargeWeight = TypefaceTokens.WeightRegular
    val HeadlineMediumFont = TypefaceTokens.Brand
    val HeadlineMediumLineHeight = 36.0.sp
    val HeadlineMediumSize = 28.sp
    val HeadlineMediumTracking = 0.0.sp
    val HeadlineMediumWeight = TypefaceTokens.WeightRegular
    val HeadlineSmallFont = TypefaceTokens.Brand
    val HeadlineSmallLineHeight = 32.0.sp
    val HeadlineSmallSize = 24.sp
    val HeadlineSmallTracking = 0.0.sp
    val HeadlineSmallWeight = TypefaceTokens.WeightRegular
    val LabelLargeFont = TypefaceTokens.Plain
    val LabelLargeLineHeight = 20.0.sp
    val LabelLargeSize = 14.sp
    val LabelLargeTracking = 0.1.sp
    val LabelLargeWeight = TypefaceTokens.WeightMedium
    val LabelMediumFont = TypefaceTokens.Plain
    val LabelMediumLineHeight = 16.0.sp
    val LabelMediumSize = 12.sp
    val LabelMediumTracking = 0.5.sp
    val LabelMediumWeight = TypefaceTokens.WeightMedium
    val LabelSmallFont = TypefaceTokens.Plain
    val LabelSmallLineHeight = 16.0.sp
    val LabelSmallSize = 11.sp
    val LabelSmallTracking = 0.5.sp
    val LabelSmallWeight = TypefaceTokens.WeightMedium
    val TitleLargeFont = TypefaceTokens.Brand
    val TitleLargeLineHeight = 28.0.sp
    val TitleLargeSize = 22.sp
    val TitleLargeTracking = 0.0.sp
    val TitleLargeWeight = TypefaceTokens.WeightRegular
    val TitleMediumFont = TypefaceTokens.Plain
    val TitleMediumLineHeight = 24.0.sp
    val TitleMediumSize = 16.sp
    val TitleMediumTracking = 0.2.sp
    val TitleMediumWeight = TypefaceTokens.WeightMedium
    val TitleSmallFont = TypefaceTokens.Plain
    val TitleSmallLineHeight = 20.0.sp
    val TitleSmallSize = 14.sp
    val TitleSmallTracking = 0.1.sp
    val TitleSmallWeight = TypefaceTokens.WeightMedium
}

object TypefaceTokens {
    val Brand = FontFamily.SansSerif
    val Plain = FontFamily.SansSerif
    val WeightBold = FontWeight.Bold
    val WeightMedium = FontWeight.Medium
    val WeightRegular = FontWeight.Normal
}


val Typography = Typography(
    displayLarge = CustomTypographyTokens.DisplayLarge,
    displayMedium = CustomTypographyTokens.DisplayMedium,
    displaySmall = CustomTypographyTokens.DisplaySmall,
    headlineLarge = CustomTypographyTokens.HeadlineLarge,
    headlineMedium = CustomTypographyTokens.HeadlineMedium,
    headlineSmall = CustomTypographyTokens.HeadlineSmall,
    titleLarge = CustomTypographyTokens.TitleLarge,
    titleMedium = CustomTypographyTokens.TitleMedium,
    titleSmall = CustomTypographyTokens.TitleSmall,
    bodyLarge = CustomTypographyTokens.BodyLarge,
    bodyMedium = CustomTypographyTokens.BodyMedium,
    bodySmall = CustomTypographyTokens.BodySmall,
    labelLarge = CustomTypographyTokens.LabelLarge,
    labelMedium = CustomTypographyTokens.LabelMedium,
    labelSmall = CustomTypographyTokens.LabelSmall
)