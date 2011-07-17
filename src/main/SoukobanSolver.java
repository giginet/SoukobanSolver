package main;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Iterator;

import util.Direction;
import map.Map;

public class SoukobanSolver{
  
  private PriorityQueue<Node> queue;
  private ArrayList<Map> visited;
  
  @SuppressWarnings("unchecked")
  public SoukobanSolver(String problem){
    queue = new PriorityQueue<Node>(1, new NodeComparator());
    visited = new ArrayList<Map>();
    Node initial = new Node(Map.parse(problem));
    queue.add(initial);
    Node goalNode = null;
    while(!queue.isEmpty()){
      Node current = queue.poll();
      Direction ds[] = Direction.values();
      Map map = current.getCurrent();
      for(int i=0;i<ds.length-1;i+=2){
        Direction d = ds[i];
        if(map.canMoveChara(d)){
          Map newMap = map.moveChara(d);
          if(!visited.contains(newMap)){
            // 調査済みでなかったら、Queueに新しいノードを生成して格納
            visited.add(newMap);
            Node node = new Node(newMap, current);
            queue.add(node);
            if(newMap.isGoal()){
              goalNode = node;
              queue.clear();
              break;
            }
          }
        }
      }
    }
    Node currentNode = goalNode;
    ArrayList<Map> result = new ArrayList<Map>();
    while(currentNode != null){
      result.add(0, currentNode.getCurrent());
      currentNode = currentNode.getParent();
    }
    Iterator<Map> itr = result.iterator();
    while(itr.hasNext()){
      System.out.println(itr.next());
      if(itr.hasNext()) System.out.println("↓");
    }
    System.out.println("finish");
  }
  
  public static void main(String[] args){
    /*final String problem = 
        "#########\n" +
        "#.......#\n" +
        "#.##*##.#\n" +
        "#.@...*.#\n" +
        "##*GGG#.#\n" +
        "##....#.#\n" +
        "####..###\n" +
        "#########";*/
    final String problem =
        "#####\n" +
        "#@*G#\n" +
        "#.*.#\n" +
        "#G..#\n" +
        "#####";
    SoukobanSolver ss = new SoukobanSolver(problem);
  }
}
