# EalsticSearch 

## 1. 介绍

Elasticsearch（简称ES）是一个基于Lucene构建的开源、分布式、RESTful搜索引擎。它提供了一个分布式多租户能力的全文搜索引擎，具有HTTP网络接口和无模式的JSON文档。Elasticsearch的核心特性和功能如下：

1. **分布式特性**：
   - Elasticsearch是分布式的，可以在多个服务器上运行，并能够自动将数据在服务器之间进行负载均衡。
   - 它支持自动分片和复制功能，能够自动将数据分散到不同的节点上，并提供数据冗余和容错能力，确保系统的高可用性和稳定性。

2. **可扩展性**：
   - 无论是存储、节点还是查询吞吐量，Elasticsearch都提供了可扩展的架构，可以随着业务需求的变化而增加资源。
   - 通过增加节点，Elasticsearch可以水平扩展其处理能力。

3. **实时性**：
   - Elasticsearch具有近实时的搜索能力，能够快速地索引和更新数据，并实时返回搜索结果。

4. **全文检索**：
   - 提供了强大的全文搜索引擎，能够处理各种复杂的查询，如短语搜索、模糊搜索、范围搜索等。
   - 支持对大量数据进行复杂的搜索和分析。

5. **分析性**：
   - 提供了强大的分析功能，包括聚合、统计和排序等，帮助用户从海量数据中提取有价值的信息。

6. **多租户能力**：
   - 可以配置为多租户环境，允许不同的用户和应用共享相同的集群资源。

7. **监控和警报**：
   - 提供了内置的监控和警报功能，使得用户可以实时了解系统的运行状态，并在出现异常时得到通知。

8. **灵活的数据类型**：
   - 支持多种数据类型，包括字符串、数字、日期等。

9. **日志与监控**：
   - 常被用作日志管理系统，能够高效地存储、搜索和分析日志数据。
   - 通过集成Kibana等可视化工具，可以实时展示日志数据、监控系统状态，并提供警报和通知功能。

10. **安全性**：
    - 提供了强大的安全功能，如身份验证、授权和加密等，保护数据的安全性和隐私性。

Elasticsearch通常与Logstash（数据收集和日志解析引擎）和Kibana（分析和可视化平台）一起使用，它们共同构成了Elastic Stack（以前称为ELK Stack），为企业提供了强大的日志分析、监控和搜索解决方案。

Elasticsearch由于其出色的性能和广泛的应用场景，已成为构建现代化搜索和分析解决方案的重要工具之一。

## 2. ES中的核心概念

在Elasticsearch中，`Index`、`Type`（在某些版本中已被废弃）和`Document`是核心概念，它们共同构成了Elasticsearch的数据模型。以下是这些概念的详细解释：

### 1. Index（索引）

* **定义**：Index是Elasticsearch中的一个核心概念，它类似于关系型数据库中的“数据库”或“表”的概念。它是一个包含文档的逻辑数据集合。
* **特性**：
  * **不可变性**：Index一旦创建就不能更改。当需要修改文档时，会创建一个新的版本，并将旧版本标记为已删除。
  * **搜索、聚合、过滤和排序**：Index中的文档可以被搜索、聚合、过滤和排序。
  * **创建**：创建Index需要使用PUT API。例如，`PUT /my_index` 可以创建一个名为`my_index`的Index。
  * **验证**：可以通过GET API来验证Index是否创建成功。例如，`GET /my_index` 如果Index已经存在，则会返回Index的元数据，否则会返回404状态码。

### 2. Type（类型）（在Elasticsearch 7.x及以后版本中被废弃）

* **定义**：在Elasticsearch的早期版本中，Type是Index下的一个子概念，类似于关系型数据库中的“表”。但在Elasticsearch 7.x及以后版本中，Type的概念被弱化并最终被废弃，每个Index中只有一个默认的Type，即`_doc`。
* **注意**：在Elasticsearch 7.x及以后版本中，不再需要显式指定Type，而是将Type的概念与Index合并，即Index既可以被认为对应MySQL的Database，也可以认为对应Table。

### 3. Document（文档）

