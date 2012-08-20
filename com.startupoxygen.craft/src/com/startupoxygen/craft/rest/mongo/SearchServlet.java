/*
 * Copyright Andrei Goumilevski
 * This file licensed under GPLv3 for non commercial projects
 * GPLv3 text http://www.gnu.org/licenses/gpl-3.0.html
 * For commercial usage please contact me
 * gmlvsk2@gmail.com
 *
*/

package com.startupoxygen.craft.rest.mongo;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_IMPLEMENTED;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@SuppressWarnings("serial")
@WebServlet(name="SearchServlet")
public class SearchServlet extends SkeletonMongodbServlet {
  
  private static final Logger log = LoggerFactory.getLogger(SearchServlet.class);
  private ThreadLocal<StringBuilder> tl = new ThreadLocal<StringBuilder>(){
    @Override 
    protected synchronized StringBuilder initialValue(){
      return new StringBuilder( 1024*4 );
    }
  };
  private boolean rm_id = Config.mongo_remove_idfield;
  // number of search result in top hits
  private static final int DEFAULT_COUNT = 10;

  // --------------------------------
  @Override 
  public void init() throws ServletException{

    super.init();
    String name = getServletName();
    log.trace( "init() "+name );

  }

  // --------------------------------
  @Override 
  public void destroy(){

    String name = getServletName();
    log.trace( "destroy() "+name );

  }

  // ------------------------------------
  private static int get_limit( String s ){

    if( s==null )
      return DEFAULT_COUNT;

    int lim;
    try{
      lim =  Integer.parseInt( s );
    }catch( NumberFormatException e ){
      lim = DEFAULT_COUNT;
    }

    return lim;

  }

  // POST
  // ------------------------------------
  @Override 
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "doPost()" );

    if( !Config.search ){
      error( res, SC_NOT_IMPLEMENTED, Status.get("search not configured") );
      return;
    }

    if( !can_read(req) ){
      res.sendError( SC_UNAUTHORIZED );
      return;
    }

    InputStream is = req.getInputStream();
    String db_name = req.getParameter( "dbname" );
    String col_name = req.getParameter( "colname" );
    if( db_name==null || col_name==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }

    int limit = get_limit( req.getParameter("limit") );

    BufferedReader r = null;
    String q = null;

    try{

      r = new BufferedReader( new InputStreamReader(is) ); 
      q = r.readLine();
      if( q==null ){
	error( res, SC_BAD_REQUEST, Status.get("no data") );
	return;
      }

    }
    finally{
      if( r!=null )
	r.close();
    }


    String full_name = db_name + "." + col_name;
    Document[] hits = null;
    try{
      hits = search.get_searcher().search2( full_name, q, limit );
    }
    catch( ParseException e ){
      error( res, SC_BAD_REQUEST, Status.get("query parse error:"+e) );
      return;
    }

    if( hits==null || hits.length==0 ){
      error( res, SC_NOT_FOUND, Status.get("no documents found") );
      return;
    }

    DB db = mongo.getDB( db_name );

    // mongo auth
    String user = req.getParameter( "user" );
    String passwd = req.getParameter( "passwd" );
    if( user!=null&&passwd!=null&&(!db.isAuthenticated()) ){
      boolean auth = db.authenticate( user, passwd.toCharArray() );
      if( !auth ){
	res.sendError( SC_UNAUTHORIZED );
	return;
      }
    }

    DBCollection col = db.getCollection( col_name );

    String ret = search2mongo( hits, col );

    if( ret==null ){
      error( res, SC_NOT_FOUND, Status.get("no documents found") );
      return;
    }

    out_str( req, ret, "application/json" );

  } 


  // ------------------------------------
  private String search2mongo( Document hits[], DBCollection col ){

    List<ObjectId> values = new ArrayList<ObjectId>();
    for( Document hit:hits ){
      String _id = hit.get("_id");
      ObjectId	oid = ObjectId.massageToObjectId( _id ); 
      values.add( oid );
    }

    BasicDBObject q = new BasicDBObject();
    q.put( "_id", new BasicDBObject("$in", values) );

    DBCursor c = col.find( q );
    if( c==null || c.count()==0 ){
      return null;
    }

    StringBuilder buf = tl.get();
    // reset buf
    buf.setLength( 0 );

    int no = 0;
    buf.append( "[" );
    while( c.hasNext() ){

      DBObject o = c.next();
      if( rm_id )
	o.removeField( "_id" );
      JSON.serialize( o, buf );
      buf.append( "," );
      no++;

    }
    c.close();

    if( no>0 )
      buf.setCharAt( buf.length()-1, ']' );
    else
      buf.append( ']' );

    return buf.toString();

  }

  // GET
  // ------------------------------------
  @Override 
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "doGet()" );

    if( !Config.search ){
      error( res, SC_NOT_IMPLEMENTED, Status.get("search not configured") );
      return;
    }

    if( !can_read(req) ){
      res.sendError( SC_UNAUTHORIZED );
      return;
    }

    String op = req.getParameter( "op" );
    if( op==null )
      op = "search";

    if( "commit".equals(op) ){
      if( !can_write(req) ){
	res.sendError( SC_UNAUTHORIZED );
	return;
      }
      search.commit();
      out_json( req, Status.OK );
      return;
    }

    String db_name = req.getParameter( "dbname" );
    String col_name = req.getParameter( "colname" );
    String field = req.getParameter( "field" );
    String text = req.getParameter( "text" );
    if( db_name==null || col_name==null || field==null || text==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }

    int limit = get_limit( req.getParameter("limit") );

    String full_name = db_name + "." + col_name;
    Document[] hits = null;
    try{
      hits = search.get_searcher().search( full_name, field, text.toLowerCase(), limit );
    }
    catch( ParseException e ){
      error( res, SC_BAD_REQUEST, Status.get("query parse error:"+e) );
      return;
    }

    if( hits==null || hits.length==0 ){
      error( res, SC_NOT_FOUND, Status.get("no documents found") );
      return;
    }

    DB db = mongo.getDB( db_name );

    // mongo auth
    String user = req.getParameter( "user" );
    String passwd = req.getParameter( "passwd" );
    if( user!=null&&passwd!=null&&(!db.isAuthenticated()) ){
      boolean auth = db.authenticate( user, passwd.toCharArray() );
      if( !auth ){
	res.sendError( SC_UNAUTHORIZED );
	return;
      }
    }

    DBCollection col = db.getCollection( col_name );

    String ret = search2mongo( hits, col );

    if( ret==null ){
      error( res, SC_NOT_FOUND, Status.get("no documents found") );
      return;
    }

    out_str( req, ret, "application/json" );

  } 

  // -------------------------------------------------
  // For cross-origin requests
  /*
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "options" );

    Header h = req.getFirstHeader( "Access-Control-Request-Headers" );
    h = req.getFirstHeader( "Access-Control-Request-Method" );
    h = req.getFirstHeader( "Origin" );

    res.setHeader( "Access-Control-Allow-Origin", "*" );
    res.setHeader( "Access-Control-Allow-Headers", "authorization" );
    res.setHeader( "Access-Control-Allow-Methods", "GET,POST,OPTIONS" );

    res.setStatus( SC_OK );

  }
  */

}
