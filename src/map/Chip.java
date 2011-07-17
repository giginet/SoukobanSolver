/**
 * Chip.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;
import java.lang.Math;
import java.awt.Point;


/**
 * マップ上に存在するマス目の基底クラスです
 * @author giginet
 */
abstract public class Chip{
  protected boolean goal = false;
  protected boolean through = false;
  protected Point location = null;
  
  /**
   * コンストラクタです。初期位置は0,0に設定されます
   */
  public Chip(){
    this.location = new Point(0, 0);
  }
  
  /**
   * 配置位置を指定するコンストラクタです
   * @param location 配置位置
   */
  public Chip(Point location){
    this.location = (Point)location.clone();
  }
  
  /**
   * このマス目がゴール（荷物の最終的な配置位置）であるかどうかを返します
   * @return このマス目がゴールであるかどうか
   */
  public boolean isGoal(){
    return this.goal;
  }
  
  /**
   * このマス目を人間や荷物が通過可能かどうかを返します
   * @return このマス目が通行可能かどうか
   */
  public boolean canThrough(){
    return this.through;
  }
  
  /**
   * あるマス目とこのマス目が隣接しているかどうかを返します
   * @param other 比較する他のマス目
   * @return ２つのマスが隣接しているかどうか
   */
  public boolean connect(Chip other){
    return Math.abs(getX()-other.getX()) + Math.abs(getY()-other.getY()) == 1;
  }
  
  /**
   * このマス目のx座標を返します
   * @return マス目のx座標
   */
  public int getX(){
    return this.location.x;
  }
  
  /**
   * このマス目のy座標を返します
   * @return マス目のy座標
   */
  public int getY(){
    return this.location.y;
  }
  
  /**
   * このマス目の座標を返します
   * @return 座標を表すPoint
   */
  public Point getLocation(){
    return location;
  }
  
  /** (non-Javadoc)
   * @see java.lang.Object#toString(java.lang.Object)
   */
  @Override
  public String toString(){
    return "Chip [location=" + location + ", getClass()=" + getClass() + "]";
  }
  
  /** (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj){
    Chip other = (Chip)obj;
    return location == other.getLocation() && getClass() == other.getClass();
  }

  @Override
  protected Object clone() throws CloneNotSupportedException{
    return super.clone();
  }
}
