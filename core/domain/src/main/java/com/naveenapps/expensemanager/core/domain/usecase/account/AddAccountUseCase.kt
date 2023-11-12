package com.naveenapps.expensemanager.core.domain.usecase.account

import com.naveenapps.expensemanager.core.data.repository.AccountRepository
import com.naveenapps.expensemanager.core.model.Account
import com.naveenapps.expensemanager.core.model.Resource
import javax.inject.Inject

class AddAccountUseCase @Inject constructor(
    private val repository: AccountRepository,
    private val checkAccountValidationUseCase: CheckAccountValidationUseCase
) {
    suspend operator fun invoke(account: Account): Resource<Boolean> {
        return when (val validationResult = checkAccountValidationUseCase(account)) {
            is Resource.Error -> {
                validationResult
            }

            is Resource.Success -> {
                repository.addAccount(account)
            }
        }
    }
}