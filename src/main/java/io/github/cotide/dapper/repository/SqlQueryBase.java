package io.github.cotide.dapper.repository;

import io.github.cotide.dapper.core.unit.Sql2oCache;
import io.github.cotide.dapper.repository.base.SqlBase;
import io.github.cotide.dapper.basic.collections.PageList;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import io.github.cotide.dapper.core.unit.IUnitOfWork;
import io.github.cotide.dapper.basic.collections.PageQueryInfo;
import io.github.cotide.dapper.query.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * CRUD 仓储 实例
 * @author cotide
 */
public class SqlQueryBase  extends SqlBase {


    /**
     * 构造函数
     * @param unitOfWork
     */
    public SqlQueryBase(IUnitOfWork unitOfWork) {
         super(unitOfWork);
    }



    //#region 获取列表数据

    /**
     * 获取实体列表数据
     * @param returnType
     * @param sql
     * @param <TDto>
     * @return
     */
    public <TDto> List<TDto> getDtoList(
    Class<TDto> returnType,
    Sql sql)
    {
        try {
            return  createQuery(
                    returnType,
                    sql.getFinalSql(),
                    sql.getFinalArgs())
                    .executeAndFetch(returnType);
        } finally {
            UnitOfWork.close();
        }
    }


    /**
     * 获取实体列表数据
     * @param returnType
     * @param sql
     * @param param
     * @param <TDto>
     * @return
     */
    public  <TDto> List<TDto> getDtoList(
    Class<TDto> returnType,
    String sql,
    Object... param)
    {
        return this.getDtoList(returnType,new Sql(sql,param));
    }

    //#endregion

    //#region 获取单条数据
    /**
     * 获取实体数据
     * @param returnType
     * @param sql
     * @param <TDto>
     * @return
     */
    public <TDto> TDto getDto(
            Class<TDto> returnType,
            Sql sql) {

            try {
                return createQuery(
                        returnType,
                        sql.getFinalSql(),
                        sql.getFinalArgs())
                        .executeAndFetchFirst(returnType);
            } finally {
                UnitOfWork.close();
            }
    }


    /**
     * 获取实体数据
     * @param returnType
     * @param sql
     * @param param
     * @param <TDto>
     * @return
     */
    public <TDto> TDto getDto(
            Class<TDto> returnType,
            String sql,
            Object... param)
    {
        return  this.getDto(returnType,new Sql(sql,param));
    }

    /**
     * 统计总数
     * @param sql
     * @return
     */
    public int count(
            Sql sql)
    {
        try {
             Integer result = createQuery(sql.getFinalSql(), sql.getFinalArgs())
                    .executeScalar(Integer.class);
             return result==null?0:result;
        } finally {
             UnitOfWork.close();
        }
    }



    //#region 分页获取数据


    public <TDto> PageList<TDto> getPageDtoList(Class<TDto> returnType, int pageIndex, int pageSize, Sql sql) {
        return getPageDtoList(returnType,pageIndex,pageSize,sql.getFinalSql());
    }

    public <TDto> PageList<TDto> getPageDtoList(Class<TDto> returnType, int pageIndex, int pageSize, String sql, Object... param) {
        sql = addSelectClause(returnType, sql);
        PageQueryInfo queryInfo = buildPagingQueris((pageIndex - 1) * pageSize, pageSize, sql);
        Integer count =  getDto(Integer.class,queryInfo.getCountSql(),param);
        if(count==null||count<=0)
        {
            return new PageList<>(new ArrayList<>(), pageIndex, pageSize, 0);
        }else {
            List<TDto>  list = getDtoList(returnType,queryInfo.getPageSql(),param);
            PageList<TDto> result = new PageList<>(list, pageIndex, pageSize, count);
            return result;
        }
    }

    //#endregion


    //#region Helper



