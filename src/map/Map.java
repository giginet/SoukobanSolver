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

/**
 * @author giginet
 *
 */
public class Map{
  
  HashMap<Point, Chip> map = null;
  Character chara = null;
  int width = 0;
  int height = 0;
  
  public Map(){
    
  }
  
  /**
   * @param
   * map 座標をキー、 値をchipに持つHashmapを渡します
   * 
   */
  public Map(HashMap<Point, Chip> map, Point start){
    this.map = map;
  }
  
  
}
