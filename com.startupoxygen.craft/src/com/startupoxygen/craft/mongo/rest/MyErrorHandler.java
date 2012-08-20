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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.AbstractHttpConnection;
import org.eclipse.jetty.server.Dispatcher;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoException;

public class MyErrorHandler extends ErrorPageErrorHandler {

  private static final Logger log = LoggerFactory.getLogger(MyErrorHandler.class);

  // -------------------------------------------
  public MyErrorHandler(){
    super();
  }

  // -------------------------------------------
  @Override
  public void handle( String target, Request base_request, 
    HttpServletRequest req, HttpServletResponse res ) throws IOException {

    log.trace("handle");

    Class<?> ex_class = (Class<?>)req.getAttribute( Dispatcher.ERROR_EXCEPTION_TYPE );

    if( MyException.class.equals(ex_class) ){

      MyException exc = (MyException)req.getAttribute(Dispatcher.ERROR_EXCEPTION);

      res.setContentType( "application/json;charset=UTF-8" );
      res.setStatus( exc.code );

      Status st = exc.status;
      if( st==null )
	st = Status.FAIL;

      String s = Status.to_json( st );
      PrintWriter w = res.getWriter();
      w.println( s );
      w.flush();
      AbstractHttpConnection.getCurrentConnection().getRequest().setHandled(true);
      return;

    }
    else if( MongoException.Network.class.equals(ex_class) ){

      handle_mongo_error( "Mongodb server down or can not be reached", res );
      return;

    }
    else if( MongoException.CursorNotFound.class.equals(ex_class) ){

      handle_mongo_error( "Mongodb: cursor not found", res );
      return;

    }
    else if( MongoException.DuplicateKey.class.equals(ex_class) ){

      handle_mongo_error( "Mongodb: duplicate key", res );
      return;

    }
    else if( ex_class!=null&&"com.mongodb.CommandResult$CommandFailure".equals(ex_class.getName()) ){

      Exception exc = (Exception)req.getAttribute(Dispatcher.ERROR_EXCEPTION);
      handle_mongo_error( "Mongodb: "+exc.getMessage(), res );
      return;

    }

    super.handle( target, base_request, req, res );


  }

  // -------------------------------------------
  private void handle_mongo_error( String msg, HttpServletResponse res )
    throws IOException {
    res.setContentType( "application/json;charset=UTF-8" );
    res.setStatus( SC_BAD_REQUEST );

    Status st = Status.get( msg );

    String s = Status.to_json( st );
    PrintWriter w = res.getWriter();
    w.println( s );
    w.flush();
    AbstractHttpConnection.getCurrentConnection().getRequest().setHandled(true);
  }

  /*
  @Override
  protected void handleErrorPage( HttpServletRequest request, Writer writer, int code, String message)
    throws IOException {

    log.info("handleErrorPage");

  }
    
  @Override
  protected void writeErrorPage( HttpServletRequest request, Writer writer, 
    int code, String message, boolean showStacks) throws IOException {

    log.info("writeErrorPage");

  }

  @Override
  protected void writeErrorPageHead( HttpServletRequest request, Writer writer, 
    int code, String message) throws IOException {

    log.info("writeErrorPageHead");

  }

  @Override
  protected void writeErrorPageBody( HttpServletRequest request, Writer writer, 
    int code, String message, boolean showStacks) throws IOException {

    log.info("writeErrorPageBody");

  }

  @Override
  protected void writeErrorPageMessage( HttpServletRequest request, Writer writer, 
    int code, String message,String uri) throws IOException {

    log.info("writeErrorPageBody");

  }

  @Override
  protected void writeErrorPageStacks( HttpServletRequest request, Writer writer)
    throws IOException {

    log.info("writeErrorPageBody");

  }


  @Override
  protected void write(Writer writer,String string)
    throws IOException {

    log.info("writeErrorPageBody");

  }
  */

}
