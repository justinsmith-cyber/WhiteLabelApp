package com.velsol.feature.inventory

internal class GetInventoryItemUseCase(private val repository: InventoryRepository) {
    operator fun invoke(sku: String): InventoryItem? = repository.getInventoryItem(sku)
}
