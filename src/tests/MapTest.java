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
  static final String testMap2 = 
      "#########\n" +
      "#...*...#\n" +
      "#.##.##.#\n" +
      "#.@...*.#\n" +
      "##*GGG#.#\n" +
      "##....#.#\n" +
      "####..###\n" +
      "#########";
  static final String testMap3 = 
      "#########\n" +
      "#.......#\n" +
      "#.##*.#.#\n" +
      "#.@...*.#\n" +
      "##*GGG#.#\n" +
      "##....#.#\n" +
      "####..###\n" +
      "#########";
  static final String testMap4 = 
      "#########\n" +
      "#.......#\n" +
      "#.##*##.#\n" +
      "#@....*.#\n" +
      "##*GGG#.#\n" +
      "##....#.#\n" +
      "####..###\n" +
      "#########";
  static final String testMap5 = 
      "#########\n" +
      "#...*...#\n" +
      "#.##.##.#\n" +
      "#.....*.#\n" +
      "##*aGG#.#\n" +
      "##....#.#\n" +
      "####..###\n" +
      "#########";
  static final String testMap6 = 
      "#########\n" +
      "#.......#\n" +
      "#.##*.#.#\n" +
      "#.....*.#\n" +
      "##@GGG#.#\n" +
      "##*...#.#\n" +
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
   * マップの比較ができているかどうかをテストします
   */
  @Test
  public void equalsTest(){
    Map map2 = Map.parse(MapTest.testMap);
    Map map3 = Map.parse(MapTest.testMap2);
    Map map4 = Map.parse(MapTest.testMap3);
    Map map5 = Map.parse(MapTest.testMap4);
    assertEquals("生成された文字列が同じ", map.toString(), map2.toString());
    assertEquals("マップ自身が同じ", map, map2);
    assertFalse("生成された文字列が違う", map.toString().equals(map3.toString()));
    assertFalse("マップ自身が違う", map.equals(map3));
    assertFalse("生成された文字列が違う", map.toString().equals(map4.toString()));
    assertFalse("マップ自身が違う", map.equals(map4));
    assertFalse("生成された文字列が違う", map.toString().equals(map5.toString()));
    assertFalse("マップ自身が違う", map.equals(map5));
    assertNotSame("オブジェクト自体は違う", map, map2);
}
  /**
   * マップのcloneができているかどうかをテストします
   */
  @Test
  public void cloneTest(){
    try{
      Map map2 = map.deepClone();
      assertEquals("生成された文字列が同じ", map.toString(), map2.toString());
      assertEquals("マップ自身が同じ", map, map2);
      assertNotSame("オブジェクト自体は違う", map, map2);
    }catch(CloneNotSupportedException e){
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * 進行可能かどうかが正しく判定できているかテストします
   */
  @Test
  public void canThroughTest(){
    assertEquals("5, 2は壁である", map.getChipAt(new Point(5, 2)).getClass(), Wall.class);
    assertFalse("5, 2は進行できない", map.getChipAt(new Point(5, 2)).canThrough());
    assertFalse("荷物のあるところへは進行できない", map.canThrough(new Point(4, 2)));
    assertFalse("壁へは進行できない", map.canThrough(new Point(5, 2)));
    assertTrue("何もないところへは進行できる", map.canThrough(new Point(1, 1)));
    assertFalse("マップ外には進行できない", map.canThrough(new Point(-1, 100)));
  }
  
  /**
   * 箱を動かせるかどうかの判定が正常に出来ているかどうかをテストします
   */
  @Test
  public void canMoveLoadTest(){
    assertTrue("上へ動かせる", map.canMove(new Point(4, 2), Direction.Up));
    assertTrue("下へ動かせる", map.canMove(new Point(4, 2), Direction.Down));
    assertFalse("右へ動かせない", map.canMove(new Point(4, 2), Direction.Right));
    assertFalse("左へ動かせない", map.canMove(new Point(4, 2), Direction.Left)); 
  }

  /**
   * キャラクターを動かす動作が正常に動いているかをテストします
   */
  @Test
  public void moveCharaTest(){
    Map moved = map.moveChara(Direction.Left);
    assertEquals("何もないところへ移動できる", moved.toString(), MapTest.testMap4);
    Map map4 = Map.parse(MapTest.testMap4);
    assertEquals("壁へは移動できない", map4.moveChara(Direction.Left).toString(), MapTest.testMap4);  
    Map map3 = Map.parse(MapTest.testMap3);
    assertEquals("荷物も一緒に移動できる", map3.moveChara(Direction.Down).toString(), MapTest.testMap6);  
    Map map5 = Map.parse(MapTest.testMap5);
    assertEquals("荷物を移動できない", map5.moveChara(Direction.Left).toString(), MapTest.testMap5);  
  }
  
  /**
   * 荷物を動かす動作が正常に動いているかをテストします
   */
  @Test
  public void moveLoadTest(){
    Map moved = map.moveLoad(new Point(4, 2), Direction.Up);
    assertEquals("何もないところへ移動できる", moved.toString(), MapTest.testMap2);
  }
}
