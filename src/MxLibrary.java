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
 * Interface to Mathworks' MX Matrix Library.
 * The native methods in this class are intentionally undocumented because
 * I don't want to run into copyright issues with Mathworks.  Also, the
 * {@link MxArray} class provides a much more object-oriented API to the
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
public class MxLibrary {
  
  private static Throwable error = new Error("MxLibrary.init() not called");
  
  /**
   * Loads the native library (<code>libmx</code>).
   *
   * @return whether the library was successfully loaded
   *
   * @see #isLoaded
   * @see #getError
   */
  public static boolean init() {
    if (isLoaded()) return true;  // nothing to do here
    try { Native.register("mx"); error = null; } catch (Throwable t) { error = t; }
    return isLoaded();
  }

  /**
   * Checks whether the native library (<code>libmx</code>) has been successfully loaded.
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

  
  // typedef size_t mwIndex -> NativeLong
  // typedef ptrdiff_t mwSignedIndex -> NativeLong
  // typedef size_t mxSize -> NativeLong
  public static native int mxAddField(MxArray pm, String fieldname);
  public static native String mxArrayToString(MxArray array_ptr);  // FIXME: multibyte characters? returns char*
  //public static native void mxAssert(int expr, String error_message);
  //public static native void mxAssertS(int expr, String error_message);
  private static native NativeLong mxCalcSingleSubscript_730(MxArrayInfo pm, NativeLong nsubs, NativeLongArray subs);
    public static NativeLong mxCalcSingleSubscript(MxArrayInfo pm, NativeLong nsubs, NativeLongArray subs) {
      return mxCalcSingleSubscript_730(pm, nsubs, subs); }
  public static native Pointer mxCalloc(NativeLong n, NativeLong size);
  // typedef char16_t mxChar -> FIXME: ???
  private static native MxArray mxCreateCellArray_730(NativeLong ndim, NativeLongArray dims);
    public static MxArray mxCreateCellArray(NativeLong ndim, NativeLongArray dims) {
      return mxCreateCellArray_730(ndim, dims); }
  private static native MxArray mxCreateCellMatrix_730(NativeLong m, NativeLong n);
    public static MxArray mxCreateCellMatrix(NativeLong m, NativeLong n) {
      return mxCreateCellMatrix_730(m, n); }
  private static native MxArray mxCreateCharArray_730(NativeLong ndim, NativeLongArray dims);
    public static MxArray mxCreateCharArray(NativeLong ndim, NativeLongArray dims) {
      return mxCreateCharArray_730(ndim, dims); }
  private static native MxArray mxCreateCharMatrixFromStrings_730(NativeLong m, StringArray str);
    public static MxArray mxCreateCharMatrixFromStrings(NativeLong m, StringArray str) {
      return mxCreateCharMatrixFromStrings_730(m, str); }
  private static native MxArray mxCreateDoubleMatrix_730(NativeLong m, NativeLong n, int ComplexFlag);
    public static MxArray mxCreateDoubleMatrix(NativeLong m, NativeLong n, int ComplexFlag) {
      return mxCreateDoubleMatrix_730(m, n, ComplexFlag); }
  public static native MxArray mxCreateDoubleScalar(double value);
  private static native MxArray mxCreateLogicalArray_730(NativeLong ndim, NativeLongArray dims);
    public static MxArray mxCreateLogicalArray(NativeLong ndim, NativeLongArray dims) {
      return mxCreateLogicalArray_730(ndim, dims); }
  private static native MxArray mxCreateLogicalMatrix_730(NativeLong m, NativeLong n);
    public static MxArray mxCreateLogicalMatrix(NativeLong m, NativeLong n) {
      return mxCreateLogicalMatrix_730(m, n); }
  public static native MxArray mxCreateLogicalScalar(boolean value);
  private static native MxArray mxCreateNumericArray_730(NativeLong ndim, NativeLongArray dims, int classid, int ComplexFlag);
    public static MxArray mxCreateNumericArray(NativeLong ndim, NativeLongArray dims, int classid, int ComplexFlag) {
      return mxCreateNumericArray_730(ndim, dims, classid, ComplexFlag); }
  private static native MxArray mxCreateNumericMatrix_730(NativeLong m, NativeLong n, int classid, int ComplexFlag);
    public static MxArray mxCreateNumericMatrix(NativeLong m, NativeLong n, int classid, int ComplexFlag) {
      return mxCreateNumericMatrix_730(m, n, classid, ComplexFlag); }
  private static native MxArray mxCreateSparse_730(NativeLong m, NativeLong n, NativeLong nzmax, int ComplexFlag);
    public static MxArray mxCreateSparse(NativeLong m, NativeLong n, NativeLong nzmax, int ComplexFlag) {
      return mxCreateSparse_730(m, n, nzmax, ComplexFlag); }
  private static native MxArray mxCreateSparseLogicalMatrix_730(NativeLong m, NativeLong n, NativeLong nzmax);
    public static MxArray mxCreateSparseLogicalMatrix(NativeLong m, NativeLong n, NativeLong nzmax) {
      return mxCreateSparseLogicalMatrix_730(m, n, nzmax); }
  public static native MxArray mxCreateString(String str);
  private static native MxArray mxCreateStructArray_730(NativeLong ndim, NativeLongArray dims, int nfields, StringArray fieldnames);
    public static MxArray mxCreateStructArray(NativeLong ndim, NativeLongArray dims, int nfields, StringArray fieldnames) {
      return mxCreateStructArray_730(ndim, dims, nfields, fieldnames); }
  private static native MxArray mxCreateStructMatrix_730(NativeLong m, NativeLong n, int nfields, StringArray fieldnames);
    public static MxArray mxCreateStructMatrix(NativeLong m, NativeLong n, int nfields, StringArray fieldnames) {
      return mxCreateStructMatrix_730(m, n, nfields, fieldnames); }
  public static native void mxDestroyArray(MxArrayInfo pm);
  public static native MxArray mxDuplicateArray(MxArray pm);
  public static native void mxFree(Pointer ptr);
  private static native MxArray mxGetCell_730(MxArray pm, NativeLong index);
    public static MxArray mxGetCell(MxArray pm, NativeLong index) {
      return mxGetCell_730(pm, index); }
  public static native String mxGetChars(MxArray array_ptr);  // FIXME: multibyte? returns mxChar*
  public static native int mxGetClassID(MxArrayInfo pm);
  public static native String mxGetClassName(MxArrayInfo pm);
  public static native Pointer mxGetData(MxArray pm);
  private static native NativeLongArray mxGetDimensions_730(MxArrayInfo pm);
    public static NativeLongArray mxGetDimensions(MxArrayInfo pm) {
      return mxGetDimensions_730(pm); }
  public static native NativeLong mxGetElementSize(MxArrayInfo pm);
  public static native double mxGetEps();
  private static native MxArray mxGetField_730(MxArray pm, NativeLong index, String fieldname);
    public static MxArray mxGetField(MxArray pm, NativeLong index, String fieldname) {
      return mxGetField_730(pm, index, fieldname); }
  private static native MxArray mxGetFieldByNumber_730(MxArray pm, NativeLong index, int fieldnumber);
    public static MxArray mxGetFieldByNumber(MxArray pm, NativeLong index, int fieldnumber) {
      return mxGetFieldByNumber_730(pm, index, fieldnumber); }
  public static native String mxGetFieldNameByNumber(MxArrayInfo pm, int fieldnumber);
  public static native int mxGetFieldNumber(MxArrayInfo pm, String fieldname);
  public static native Pointer mxGetImagData(MxArray pm);
  public static native double mxGetInf();
  private static native NativeLongArray mxGetIr_730(MxArray pm);  // returns mwIndex*
    public static NativeLongArray mxGetIr(MxArray pm) {
      return mxGetIr_730(pm); }
  private static native NativeLongArray mxGetJc_730(MxArray pm);  // returns mwIndex*
    public static NativeLongArray mxGetJc(MxArray pm) {
      return mxGetJc_730(pm); }
  public static native Pointer mxGetLogicals(MxArray array_ptr);  // returns mxLogical*
  public static native NativeLong mxGetM(MxArrayInfo pm);
  public static native NativeLong mxGetN(MxArrayInfo pm);
  public static native double mxGetNaN();
  private static native NativeLong mxGetNumberOfDimensions_730(MxArrayInfo pm);
    public static NativeLong mxGetNumberOfDimensions(MxArrayInfo pm) {
      return mxGetNumberOfDimensions_730(pm); }
  public static native NativeLong mxGetNumberOfElements(MxArrayInfo pm);
  public static native int mxGetNumberOfFields(MxArrayInfo pm);
  private static native NativeLong mxGetNzmax_730(MxArray pm);
    public static NativeLong mxGetNzmax(MxArray pm) {
      return mxGetNzmax_730(pm); }
  public static native Pointer mxGetPi(MxArray pm);  // returns double*
  public static native Pointer mxGetPr(MxArray pm);  // returns double*
  private static native Pointer mxGetProperty_730(MxArray pm, NativeLong index, String propname);  // returns mxArray*
    public static Pointer mxGetProperty(MxArray pm, NativeLong index, String propname) {
      return mxGetProperty_730(pm, index, propname); }
  public static native double mxGetScalar(MxArray pm);
  private static native int mxGetString_730(MxArray pm, byte str[], NativeLong strlen);  // FIXME: correct? char* str
    public static int mxGetString(MxArray pm, byte str[], NativeLong strlen) {
      return mxGetString_730(pm, str, strlen); }
  public static native boolean mxIsCell(MxArrayInfo pm);
  public static native boolean mxIsChar(MxArrayInfo pm);
  public static native boolean mxIsClass(MxArrayInfo pm, String classname);
  public static native boolean mxIsComplex(MxArrayInfo pm);
  public static native boolean mxIsDouble(MxArrayInfo pm);
  public static native boolean mxIsEmpty(MxArray pm);
  public static native boolean mxIsFinite(double value);
  public static native boolean mxIsFromGlobalWS(MxArrayInfo pm);
  public static native boolean mxIsInf(double value);
  public static native boolean mxIsInt16(MxArrayInfo pm);
  public static native boolean mxIsInt32(MxArrayInfo pm);
  public static native boolean mxIsInt64(MxArrayInfo pm);
  public static native boolean mxIsInt8(MxArrayInfo pm);
  public static native boolean mxIsLogical(MxArrayInfo pm);
  public static native boolean mxIsLogicalScalar(MxArrayInfo array_ptr);
  public static native boolean mxIsLogicalScalarTrue(MxArray array_ptr);
  public static native boolean mxIsNaN(double value);
  public static native boolean mxIsNumeric(MxArrayInfo pm);
  public static native boolean mxIsSingle(MxArrayInfo pm);
  public static native boolean mxIsSparse(MxArrayInfo pm);
  public static native boolean mxIsStruct(MxArrayInfo pm);
  public static native boolean mxIsUint16(MxArrayInfo pm);
  public static native boolean mxIsUint32(MxArrayInfo pm);
  public static native boolean mxIsUint64(MxArrayInfo pm);
  public static native boolean mxIsUint8(MxArrayInfo pm);
  // typedef bool mxLogical -> boolean
  public static native Pointer mxMalloc(NativeLong n);
  public static native Pointer mxRealloc(Pointer ptr, NativeLong size);
  public static native void mxRemoveField(MxArray pm, int fieldnumber);
  private static native void mxSetCell_730(MxArray pm, NativeLong index, MxArray value);
    public static void mxSetCell(MxArray pm, NativeLong index, MxArray value) {
      mxSetCell_730(pm, index, value); }
  public static native int mxSetClassName(MxArray array_ptr, String classname);
  public static native void mxSetData(MxArray pm, Pointer ptr);
  private static native int mxSetDimensions_730(MxArray pm, NativeLongArray dims, NativeLong ndim);
    public static int mxSetDimensions(MxArray pm, NativeLongArray dims, NativeLong ndim) {
      return mxSetDimensions_730(pm, dims, ndim); }
  private static native void mxSetField_730(MxArray pm, NativeLong index, String fieldname, MxArray pvalue);
    public static void mxSetField(MxArray pm, NativeLong index, String fieldname, MxArray pvalue) {
      mxSetField_730(pm, index, fieldname, pvalue); }
  private static native void mxSetFieldByNumber_730(MxArray pm, NativeLong index, int fieldnumber, MxArray pvalue);
    public static void mxSetFieldByNumber(MxArray pm, NativeLong index, int fieldnumber, MxArray pvalue) {
      mxSetFieldByNumber_730(pm, index, fieldnumber, pvalue); }
  public static native void mxSetImagData(MxArray pm, Pointer pi);
  private static native void mxSetIr_730(MxArray pm, Pointer ir);
    public static void mxSetIr(MxArray pm, Pointer ir) {
      mxSetIr_730(pm, ir); }
  private static native void mxSetJc_730(MxArray pm, Pointer jc);
    public static void mxSetJc(MxArray pm, Pointer jc) {
      mxSetJc_730(pm, jc); }
  private static native void mxSetM_730(MxArray pm, NativeLong m);
    public static void mxSetM(MxArray pm, NativeLong m) {
      mxSetM_730(pm, m); }
  private static native void mxSetN_730(MxArray pm, NativeLong n);
    public static void mxSetN(MxArray pm, NativeLong n) {
      mxSetN_730(pm, n); }
  private static native void mxSetNzmax_730(MxArray pm, NativeLong nzmax);
    public static void mxSetNzmax(MxArray pm, NativeLong nzmax) {
      mxSetNzmax_730(pm, nzmax); }
  public static native void mxSetPi(MxArray pm, double pi[]);  // FIXME: pi should be a pointer obtained by mxCalloc... same for ir, jc, etc above)
  public static native void mxSetPr(MxArray pm, double pr[]);  // FIXME: pi should be a pointer obtained by mxCalloc... same for ir, jc, etc above)
  private static native void mxSetProperty_730(MxArray pa, NativeLong index, String propname, MxArray value);
    public static void mxSetProperty(MxArray pa, NativeLong index, String propname, MxArray value) {
      mxSetProperty_730(pa, index, propname, value); }
  
}
