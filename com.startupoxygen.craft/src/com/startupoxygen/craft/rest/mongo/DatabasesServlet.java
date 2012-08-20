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
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.util.List;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.BasicBSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;

@SuppressWarnings("serial")
@WebServlet(name="DatabasesServlet")
public class DatabasesServlet extends SkeletonMongodbServlet {
  
  private static final Logger log = LoggerFactory.getLogger(DatabasesServlet.class);

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

    mongo.dropDatabase( db_name );
    out_json( req, Status.OK );

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

    String op = req.getParameter( "op" );
    if( op==null )
      op = "list";

    if( "list".equals(op) ){
      List<String> dbs = mongo.getDatabaseNames();
      out_json( req, dbs );
      return;
    }

    // requires dbname
    String db_name = req.getParameter( "dbname" );
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
    DB db = mongo.getDB( db_name );

    if( "stats".equals(op) ){
      BasicBSONObject o = db.getStats();
      out_str( req, o.toString(), "application/json" );
    }
    else if( "lasterror".equals(op) ){
      BasicBSONObject o = db.getLastError();
      out_str( req, o.toString(), "application/json" );
    }
    else if( "setreadonly".equals(op) ){
      if( !can_write(req) ){
	res.sendError( SC_UNAUTHORIZED );
	return;
      }
      String tmp = req.getParameter( "readonly" );
      if( tmp==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }
      boolean read_only = Boolean.parseBoolean( tmp );
      db.setReadOnly( read_only );
      out_json( req, Status.OK );
    }
    else
      res.sendError(  SC_BAD_REQUEST );

  } 


}
