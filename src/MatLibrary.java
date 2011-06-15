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
import com.sun.jna.ptr.*;

/**
 * Interface to Mathworks' MAT-File Library.
 * The native methods in this class are intentionally undocumented because
 * I don't want to run into copyright issues with Mathworks.  Also, the
 * {@link MatFile} class provides a much more object-oriented API to the
 * same functionality and should be used instead of calling the native
 * functions directly.  However, the brave may find documentation in
 * the <a href="http://www.mathworks.com/help/techdoc/apiref/bqoqnz0.html">MATLAB C/C++ API Reference</a>.
 * <p>
 * The user will need the (closed-source) native libraries for his/her platform.
 * TODO: Expand documentation on this.
 * <p>
 * TODO: Licensing blabla...
 *
 * @see JNMatLib
 *
 * @author Christian Thiemann
 * @version 1.0
 */
public class MatLibrary {

  private static Throwable error = new Error("MatLibrary.init() has not been called");

  /**
   * Loads the native library (<code>libmat</code>).
   *
   * @return whether the library was successfully loaded
   *
   * @see #isLoaded
   * @see #getError
   */
  public static boolean init() {
    if (isLoaded()) return true;  // duh, nothing to do here...
    try { Native.register("mat"); error = null; } catch (Throwable t) { error = t; }
    return isLoaded();
  }
  
  /**
   * Checks whether the native library (<code>libmat</code>) has been successfully loaded.
   * If the library is not loaded, any calls to the native methods will have very undesired
   * consequences.
   *
   * @return <code>true</code> if the library has been successfully loaded
   *
   * @see #init
   * @see #getError
   */
  public static boolean isLoaded() { return error == null; }

  /**
   * Gets the <code>Throwable</code> explaining why the native library is not loaded.
   * This can be simply stating that {@link #init} has not been called,
   * or an error thrown by {@link Native#register(String)}.
   *
   * @return the error, or <code>null</code> if the library has been successfully loaded
   *
   * @see #init
   * @see #isLoaded
   */
  public static Throwable getError() { return error; }

  
  public static native int matClose(MatFile mfp);
  public static native int matDeleteVariable(MatFile mfp, String name);
  public static native Pointer matGetDir(MatFile mfp, IntByReference num);  // returns char**
  public static native Pointer matGetFp(MatFile mfp);  // returns FILE*
  public static native MxArray matGetNextVariable(MatFile mfp, PointerByReference name);  // const char **name
  public static native MxArray matGetNextVariableInfo(MatFile mfp, PointerByReference name);  // const char **name
  public static native MxArray matGetVariable(MatFile mfp, String name);
  public static native MxArray matGetVariableInfo(MatFile mfp, String name);
  public static native MatFile matOpen(String filename, String mode);
  public static native int matPutVariable(MatFile mfp, String name, MxArray pm);
  public static native int matPutVariableAsGlobal(MatFile mfp, String name, MxArray pm);

}