* **定义**：Document是Elasticsearch中的数据的最小存储单元，相当于关系型数据库中的“行”。每个文档都有一个唯一的`_id`，且内部由一系列键值对（field:value）组成，采用JSON格式表示。
* **特性**：
  * **独立性**：文档是独立的实体，无需与其他文档有明确的关系。
  * **结构灵活性**：文档可以包含任意数量的字段，字段可以是简单类型（如字符串、数值、日期等）或复杂类型（如数组、嵌套对象等），且无需预定义严格的schema。
  * **动态映射**：Elasticsearch会根据文档首次索引时的实际内容自动推断字段类型和属性，实现动态映射。后期也可以通过显式定义映射（mapping）来约束字段类型和设置索引选项。
* **操作**：
  * **创建**：通过PUT或POST请求向Index添加文档。
  * **更新**：通过UPDATE或PUT请求更新文档的部分或全部内容。

### Field（字段）：

字段是文档中的一个属性，可以包含文本、数字、日期等数据类型。
字段在 Elasticsearch 中被用于搜索、排序和聚合等操作。
在 Elasticsearch 的任何版本中，字段都是文档的重要组成部分。

### 总结

在Elasticsearch中，Index是包含文档的逻辑数据集合，而Document是数据的最小存储单元。Type的概念在较新版本的Elasticsearch中已被废弃，每个Index中只有一个默认的Type，即`_doc`。这些概念共同构成了Elasticsearch的数据模型，使得Elasticsearch能够高效地存储、检索和分析大量数据。


## 2. 常用 API

### 1. 创建索引

```PUT /{index_name}```

response:
```json
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "test_index"
}
```

### 2. 查询所有索引

```GET /_cat/indices?v```

response:
```
health status index      uuid                   pri rep docs.count docs.deleted store.size pri.store.size
green  open   hotel      laBiiShBRlS3Hon6lEVZqA   1   0          7            0     44.2kb         44.2kb
yellow open   test_index 3PJBWfKGR2-WLdv5HQ9FMg   1   1          0            0       208b           208b
yellow open   shopping   Rs6G9j6cTfiX2dGqFuU5Tw   1   1          0            0       208b           208b
```


### 3. 查询单个索引

```GET /{index_name}```

response:
```json
{
    "index_2": {
        "aliases": {},
        "mappings": {},
        "settings": {
            "index": {
                "creation_date": "1719212709943",
                "number_of_shards": "1",
                "number_of_replicas": "1",
                "uuid": "ecP_6HJJSDyB9JVVL492_g",
                "version": {
                    "created": "7070099"
                },
                "provided_name": "index_2"
            }
        }
    }
}
```

### 4. 删除单个索引

```DELETE /{index_nam}```

response:
```json
{
    "acknowledged": true
}
```

### 5. 文档创建

```POST /{index_name}/_doc```

request body:

```json
{
    "title":"小米手机",
    "category":"小米",
    "images":"http://www.gulixueyuan.com/xm.jpg",
    "price":3999.00
}
```

response:

```json
{
    "_index": "shopping",//索引
    "_type": "_doc",//类型-文档
    "_id": "6M8XSZABl--fUNBdfbnG",//唯一标识，可以类比为 MySQL 中的主键，随机生成
    "_version": 1,//版本
    "result": "created",//结果，这里的 create 表示创建成功
    "_shards": {//
        "total": 2,//分片 - 总数
        "successful": 1,//分片 - 总数
        "failed": 0//分片 - 总数
    },
    "_seq_no": 0,
    "_primary_term": 1
}

```

上面的数据创建后，由于没有指定数据唯一性标识（ID），默认情况下， ES 服务器会随机生成一个。

如果想要自定义唯一性标识，需要在创建时指定： http://127.0.0.1:9200/shopping/_doc/1，请求体JSON内容为：

```json
{
    "title":"小米手机",
    "category":"小米",
    "images":"http://www.gulixueyuan.com/xm.jpg",
    "price":3999.00
}
```

