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
import java.lang.reflect.Array;
 
/**
 * This class is used to manipulate MATLAB variables.
 * It provides an object-oriented interface to <code>struct MxArray</code>
 * defined in Mathworks' MX Array Library and relies on the native library
 * <code>libmx</code> being loaded.
 * <p>
 * The native libraries use <code>struct MxArray</code> for representing both
 * actual variable data and variable metadata. In this Java interface, the
 * variable metadata is represented by a {@link MxArrayInfo} instance, while
 * this class extends on <code>MxArrayInfo</code> to provide data retrieval
 * and manipulation methods.
 * <p>
 * All methods may throw a {@link MatLibException} if the native library
 * has not been loaded or any of the native functions returns unexpected output.
 *
 * @see MxLibrary
 *
 * @author Christian Thiemann
 * @version 1.0
 */
public class MxArray extends MxArrayInfo {

  // FIXME: implement methods to create new MxArrays and write data to them

  /**
   * Tests whether all of the variable's dimensions are zero.
   *
   * @return <code>true</code> if the variable contains no data
   */
  public boolean isEmpty() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxIsEmpty(this);
  }
  
  /**
   * Retrieves scalar boolean value of this variable.
   *
   * @return boolean value of this variable's only value
   * @throws MatLibException if variable is not a logical scalar
   */
  public boolean isLogicalScalarTrue() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    if (!isLogicalScalar())
      throw new MatLibException("not a logical scalar");
    return MxLibrary.mxIsLogicalScalarTrue(this);
  }
  
  public Number getValueAt(int... subs) {
    long index = MxLibrary.mxCalcSingleSubscript(this, new NativeLong(subs.length), new NativeLongArray(subs)).longValue();
    if (MxLibrary.mxIsLogical(this)) {
      return MxLibrary.mxGetLogicals(this).getByte(index*1);
    }
    Pointer p = MxLibrary.mxGetData(this);
    switch (MxLibrary.mxGetClassID(this)) {
      case LOGICAL_CLASS: return p.getByte(index*1);
      case DOUBLE_CLASS: return p.getDouble(index*8);
      case SINGLE_CLASS: return p.getFloat(index*4);
      case INT8_CLASS: return p.getByte(index*1);
      case INT16_CLASS: return p.getShort(index*2);
      case INT32_CLASS: return p.getInt(index*4);
      case INT64_CLASS: return p.getLong(index*8);
      case UINT8_CLASS: return 0xFF & (short)p.getByte(index*1);
      case UINT16_CLASS: return 0xFFFF & (int)p.getByte(index*1);
      case UINT32_CLASS: return 0xFFFFFFFF & (long)p.getByte(index*1);
      case UINT64_CLASS:
        throw new MatLibException("cannot return uint64 as unsigned Java value");
      default:
        throw new MatLibException("cannot handle data class " + getClassID() + " (" + getClassName() + ")");
    }
  }
  
  public boolean booleanValue() { return booleanValue(0); }
  public boolean booleanValue(int... subs) { return getValueAt(subs).byteValue() != 0; }
  public byte byteValue() { return byteValue(0); }
  public byte byteValue(int... subs) { return getValueAt(subs).byteValue(); }
  public short shortValue() { return shortValue(0); }
  public short shortValue(int... subs) { return getValueAt(subs).shortValue(); }
  public int intValue() { return intValue(0); }
  public int intValue(int... subs) { return getValueAt(subs).intValue(); }
  public long longValue() { return longValue(0); }
  public long longValue(int... subs) { return getValueAt(subs).longValue(); }
  public float floatValue() { return floatValue(0); }
  public float floatValue(int... subs) { return getValueAt(subs).floatValue(); }
  public double doubleValue() { return doubleValue(0); }
  public double doubleValue(int... subs) { return getValueAt(subs).doubleValue(); }
  

  /**
   * Retrieves all data values in a Java array. This only works
   * on numeric or logical arrays. If the data contains complex
   * numbers, the real part is returned.
   *
   * The type of the return value depends on the data class:
   * <dl>
   *   <dt>double</dt><dd><code>double[]</code></dd>
   *   <dt>single</dt><dd><code>float[]</code></dd>
   *   <dt>int8</dt><dd><code>byte[]</code></dd>
   *   <dt>int16</dt><dd><code>short[]</code></dd>
   *   <dt>int32</dt><dd><code>int[]</code></dd>
   *   <dt>int64</dt><dd><code>long[]</code></dd>
   *   <dt>uint8</dt><dd><code>short[]</code></dd>
   *   <dt>uint16</dt><dd><code>int[]</code></dd>
   *   <dt>uint32</dt><dd><code>long[]</code></dd>
   *   <dt>logical</dt><dd><code>boolean[]</code></dd>
   *   <dt>char</dt><dd><code>char[]</code></dd>
   * </dl>
   *
   * @return a Java array containing all values
   * @see #isNumeric
   * @see #isLogical
   * @see #getImagData
   * @see java.lang.reflect.Array
   */
  public Object getData() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    if (!isNumeric() && !isLogical())
      throw new MatLibException("not a numeric or logical array");
    if (isSparse())
      throw new MatLibException("getData() cannot handle sparse matrices, use getDataAsMatrix() instead");
    Pointer p = isNumeric() ? MxLibrary.mxGetData(this) : MxLibrary.mxGetLogicals(this);
    if (p == null)
      throw new MatLibException((isNumeric() ? "mxGetData" : "mxGetLogicals") + " returned null");
    if ((getClassID() < 0) || (getClassID() >= classtypes.length))
      throw new MatLibException("illegal data class " + getClassID() + " (" + getClassName() + ")");
    int length = (int)getNumberOfElements();
    if (length < 0)
      throw new MatLibException("number of elements is negative or too large: " + getNumberOfElements());
    return getArrayFromNative(p, length);
  }

  /**
   * Retrieves all imaginary data values in a Java array. This only works
   * on numeric arrays that contain complex values.
   *
   * The type of the return value depends on the data class:
   * <dl>
   *   <dt>double</dt><dd><code>double[]</code></dd>
   *   <dt>single</dt><dd><code>float[]</code></dd>
   *   <dt>int8</dt><dd><code>byte[]</code></dd>
   *   <dt>int16</dt><dd><code>short[]</code></dd>
   *   <dt>int32</dt><dd><code>int[]</code></dd>
   *   <dt>int64</dt><dd><code>long[]</code></dd>
   *   <dt>uint8</dt><dd><code>short[]</code></dd>
   *   <dt>uint16</dt><dd><code>int[]</code></dd>
   *   <dt>uint32</dt><dd><code>long[]</code></dd>
   * </dl>
   *
   * @return a Java array containing all values
   * @see #isNumeric
   * @see #isComplex
   * @see #getData
   * @see java.lang.reflect.Array
   */
  public Object getImagData() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    if (!isNumeric() || !isComplex())
      throw new MatLibException("not a complex numeric array");
    if (isSparse())
      throw new MatLibException("getImagData() cannot handle sparse matrices, use getImagDataAsMatrix() instead");
    Pointer p = MxLibrary.mxGetImagData(this);
    if (p == null)
      throw new MatLibException((isNumeric() ? "mxGetImagData" : "mxGetLogicals") + " returned null");
    if ((getClassID() < 0) || (getClassID() >= classtypes.length))
      throw new MatLibException("illegal data class " + getClassID() + " (" + getClassName() + ")");
    int length = (int)getNumberOfElements();
    if (length < 0)
      throw new MatLibException("number of elements is negative or too large: " + getNumberOfElements());
    return getArrayFromNative(p, length);
  }

  private Object getRawArrayFromNative(Pointer p, int length) {
    switch (MxLibrary.mxGetClassID(this)) {
      case LOGICAL_CLASS: return p.getByteArray(0, length);  // FIXME: is "logical" always 8-bit?
      case CHAR_CLASS: return p.getCharArray(0, length);  // FIXME: does Matlab always use 2-byte chars?
      case DOUBLE_CLASS: return p.getDoubleArray(0, length);
      case SINGLE_CLASS: return p.getFloatArray(0, length);
      case INT8_CLASS: return p.getByteArray(0, length);
      case INT16_CLASS: return p.getShortArray(0, length);
      case INT32_CLASS: return p.getIntArray(0, length);
      case INT64_CLASS: return p.getLongArray(0, length);
      case UINT8_CLASS: return p.getByteArray(0, length);
      case UINT16_CLASS: return p.getShortArray(0, length);
      case UINT32_CLASS: return p.getIntArray(0, length);
      case UINT64_CLASS: return p.getLongArray(0, length);
      default:
        throw new MatLibException("cannot handle data class " + getClassID() + " (" + getClassName() + ")");
    }
  }
  
  private Object getArrayFromNative(Pointer p, int length) {
    switch (MxLibrary.mxGetClassID(this)) {
      case LOGICAL_CLASS:
        boolean bb[] = new boolean[length];
        for (int i = 0; i < length; i++)
          bb[i] = p.getByte(i) != 0;
        return bb;
      case UINT8_CLASS:
        short ss[] = new short[length];
        for (int i = 0; i < length; i++)
          ss[i] = (short)(0xFF & (short)p.getByte(i));
        return ss;
      case UINT16_CLASS:
        int ii[] = new int[length];
        for (int i = 0; i < length; i++)
          ii[i] = 0xFFFF & (int)p.getShort(i*2);
        return ii;
      case UINT32_CLASS:
        long ll[] = new long[length];
        for (int i = 0; i < length; i++)
          ll[i] = 0xFFFFFFFFL & (long)p.getInt(i*4);
        return ll;
      case UINT64_CLASS:
        throw new MatLibException("cannot return uint64 as unsigned Java value");
      default:
        return getRawArrayFromNative(p, length);
    }
  }
  
  /**
   * Returns the values in this array as floats. If this array is of type
   * <code>logical</code>, the returned values are either 1 or 0.
   *
   * @see #getData
   */
  public float[] floatValues() {
    Object data = getData();
    if (data.getClass().getComponentType() == Float.TYPE)
      return (float[])data;
    else {
      float res[] = new float[Array.getLength(data)];
      if (data.getClass().getComponentType() == Boolean.TYPE)
        for (int i = 0; i < res.length; i++)
          res[i] = ((boolean[])data)[i] ? 1 : 0;
      else
        for (int i = 0; i < res.length; i++)
          res[i] = ((Number)Array.get(data, i)).floatValue();  // FIXME: can we prevent the detour over Number?
      return res;
    }
  }
  
  /**
   * Returns the real values stored in this variable as a 2-D array.
   * This only works if the variable is a 2-D array, obviously.
   *
   * @return a Java array which type depends on the variable's data class
   *
   * @see #getData
   * @see java.lang.reflect.Array
   */
  public Object getDataAsMatrix() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    if (getDimensionCount() != 2)
      throw new MatLibException("variable is not a matrix (num dim = " + getDimensionCount() + ")");
    if (!isNumeric() && !isLogical())
      throw new MatLibException("not a numeric or logical array");
    Pointer p = isNumeric() ? MxLibrary.mxGetData(this) : MxLibrary.mxGetLogicals(this);
    if (p == null)
      throw new MatLibException((isNumeric() ? "mxGetData" : "mxGetLogicals") + " returned null");
    return getMatrixFromNative(p);
  }
  
  /**
   * Returns the imaginary values stored in this variable as a 2-D array.
   * This only works if the variable is a complex 2-D array, obviously.
   *
   * @return a Java array which type depends on the variable's data class
   *
   * @see #getData
   * @see java.lang.reflect.Array
   */
  public Object getImagDataAsMatrix() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    if (getDimensionCount() != 2)
      throw new MatLibException("variable is not a matrix (num dim = " + getDimensionCount() + ")");
    if (!isNumeric() || !isComplex())
      throw new MatLibException("not a complex numeric array");
    Pointer p = MxLibrary.mxGetImagData(this);
    if (p == null)
      throw new MatLibException((isNumeric() ? "mxGetData" : "mxGetLogicals") + " returned null");
    return getMatrixFromNative(p);
  }
  
  private Object getMatrixFromNative(Pointer p) {
    int Nr = MxLibrary.mxGetM(this).intValue(), Nc = MxLibrary.mxGetN(this).intValue();
    if (Nr < 0)
      throw new MatLibException("number of rows is negative or too large: " + MxLibrary.mxGetM(this).longValue());
    if (Nc < 0)
      throw new MatLibException("number of columns is negative or too large: " + MxLibrary.mxGetN(this).longValue());
    if (isSparse()) {  // sparse can only be double or logical
      int jc[] = MxLibrary.mxGetJc(this).intValues(Nc + 1);
      int ir[] = MxLibrary.mxGetIr(this).intValues(jc[Nc]);
      int l = 0;  // running index into ir and data vector
      switch (getClassID()) {
        case DOUBLE_CLASS:
          double ddres[][] = new double[Nr][Nc];  // matrix return value
          double dd[] = p.getDoubleArray(0, jc[Nc]);  // data vector
          for (int j = 0; j < Nc; j++)  // current column index
            while (l < jc[j+1])  // ir[l] is current row index
              ddres[ir[l]][j] = dd[l++];
          return ddres;
        case LOGICAL_CLASS:
          boolean bbres[][] = new boolean[Nr][Nc];  // matrix return value
          byte bb[] = p.getByteArray(0, jc[Nc]);  // data vector
          for (int j = 0; j < Nc; j++)  // current column index
            while (l < jc[j+1])  // ir[l] is current row index
              bbres[ir[l]][j] = bb[l++] != 0;
          return bbres;
        default:
          throw new MatLibException("uh, hm... don't know how to handle non-double, non-logical sparse matrices");
      }
    } else {
      Object data = getData();
      Object res = Array.newInstance(data.getClass().getComponentType(), new int[] { Nr, Nc });
      for (int i = 0; i < Nr; i++) {
        Object row = Array.get(res, i);
        for (int j = 0; j < Nc; j++)
          Array.set(row, j, Array.get(data, j*Nc + i));  // FIXME: can we avoid the boxing and unboxing?
      }
      return res;
    }
  }
  
  /**
   * Returns the values in this array as a 2-D array of floats.
   * If this array is of type <code>logical</code>, the returned
   * values are either 1 or 0.
   *
   * @see #doubleMatrix
   */
  public double[] doubleVector() {
    Object vector = getData();
    if (vector.getClass().getComponentType().getComponentType() == Float.TYPE)
      return (double[])vector;
    else {
      int length = (int)getNumberOfElements();
      double res[] = new double[length];
      for (int i = 0; i < length; i++) {
        res[i] = ((Number)Array.get(vector, i)).doubleValue();  // FIXME: can we prevent the detour over Number?
      }
      return res;
    }
  }


  /**
   * Returns the values in this array as a 2-D array of doubles.
   * If this array is of type <code>logical</code>, the returned
   * values are either 1 or 0.
   *
   * @see #getDataAsMatrix
   */
  public double[][] doubleMatrix() {
    Object matrix = getDataAsMatrix();
    if (matrix.getClass().getComponentType().getComponentType() == Double.TYPE)
      return (double[][])matrix;
    else {
      int Nr = MxLibrary.mxGetM(this).intValue(), Nc = MxLibrary.mxGetN(this).intValue();  // FIXME: handle overflow
      double res[][] = new double[Nr][Nc];
      for (int i = 0; i < Nr; i++) {
        Object row = Array.get(matrix, i);
        for (int j = 0; j < Nc; j++)
          res[i][j] = ((Number)Array.get(row, j)).doubleValue();  // FIXME: can we prevent the detour over Number?
      }
      return res;
    }
  }


  /**
   * Returns the values in this array as a 2-D array of floats.
   * If this array is of type <code>logical</code>, the returned
   * values are either 1 or 0.
   *
   * @see #floatMatrix
   */
  public float[] floatVector() {
    Object vector = getData();
    if (vector.getClass().getComponentType().getComponentType() == Float.TYPE)
      return (float[])vector;
    else {
      int length = (int)getNumberOfElements();
      float res[] = new float[length];
      for (int i = 0; i < length; i++) {
        res[i] = ((Number)Array.get(vector, i)).floatValue();  // FIXME: can we prevent the detour over Number?
      }
      return res;
    }
  }


  /**
   * Returns the values in this array as a 2-D array of floats.
   * If this array is of type <code>logical</code>, the returned
   * values are either 1 or 0.
   *
   * @see #getDataAsMatrix
   */
  public float[][] floatMatrix() {
    Object matrix = getDataAsMatrix();
    if (matrix.getClass().getComponentType().getComponentType() == Float.TYPE)
      return (float[][])matrix;
    else {
      int Nr = MxLibrary.mxGetM(this).intValue(), Nc = MxLibrary.mxGetN(this).intValue();  // FIXME: handle overflow
      float res[][] = new float[Nr][Nc];
      for (int i = 0; i < Nr; i++) {
        Object row = Array.get(matrix, i);
        for (int j = 0; j < Nc; j++)
          res[i][j] = ((Number)Array.get(row, j)).floatValue();  // FIXME: can we prevent the detour over Number?
      }
      return res;
    }
  }
  

  public MxArray getCell(int... subs) {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    long index = MxLibrary.mxCalcSingleSubscript(this, new NativeLong(subs.length), new NativeLongArray(subs)).longValue();
    return MxLibrary.mxGetCell(this, new NativeLong(index));
  }

  public MxArray getField(String fieldname) {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetField(this, new NativeLong(0), fieldname);
  }
  public MxArray getField(String fieldname, int... subs) {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    long index = MxLibrary.mxCalcSingleSubscript(this, new NativeLong(subs.length), new NativeLongArray(subs)).longValue();
    return MxLibrary.mxGetField(this, new NativeLong(index), fieldname);
  }
  public MxArray getFieldByNumber(int fieldnumber, int... subs) {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    long index = MxLibrary.mxCalcSingleSubscript(this, new NativeLong(subs.length), new NativeLongArray(subs)).longValue();
    return MxLibrary.mxGetFieldByNumber(this, new NativeLong(index), fieldnumber);
  }

  public String arrayToString() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxArrayToString(this);
  }
  public String getChars() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetChars(this);
  }
  public int getString(byte str[], long strlen) {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetString(this, str, new NativeLong(strlen));
  }

  public double getScalar() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetScalar(this);
  }

  public long getNzmax() {
    if (!MxLibrary.isLoaded())
      throw new MatLibException("MxLibrary (libmx) not loaded", MxLibrary.getError());
    return MxLibrary.mxGetNzmax(this).longValue();
  }
  
  
  /** Overrides {@link MxArrayInfo#finalize} as to not destroy the native
   * <code>struct MxArray</struct> automatically when this instance is gc'd. */
  protected void finalize() { }

}
