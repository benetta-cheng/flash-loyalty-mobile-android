package com.ibm2105.loyaltyapp;

class PointsListItem {
    private String rewardName;
    private int pointsImage, pointsValue;

    public PointsListItem(String rewardName, int pointsImage, int pointsValue) {
        this.rewardName = rewardName;
        this.pointsImage = pointsImage;
        this.pointsValue = pointsValue;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public int getPointsImage() {
        return pointsImage;
    }

    public void setPointsImage(int pointsImage) {
        this.pointsImage = pointsImage;
    }

    public int getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(int pointsValue) {
        this.pointsValue = pointsValue;
    }
}
