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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;

@SuppressWarnings("serial")
@WebServlet(name="GridfsServlet")
public class GridfsServlet extends SkeletonMongodbServlet {
  
  private static final Logger log = LoggerFactory.getLogger(GridfsServlet.class);

  private ThreadLocal<StringBuilder> tl = new ThreadLocal<StringBuilder>(){
    @Override 
    protected synchronized StringBuilder initialValue(){
      return new StringBuilder( 1024*4 );
    }
  };

  private Map<String,GridFS> fs_cache = Collections.synchronizedMap( new HashMap<String,GridFS>() );

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
    if( fs_cache!=null )
      fs_cache.clear();

  }

  // POST
  // ------------------------------------
  @Override 
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "doPost()" );

    if( !can_write(req) ){
      res.sendError( SC_UNAUTHORIZED );
      return;
    }

    InputStream tmp = req.getInputStream();
    InputStream is = new BufferedInputStream( tmp );
    String db_name = req.getParameter( "dbname" );
    String bucket_name = req.getParameter( "bucketname" );
    if( db_name==null || bucket_name==null ){
      String names[]  = req2mongonames( req );
      if( names!=null ){
	db_name = names[0];
	bucket_name = names[1];
      }
      if( db_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }
    }

    if( bucket_name==null )
      bucket_name = "fs";

    String file_name = req.getParameter( "filename" );

    if( file_name==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }

    DB db = mongo.getDB( db_name );

    String fs_cache_key = db_name + bucket_name;
    GridFS fs = fs_cache.get( fs_cache_key );
    if( fs==null ){
      fs = new GridFS( db, bucket_name );
      fs_cache.put( fs_cache_key, fs );
    }

    GridFSDBFile db_file_old = fs.findOne( file_name ); 
    if( db_file_old==null ){
      error( res, SC_NOT_FOUND, Status.get("file doe not exists, use PUT") );
      return;
    }

    String ct = req.getContentType();
    GridFSInputFile db_file = fs.createFile( file_name );
    if( ct!=null )
      db_file.setContentType( ct );
    OutputStream os = db_file.getOutputStream();

    final int len = 4096;
    byte data[] = new byte[len];
    int n = 0, bytesno = 0;
    while( (n=is.read(data, 0, len))>0 ){
      os.write( data, 0, n );
      bytesno += n;
    }
    os.close();

    if( is!=null )
      is.close();

    out_json( req, Status.OK );

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

    InputStream tmp = req.getInputStream();
    InputStream is = new BufferedInputStream( tmp );
    String db_name = req.getParameter( "dbname" );
    String bucket_name = req.getParameter( "bucketname" );
    if( db_name==null || bucket_name==null ){
      String names[]  = req2mongonames( req );
      if( names!=null ){
	db_name = names[0];
	bucket_name = names[1];
      }
      if( db_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }
    }

    if( bucket_name==null )
      bucket_name = "fs";

    String file_name = req.getParameter( "filename" );

    if( file_name==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }

    DB db = mongo.getDB( db_name );

    String fs_cache_key = db_name + bucket_name;
    GridFS fs = fs_cache.get( fs_cache_key );
    if( fs==null ){
      fs = new GridFS( db, bucket_name );
      fs_cache.put( fs_cache_key, fs );
    }

    GridFSDBFile db_file_old = fs.findOne( file_name ); 
    if( db_file_old!=null ){
      error( res, SC_BAD_REQUEST, Status.get("file already exists, use POST") );
      return;
    }

    String ct = req.getContentType();
    GridFSInputFile db_file = fs.createFile( file_name );
    if( ct!=null )
      db_file.setContentType( ct );
    OutputStream os = db_file.getOutputStream();

    final int len = 4096;
    byte data[] = new byte[len];
    int n = 0, bytesno = 0;
    while( (n=is.read(data, 0, len))>0 ){
      os.write( data, 0, n );
      bytesno += n;
    }
    os.flush();
    os.close();

    if( is!=null )
      is.close();

    out_json( req, Status.OK );

  }

  // DELETE
  // ------------------------------------
  @Override 
  protected void doDelete(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    log.trace( "doDelete()" );

    if( !can_write(req) ){
      res.sendError( SC_UNAUTHORIZED );
      return;
    }

    String db_name = req.getParameter( "dbname" );
    String bucket_name = req.getParameter( "bucketname" );
    if( db_name==null || bucket_name==null ){
      String names[]  = req2mongonames( req );
      if( names!=null ){
	db_name = names[0];
	bucket_name = names[1];
      }
      if( db_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }
    }

    if( bucket_name==null )
      bucket_name = "fs";

    DB db = mongo.getDB( db_name );

    String fs_cache_key = db_name + bucket_name;
    GridFS fs = fs_cache.get( fs_cache_key );
    if( fs==null ){
      fs = new GridFS( db, bucket_name );
      fs_cache.put( fs_cache_key, fs );
    }

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

    String file_name = req.getParameter( "filename" );

    if( file_name==null ){
      error( res, SC_BAD_REQUEST, Status.get("param name missing") );
      return;
    }

    fs.remove( file_name );

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

    String db_name = req.getParameter( "dbname" );
    String bucket_name = req.getParameter( "bucketname" );
    if( db_name==null || bucket_name==null ){
      String names[]  = req2mongonames( req );
      if( names!=null ){
	db_name = names[0];
	bucket_name = names[1];
      }
      if( db_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }
    }

    if( bucket_name==null )
      bucket_name = "fs";

    DB db = mongo.getDB( db_name );

    String fs_cache_key = db_name + bucket_name;
    GridFS fs = fs_cache.get( fs_cache_key );
    if( fs==null ){
      fs = new GridFS( db, bucket_name );
      fs_cache.put( fs_cache_key, fs );
    }

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

    String op = req.getParameter( "op" );
    if( op==null )
      op = "get";

    StringBuilder buf = tl.get();
    // reset buf
    buf.setLength( 0 );

    // list
    if( "get".equals(op) ){

      String file_name = req.getParameter( "filename" );
      if( file_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }

      GridFSDBFile db_file = fs.findOne( file_name ); 
      if( db_file==null ){
	error( res, SC_NOT_FOUND, Status.get("file does not exists") );
	return;
      }

      res.setContentLength( (int)db_file.getLength() );
      String ct = db_file.getContentType();
      if( ct!=null )
	res.setContentType( ct );
      OutputStream os = res.getOutputStream();
      long l;
      while( (l=db_file.writeTo( os ))>0 );
      os.flush();
      os.close();

    }
    // list
    else if( "list".equals(op) ){

      DBCursor c = fs.getFileList();
      if( c==null ){
	error( res, SC_NOT_FOUND, Status.get("no documents found") );
	return;
      }

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

      out_str( req, buf.toString(), "application/json" );

    }
    // info
    else if( "info".equals(op) ){

      String file_name = req.getParameter( "filename" );
      if( file_name==null ){
	error( res, SC_BAD_REQUEST, Status.get("param name missing") );
	return;
      }

      GridFSDBFile db_file = fs.findOne( file_name ); 
      if( db_file==null ){
	error( res, SC_NOT_FOUND, Status.get("no documents found") );
	return;
      }

      buf.append( "{" );
      buf.append( String.format("\"ContentType\":%s,", db_file.getContentType()) );
      buf.append( String.format("\"Length\":%d,", db_file.getLength()) );
      buf.append( String.format("\"MD5\":%s", db_file.getMD5()) );
      buf.append( "}" );

      out_str( req, buf.toString(), "application/json" );

    }
    else
      res.sendError(  SC_BAD_REQUEST );

  } 

}
