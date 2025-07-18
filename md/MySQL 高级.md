# MySQL 高级



## 1、MySQL架构介绍

1. **连接层**：最上层是一些客户端和连接服务，包含本地sock通信和大多数基于客户端/服务端工具实现的类似于tcp/ip 的通信。主要完成一些类似于连接处理、授权认证、以及相关的安全方案。在该层上引入了线程池的概念，为通过认证安全接入的客户端提供线程。同样在该层上可以实现基于SSL 的安全连接。服务器也会为安全接入的每个客户端验证它所具有的操作权限
2. **服务层**：第二层架构主要完成大多数的核心服务功能，如SQL接口，并完成缓存查询的实现，SQL 的分析以及部分内置函数的执行。所有跨存储引擎的功能也是在这一层实现，如过程、函数等、在该层，服务器会解析查询并创建相应的内部解析树，并对其完成相应的优化如确定查询表的顺序，是否利用索引等，最后生成相应的执行操作。如果是select语句，服务器还会查询内部的缓存。如果缓存空间足够大，这样在解决大量读操作的环境中能够很好的提升系统的性能
3. **引擎层**：存储引擎真正赋值了MySql 中数据的存储和提前，服务器通过API与存储引擎进行通信。不同发的存储引擎具有的功能不同，这样我们可以根据自己的实际需要进行选取。
4. **存储层**：数据存储层，主要是将数据存储在运行于裸设备的文件系统上，并完成与存储引擎的交互



## 2、MyISAM 和 InnoDB 对比



| 对比项   | MyISAM                               | InnoDB                                                       |
| -------- | ------------------------------------ | ------------------------------------------------------------ |
| 主外键   | 不支持                               | 支持                                                         |
| 事务     | 不支持                               | 支持                                                         |
| 行表锁   | 表锁，即使操作一条记录也会锁住整个表 | 行锁，操作时只锁某一行，不对其他行有影响                     |
| 缓存     | 只缓存索引，不缓存真实数据           | 不仅缓存真实数据，对内存要求较高，而且内存大小对性能有决定性作用 |
| 表空间   | 小                                   | 大                                                           |
| 关注点   | 性能                                 | 事务                                                         |
| 默认安装 | Y                                    | Y                                                            |

### InnoDB

- MySQL从3.23.34a开始就包含InnoDB存储引擎。 大于等于5.5之后，默认采用InnoDB引擎 。 
- InnoDB是MySQL的 默认事务型引擎 ，它被设计用来处理大量的短期(short-lived)事务。可以确保事务 的完整提交(Commit)和回滚(Rollback)。 
- 除了增加和查询外，还需要更新、删除操作，那么，应优先选择InnoDB存储引擎。 
- 除非有非常特别的原因需要使用其他的存储引擎，否则应该优先考虑InnoDB引擎。 
- 数据文件结构：（在《第02章_MySQL数据目录》章节已讲） 
  - 表名.frm 存储表结构（MySQL8.0时，合并在表名.ibd中） 
  - 表名.ibd 存储数据和索引
- InnoDB是 为处理巨大数据量的最大性能设计 。
  -  在以前的版本中，字典数据以元数据文件、非事务表等来存储。现在这些元数据文件被删除 了。比如： .frm ， .par ， .trn ， .isl ， .db.opt 等都在MySQL8.0中不存在了。 
- 对比MyISAM的存储引擎， InnoDB写的处理效率差一些 ，并且会占用更多的磁盘空间以保存数据和 索引。 
- MyISAM只缓存索引，不缓存真实数据；InnoDB不仅缓存索引还要缓存真实数据， 对内存要求较 高 ，而且内存大小对性能有决定性的影响。



### MyISAM

