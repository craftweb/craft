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
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

@SuppressWarnings("serial")
@WebServlet(name="QueryServlet")
public class DistinctServlet extends SkeletonMongodbServlet {
  
  private final static int MAX_FIELDS_TO_RETURN = 1000;
  private static final Logger log = LoggerFactory.getLogger(DistinctServlet.class);
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
    String key = req.getParameter( "key" );
    if( db_name==null || col_name==null ){
      String names[]  = req2mongonames( req );
      if( names!=null ){
	db_name = names[0];
	col_name = names[1];
      }
      if( db_name==null || col_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }
    }
    if( key==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }

    DB db = mongo.getDB( db_name );
    DBCollection col = db.getCollection( col_name );

    BufferedReader r = null;
    DBObject q = null;
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

    }
    finally{
      if( r!=null )
	r.close();
    }

    List l = col.distinct( key, q );
    if( l==null || l.size()==0 ){
      error( res, SC_NOT_FOUND, Status.get("no documents found") );
      return;
    }

    res.setIntHeader( "X-Documents-Count", l.size() );

    StringBuilder buf = tl.get();
    // reset buf
    buf.setLength( 0 );

    JSON.serialize( l, buf );
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
    String key = req.getParameter( "key" );
    if( db_name==null || col_name==null ){
      String names[]  = req2mongonames( req );
      if( names!=null ){
	db_name = names[0];
	col_name = names[1];
      }
      if( db_name==null || col_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }
    }
    if( key==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }

    DB db = mongo.getDB( db_name );
    DBCollection col = db.getCollection( col_name );

    List l = col.distinct( key );
    if( l==null || l.size()==0 ){
      error( res, SC_NOT_FOUND, Status.get("no documents found") );
      return;
    }

    res.setIntHeader( "X-Documents-Count", l.size() );

    StringBuilder buf = tl.get();
    buf.setLength( 0 );

    JSON.serialize( l, buf );
    out_str( req, buf.toString(), "application/json" );

  } 
}
