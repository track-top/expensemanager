package com.naveenapps.expensemanager.feature.category.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.naveenapps.expensemanager.core.common.utils.UiState
import com.naveenapps.expensemanager.core.common.utils.toPercentString
import com.naveenapps.expensemanager.core.designsystem.AppPreviewsLightAndDarkMode
import com.naveenapps.expensemanager.core.designsystem.components.EmptyItem
import com.naveenapps.expensemanager.core.designsystem.ui.components.IconAndBackgroundView
import com.naveenapps.expensemanager.core.designsystem.ui.components.PieChartUiData
import com.naveenapps.expensemanager.core.designsystem.ui.components.PieChartView
import com.naveenapps.expensemanager.core.designsystem.ui.components.SmallIconAndBackgroundView
import com.naveenapps.expensemanager.core.designsystem.ui.extensions.toColor
import com.naveenapps.expensemanager.core.designsystem.ui.theme.ExpenseManagerTheme
import com.naveenapps.expensemanager.core.designsystem.ui.utils.ItemSpecModifier
import com.naveenapps.expensemanager.core.model.Amount
import com.naveenapps.expensemanager.core.model.CategoryTransaction
import com.naveenapps.expensemanager.core.model.CategoryTransactionUiModel
import com.naveenapps.expensemanager.core.model.CategoryType
import com.naveenapps.expensemanager.core.model.PieChartData
import com.naveenapps.expensemanager.core.model.isExpense
import com.naveenapps.expensemanager.feature.category.R
import com.naveenapps.expensemanager.feature.category.list.getCategoryData
import com.naveenapps.expensemanager.feature.datefilter.FilterView
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTransactionTabScreen(
    viewModel: CategoryTransactionListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.categoryTransaction.collectAsState()
    val categoryType by viewModel.categoryType.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.categories),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(onClick = viewModel::openCategoryList) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(id = R.string.edit),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::openTransactionCreatePage) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            CategoryTransactionListScreenContent(
                uiState = uiState,
                categoryType = categoryType,
                changeChart = viewModel::switchCategory,
                onItemClick = viewModel::openCategoryDetailsPage
            )
        }
    }
}

@Composable
private fun CategoryTransactionListScreenContent(
    uiState: UiState<CategoryTransactionUiModel>,
    categoryType: CategoryType,
    changeChart: (() -> Unit)? = null,
    onItemClick: ((CategoryTransaction) -> Unit)? = null,
) {
    when (uiState) {
        UiState.Empty -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    text = stringResource(id = R.string.no_transactions_available),
                    textAlign = TextAlign.Center,
                )
            }
        }

        UiState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.Center),
                )
            }
        }

        is UiState.Success -> {
            val indicationSource = remember { MutableInteractionSource() }

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    FilterView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 6.dp),
                    )
                }
                item {
                    PieChartView(
                        if (categoryType.isExpense()) {
                            stringResource(id = R.string.expense)
                        } else {
                            stringResource(id = R.string.income)
                        } + "\n" + uiState.data.totalAmount.amountString,
                        chartData = uiState.data.pieChartData.map {
                            PieChartUiData(
                                it.name,
                                it.value,
                                it.color.toColorInt(),
                            )
                        },
                        chartHeight = 600,
                        hideValues = uiState.data.hideValues,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp)
                            .clickable(indicationSource, null) {
                                changeChart?.invoke()
                            },
                    )
                }

                if (uiState.data.categoryTransactions.isEmpty()) {
                    item {
                        EmptyItem(
                            modifier = Modifier.fillMaxSize(),
                            emptyItemText = stringResource(id = R.string.no_transactions_available),
                            icon = com.naveenapps.expensemanager.core.designsystem.R.drawable.ic_no_grouping_available
                        )
                    }
                } else {
                    items(
                        uiState.data.categoryTransactions,
                        key = { it.category.id }
                    ) { categoryTransaction ->
                        CategoryTransactionItem(
                            modifier = Modifier
                                .clickable {
                                    onItemClick?.invoke(categoryTransaction)
                                }
                                .then(ItemSpecModifier),
                            name = categoryTransaction.category.name,
                            icon = categoryTransaction.category.storedIcon.name,
                            iconBackgroundColor = categoryTransaction.category.storedIcon.backgroundColor,
                            amount = categoryTransaction.amount.amountString ?: "",
                            percentage = categoryTransaction.percent,
                        )
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryTransactionItem(
    modifier: Modifier,
    name: String,
    icon: String,
    iconBackgroundColor: String,
    amount: String,
    percentage: Float,
) {
    Row(modifier = modifier) {
        IconAndBackgroundView(
            modifier = Modifier.align(Alignment.CenterVertically),
            icon = icon,
            iconBackgroundColor = iconBackgroundColor,
            name = name,
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp),
        ) {
            Row {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically),
                    text = amount,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Row {
                LinearProgressIndicator(
                    progress = percentage / 100,
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .align(Alignment.CenterVertically),
                    color = iconBackgroundColor.toColor(),
                    strokeCap = StrokeCap.Round,
                )
                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically),
                    text = percentage.toPercentString(),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

val getPieChartData = listOf(
    PieChartData("Chrome", 34.68f, "#43A546"),
    PieChartData("Firefox", 16.60F, "#F44336"),
    PieChartData("Safari", 16.15F, "#166EF7"),
    PieChartData("Internet Explorer", 15.62F, "#121212"),
)

fun getRandomCategoryTransactionData(): CategoryTransactionUiModel {
    return CategoryTransactionUiModel(
        pieChartData = getPieChartData,
        totalAmount = Amount(300.0, "300.00$"),
        categoryTransactions = buildList {
            repeat(15) {
                add(
                    CategoryTransaction(
                        category = getCategoryData(it, CategoryType.EXPENSE),
                        amount = Amount(300.0, "300.00$"),
                        percent = Random(100).nextFloat(),
                        transaction = emptyList(),
                    ),
                )
            }
        },
    )
}

@Composable
fun CategoryTransactionSmallItem(
    name: String,
    icon: String,
    iconBackgroundColor: String,
    amount: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        SmallIconAndBackgroundView(
            icon = icon,
            iconBackgroundColor = iconBackgroundColor,
            modifier = Modifier.align(Alignment.CenterVertically),
            name = name,
            iconSize = 12.dp,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp, end = 16.dp),
            text = name,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = amount,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@AppPreviewsLightAndDarkMode
@Composable
private fun CategoryTransactionSmallItemPreview() {
    ExpenseManagerTheme {
        CategoryTransactionSmallItem(
            modifier = Modifier
                .fillMaxWidth()
                .then(ItemSpecModifier),
            name = "Utilities",
            icon = "ic_calendar",
            iconBackgroundColor = "#000000",
            amount = "$100.00",
        )
    }
}

@Preview
@Composable
private fun CategoryTransactionItemPreview() {
    ExpenseManagerTheme {
        CategoryTransactionItem(
            modifier = Modifier
                .fillMaxWidth()
                .then(ItemSpecModifier),
            name = "Utilities",
            icon = "ic_calendar",
            iconBackgroundColor = "#000000",
            amount = "$100.00",
            percentage = 0.5f,
        )
    }
}

@AppPreviewsLightAndDarkMode
@Composable
private fun CategoryTransactionTabScreenPreview() {
    ExpenseManagerTheme {
        CategoryTransactionTabScreen()
    }
}
