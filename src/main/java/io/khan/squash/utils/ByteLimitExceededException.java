package io.khan.squash.utils;

//
//ByteLimitExceededException.java
//
//Author: Walter Brameld
//Date: 3/6/2001
//
//Purpose: A subclass of IOException thrown when the file size upload limit
//       is exceeded.
//
//Copyright 2001 Walter Brameld
//




import java.io.*;


/**
* A subclass of <TT>IOException</TT> thrown when the file size upload limit
* is exceeded.
*/
public class ByteLimitExceededException extends IOException {

 /**
  * Creates a <TT>ByteLimitExceededException</TT> with no detail message.
  */
 public ByteLimitExceededException() {
 }  // end constructor


 /**
  * Creates a <TT>ByteLimitExceededException</TT> with the specified detail
  * message.
  */
 public ByteLimitExceededException( String message ) {
     super( message );
 }  // end constructor

}  // end class ByteLimitExceededException