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

import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import edu.northwestern.rocs.jnmatlib.*;
import java.lang.reflect.Array;

public class JNMatLibTest {
  
  @Test public void testLibrariesPresent() throws Throwable {
    if (!JNMatLib.isLoaded())
      throw JNMatLib.getError();
  }
  
  @Test public void testOpenMatFile() {
    MatFile mf = new MatFile("test1.mat");
    mf.close();
  }
  
  @Test public void testElementCount() {
    MatFile mf = new MatFile("test1.mat");
    assumeNotNull(mf);
    for (String var : mf.getVariableNames()) {
      MxArray arr = mf.readVariable(var);
      assumeNotNull("variable `" + var + "':", arr);
      if (arr.isSparse()) continue;
      assertEquals("elements in `" + var + "':", Array.getLength(arr.getData()), arr.getNumberOfElements());
    }
    mf.close();
  }
  
  public void testAsFloat1(MatFile mf, String name, float expected) {
    assertNotNull(mf);
    MxArray arr = mf.readVariable(name);
    assertNotNull(arr);
    float data[] = arr.floatValues();
    assertNotNull(data);
    float sum = 0; for (float val : data) sum += val;
    assertEquals("float values of `" + name + "':", expected, sum, 1e-7);
  }
  
  public void testAsFloat2(MatFile mf, String name, float expected[]) {
    assertNotNull(mf);
    MxArray arr = mf.readVariable(name);
    assertNotNull(arr);
    float data[][] = arr.floatMatrix();
    assertNotNull(data);
    assertEquals("row number mismatch:", expected.length, data.length);
    for (int j = 0; j < expected.length; j++) {
      float sum = 0; for (float val : data[j]) sum += val;
      assertEquals("float values of `" + name + "', row " + j + ":", expected[j], sum, 1e-7);
    }
  }
  
  @Test public void testVectors() {
    MatFile mf = new MatFile("test1.mat");
    testAsFloat1(mf, "double1a", 0);
    testAsFloat1(mf, "double1b", 0);
    testAsFloat1(mf, "single1a", 0);
    testAsFloat1(mf, "single1b", 0);
    testAsFloat1(mf, "logical1a", 2);
    testAsFloat1(mf, "logical1b", 2);
    mf.close();
  }
  
  @Test public void testMatrices() {
    MatFile mf = new MatFile("test1.mat");
    testAsFloat1(mf, "double2", 0);
    testAsFloat2(mf, "double2", new float[] { -.9f, 0, .9f });
    testAsFloat1(mf, "single2", 0);
    testAsFloat2(mf, "single2", new float[] { -.9f, 0, .9f });
    mf.close();
  }
  
}