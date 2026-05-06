package com.example.rmapp.presentation.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.rmapp.domain.model.RawMaterial
import com.example.rmapp.presentation.TopAppBarSection

@Composable
fun InventoryFormScreen(
    vm: InventoryViewModel,
    navController: NavHostController,
    id: String?
) {

    val items by vm.items.collectAsState()
    val editing = items.find { it.id == id }

    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    LaunchedEffect(editing) {
        name = editing?.name ?: ""
        unit = editing?.unit ?: ""
        price = editing?.price?.toString() ?: ""
        stock = editing?.stockAvailable?.toString() ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBarSection(
                title = if (editing == null) "Add Material" else "Edit Material",
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

            OutlinedTextField(name, { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(unit, { unit = it }, label = { Text("Unit") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(price, { price = it }, label = { Text("Price") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(stock, { stock = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    vm.save(
                        RawMaterial(
                            id = editing?.id ?: "",
                            name = name,
                            unit = unit,
                            price = price.toDoubleOrNull() ?: 0.0,
                            stockAvailable = stock.toDoubleOrNull() ?: 0.0
                        )
                    )

                    vm.clearEdit()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editing == null) "Save" else "Update")
            }
        }
    }
}