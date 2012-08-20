/*
 * Copyright Andrei Goumilevski
 * This file licensed under GPLv3 for non commercial projects
 * GPLv3 text http://www.gnu.org/licenses/gpl-3.0.html
 * For commercial usage please contact me
 * gmlvsk2@gmail.com
 *
*/

package com.startupoxygen.craft.mongo.rest;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

@SuppressWarnings("serial")
@WebServlet(name="QueryServlet")
public class AggregateServlet extends SkeletonMongodbServlet {
  
  private final static int MAX_FIELDS_TO_RETURN = 1000;
  private static final Logger log = LoggerFactory.getLogger(AggregateServlet.class);
  private ThreadLocal<StringBuilder> tl = new ThreadLocal<StringBuilder>(){
    @Override 
    protected synchronized StringBuilder initialValue(){
      return new StringBuilder( 1024*4 );
    }
  };

  // --------------------------------
  @Override 
  public void init() throws ServletException{

    String name = getServletName();
    log.trace( "init() "+name );

  }

  // --------------------------------
  @Override 
  public void destroy(){

    String name = getServletName();
    log.trace( "destroy() "+name );

  }

  // POST
  // ------------------------------------
  @Override 
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "doPost()" );

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
    String skip = req.getParameter( "skip" );
    String limit = req.getParameter( "limit" );

    DB db = mongo.getDB( db_name );
    DBCollection col = db.getCollection( col_name );

    BufferedReader r = null;
    DBObject q=null, sort=null;
    try{

      r = new BufferedReader( new InputStreamReader(is) ); 
      String data = r.readLine();
      if( data==null ){
	error( res, SC_BAD_REQUEST, Status.get("no data") );
	return;
      }
      try{
	q = (DBObject)JSON.parse( data );
      }
      catch( JSONParseException e ){
	error( res, SC_BAD_REQUEST, Status.get("can not parse data") );
	return;
      }
      // sort param
      data = r.readLine();
      if( data!=null ){
	try{
	  sort = (DBObject)JSON.parse( data );
	}
	catch( JSONParseException e ){
	  error( res, SC_BAD_REQUEST, Status.get("can not parse sort arg") );
	  return;
	}
      }

    }
    finally{
      if( r!=null )
	r.close();
    }

    DBCursor c;
    if( sort==null )
      c = col.find( q );
    else
      c = col.find( q ).sort( sort );
    if( c==null ){
      error( res, SC_NOT_FOUND, Status.get("no documents found") );
      return;
    }

    res.setIntHeader( "X-Documents-Count", c.count() );

    if( limit!=null ){
      try{
	c.limit( Math.min(Integer.parseInt(limit), MAX_FIELDS_TO_RETURN) );
      }catch( NumberFormatException e ){
	error( res, SC_BAD_REQUEST, Status.get("can not parse limit") );
	c.close();
	return;
      }
    }
    else
      c.limit( MAX_FIELDS_TO_RETURN );

    if( skip!=null ){
      try{
	c.skip( Integer.parseInt(skip) );
      }catch( NumberFormatException e ){
	error( res, SC_BAD_REQUEST, Status.get("can not parse skip") );
	c.close();
	return;
      }
    }

    StringBuilder buf = tl.get();
    // reset buf
    buf.setLength( 0 );

    int no = 0;
    buf.append( "[" );
    while( c.hasNext() ){

      DBObject o = c.next();
      JSON.serialize( o, buf );
      buf.append( "," );
      no++;

    }

    if( no>0 )
      buf.setCharAt( buf.length()-1, ']' );
    else
      buf.append( ']' );

    res.setIntHeader( "X-Documents-Returned", no );

    out_str( req, buf.toString(), "application/json" );

  } 

  // GET
  // ------------------------------------
  @Override 
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "doGet()" );

    if( !can_read(req) ){
      res.sendError( SC_UNAUTHORIZED );
      return;
    }

    String db_name = req.getParameter( "dbname" );
    String col_name = req.getParameter( "colname" );
    if( db_name==null || col_name==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }
    String skip = req.getParameter( "skip" );
    String limit = req.getParameter( "limit" );

    DB db = mongo.getDB( db_name );
    DBCollection col = db.getCollection( col_name );

    DBCursor c = col.find();
    if( c==null ){
      error( res, SC_NOT_FOUND, Status.get("no documents found") );
      return;
    }

    res.setIntHeader( "X-Documents-Count", c.count() );

    if( limit!=null ){
      try{
	c.limit( Math.min(Integer.parseInt(limit), MAX_FIELDS_TO_RETURN) );
      }catch( NumberFormatException e ){
	error( res, SC_BAD_REQUEST, Status.get("can not parse limit") );
	c.close();
	return;
      }
    }
    else
      c.limit( MAX_FIELDS_TO_RETURN );

    if( skip!=null ){
      try{
	c.skip( Integer.parseInt(skip) );
      }catch( NumberFormatException e ){
	error( res, SC_BAD_REQUEST, Status.get("can not parse skip") );
	c.close();
	return;
      }
    }

    StringBuilder buf = tl.get();
    buf.setLength( 0 );

    int no = 0;
    buf.append( "[" );
    while( c.hasNext() ){

      DBObject o = c.next();
      JSON.serialize( o, buf );
      buf.append( "," );
      no++;

    }

    if( no>0 )
      buf.setCharAt( buf.length()-1, ']' );
    else
      buf.append( ']' );

    res.setIntHeader( "X-Documents-Returned", no );

    out_str( req, buf.toString(), "application/json" );

  } 
}
