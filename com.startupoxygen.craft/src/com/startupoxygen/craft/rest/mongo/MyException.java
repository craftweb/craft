/*
 * Copyright Andrei Goumilevski
 * This file licensed under GPLv3 for non commercial projects
 * GPLv3 text http://www.gnu.org/licenses/gpl-3.0.html
 * For commercial usage please contact me
 * gmlvsk2@gmail.com
 *
*/

package com.startupoxygen.craft.rest.mongo;

@SuppressWarnings("serial")
public class MyException extends RuntimeException {

  public  int code;
  public  Status status;

  public MyException( int code, Status status ){
    this( code );
    this.status = status;
  }

  public MyException( int code ){
    this.code = code;
  }

}