response:
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 1,
    "_primary_term": 1
}
```
此处需要注意：如果增加数据时明确数据主键，那么请求方式也可以为 PUT。

### 6. 查询某个索引下的所有doc

```GET /{index_nam}/_search```

response:
```json
{
    "took": 2,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 4,
            "relation": "eq"
        },
        "max_score": 1.0,
        "hits": [
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "6M8XSZABl--fUNBdfbnG",
                "_score": 1.0,
                "_source": {
                    "title": "小米手机",
                    "category": "小米",
                    "images": "http://www.gulixueyuan.com/xm.jpg",
                    "price": 3999.00
                }
            },
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "1",
                "_score": 1.0,
                "_source": {
                    "title": "小米手机",
                    "category": "小米",
                    "images": "http://www.gulixueyuan.com/xm.jpg",
                    "price": 3999.00
                }
            },
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "2",
                "_score": 1.0,
                "_source": {
                    "title": "小米手机",
                    "category": "小米",
                    "images": "http://www.gulixueyuan.com/xm.jpg",
                    "price": 3999.00
                }
            },
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "6c8bSZABl--fUNBd1Lm6",
                "_score": 1.0,
                "_source": {
                    "title": "小米手机",
                    "category": "小米",
                    "images": "http://www.gulixueyuan.com/xm.jpg",
                    "price": 3999.00
                }
            }
        ]
    }
}
```

### 7. 全量修改

```POST /{index_name}/_doc/{doc_id}```

requset：
```json
{
    "title":"华为手机",
    "category":"华为",
    "images":"http://www.gulixueyuan.com/hw.jpg",
    "price":1999.00
}
```

response:
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1",
    "_version": 4,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 6,
    "_primary_term": 1
}
```

### 8. 局部修改

```POST /{index_name}/_update/{doc_id}```

request:
```json
{
	"doc": {
		"title":"小米手机",
		"category":"小米"
	}
}
```

response:
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1",
    "_version": 5,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 7,
    "_primary_term": 1
}
```

### 9. 根据id查询文档

```GET /{index_name}/_doc/{doc_id}```

response:
```json
{
    "_index": "shopping",
    "_type": "_doc",
    "_id": "1",
    "_version": 5,
    "_seq_no": 7,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "title": "小米手机",
        "category": "小米",
        "images": "http://www.gulixueyuan.com/hw.jpg",
        "price": 1999.0
    }
}
```
### 10. 根据ID删除文档

```DELETE /{index_name}/_doc/{doc_id}```


### 11. 条件查询 & 分页查询 & 查询排序

1. URL带参查询 ```/{index_name}/_search?q={field_name}:{field_value}```

2. 请求体带参查询 ```POST /{index_name}/_search ```

request:
```json
{
    "query":{
        "match":{
            "category":"小米"  // category == '小米'
        }
    },
    "_source":["title"],  // select
    "from":0,           // limit
	 "size":2,
    "sort":{
		"price":{
			"order":"desc" // order by
		}
	}
}
```

response:
```json
{
    "took": 2,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 3,
            "relation": "eq"
        },
        "max_score": null,
        "hits": [
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "1",
                "_score": null,
                "_source": {
                    "title": "小米手机"
                },
                "sort": [
                    5000.0
                ]
            },
            {
                "_index": "shopping",
                "_type": "_doc",
                "_id": "6M8XSZABl--fUNBdfbnG",
                "_score": null,
                "_source": {
                    "title": "小米手机"
                },
                "sort": [
                    3999.0
                ]
            }
        ]
    }
}

