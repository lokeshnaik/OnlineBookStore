package com.bridgelabz.bookstore.serviceimplementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstore.configuration.ElasticSearchConfig;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.service.ElasticSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ElasticSearchServiceImplementation implements ElasticSearchService {

	@Autowired
	ElasticSearchConfig elasticSearchConfig;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String INDEX = "springboot";

	private static final String TYPE = "note_details";

	@Override
	public String addBook(BookModel bookModel) {

		Map<String, Object> notemapper = objectMapper.convertValue(bookModel, Map.class);
		IndexRequest indexrequest = new IndexRequest(INDEX, TYPE, String.valueOf(bookModel.getBookId()))
				.source(notemapper);
		IndexResponse indexResponse = null;
		try {
			indexResponse = elasticSearchConfig.client().index(indexrequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		log.info(indexrequest);
		log.info(indexResponse);
		return indexResponse.getResult().name();
	}
	
	 @Override
	 public List<BookModel> searchByTitle(String title) {

	 SearchRequest searchrequest = new SearchRequest("springboot");
	 SearchSourceBuilder searchsource = new SearchSourceBuilder();

	 searchsource.query(QueryBuilders.matchQuery("bookName", title));
	 searchrequest.source(searchsource);
	 SearchResponse searchresponse = null;
	 try {
	 searchresponse = elasticSearchConfig.client().search(searchrequest, RequestOptions.DEFAULT);
	 } catch (Exception e) {
	 log.info(e.getMessage());
	 }
	 return getResult(searchresponse);
	 }
	 
	 private List<BookModel> getResult(SearchResponse searchresponse) {
		 SearchHit[] searchhits = searchresponse.getHits().getHits();
		 List<BookModel> books = new ArrayList<>();
		 if (searchhits.length > 0) {
		 Arrays.stream(searchhits)
		 .forEach(hit -> books.add(objectMapper.convertValue(hit.getSourceAsMap(), BookModel.class)));
		 }
		 return books;
	}


	 @Override
	 public String updateBook(BookModel bookModel) {
	 Map<String, Object> notemapper = objectMapper.convertValue(bookModel, Map.class);
	 log.info(bookModel.getBookId());
	 UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(bookModel.getBookId()))
	 .doc(notemapper);
	org.elasticsearch.action.update.UpdateResponse updateResponse = null;
	 try {
	 updateResponse = elasticSearchConfig.client().update(updateRequest, RequestOptions.DEFAULT);
	 } catch (IOException e) {
	 log.info(e.getMessage());
	 }
	 log.info(updateRequest);
	log.info(updateResponse);
	 return updateResponse.getResult().name();

	 }
	
	 @Override
	 public Long deleteNote(Long bookId) {
	 Map<Long, Object> notemapper = objectMapper.convertValue(bookId, Map.class);
	 DeleteRequest deleterequest = new DeleteRequest(INDEX, TYPE, toString(bookId));
	 DeleteResponse deleteResponse = null;
	 try {
	 deleteResponse = elasticSearchConfig.client().delete(deleterequest, RequestOptions.DEFAULT);
	 } catch (IOException e) {
	 log.info(e.getMessage());
	 }
    return bookId;
	 
	 }

	private String toString(Long bookId) {
		
		return Long.toString(bookId);
	}
	
}
