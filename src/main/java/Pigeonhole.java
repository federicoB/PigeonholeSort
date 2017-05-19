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
  private static <Type> void fillTmpArray(ArrayList<?>[] tmpArray, Type[] arrayToSort) {
    for (Type element : arrayToSort) {
      Integer hashCode = element.hashCode();
      if (tmpArray[hashCode] == null) {
        tmpArray[hashCode] = new ArrayList<Type>();
      }
      ArrayList<Type> elementList = (ArrayList<Type>) tmpArray[hashCode];
      elementList.add(element);
    }
  }

  /**
   * Iterate through an array of list and concat them into the array to sort
   *
   * @param tmpArray    LinkedList<Type>[]: the array to iterate
   * @param arrayToSort ype[]: the array to sort
   * @param <Type>      Type of the array, inferred.
   */
  private static <Type> void fillOrderedArray(ArrayList<?>[] tmpArray, Type[] arrayToSort) {
    int i = 0;
    for (Object tmpElement : tmpArray) {
      if (tmpElement != null) {
        ArrayList<Type> element = (ArrayList<Type>) tmpElement;
        int size = element.size();
        System.arraycopy(element.toArray(), 0, arrayToSort, i, size);
        i += size;
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
    ArrayList<?>[] tmpArray = new ArrayList<?>[max + 1];
    Pigeonhole.fillTmpArray(tmpArray, arrayToSort);
    Pigeonhole.fillOrderedArray(tmpArray, arrayToSort);
  }
}