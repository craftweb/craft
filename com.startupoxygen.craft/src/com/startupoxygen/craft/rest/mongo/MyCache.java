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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache extends MemcachedClient {

  private static final Logger log = LoggerFactory.getLogger(MyCache.class);
  private static int expire;

  private static MyCache _memcached_client = null;

  // -------------------------------------------
  private MyCache( ConnectionFactory fact, List<InetSocketAddress> addrs ) 
    throws IOException {

    super( fact, addrs );

  }

  // -------------------------------------------
  public static boolean check_connection( InetSocketAddress adr ){

    Socket sock = null;
    boolean reachable = false;

    try {
      sock = new Socket( adr.getHostName(), adr.getPort() );
      reachable = true;
    }
    catch( UnknownHostException e ){
      log.warn( "Memcached failed:"+e );
    } 
    catch( IOException e ){
      log.warn( "Memcached failed:"+e );
    } 
    finally {            
      if( sock!=null ) try{sock.close();}catch(IOException e){}
    }

    return reachable;

  }

  // -------------------------------------------
  static MyCache get(){

    log.trace( "cache get" );

    if( _memcached_client!=null )
      return _memcached_client;

    // example server1:11211 server2:11211
    List<InetSocketAddress> adrs = AddrUtil.getAddresses( Config.memcached_servers );
    List<InetSocketAddress> myadrs = new ArrayList<InetSocketAddress>();

    for( InetSocketAddress adr:adrs ){

      if( !check_connection(adr) ){
	log.error( "IMPORTANT !!! Could not reach memcache server "+adr );
      }
      else
	myadrs.add( adr );

    }

    if( myadrs.size()==0 ){
      log.error( "No available memcached servers, disabling memcache" );
      _memcached_client = null;
      return null;
    }

    try{
      _memcached_client = new MyCache( new BinaryConnectionFactory(), myadrs );
    }
    catch( IOException e ){
      log.error( "could not init memcached client:"+e );
    }
    if( Config.memcached_expire==0||(Config.memcached_expire>60*60*24*30))
      expire = 60*60*24*30;
    else
      expire = Config.memcached_expire;

    log.info( "Memcached:"+myadrs );

    return _memcached_client;

  }

  // ----------------------------
  public OperationFuture<Boolean> set( String key, Object o ){
    return super.set( key, expire, o );
  }

  // ----------------------------
  @Override
  public void  connectionEstablished( SocketAddress sa, int reconnectCount ){
    log.trace( "connectionEstablished()" );
  }

  // ----------------------------
  @Override
  public void connectionLost( SocketAddress sa ){
    log.trace( "connectionLost()" );
  }


}
