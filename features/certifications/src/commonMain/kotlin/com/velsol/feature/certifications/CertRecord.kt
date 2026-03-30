package com.velsol.feature.certifications

enum class CertStatus { Active, Expiring, Expired }

data class CertRecord(
    val name: String,
    val technician: String,
    val expires: String,
    val status: CertStatus
)

internal val mockCertifications = listOf(
    CertRecord("EPA 608 Universal", "Alex Rivera", "Dec 2026", CertStatus.Active),
    CertRecord("NATE Core Excellence", "Jordan Kim", "Mar 2025", CertStatus.Expiring),
    CertRecord("R-410A Safety Handling", "Sam Torres", "Nov 2024", CertStatus.Expired),
    CertRecord("Gas Furnace Installation", "Casey Morgan", "Aug 2026", CertStatus.Active),
    CertRecord("Electrical Safety (OSHA)", "Drew Patel", "Jan 2027", CertStatus.Active),
)
