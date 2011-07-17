/**
 * Wall.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;

import java.awt.Point;

/**
 * 壁を表すクラスです。全ての人物、荷物は進入できません
 * @author giginet
 */
public class Wall extends Chip{
  protected final boolean goal = false;
  protected final boolean through = false;
  
  public Wall(Point location){
    super(location);
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    return "#";
  }
}
