import java.util.*;

public class Main {
    public static List<Node> graph = new ArrayList<>();
    public static Map<String, String> stairFloor = new HashMap<>();

    public static void main(String[] args) {
        String src = "A01N001";
        String dst = "C01N005";

        //A건물, 1층
        graph.add(new Node("A01N001", "{\"A01S001\":1, \"A01G001\":1, \"A01N002\":1}", 4, 1));
        graph.add(new Node("A01N002", "{\"A01N001\":1, \"A01N003\":1}", 4, 2));
        graph.add(new Node("A01N003", "{\"A01N002\":1, \"A01S002\":1, \"A01G002\":1}", 4, 3));
        graph.add(new Node("A01S001", "{\"A01N001\":1}", 4, 0));
        graph.add(new Node("A01S002", "{\"A01N003\":1}", 3, 3));
        graph.add(new Node("A01G001", "{\"A01N001\":1}", 3, 1));
        graph.add(new Node("A01G002", "{\"A01N003\":1}", 4, 4));
        //A건물, 2층
        graph.add(new Node("A02N001", "{\"A02S001\":1, \"A02N002\":1}", 4, 1));
        graph.add(new Node("A02N002", "{\"A02N001\":1, \"A02N003\":1}", 4, 2));
        graph.add(new Node("A02N003", "{\"A02N002\":1, \"A02S002\":1}", 4, 3));
        graph.add(new Node("A02S001", "{\"A02N001\":1}", 4, 0));
        graph.add(new Node("A02S002", "{\"A02N003\":1}", 3, 3));
        //A건물, stairFloor 정보
        stairFloor.put("A01S001", "01,02");
        stairFloor.put("A02S001", "01,02");
        stairFloor.put("A01S002", "01,02");
        stairFloor.put("A02S002", "01,02");

        //B건물, 1층
        graph.add(new Node("B01N001", "{\"B01N002\":1}", 0, 1));
        graph.add(new Node("B01N002", "{\"B01N001\":1, \"B01N003\":1, \"B01G001\":1}", 0, 2));
        graph.add(new Node("B01N003", "{\"B01N002\":1, \"B01S001\":1}", 0, 3));
        graph.add(new Node("B01S001", "{\"B01N003\":1}", 0, 4));
        graph.add(new Node("B01G001", "{\"B01N002\":1}", 1, 2));

        //B건물, 2층
        graph.add(new Node("B02N001", "{\"B02N002\":1, \"B02S002\":1}", 0, 1));
        graph.add(new Node("B02N002", "{\"B02N001\":1, \"B02N003\":1}", 0, 2));
        graph.add(new Node("B02N003", "{\"B02N002\":1, \"B02S001\":1}", 0, 3));
        graph.add(new Node("B02S001", "{\"B02N003\":1}", 0, 4));
        graph.add(new Node("B02S002", "{\"B02N001\":1}", 0, 0));

        //B건물, 3층
        graph.add(new Node("B03N001", "{\"B03N002\":1, \"B03S002\":1}", 0, 1));
        graph.add(new Node("B03N002", "{\"B03N001\":1, \"B03N003\":1}", 0, 2));
        graph.add(new Node("B03N003", "{\"B03N002\":1}", 0, 3));
        graph.add(new Node("B03S002", "{\"B03N001\":1}", 0, 0));

        //B건물, stairFloor 정보
        stairFloor.put("B01S001", "01,02");
        stairFloor.put("B02S001", "01,02");
        stairFloor.put("B02S002", "02,03");
        stairFloor.put("B03S002", "02,03");

        //C건물, 1층
        graph.add(new Node("C01N001", "{\"C01N002\":1, \"C01N008\":1}", 5, 6));
        graph.add(new Node("C01N002", "{\"C01N001\":1, \"C01N003\":1}", 5, 7));
        graph.add(new Node("C01N003", "{\"C01N002\":1, \"C01N004\":1}", 5, 8));
        graph.add(new Node("C01N004", "{\"C01N003\":1, \"C01N005\":1}", 4, 8));
        graph.add(new Node("C01N005", "{\"C01N004\":1, \"C01N006\":1}", 3, 8));
        graph.add(new Node("C01N006", "{\"C01N005\":1, \"C01N007\":1, \"C01G002\":1}", 3, 7));
        graph.add(new Node("C01N007", "{\"C01N006\":1, \"C01N008\":1}", 3, 6));
        graph.add(new Node("C01N008", "{\"C01N001\":1, \"C01N007\":1, \"C01G001\":1}", 4, 6));
        graph.add(new Node("C01G001", "{\"C01N008\":1}", 4, 5));
        graph.add(new Node("C01G002", "{\"C01N006\":1}", 2, 7));

        Node srcNode = null;
        Node dstNode = null;
        //id에 맞는 노드 가져오기
        for (Node node : graph) {
            if (node.getId().equals(src)) {
                srcNode = node;
            }
            if (node.getId().equals(dst)) {
                dstNode = node;
            }
            if (srcNode != null && dstNode != null) {
                break;
            }
        }

        //둘 다 graph에 존재하면 경로 찾기
        if (srcNode != null && dstNode != null) {
            List<Node> result = navigate(srcNode, dstNode);

            for (Node node : result) {
                System.out.println(node.getId());
            }
        }
    }

