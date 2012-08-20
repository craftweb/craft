/*
 * Copyright Andrei Goumilevski
 * This file licensed under GPLv3 for non commercial projects
 * GPLv3 text http://www.gnu.org/licenses/gpl-3.0.html
 * For commercial usage please contact me
 * gmlvsk2@gmail.com
 *
*/

package com.startupoxygen.craft.rest.mongo;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class OutputFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(OutputFilter.class);

  public static final int EMPTY = 0;
  public static final int STR = 1;
  public static final int JSON = 2;
  public static final int XML = 3;

  private Gson gson = new Gson();

  // --------------------------------
  public void init( FilterConfig config ){
    log.trace( "start OutputFilter" );
  }

  // --------------------------------
  public void doFilter( ServletRequest _req, ServletResponse _res, FilterChain chain )
    throws ServletException, IOException {

    chain.doFilter( _req, _res );

    log.trace( "doFilter()" );

    HttpServletRequest req = (HttpServletRequest)_req;
    HttpServletResponse res = (HttpServletResponse)_res;

    res.setHeader( "Cache-Control", "no-cache,no-store,must-revalidate" );

    final Integer what = (Integer)req.getAttribute( "what" );
    if( what==null ){
      return;
    }

    switch( what ){

      case EMPTY:{
        res.setStatus( SC_OK );
      }
      break;

      case STR:{
	String content_type = (String)req.getAttribute( "type" );
	if( content_type!=null )
	  res.setContentType( content_type+";charset=UTF-8" );
	else
	  res.setContentType( "html/text;charset=UTF-8" );
        res.setStatus( SC_OK );
	Object o = req.getAttribute( "value" );
	if( o!=null ){
	  PrintWriter w = res.getWriter();
	  w.println( o );
	  w.flush();
	}
      }
      break;

      case JSON:{
	res.setContentType( "application/json;charset=UTF-8" );
        res.setStatus( SC_OK );
	Object o = req.getAttribute( "value" );
	if( o!=null ){
	  String s = gson.toJson( o );
	  PrintWriter w = res.getWriter();
	  w.println( s );
	  w.flush();
	}
      }
      break;

      case XML:{
	res.setContentType( "application/xml;charset=UTF-8" );
        res.setStatus( SC_OK );
      }
      break;

    }

  }

  // --------------------------------
  public void destroy(){
    log.trace( "stop OutputFilter" );
  }

}
