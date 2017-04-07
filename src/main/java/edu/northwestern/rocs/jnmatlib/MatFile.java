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
import java.io.*;
import com.sun.jna.*;
import com.sun.jna.ptr.*;

/**
 * This class is used to read from or write to a MAT-file.
 * It provides an object-oriented interface to <code>struct MATFile</code>
 * defined in Mathworks' MAT-File Library and relies on the native library
 * <code>libmat</code> being loaded.
 * <p>
 * All methods may throw a {@link MatLibException} if the native library
 * has not been loaded or any of the native functions returns unexpected output.
 *
 * @see MatLibrary
 *
 * @author Christian Thiemann
 * @version 1.0
 */
public class MatFile extends PointerType {
  
  /** Open file for reading only */
  public static final String MODE_READ = "r";
  /** Open file for reading and writing (MAT-file must already exist) */
  public static final String MODE_UPDATE = "u";
  /** Create file for writing (overwriting existing file) */
  public static final String MODE_WRITE = "w";
  /** Create file in HDF5-based format, capable of string objects larger than 2GB */
  public static final String MODE_WRITE73 = "w7.3";
  
  public MatFile() { super(); }
  public MatFile(Pointer p) { super(p); }  
  
  /**
   * Opens an existing MAT-file for reading.
   *
   * @param file path to the MAT-file
   */
  public MatFile(File file) {
    this(file.getAbsolutePath(), MODE_READ);
  }
  
  /**
   * Opens an existing MAT-file for reading.
   *
   * @param filename name of the MAT-file
   */
  public MatFile(String filename) {
    this(filename, MODE_READ);
  }
  
  /**
   * Opens or creates a MAT-file in any access mode.
   *
   * @param file path to the MAT-file
   * @param mode any of the <code>MODE_XXX</code> constants
   */
  public MatFile(File file, String mode) {
    this(file.getAbsolutePath(), mode);
  }

  /**
   * Opens or creates a MAT-file in any access mode.
   *
   * @param filename name of the MAT-file
   * @param mode any of the <code>MODE_XXX</code> constants
   */
  public MatFile(String filename, String mode) {
    if (filename == null)
      throw new NullPointerException("filename cannot be null");
    if (mode == null)
      throw new NullPointerException("mode cannot be null");
    if (filename.isEmpty())
      throw new NullPointerException("filename cannot be empty");
    if (!MatLibrary.isLoaded())
      throw new MatLibException("MatLibrary (libmat) not loaded", MatLibrary.getError());
    MatFile mf = MatLibrary.matOpen(filename, mode);
    if (mf == null)
      throw new MatLibException("matOpen returned null for file '" + filename + "' with mode '" + mode + "'");
    setPointer(mf.getPointer());
  }
  
  /**
   * Return the number of variables stored in this MAT-file.
   *
   * @return number of variables
   * @see #getVariableNames
   */
  public int getVariableCount() {
    return getVariableNames().length;
  }

  /**
   * Lists the names of all variables in this MAT-file.
   *
   * @return the variable names, or an empty array if the file is empty
   */
  public String[] getVariableNames() {
    if (!MatLibrary.isLoaded())
      throw new MatLibException("MatLibrary (libmat) not loaded", MatLibrary.getError());
    IntByReference num = new IntByReference(0);
    Pointer p = MatLibrary.matGetDir(this, num);
    if (num.getValue() < 0)
      throw new MatLibException("matGetDir reported " + num.getValue() + " variables");
    if (num.getValue() == 0)
      return new String[0];
    if (p == null)
      throw new MatLibException("matGetDir reported " + num.getValue() + " variables, but returned a null pointer");
    return p.getStringArray(0, num.getValue());
  }
  
  /**
   * Provides information on a variable in this MAT-file.
   *
   * @return the variable metadata
   */
  public MxArrayInfo getVariableInfo(String name) {
    if (!MatLibrary.isLoaded())
      throw new MatLibException("MatLibrary (libmat) not loaded", MatLibrary.getError());
    MxArrayInfo res = MatLibrary.matGetVariableInfo(this, name);
    if (res == null)
      throw new MatLibException("matGetVariableInfo returned null; maybe '" + name + "' is not a variable?");
    return res;
  }
  
  /**
   * Reads a variable from the MAT-file.
   *
   * @param name name of the variable
   * @return the variable, represented as an {@link MxArray}
   */
  public MxArray readVariable(String name) {
    if (!MatLibrary.isLoaded())
      throw new MatLibException("MatLibrary (libmat) not loaded", MatLibrary.getError());
    MxArray res = MatLibrary.matGetVariable(this, name);
    if (res == null)
      throw new MatLibException("matGetVariable returned null; maybe '" + name + "' is not a variable?");
    return res;
  }
  
  /**
   * Writes a variable to the MAT-file. If a variable with the same name already
   * exists it will be replaced.
   *
   * @param name name of the variable
   * @param pm the variable, represented as an {@link MxArray}
   */
  public void writeVariable(String name, MxArray pm) { writeVariable(name, pm, false); }
  
  /**
   * Write a variable to the MAT-file. If a variable with the same name already
   * exists it will be replaced.
   *
   * @param name name of the variable
   * @param pm the variable, represented as an {@link MxArray}
   * @param asGlobal if <code>true</code>, uses {@link MatLibrary#matPutVariableAsGlobal}
   *   instead of {@link MatLibrary#matPutVariable} to store the variable
   */
  public void writeVariable(String name, MxArray pm, boolean asGlobal) {
    if (!MatLibrary.isLoaded())
      throw new MatLibException("MatLibrary (libmat) not loaded", MatLibrary.getError());
    if ((asGlobal ? MatLibrary.matPutVariableAsGlobal(this, name, pm) : MatLibrary.matPutVariable(this, name, pm)) != 0)
      throw new MatLibException("matPutVariable return non-zero value (name = \"" + name + "\")");
  }
  
  /**
   * Delete a variable from the MAT-file.
   *
   * @param name name of variable
   */
  public void deleteVariable(String name) {
    if (!MatLibrary.isLoaded())
      throw new MatLibException("MatLibrary (libmat) not loaded", MatLibrary.getError());
    if (MatLibrary.matDeleteVariable(this, name) != 0)
      throw new MatLibException("matDeleteVariable returned non-zero value (name = \"" + name + "\")");
  }
  
  /**
   * Closes the MAT-file.
   */
  public void close() {
    if (MatLibrary.matClose(this) != 0)
      throw new MatLibException("matClose returned non-zero value");
  }
  
  // FIXME: This finalize() gets repeatedly called when the Java
  // code is repeatedly executed. This can cause crashes in
  // MatLibrary.matClose() which seems to crash in libmat.so. If
  // the user carefully closes their Matlab file, we should not
  // have problems even without this finalize. What to do?
  public void finalize() {
    try {
      close();
      super.finalize();
    } catch (Throwable t) {
      // never mind...
    }
  }
  
}