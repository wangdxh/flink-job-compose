# 设计
* 根据directshow中 filter 和graphedit的概念，通过重新封装和实现flink的算子，可以查询算子的类型source，transform，sink，得到算子支持的输入输出类型，通过使用有向无环图的数据编排，来实现flink job的动态生成。   

* 数据接口之间可以使用动态类型，也可以支持静态类型，增加了groovy的transform算子，可以通过动态脚本迁入不同的业务。达到flink job级别的复用。   

* 同时提供了flink sql 提交，同时支持sql protobuf格式。

![graphedit](https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhiphotos.baidu.com%2Fbinaryghost%2Fpic%2Fitem%2F05a7d440fbf2b211072085f4ca8065380dd78ea4.jpg&refer=http%3A%2F%2Fhiphotos.baidu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623380158&t=9e4582e403610eccdd050a7edaa4675b)


## 实例 
通过有向无环图，来描述flink job
``` json
{
  "graphconfig": {
    "parallel": 1,
    "jobname": "connectloop1",
    "chkpointcfg": {
      "enable": false
    }
  },
  "nodes": [
    {
      "nodeid": "1",
      "elementname": "flinksource_filelines",
      "elementconfig": {
        "filepath": "D:/wikiticker20150912sampled.json",
        "parallel": 2
      }
    },
    {
      "nodeid": "2",
      "elementname": "flinktransform_jsontomap",
      "elementconfig":{
        "parallel": 2
      }
    },
    {
      "nodeid": "3",
      "elementname": "flinksource_filelines",
      "elementconfig": {
        "filepath": "D:/wikiticker20150912sampled.json"
      }
    },
    {
      "nodeid": "4",
      "elementname": "flinktransform_jsontomap",
      "elementconfig":{
      }
    },
    {
      "nodeid": "5",
      "elementname": "flinktransform_connect",
      "elementconfig": {
        "parallel": 2
      }
    },
    {
      "nodeid": "6",
      "elementname": "flinksink_print",
      "elementconfig":{
        "parallel": 2
      }
    }
  ],
  "links": [
    {
      "sourcenode": {
        "nodeid": "1",
        "portindex": 0,
        "portselectedtype": "string"
      },
      "destnode": {
        "nodeid": "2",
        "portindex": 0,
        "portselectedtype": "string"
      }
    },
    {
      "sourcenode": {
        "nodeid": "3",
        "portindex": 0,
        "portselectedtype": "string"
      },
      "destnode": {
        "nodeid": "4",
        "portindex": 0,
        "portselectedtype": "string"
      }
    },
    {
      "sourcenode": {
        "nodeid": "2",
        "portindex": 0,
        "portselectedtype": "mapobject"
      },
      "destnode": {
        "nodeid": "5",
        "portindex": 0,
        "portselectedtype": "mapobject"
      }
    },
    {
      "sourcenode": {
        "nodeid": "4",
        "portindex": 0,
        "portselectedtype": "mapobject"
      },
      "destnode": {
        "nodeid": "5",
        "portindex": 1,
        "portselectedtype": "mapobject"
      }
    },
    {
      "sourcenode": {
        "nodeid": "5",
        "portindex": 0,
        "portselectedtype": "mapobject"
      },
      "destnode": {
        "nodeid": "6",
        "portindex": 0,
        "portselectedtype": "object"
      }
    }
  ]
}


```
## 算子列表
提供了 kafka，redis，httpasync，......,groovy等，支持json，csv，protobuf等格式

