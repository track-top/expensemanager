package com.naveenapps.expensemanager.core.data.repository

import com.naveenapps.expensemanager.core.common.utils.AppCoroutineDispatchers
import com.naveenapps.expensemanager.core.model.CategoryType
import com.naveenapps.expensemanager.core.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: com.naveenapps.expensemanager.core.datastore.SettingsDataStore,
    private val dispatcher: AppCoroutineDispatchers
) : SettingsRepository {
    override fun getCategoryTypes(): Flow<List<CategoryType>?> {
        return dataStore.getCategoryTypes()
    }

    override suspend fun setCategoryTypes(categoryTypes: List<CategoryType>?): Resource<Boolean> =
        withContext(dispatcher.io) {
            dataStore.setCategoryTypes(categoryTypes)
            return@withContext Resource.Success(true)
        }

    override fun getAccounts(): Flow<List<String>?> {
        return dataStore.getAccounts()
    }

    override suspend fun setAccounts(accounts: List<String>?): Resource<Boolean> =
        withContext(dispatcher.io) {
            dataStore.setAccounts(accounts)
            return@withContext Resource.Success(true)
        }

    override fun getCategories(): Flow<List<String>?> {
        return dataStore.getCategories()
    }

    override suspend fun setCategories(categories: List<String>?): Resource<Boolean> =
        withContext(dispatcher.io) {
            dataStore.setCategories(categories)
            return@withContext Resource.Success(true)
        }

    override fun isFilterEnabled(): Flow<Boolean> {
        return dataStore.isFilterEnabled()
    }

    override suspend fun setFilterEnabled(filterEnable: Boolean): Resource<Boolean> =
        withContext(dispatcher.io) {
            dataStore.setFilterEnabled(filterEnable)
            return@withContext Resource.Success(true)
        }

    override fun isPreloaded(): Flow<Boolean> {
        return dataStore.isPreloaded()
    }

    override suspend fun setPreloaded(preloaded: Boolean): Resource<Boolean> =
        withContext(dispatcher.io) {
            dataStore.setPreloaded(preloaded)
            return@withContext Resource.Success(true)
        }

    override fun isOnboardingCompleted(): Flow<Boolean> {
        return dataStore.isOnboardingCompleted()
    }

    override suspend fun setOnboardingCompleted(isOnboardingCompleted: Boolean): Resource<Boolean> =
        withContext(dispatcher.io) {
            dataStore.setOnboardingCompleted(isOnboardingCompleted)
            return@withContext Resource.Success(true)
        }
}