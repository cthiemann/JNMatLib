/*
 * Copyright 2011 Christian Thiemann <christian@spato.net>
 * Developed at Northwestern University <http://rocs.northwestern.edu>
 *
 * This file is part of JNMatLib, a native interface to libmat and libmx.
 *
 * JNMatLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JNMatLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JNMatLib.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.northwestern.rocs.jnmatlib;
import com.sun.jna.*;

/**
 * Driver to the two native library interface classes.  This class will
 * automatically try to load the two Mathworks libraries <code>libmat</code>
 * and <code>libmx</code> on start-up.  If this fails, {@link #getError} can
 * be used to obtain more information, and {@link #init} can be called to
 * retry loading the libraries.  Use {@link #isLoaded} to verify that the
 * libraries are present before calling any of the native functions.
 *
 * @see MatLibrary
 * @see MxLibrary
 *
 * @author Christian Thiemann
 * @version 1.0
 */
public class JNMatLib {
  
  private static Throwable error = new Error("JNMatLib.init() not called");
  
  /**
   * Loads the two native libraries (<code>libmat</code> and <code>libmx</code>).
   *
   * @return whether the libraries were successfully loaded
   *
   * @see #isLoaded
   * @see #getError
   */
  public static boolean init() {
    if (isLoaded()) return true;
    if (!MatLibrary.init()) { error = MatLibrary.getError(); return false; }
    if (!MxLibrary.init()) { error = MxLibrary.getError(); return false; }
    error = null;
    return isLoaded();
  }

  /**
   * Checks whether both native libraries have been successfully loaded.
   * If either library is not loaded, any calls to the native methods will
   * have very undesired consequences.
   *
   * @return <code>true</code> if both libraries have been successfully loaded
   *
   * @see #init
   * @see #getError
   */
  public static boolean isLoaded() { return error == null; }

  /**
   * Gets the <code>Throwable</code> explaining why the native libraries are not loaded.
   * This can be simply stating that {@link #init} has not been called,
   * or an error thrown by {@link Native#register(String)}.
   *
   * @return the error, or <code>null</code> if the libraries have been successfully loaded
   *
   * @see #init
   * @see #isLoaded
   */
  public static Throwable getError() { return error; }

  // Automatically initialize on start-up; if this fails, the application
  // may try to fix things and call init() again.
  static {
    init();
  }
  
}
