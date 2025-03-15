package com.superterminais.rivermobile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.superterminais.rivermobile.ui.theme.DisabledRedButtonColor
import com.superterminais.rivermobile.ui.theme.EnabledButtonColor
import com.superterminais.rivermobile.ui.theme.EnabledRedButtonColor

val DangerButtonColors: ButtonColors = ButtonColors(
    containerColor = EnabledRedButtonColor,
    contentColor = Color.White,
    disabledContainerColor = DisabledRedButtonColor,
    disabledContentColor = Color.White
)

val PrimaryButtonColors: ButtonColors = ButtonColors(
    containerColor = EnabledButtonColor,
    contentColor = Color.White,
    disabledContainerColor = EnabledButtonColor.copy(alpha = 0.5f),
    disabledContentColor = Color.White
)

val PrimaryIconButtonColors: IconButtonColors = IconButtonColors(
    containerColor = EnabledButtonColor,
    contentColor = Color.White,
    disabledContainerColor = EnabledButtonColor.copy(alpha = 0.5f),
    disabledContentColor = Color.White
)

data class ButtonAction(
    val value: String,
    val label: String,
    val icon: ImageVector,
    val color: ButtonColors
)

@Composable
fun RoundedButton(
    text: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colorsType: ButtonColors = PrimaryButtonColors,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Button(
        onClick = onClick,
        colors = colorsType,
        enabled = enabled,
        modifier = modifier
    ) {
        if (icon != null)
            Icon(imageVector = icon, contentDescription = text ?: "Icon")
        if (text != null)
            Text(text = text)
        content()
    }
}

@Composable
fun RoundedButtonsRow(
    actions: List<ButtonAction>,
    executeAction: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
        for (action in actions) {
            RoundedButton(
                text = action.label,
                icon = action.icon,
                colorsType = action.color,
                onClick = {
                    executeAction(action.value)
                }, modifier = Modifier.padding(8.dp)
            )
        }
    }
}