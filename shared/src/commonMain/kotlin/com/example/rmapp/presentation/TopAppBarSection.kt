package com.example.rmapp.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import rmapp.shared.generated.resources.Res
import rmapp.shared.generated.resources.ic_home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSection(title: String,
                     showBack: Boolean = false,
                     onBackClick: () -> Unit = {},
                     showItem: Boolean = false,
                     onItemClick: () -> Unit = {}) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_home),
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { }
                    .padding(horizontal = 16.dp)
                    .size(24.dp)
            )
        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.White
//        ),
        actions = {

            if (showItem) {
                Icon(
                    painter = painterResource(Res.drawable.ic_home),
                    contentDescription = "Add",
                    modifier = Modifier
                        .clickable { onItemClick() }
                        .padding(horizontal = 16.dp)
                        .size(24.dp),
                )
            }
        }
    )
}
