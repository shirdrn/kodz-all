package org.shirdrn.platform.dubbo.service.rpc.client;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.shirdrn.platform.dubbo.service.rpc.api.SolrSearchService;
import org.shirdrn.platform.dubbo.service.rpc.api.SolrSearchService.ResponseType;
import org.springframework.beans.BeansException;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.rpc.RpcContext;

public class SearchConsumer {
	
	private final String collection;
	private AbstractXmlApplicationContext context;
	private SolrSearchService searchService;
	
	public SearchConsumer(String collection, Callable<AbstractXmlApplicationContext> call) {
		super();
		this.collection = collection;
		try {
			context = call.call();
			context.start();
			searchService = (SolrSearchService) context.getBean("searchService");
		} catch (BeansException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Future<String> asyncCall(final String q, final ResponseType type, final int start, final int rows) {
		Future<String> future = RpcContext.getContext().asyncCall(new Callable<String>() {
			public String call() throws Exception {
				return search(q, type, start, rows);
			}
		});
		return future;
	}
	
	public String syncCall(final String q, final ResponseType type, final int start, final int rows) {
		return search(q, type, start, rows);
	}

	private String search(final String q, final ResponseType type, final int start, final int rows) {
		return searchService.search(collection, q, type, start, rows);
	}
	
	public static void main(String[] args) throws Exception {
		final String collection = "tinycollection";
		final String beanXML = "search-consumer.xml";
		final String config = SearchConsumer.class.getPackage().getName().replace('.', '/') + "/" + beanXML;
		SearchConsumer consumer = new SearchConsumer(collection, new Callable<AbstractXmlApplicationContext>() {
			public AbstractXmlApplicationContext call() throws Exception {
				final AbstractXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
				return context;
			}
		});
		
		String q = "q=上海&fl=*&fq=building_type:1";
		int start = 0;
		int rows = 10;
		ResponseType type  = ResponseType.XML;
		for (int k = 0; k < 1000; k++) {
			for (int i = 0; i < 10; i++) {
				start = 1 * 10 * i;
				if(i % 2 == 0) {
					type = ResponseType.XML;
				} else {
					type = ResponseType.JSON;
				}
				Thread.sleep(200);
//				String result = consumer.syncCall(q, type, start, rows);
//				System.out.println(result);
				Future<String> future = consumer.asyncCall(q, type, start, rows);
				System.out.println(future.get());
			}
		}
	}

}
