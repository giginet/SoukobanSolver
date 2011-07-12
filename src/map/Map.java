/**
 * Map.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;

import java.lang.*;
import java.util.*;
import java.awt.*;

/**
 * @author giginet
 *
 */
public class Map{
  
  HashMap<Point, Chip> map = null;
  Character chara = null;
  int width = 0;
  int height = 0;
  
  /**
   * コンストラクタ。大きさ0の空のマップを生成します
   */
  public Map(){
    map = new HashMap<Point, Chip>();
  }
  
  /**
   * @param
   * map 座標をキー、 値をchipに持つHashmapを渡します
   * @exception
   * 渡されたマップのサイズが不正なとき、IllegalArgumentExceptionを返します
   */
  public Map(HashMap<Point, Chip> map, Point start) throws IllegalArgumentException{
    this.map = map;
    int max = 0;
    if(map.size() == 0) throw new IllegalArgumentException("渡されたマップが空です");
    // マップの横幅を算出する
    Iterator<Point> itr = map.keySet().iterator();
    while(itr.hasNext()){
      int x = itr.next().x;
      if(x > max) max = x;
    }
    width = max+1;
    // マップの高さを算出する
    if(map.size()%width != 0) throw new IllegalArgumentException("渡されたマップのサイズが不正です");
    height = map.size()/width;
  }  
}
