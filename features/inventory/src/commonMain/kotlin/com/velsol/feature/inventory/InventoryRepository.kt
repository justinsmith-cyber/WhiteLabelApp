package com.velsol.feature.inventory

interface InventoryRepository {
    fun getInventoryItems(): List<InventoryItem>

    fun getInventoryItem(sku: String): InventoryItem?
}

fun createInventoryRepository(): InventoryRepository = DefaultInventoryRepository()

internal class DefaultInventoryRepository : InventoryRepository {
    override fun getInventoryItems(): List<InventoryItem> = mockInventory

    override fun getInventoryItem(sku: String): InventoryItem? = mockInventory.find { it.sku == sku }
}
