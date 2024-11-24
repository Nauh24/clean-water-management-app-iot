package com.nauh.waterqualitymonitor.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nauh.waterqualitymonitor.R
import com.nauh.waterqualitymonitor.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    pageTitle: String,
    username: String = "",
    onBackClick: (() -> Unit)? = null, // Thêm tùy chọn icon trở về
    onAccountClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            TopBarContent(
                pageTitle = pageTitle,
                username = username,
                onBackClick = onBackClick, // Truyền hàm callback để hiển thị icon trở về nếu có
                onAccountClick = onAccountClick
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
private fun TopBarContent(
    pageTitle: String,
    username: String,
    onBackClick: (() -> Unit)?,
    onAccountClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Hiển thị icon trở về nếu hàm onBackClick khác null
        if (onBackClick != null) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back), // Thêm icon trở về
                    contentDescription = "Back"
                )
            }
        }

        Text(
            text = pageTitle,
            style = Typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            AccountIcon(onClick = onAccountClick)
            UserName(username = username)
        }
    }
}

@Composable
private fun AccountIcon(onClick: () -> Unit) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.user),
        contentDescription = "Account",
        modifier = Modifier
            .size(24.dp)
            .clickable { onClick() }
    )
}

@Composable
private fun UserName(username: String) {
    Text(
        text = username,
        style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(start = 8.dp)
    )
}
