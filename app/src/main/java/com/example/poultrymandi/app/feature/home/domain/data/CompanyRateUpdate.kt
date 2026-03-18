package com.example.poultrymandi.app.feature.home.domain.data

/**
 * Data model for company-specific rate updates.
 */
data class CompanyRateUpdate(
    val id: String,
    val city: String,
    val companyName: String,
    val category: String, // Broiler, Eggs, Chick
    val variety: String,  // Standard, Premium, etc.
    val rate: Double,
    val timestamp: String, // e.g., "10m ago"
    val isLive: Boolean = false
)

/**
 * Realistic mock data generator for 3 companies across all categories.
 */
fun getMockRateData(): List<CompanyRateUpdate> {
    return listOf(
        // --- IB Group ---
        CompanyRateUpdate("1", "Indore", "IB Group", "Broiler", "Standard", 102.0, "5m ago", true),
        CompanyRateUpdate("2", "Indore", "IB Group", "Broiler", "Standard", 105.0, "2h ago"),
        CompanyRateUpdate("3", "Indore", "IB Group", "Broiler", "Standard", 100.0, "5h ago"),
        CompanyRateUpdate("4", "Indore", "IB Group", "Broiler", "Standard", 98.0, "8h ago"),
        CompanyRateUpdate("5", "Indore", "IB Group", "Broiler", "Standard", 101.0, "12h ago"),
        
        CompanyRateUpdate("6", "Indore", "IB Group", "Eggs", "Premium", 4.55, "12m ago", true),
        CompanyRateUpdate("7", "Indore", "IB Group", "Eggs", "Premium", 4.50, "4h ago"),
        CompanyRateUpdate("8", "Indore", "IB Group", "Chick", "Day Old", 28.0, "1h ago"),
        
        // --- Venky's ---
        CompanyRateUpdate("9", "Indore", "Venky's", "Broiler", "Premium", 110.0, "8m ago", true),
        CompanyRateUpdate("10", "Indore", "Venky's", "Broiler", "Premium", 112.0, "3h ago"),
        CompanyRateUpdate("11", "Indore", "Venky's", "Broiler", "Premium", 108.0, "6h ago"),
        CompanyRateUpdate("12", "Indore", "Venky's", "Broiler", "Premium", 105.0, "10h ago"),
        CompanyRateUpdate("13", "Indore", "Venky's", "Broiler", "Premium", 107.0, "15h ago"),
        
        CompanyRateUpdate("14", "Indore", "Venky's", "Eggs", "Standard", 4.40, "15m ago", true),
        CompanyRateUpdate("15", "Indore", "Venky's", "Eggs", "Standard", 4.45, "5h ago"),
        CompanyRateUpdate("16", "Indore", "Venky's", "Chick", "Standard", 26.5, "2h ago"),

        // --- Suguna ---
        CompanyRateUpdate("17", "Indore", "Suguna", "Broiler", "Large", 98.0, "20m ago"),
        CompanyRateUpdate("18", "Indore", "Suguna", "Broiler", "Large", 95.0, "4h ago"),
        CompanyRateUpdate("19", "Indore", "Suguna", "Broiler", "Large", 95.0, "8h ago"),
        CompanyRateUpdate("20", "Indore", "Suguna", "Broiler", "Large", 92.0, "12h ago"),
        CompanyRateUpdate("21", "Indore", "Suguna", "Broiler", "Large", 94.0, "18h ago"),
        
        CompanyRateUpdate("22", "Indore", "Suguna", "Eggs", "Export", 4.65, "3m ago", true),
        CompanyRateUpdate("23", "Indore", "Suguna", "Eggs", "Export", 4.60, "1h ago"),
        CompanyRateUpdate("24", "Indore", "Suguna", "Chick", "Premium", 30.0, "30m ago")
    )
}
