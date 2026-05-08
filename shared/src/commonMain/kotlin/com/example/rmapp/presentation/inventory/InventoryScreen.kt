package com.example.rmapp.presentation.inventory

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
import androidx.compose.material3.Surface
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
import com.example.rmapp.domain.model.RawMaterial
import com.example.rmapp.presentation.TopAppBarSection
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import rmapp.shared.generated.resources.Res
import rmapp.shared.generated.resources.ic_delete
import rmapp.shared.generated.resources.ic_edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    vm: InventoryViewModel,
    navController: NavHostController
) {

    val items by vm.items.collectAsState()
    val search by vm.searchQuery.collectAsState()

    var selectedItem by remember {
        mutableStateOf<RawMaterial?>(null)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {

            TopAppBarSection(
                title = "Inventory",
                showBack = true,
                showItem = true,
                onBackClick = {
                    navController.popBackStack()
                },
                onItemClick = {

                    vm.clearEdit()

                    selectedItem = RawMaterial()
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
                    onValueChange = {
                        vm.search(it)
                    },
                    placeholder = {
                        Text("Search inventory...")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(items, key = { it.id }) { item ->

                        InventorySwipeToDeleteContainer(
                            onDelete = {

                                vm.delete(item.id)

                                scope.launch {

                                    val result =
                                        snackbarHostState.showSnackbar(
                                            message = "${item.name} deleted",
                                            actionLabel = "Undo",
                                            duration = SnackbarDuration.Long
                                        )

                                    if (result == SnackbarResult.ActionPerformed) {

                                        vm.save(item)
                                    }
                                }
                            }
                        ) {

                            InventoryCard(
                                item = item,
                                onEdit = {

                                    vm.edit(item)
                                    selectedItem = item
                                }
                            )
                        }
                    }
                }
            }

            if (selectedItem != null) {

                ModalBottomSheet(
                    onDismissRequest = {
                        selectedItem = null
                    },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true
                    )
                ) {

                    InventoryForm(
                        item = selectedItem!!,
                        onSave = {

                            vm.save(it)
                            selectedItem = null
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventorySwipeToDeleteContainer(
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
        content = {
            content()
        }
    )
}

@Composable
fun InventoryCard(
    item: RawMaterial,
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
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Row {

                    Column {

                        Text(
                            text = "Unit",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        Text(
                            text = item.unit,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.width(20.dp))

                    Column {

                        Text(
                            text = "Price",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        Text(
                            text = "₹${item.price}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.width(20.dp))

                    Column {

                        Text(
                            text = "Stock",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )

                        Text(
                            text = "${item.stockAvailable}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {

                Text(
                    text = item.unit,
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    ),
                    style = MaterialTheme.typography.labelSmall
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
fun InventoryForm(
    item: RawMaterial,
    onSave: (RawMaterial) -> Unit
) {

    var name by remember {
        mutableStateOf(item.name)
    }

    var unit by remember {
        mutableStateOf(item.unit)
    }

    var price by remember {
        mutableStateOf(
            item.price.takeIf { it != 0.0 }?.toString() ?: ""
        )
    }

    var stock by remember {
        mutableStateOf(
            item.stockAvailable.takeIf { it != 0.0 }?.toString() ?: ""
        )
    }

    var nameError by remember { mutableStateOf<String?>(null) }
    var unitError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    val nameFocus = remember { FocusRequester() }
    val unitFocus = remember { FocusRequester() }
    val priceFocus = remember { FocusRequester() }
    val stockFocus = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Material",
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
            label = {
                Text("Material Name")
            },
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
                    unitFocus.requestFocus()
                }
            )
        )

        Spacer(Modifier.height(8.dp))

        // UNIT

        OutlinedTextField(
            value = unit,
            onValueChange = {

                unit = it
                unitError = null
            },
            label = {
                Text("Unit")
            },
            isError = unitError != null,
            supportingText = {
                unitError?.let { Text(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(unitFocus),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    priceFocus.requestFocus()
                }
            )
        )

        Spacer(Modifier.height(8.dp))

        // PRICE

        OutlinedTextField(
            value = price,
            onValueChange = {

                price = it.filter { ch ->
                    ch.isDigit() || ch == '.'
                }

                priceError = null
            },
            label = {
                Text("Price")
            },
            isError = priceError != null,
            supportingText = {
                priceError?.let { Text(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(priceFocus),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    stockFocus.requestFocus()
                }
            )
        )

        Spacer(Modifier.height(8.dp))

        // STOCK

        OutlinedTextField(
            value = stock,
            onValueChange = {

                stock = it.filter { ch ->
                    ch.isDigit() || ch == '.'
                }

                stockError = null
            },
            label = {
                Text("Stock Available")
            },
            isError = stockError != null,
            supportingText = {
                stockError?.let { Text(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(stockFocus),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
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

                val priceValue = price.toDoubleOrNull()
                val stockValue = stock.toDoubleOrNull()

                var isValid = true

                if (name.isBlank()) {

                    nameError = "Name required"
                    isValid = false
                }

                if (unit.isBlank()) {

                    unitError = "Unit required"
                    isValid = false
                }

                if (priceValue == null || priceValue <= 0) {

                    priceError = "Enter valid price"
                    isValid = false
                }

                if (stockValue == null || stockValue < 0) {

                    stockError = "Enter valid stock"
                    isValid = false
                }

                if (isValid) {

                    onSave(
                        item.copy(
                            name = name.trim(),
                            unit = unit.trim(),
                            price = priceValue!!,
                            stockAvailable = stockValue!!
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