/*
 * Copyright Andrei Goumilevski
 * This file licensed under GPLv3 for non commercial projects
 * GPLv3 text http://www.gnu.org/licenses/gpl-3.0.html
 * For commercial usage please contact me
 * gmlvsk2@gmail.com
 *
*/

package com.startupoxygen.craft.mongo.rest;

import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

import com.google.gson.Gson;

public class Status{

  private static Gson gson = new Gson();

  public  final static Status OK = new Status( "success" );
  public  final static Status FAIL = new Status( "fail" );
  String status;

  static private final Map<String,Status> _cache = Collections.synchronizedMap(
      new HashMap<String,Status>() );

  public static Status get( String status ){
    Status st = _cache.get( status );
    if( st!=null )
      return st;
    st = new Status( status );
    _cache.put( status, st );
    return st;
  }

  private Status( String status ){
    this.status = status;
  }

  public static String to_json( Status st ){
    return gson.toJson( st );
  }

}
