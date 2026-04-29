package com.example.rmapp.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardCard(
    title: String,
    value: Number,
    isAlert: Boolean = false
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (isAlert)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}