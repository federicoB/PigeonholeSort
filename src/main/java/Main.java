/**
 * This file is part of PigeonholeSort
 * <p>
 * Created by Federico Bertani on 07/05/17.
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

import java.util.Arrays;
import java.util.Random;

/**
 * Entry point class.
 * Used for profiling purposes
 * @author Federico Bertani
 */
public class Main {

  private static Integer[] array;

  public static void main(String[] args) {
    array = initializeArray();
    Pigeonhole.sort(array);
    array = initializeArray();
    Arrays.sort(array);
  }

  public static Integer[] initializeArray() {
    Random generator = new Random();
    array = new Integer[1000000];
    for (int i = 0; i < 1000000; i++) {
      array[i] = generator.nextInt(10000000);
    }
    return array;
  }
}
