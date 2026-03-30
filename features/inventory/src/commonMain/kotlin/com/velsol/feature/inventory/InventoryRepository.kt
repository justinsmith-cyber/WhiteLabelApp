package com.velsol.feature.inventory

internal interface InventoryRepository {
    fun getInventoryItems(): List<InventoryItem>

    fun getInventoryItem(sku: String): InventoryItem?
}

internal class DefaultInventoryRepository : InventoryRepository {
    override fun getInventoryItems(): List<InventoryItem> = mockInventory

    override fun getInventoryItem(sku: String): InventoryItem? = mockInventory.find { it.sku == sku }
}
