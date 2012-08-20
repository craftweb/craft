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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

@SuppressWarnings("serial")
@WebServlet(name="SkeletonMongodbServlet")
public class WriteServlet extends SkeletonMongodbServlet {
  
  private static final Logger log = LoggerFactory.getLogger(WriteServlet.class);
  private WriteConcern write_concern = MongoDB.write_concern; 
  private boolean do_return = write_concern==WriteConcern.FSYNC_SAFE;
  private boolean do_search = Config.search;

  // --------------------------------
  @Override 
  public void init() throws ServletException{

    super.init();
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

    if( !can_write(req) ){
      res.sendError( SC_UNAUTHORIZED );
      return;
    }

    InputStream is = req.getInputStream();
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

    // search
    if( do_search ){

      DBCursor c = col.find( q );
      long l = c.count();
      String todelete[] = new String[(int)l];
      int n = 0;

      while( c.hasNext() ){

	DBObject o = c.next();
	ObjectId oid = (ObjectId)o.get( "_id" ); 
	String id = oid.toStringMongod();
	todelete[n++] = id;

      }
      c.close();
      search.get_writer().delete( todelete );

    }

    WriteResult wr = col.remove( q, write_concern );

    // return operation status
    if( do_return ){
      out_str( req, wr.toString() );
      if( wr.getError()==null ){
	res.setStatus( SC_BAD_REQUEST );
	return;
      }
    }

    res.setStatus( SC_OK );

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

    InputStream is = req.getInputStream();
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

    boolean upsert = Boolean.parseBoolean( req.getParameter("upsert") );
    boolean multi = Boolean.parseBoolean( req.getParameter("multi") );

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
    DBObject q=null, o=null;
    try{

      r = new BufferedReader( new InputStreamReader(is) ); 
      String q_s = r.readLine();
      if( q_s==null ){
	error( res, SC_BAD_REQUEST, Status.get("no data") );
	return;
      }
      String o_s = r.readLine();
      if( o_s==null ){
	error( res, SC_BAD_REQUEST, Status.get("obj to update missing") );
	return;
      }
      try{
	q = (DBObject)JSON.parse( q_s );
	o = (DBObject)JSON.parse( o_s );
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
    //
    // search
    if( do_search ){

      String fn = col.getFullName();
      DBCursor c = col.find( q );
      int cnt = c.count();
      if( !multi ) c.limit( 1 );
      long l = multi?cnt:1;
      String toupdate[] = new String[(int)l];
      int n = 0;
      boolean insert = false;

      if( upsert && !multi && cnt==0 )
	insert = true;

      while( c.hasNext() ){

	DBObject _o = c.next();
	ObjectId oid = (ObjectId)_o.get( "_id" ); 
	String id = oid.toStringMongod();
	toupdate[n++] = id;

      }
      c.close();

      List<String> flds = Config.search_index_fields.get( fn );
      boolean commit = false;
      Document doc = null;
      Search _writer = search.get_writer();
      if( flds!=null&&flds.size()>0 ){
	doc = new Document();
	try{
	  for( String fld:flds ){
	    String val = (String)o.get( fld );
	    if( val==null )
	      continue;
	    _writer.add_searchable_s( doc, fld, val );
	    commit = true;
	  }
	  if( commit )
	    _writer.commit( doc );
	}
	catch( ClassCastException e ){
	  error( res, SC_BAD_REQUEST, Status.get("searchable fields must be type String") );
	  return;
	}
	catch( CorruptIndexException e ){
	  error( res, SC_BAD_REQUEST, Status.get("Search corrupt index"+e) );
	  return;
	}
      }
      if( commit && insert )
	log.warn( "upsert with search not implemented yet" );
      else
	_writer.update( toupdate, doc );

    }

    WriteResult wr = col.update( q, o, upsert, multi, write_concern );

    // return operation status
    if( do_return ){
      out_str( req, wr.toString() );
      if( wr.getError()==null ){
	res.setStatus( SC_BAD_REQUEST );
	return;
      }
    }

    res.setStatus( SC_CREATED );

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

    InputStream is = req.getInputStream();
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
    ArrayList<DBObject> ar = new ArrayList<DBObject>();
    try{

      r = new BufferedReader( new InputStreamReader(is) ); 
      String data;
      while( (data=r.readLine())!=null ){
	if( data!=null ){
	  DBObject o;
	  try{
	    o = (DBObject)JSON.parse( data );
	    ar.add( o );
	  }
	  catch( JSONParseException e ){
	    error( res, SC_BAD_REQUEST, Status.get("can not parse data") );
	    return;
	  }
	}
      }

    }
    finally{
      if( r!=null )
	r.close();
    }

    if( ar.size()==0 ){
      error( res, SC_BAD_REQUEST, Status.get("can not parse data") );
      return;
    }

    WriteResult wr = col.insert( ar, write_concern );

    // search
    if( do_search ){
      String fn = col.getFullName();
      List<String> flds = Config.search_index_fields.get( fn );
      if( flds!=null&&flds.size()>0 ){
	Search _writer = search.get_writer();
	try{
	  for( DBObject o:ar ){
	    boolean commit = false;
	    Document doc = new Document();
	    for( String fld:flds ){
	      String val = (String)o.get( fld );
	      if( val==null )
		continue;
	      _writer.add_searchable_s( doc, fld, val );
	      commit = true;
	    }
	    if( commit ){
	      ObjectId id = (ObjectId)o.get("_id");
	      String sid = id.toStringMongod();
	      _writer.add_storable( doc, "_id", sid );
	      _writer.add_searchable_s( doc, "_dbid_", fn );
	      _writer.commit( doc );
	    }
	  }
	}
	catch( ClassCastException e ){
	  error( res, SC_BAD_REQUEST, Status.get("searchable fields must be type String") );
	  return;
	}
	catch( CorruptIndexException e ){
	  error( res, SC_BAD_REQUEST, Status.get("Search corrupt index"+e) );
	  return;
	}
      }
    }

    // return operation status
    if( do_return ){
      out_str( req, wr.toString() );
      if( wr.getError()==null ){
	res.setStatus( SC_BAD_REQUEST );
	return;
      }
    }

    res.setStatus( SC_CREATED );

  } 


}
