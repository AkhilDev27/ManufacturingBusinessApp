package com.example.rmapp.presentation.customers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rmapp.domain.model.Customer
import com.example.rmapp.presentation.TopAppBarSection

@Composable
fun CustomerFormScreen(
    vm: CustomerViewModel,
    navController: NavHostController,
    customerId: String?
) {

    val customers by vm.customers.collectAsState()
    val editing = customers.find { it.id == customerId }

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    LaunchedEffect(editing) {
        name = editing?.name ?: ""
        phone = editing?.phone ?: ""
        address = editing?.address ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBarSection(
                title = if (editing == null) "Add Customer" else "Edit Customer",
                showBack = true,
                onBackClick = {
                    vm.clearEdit()
                    navController.popBackStack()
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    vm.save(
                        Customer(
                            id = editing?.id ?: "",
                            name = name,
                            phone = phone,
                            address = address
                        )
                    )
                    vm.clearEdit()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editing == null) "Save" else "Update")
            }

            OutlinedButton(
                onClick = {
                    vm.clearEdit()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}