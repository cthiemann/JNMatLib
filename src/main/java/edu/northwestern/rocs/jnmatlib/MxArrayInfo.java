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
 * This class is used to provide metadata on MATLAB variables.
 * It provides an object-oriented interface to <code>struct MxArray</code>
 * defined in Mathworks' MX Array Library and relies on the native library
 * <code>libmx</code> being loaded.
 * <p>
 * In the native libraries, variables and variable metadata are both represented
 * by a <code>struct MxArray</code>, and certain restrictions apply if such
 * <code>struct MxArray</code> is metadata and not real data. These restrictions
 * are properly implemented by the two classes <code>MxArrayInfo</code> and
 * {@link MxArray}.
 * <p>
 * Instances of this class cannot be directly created, but are returned by
 * {@link MatFile#getVariableInfo}.
 * <p>
 * All methods may throw a {@link MatLibException} if the native library
 * has not been loaded or any of the native functions returns unexpected output.
 *
 * @see MxArray
 * @see MatFile#getVariableInfo
 *
 * @author Christian Thiemann
 * @version 1.0
 */
public class MxArrayInfo extends PointerType {

  // enum mxClassID
  public static final int UNKNOWN_CLASS = 0;
  public static final int CELL_CLASS = 1;
  public static final int STRUCT_CLASS = 2;
  public static final int LOGICAL_CLASS = 3;
  public static final int CHAR_CLASS = 4;
  public static final int VOID_CLASS = 5;
  public static final int DOUBLE_CLASS = 6;
  public static final int SINGLE_CLASS = 7;
  public static final int INT8_CLASS = 8;
  public static final int UINT8_CLASS = 9;
  public static final int INT16_CLASS = 10;
  public static final int UINT16_CLASS = 11;
  public static final int INT32_CLASS = 12;
  public static final int UINT32_CLASS = 13;
  public static final int INT64_CLASS = 14;
  public static final int UINT64_CLASS = 15;
  public static final int FUNCTION_CLASS = 16;
  
  /** Names of the classes identified by the XXX_CLASS constants */
  public static final String classnames[] = {
    "unknown", "cell", "struct", "logical", "char", "void", "double", "single",
    "int8", "uint8", "int16", "uint16", "int32", "uint32", "int64", "uint64",
    "function_handle"
  };
  
  /** Java types of the classes identified by the XXX_CLASS constants */
  public static final Class classtypes[] = {
    null, null, null, Boolean.TYPE, Character.TYPE, null, Double.TYPE, Float.TYPE,
    Byte.TYPE, Short.TYPE, Short.TYPE, Integer.TYPE, Integer.TYPE, Long.TYPE, null,
    null
  };
  
  /** True if the native counter-part has to be destroyed by us. */
  private boolean doomed;

  /**
   * Asserts the variable's underlying data type. Every <code>MxArray</code>
   * represents a multi-dimensional array of values of some type.
   *
   * @param classname name of class to test for
   * @return <code>true</code> if the values in this array are of the specified type
   * @see #classnames
   * @see #isCell
   * @see #isChar
   * @see #isStruct
   * @see #isLogical
   * @see #isNumeric
   * @see #isDouble
   * @see #isSingle
   * @see #isInt8
   * @see #isUint8
   * @see #isInt16
   * @see #isUint16
   * @see #isInt32
   * @see #isUint32
   * @see #isInt64
   * @see #isUint64
   */
  public boolean isClass(String classname) {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsClass(this, classname);
  }
  
  /** @see #isClass */
  public boolean isCell() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsCell(this);
  }
  
  /** @see #isClass */
  public boolean isChar() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsChar(this);
  }
  
  /** @see #isClass */
  public boolean isStruct() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsStruct(this);
  }
  
  /** @see #isClass */
  public boolean isLogical() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsLogical(this);
  }
  
  /** @see #isClass */
  public boolean isNumeric() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsNumeric(this);
  }
  
  /** @see #isClass */
  public boolean isDouble() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsDouble(this);
  }
  
  /** @see #isClass */
  public boolean isSingle() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsSingle(this);
  }
  
  /** @see #isClass */
  public boolean isInt8() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsInt8(this);
  }
  
  /** @see #isClass */
  public boolean isUint8() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsUint8(this);
  }
  
  /** @see #isClass */
  public boolean isInt16() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsInt16(this);
  }
  
  /** @see #isClass */
  public boolean isUint16() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsUint16(this);
  }
  
  /** @see #isClass */
  public boolean isInt32() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsInt32(this);
  }
  
  /** @see #isClass */
  public boolean isUint32() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsUint32(this);
  }
  
  /** @see #isClass */
  public boolean isInt64() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsInt64(this);
  }
  
  /** @see #isClass */
  public boolean isUint64() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsUint64(this);
  }
  

  /** Returns <code>true</code> if the underlying data is stored as a sparse matrix. */
  public boolean isSparse() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsSparse(this);
  }
  
  /** Returns <code>true</code> if the underlying data are complex numbers. */
  public boolean isComplex() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsComplex(this);
  }
  

  /** Returns <code>true</code> if the variable lives in the global workspace. */
  public boolean isFromGlobalWS() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsFromGlobalWS(this);
  }
  

  /** Returns <code>true</code> if variable data is exactly one logical (<code>boolean</code>). */
  public boolean isLogicalScalar() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsLogicalScalar(this);
  }
  

  /**
   * Get class ID of this variable's underlying data type.
   *
   * @return one of the XXX_CLASS constants
   * @see #classnames
   * @see #getClassName
   * @see #isClass
   */
  public int getClassID() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetClassID(this);
  }
  
  /**
   * Get class name of this variable's underlying data type.
   *
   * @return the name of the data class
   * @see #isClass
   */
  public String getClassName() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetClassName(this);
  }
  

  /** 
   * Get this variable's number of dimensions. Every MATLAB variable is
   * a multi-dimensional array of values. The number of dimensions
   * is at least two.
   *
   * @return number of dimensions
   * @see #getDimensions
   * @see #getM
   * @see #getN
   */
  public int getDimensionCount() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetNumberOfDimensions(this).intValue();
  }
  
  /** 
   * Get this variable's dimensions. Every MATLAB variable is
   * a multi-dimensional array of values. The number of dimensions
   * is at least two. If this variable is a scalar value, this
   * function will return <code>int[] { 1, 1 }</code>.
   *
   * @return array of dimensions
   * @see #getDimensionCount
   * @see #getM
   * @see #getN
   */
  public int[] getDimensions() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetDimensions(this).intValues(getDimensionCount());
  }
  

  /** 
   * Get number of rows. Every MATLAB variable is
   * a multi-dimensional array of values. The number of dimensions
   * is at least two, and this function returns the first dimension.
   *
   * @return number of rows in this array
   * @see #getDimensionCount
   * @see #getDimensions
   * @see #getN
   */
  public long getM() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetM(this).longValue();
  }
  
  /** 
   * Get number of columns. Every MATLAB variable is
   * a multi-dimensional array of values. The number of dimensions
   * is at least two, and this function returns the second dimension.
   *
   * @return number of columns in this array
   * @see #getDimensionCount
   * @see #getDimensions
   * @see #getM
   */
  public long getN() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetN(this).longValue();
  }
  

  /** Returns the size of this variable's underlying data elements (in bytes per element). */
  public long getElementSize() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetElementSize(this).longValue();
  }
  
  /** Returns the number of values in this variable. */
  public long getNumberOfElements() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetNumberOfElements(this).longValue();
  }
  

  public void destroy() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    MxLibrary.mxDestroyArray(this);
  }
  


  /** Returns the number of fields this variable has (assuming it's of type <code>struct</code>). */
  public int getNumberOfFields() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    if (!isStruct())
      throw new MatLibException("variable is not a struct");
    return MxLibrary.mxGetNumberOfFields(this);
  }
  
  /**
   * Get name of field by field's index.
   *
   * @param index index of the field
   * @return field's name
   */
  public String getFieldName(int index) {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    if (!isStruct())
      throw new MatLibException("variable is not a struct");
    if ((index < 0) && (index >= getNumberOfFields()))
      throw new ArrayIndexOutOfBoundsException(index);
    return MxLibrary.mxGetFieldNameByNumber(this, index);
  }
  
  /**
   * Get index of field by field's name.
   *
   * @param name name of the field
   * @return field's index, or <code>-1</code> on failure
   */
  public int getFieldIndex(String name) {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    if (!isStruct())
      throw new MatLibException("variable is not a struct");
    return MxLibrary.mxGetFieldNumber(this, name);
  }

  
  /**
   * Overrides {@link Object#finalize} in order to call {@link MxLibrary#mxDestroyArray}
   * as required for <code>struct MxArray</code>s returned by {@link MatLibrary#matGetVariableInfo}.
   */
  protected void finalize() {
    if (MxLibrary.isLoaded() && doomed)
      MxLibrary.mxDestroyArray(this);
  }

}
