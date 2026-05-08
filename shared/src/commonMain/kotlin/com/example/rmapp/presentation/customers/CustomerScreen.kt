package com.example.rmapp.presentation.customers

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rmapp.domain.model.Customer
import com.example.rmapp.presentation.TopAppBarSection
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import rmapp.shared.generated.resources.Res
import rmapp.shared.generated.resources.ic_delete
import rmapp.shared.generated.resources.ic_edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerScreen(
    vm: CustomerViewModel,
    navController: NavHostController
) {

    val customers by vm.customers.collectAsState()
    val search by vm.searchQuery.collectAsState()

    var selectedCustomer by remember { mutableStateOf<Customer?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBarSection(
                title = "Customers",
                showBack = true,
                showItem = true,
                onBackClick = { navController.popBackStack() },
                onItemClick = {
                    vm.clearEdit()
                    selectedCustomer = Customer()
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = search,
                    onValueChange = { vm.search(it) },
                    placeholder = { Text("Search customers...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(customers, key = { it.id }) { customer ->

                        CustomerSwipeToDeleteContainer(
                            onDelete = {

                                vm.delete(customer.id)

                                scope.launch {

                                    val result = snackbarHostState.showSnackbar(
                                        message = "${customer.name} deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Long
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {

                                        vm.save(customer)

                                    }
                                }
                            }
                        ) {

                            CustomerCard(
                                customer = customer,
                                onEdit = {
                                    vm.edit(customer)
                                    selectedCustomer = customer
                                }
                            )
                        }
                    }
                }
            }

            if (selectedCustomer != null) {

                ModalBottomSheet(
                    onDismissRequest = {
                        selectedCustomer = null
                    },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true
                    )
                ) {

                    CustomerForm(
                        customer = selectedCustomer!!,
                        onSave = {

                            vm.save(it)
                            selectedCustomer = null
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerSwipeToDeleteContainer(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {

            if (it == SwipeToDismissBoxValue.EndToStart) {

                onDelete()
                true

            } else false
        }
    )

    val progress = dismissState.progress

    val color by animateColorAsState(
        targetValue = if (progress > 0f)
            MaterialTheme.colorScheme.error
        else
            MaterialTheme.colorScheme.surface
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {

            val shape = MaterialTheme.shapes.large

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
                    .background(color)
                    .padding(end = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {

                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        },
        content = { content() }
    )
}

@Composable
fun CustomerCard(
    customer: Customer,
    onEdit: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = customer.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = customer.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = customer.address,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = onEdit
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit),
                    contentDescription = "Edit"
                )
            }
        }
    }
}

@Composable
fun CustomerForm(
    customer: Customer,
    onSave: (Customer) -> Unit
) {

    var name by remember { mutableStateOf(customer.name) }
    var phone by remember { mutableStateOf(customer.phone) }
    var address by remember { mutableStateOf(customer.address) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    val nameFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val addressFocus = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Customer",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        // NAME

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = null
            },
            label = { Text("Customer Name") },
            isError = nameError != null,
            supportingText = {
                nameError?.let { Text(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocus),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    phoneFocus.requestFocus()
                }
            )
        )

        Spacer(Modifier.height(8.dp))

        // PHONE

        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it.filter { ch -> ch.isDigit() }
                phoneError = null
            },
            label = { Text("Phone Number") },
            isError = phoneError != null,
            supportingText = {
                phoneError?.let { Text(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(phoneFocus),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    addressFocus.requestFocus()
                }
            )
        )

        Spacer(Modifier.height(8.dp))

        // ADDRESS

        OutlinedTextField(
            value = address,
            onValueChange = {
                address = it
                addressError = null
            },
            label = { Text("Address") },
            isError = addressError != null,
            supportingText = {
                addressError?.let { Text(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(addressFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {

                var isValid = true

                if (name.isBlank()) {
                    nameError = "Name required"
                    isValid = false
                }

                if (phone.length < 10) {
                    phoneError = "Enter valid phone number"
                    isValid = false
                }

                if (address.isBlank()) {
                    addressError = "Address required"
                    isValid = false
                }

                if (isValid) {

                    onSave(
                        customer.copy(
                            name = name.trim(),
                            phone = phone.trim(),
                            address = address.trim()
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Save")
        }

        Spacer(Modifier.height(40.dp))
    }
}