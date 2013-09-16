
SolrCloud集群安装指南


================
               术  语
================
Collection：一个关系表的对应一个Collection，一个Collection是Solr中一个逻辑意义上完整的索引，如果设置分片，一个Collection由多个物理上分布的分片组成。


================
            命名规范
================
为了便于维护SolrCloud集群，在进行部署多个Collection的过程中，对配置目录等进行规范化，能够更好地根据需要动态扩展和变更。
1、ZooKeeper中存储的Collection名称规范
    (1)必须小写字母
    (2)必须以_collection结尾
例如：kpi_event_collection

2、ZooKeeper中存储的配置目录名称规范
    (1)必须小写字母
    (2)必须以_conf结尾
例如：kpi_event_conf


================
               前置条件
================
1、安装Tomcat Web服务器
2、安装ZooKeeper集群
3、Tomcat启动前，在bin/catalina.sh脚本中增加ZooKeeper集群信息配置：
JAVA_OPTS="-server -Xmx4096m -Xms1024m -verbose:gc -Xloggc:solr_gc.log -Dsolr.solr.home=/home/hadoop/applications/solr/cloud/multicore -DzkHost=master:2188,slave1:2188,slave4:2188"
4、将solr-cloud webapp部署到Tomcat服务器（http://10.95.3.38:81/svn/COOLTEST_5_0/trunk/solr-cloud）

================
               基本命令
================
1、上传Solr Collection配置文件到ZooKeeper集群
java -classpath <SOLR_LIB_CLASSPATH> org.apache.solr.cloud.ZkCLI -zkhost <zkHost1[,zkHost2[,...zkHostN]]> -cmd upconfig -confdir <COLLECTION_CONF> -confname <CONF_NAME>

2、建立Solr Collection配置到Collection名称的映射
java -classpath <SOLR_LIB_CLASSPATH> org.apache.solr.cloud.ZkCLI -zkhost <zkHost1[,zkHost2[,...zkHostN]]> -cmd linkconfig -collection <COLLECTION_NAME> -confname <CONF_NAME>

3、下载Solr Collection配置文件从ZooKeeper集群
java -classpath <SOLR_LIB_CLASSPATH> org.apache.solr.cloud.ZkCLI -zkhost <zkHost1[,zkHost2[,...zkHostN]]> -cmd downconfig -confdir <COLLECTION_CONF> -confname <CONF_NAME>

4、创建Solr Collection
curl 'http://<HOST>:<PORT>/<WEBAPP_ROOT>/admin/collections?action=CREATE&name=<COLLECTION_NAME>&numShards=<NUM_OF_SHARD>&replicationFactor=<NUM_OF_REPLICAS>'

5、删除Solr Collection
curl 'http://<HOST>:<PORT>/<WEBAPP_ROOT>/admin/collections?action=DELETE&name=<COLLECTION_NAME>'

6、分割一个Collection的Shard
curl 'http://<HOST>:<PORT>/<WEBAPP_ROOT>/admin/collections?action=SPLITSHARD&collection=<COLLECTION_NAME>&shard=<_NAME>'


================
     部署Collection
================
1、部署kpi_event_collection
执行如下命令：
java -classpath .:/home/hadoop/applications/solr/cloud/lib/* org.apache.solr.cloud.ZkCLI -cmd upconfig -zkhost slave1:2188,slave3:2188,slave4:2188 -confdir /home/hadoop/solr/collections/kpi_event_collection/kpi_event/conf -confname kpi_event_conf
java -classpath .:/home/hadoop/applications/solr/cloud/lib/* org.apache.solr.cloud.ZkCLI -cmd linkconfig -collection kpi_event -confname kpi_event_conf -zkhost slave1:2188,slave3:2188,slave4:2188
curl 'http://slave1:8888/solr-cloud/admin/collections?action=CREATE&name=kpi_event&numShards=1&replicationFactor=2'

2、部署i_event_collection
执行如下命令：
java -classpath .:/home/hadoop/applications/solr/cloud/lib/* org.apache.solr.cloud.ZkCLI -cmd upconfig -zkhost slave1:2188,slave3:2188,slave4:2188 -confdir /home/hadoop/solr/collections/i_event_collection/i_event/conf -confname i_event_conf
java -classpath .:/home/hadoop/applications/solr/cloud/lib/* org.apache.solr.cloud.ZkCLI -cmd linkconfig -collection i_event -confname i_event_conf -zkhost slave1:2188,slave3:2188,slave4:2188
curl 'http://slave1:8888/solr-cloud/admin/collections?action=CREATE&name=i_event&numShards=2&replicationFactor=3'



java -classpath .:/home/hadoop/applications/solr/cloud/lib/* org.apache.solr.cloud.ZkCLI -cmd upconfig -zkhost slave1:2188,slave3:2188,slave4:2188 -confdir /home/hadoop/solr/collections/i_event_2_collection/i_event_2/conf -confname i_event_2_conf
java -classpath .:/home/hadoop/applications/solr/cloud/lib/* org.apache.solr.cloud.ZkCLI -cmd linkconfig -collection i_event_2 -confname i_event_2_conf -zkhost slave1:2188,slave3:2188,slave4:2188
curl 'http://slave1:8888/solr-cloud/admin/collections?action=CREATE&name=i_event_2&numShards=2&replicationFactor=2'