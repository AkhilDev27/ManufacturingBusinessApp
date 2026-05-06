package com.example.rmapp.presentation.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.rmapp.domain.model.Product
import com.example.rmapp.presentation.TopAppBarSection

@Composable
fun ProductFormScreen(
    vm: ProductViewModel,
    navController: NavHostController,
    productId: String?
) {

    val products by vm.products.collectAsState()

    val editing = products.find { it.id == productId }

    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var selling by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }

    LaunchedEffect(editing) {
        if (editing != null) {
            name = editing.name
            unit = editing.unit
            selling = editing.sellingPrice.toString()
            cost = editing.costPrice.toString()
        } else {
            name = ""
            unit = ""
            selling = ""
            cost = ""
        }
    }
    Scaffold(
        topBar = {
            TopAppBarSection(
                title = if (productId != null) "Edit Product" else "Add Product",
                showBack = true,
                onBackClick = {
                    vm.clearEdit()
                    navController.popBackStack()
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
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
                value = unit,
                onValueChange = { unit = it },
                label = { Text("Unit") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = selling,
                onValueChange = { selling = it },
                label = { Text("Selling Price") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it },
                label = { Text("Cost Price") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    vm.save(
                        Product(
                            id = editing?.id ?: "",
                            name = name,
                            unit = unit,
                            sellingPrice = selling.toDoubleOrNull() ?: 0.0,
                            costPrice = cost.toDoubleOrNull() ?: 0.0
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