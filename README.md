# sql2o-plus  

[![Build Status](https://travis-ci.org/cotide/sql2o-plus.svg?branch=master)](https://travis-ci.org/cotide/sql2o-plus)
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
    <version>1.0.0</version>
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
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

 
## 特点

- [sql2o](https://github.com/aaberg/sql2o)驱动
- 集成SQL linq语法糖
- 集成SQL分页
- CRUD 封装/简化调用方法
- 支持事务

 


## 实体映射 

```java
package com.sqltest.model;
import com.sqltest.model.enums.EnumUserStatus;
import com.sqltest.model.enums.EnumVipLevel;
import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.basic.enums.EnumMapping;
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

    // 映射枚举规则
    @EnumMapping(EnumMapping.ORDINAL)
    private EnumUserStatus status;
     
    @Column("level")
    private EnumVipLevel level;

    private int login;

    @Column("create_time")
    private Date createTime;

    @Ignore
    private String other;
}

```

### 注解描述

- @Table (表名)
- @PrimaryKey (主键)
- @Column (字段名,与数据库字段名称一致可不标记)
- @Ignore (忽略字段)
- @EnumMapping (枚举映射)


## Dto实体

```java
package com.sqltest.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private int id;

    private String name;

    private int login;
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

wrapper
List<UserInfo> result2 = userInfoRepository.getList(Sql.builder().select().from(UserInfo.class));

wrapper
String sql = "select * from user_info where user_id = @0 ";
List<UserInfo> result3 = userInfoRepository.getList(sql,1);

// ** Dto getList **
wrapper
Sql sql1 = Sql.builder().select(" user_id as id, user_Name as name ").from(UserInfo.class).where("user_id = @0",1);
List<UserInfoDto> result4 =  db.getSqlQuery().getDtoList(UserInfoDto.class,sql1);

wrapper
String sql2 = "select user_id as id, user_Name as name from user_info where user_id = @0 ";
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
        Sql.builder().select().from(UserInfo.class).where("user_id  = @0",1));

// get(String sql, Object ...  param)
String sql = "select * from user_info where user_id = @0 ";
UserInfo result3 = userInfoRepository.get(sql,1);

// ** Dto get **
// getDto(Class<TDto> returnType, Sql sql)
UserInfoDto result4 = db.getSqlQuery().getDto(
        UserInfoDto.class,Sql.builder().select("user_id as id, user_Name as name").from(UserInfo.class).where("user_id  = @0",1));
```

### 分页

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
// getPageList(int pageIndex, int pageSize, Sql sql)
PageList<UserInfo> result = userInfoRepository.getPageList(1,10,Sql.builder().select().from(UserInfo.class));
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
UserInfo user =  userInfoRepository.get(Sql.builder().select().from(UserInfo.class).where("user_id = @0",3399));
// update
user.setName("Test_2 ## -- ");
userInfoRepository.update(user); 
```

### 删除

```java
Database db = getDatabase();
IRepository<UserInfo> userInfoRepository =  db.getRepository(UserInfo.class);
// get
UserInfo user =  userInfoRepository.get(Sql.builder().select().from(UserInfo.class).where("user_id = @0",3391));
// delete
userInfoRepository.delete(user);
```

## 事务

### 事务支持

```java
try(Database db = getDatabase()){
   IRepository<UserInfo> userInfoIRepository =
           db.getRepository(UserInfo.class); 
   // 开启事务
   db.beginTransaction();
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
           "INSERT INTO user_info (user_Name,password,login,create_time) VALUES (@0,@1,@2,@3)";
   // Create
   int id =  db.getSqlRun().execute(
           insertSql,
           "Execute Test",
           "123456",
           10086,
           new Date()).asInt();  
   // Update
   final String updateSql  =
           "UPDATE user_info set user_Name = @0 WHERE user_id = @1";
   db.getSqlRun().execute(updateSql,"Execute Test2",id);
   // 事务提交
   db.commit();
   // Select
   Sql sql = Sql.builder()
           .select("user_id as id, user_Name as name")
           .from(UserInfo.class).where("user_id  = @0", id);
   UserInfoDto resultDto = db.getSqlQuery().getDto(UserInfoDto.class,sql);  
}
```


## 注意问题

如果使用SpringBoot时,需要指定JPA版本，不然@Column不会被解析映射到POJO对象

```json
<dependency>
    <scope>provided</scope>
    <groupId>javax.persistence</groupId>
    <artifactId>persistence-api</artifactId>
    <version>1.0.2</version>
</dependency> 
```


## License

[Apache2](http://www.apache.org/licenses/LICENSE-2.0.txt)
