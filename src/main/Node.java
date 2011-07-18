/**
 * 
 */
package main;

import java.lang.Math;
import java.util.*;
import java.awt.Point;
import map.*;
import map.Map;
import util.Direction;

/**
 * 現在の探索ノードを保持しておくクラスです
 * @author giginet
 *
 */
public class Node{
  private int cost;
  private MapState current;
  private Node parent;
  
  /**
   * コンストラクタ。親ノードを持たない、現在の状態を持ったノードを生成します
   * @param m 現在のマップ状態
   */
  public Node(MapState m){
    this.parent = null;
    this.current = m;
    this.cost = calcCost();
  }
  
  /**
   * コンストラクタ。親ノードと現在のマップの状態からノードを生成します
   * @param map 現在のマップ状態
   * @param parent 親ノード
   */
  public Node(MapState map, Node parent){
    this.parent = parent;
    this.current = map.deepClone();
    this.cost = calcCost();
  }
  
  /**
   * コンストラクタ。あるNodeを渡し、その状態からキャラクターを動かしたときのノードを生成します
   * @param node 親ノード
   * @param d 親ノードからどう変化させるか
   */
  public Node(Node node, Direction d){
    this.parent = node;
    this.current = node.getCurrent().moveChara(d).deepClone();
    this.cost = calcCost();
  }
  
  /**
   * 現在のコストを返します
   * @return コスト
   */
  public int getCost(){
    return cost;
  }
  
  /**
   * 現在のマップを返します
   * @return 現在のマップ
   */
  public MapState getCurrent(){
    return current;
  }
  
  /**
   * 親ノードを返します。rootの場合はnullを返します
   * @return 親ノード
   */
  public Node getParent(){
    return parent;
  }
  
  /**
   * コストを計算します
   * @return 計算されたコスト
   */
  private int calcCost(){
    int total = 0;
    if(current.isGoal()) return 0;
    Iterator<Point> loadItr = current.getLoads().iterator();
    // 一番コストの低い荷物を検索する
    int minCost = 1000000;
    Point minPoint = null;
    Direction ds[] = Direction.values();
    while(loadItr.hasNext()){
      Point p = loadItr.next();
      // もし、いずれの方向にも移動不可で、ゴールに到達していない荷物が一つでもあったら
      // その時点で非常に大きなスコアを返す（デッドロック）
      int cost = calcCostForLoad(p);
      /*if(cost!=0){
        int throughCount = 0;
        for(int i=0;i<2;++i){
          Direction d1 = ds[i*2];
          Direction d2 = ds[i*2+4];
          if(current.canThrough(Map.movePoint(p, d1)) && current.canThrough(Map.movePoint(p, d2))){
            ++throughCount;
          }
        }
        if(throughCount == 0){
          return 1000000000;
        }
      }*/
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
    Iterator<Point> goalItr = current.getMap().getGoals().iterator();
    while(goalItr.hasNext()){
      Point point = goalItr.next();
      if(current.getLoads().contains(point)) continue; // ゴールと一致する荷物があったら無視
      int cost = distance(load, point) * 100;
      if(cost < minCost){
        minCost = cost;
        minPoint = point;
      }
    }
    Point goal = minPoint;
    Point chara = current.getChara();
    // load周辺の4方向について、goalと一番遠い点を求める
    int maxDistance = 0;
    Point maxPoint = null;
    for(int i=0;i<ds.length-1;i+=2){
      Direction d = ds[i];
      Point next = Map.movePoint(load, d);
      int cost = distance(next, goal) * 100;
      if(maxDistance < cost){
        maxDistance = cost;
        maxPoint = next;
      }
    }    
    goal = maxPoint;
    // キャラクターと荷物が隣接しているかどうか調べる
    if(!current.getMap().getChipAt(load).isConnect(current.getMap().getChipAt(current.getChara()))){
      // 隣接していない場合
      // maxPointまでのコストを加算する
      total += maxDistance + 300;
    }else{
      // 隣接している場合
      // 一番遠い点から時計回りにどれくらいのコストが加算されるかを調べる
      // goalと今いる点のマンハッタン距離を調べて、距離に応じてスコア加算
      total += distance(goal, chara) * 50;
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
    if(current.getMap().getGoals().contains(load)) return 0; // 荷物がゴール上に存在した場合、0を返す
    Iterator<Point> itr = current.getMap().getGoals().iterator();
    while(itr.hasNext()){
      Point goal = itr.next();
      int cost = distance(load, goal) * getObstacleCount(load, goal);
      total += cost;
    }
   return total;
  }

  /**
   * 任意の2点間のマンハッタン距離を計測します
   * @param p1
   * @param p2
   * @return 計測された距離
   */
  private int distance(Point p1, Point p2){
    return Map.manhattanDistance(p1, p2);
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

}
