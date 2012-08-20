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
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.WriteResult;

@SuppressWarnings("serial")
@WebServlet(name="AdminServlet")
public class AdminServlet extends SkeletonMongodbServlet {
  
  private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);

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
    String user = req.getParameter( "user" );
    if( db_name==null || user==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }

    DB db = mongo.getDB( db_name );
    WriteResult o = db.removeUser( user );

    out_str( req, o.toString(), "application/json" );

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

    String db_name = req.getParameter( "dbname" );
    String user = req.getParameter( "user" );
    String passwd = req.getParameter( "passwd" );
    if( db_name==null || user==null || passwd==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }
    boolean read_only = Boolean.parseBoolean( req.getParameter("readonly") );

    DB db = mongo.getDB( db_name );
    WriteResult o = db.addUser( user, passwd.toCharArray(), read_only );

    out_str( req, o.toString(), "application/json" );

  } 


}
