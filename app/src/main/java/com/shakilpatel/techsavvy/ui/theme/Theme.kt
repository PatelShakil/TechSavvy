package com.shakilpatel.techsavvy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ==================== THEME OPTIONS ====================

enum class AppTheme {
    TECH_SAVVY_BLUE,    // Default - Professional Blue
    OCEAN_BREEZE,       // Calm Blue-Cyan
    SUNSET_ORANGE,      // Warm Orange-Red
    FOREST_GREEN,       // Natural Green
    ROYAL_PURPLE,       // Premium Purple
    DARK_MODE           // Pure Dark Theme
}

// ==================== COLOR SCHEMES ====================

// 1. TECH SAVVY BLUE (Default) - Professional & Trust
private val TechSavvyBlueLightScheme = lightColorScheme(
    primary = Color(0xFF1E40AF),           // Deep Blue
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDBE2FF),
    onPrimaryContainer = Color(0xFF001258),

    secondary = Color(0xFF7C3AED),         // Purple Accent
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEDE7FF),
    onSecondaryContainer = Color(0xFF2B0057),

    tertiary = Color(0xFF10B981),          // Success Green
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = Color(0xFF064E3B),

    error = Color(0xFFDC2626),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),

    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF1E293B),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),

    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

private val TechSavvyBlueDarkScheme = darkColorScheme(
    primary = Color(0xFF3B82F6),
    onPrimary = Color(0xFF001D3D),
    primaryContainer = Color(0xFF002D5C),
    onPrimaryContainer = Color(0xFFDBE2FF),

    secondary = Color(0xFFA78BFA),
    onSecondary = Color(0xFF381E72),
    secondaryContainer = Color(0xFF522E91),
    onSecondaryContainer = Color(0xFFEDE7FF),

    tertiary = Color(0xFF34D399),
    onTertiary = Color(0xFF003920),
    tertiaryContainer = Color(0xFF00522F),
    onTertiaryContainer = Color(0xFFD1FAE5),

    error = Color(0xFFF87171),
    onError = Color(0xFF5F1010),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFEE2E2),

    background = Color(0xFF0F172A),
    onBackground = Color(0xFFE2E8F0),

    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),

    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155)
)

// 2. OCEAN BREEZE - Calm & Refreshing
private val OceanBreezeLightScheme = lightColorScheme(
    primary = Color(0xFF0891B2),           // Cyan
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFCFFAFE),
    onPrimaryContainer = Color(0xFF083344),

    secondary = Color(0xFF06B6D4),         // Light Cyan
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF082F49),

    tertiary = Color(0xFF14B8A6),          // Teal
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFCCFBF1),
    onTertiaryContainer = Color(0xFF134E4A),

    error = Color(0xFFDC2626),
    background = Color(0xFFF0FDFA),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A)
)

private val OceanBreezeDarkScheme = darkColorScheme(
    primary = Color(0xFF22D3EE),
    onPrimary = Color(0xFF083344),
    primaryContainer = Color(0xFF164E63),
    onPrimaryContainer = Color(0xFFCFFAFE),

    secondary = Color(0xFF38BDF8),
    onSecondary = Color(0xFF082F49),
    secondaryContainer = Color(0xFF075985),
    onSecondaryContainer = Color(0xFFE0F2FE),

    tertiary = Color(0xFF2DD4BF),
    onTertiary = Color(0xFF134E4A),
    tertiaryContainer = Color(0xFF115E59),
    onTertiaryContainer = Color(0xFFCCFBF1),

    background = Color(0xFF083344),
    surface = Color(0xFF0E4C5F),
    onBackground = Color(0xFFCFFAFE),
    onSurface = Color(0xFFCFFAFE)
)

// 3. SUNSET ORANGE - Warm & Energetic
private val SunsetOrangeLightScheme = lightColorScheme(
    primary = Color(0xFFEA580C),           // Orange
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFEDD5),
    onPrimaryContainer = Color(0xFF7C2D12),

    secondary = Color(0xFFF59E0B),         // Amber
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),

    tertiary = Color(0xFFDC2626),          // Red Accent
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFEE2E2),
    onTertiaryContainer = Color(0xFF7F1D1D),

    background = Color(0xFFFFF7ED),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1F2937),
    onSurface = Color(0xFF1F2937)
)

private val SunsetOrangeDarkScheme = darkColorScheme(
    primary = Color(0xFFFB923C),
    onPrimary = Color(0xFF7C2D12),
    primaryContainer = Color(0xFFC2410C),
    onPrimaryContainer = Color(0xFFFFEDD5),

    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF78350F),
    secondaryContainer = Color(0xFFD97706),
    onSecondaryContainer = Color(0xFFFEF3C7),

    tertiary = Color(0xFFF87171),
    onTertiary = Color(0xFF7F1D1D),
    tertiaryContainer = Color(0xFFDC2626),
    onTertiaryContainer = Color(0xFFFEE2E2),

    background = Color(0xFF1F2937),
    surface = Color(0xFF374151),
    onBackground = Color(0xFFF9FAFB),
    onSurface = Color(0xFFF9FAFB)
)