```


### 12. 多条件查询 & 范围查询

```json
{
	"query": {
		"bool": {
			"should": [
				{
					"match": {
						"category": "小米"
					}
				},
				{
					"match": {
						"category": "华为"
					}
				}
			],
			"filter": {
				"range": {
					"price": {
						"gt": 4000
					}
				}
			}
		}
	}
}
```

## 3. query Json 常用参数总结

在 Elasticsearch 中，`query` 参数是搜索请求的核心部分，它定义了如何找到匹配的文档。`query` 参数下面可以包含多种不同的 JSON 参数，这些参数定义了不同的查询类型。以下是一些常用的查询类型和它们对应的 JSON 参数：

### 1. Match 查询


	* `match`: 基于文本字段的全文搜索，包括模糊匹配。
	
	
	```json
	{
	  "query": {
	    "match": {
	      "field_name": "search_term"
	    }
	  }
	}
	```
### 2. Match Phrase 查询


	* `match_phrase`: 搜索文本字段中的短语或词组。
	
	
	```json
	{
	  "query": {
	    "match_phrase": {
	      "field_name": "search phrase"
	    }
	  }
	}
	```
### 3. Term 查询


	* `term`: 精确值搜索，不分析查询字符串。
	
	
	```json
	{
	  "query": {
	    "term": {
	      "field_name": "exact_value"
	    }
	  }
	}
	```
### 4. Terms 查询


	* `terms`: 搜索多个精确值。
	
	
	```json
	{
	  "query": {
	    "terms": {
	      "field_name": ["value1", "value2"]
	    }
	  }
	}
	```
### 5. Range 查询


	* `range`: 搜索在指定范围内的值。
	
	
	```json
	{
	  "query": {
	    "range": {
	      "field_name": {
	        "gte": "lower_value",
	        "lte": "upper_value"
	      }
	    }
	  }
	}
	```
### 6. Bool 查询


	* `bool`: 组合多个查询子句，包括 `must`、`should`、`must_not` 和 `filter`。
	
	
	```json
	{
	  "query": {
	    "bool": {
	      "must": [
	        { "match": { "field1": "value1" } },
	        { "match": { "field2": "value2" } }
	      ],
	      "should": [
	        { "match": { "field3": "value3" } }
	      ],
	      "must_not": [
	        { "match": { "field4": "value4" } }
	      ]
	    }
	  }
	}
	```
### 7. Wildcard 查询


	* `wildcard`: 支持通配符的查询。
	
	
	```json
	{
	  "query": {
	    "wildcard": {
	      "field_name": "value*"
	    }
	  }
	}
	```
### 8. Prefix 查询


	* `prefix`: 搜索以指定前缀开头的值。
	
	
	```json
	{
	  "query": {
	    "prefix": {
	      "field_name": "pre"
	    }
	  }
	}
	```
### 9. Fuzzy 查询


	* `fuzzy`: 模糊搜索，支持拼写错误。
	
	
	```json
	{
	  "query": {
	    "fuzzy": {
	      "field_name": "search_term~"
	    }
	  }
	}
	```
### 10. Geo Distance 查询
* `geo_distance`: 搜索指定位置附近的文档。


```json
{
  "query": {
    "geo_distance": {
      "distance": "200km",
      "location": {
        "lat": 40,
        "lon": -70
      }
    }
  }
}
```
当然，我可以继续列举更多Elasticsearch查询类型及其JSON参数来完成这个列表。请注意，由于Elasticsearch的功能非常丰富，这里只列出了一些常见和重要的查询类型。

### 11. Function Score 查询

Function Score查询允许你在查询时修改文档的得分。你可以基于字段值、距离、脚本等来计算新的得分。

```json
{
  "query": {
    "function_score": {
      "query": { "match_all": {} },
      "functions": [
        {
          "field_value_factor": {
            "field": "field_name",
            "modifier": "log1p",
            "factor": 1.2,
            "missing": 1
          }
        },
        {
          "gauss": {
            "location": {
              "origin": "lat,lon",
              "scale": "2km",
              "offset": "0km",
              "decay": 0.3
            }
          }
        }
      ],
      "score_mode": "sum",
      "boost_mode": "multiply",
      "max_boost": 3.0,
      "min_score": 0.1
    }
  }
}
```

### 12. Has Child 查询

Has Child查询用于在父子关系文档中查找与给定子文档查询匹配的父文档。

```json
{
  "query": {
    "has_child": {
      "type": "child_type",
      "query": { "match": { "field_name": "search_term" } },
      "score_mode": "none",
      "inner_hits": {},
      "min_children": 1,
      "max_children": 2,
      "boost": 1.0
    }
  }
}
```

### 13. Has Parent 查询

Has Parent查询用于在父子关系文档中查找与给定父文档查询匹配的子文档。

```json
{
  "query": {
    "has_parent": {
      "parent_type": "parent_type",
      "query": { "match": { "field_name": "search_term" } },
      "score": true,
      "inner_hits": {}
    }
  }
}
```

### 14. More Like This 查询

More Like This查询用于找到与给定文档相似的其他文档。

```json
{
  "query": {
    "more_like_this": {
      "fields": ["field1", "field2"],
      "like": ["text like this one", "text like that one"],
      "min_term_freq": 1,
      "max_query_terms": 25,
      "stop_words": [],
      "min_doc_freq": 5,
      "max_doc_freq": 100,
      "min_word_length": 2,
      "boost_terms": 1,
      "boost": 1.0
    }
  }
}
```

### 15. Script 查询

Script查询允许你使用自定义脚本来定义查询逻辑。

```json
{
  "query": {
    "script": {
      "script": {
        "source": "doc['field_name'].value > params.threshold",
        "lang": "painless",
        "params": {
          "threshold": 100
        }
      }
    }
  }
}
```

### 16. Nested 查询

Nested查询用于查询嵌套在对象字段中的文档。

```json
{
  "query": {
    "nested": {
      "path": "nested_field",
      "query": {
        "match": {
          "nested_field.field_name": "search_term"
        }
      },
      "inner_hits": {},
      "ignore_unmapped": false,
      "score_mode": "avg",
      "boost": 1.0
    }
  }
}
```

### 17. Geo Shape 查询

Geo Shape查询用于搜索与给定形状相交的地理形状字段。

```json
{
  "query": {
    "geo_shape": {
      "location": {
        "shape": {
          "type": "circle",
          "coordinates": [lon, lat],
          "radius": "10km"
        },
         "relation": "intersects"
      }
     }
  }
}

