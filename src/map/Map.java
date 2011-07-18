/**
 * Map.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;

import java.util.*;
import java.awt.*;
import util.Direction;

/**
 * @author giginet
 *
 */
public class Map{
  
  private HashSet<Point> goals = null;
  private int height = 0;
  private HashMap<Point, Chip> map = null;
  private int width = 0;
  
  /**
   * コンストラクタ。大きさ0の空のマップを生成します
   */
  public Map(){
    map = new HashMap<Point, Chip>();
  }
  
  /**
   * コンストラクタ。渡されたHashMapを元に、マップを初期化します
   * @param map キーをPoint、値にマスを格納したHashMap
   * @exception 渡されたマップのサイズが不正なとき、IllegalArgumentException
   */
  public Map(HashMap<Point, Chip> map) throws IllegalArgumentException{
    this.map = map;
    this.goals = new HashSet<Point>();
    int max = 0;
    if(map.size() == 0) throw new IllegalArgumentException("渡されたマップが空です");
    // マップの横幅を算出する
    Iterator<Point> itr = map.keySet().iterator();
    while(itr.hasNext()){
      Point p = itr.next();
      if(p.x > max) max = p.x;
      Chip chip = map.get(p);
      if(chip.isGoal()){
        goals.add((Point)p.clone());
      }
    }
    width = max+1;
    // マップの高さを算出する
    if(map.size()%width != 0){
      throw new IllegalArgumentException("渡されたマップのサイズが不正です");
    }
    height = map.size()/width;
  }
  
  /**
   * 2点間のマンハッタン距離を返します
   * @param p1 始点
   * @param p2 終点
   * @return マンハッタン距離
   */
  static public int manhattanDistance(Point p1, Point p2){
    return Math.abs(p2.x-p1.x) + Math.abs(p2.y-p1.y);
  }
  
  /**
   * 渡した点をDirectionの方向に動かした新しい点を生成します
   * @param point 元の点
   * @param d 動かしたいDirection
   * @return 動いた後の新しい点
   */
  static public Point movePoint(Point point, Direction d){
    Point p = (Point)point.clone();
    switch(d){
    case North:
      p.translate(0, -1);
      break;
    case East:
      p.translate(1, 0);
      break;
    case South:
      p.translate(0, 1);
      break;
    case West:
      p.translate(-1, 0);
      break;
    case NorthEast:
      p.translate(1, -1);
      break;
    case SouthEast:
      p.translate(1, 1);
      break;
    case SouthWest:
      p.translate(-1, 1);
      break;
    case NorthWest:
      p.translate(-1, -1);
      break;
    }
    return p;
  }
 
  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj){
    Map other = (Map)obj;
    Iterator<Point> itr = map.keySet().iterator();
    while(itr.hasNext()){
      Point key = itr.next();
      if(!(this.getChipAt(key).equals(other.getChipAt(key)))) return false;
    }
    return this.getWidth() == other.getWidth() && this.getHeight() == other.getHeight();
  }
  
  /**
   * 指定された座標にあるマスを取り出します。見つからない場合はnullを返します
   * @param p 取り出したい座標
   * @return その座標にあるマス。ない場合はnull
   */
  public Chip getChipAt(Point p){
    if(this.map.containsKey(p)){
      return this.map.get(p);
    }else{
      return null;
    }
  }

  /**
   * ゴールの位置の一覧を返します
   * @return ゴールの位置を持ったHashMap
   */
  public HashSet<Point> getGoals(){
    return goals;
  }

  /**
   * マップの高さを返します
   * @return マップ高さ
   */
  public int getHeight(){
    return height;
  }
   
  /**
   * マップの幅を返します
   * @return マップ幅
   */
  public int getWidth(){
    return width;
  }
}
