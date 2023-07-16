package com.x6.arcade.enums;

public enum Department {

    // 1. 风控部门
    AviraTool("风控", "AviraTool"),
    // 2. 大数据
    BigDataTool("大数据","BigDataTool"),
    // 3. 司机
    DriverTool("司机","DriverTool"),
    // 4. 金融
    FinanceTool("金融","FinanceTool"),
    // 5. 国际化
    GlobalTool("国际化","GlobalTool"),
    // 6. 地图
    LbsTool("地图","LbsTool"),
    // 7. 消息
    MessageTool("消息","MessageTool"),
    // 8. 增长中台
    OperateTool("增长中台","OperateTool"),
    // 9. 订单
    OrderTool("订单","OrderTool"),
    // 10. 用户
    UserTool("用户","UserTool"),
    // 11. 公共
    UtilTool("公共","UtilTool");

    private String departmentName;
    private String departmentValue;

    // get set方法
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentValue() {
        return departmentValue;
    }

    public void setDepartmentValue(String departmentValue) {
        this.departmentValue = departmentValue;
    }


    Department(String departmentName, String departmentValue) {
        this.departmentName = departmentName;
        this.departmentValue = departmentValue;
    }

    // 根据部门名称获取部门值
    public static String getDepartmentValue(String departmentName) {
        Department[] departments = Department.values();
        for (Department department : departments) {
            if (department.getDepartmentName().equals(departmentName)) {
                return department.getDepartmentValue();
            }
        }
        return null;
    }

    // 根据部门值获取部门名称
    public static String getDepartmentName(String departmentValue) {
        Department[] departments = Department.values();
        for (Department department : departments) {
            if (department.getDepartmentValue().equals(departmentValue)) {
                return department.getDepartmentName();
            }
        }
        return null;
    }

    // 获取所有部门名称
    public static String[] getAllDepartmentName() {
        Department[] departments = Department.values();
        String[] departmentNames = new String[departments.length];
        for (int i = 0; i < departments.length; i++) {
            departmentNames[i] = departments[i].getDepartmentName();
        }
        return departmentNames;
    }

    // 获取所有部门值
    public static String[] getAllDepartmentValue() {
        Department[] departments = Department.values();
        String[] departmentValues = new String[departments.length];
        for (int i = 0; i < departments.length; i++) {
            departmentValues[i] = departments[i].getDepartmentValue();
        }
        return departmentValues;
    }
}
