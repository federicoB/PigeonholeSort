/**
 * This file is part of PigeonholeSort
 * <p>
 * Created by Federico Bertani on 06/05/17.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Static class containing sort method using pigeonhole algorithm.
 * @see <a href="https://en.wikipedia.org/wiki/Pigeonhole_sort">Wikipedia page for pigeonhole sort</a>
 * @author Federico Bertani
 */
public abstract class Pigeonhole {

  /**
   * Get maximum hashCode value of a given generic array
   * @param array Type[]: the array to calculate the maximum
   * @param <Type> Type of the array, inferred.
   * @return
   */
  private static <Type> Integer getMax(Type[] array) {
    int max = 0;
    for (Type element : array) {
      if (element.hashCode() > max) {
        max = element.hashCode();
      }
    }
    return max;
  }

  /**
   * Fill a temporary array at position corresponding to values taken from array to sort.
   * Every position of the array has a linked list for handling multiple equal value.
   * @param tmpArray LinkedList<Type>[]: the array to fill
   * @param arrayToSort Type[]: the array to sort
   * @param <Type> Type of the array, inferred.
   */
  private static <Type> void fillTmpArray(LinkedList<?>[] tmpArray, Type[] arrayToSort) {
    for (Type element : arrayToSort) {
      Integer hashCode = element.hashCode();
      if (tmpArray[hashCode] == null) {
        tmpArray[hashCode] = new LinkedList<Type>();
      }
      LinkedList<Type> elementList = (LinkedList<Type>) tmpArray[hashCode];
      elementList.add(element);
    }
  }

  /**
   * Iterate through an array of list and concatenate them.
   * @param tmpArray LinkedList<Type>[]: the array to iterate
   * @param finalList ArrayList<Type>: the result of the concatenation
   * @param <Type> Type of the array, inferred.
   */
  private static <Type> void createFinalList(LinkedList<?>[] tmpArray, ArrayList<Type> finalList) {
    int length = tmpArray.length;
    for (int i = 0; i <= length; i++) {
      LinkedList<Type> element = (LinkedList<Type>) tmpArray[i];
      if (element != null) {
        finalList.addAll(element);
      }
    }
  }

  /**
   * Sort given array using pigeonhole sort algorithm.
   * @see <a href="https://en.wikipedia.org/wiki/Pigeonhole_sort">Wikipedia page for pigeonhole sort</a>
   * @param arrayToSort Type[]: the array to sort.
   * @param <Type> Type of the array, inferred.
   */
  public static <Type> void sort(Type[] arrayToSort) {
    int max = Pigeonhole.getMax(arrayToSort);
    LinkedList<?>[] tmpArray = new LinkedList<?>[max + 1];
    Pigeonhole.fillTmpArray(tmpArray, arrayToSort);
    int length = arrayToSort.length;
    ArrayList<Type> finalList = new ArrayList<Type>(length);
    Pigeonhole.createFinalList(tmpArray, finalList);
    Type[] finalArray = (Type[]) finalList.toArray();
    System.arraycopy(finalArray, 0, arrayToSort, 0, length);
  }
}