    public static List<Node> navigate(Node src, Node dst) {
        String srcId = src.getId();
        String dstId = dst.getId();

        //같은 건물인 경우(나중에 글자 수 늘어날 수도 있으니까 substring...)
        if (srcId.substring(0, 1).equals(dstId.substring(0, 1))) {
            //하나의 건물 안에서의 길찾기
            return navigateBuilding(src, dst);
        }

        //다른 건물인 경우
        //서로 가장 가까운 src의 출입구 & dst의 출입구 찾기
        List<Node> srcGates = new ArrayList<>();
        List<Node> dstGates = new ArrayList<>();
        Node srcGate = null;
        Node dstGate = null;

        for(Node node : graph) {
            if(srcId.substring(0, 1).equals(node.getId().substring(0, 1)) && node.getId().charAt(3) == 'G') {
                //src 건물의 출입구들을 배열에 저장
                srcGates.add(node);
            }
            if(dstId.substring(0, 1).equals(node.getId().substring(0, 1)) && node.getId().charAt(3) == 'G') {
                //dst 건물의 출입구들을 배열에 저장
                dstGates.add(node);
            }
        }

        double distance = 0; //최소 거리
        for (Node sGate : srcGates) {
            for (Node dGate : dstGates) {
                double temp = Math.sqrt(Math.pow(sGate.getLatitude() - dGate.getLatitude(), 2) + Math.pow(sGate.getLongitude() - dGate.getLongitude(), 2));
                if (distance == 0 || distance > temp) {
                    distance = temp;
                    srcGate = sGate;
                    dstGate = dGate;
                }
            }
        }

        List<Node> fullResult = new ArrayList<>();

        //src->srcGate && dstGate->dst 경로 합치기
        fullResult.addAll(navigateBuilding(src, srcGate));
        fullResult.addAll(navigateBuilding(dstGate, dst));

        return fullResult;
    }

    //하나의 건물 안에서 길찾기
    public static List<Node> navigateBuilding(Node src, Node dst) {
        List<Node> dsts = new ArrayList<>();
        List<Node> fullResult = new ArrayList<>();
        String srcId = src.getId();
        String dstId = dst.getId();

        dsts.add(dst);
        //층이 동일한 경우
        if (srcId.substring(1, 3).equals(dstId.substring(1, 3))) {
            return dijkstra(src, dsts);
        }

        //층이 다른 경우
        Node nextStair = src;
        do {
            fullResult.addAll(diffStair(nextStair, dst));
            nextStair = changeFloor(fullResult.get(fullResult.size()-1), dst);
        } while(!nextStair.getId().substring(0, 3).equals(dst.getId().substring(0, 3)));

        if(nextStair.getId().substring(1, 3).equals(dst.getId().substring(1, 3))) {
            fullResult.addAll(dijkstra(nextStair, dsts));
        }
        return fullResult;
    }

