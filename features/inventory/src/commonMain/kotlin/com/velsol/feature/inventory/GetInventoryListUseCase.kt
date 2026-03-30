package com.velsol.feature.inventory

internal class GetInventoryListUseCase(private val repository: InventoryRepository) {
    operator fun invoke(): List<InventoryItem> = repository.getInventoryItems()
}
