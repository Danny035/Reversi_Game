import org.junit.Assert;
import org.junit.Test;

import cs3500.reversi.utils.Utils;

/**
 * Test class for testing methods in Utils class.
 */
public class UtilsTests {
  @Test
  public void testCheckValuesInBetween() {
    // check true cases for 0, positive, and negative numbers
    Assert.assertTrue(Utils.checkValuesInBetween(0, 0, 1));
    Assert.assertTrue(Utils.checkValuesInBetween(0, -1, 0));
    Assert.assertTrue(Utils.checkValuesInBetween(0, -1, 1));
    Assert.assertTrue(Utils.checkValuesInBetween(0, 0, 0));

    Assert.assertTrue(Utils.checkValuesInBetween(1, 1, 2));
    Assert.assertTrue(Utils.checkValuesInBetween(1, 0, 1));
    Assert.assertTrue(Utils.checkValuesInBetween(1, 0, 2));
    Assert.assertTrue(Utils.checkValuesInBetween(1, 1, 1));

    Assert.assertTrue(Utils.checkValuesInBetween(-1, -1, 0));
    Assert.assertTrue(Utils.checkValuesInBetween(-1, -2, -1));
    Assert.assertTrue(Utils.checkValuesInBetween(-1, -2, 0));
    Assert.assertTrue(Utils.checkValuesInBetween(-1, -1, -1));

    // check false cases for 0, positive, and negative numbers
    Assert.assertFalse(Utils.checkValuesInBetween(0, -2, -1));
    Assert.assertFalse(Utils.checkValuesInBetween(0, 1, 2));
    Assert.assertFalse(Utils.checkValuesInBetween(1, -1, 0));
    Assert.assertFalse(Utils.checkValuesInBetween(1, 2, 3));
    Assert.assertFalse(Utils.checkValuesInBetween(-1, 0, 1));
    Assert.assertFalse(Utils.checkValuesInBetween(-1, -3, -2));

  }
}
