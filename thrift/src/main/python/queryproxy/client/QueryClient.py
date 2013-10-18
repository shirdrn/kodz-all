#!/usr/bin/python

# coding=utf-8
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from queryproxy.thrift.protocol.QueryProxyService import Client

def query():
    try:
        transport = TSocket.TSocket('server.query-proxy.local', 9966)
        transport = TTransport.TBufferedTransport(transport)
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        transport.open()
        
        Client client = new Client(protocol)
        client.query()
    except TException, tx:
        print '%s' % (tx.message) 


if __name__ == '__main__':
    query()