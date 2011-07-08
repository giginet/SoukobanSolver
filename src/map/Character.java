/**
 * Character.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;

import java.awt.*;

/**
 * @author giginet
 *
 */
public class Character{

  /**
   * 
   */
  
  private Point location = null;
  
  public Character(Point initial){
    location = initial;
  }

  public Point getLocation(){
    return location;
  }

  public void setLocation(Point location){
    this.location = location;
  }

}
