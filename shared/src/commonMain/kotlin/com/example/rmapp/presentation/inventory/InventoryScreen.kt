package com.example.rmapp.presentation.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rmapp.domain.model.RawMaterial
import com.example.rmapp.presentation.TopAppBarSection
import org.jetbrains.compose.resources.painterResource
import rmapp.shared.generated.resources.Res
import rmapp.shared.generated.resources.ic_home

@Composable
fun InventoryScreen(vm: InventoryViewModel) {

    val editing = vm.editingItem
    var showForm by remember { mutableStateOf(false) }

    var name by remember(editing) { mutableStateOf(editing?.name ?: "") }
    var unit by remember(editing) { mutableStateOf(editing?.unit ?: "") }
    var price by remember(editing) { mutableStateOf(editing?.price?.toString() ?: "") }
    var stock by remember(editing) { mutableStateOf(editing?.stockAvailable?.toString() ?: "") }

    Scaffold(

        // ---------------- TOP BAR ----------------
        topBar = {
            TopAppBarSection(
                title = "Inventory",
                showBack = true,
                showItem = true,
                onItemClick = {
                    vm.clearEdit()
                    showForm = true
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

            // ---------------- FORM ----------------
            if (showForm || editing != null) {

                Text(
                    text = if (editing == null) "Add Material" else "Edit Material",
                    style = MaterialTheme.typography.titleMedium
                )

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
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") },
                    modifier = Modifier.fillMaxWidth()
                )

                // ---------------- BUTTONS ----------------
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = {

                            vm.save(
                                RawMaterial(
                                    id = "",
                                    name = name,
                                    unit = unit,
                                    price = price.toDoubleOrNull() ?: 0.0,
                                    stockAvailable = stock.toDoubleOrNull() ?: 0.0
                                )
                            )

                            // reset form
                            name = ""
                            unit = ""
                            price = ""
                            stock = ""
                            showForm = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (editing == null) "Save" else "Update")
                    }

                    OutlinedButton(
                        onClick = {
                            vm.clearEdit()
                            showForm = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            }

            // ---------------- LIST ----------------
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(vm.items) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            // DETAILS
                            Column {
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text("Unit: ${item.unit}")
                                Text("Price: ${item.price}")
                                Text("Stock: ${item.stockAvailable}")
                            }

                            // ACTIONS
                            Row {

                                IconButton(onClick = {
                                    vm.edit(item)
                                    showForm = true
                                }) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_home),
                                        contentDescription = "Edit"
                                    )
                                }

                                IconButton(onClick = {
                                    vm.delete(item.id)
                                }) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_home),
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}