    //층 변환
    public static Node changeFloor(Node current, Node dst) {
        //층 정보 바꿔주기(최상층 노드를 src로)
        String[] floors = stairFloor.get(current.getId()).split(",");
        String nextFloor;

        if(Integer.parseInt(dst.getId().substring(1, 3)) > Integer.parseInt(current.getId().substring(1, 3))) {
            nextFloor = floors[floors.length - 1];
        } else {
            nextFloor = floors[0];
        }

        //max의 층 중 막노드의 끝4자리와 동일한 노드 찾기, 그걸 src로 만들기
        for (Node node : graph) {
            if (node.getId().substring(0, 1).equals(current.getId().substring(0, 1)) && node.getId().substring(1, 3).equals(nextFloor) && node.getId().substring(3, 7).equals(current.getId().substring(3, 7))) {
                return node;
            }
        }
        return current;
    }

    //다른 층인 경우, 계단 찾기
    public static List<Node> diffStair(Node src, Node dst) {
        List<Node> dsts = new ArrayList<>();
        String srcId = src.getId();
        String dstId = dst.getId();

        int maxFloor = -500;
        int minFloor = 500;
        Node dstStair = null;

        for (Node node : graph) {
            //같은 건물, 같은 층의 계단들 중
            if (srcId.substring(0, 3).equals(node.getId().substring(0, 3)) && node.getId().charAt(3) == 'S') {
                //가는 층 정보에서 src, dst가 모두 포함된 경우 (같은 id의 계단이 둘 다 존재)
                if (node.getId().substring(3, 7).equals(dstId.substring(3, 7))) {
                    dsts.add(node);
                } else {
                    //가장 높은 곳/낮은 곳 까지 가는 계단을 dst로 설정(실제에서는 DB의 정보로..)
                    String[] floors = stairFloor.get(node.getId()).split(","); // 01, 02
                    //dst가 src보다 높은 층
                    if(Integer.parseInt(dst.getId().substring(1, 3)) > Integer.parseInt(src.getId().substring(1, 3))) {
                        if (maxFloor < Integer.parseInt(floors[floors.length - 1])) {
                            maxFloor = Integer.parseInt(floors[floors.length - 1]);
                            dstStair = node;
                        }
                    } else if(Integer.parseInt(dst.getId().substring(1, 3)) < Integer.parseInt(src.getId().substring(1, 3))) {
                         if(minFloor > Integer.parseInt(floors[0])) {
                            minFloor = Integer.parseInt(floors[0]);
                            dstStair = node;
                        }
                    }
                }
            }
        }
        if (dsts.isEmpty()) {
            //src->dst의 직행 계단 없는 경우
            dsts.add(dstStair);
        }

        //(임시)결과를 return
        return dijkstra(src, dsts);
    }

    // 다익스트라 알고리즘 구현, 한 층 안에서 길찾기
    public static List<Node> dijkstra(Node src, List<Node> dsts) {
        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> prev = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        // 초기화
        for (Node node : graph) {
            distance.put(node, Integer.MAX_VALUE);
            prev.put(node, null);
        }
        distance.put(src, 0);
        pq.add(src);

        // Dijkstra 알고리즘
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            for (Map.Entry<String, Integer> neighborEntry : current.getConnectNodes().entrySet()) {
                String neighborId = neighborEntry.getKey();
                int weight = neighborEntry.getValue();
                Node neighbor = graph.stream().filter(n -> n.getId().equals(neighborId)).findFirst().orElse(null);
                if (neighbor == null) continue;

                int alt = distance.get(current) + weight;
                if (alt < distance.get(neighbor)) {
                    distance.put(neighbor, alt);
                    prev.put(neighbor, current);
                    pq.add(neighbor);
                }
            }
        }

        // 최단 경로 구성
        int minDistance = Integer.MAX_VALUE;
        Node shortestNode = null;
        for (Node node : dsts) {
            if (distance.containsKey(node) && distance.get(node) < minDistance) {
                minDistance = distance.get(node);
                shortestNode = node;
            }
        }

//        if (shortestNode == null) {
//            return null; // 최단 경로가 없는 경우
//        }

        List<Node> shortestPath = new ArrayList<>();
        for (Node at = shortestNode; at != null; at = prev.get(at)) {
            shortestPath.add(at);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }
}