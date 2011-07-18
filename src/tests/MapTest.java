/**
 * 
 */
package tests;

import java.awt.Point;
import java.util.ArrayList;

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

  MapState map = null;
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
  static final String testMap7 = 
      "#########\n" +
      "#.......#\n" +
      "#.##*.#.#\n" +
      "#....@*G#\n" +
      "##..GG#.#\n" +
      "##*...#.#\n" +
      "####..###\n" +
      "#########";
  static final String testMap8 = 
      "#########\n" +
      "#.......#\n" +
      "#.##*.#.#\n" +
      "#.....@+#\n" +
      "##..GG#.#\n" +
      "##*...#.#\n" +
      "####..###\n" +
      "#########";
  static final String testMap9 = 
      "#########\n" +
      "#.......#\n" +
      "#.##.##.#\n" +
      "#.@.....#\n" +
      "##.+++#.#\n" +
      "##....#.#\n" +
      "####..###\n" +
      "#########";
  
  
  @Before
  public void setUp() throws Exception{
    map = MapState.parse(MapTest.testMap);
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
    MapState map2 = MapState.parse(MapTest.testMap);
    MapState map3 = MapState.parse(MapTest.testMap2);
    MapState map4 = MapState.parse(MapTest.testMap3);
    MapState map5 = MapState.parse(MapTest.testMap4);
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
    MapState map2 = map.deepClone();
    assertEquals("生成された文字列が同じ", map.toString(), map2.toString());
    assertEquals("マップ自身が同じ", map, map2);
    assertNotSame("オブジェクト自体は違う", map, map2);
  }
  
  /**
   * 進行可能かどうかが正しく判定できているかテストします
   */
  @Test
  public void canThroughTest(){
    assertEquals("5, 2は壁である", map.getMap().getChipAt(new Point(5, 2)).getClass(), Wall.class);
    assertFalse("5, 2は進行できない", map.getMap().getChipAt(new Point(5, 2)).canThrough());
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
    assertTrue("上へ動かせる", map.canMove(new Point(4, 2), Direction.North));
    assertTrue("下へ動かせる", map.canMove(new Point(4, 2), Direction.South));
    assertFalse("右へ動かせない", map.canMove(new Point(4, 2), Direction.East));
    assertFalse("左へ動かせない", map.canMove(new Point(4, 2), Direction.West)); 
  }

  /**
   * キャラクターを動かす動作が正常に動いているかをテストします
   */
  @Test
  public void moveCharaTest(){
    MapState moved = map.moveChara(Direction.West);
    assertEquals("何もないところへ移動できる", moved.toString(), MapTest.testMap4);
    MapState map4 = MapState.parse(MapTest.testMap4);
    assertEquals("壁へは移動できない", map4.moveChara(Direction.West).toString(), MapTest.testMap4);  
    MapState map3 = MapState.parse(MapTest.testMap3);
    assertEquals("荷物も一緒に移動できる", map3.moveChara(Direction.South).toString(), MapTest.testMap6);  
    MapState map5 = MapState.parse(MapTest.testMap5);
    assertEquals("荷物を移動できない", map5.moveChara(Direction.West).toString(), MapTest.testMap5); 
    MapState map7 = MapState.parse(MapTest.testMap7);
    assertEquals("荷物を移動できる", map7.moveChara(Direction.East).toString(), MapTest.testMap8); 
  }
  
  /**
   * キャラクターを動かせるかどうかの判定をテストします
   */
  @Test
  public void canMoveCharaTest(){
    assertFalse("上に移動できない", map.canMoveChara(Direction.North));
    assertTrue("右に移動できる", map.canMoveChara(Direction.East));
    assertTrue("下に移動できる", map.canMoveChara(Direction.South));
    assertTrue("左に移動できる", map.canMoveChara(Direction.West));
    assertFalse("左上に移動できない", map.canMoveChara(Direction.NorthWest));
  }
  
  /**
   * 荷物を動かす動作が正常に動いているかをテストします
   */
  @Test
  public void moveLoadTest(){
    MapState moved = map.moveLoad(new Point(4, 2), Direction.North);
    assertEquals("何もないところへ移動できる", moved.toString(), MapTest.testMap2);
  }
  
  /**
   * 探索終了状態かテストします
   */
  @Test
  public void isGoalTest(){
    MapState map9 = MapState.parse(MapTest.testMap9);
    assertFalse("ゴールではない", map.isGoal());
    assertTrue("ゴールである", map9.isGoal());
  }

  /**
   * 含まれているかのテスト
   */
  @Test
  public void hashSetContainsTest(){
    ArrayList<MapState> set = new ArrayList<MapState>();
    MapState map2 = MapState.parse(MapTest.testMap);
    set.add(map);
    assertEquals("大きさが1", set.size(), 1);
    assertTrue("含まれている", set.contains(map2));
  }
  
}
