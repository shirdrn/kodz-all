using System;
using System.Collections.Generic;

using Thrift.Transport;
using Thrift.Protocol;
using Query.Proxy.Thrift.Protocol;

namespace CSharpQueryClient
{
    class QueryClient
    {
        static void Main(string[] args)
        {
            string host = "server.query-proxy.local";
            TTransport transport = new TSocket(host, 9966);
            TProtocol protocol = new TBinaryProtocol(transport, true, true);
            transport.Open();
            QueryProxyService.Client client = new QueryProxyService.Client(protocol);

            QueryParams queryParams = new QueryParams();
            queryParams.Table = "collection1";
            queryParams.Type = QueryType.SOLR;
            queryParams.ParamList = new List<string>();
            queryParams.ParamList.Add("q=上海");
            queryParams.ParamList.Add("fl=*");
            queryParams.ParamList.Add("fq=building_type:1");
            queryParams.ParamList.Add("start=50");
            queryParams.ParamList.Add("rows=10");
            queryParams.ParamList.Add("wt=json");

            QueryResult result = client.query(queryParams);
            Console.WriteLine("offset=" + result.Offset);
            Console.WriteLine("length=" + result.Length);
            foreach (string record in result.Results)
            {
                Console.WriteLine("record=" + record);
            }
            
            Console.Read();
        }
    }
}
