package com.example.rmapp.presentation.customers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.rmapp.ui.utils.AppRoutes
import org.jetbrains.compose.resources.painterResource
import rmapp.shared.generated.resources.Res
import rmapp.shared.generated.resources.ic_delete
import rmapp.shared.generated.resources.ic_edit

@Composable
fun CustomerScreen(
    vm: CustomerViewModel,
    navController: NavHostController
) {
    val customers by vm.customers.collectAsState()
    val search by vm.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarSection(
                title = "Customers",
                showBack = true,
                showItem = true,
                onBackClick = { navController.popBackStack() },
                onItemClick = {
                    vm.clearEdit()
                    navController.navigate(AppRoutes.CUSTOMERS_FORM)
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = { vm.search(it) },
                label = { Text("Search Customer") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(customers) { c ->

                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text(c.name)
                                Text("Phone: ${c.phone}")
                                Text("Address: ${c.address}")
                            }

                            Row {

                                IconButton(onClick = {
                                    vm.edit(c)
                                    navController.navigate(
                                        AppRoutes.customerFormWithId(c.id)
                                    )
                                }) {
                                    Icon(
                                        painterResource(Res.drawable.ic_edit),
                                        contentDescription = "Edit"
                                    )
                                }

                                IconButton(onClick = {
                                    vm.delete(c.id)
                                }) {
                                    Icon(
                                        painterResource(Res.drawable.ic_delete),
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