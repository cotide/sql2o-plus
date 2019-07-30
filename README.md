# sql2o-plus  


[![Build Status](https://travis-ci.org/cotide/sql2o-plus.svg?branch=master)](https://travis-ci.org/cotide/sql2o-plus)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven/apache-maven.svg)](https://search.maven.org/artifact/io.github.cotide/sql2o-plus/1.0.1/jar)
[![License](https://img.shields.io/badge/license-Apache2-blue.svg)](https://github.com/cotide/sql2o-plus/blob/master/LICENSE)
[![Gitter](https://img.shields.io/gitter/room/nwjs/nw.js.svg)](https://gitter.im/sql2o-plus/Lobby)

🍌sql2o-plus a simple object mapper for java 

> Java版本 1.8

## Maven 

> pom.xml 

 
### releases 版本

```xml 
<dependency>
    <groupId>io.github.cotide</groupId>
    <artifactId>sql2o-plus</artifactId>
    <version>1.0.2</version>
</dependency> 
```

### snapshots 版本

```xml
<repository>
    <id>oss-snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository> 
<dependency>
    <groupId>io.github.cotide</groupId>
    <artifactId>sql2o-plus</artifactId>
    <version>1.0.3-SNAPSHOT</version>
</dependency>
```

 
## 特点

- [sql2o](https://github.com/aaberg/sql2o)驱动
- 集成SQL Lambda表达式
- 集成SQL分页
- CRUD 封装/简化调用方法
- 支持事务

  

## 实体映射 

```java
package com.sqltest.model;

import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.basic.domain.Entity; 
import io.github.cotide.dapper.core.attr.Column;
import io.github.cotide.dapper.core.attr.Ignore;
import io.github.cotide.dapper.core.attr.PrimaryKey;
import io.github.cotide.dapper.core.attr.Table;

import java.util.Date;

/**
 * 用户信息
 */
@lombok.Getter
@lombok.Setter
@Table("user_info")
public class UserInfo extends Entity {

    @PrimaryKey("user_id")
    private int id;

    @Column("user_Name")
    private String name;

    @Column("password")
    private String pwd;

    @Column("status")
    private EnumUserStatus status;

    @Column("level")
    private EnumVipLevel level;

    @Column("`group`")
    private EnumGroup group;

    private int login;

    @Column("create_time")
    private Date createTime;

    @Ignore
    private String other;
}

```

### 枚举定义 

- IntegerEnum 整数型枚举存储-> [例子](src/test/java/com/sqltest/model/enums/EnumUserStatus.java)
- StringEnum 字符串型枚举存储-> [例子](src/test/java/com/sqltest/model/enums/EnumGroup.java)
 

### 注解描述

- @Table (表名)
- @PrimaryKey (主键)
- @Column (字段名,与数据库字段名称一致可不标记)
- @Ignore (忽略字段) 


## Dto实体

```java
package com.sqltest.dto;

import com.sqltest.model.enums.EnumGroup;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel; 
import lombok.Data;
import java.util.Date;


@Data
public class UserInfoDto {
 
    @Column("user_id")
    private int id;

    @Column("user_name")
    private String name;

    private int login;

    private EnumUserStatus status;

    @Column("`group`")
    private EnumGroup group;

    private EnumVipLevel level;

    private Date createTime;

    @Ignore
    private String other;
}
```

## 初始化


```java 

String url = "jdbc:mysql://192.168.1.100:3307/g_main_test?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
String user ="test";
String pass ="123456";

// Mysql
protected Database getDatabase() { 
   return new Database(url,user,pass);
} 

// Druid DataSource
protected Database getDruidDatabase() {
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(url);
    dataSource.setUsername(user);
    dataSource.setPassword(pass);
    return new Database(dataSource);
}

```

## 查询

### 列表查询

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);

// getList()
List<UserInfo> result1 =  userInfoRepository.getList();

// getList(Sql sql)
List<UserInfo> result2 = userInfoRepository.getList(Sql.builder().select().from(UserInfo.class));

// getList(String sql,Object ... param)
String sql = "select * from user_info where user_id = ? ";
List<UserInfo> result3 = userInfoRepository.getList(sql,1);

// ** Dto getList **
Sql sql1 = Sql.builder().select(" user_id as id, user_Name as name ").from(UserInfo.class).where("user_id = ?",1);
List<UserInfoDto> result4 =  db.getSqlQuery().getDtoList(UserInfoDto.class,sql1);

String sql2 = "select user_id as id, user_Name as name from user_info where user_id = ? ";
List<UserInfoDto> result5 =  db.getSqlQuery().getDtoList(UserInfoDto.class,sql2,1);
```

 

### 获取对象

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);

// getById(Object primaryKey)
UserInfo result1 =  userInfoRepository.getById(1);

// get(Sql sql)
UserInfo result2 = userInfoRepository.get(
        Sql.builder().select().from(UserInfo.class).where("user_id  = ?",1));

// get(String sql, Object ...  param)
String sql = "select * from user_info where user_id = ? ";
UserInfo result3 = userInfoRepository.get(sql,1);

// ** Dto get **
// getDto(Class<TDto> returnType, Sql sql)
UserInfoDto result4 = db.getSqlQuery().getDto(
        UserInfoDto.class,Sql.builder().select("user_id as id, user_Name as name").from(UserInfo.class).where("user_id  = ?",1));
```


### 联表查询

```java
Database db = getDatabase();
Sql sql = Sql.builder()
    .select("a.user_id as id," +
            "a.user_Name as name,"+
            "b.id as typeId,"+
            "b.name as typeName,"+
            "a.login,"+
            "a.status,"+
            "a.group,"+
            "a.level,"+
            "a.create_time as createTime")
    .from(UserInfo.class,"a")
    .join(UserType.class,"b")
    .on("a.user_type_id = b.id");
List<UserInfoDetailDto> result =  db.getSqlQuery(.getDtoList(UserInfoDetailDto.class,sql); 
```


### 分页

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
// getPageList(int pageIndex, int pageSize, Sql sql)
PageList<UserInfo> result = userInfoRepository.getPageList(1,10,Sql.builder().select().from(UserInfo.class));
```

## Sql Lambda

```java
Sql sql = Sql.builder()
          .select(UserInfo::getId,UserInfo::getName)
          .from(UserInfo.class)
          .where(UserInfo::getName,"Test")
          .whereIn(UserInfo::getId,1,2)
          .orderBy(UserInfo::getCreateTime, OrderBy.DESC);
                
/*** [Sql语句] ***/
// select user_id,user_name 
// FROM user_info 
// where user_name  = :p0 and user_id in (:p1,:p2)
// order by create_time DESC
/*** [参数值] ***/
// [Test],[1],[2]
```
 

## Dto Mapper

### ResultMap

```java
ResultMap column  = new ResultMap();
column.put(UserInfo::getId, UserInfoDto::getId);
column.put(UserInfo::getName, "name");
column.put(UserInfo::getUserTypeId); 

Sql sql = Sql.builder().select(
         column)
        .from(UserInfo.class)
        .where(UserInfo::getName,"Test")
        .whereIn(UserInfo::getId,1,2)
        .orderBy(UserInfo::getCreateTime, OrderBy.DESC); 

/*** [Sql语句] ***/
// select user_id  as id,
// user_name  as name,
// user_type_id 
// from user_info 
// where user_name  = :p0 
// and user_id in (:p1,:p2)
// order by create_time DESC
/*** [参数值] ***/
// [Test],[1],[2]
```

### SelectTo

```java
Sql sql = Sql.builder()
                .selectTo(UserInfoDto.class)
                .from(UserInfo.class)
                .where(UserInfo::getName,"Test")
                .whereIn(UserInfo::getId,1,2)
                .orderBy(UserInfo::getCreateTime, OrderBy.DESC);

/*** [Sql语句] ***/
// select createTime,
// level,
// `group` as group,
// user_id as id,
// user_name as name,
// login,
// status 
// from user_info 
// where user_name  = :p0 
// and user_id in (:p1,:p2)
// order by create_time DESC
/*** [参数值] ***/
// [Test],[1],[2]
``` 


## 持久化


### 新增

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
UserInfo domain = new UserInfo();
domain.setName("Test");
domain.setLogin(10086);
domain.setPwd("123456");
domain.setCreatTime(new Date());
UserInfo user =   userInfoRepository.create(domain);
```

### 修改

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
// get 
UserInfo user =  userInfoRepository.get(Sql.builder().select().from(UserInfo.class).where("user_id = ?",3399));
// update
user.setName("Test_2 ## -- ");
userInfoRepository.update(user); 
```

### 删除

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
// get
UserInfo user =  userInfoRepository.get(Sql.builder().select().from(UserInfo.class).where("user_id = ?",3391));
// delete
userInfoRepository.delete(user);
```

## 事务

### 事务支持

```java
try(Database db = getDatabase()){
   // 开启事务
   db.beginTransaction();
   IRepository<UserInfo> userInfoIRepository =
           db.getRepository(UserInfo.class); 
   UserInfo domain = new UserInfo();
   domain.setName("Test");
   domain.setLogin(10086);
   domain.setPwd("123456");
   domain.setCreateTime(new Date()); 
   // 新增
   UserInfo user = userInfoIRepository.create(domain); 
   user.setName("Test_Update");
   // 修改
   userInfoIRepository.update(user);  
   // 提交事务
   db.commit();
   assert(user.getId()>0):"database transaction is error";
}  
```


**注意：如果使用事务请使用try(){} 用于释放数据库连接**

## SQL语句执行

```java
try(Database db = getDatabase()){
   // 开启事务
   db.beginTransaction();
   final  String insertSql  =
           "INSERT INTO user_info (user_Name,password,login,create_time) VALUES (?,?,?,?)";
   // Create
   int id =  db.getSqlRun().execute(
           insertSql,
           "Execute Test",
           "123456",
           10086,
           new Date()).asInt();  
   // Update
   final String updateSql  =
           "UPDATE user_info set user_Name = ? WHERE user_id = ?";
   db.getSqlRun().execute(updateSql,"Execute Test2",id);
   // 事务提交
   db.commit();
   // Select
   Sql sql = Sql.builder()
           .select("user_id as id, user_Name as name")
           .from(UserInfo.class).where("user_id  = ?", id);
   UserInfoDto resultDto = db.getSqlQuery().getDto(UserInfoDto.class,sql);  
}
```

## Debug模式

```java
Database db = getDatabase();
db.isDebug(true); 
```
Database指定isDebug(true)后，查询结果属性不能匹配会抛出异常信息,[例子](src/test/java/com/sqltest/db/DebugTest.java)
 

## 获取表字段名

```java
Sql2oUtils.getColumnName(UserInfo::getId);
// 输出
user_id
```

## 其他

- [示例数据库脚本](https://github.com/cotide/sql2o-plus/wiki/%E7%A4%BA%E4%BE%8B%E6%95%B0%E6%8D%AE%E5%BA%93%E8%84%9A%E6%9C%AC)
- [示例项目](https://github.com/cotide/moni-webapi)

## License

- [Apache2](http://www.apache.org/licenses/LICENSE-2.0.txt)
