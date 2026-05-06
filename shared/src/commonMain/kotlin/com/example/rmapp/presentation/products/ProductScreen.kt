package com.example.rmapp.presentation.products

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
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component3
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component4
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rmapp.domain.model.Product
import com.example.rmapp.presentation.TopAppBarSection
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import rmapp.shared.generated.resources.Res
import rmapp.shared.generated.resources.ic_delete
import rmapp.shared.generated.resources.ic_edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    vm: ProductViewModel,
    navController: NavHostController
) {
    val products by vm.products.collectAsState()
    val search by vm.searchQuery.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBarSection(
                title = "Products",
                showBack = true,
                showItem = true,
                onBackClick = { navController.popBackStack() },
                onItemClick = {
                    vm.clearEdit()
                    selectedProduct = Product()
                }
            )
        }
    ) { padding ->

        Box(Modifier.fillMaxSize()) {

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
                    placeholder = { Text("Search products...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products, key = { it.id }) { product ->

                        SwipeToDeleteContainer(
                            onDelete = {

                                vm.delete(product)

                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "${product.name} deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Long
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        vm.undoDelete()
                                    } else {
                                        vm.confirmDelete()
                                    }
                                }
                            }
                        ) {
                            ProductCard(
                                product = product,
                                onEdit = {
                                    vm.edit(product)
                                    selectedProduct = product
                                }
                            )
                        }
                    }
                }
            }

            /* ───── BOTTOM SHEET ───── */
            if (selectedProduct != null) {
                ModalBottomSheet(
                    onDismissRequest = { selectedProduct = null },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true
                    )
                ) {
                    ProductForm(
                        product = selectedProduct!!,
                        onSave = {
                            vm.save(it)
                            selectedProduct = null
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
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
fun ProductCard(
    product: Product,
    onEdit: () -> Unit
) {
    val profit = product.sellingPrice - product.costPrice
    val profitPercent = if (product.costPrice != 0.0) {
        (profit / product.costPrice) * 100
    } else 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Column(modifier = Modifier.padding(14.dp)) {

            /* ───── TOP ROW ───── */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = "${product.name} - (${product.unit})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = "Cost",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "₹${product.costPrice}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        // RETAIL Block
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = "Retail",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "₹${product.sellingPrice}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (profit >= 0)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = "₹${profit} (${profitPercent.toInt()}%)",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(Modifier.width(8.dp))

                IconButton(onClick = onEdit) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_edit),
                        contentDescription = "Edit"
                    )
                }
            }
        }
    }
}

@Composable
fun PriceTag(label: String, value: String) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(text = value, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ProductForm(
    product: Product,
    onSave: (Product) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var unit by remember { mutableStateOf(product.unit) }
    var cost by remember {
        mutableStateOf(
            product.costPrice.takeIf { it != 0.0 }?.toString() ?: ""
        )
    }
    var sell by remember {
        mutableStateOf(
            product.sellingPrice.takeIf { it != 0.0 }?.toString() ?: ""
        )
    }

    // Validation states
    var nameError by remember { mutableStateOf<String?>(null) }
    var costError by remember { mutableStateOf<String?>(null) }
    var sellError by remember { mutableStateOf<String?>(null) }

    // Focus management
    val focusManager = LocalFocusManager.current
    val (nameFocus, unitFocus, costFocus, sellFocus) = FocusRequester.createRefs()

    Column(
        Modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

        Text("Product", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(12.dp))

        // NAME
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = null
            },
            label = { Text("Name") },
            isError = nameError != null,
            supportingText = { nameError?.let { Text(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocus),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { unitFocus.requestFocus() }
            )
        )

        Spacer(Modifier.height(8.dp))

        // UNIT
        OutlinedTextField(
            value = unit,
            onValueChange = { unit = it },
            label = { Text("Unit (kg, pcs, etc)") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(unitFocus),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { costFocus.requestFocus() }
            )
        )

        Spacer(Modifier.height(8.dp))

        // COST
        OutlinedTextField(
            value = cost,
            onValueChange = {
                cost = it.filter { ch -> ch.isDigit() || ch == '.' }
                costError = null
            },
            label = { Text("Cost Price") },
            isError = costError != null,
            supportingText = { costError?.let { Text(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(costFocus),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { sellFocus.requestFocus() }
            )
        )

        Spacer(Modifier.height(8.dp))

        // SELL
        OutlinedTextField(
            value = sell,
            onValueChange = {
                sell = it.filter { ch -> ch.isDigit() || ch == '.' }
                sellError = null
            },
            label = { Text("Selling Price") },
            isError = sellError != null,
            supportingText = { sellError?.let { Text(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(sellFocus),
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
                val costValue = cost.toDoubleOrNull()
                val sellValue = sell.toDoubleOrNull()

                var isValid = true

                if (name.isBlank()) {
                    nameError = "Name required"
                    isValid = false
                }

                if (costValue == null || costValue <= 0) {
                    costError = "Enter valid cost"
                    isValid = false
                }

                if (sellValue == null || sellValue <= 0) {
                    sellError = "Enter valid selling price"
                    isValid = false
                }

                if (isValid) {
                    onSave(
                        product.copy(
                            name = name.trim(),
                            unit = unit.trim(),
                            costPrice = costValue!!,
                            sellingPrice = sellValue!!
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