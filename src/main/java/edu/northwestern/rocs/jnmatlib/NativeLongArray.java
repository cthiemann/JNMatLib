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
 * Handles wrapping/unwrapping arrays of native <code>long</code>
 * from/to arrays of {@link NativeLong}.
 *
 * @author Christian Thiemann
 * @version 1.0
 */
public class NativeLongArray extends PointerType {
  
  public NativeLongArray() { super(); }
  public NativeLongArray(Pointer p) { super(p); }
  
  /**
   * Allocates new native memory and converts the values in <code>ll</code>
   * to native <code>long</code>s.
   *
   * @param ll the values
   */
  public NativeLongArray(long ll[]) {
    Memory p = new Memory(ll.length*NativeLong.SIZE);
    for (int i = 0; i < ll.length; i++)
      p.setNativeLong(i*NativeLong.SIZE, new NativeLong(ll[i]));
  }
  
  /**
   * Allocates new native memory and converts the values in <code>ii</code>
   * to native <code>long</code>s.
   *
   * @param ii the values
   */
  public NativeLongArray(int ii[]) {
    Memory p = new Memory(ii.length*NativeLong.SIZE);
    for (int i = 0; i < ii.length; i++)
      p.setNativeLong(i*NativeLong.SIZE, new NativeLong(ii[i]));
  }
  
  /**
   * Retrieves the native <code>long</code> values as Java <code>long</code>s.
   * The class does not necessarily know the length of the native array,
   * thus the number of values have to be explicitly stated.
   *
   * @param n number of values to retrieve
   */
  public long[] longValues(int n) { return longValues(n, 0); }
  
  /**
   * Retrieves the native <code>long</code> values as Java <code>long</code>s.
   * The class does not necessarily know the length of the native array,
   * thus the number of values have to be explicitly stated.
   *
   * @param n number of values to retrieve
   * @param off index of first value to retrieve
   */
  public long[] longValues(int n, int off) {
    Pointer p = getPointer();
    long ll[] = new long[n];
    for (int i = 0; i < n; i++)
      ll[i] = p.getNativeLong((off + i)*NativeLong.SIZE).longValue();
    return ll;
  }
  
  /**
   * Retrieves the native <code>long</code> values as Java <code>int</code>s.
   * The class does not necessarily know the length of the native array,
   * thus the number of values have to be explicitly stated.
   *
   * @param n number of values to retrieve
   */
  public int[] intValues(int n) { return intValues(n, 0); }

  /**
   * Retrieves the native <code>long</code> values as Java <code>int</code>s.
   * The class does not necessarily know the length of the native array,
   * thus the number of values have to be explicitly stated.
   *
   * @param n number of values to retrieve
   * @param off index of first value to retrieve
   */
  public int[] intValues(int n, int off) {
    Pointer p = getPointer();
    int ii[] = new int[n];
    for (int i = 0; i < n; i++)
      ii[i] = p.getNativeLong((off + i)*NativeLong.SIZE).intValue();
    return ii;
  }

}
