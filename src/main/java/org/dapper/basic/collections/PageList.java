package org.dapper.basic.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 */
public class PageList<T> {


    /**
     * 开始页码
     */
    private int pageIndex;

    /**
     * 分页大小
     */
    private int pageSize;


    /**
     * 总页数
     */
    private int totalPage;


    /**
     * 总条数
     */
    private long totalCount;


    /**
     * 数据列表
     */
    private List<T> items ;


    public PageList(
            List<T> list,
            int pageIndex,
            int pageSize,
            long totalCount)
    {
        if(list==null)
        {
            this.items = new ArrayList<T>();
        }else{
            this.items = list;
        }

        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalPage =  (int) (totalCount / pageSize + (totalCount % pageSize != 0 ? 1 : 0));
        this.totalCount = totalCount;
    }


    public PageList(
            List<T> list,
            int pageIndex,
            int pageSize)
    {
        this(list,pageIndex,pageSize,list.size());
    }


    //#region get And Set
    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    //#endregion
}
