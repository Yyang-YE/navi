import java.util.HashMap;
import java.util.Map;

public class Node {
    private String id;
    private Map<String, Integer> connectedNodes;

    private int latitude; //위도, 가로선
    private int longitude; //경도, 세로선

    public Node(String id, String connectNodes, int latitude, int longitude) {
        this.id = id;
        this.connectedNodes = parseMap(connectNodes);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //차후 jackson 써서 String json -> map으로 코드 바꾸기
    private Map<String, Integer> parseMap(String connectNodes) {
        Map<String, Integer> map = new HashMap<>();

        connectNodes = connectNodes.substring(1, connectNodes.length() - 1); // {} 제거
        String[] connections = connectNodes.split(", ");
        for (String connection : connections) {
            String[] parts = connection.split(":");
            String nodeId = parts[0].replaceAll("\"", "");
            int weight = Integer.parseInt(parts[1]);
            map.put(nodeId, weight);
        }
        return map;
    }


    public String getId() {
        return id;
    }

    public Map<String, Integer> getConnectNodes() {
        return connectedNodes;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }
}