### 18. Parent-Id 查询

Parent-Id查询允许你根据父文档的ID来查询子文档。

```json
{
  "query": {
    "parent_id": {
      "type": "child_type",
      "id": "parent_document_id"
    }
  }
}
```

### 19. Percolate 查询

Percolate查询用于在已存储的查询中匹配文档。这通常用于文档路由和警报。

```json
{
  "query": {
    "percolate": {
      "field": "query_field",
      "document": {
        "field1": "value1",
        "field2": "value2"
      },
      "size": 10,
      "version": true,
      "track_scores": true,
      "query_name": "my_query"
    }
  }
}
```

### 20. Shape 查询

Shape查询用于在形状字段中搜索形状。

```json
{
  "query": {
    "shape": {
      "field_name": {
        "shape": {
          "type": "rectangle",
          "coordinates": [
            [top_left_lon, top_left_lat],
            [bottom_right_lon, bottom_right_lat]
          ]
        },
        "relation": "intersects"
      }
    }
  }
}
```
## 4. 聚合查询 aggs

```json
{
  "size": 0,
  "query": {
    "match": {
      "field_name": "search_term"
    }
  },
  "aggs": {
    "group_by_field": {   // key 自定义聚合名称
      "terms": {
        "field": "group_field.keyword",  // 注意这里可能需要.keyword后缀来针对keyword类型字段
        "size": 10,                      // 返回最热门的10个分组
        "order": {
          "_count": "desc"               // 按文档数降序排列
        }
      },
      "aggs": {
        "average_value": {
          "avg": {
            "field": "value_field"
          }
        }
      }
    }
  }
}
```

- `size: 0` 表示我们不关心搜索结果中具体有哪些文档，因为我们只对聚合结果感兴趣。
- `query` 部分使用了一个`match`查询来过滤文档，只包含`field_name`字段值为`search_term`的文档。
- `aggs` 部分定义了一个名为`group_by_field`的聚合，它使用`terms`聚合来按`group_field`字段的值分组文档。`terms`聚合中的`size`参数限制了返回的分组数量，而`order`参数指定了如何对这些分组进行排序。
- 在`group_by_field`聚合内部，又定义了一个名为`average_value`的子聚合，它使用`avg`聚合来计算每个分组中`value_field`字段的平均值。

字段名（`field_name`、`group_field`和`value_field`）和值（`search_term`）应该根据引映射和实际数据替换。另外，字段是text类型并希望按确切值进行分组，你可能需要使用`.keyword`后缀来引用该字段的keyword子字段（如示例中的`group_field.keyword`）。

## 5. 映射创建

主要作用

1. **定义字段的类型**：
   - Elasticsearch中的每个字段都必须有一个类型。映射可以用于指定字段的类型，如文本类型（text和keyword）、数值类型（long、integer、short、byte、double、float和half_float）、日期类型（date）、布尔类型（boolean）、二进制类型（binary）等。
   - 通过定义字段类型，Elasticsearch能够更准确地理解和处理数据，提高搜索和分析的效率。

