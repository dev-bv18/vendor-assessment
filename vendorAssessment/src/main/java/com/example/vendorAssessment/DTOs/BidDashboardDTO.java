package com.example.vendorAssessment.DTOs;

import com.example.vendorAssessment.entities.BidStatus;
import com.example.vendorAssessment.entities.BidType;

import java.util.List;

// Dashboard DTO
public class BidDashboardDTO {
    private long totalBids;
    private long activeBids;
    private long completedBids;
    private long pendingReview;
    private Double totalValue;
    private List<BidStatusCount> statusCounts;
    private List<BidTypeCount> typeCounts;

    // Getters and Setters
    public long getTotalBids() { return totalBids; }
    public void setTotalBids(long totalBids) { this.totalBids = totalBids; }

    public long getActiveBids() { return activeBids; }
    public void setActiveBids(long activeBids) { this.activeBids = activeBids; }

    public long getCompletedBids() { return completedBids; }
    public void setCompletedBids(long completedBids) { this.completedBids = completedBids; }

    public long getPendingReview() { return pendingReview; }
    public void setPendingReview(long pendingReview) { this.pendingReview = pendingReview; }

    public Double getTotalValue() { return totalValue; }
    public void setTotalValue(Double totalValue) { this.totalValue = totalValue; }

    public List<BidStatusCount> getStatusCounts() { return statusCounts; }
    public void setStatusCounts(List<BidStatusCount> statusCounts) { this.statusCounts = statusCounts; }

    public List<BidTypeCount> getTypeCounts() { return typeCounts; }
    public void setTypeCounts(List<BidTypeCount> typeCounts) { this.typeCounts = typeCounts; }

    public static class BidStatusCount {
        private BidStatus status;
        private long count;

        public BidStatusCount(BidStatus status, long count) {
            this.status = status;
            this.count = count;
        }

        public BidStatus getStatus() { return status; }
        public void setStatus(BidStatus status) { this.status = status; }

        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }

    public static class BidTypeCount {
        private BidType type;
        private long count;

        public BidTypeCount(BidType type, long count) {
            this.type = type;
            this.count = count;
        }

        public BidType getType() { return type; }
        public void setType(BidType type) { this.type = type; }

        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
    }
}
