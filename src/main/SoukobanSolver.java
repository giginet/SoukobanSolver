package main;

import java.io.*;
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
    Node current = null;
    // 探索する
    long start = System.currentTimeMillis();
    while(!queue.isEmpty()){
      current = queue.poll();
      if(current.getCurrent().isGoal())
        break;
      Direction ds[] = Direction.values();
      Map map = current.getCurrent();
      for(int i = 0; i < ds.length - 1; i += 2){
        Direction d = ds[i];
        if(!map.canMoveChara(d)) continue;
        Map newMap = map.moveChara(d);
        if(visited.contains(newMap)) continue;
        // 調査済みでなかったら、Queueに新しいノードを生成して格納
        visited.add(newMap);
        Node node = new Node(newMap, current);
        queue.add(node);
      }
    }
    long end = System.currentTimeMillis();
    // 探索結果をたどる
    Node currentNode = current;
    ArrayList<Map> result = new ArrayList<Map>();
    while(currentNode != null){
      result.add(0, currentNode.getCurrent());
      currentNode = currentNode.getParent();
    }
    // 結果出力
    Iterator<Map> itr = result.iterator();
    while(itr.hasNext()){
      System.out.println(itr.next());
      if(itr.hasNext())
        System.out.println("↓");
    }
    System.out.println("finish");
    System.out.println((end-start)/1000.0 + " second");
  }

  public static void main(String[] args){
    String problem = "";
    try{
      FileReader fr = new FileReader("src/problems/problem2");
      BufferedReader br = new BufferedReader(fr);
      char tmp;
      while((tmp = (char)br.read()) != (char)-1){
        problem += String.valueOf(tmp);
      }
      br.close();
    }catch(IOException e){
      e.printStackTrace();
      System.exit(1);
    }
    SoukobanSolver ss = new SoukobanSolver(problem);
  }
}
