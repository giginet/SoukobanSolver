/**
 * 
 */
package tests;

import java.awt.Point;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import map.*;
import util.*;

/**
 * Map用のテストケースクラスです
 * @author giginet
 *
 */
public class MapTest{

  Map map = null;
  static final String testMap = 
      "#########\n" +
      "#.......#\n" +
      "#.##*##.#\n" +
      "#.@...*.#\n" +
      "##*GGG#.#\n" +
      "##....#.#\n" +
      "####..###\n" +
      "#########";
  
  @Before
  public void setUp() throws Exception{
    map = Map.parse(MapTest.testMap);
  }

  /**
   * Map#toStringが正常に動作しているかをテストします
   */
  @Test
  public void toStringTest(){
    assertEquals(map.toString(), testMap.toString());
  }
  
  /**
   * 荷物を動かす動作が正常に動いているかをテストします
   */
  @Test
  public void moveLoadTest(){
    Map moved = map.moveLoad(new Point(2, 4), Direction.Up);
    String movedStr = "#########\n" +
                      "#...*...#\n" +
                      "#.##.##.#\n" +
                      "#.@...*.#\n" +
                      "##*GGG#.#\n" +
                      "##....#.#\n" +
                      "####..###\n" +
                      "#########";
    assertEquals(moved.toString(), movedStr);
  }

}
