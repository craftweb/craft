/*
 * Copyright Andrei Goumilevski
 * This file licensed under GPLv3 for non commercial projects
 * GPLv3 text http://www.gnu.org/licenses/gpl-3.0.html
 * For commercial usage please contact me
 * gmlvsk2@gmail.com
 *
*/

package com.startupoxygen.craft.mongo.rest;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * query syntax
 * http://lucene.apache.org/core/old_versioned_docs/versions/3_5_0/queryparsersyntax.html
 */

public class Search {

  private static final Logger log = LoggerFactory.getLogger(Search.class);
  private IndexWriter writer;
  private static Search mysearch;
  //private TopScoreDocCollector collector;
  private StandardAnalyzer analyzer;
  private String index_path;
  private Directory index;
  private boolean commited = false;
  private SearcherManager sm;
  private ScheduledFuture<?> alarm;
  private ScheduledExecutorService service;
  private ThreadLocal<QueryParser> tl;

  // -------------------------------------------------
  private Search( String index_path ) throws IOException, ParseException {

    this.index_path = index_path;
    analyzer = new StandardAnalyzer( Version.LUCENE_36 );
    tl = new ThreadLocal<QueryParser>(){
      @Override 
      protected synchronized QueryParser initialValue(){
	return new QueryParser( Version.LUCENE_36, Config.search_default_field, analyzer );
      }
    };

    File f = new File( index_path );
    if( !f.exists() )
      f.mkdir();
    index = FSDirectory.open( f );

    try{
      sm = new SearcherManager( index, new SearcherFactory() );
    }
    catch( IndexNotFoundException e ){
      log.warn( "No index found. First time use?" );
      create_writer();
      writer.commit();
      sm = new SearcherManager( index, new SearcherFactory() );
    }

    final Runnable r = new Runnable(){
      public void run(){
	try{
	  sm.maybeRefresh();
	}
	catch( IOException e ){
	  log.error( "maybeRefresh() error "+e );
	}
      }
    };
    service = Executors.newScheduledThreadPool( 1 );
    alarm = service.scheduleAtFixedRate( r, 30, 30, MINUTES );

  }

  // -------------------------------------------------
  private void create_writer() 
    throws IOException, CorruptIndexException, LockObtainFailedException {

    if( writer!=null )
      return;

    IndexWriterConfig config = new IndexWriterConfig( Version.LUCENE_36, analyzer );

    writer = new IndexWriter( index, config );

  }

  // -------------------------------------------------
  public Search get_writer() 
    throws IOException, CorruptIndexException, LockObtainFailedException {

    if( writer!=null )
      return mysearch;

    create_writer();

    return mysearch;

  }

  // -------------------------------------------------
  public static Search get(){ 
    return mysearch;
  }

  // -------------------------------------------------
  public Search get_searcher() throws IOException {

    if( commited ){
      sm.maybeRefresh();
      commited = false;
    }

    return mysearch;

  }

  // -------------------------------------------------
  public static Search init( String index_path ) throws IOException, ParseException {

    log.info( "Search init:"+index_path );

    if( mysearch!=null )
      return mysearch;

    return mysearch=new Search( index_path );

  }

  // -------------------------------------------------
  public void _close() throws IOException {

    if( writer!=null )
      writer.close();
    writer = null;

    sm.close();
    sm = null;

    alarm.cancel( true );
    alarm = null;
    service.shutdownNow();
    service = null;

  }

  // -------------------------------------------------
  public static void close() throws IOException {

    if( mysearch==null )
      return;

    mysearch._close();
    mysearch = null;

  }

  // -------------------------------------------------
  public void add( Document doc ) throws IOException {
    writer.addDocument(doc);
  }

  // -------------------------------------------------
  static void add_searchable_s( Document doc, String k, String v ){
    Field f = new Field( k, v, Field.Store.NO, Field.Index.ANALYZED );
    doc.add( f );
  }

  // -------------------------------------------------
  static void add_searchable_n( Document doc, String k, int v ){
    NumericField f = new NumericField( k ).setIntValue(v);
    f.setOmitNorms( true );
    doc.add( f );
  }

  // -------------------------------------------------
  static void add_storable( Document doc, String k, String v ){
    Field f = new Field( k, v, Field.Store.YES, Field.Index.NO );
    doc.add( f );
  }

  // -------------------------------------------------
  void commit( Document doc ) throws CorruptIndexException, IOException {
    if( writer!=null )
      writer.addDocument( doc );
    else
      log.warn( "writer not available" );
  }

  // -------------------------------------------------
  void commit() throws CorruptIndexException, IOException {
    if( writer!=null ){
      writer.commit();
      commited = true;
    }
    else
      log.warn( "writer not available" );
  }

  // -------------------------------------------------
  public void update( String toupdate[], Document doc ) 
    throws IOException {

    for( String id:toupdate ){

      Term t = new Term( "_id", id );
      writer.updateDocument( t, doc );

    }

  }

  // -------------------------------------------------
  public void delete( String todelete[] ) throws IOException {

    for( String id:todelete ){

      Term t = new Term( "_id", id );
      Query q = new TermQuery( t );
      writer.deleteDocuments( q );

    }

  }

  // -------------------------------------------------
  private static Query add_dbid( Query q2, String dbid ){

    BooleanQuery bq = new BooleanQuery();

    Term t = new Term( "_dbid_", dbid );
    Query q = new TermQuery( t );
    bq.add( q, Occur.MUST );

    bq.add( q2, Occur.MUST );

    return bq;

  }

  // -------------------------------------------------
  public Document[] search( String dbid, String k, String v, int count ) 
    throws IOException, ParseException {


    Term t = new Term( k, v );
    Query q = new TermQuery( t );
    Query q2 = add_dbid( q, dbid );

    TopScoreDocCollector collector = TopScoreDocCollector.create( count, true );
    IndexSearcher searcher = sm.acquire();
    Document docs[] = null;

    try{
      searcher.search( q2, collector );
      ScoreDoc[] hits = collector.topDocs().scoreDocs;
      if( hits.length==0 )
	return null;
      docs = new Document[hits.length]; 
      for( int i=0; i<hits.length; i++ ){
	int doc_id = hits[i].doc;
	docs[i] = searcher.doc( doc_id );
      }
    }
    finally{
      sm.release( searcher );
    }

    return docs;

  }

  // -------------------------------------------------
  public Document[] search2( String dbid, String q, int count ) 
    throws IOException, ParseException {


    Query query = tl.get().parse( QueryParser.escape(q) );
    Query q2 = add_dbid( query, dbid );

    TopScoreDocCollector collector = TopScoreDocCollector.create( count, true );
    IndexSearcher searcher = sm.acquire();
    Document docs[] = null;

    try{
      searcher.search( q2, collector );
      ScoreDoc[] hits = collector.topDocs().scoreDocs;
      if( hits.length==0 )
	return null;
      docs = new Document[hits.length]; 
      for( int i=0; i<hits.length; i++ ){
	int doc_id = hits[i].doc;
	docs[i] = searcher.doc( doc_id );
      }
    }
    finally{
      sm.release( searcher );
    }

    return docs;

  }

}
