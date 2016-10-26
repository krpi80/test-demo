import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class Enricher {

	// Parameter and return value can be Object or String for json payload.	
	public String enrich(String json) throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode root = mapper.readValue(json, ObjectNode.class);
		root.put("traderBook", "ABCD");
		return mapper.writeValueAsString(root);
		/*return "{"+
	"\"value\"     : \"100.50\","+
	"\"valueType\" : \"price\","+
	"\"date\"      : 20102016,"+
	"\"time\"      : 103052,"+
	"\"currency\"  : \"USD\","+
	"\"trader\"    : \"John Smith\","+
	"\"traderBook\": \"ABCD\","+
	"\"system\"    : \"MarketAcessLondon\""+
"}";*/
	}
	
}
