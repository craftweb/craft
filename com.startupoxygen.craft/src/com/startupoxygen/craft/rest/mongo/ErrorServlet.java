/*
 * Copyright Andrei Goumilevski
 * This file licensed under GPLv3 for non commercial projects
 * GPLv3 text http://www.gnu.org/licenses/gpl-3.0.html
 * For commercial usage please contact me
 * gmlvsk2@gmail.com
 *
*/

package com.startupoxygen.craft.rest.mongo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("serial")
@WebServlet(name="ErrorServlet")
public class ErrorServlet extends HttpServlet { 

  private static final Logger log = LoggerFactory.getLogger(ErrorServlet.class);

  // --------------------------------
  @Override 
  public void init() throws ServletException{
    log.trace( "inited" );
    super.init();
  }

  // ------------------------------------
  @Override 
  protected void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    // Nothing

  }

} 