``` json
[ {
  "elementname" : "flinksource_kafka",
  "desc" : "kafka source",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "source",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "topics" : {
        "type" : "string"
      },
      "brokers" : {
        "type" : "string"
      },
      "groupid" : {
        "type" : "string"
      },
      "startfrom" : {
        "type" : "string",
        "enum" : [ "GROUPOFFSET", "EARLIEST", "LATEST" ],
        "default" : "GROUPOFFSET"
      }
    },
    "required" : [ "topics", "brokers", "groupid" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "bytearray", "string", "kafkafulldata" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_protobuftomap",
  "desc" : "protobuftomap transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "descfilepath" : {
        "type" : "string"
      },
      "descfilecontent" : {
        "type" : "string"
      },
      "topictoprotoclass" : {
        "type" : "array",
        "items" : {
          "type" : "object",
          "properties" : {
            "topicname" : {
              "type" : "string"
            },
            "protoclassname" : {
              "type" : "string"
            }
          },
          "required" : [ "topicname", "protoclassname" ]
        }
      }
    },
    "required" : [ "topictoprotoclass" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "bytearray", "kafkafulldata" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinksink_kafka",
  "desc" : "kafka",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "sink",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "topic" : {
        "type" : "string"
      },
      "brokers" : {
        "type" : "string"
      }
    },
    "required" : [ "topic", "brokers" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object", "bytearray", "string" ],
        "portname" : "input 0",
        "portdesc" : "datainput"
      } ]
    },
    "outputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    }
  }
}, {
  "elementname" : "flinksink_print",
  "desc" : "print element",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "sink",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object" ],
        "portname" : "input 0",
        "portdesc" : "datainput"
      } ]
    },
    "outputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    }
  }
}, {
  "elementname" : "flinksink_discard",
  "desc" : "a discard element, just discard every input",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "sink",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object" ],
        "portname" : "input 0",
        "portdesc" : "datainput"
      } ]
    },
    "outputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    }
  }
}, {
  "elementname" : "flinktransform_groovy",
  "desc" : "groovy transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "groovyfilepath" : {
        "type" : "string"
      },
      "groovyfilecontent" : {
        "type" : "string"
      },
      "throwgroovyexception" : {
        "type" : "boolean",
        "default" : false,
        "description" : "when u debug groovyfile, this is true, after debugging set this to false"
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject", "object" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_jsontomap",
  "desc" : "json transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "string", "bytearray" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinksource_filelines",
  "desc" : "filelines source",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "source",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "filepath" : {
        "type" : "string"
      },
      "linesinterval" : {
        "type" : "integer",
        "default" : 0,
        "description" : "lines interval sleep millseconds"
      }
    },
    "required" : [ "filepath" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "string" ],
        "portname" : "output 0",
        "portdesc" : "data output"
      } ]
    }
  }
}, {
  "elementname" : "flinksource_jdbc",
  "desc" : "jdbc source",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "source",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "jdbcurl" : {
        "type" : "string",
        "description" : "jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=UTF-8"
      },
      "dbusername" : {
        "type" : "string",
        "default" : "root"
      },
      "dbpassword" : {
        "type" : "string",
        "default" : "kedacom"
      },
      "startoffset" : {
        "type" : "integer",
        "default" : 0
      },
      "selectsql" : {
        "type" : "string",
        "description" : "select * from tablename as t1 join (select id from tablename order by id limit ?, 1) as t2 where t1.id >= t2.id order by t1.id limit 500"
      }
    },
    "required" : [ "jdbcurl", "selectsql" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinksink_redis",
  "desc" : "redis element",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "sink",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "redishost" : {
        "type" : "string",
        "description" : "127.0.0.1"
      },
      "redisport" : {
        "type" : "integer",
        "default" : 6379
      },
      "dbindex" : {
        "type" : "integer",
        "default" : 0
      },
      "password" : {
        "type" : "string",
        "default" : ""
      },
      "cluster" : {
        "type" : "string",
        "description" : "now not support cluster mode"
      },
      "valuecodec" : {
        "type" : "string",
        "default" : "STRING",
        "description" : "the codec of value",
        "enum" : [ "STRING", "BYTEARRAY" ]
      }
    },
    "required" : [ "redishost" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "listobject" ],
        "portname" : "input 0",
        "portdesc" : "datainput"
      } ]
    },
    "outputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    }
  }
}, {
  "elementname" : "flinksink_jdbc",
  "desc" : "jdbc sink",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "sink",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "jdbcurl" : {
        "type" : "string",
        "description" : "jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=UTF-8"
      },
      "dbusername" : {
        "type" : "string",
        "default" : "root"
      },
      "dbpassword" : {
        "type" : "string",
        "default" : "kedacom"
      },
      "insertsql" : {
        "type" : "string",
        "description" : "insert into testtable(testtime, testcomment) values (?, ?);"
      },
      "batchnums" : {
        "type" : "integer",
        "default" : 1000
      }
    },
    "required" : [ "jdbcurl", "insertsql" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "listobject" ],
        "portname" : "input 0",
        "portdesc" : "datainput"
      } ]
    },
    "outputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    }
  }
}, {
  "elementname" : "flinktransform_httpasync",
  "desc" : "httpasync transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "httpheaders" : {
        "type" : "array",
        "items" : {
          "type" : "object",
          "properties" : {
            "key" : {
              "type" : "string"
            },
            "value" : {
              "type" : "string"
            }
          }
        }
      },
      "httpconcurrency" : {
        "type" : "integer",
        "default" : 100
      },
      "retries" : {
        "type" : "integer",
        "default" : 3,
        "description" : "http request retry times"
      },
      "throwhttpexception" : {
        "type" : "boolean",
        "default" : true,
        "description" : "when u debug http, this is true, after debugging set this to false"
      },
      "connecttimeout" : {
        "type" : "integer",
        "default" : 5,
        "description" : "tcp connect time out seconds"
      },
      "sockettimeout" : {
        "type" : "integer",
        "default" : 10,
        "description" : "when tcp socket no data or server no response, will timeout"
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "httprequestbean" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "httpresponsebean" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinksink_elasticsearch7",
  "desc" : "elasticsearch 7 sink",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "sink",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "hosts" : {
        "type" : "string",
        "description" : "127.0.0.1:9200,192.168.1.2:9202"
      },
      "batchnums" : {
        "type" : "integer",
        "default" : 1000
      },
      "flushinterval" : {
        "type" : "integer",
        "default" : 1000,
        "description" : "timeunit is milliseconds, when this set batchnums will not working"
      },
      "throwesexception" : {
        "type" : "boolean",
        "default" : true,
        "description" : "when u debug es, this is true, after debugging set this to false"
      },
      "retries" : {
        "type" : "integer",
        "default" : 3,
        "description" : "es request retry times"
      }
    },
    "required" : [ "hosts" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "esindexbean" ],
        "portname" : "input 0",
        "portdesc" : "datainput"
      } ]
    },
    "outputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    }
  }
}, {
  "elementname" : "flinktransform_sideout",
  "desc" : "sideout transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "groovyfilepath" : {
        "type" : "string"
      },
      "groovyfilecontent" : {
        "type" : "string"
      },
      "throwgroovyexception" : {
        "type" : "boolean",
        "default" : true,
        "description" : "when u debug groovyfile, this is true, after debugging set this to false"
      },
      "sideouttags" : {
        "type" : "array",
        "items" : {
          "type" : "object",
          "properties" : {
            "outputportindex" : {
              "type" : "integer",
              "minimum" : 0
            },
            "tagname" : {
              "type" : "string"
            }
          },
          "required" : [ "tagname", "outputportindex" ]
        }
      }
    },
    "required" : [ "sideouttags" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : -1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object" ],
        "portname" : "output",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_ruler",
  "desc" : "ruler transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_redis",
  "desc" : "redis transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "throwexception" : {
        "type" : "boolean",
        "default" : true,
        "description" : "when u debug http, this is true, after debugging set this to false"
      },
      "redisinfo" : {
        "$schema" : "http://json-schema.org/draft-04/schema#",
        "type" : "object",
        "properties" : {
          "parallel" : {
            "type" : "integer",
            "description" : "独立配置算子的并发度",
            "default" : 0
          },
          "redishost" : {
            "type" : "string",
            "description" : "127.0.0.1"
          },
          "redisport" : {
            "type" : "integer",
            "default" : 6379
          },
          "dbindex" : {
            "type" : "integer",
            "default" : 0
          },
          "password" : {
            "type" : "string",
            "default" : ""
          },
          "cluster" : {
            "type" : "string",
            "description" : "now not support cluster mode"
          },
          "valuecodec" : {
            "type" : "string",
            "default" : "STRING",
            "description" : "the codec of value",
            "enum" : [ "STRING", "BYTEARRAY" ]
          }
        },
        "required" : [ "redishost" ]
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "RedisTransInputBean" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "RedisTransResultBean" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinksource_filesystem",
  "desc" : "filesystem source",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "source",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "basedirectory" : {
        "type" : "string",
        "description" : "the top directory contained target files."
      },
      "filterrule" : {
        "type" : "string",
        "default" : "*",
        "description" : "filename wildcard character. this config can be null"
      },
      "delete" : {
        "type" : "boolean",
        "default" : false,
        "description" : "whether deletes file processed; default value false, don't delete it."
      },
      "contenttype" : {
        "type" : "string",
        "default" : "LINES",
        "description" : "the type of file content has tow kinds, lines and structs. ",
        "enum" : [ "LINES", "STRUCTS" ]
      },
      "linefileconfig" : {
        "type" : "object",
        "description" : "some configs about processing txtfile.",
        "properties" : {
          "encoding" : {
            "type" : "string",
            "deafult" : "utf-8",
            "description" : "file encoding, such as utf-8, GB2312 ... if content type is lines, it need this config; if contenttype is structs msg content needs this config"
          },
          "skiplinenums" : {
            "type" : "integer",
            "default" : 0,
            "description" : "this config item will control the line number that filesystemcsourece operator will skip these lines when process this file the first time."
          }
        }
      },
      "structfileconfig" : {
        "type" : "object",
        "description" : "some configs about struct byte files.",
        "properties" : {
          "lengthbytes" : {
            "type" : "integer",
            "default" : 4,
            "description" : "support 2 or 4.only when contenttype is structs this item is needed; this config stands for the length of msg."
          },
          "endian" : {
            "type" : "string",
            "default" : "BIG",
            "enum" : [ "BIG", "LITTLE" ],
            "description" : "endian mode, big, little, default value big-endian as well as jvm"
          },
          "msgsizelimit" : {
            "type" : "integer",
            "default" : 1048576,
            "description" : "the size limit of per msg, unit Byte. stop to process current file when finding one msg size great than msgsizelimit."
          }
        }
      }
    },
    "required" : [ "basedirectory" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "string", "bytearray" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_csvtomap",
  "desc" : "cvstomap transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "splitstr" : {
        "type" : "string",
        "default" : ",",
        "description" : "the separator among cells within line, default value: half angle comma"
      },
      "columninfo" : {
        "type" : "array",
        "items" : {
          "type" : "object",
          "properties" : {
            "colindex" : {
              "type" : "integer",
              "minimum" : 0,
              "description" : "the index of element within line, starting from 0"
            },
            "colname" : {
              "type" : "string",
              "description" : "column name of element within line, used to construct a map object"
            },
            "coltype" : {
              "type" : "string",
              "enum" : [ "STRING", "LONG", "INTEGER", "FLOAT" ],
              "description" : "the data type of element, support String, Long, Integer, Float"
            }
          },
          "required" : [ "colindex", "colname", "coltype" ]
        }
      }
    },
    "required" : [ "columninfo" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "string" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinksource_jdbcv2",
  "desc" : "jdbcv2 source",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "source",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "jdbcurl" : {
        "type" : "string",
        "description" : "jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=UTF-8"
      },
      "dbusername" : {
        "type" : "string",
        "default" : "root"
      },
      "dbpassword" : {
        "type" : "string",
        "default" : "kedacom"
      },
      "uniquecolumn" : {
        "type" : "string"
      },
      "selectsql" : {
        "type" : "string",
        "description" : "select * from tablename where (a > b) and uniquecolumn > ? order by uniquecolumn asc limit 500"
      },
      "firstselectsql" : {
        "type" : "string",
        "description" : "select * from tablename where (a > b) order by uniquecolumn asc limit 500"
      },
      "countsql" : {
        "type" : "string",
        "description" : "select count(*) as totalnum from tablename where (a>b)"
      },
      "incremental" : {
        "type" : "boolean",
        "description" : "is incremental or not",
        "default" : false
      }
    },
    "required" : [ "jdbcurl", "uniquecolumn", "selectsql", "firstselectsql", "countsql" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_delay",
  "desc" : "delay transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "delayedtime" : {
        "type" : "integer",
        "default" : 10,
        "description" : "delay time, unit is second, default is 10 second"
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_connect",
  "desc" : "connect transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 2,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      }, {
        "index" : 1,
        "supporttypes" : [ "mapobject" ],
        "portname" : "input 1",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_watermarkandtimestamp",
  "desc" : "watermarkandtimestamp transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "timetype" : {
        "type" : "string",
        "default" : "PROCESSTIME",
        "description" : "the type of the flink system, default is process time, most is event time",
        "enum" : [ "EVENTTIME", "PROCESSTIME" ]
      },
      "watermarkinterval" : {
        "type" : "integer",
        "default" : 0,
        "description" : "Periodic Watermarks default is 0, when event time should set bigger than 0 "
      },
      "boundedoutoforderness" : {
        "type" : "object",
        "properties" : {
          "maxoutoforderness" : {
            "type" : "integer",
            "description" : "max outof orderness unit is mill seconds",
            "default" : 100
          },
          "extracttimestamp" : {
            "type" : "string",
            "description" : "extracetimestamp from map object, this is the key name"
          }
        }
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "object" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_join",
  "desc" : "join transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "leftkeyselector" : {
        "type" : "string"
      },
      "rightkeyselector" : {
        "type" : "string"
      },
      "windowinfo" : {
        "type" : "object",
        "properties" : {
          "windowtype" : {
            "type" : "string",
            "default" : "SLIDING",
            "enum" : [ "TUMBLING", "SLIDING" ]
          },
          "size" : {
            "type" : "integer",
            "default" : 0,
            "description" : "unit is millisecond"
          },
          "slide" : {
            "type" : "integer",
            "default" : 0,
            "description" : "unit is millisecond just in sliding"
          },
          "offset" : {
            "type" : "integer",
            "default" : 0,
            "description" : "unit is millisecond"
          },
          "allowedLateness" : {
            "type" : "integer",
            "default" : 0,
            "description" : "unit is millisecond"
          }
        }
      }
    },
    "required" : [ "leftkeyselector", "rightkeyselector" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 2,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      }, {
        "index" : 1,
        "supporttypes" : [ "mapobject" ],
        "portname" : "input 1",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "joinresult" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_intervaljoin",
  "desc" : "intervaljoin transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "leftkeyselector" : {
        "type" : "string"
      },
      "rightkeyselector" : {
        "type" : "string"
      },
      "lowerbound" : {
        "type" : "integer",
        "default" : -5000,
        "description" : "lower bound, unit is milliseconds"
      },
      "upperbound" : {
        "type" : "integer",
        "default" : 5000,
        "description" : "lower bound, unit is milliseconds"
      }
    },
    "required" : [ "leftkeyselector", "rightkeyselector" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 2,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "left input",
        "portdesc" : "base compare data stream"
      }, {
        "index" : 1,
        "supporttypes" : [ "mapobject" ],
        "portname" : "right input",
        "portdesc" : "be compared data stream"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "joinresult" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinksource_redis",
  "desc" : "redis source",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "source",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "redisinfo" : {
        "$schema" : "http://json-schema.org/draft-04/schema#",
        "type" : "object",
        "properties" : {
          "parallel" : {
            "type" : "integer",
            "description" : "独立配置算子的并发度",
            "default" : 0
          },
          "redishost" : {
            "type" : "string",
            "description" : "127.0.0.1"
          },
          "redisport" : {
            "type" : "integer",
            "default" : 6379
          },
          "dbindex" : {
            "type" : "integer",
            "default" : 0
          },
          "password" : {
            "type" : "string",
            "default" : ""
          },
          "cluster" : {
            "type" : "string",
            "description" : "now not support cluster mode"
          },
          "valuecodec" : {
            "type" : "string",
            "default" : "STRING",
            "description" : "the codec of value",
            "enum" : [ "STRING", "BYTEARRAY" ]
          }
        },
        "required" : [ "redishost" ]
      },
      "command" : {
        "type" : "string",
        "default" : "",
        "description" : "supported redis commands: get, hget, hgetall",
        "enum" : [ "GET", "HGET", "HGETALL" ]
      },
      "rediskey" : {
        "type" : "string",
        "description" : "required. this key stands for the primary KEY"
      },
      "additionalkey" : {
        "type" : "string",
        "description" : "option"
      },
      "increment" : {
        "type" : "integer",
        "default" : 0,
        "minimum" : 0,
        "description" : "the interval refresh the key value, seconds"
      }
    },
    "required" : [ "redisinfo", "rediskey" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 0,
      "portlist" : [ ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_protobufdecoder",
  "desc" : "protobufdecoder transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      },
      "topictoprotojavaclass" : {
        "type" : "array",
        "items" : {
          "type" : "object",
          "properties" : {
            "topicname" : {
              "type" : "string"
            },
            "protojavaclassname" : {
              "type" : "string",
              "description" : "protobuf message java classname"
            }
          },
          "required" : [ "topicname", "protojavaclassname" ]
        }
      }
    },
    "required" : [ "topictoprotojavaclass" ]
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "bytearray", "kafkafulldata" ],
        "portname" : "input 0",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "protobufmessage" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
}, {
  "elementname" : "flinktransform_union",
  "desc" : "union transform",
  "elementtype" : "algorithm",
  "enginetype" : "flink",
  "enginecaptype" : "transform",
  "elementconfigschema" : {
    "$schema" : "http://json-schema.org/draft-04/schema#",
    "type" : "object",
    "properties" : {
      "parallel" : {
        "type" : "integer",
        "description" : "独立配置算子的并发度",
        "default" : 0
      }
    }
  },
  "portinfos" : {
    "inputport" : {
      "portcount" : -1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "input",
        "portdesc" : "dataoinput"
      } ]
    },
    "outputport" : {
      "portcount" : 1,
      "portlist" : [ {
        "index" : 0,
        "supporttypes" : [ "mapobject" ],
        "portname" : "output 0",
        "portdesc" : "dataoutput"
      } ]
    }
  }
} ]

```

## elasticsearch7 1.9.0
in the es7

## flinketl
sh compile.sh

copy flinketl-1.0.0.jar to flink/lib  
submit job flinketljob-1.0.0.jar --jsonfile /path  
### debugging flinketl in the flinketljob project
in the flinketljob project, right side maven menu, press + "add maven projects", will add flinketl to flinketljob.    
do not use project structure, to import module.


### protobuf to map and map to protobuf
can get information from here:  

https://github.com/bivas/protobuf-java-format/blob/f2e648f5474ee1de0372eb6736b70ca366897f13/src/main/java/com/googlecode/protobuf/format/JsonFormat.java