    private final static Pattern selectPattern = Pattern.compile("\\s*(SELECT|EXECUTE|CALL)\\s", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
    private final static Pattern fromPattern = Pattern.compile("\\s*FROM\\s", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * 自动增加SELECT/FROM语句，以及列名
     *
     * @param type 类型
     * @param sql  SQL
     * @param <T>  泛型
     * @return SQL
     */
    private <T> String addSelectClause(Class<T> type, String sql) {
        if (sql.startsWith(";")) {
            return sql.substring(1);
        }
        Matcher selectMatcher = selectPattern.matcher(sql);
        Matcher fromMatcher = fromPattern.matcher(sql);

        if (!selectMatcher.find()) {

            StringBuffer cols = new StringBuffer();
            Map<String, String> colums =  Sql2oCache.computeModelColumnMappings(type);
            for (Map.Entry<String, String> entry : colums.entrySet()) {
                cols.append(entry.getValue()).append(",");
            }
            if (fromMatcher.find()) {
                sql = String.format("SELECT %s %s", cols.substring(0, cols.length() - 1), sql);
            } else {
                sql = String.format("SELECT %s FROM %s %s", cols.substring(0, cols.length() - 1), Sql2oCache.getTableName(type), sql);
            }
        }

        return sql;
    }


    private final static Pattern PATTERN_BRACKET = Pattern.compile("(\\(|\\)|[^\\(\\)]*)");
    private final static Pattern PATTERN_SELECT = Pattern.compile("select([\\W\\w]*)from", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS | Pattern.MULTILINE);
    private final static Pattern PATTERN_DISTINCT = Pattern.compile("\\ADISTINCT\\s", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS | Pattern.MULTILINE);

    /**
     * 组装分页SQL
     *
     * @param skip skip
     * @param take 提取的条数
     * @param sql  SQL
     * @return 分页SQL(获取总数的SQL，获取列表的SQL)
     */
    private PageQueryInfo buildPagingQueris(long skip, int take, String sql)   {
        // 匹配所有括号
        Matcher matcherBracket = PATTERN_BRACKET.matcher(sql);
        String sqlReplace = sql;
        if (matcherBracket.find()) {
            matcherBracket.reset();
            List<String> tempList = new ArrayList<>();
            while (matcherBracket.find()) {
                tempList.add(matcherBracket.group());
            }

            List<String> copyList = new ArrayList<>();
            tempList.forEach(copyList::add);

            while (true) {
                int endIndex = IntStream.range(0, copyList.size())
                        .filter(i -> copyList.get(i).contains(")"))
                        .findFirst()
                        .orElse(-1);
                if (endIndex < 0)
                {
                    break;
                }
                int startIndex = IntStream.range(0, endIndex)
                        .filter(i -> copyList.get(i).contains("("))
                        .reduce((a, b) -> b)
                        .orElse(-1);
                if (startIndex < 0)
                {
                    break;
                }

                IntStream.range(startIndex, endIndex + 1)
                        .forEach(i -> copyList.set(i, ""));
            }

            int start = IntStream.range(0, copyList.size())
                    .filter(i -> copyList.get(i).toLowerCase().contains("select"))
                    .findFirst()
                    .orElse(-1);
            int end = IntStream.range(0, copyList.size())
                    .filter(i -> copyList.get(i).toLowerCase().contains("from"))
                    .findFirst()
                    .orElse(-1);
            if (start < 0 || end < 0)
            {
                try {
                    throw new SqlBuildException("build paging querySql error:no select or no from");
                } catch (SqlBuildException e) {
                    e.printStackTrace();
                }
            }
            sqlReplace = "";
            for (int i = start; i <= end; i++) {
                sqlReplace += tempList.get(i);
            }
        }

        Matcher matcherSelect = PATTERN_SELECT.matcher(sqlReplace);
        if (!matcherSelect.find()) {
            try {
                throw new SqlBuildException("build paging querySql error:canot find select from");
            } catch (SqlBuildException e) {
                e.printStackTrace();
            }
        }

        String sqlSelectCols = null;
        int colsStartIndex = -1;
        int colsEndIndex = -1;
        sqlSelectCols = matcherSelect.group(1);
        colsStartIndex = matcherSelect.start(1);
        colsEndIndex = matcherSelect.end(1);

        String countSql = "";
        Matcher matcherDistinct = PATTERN_DISTINCT.matcher(sqlSelectCols);
        if (matcherDistinct.find()) {
            countSql = String.format("%s COUNT(%s) \n%s", sql.substring(0, colsStartIndex), sqlSelectCols, sql.substring(colsEndIndex));
        } else {
            countSql = String.format("%s COUNT(1) \n%s", sql.substring(0, colsStartIndex), sql.substring(colsEndIndex));
        }

        String pageSql = String.format("%s \nLIMIT %s OFFSET %s", sql, take, skip);

        PageQueryInfo queryInfo = new PageQueryInfo();
        queryInfo.setPageSql(pageSql);
        queryInfo.setCountSql(countSql);
        return queryInfo;
    }

    //#endregion
}
