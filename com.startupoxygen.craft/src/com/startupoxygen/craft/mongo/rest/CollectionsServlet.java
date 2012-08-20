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

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BasicBSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;


@SuppressWarnings("serial")
@WebServlet(name="CollectionsServlet")
public class CollectionsServlet extends SkeletonMongodbServlet {
  
  private static final Logger log = LoggerFactory.getLogger(CollectionsServlet.class);

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

    if( !can_write(req) ){
      res.sendError( SC_UNAUTHORIZED );
      return;
    }

    String db_name = req.getParameter( "dbname" );
    String col_name = req.getParameter( "colname" );
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

    DB db = mongo.getDB( db_name );
    db.createCollection( col_name, null );

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

    String db_name = req.getParameter( "dbname" );
    String col_name = req.getParameter( "colname" );
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

    DB db = mongo.getDB( db_name );
    DBCollection col = db.getCollection( col_name );
    col.drop();
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
    String op = req.getParameter( "op" );
    if( db_name==null ){
      String names[]  = req2mongonames( req );
      if( names!=null ){
	db_name = names[0];
      }
      if( db_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }
    }

    if( op==null )
      op = "list";

    DB db = mongo.getDB( db_name );

    if( "list".equals(op) ){
      Set<String> cols = db.getCollectionNames();
      out_json( req, cols );
      return;
    }

    // requires colname
    String col_name = req.getParameter( "colname" );
    if( col_name==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }
    DBCollection col = db.getCollection( col_name );

    if( "count".equals(op) ){
      out_str( req, "{\"count\":"+col.count()+"}", "application/json" );
    }
    else if( "stats".equals(op) ){
      BasicBSONObject o = col.getStats();
      out_str( req, o.toString(), "application/json" );
      return;
    }
    else
      res.sendError(  SC_BAD_REQUEST );

  } 


}
