package com.linkit.company.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.linkit.company.core.designsystem.theme.ButtonDisabledBg
import com.linkit.company.core.designsystem.theme.ButtonDisabledText
import com.linkit.company.core.designsystem.theme.ButtonEnabled
import com.linkit.company.core.designsystem.theme.LinkItShape
import com.linkit.company.core.designsystem.theme.LinkItTextStyle
import com.linkit.company.core.designsystem.theme.White

@Composable
fun LinkItButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        shape = LinkItShape.pill,
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonEnabled,
            contentColor = White,
            disabledContainerColor = ButtonDisabledBg,
            disabledContentColor = ButtonDisabledText,
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 14.dp),
    ) {
        Text(
            text = text,
            style = LinkItTextStyle.base2,
        )
    }
}