- MyISAM提供了大量的特性，包括全文索引、压缩、空间函数(GIS)等，但MyISAM 不支持事务、行级 锁、外键 ，有一个毫无疑问的缺陷就是 崩溃后无法安全恢复 。
- 优势是访问的 速度快 ，对事务完整性没有要求或者以SELECT、INSERT为主的应用
- 针对数据统计有额外的常数存储。故而 count(*) 的查询效率很高
- 数据文件结构：
  - 表名.frm 存储表结构 
  - 表名.MYD 存储数据 (MYData) 
  - 表名.MYI 存储索引 (MYIndex）
- 应用场景：只读应用或者以读为主的业务



## 3. 索引的数据结构

#### 索引的本质

MySQL官方对索引的定义为：索引（Index）是帮助MySQL高效获取数据的数据结构。 

**索引的本质：**索引是数据结构。你可以简单理解为“排好序的快速查找数据结构”，满足特定查找算法。 这些数据结构以某种方式指向数据， 这样就可以在这些数据结构的基础上实现 高级查找算法 。

#### 优点

（1）类似大学图书馆建书目索引，提高数据检索的效率，降低 数据库的IO成本 ，这也是创建索引最主 要的原因。 

（2）通过创建唯一索引，可以保证数据库表中每一行 数据的唯一性 。 

（3）在实现数据的 参考完整性方面，可以 加速表和表之间的连接 。换句话说，对于有依赖关系的子表和父表联合查询时， 可以提高查询速度。 

（4）在使用分组和排序子句进行数据查询时，可以显著 减少查询中分组和排序的时 间 ，降低了CPU的消耗。

#### 缺点

（1）创建索引和维护索引要 耗费时间 ，并 且随着数据量的增加，所耗费的时间也会增加。 

（2）索引需要占 磁盘空间 ，除了数据表占数据空间之 外，每一个索引还要占一定的物理空间， 存储在磁盘上 ，如果有大量的索引，索引文件就可能比数据文 件更快达到最大文件尺寸。 

（3）虽然索引大大提高了查询速度，同时却会 降低更新表的速度 。当对表 中的数据进行增加、删除和修改的时候，索引也要动态地维护，这样就降低了数据的维护速度。

#### B+ 树

![image-20220427193109782](typora-user-images\image-20220427193109782.png)



#### B 树

![image-20220427194104917](typora-user-images\image-20220427194104917.png)

![image-20220427194204929](typora-user-images\image-20220427194204929.png)



#### B 树 和B+ 树的对比

1. B+树内节点不存储数据，所有 data 存储在叶节点导致查询时间复杂度固定为 log n。而B-树查询时间复杂度不固定，与 key 在树中的位置有关，最好为O(1)。
2.  B+树叶节点两两相连可大大增加区间访问性，可使用在范围查询等，而B-树每个节点 key 和 data 在一起，则无法区间查找。
3. B+树更适合外部存储。由于内节点无 data 域，每个节点能索引的范围更大更精确

#### 索引分类

1. 聚簇索引
2. 二级索引
3. 联合索引

## 4、三大范式

1. 第一范式是最基本的范式。如果数据库表中的所有字段值都是不可分解的原子值，就说明该数据库表满足了第一范式。
2. 第二范式在第一范式的基础之上更进一层。第二范式需要确保数据库表中的每一列都和主键相关，而不能只与主键的某一部分相关（主要针对联合主键而言）。也就是说在一个数据库表中，一个表中只能保存一种数据，不可以把多种数据保存在同一张数据库表中。
3. 第三范式需要确保数据表中的每一列数据都和主键直接相关，而不能间接相关。

## 5、关于优化

创建索引

```sql
create index idx_user_name on user(name)
```



性能下降原因：

- 查询语句写的不好
- 索引失效
- 关联查询太多join（设计缺陷或者不得已的需求）
- 服务器调优以及各个参数设置（缓存、线程数等）



SQL执行顺序 

1. from
2. on
3. join
4. where
5. group by
6. having
7. select
8. distinct
9. order by
10. limit

### 索引失效



最佳左前缀法则

主键的插入顺序，**页分裂的问题**

计算、函数、类型转换导致索引失效

范围条件右边的索引失效

使用不等于无法使用索引

like 以通配符%开头

or 前后存在非索引的列，索引失效



### 关联查询优化

采用左外连接时对**被驱动表**添加索引

采用内连接时 MySQL 会自动选择驱动表

使用小表作为驱动表

### 子查询优化

子 查询的执行效率不高。原因：

 ① 执行子查询时，MySQL需要为内层查询语句的查询结果 建立一个临时表 ，然后外层查询语句从临时表 中查询记录。查询完毕后，再 撤销这些临时表 。这样会消耗过多的CPU和IO资源，产生大量的慢查询。 

② 子查询的结果集存储的临时表，不论是内存临时表还是磁盘临时表都 不会存在索引 ，所以查询性能会 受到一定的影响。 

③ 对于返回结果集比较大的子查询，其对查询性能的影响也就越大。

 在MySQL中，可以使用连接（JOIN）查询来替代子查询。连接查询 不需要建立临时表 ，其 速度比子查询 要快 ，如果查询中使用索引的话，性能就会更好。 

**结论：尽量不要使用NOT IN 或者 NOT EXISTS，用LEFT JOIN xxx ON xx WHERE xx IS NULL替代**



### 排序优化

**单路排序和双路排序**

1. 双路排序（又叫回表排序模式）：先根据相应的条件取出相应的排序字段和可以直接定位行 数据的行 ID，然后在 sort buffer 中进行排序，排序完后需要再次取回其它需要的字段；
2. 单路排序：是一次性取出满足条件行的所有字段，然后在sort buffer中进行排序

**优化建议**

1. SQL 中，可以在 WHERE 子句和 ORDER BY 子句中使用索引，目的是在 WHERE 子句中 避免全表扫 描 ，在 ORDER BY 子句 避免使用 FileSort 排序 。当然，某些情况下全表扫描，或者 FileSort 排 序不一定比索引慢。但总的来说，我们还是要避免，以提高查询效率。 
2.  尽量使用 Index 完成 ORDER BY 排序。如果 WHERE 和 ORDER BY 后面是相同的列就使用单索引列； 如果不同就使用联合索引。 
3.  无法使用 Index 时，需要对 FileSort 方式进行调优。

**优化策略**

1. 尝试提高 sort_buffer_size 
2. 尝试提高 max_length_for_sort_data
3. Order by 时select * 是一个大忌。最好只Query需要的字段。

### 索引覆盖

### 前缀索引

### 索引下推

- **索引下推**（index condition pushdown ）简称ICP，在**Mysql5.6**的版本上推出，用于优化查询。
- 在不使用ICP的情况下，在使用**非主键索引（又叫普通索引或者二级索引）**进行查询时，存储引擎通过索引检索到数据，然后返回给MySQL服务器，服务器然后判断数据是否符合条件 。
- 在使用ICP的情况下，如果存在某些被索引的列的判断条件时，MySQL服务器将这一部分判断条件传递给存储引擎，然后由存储引擎通过判断索引是否符合MySQL服务器传递的条件，只有当索引符合条件时才会将数据检索出来返回给MySQL服务器 。
- **索引条件下推优化可以减少存储引擎查询基础表的次数，也可以减少MySQL服务器从存储引擎接收数据的次数**。



## 6、事务基础

只用 InnoDB 支持事务

事务的ACID

脏写、脏读、不可重复读、幻读

四种隔离级别

、、

## 7、MySQL 事务日志



事务有4种特性：原子性、一致性、隔离性和持久性。那么事务的四种特性到底是基于什么机制实现呢？ 

- 事务的隔离性由 锁机制 实现。 
- 而事务的原子性、一致性和持久性由事务的 redo 日志和undo 日志来保证。
  -  REDO LOG 称为 重做日志 ，提供再写入操作，恢复提交事务修改的页操作，用来保证事务的持久性。
  -  UNDO LOG 称为 回滚日志 ，回滚行记录到某个特定版本，用来保证事务的原子性、一致性。
-  有的DBA或许会认为 UNDO 是 REDO 的逆过程，其实不然。



### redo log

一方面，缓冲池可以帮助我们消除CPU和磁盘之间的鸿沟，checkpoint机制可以保证数据的最终落盘，然 而由于checkpoint 并不是每次变更的时候就触发 的，而是master线程隔一段时间去处理的。所以最坏的情 况就是事务提交后，刚写完缓冲池，数据库宕机了，那么这段数据就是丢失的，无法恢复。



另一方面，事务包含 持久性 的特性，就是说对于一个已经提交的事务，在事务提交后即使系统发生了崩 溃，这个事务对数据库中所做的更改也不能丢失。



那么如何保证这个持久性呢？ 一个简单的做法 ：在事务提交完成之前把该事务所修改的所有页面都刷新 到磁盘，但是这个简单粗暴的做法有些问题 

另一个解决的思路 ：我们只是想让已经提交了的事务对数据库中数据所做的修改永久生效，即使后来系 统崩溃，在重启后也能把这种修改恢复出来。所以我们其实没有必要在每次事务提交时就把该事务在内 存中修改过的全部页面刷新到磁盘，只需要把 修改 了哪些东西 记录一下 就好。比如，某个事务将系统 表空间中 第10号 页面中偏移量为 100 处的那个字节的值 1 改成 2 。我们只需要记录一下：将第0号表 空间的10号页面的偏移量为100处的值更新为 2 

**redo log 的好处和特点**

1. 好处
   1. redo 日志降低了刷盘频率
   2. redo 日志占用更多空间非常小
2. 特点
   1. redo 日志是顺序写入磁盘的
   2. 事务执行过程中，redo log 不断记录

**整体流程**

![image-20220427201332068](typora-user-images\image-20220427201332068.png)

![image-20220427201434906](typora-user-images\image-20220427201434906.png)



**刷盘策略**

![image-20220427201531713](typora-user-images\image-20220427201531713.png)

注意，redo log buffer刷盘到redo log file的过程并不是真正的刷到磁盘中去，只是刷入到 文件系统缓存 （page cache）中去（这是现代操作系统为了提高文件写入效率做的一个优化），真正的写入会交给系 统自己来决定（比如page cache足够大了）。那么对于InnoDB来说就存在一个问题，如果交给系统来同 步，同样如果系统宕机，那么数据也丢失了（虽然整个系统宕机的概率还是比较小的）。



针对这种情况，InnoDB给出 innodb_flush_log_at_trx_commit 参数，该参数控制 commit提交事务 时，如何将 redo log buffer 中的日志刷新到 redo log file 中。它支持三种策略：

- 设置为0 ：表示每次事务提交时不进行刷盘操作。（系统默认master thread每隔1s进行一次重做日志的同步）
- 设置为1 ：表示每次事务提交时都将进行同步，刷盘操作（ 默认值 ）
- 设置为2 ：表示每次事务提交时都只把 redo log buffer 内容写入 page cache，不进行同步。由os自 己决定什么时候同步到磁盘文件。



**流程图**

![image-20220427201713212](typora-user-images\image-20220427201713212.png)

![image-20220427201731363](typora-user-images\image-20220427201731363.png)

![image-20220427201737148](typora-user-images\image-20220427201737148.png)



mtr 相关概念

### undo log

redo log是事务持久性的保证，undo log是事务原子性的保证。在事务中 更新数据 的 前置操作 其实是要 先写入一个 undo log 。

![image-20220427203942447](typora-user-images\image-20220427203942447.png)

### undo log 的刷盘策略

[关于Innodb undo log的刷新时机？ - 知乎 (zhihu.com)](https://www.zhihu.com/question/267595935)

[庖丁解InnoDB之Undo LOG - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/427911093)



## 8、锁

![image-20220427204432388](typora-user-images\image-20220427204432388.png)

### 表锁

读锁/共享锁

写锁/排它锁

表锁（S锁和X锁）：DDL 专用

元数据锁：当对表数据进行 crud 时，添加这个锁

意向锁：它允许 行级锁 与 表级锁 共存，而意向 锁就是其中的一种 表锁 。

![image-20220427205214045](typora-user-images\image-20220427205214045.png)

意向锁的意义：

[mysql数据库意向锁意义 - 简书 (jianshu.com)](https://www.jianshu.com/p/e937830bc2de)



自增锁：用于维护高并发情况下 auto-increment 的并发安全

### 行锁

**记录锁：**记录锁也就是仅仅把一条记录锁上，官方的类型名称为： LOCK_REC_NOT_GAP 。

**间隙锁：**MySQL 在 REPEATABLE READ 隔离级别下是可以解决幻读问题的，解决方案有两种，可以使用 MVCC 方 案解决，也可以采用 加锁 方案解决。但是在使用加锁方案解决时有个大问题，就是事务在第一次执行读 取操作时，那些幻影记录尚不存在，我们无法给这些 幻影记录 加上 记录锁 。InnoDB提出了一种称之为 Gap Locks 的锁，官方的类型名称为： LOCK_GAP ，我们可以简称为 gap锁 。

**临键锁**：有时候我们既想 锁住某条记录 ，又想 阻止 其他事务在该记录前边的 间隙插入新记录 ，所以InnoDB就提 出了一种称之为 Next-Key Locks 的锁，官方的类型名称为： LOCK_ORDINARY ，我们也可以简称为 next-key锁 。Next-Key Locks是在存储引擎 innodb 、事务级别在 可重复读 的情况下使用的数据库锁， innodb默认的锁就是Next-Key locks

**插入意向锁：**我们说一个事务在 插入 一条记录时需要判断一下插入位置是不是被别的事务加了 gap锁 （ next-key锁 也包含 gap锁 ），如果有的话，插入操作需要等待，直到拥有 gap锁 的那个事务提交。但是InnoDB规 定事务在等待的时候也需要在内存中生成一个锁结构，表明有事务想在某个 间隙 中 插入 新记录，但是 现在在等待。InnoDB就把这种类型的锁命名为 Insert Intention Locks ，官方的类型名称为： LOCK_INSERT_INTENTION ，我们称为 插入意向锁 。插入意向锁是一种 Gap锁 ，不是意向锁，在insert 操作时产生。



### 页锁

页锁就是在 页的粒度 上进行锁定，锁定的数据资源比行锁要多，因为一个页中可以有多个行记录。当我 们使用页锁的时候，会出现数据浪费的现象，但这样的浪费最多也就是一个页上的数据行。页锁的开销 介于表锁和行锁之间，会出现死锁。锁定粒度介于表锁和行锁之间，并发度一般。 每个层级的锁数量是有限制的，因为锁会占用内存空间， 锁空间的大小是有限的 。当某个层级的锁数量 超过了这个层级的阈值时，就会进行 锁升级 。锁升级就是用更大粒度的锁替代多个更小粒度的锁，比如 InnoDB 中行锁升级为表锁，这样做的好处是占用的锁空间降低了，但同时数据的并发度也下降了。



### 锁结构

![image-20220427210423609](typora-user-images\image-20220427210423609.png)



## 9、多版本并发控制



MVCC （Multiversion Concurrency Control），多版本并发控制。顾名思义，MVCC 是通过数据行的多个版 本管理来实现数据库的 并发控制 。这项技术使得在InnoDB的事务隔离级别下执行 一致性读 操作有了保 证。换言之，就是为了查询一些正在被另一个事务更新的行，并且可以看到它们被更新之前的值，这样 在做查询的时候就不用等待另一个事务释放锁。



### 快照读

快照读又叫一致性读，读取的是快照数据。不加锁的简单的 SELECT 都属于快照读，即不加锁的非阻塞读

之所以出现快照读的情况，是基于提高并发性能的考虑，快照读的实现是基于MVCC，它在很多情况下， 避免了加锁操作，降低了开销。

既然是基于多版本，那么快照读可能读到的并不一定是数据的最新版本，而有可能是之前的历史版本。

快照读的前提是隔离级别不是串行级别，串行级别下的快照读会退化成当前读。

### 当前读

当前读读取的是记录的最新版本（最新数据，而不是历史版本的数据），读取时还要保证其他并发事务 不能修改当前记录，会对读取的记录进行加锁。加锁的 SELECT，或者对数据进行增删改都会进行当前 读。



### Undo Log 版本链

![image-20220427211139694](typora-user-images\image-20220427211139694.png)

insert undo只在事务回滚时起作用，当事务提交后，该类型的undo日志就没用了，它占用的Undo Log Segment也会被系统回收（也就是该undo日志占用的Undo页面链表要么被重用，要么被释 放）



每次对记录进行改动，都会记录一条undo日志，每条undo日志也都有一个 roll_pointer 属性 （ INSERT 操作对应的undo日志没有该属性，因为该记录并没有更早的版本），可以将这些 undo日志 都连起来，串成一个链表：

![image-20220427211239896](typora-user-images\image-20220427211239896.png)

### ReadView

使用 READ COMMITTED 和 REPEATABLE READ 隔离级别的事务，都必须保证读到 已经提交了的 事务修改 过的记录。假如另一个事务已经修改了记录但是尚未提交，是不能直接读取最新版本的记录的，核心问 题就是需要判断一下版本链中的哪个版本是当前事务可见的，这是ReadView要解决的主要问题。



**设计思路**

1. creator_trx_id 创建这个 ReadView 的事务id
2. trx_ids ，表示在生成ReadView时当前系统中活跃的读写事务的 事务id列表 。
3. up_limit_id ，活跃的事务中最小的事务 ID。
4. low_limit_id ，表示生成ReadView时系统中应该分配给下一个事务的 id 值。low_limit_id 是系 统最大的事务id值，这里要注意是系统中的事务id，需要区别于正在活跃的事务ID。

**访问规则**

- 如果被访问版本的trx_id属性值与ReadView中的 creator_trx_id 值相同，意味着当前事务在访问 它自己修改过的记录，所以该版本可以被当前事务访问。 
- 如果被访问版本的trx_id属性值小于ReadView中的 up_limit_id 值，表明生成该版本的事务在当前 事务生成ReadView前已经提交，所以该版本可以被当前事务访问。
- 如果被访问版本的trx_id属性值大于或等于ReadView中的 low_limit_id 值，表明生成该版本的事 务在当前事务生成ReadView后才开启，所以该版本不可以被当前事务访问。 
- 如果被访问版本的trx_id属性值在ReadView的 up_limit_id 和 low_limit_id 之间，那就需要判 断一下trx_id属性值是不是在 trx_ids 列表中。

### MVCC整体操作流程

1. 首先获取事务自己的版本号，也就是事务 ID； 
2. 获取 ReadView； 
3. 查询得到的数据，然后与 ReadView 中的事务版本号进行比较； 
4. 如果不符合 ReadView 规则，就需要从 Undo Log 中获取历史快照； 
5. 最后返回符合规则的数据。



**读已提交和不可重复读**

![image-20220427212253517](typora-user-images\image-20220427212253517.png)

## 10、其他日志

- **慢查询日志：**记录所有执行时间超过long_query_time的所有查询，方便我们对查询进行优化。 

- **通用查询日志：**记录所有连接的起始时间和终止时间，以及连接发送给数据库服务器的所有指令， 对我们复原操作的实际场景、发现问题，甚至是对数据库操作的审计都有很大的帮助。 

- **错误日志：**记录MySQL服务的启动、运行或停止MySQL服务时出现的问题，方便我们了解服务器的 状态，从而对服务器进行维护。 

- **二进制日志：**记录所有更改数据的语句，可以用于主从服务器之间的数据同步，以及服务器遇到故 障时数据的无损失恢复。 

- **中继日志：**用于主从服务器架构中，从服务器用来存放主服务器二进制日志内容的一个中间文件。 从服务器通过读取中继日志的内容，来同步主服务器上的操作。 

- **数据定义语句日志：**记录数据定义语句执行的元数据操作。
