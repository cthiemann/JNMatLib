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

import edu.northwestern.rocs.jnmatlib.*;
import com.sun.jna.*;
import com.sun.jna.ptr.*;
import java.lang.reflect.Array;

public class JNMatLibExample {

  public static void InspectMxArray(MxArray vMxArray) {
    if (vMxArray == null) {
      System.out.println("Found MxArray is NULL");
    } else {
      String vArrayClass = vMxArray.getClassName();
      int vArrayDims = vMxArray.getDimensionCount();
      long vArrayM = vMxArray.getM();
      long vArrayN = vMxArray.getN();

      if (vMxArray.isStruct()) {
        int vNumFields = vMxArray.getNumberOfFields();
        System.out.print("Found MxArray, struct " + vArrayDims + "-dim with " + vNumFields + " fields (");
        for (int vFieldIdx = 0; vFieldIdx < vNumFields; ++vFieldIdx) {
          System.out.print("'" + vMxArray.getFieldName(vFieldIdx) + "'");
          if (vFieldIdx < vNumFields-1) System.out.print(", ");
        }
        System.out.println("), size (" + vArrayM + " x " + vArrayN + ").");
      } else {
        System.out.println("Found MxArray, class " + vArrayClass + ", " + vArrayDims + "-dim, size (" + vArrayM + " x " + vArrayN + ").");
      }
    }
  }

  public static void testOpenMatFile(String vFilename) {
    if (!JNMatLib.isLoaded()) {
      System.err.println("Error opening Library: " + JNMatLib.getError());
      return;
    }

    MatFile vMatFile = new MatFile(vFilename);
    System.out.println("Successfully opened " + vFilename);
    String[] vVariableNames = vMatFile.getVariableNames();
    for (int vVariableIdx = 0; vVariableIdx < vVariableNames.length; ++vVariableIdx) {
      System.out.println("Found variable '" + vVariableNames[vVariableIdx] + "'");
    }

    if (vVariableNames.length != 1) {
      System.err.println("The file has more than one variable, loading is not supported");
      return;
    }
    String vVariableName = vVariableNames[0];

    MxArray vMatVariable = vMatFile.readVariable(vVariableName);
    vMatFile.close();

    if (vMatVariable == null) {
      System.err.println("The variable " + vVariableName + " failed to load.");
      return;
    }
    System.out.println("Successfully loaded " + vVariableName);
    InspectMxArray(vMatVariable);

    vMatVariable = vMatVariable.getField("Measurements");
    InspectMxArray(vMatVariable);

    //vMatVariable = vMatVariable.getField("Image");
    vMatVariable = vMatVariable.getField("Nuclei");
    InspectMxArray(vMatVariable);

    //vMatVariable = vMatVariable.getField("FileName_OrigBlue");
    vMatVariable = vMatVariable.getField("Intensity_MeanIntensity_RescaledBlue");
    InspectMxArray(vMatVariable);


    MxArray vCellEntry = vMatVariable.getCell(0,0);
    InspectMxArray(vCellEntry);
    double[] vCellDoubleData = vCellEntry.doubleVector();

    for (int vCellIdx = 0; vCellIdx < vCellDoubleData.length; ++vCellIdx) {
      System.out.println(vCellIdx + ": " + vCellDoubleData[vCellIdx]);
    }
  }

  public static void main(String[] args) {
    //testOpenMatFile("Image.FileName_OrigBlue.mat");
    testOpenMatFile("Nuclei.Intensity_MeanIntensity_RescaledBlue.mat");
  }

}
