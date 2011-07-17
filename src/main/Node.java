/**
 * 
 */
package main;

import java.lang.Math;
import java.util.*;
import java.awt.Point;
import map.Map;
import util.Direction;

/**
 * 現在の探索ノードを保持しておくクラスです
 * @author giginet
 *
 */
public class Node{
  private Map current;
  private Node parent;
  private int cost;
  
  /**
   * 親ノードを持たない、現在の状態を持ったノードを生成します
   * @param m 現在のマップ
   */
  public Node(Map m){
    this.parent = null;
    this.current = m;
    this.cost = calcCost();
  }
  
  /**
   * コンストラクタ。あるNodeを渡し、その状態からキャラクターを動かしたときのノードを生成します
   * @param node 親ノード
   * @param d 親ノードからどう変化させるか
   */
  public Node(Node node, Direction d){
    this.parent = node;
    try{
      this.current = node.getCurrent().moveChara(d).deepClone();
    }catch(CloneNotSupportedException e){
      e.printStackTrace();
    }
    this.cost = calcCost();
  }
  
  /**
   * コンストラクタ。親ノードと現在のマップの状態からノードを生成します
   * @param map 現在のマップ 
   * @param parent 親ノード
   */
  public Node(Map map, Node parent){
    this.parent = parent;
    try{
      this.current = map.deepClone();
    }catch(CloneNotSupportedException e){
      e.printStackTrace();
    }
    this.cost = calcCost();
  }
  
  /**
   * コストを計算します
   * @return 計算されたコスト
   */
  private int calcCost(){
    int total = 0;
    if(current.isGoal()) return 0;
    //HashMap<Point, Integer> loadCosts = new HashMap<Point, Integer>();
    Iterator<Point> loadItr = current.getLoads().iterator();
    // 一番コストの低い荷物を検索する
    int minCost = 1000000;
    Point minPoint = null;
    while(loadItr.hasNext()){
      Point p = loadItr.next();
      int cost = calcCostForLoad(p);
      //loadCosts.put(p, Integer.valueOf(cost*2));
      total += cost*4;
      if(cost != 0 && cost < minCost){
        minCost = cost;
        minPoint = p;
      }
    }
    total -= minCost * 2; // 選ばれた荷物のコストを引いておく
    Point load = minPoint;
    // 一番コストの低い荷物について
    // その荷物から、一番近いゴールを探す
    minCost = 1000000;
    minPoint = null;
    Iterator<Point> goalItr = current.getGoals().iterator();
    while(goalItr.hasNext()){
      Point point = goalItr.next();
      if(current.getLoads().contains(point)) continue; // ゴールと一致する荷物があったら無視
      int cost = distance(load, point);
      if(cost < minCost){
        minCost = cost;
        minPoint = point;
      }
    }
    Point goal = minPoint;
    Point chara = current.getChara();
    // load周辺の4方向について、goalと一番遠い点を求める
    Direction ds[] = Direction.values();
    int maxDistance = 0;
    Point maxPoint = null;
    for(int i=0;i<ds.length-1;i+=2){
      Direction d = ds[i];
      Point next = Map.movePoint(load, d);
      int cost = distance(next, goal);
      if(maxDistance < cost){
        maxDistance = cost;
        maxPoint = next;
      }
    }    
    // キャラクターと荷物が隣接しているかどうか調べる
    if(!current.getChipAt(load).isConnect(current.getChipAt(current.getChara()))){
      // 隣接していない場合
      // maxPointまでのコストを加算する
      total += maxDistance + 300;
    }else{
      // 隣接している場合
      // 一番遠い点から時計回りにコストを振り、現在の位置をコストに足す
      int charaIndex = 0;
      int goalIndex = 0;
      // 一番遠い点から時計回りにどれくらいのコストが加算されるかを調べる
      int costTables[] = {0, 50, 100, 150, 200, 140, 90, 40};
      for(int i=0;i<ds.length;++i){
        Direction d = ds[i];
        Point next = Map.movePoint(load, d);
        if(next.equals(chara)){
          charaIndex = i;
        }else if(next.equals(maxPoint)){
          goalIndex = i;
        }
      }
      total += costTables[((goalIndex - charaIndex) + 8)%8];
    }
    return total;
  }
  
  /**
   * ある荷物について、マップ中に存在する全てのゴールとのコストの総和を計算します。<br>
   * ある荷物とあるゴールのコストは、２点のマンハッタン距離に、その間にある障害物の数を乗じた物と一致します。<br>
   * また、すでにその荷物がゴール上に存在した場合、無条件で0が返ります
   * @param load
   * @return
   */
  private int calcCostForLoad(Point load){
    int total = 0;
    if(current.getGoals().contains(load)) return 0; // 荷物がゴール上に存在した場合、0を返す
    Iterator<Point> itr = current.getGoals().iterator();
    while(itr.hasNext()){
      Point goal = itr.next();
      int cost = distance(load, goal) * getObstacleCount(load, goal);
      total += cost;
    }
   return total;
  }
  
  /**
   * 与えた２点を対角頂点とした区画の中にいくつの障害物があるかを計測します<br>
   * 障害物は、範囲内に存在する壁、または荷物です
   * @param p1
   * @param p2
   * @return 範囲内に存在する障害物の数
   */
  private int getObstacleCount(Point p1, Point p2){
    int count = 0;
    for(int x=Math.min(p1.x, p2.x);x<=Math.max(p1.x, p2.x);++x){
      for(int y=Math.min(p1.y, p2.y);y<=Math.max(p1.y, p2.y);++y){
        Point p = new Point(x, y);
        if(!current.canThrough(p)) ++count;
      }
    }
    return count;
  }
  
  private int distance(Point p1, Point p2){
    int dx = Math.abs(p1.x - p2.x) * 100;
    int dy = Math.abs(p1.y - p2.y) * 100;
    int d = ((dx < dy) ? dx : dy) / 2;
    return (dx + dy) - d;
  }

  /**
   * 現在のマップを返します
   * @return 現在のマップ
   */
  public Map getCurrent(){
    return current;
  }

  /**
   * 親ノードを返します
   * @return 親ノード
   */
  public Node getParent(){
    return parent;
  }

  /**
   * 現在のコストを返します
   * @return コスト
   */
  public int getCost(){
    return cost;
  }
  
  
}
