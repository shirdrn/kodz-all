namespace java org.shirdrn.queryproxy.thrift.protocol
namespace py org.shirdrn.queryproxy.thrift.python.protocol

typedef i16 short
typedef i32 int
typedef i64 long

enum QueryType {
	SOLR = 1,
	SQL = 2
}

struct QueryParams {
	1:QueryType TYPE,
	2:string table,
	3:list<string> params
}

struct QueryResult {
	1:int offset,
	2:int length
	3:list<string> results
}

exception QueryFailureException {
  1:string message
}

service QueryProxyService {

	QueryResult query(1:QueryParams params) throws (1:QueryFailureException qe)
	
}