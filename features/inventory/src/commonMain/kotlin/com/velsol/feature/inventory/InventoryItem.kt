package com.velsol.feature.inventory

internal enum class StockLevel { InStock, LowStock, OutOfStock }

internal data class InventoryItem(
    val name: String,
    val sku: String,
    val quantity: Int,
    val unit: String,
    val category: String,
    val stockLevel: StockLevel,
)

internal val mockInventory = listOf(
    InventoryItem("½\" PEX-A Tubing", "PEX-A-050-100", 240, "ft", "Pipe & Tubing", StockLevel.InStock),
    InventoryItem("Ball Valve 1\"", "BV-1IN-BRASS", 18, "pcs", "Valves", StockLevel.InStock),
    InventoryItem("SharkBite Push-Fit Elbow ½\"", "SB-ELB-050", 5, "pcs", "Fittings", StockLevel.LowStock),
    InventoryItem("Pipe Thread Sealant Tape", "PTFE-TAPE-BLK", 0, "rolls", "Sealants", StockLevel.OutOfStock),
    InventoryItem("Pressure Reducing Valve", "PRV-3QTR-ADJ", 7, "pcs", "Valves", StockLevel.InStock),
    InventoryItem("Flux Paste 4oz", "FLUX-4OZ-LF", 3, "jars", "Solder & Flux", StockLevel.LowStock),
)
