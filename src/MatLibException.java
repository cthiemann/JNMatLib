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

/**
 * Thrown to signal that a native function in <code>libmat</code> or
 * <code>libmx</code> has returned an unexpected result, or if the
 * library to perform the requested function has not been loaded.
 *
 * @see MatLibrary
 * @see MxLibrary
 *
 * @author Christian Thiemann
 * @version 1.0
 */
public class MatLibException extends RuntimeException {
  public MatLibException() { super(); }
  public MatLibException(String message) { super(message); }
  public MatLibException(Throwable cause) { super(cause); }
  public MatLibException(String message, Throwable cause) { super(message, cause); }
}