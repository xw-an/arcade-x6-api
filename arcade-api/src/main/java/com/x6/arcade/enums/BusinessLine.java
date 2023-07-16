package com.x6.arcade.enums;

public enum BusinessLine {

    // 1. 积分商城
    PointShop("积分商城", "PointShop"),
    // 2. 秒杀商城
    OmeShop("秒杀商城","OmeShop");


    private String businessLineName;
    private String businessLineValue;

    // get set方法
    public String getBusinessLineName() {
        return businessLineName;
    }

    public void setBusinessLineName(String businessLineName) {
        this.businessLineName = businessLineName;
    }

    public String getBusinessLineValue() {
        return businessLineValue;
    }

    public void setBusinessLineValue(String businessLineValue) {
        this.businessLineValue = businessLineValue;
    }


    BusinessLine(String businessLineName, String businessLineValue) {
        this.businessLineName = businessLineName;
        this.businessLineValue = businessLineValue;
    }

    // 根据业务线名称获取业务线值
    public static String getBusinessLineValue(String businessLineName) {
        BusinessLine[] businessLines = BusinessLine.values();
        for (BusinessLine businessLine : businessLines) {
            if (businessLine.getBusinessLineName().equals(businessLineName)) {
                return businessLine.getBusinessLineValue();
            }
        }
        return null;
    }


    // 根据业务线值获取业务线名称
    public static String getBusinessLineName(String businessLineValue) {
        BusinessLine[] businessLines = BusinessLine.values();
        for (BusinessLine businessLine : businessLines) {
            if (businessLine.getBusinessLineValue().equals(businessLineValue)) {
                return businessLine.getBusinessLineName();
            }
        }
        return null;
    }

    // 获取所有业务线名称
    public static String[] getAllBusinessLineName() {
        BusinessLine[] businessLines = BusinessLine.values();
        String[] businessLineNames = new String[businessLines.length];
        for (int i = 0; i < businessLines.length; i++) {
            businessLineNames[i] = businessLines[i].getBusinessLineName();
        }
        return businessLineNames;
    }

    // 获取所有业务线值
    public static String[] getAllBusinessLineValue() {
        BusinessLine[] businessLines = BusinessLine.values();
        String[] businessLineValues = new String[businessLines.length];
        for (int i = 0; i < businessLines.length; i++) {
            businessLineValues[i] = businessLines[i].getBusinessLineValue();
        }
        return businessLineValues;
    }
}
