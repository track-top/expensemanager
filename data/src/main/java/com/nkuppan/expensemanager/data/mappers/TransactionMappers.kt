package com.nkuppan.expensemanager.data.mappers

import com.nkuppan.expensemanager.core.model.Transaction
import com.nkuppan.expensemanager.data.db.entity.TransactionEntity

fun Transaction.toEntityModel(): TransactionEntity {
    return TransactionEntity(
        id = id,
        notes = notes,
        categoryId = categoryId,
        accountId = accountId,
        amount = amount,
        imagePath = imagePath,
        createdOn = createdOn,
        updatedOn = updatedOn,
    )
}

fun TransactionEntity.toDomainModel(): Transaction {
    return Transaction(
        id = id,
        notes = notes,
        categoryId = categoryId,
        accountId = accountId,
        amount = amount,
        imagePath = imagePath,
        createdOn = createdOn,
        updatedOn = updatedOn
    )
}