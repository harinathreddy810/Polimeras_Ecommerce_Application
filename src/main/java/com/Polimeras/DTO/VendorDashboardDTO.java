package com.Polimeras.DTO;

import java.util.List;
import java.util.Map;

public class VendorDashboardDTO {
    private String vendorName;
    private int totalProducts;
    private double totalSales;
    private long totalOrders;
    private List<ProductDTO> products;
    private List<OrderDTO> recentOrders;
    private SalesChartData salesChartData;
    private ProductStats productStats;

    public VendorDashboardDTO(String vendorName, int totalProducts, double totalSales,
                            long totalOrders, List<ProductDTO> products, 
                            List<OrderDTO> recentOrders,
                            SalesChartData salesChartData,
                            ProductStats productStats) {
        this.vendorName = vendorName;
        this.totalProducts = totalProducts;
        this.totalSales = totalSales;
        this.totalOrders = totalOrders;
        this.products = products;
        this.recentOrders = recentOrders;
        this.salesChartData = salesChartData;
        this.productStats = productStats;
    }

    // Nested classes
    public static class SalesChartData {
        private List<String> labels;
        private List<Double> sales;
        
        public SalesChartData(List<String> labels, List<Double> sales) {
            this.labels = labels;
            this.sales = sales;
        }
        
        public List<String> getLabels() { return labels; }
        public List<Double> getSales() { return sales; }
    }

    public static class ProductStats {
        private long activeProducts;
        private long outOfStockProducts;
        private Map<String, Long> productsByCategory;
        
        public ProductStats(long activeProducts, long outOfStockProducts, 
                          Map<String, Long> productsByCategory) {
            this.activeProducts = activeProducts;
            this.outOfStockProducts = outOfStockProducts;
            this.productsByCategory = productsByCategory;
        }
        
        public long getActiveProducts() { return activeProducts; }
        public long getOutOfStockProducts() { return outOfStockProducts; }
        public Map<String, Long> getProductsByCategory() { return productsByCategory; }
    }

    // Getters
    public String getVendorName() { return vendorName; }
    public int getTotalProducts() { return totalProducts; }
    public double getTotalSales() { return totalSales; }
    public long getTotalOrders() { return totalOrders; }
    public List<ProductDTO> getProducts() { return products; }
    public List<OrderDTO> getRecentOrders() { return recentOrders; }
    public SalesChartData getSalesChartData() { return salesChartData; }
    public ProductStats getProductStats() { return productStats; }
}