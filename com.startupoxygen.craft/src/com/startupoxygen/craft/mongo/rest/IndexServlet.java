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
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
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
@WebServlet(name="IndexServlet")
public class IndexServlet extends SkeletonMongodbServlet {
  
  private static final Logger log = LoggerFactory.getLogger(IndexServlet.class);

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

  // PUT
  // ------------------------------------
  @Override 
  protected void doPut(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "doPut()" );

    if( !can_admin(req) ){
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

    BufferedReader r = null;
    String data = null;

    try{

      r = new BufferedReader( new InputStreamReader(is) ); 
      data = r.readLine();

    }
    finally{
      if( r!=null )
	r.close();
    }
    if( data==null ){
      error( res, SC_BAD_REQUEST, Status.get("no data") );
      return;
    }

    DBObject o = null;
    try{
      o = (DBObject)JSON.parse( data );
    }
    catch( JSONParseException e ){
      error( res, SC_BAD_REQUEST, Status.get("can not parse data") );
      return;
    }

    col.createIndex( o );

    res.setStatus( SC_CREATED );

  } 

  // DELETE
  // ------------------------------------
  @Override 
  protected void doDelete(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "doDelete()" );

    if( !can_admin(req) ){
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

    BufferedReader r = null;
    String data = null;

    try{

      r = new BufferedReader( new InputStreamReader(is) ); 
      data = r.readLine();

    }
    finally{
      if( r!=null )
	r.close();
    }
    if( data==null ){
      error( res, SC_BAD_REQUEST, Status.get("no data") );
      return;
    }

    DBObject o = null;
    try{
      o = (DBObject)JSON.parse( data );
    }
    catch( JSONParseException e ){
      error( res, SC_BAD_REQUEST, Status.get("can not parse data") );
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

    col.dropIndex( o );

    res.setStatus( SC_OK );

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

    List<DBObject> info = col.getIndexInfo();
    out_json( req, info );

  } 

}