// 4. FOREST GREEN - Natural & Trustworthy
private val ForestGreenLightScheme = lightColorScheme(
    primary = Color(0xFF059669),           // Emerald
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),

    secondary = Color(0xFF10B981),         // Green
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFECFDF5),
    onSecondaryContainer = Color(0xFF065F46),

    tertiary = Color(0xFF84CC16),          // Lime
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF7FEE7),
    onTertiaryContainer = Color(0xFF365314),

    background = Color(0xFFF0FDF4),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1F2937),
    onSurface = Color(0xFF1F2937)
)

private val ForestGreenDarkScheme = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF064E3B),
    primaryContainer = Color(0xFF047857),
    onPrimaryContainer = Color(0xFFD1FAE5),

    secondary = Color(0xFF6EE7B7),
    onSecondary = Color(0xFF065F46),
    secondaryContainer = Color(0xFF059669),
    onSecondaryContainer = Color(0xFFECFDF5),

    tertiary = Color(0xFFA3E635),
    onTertiary = Color(0xFF365314),
    tertiaryContainer = Color(0xFF4D7C0F),
    onTertiaryContainer = Color(0xFFF7FEE7),

    background = Color(0xFF064E3B),
    surface = Color(0xFF065F46),
    onBackground = Color(0xFFD1FAE5),
    onSurface = Color(0xFFD1FAE5)
)

// 5. ROYAL PURPLE - Premium & Elegant
private val RoyalPurpleLightScheme = lightColorScheme(
    primary = Color(0xFF7C3AED),           // Violet
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = Color(0xFF3B0764),

    secondary = Color(0xFF9333EA),         // Purple
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFAF5FF),
    onSecondaryContainer = Color(0xFF581C87),

    tertiary = Color(0xFFDB2777),          // Pink Accent
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFCE7F3),
    onTertiaryContainer = Color(0xFF831843),

    background = Color(0xFFFAF5FF),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1F2937),
    onSurface = Color(0xFF1F2937)
)

private val RoyalPurpleDarkScheme = darkColorScheme(
    primary = Color(0xFFA78BFA),
    onPrimary = Color(0xFF3B0764),
    primaryContainer = Color(0xFF5B21B6),
    onPrimaryContainer = Color(0xFFEDE9FE),

    secondary = Color(0xFFC084FC),
    onSecondary = Color(0xFF581C87),
    secondaryContainer = Color(0xFF6B21A8),
    onSecondaryContainer = Color(0xFFFAF5FF),

    tertiary = Color(0xFFF472B6),
    onTertiary = Color(0xFF831843),
    tertiaryContainer = Color(0xFF9F1239),
    onTertiaryContainer = Color(0xFFFCE7F3),

    background = Color(0xFF3B0764),
    surface = Color(0xFF4C1D95),
    onBackground = Color(0xFFEDE9FE),
    onSurface = Color(0xFFEDE9FE)
)

// 6. PURE DARK MODE - AMOLED Friendly
private val PureDarkScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF001D3D),
    primaryContainer = Color(0xFF1E40AF),
    onPrimaryContainer = Color(0xFFDBE2FF),

    secondary = Color(0xFF818CF8),
    onSecondary = Color(0xFF1E1B4B),
    secondaryContainer = Color(0xFF4338CA),
    onSecondaryContainer = Color(0xFFE0E7FF),

    tertiary = Color(0xFF34D399),
    onTertiary = Color(0xFF064E3B),
    tertiaryContainer = Color(0xFF059669),
    onTertiaryContainer = Color(0xFFD1FAE5),

    background = Color(0xFF000000),        // Pure Black
    surface = Color(0xFF111827),
    onBackground = Color(0xFFE5E7EB),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF1F2937),
    onSurfaceVariant = Color(0xFF9CA3AF)
)

// ==================== THEME COMPOSABLE ====================

@Composable
fun TechSavvyTheme(
    appTheme: AppTheme = AppTheme.TECH_SAVVY_BLUE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.TECH_SAVVY_BLUE -> if (darkTheme) TechSavvyBlueDarkScheme else TechSavvyBlueLightScheme
        AppTheme.OCEAN_BREEZE -> if (darkTheme) OceanBreezeDarkScheme else OceanBreezeLightScheme
        AppTheme.SUNSET_ORANGE -> if (darkTheme) SunsetOrangeDarkScheme else SunsetOrangeLightScheme
        AppTheme.FOREST_GREEN -> if (darkTheme) ForestGreenDarkScheme else ForestGreenLightScheme
        AppTheme.ROYAL_PURPLE -> if (darkTheme) RoyalPurpleDarkScheme else RoyalPurpleLightScheme
        AppTheme.DARK_MODE -> PureDarkScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
