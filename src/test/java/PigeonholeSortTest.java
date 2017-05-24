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

import static org.junit.Assert.assertTrue;

import algorithm.Main;
import algorithm.PigeonholeSort;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit testing for PigeonHole sort class
 * @author Federico Bertani
 */
public class PigeonholeSortTest {

  private Integer[] array;

  @Before
  public void setUp() throws Exception {
    array = Main.initializeArray();
  }

  /**
   * Test for performance of pigeonhole sort
   * @throws Exception
   */
  @Test
  public void pigeonHoleSort() throws Exception {
    PigeonholeSort.sort(array);
  }

  /**
   * Test for performance comparison of java quicksort
   * @throws Exception
   */
  @Test
  public void quickSort() throws Exception {
    Arrays.sort(array);
  }

  /**
   * Test for performance comparison of parallel merge sort
   * @throws Exception
   */
  @Test
  public void parallelSort() throws Exception {
    Arrays.parallelSort(array);
  }

  /**
   * Test for pigeonhole sort correctness
   * @throws Exception
   */
  @Test
  public void isPigeonSortValid() throws Exception {
    PigeonholeSort.sort(array);
    int length = array.length;
    for (int i = 0; i < length - 1; i++) {
      assertTrue(array[i] <= array[i + 1]);
    }
  }

}