2. **指定字段的分析器**：
   - Elasticsearch使用分析器对文本进行分词和处理。映射可以指定哪些字段需要使用哪种分析器，以满足不同的搜索和分析需求。

3. **控制字段的索引**：
   - Elasticsearch默认对所有字段进行索引，但有些字段可能不需要被索引，例如某些只用于存储数据的字段。映射可以控制哪些字段需要被索引，哪些字段不需要被索引，从而优化索引的存储和查询性能。

4. **定义字段的属性**：
   - 映射可以定义一些额外的属性，例如字段是否需要存储原始值、是否需要支持聚合操作等。这些属性可以根据实际需求进行配置，以满足不同的业务场景。


### 1. 创建一个user映射

1. 先创建 user index
```PUT /user```

2. 创建映射
```PUT /user/_maping```

request:
```json
{
    "properties": {
        "name":{
        	"type": "text",
        	"index": true
        },
        "sex":{
        	"type": "keyword",
        	"index": true
        },
        "tel":{
        	"type": "keyword",
        	"index": false
        }
    }
}
```

### 2. 映射字段常用类型

在Elasticsearch中，映射字段的常用类型可以归纳为以下几个主要类别：

1. **文本类型**：
   - `text`：适用于全文搜索的文本数据。字段会被分词并建立倒排索引，支持模糊搜索、短语搜索、同义词搜索等复杂查询。
   - `keyword`：用于存储不可分词的、完整的、原始的值，如标签、ID、电子邮件地址等。字段用于精确匹配、排序、聚合等操作，不进行分词处理。

2. **数值类型**：
   - `long`/`integer`/`short`/`byte`：整数类型，分别对应不同大小的整数值范围。
   - `double`/`float`/`half_float`/`scaled_float`：浮点数类型，分别对应不同精度和缩放因子的浮点数值。其中，`scaled_float`是一种特殊的浮点类型，它允许存储一个浮点数并指定一个缩放因子以减少存储的精度损失。

3. **日期类型**：
   - `date`：存储日期和时间数据，支持多种格式和时间区间查询。可以使用`format`参数来指定日期时间的格式。

4. **地理类型**：
   - `geo_point`：存储经纬度坐标，支持地理距离查询、边界框查询、多边形查询等。
   - `geo_shape`：用于存储复杂的地理形状，如多边形、线等。

5. **特殊类型**：
   - `boolean`：存储布尔值（true/false）。
   - `binary`：以Base64字符串编码的二进制值。
   - `ip`：存储IPv4或IPv6地址。

6. **复杂类型**：
   - `object`：用于嵌套结构，内部可以包含其他字段类型。
   - `nested`：嵌套对象类型，允许数组中的对象独立索引，以支持嵌套文档的内部查询。

7. **其他类型**：
   - `join`：在同一索引的文档中创建父/子关系的特殊字段。
   - `alias`：别名类型，允许你为某个字段定义多个名称。
   - `rank_features`：用于机器学习的排名特征字段类型。

8. **数组类型**：
   - 数组类型可以应用于任何字段类型，允许一个字段包含多个值。

需要注意的是，Elasticsearch的映射定义了索引中文档的结构和字段，并且一旦索引被创建，其映射中的字段类型通常不能更改（尽管在某些情况下可以通过特定的API进行更新，但这并不总是推荐的）。因此，在创建索引和映射时，应该仔细考虑并规划好字段类型和属性，以满足未来的业务需求。

## Java API

### 1. POM 文件

```xml
        <dependency>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>elasticsearch-rest-high-level-client</artifactId>
            <version>${elasticsearch.version}</version>
        </dependency>
        <dependency>
            <groupId>org.elasticsearch</groupId>
            <artifactId>elasticsearch</artifactId>
            <version>7.7.0</version>
        </dependency>
```

### 2. ElasticSearchConfig 类

```java
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lichangxin
 * @create : 2024/6/6 9:58
 * @description
 */

@Configuration
public class EsConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("hostname", 9200,    "http")));
    }

}

```

