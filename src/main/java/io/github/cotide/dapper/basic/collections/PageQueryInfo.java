package io.github.cotide.dapper.basic.collections;

public class PageQueryInfo {
    private String countSql;
    private String pageSql;

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }

    public String getPageSql() {
        return pageSql;
    }

    public void setPageSql(String pageSql) {
        this.pageSql = pageSql;
    }
}