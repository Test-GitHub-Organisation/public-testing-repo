package com.alation.lambda.s3.metadata;

import com.alation.lambda.s3.http.HtmlTable;
import com.alation.lambda.s3.util.Json2Schema;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import org.apache.log4j.Logger;
import org.apache.wink.json4j.OrderedJSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JsonMetaDataHandler extends MetadataHandle {
	public static Logger logger = Logger.getLogger(JsonMetaDataHandler.class);
	@Override
	public String getBody() {
		try {

			StringBuffer buff = new StringBuffer();

			buff.append("<h4>Schema</h4>");
			buff.append("<div>");
			buff.append("<pre>");
			buff.append(Json2Schema.toSchema(new String(getFileContent())));
			buff.append("</pre>");
			buff.append("</div>");
			
			// Get Sample
			ObjectMapper objectMapper = new ObjectMapper();

			//read JSON like DOM Parser
			Object rootNode = objectMapper.readTree(getFileContent());

			if (rootNode instanceof ArrayNode) {
				if (((ArrayNode) rootNode).size() > 0) {
					buff.append("<h4>Sample JSON content</h4>");
					int index = 0;
					for (int i = 0; i < (((ArrayNode) rootNode).size() > getAlationConfig().getSampleSize() ? getAlationConfig().getSampleSize() : ((ArrayNode) rootNode).size()); i++) {
					JsonNode sample = ((ArrayNode) rootNode).get(i);
					buff.append("<h4>Sample Data #"+ ++index +" </h4>");
					buff.append("<div>");
					buff.append("<pre>");
					buff.append(sample);
					buff.append("</pre>");
					buff.append("</div>");
					}
				}
			} else {
                OrderedJSONObject outputJsonObject = new OrderedJSONObject();
                int index = 0;
				Iterator<Map.Entry<String, JsonNode>> iterator = ((JsonNode) rootNode).fields();
				while(iterator.hasNext()) {
					if (index < getAlationConfig().getSampleSize()) {
						Map.Entry<String, JsonNode> element = iterator.next();
						ObjectMapper mapper = new ObjectMapper();
						if(element.getValue().getNodeType() == JsonNodeType.OBJECT) {
							OrderedJSONObject jsonObjects = new OrderedJSONObject(mapper.writeValueAsString(element.getValue()));
							outputJsonObject.put(element.getKey(), jsonObjects);
						}
						else if(element.getValue().getNodeType() == JsonNodeType.ARRAY) {
							org.apache.wink.json4j.JSONArray jsonArray = new org.apache.wink.json4j.JSONArray(mapper.writeValueAsString(element.getValue()));
							outputJsonObject.put(element.getKey(), jsonArray);
						}
						else if(element.getValue().getNodeType() == JsonNodeType.STRING) {
							String stringVal = new String(mapper.writeValueAsString(element.getValue()));
							stringVal = stringVal.replaceAll("\"", "");
							outputJsonObject.put(element.getKey(), stringVal);
						}	
						else {
						outputJsonObject.put(element.getKey(), mapper.writeValueAsString(element.getValue()));
						}
						index++;
					} else {
						break;
					}
				}
                buff.append("<h4>Sample JSON content</h4>");
                buff.append("<div>");
                buff.append("<pre>");
                buff.append(outputJsonObject.toString(2));
                buff.append("</pre>");
                buff.append("</div>");
			}

			return buff.toString();

		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
		}

		return HtmlTable.getEmptyTableHTML();
	}

    public static void main(String[] args) throws IOException, JSONException, org.apache.wink.json4j.JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object rootNode = objectMapper.readTree(new FileReader("C:\\Users\\subha\\Desktop\\Alation\\docs\\JSON\\battersubha33.json"));
        if (rootNode instanceof ArrayNode) {
            JSONArray jsonArray = (JSONArray) rootNode;
			logger.info(jsonArray.get(0));

        } else {
			Iterator<Map.Entry<String, JsonNode>> iterator = ((JsonNode) rootNode).fields();
			JSONObject jsonObject = new JSONObject();
			while(iterator.hasNext()) {
				Map.Entry<String, JsonNode> element = iterator.next();
				ObjectMapper objectMapper1 = new ObjectMapper();
				logger.info(element.getValue().getNodeType()); 
				if(element.getValue().getNodeType() == JsonNodeType.OBJECT) {
					OrderedJSONObject jsonObjects = new OrderedJSONObject(objectMapper1.writeValueAsString(element.getValue()));
					jsonObject.put(element.getKey(), jsonObjects);
					
				}
				else if(element.getValue().getNodeType() == JsonNodeType.ARRAY) {
					org.apache.wink.json4j.JSONArray a = new org.apache.wink.json4j.JSONArray(objectMapper1.writeValueAsString(element.getValue()));					
					jsonObject.put(element.getKey(), a);
				}
				else if(element.getValue().getNodeType() == JsonNodeType.STRING) {
					String a = new String(objectMapper1.writeValueAsString(element.getValue()));
					a = a.replaceAll("\"", "");
					jsonObject.put(element.getKey(), a);
				}	 
				 
				else {
				jsonObject.put(element.getKey(), objectMapper1.writeValueAsString(element.getValue()));
				}
			}
			String output = jsonObject.toString(2);
//			output = output.replaceAll("\\\"", "'");
			logger.info(output);

        }
    }

	@Override
	public String getSchema() {
		return Json2Schema.toSchema(new String(getFileContent()));
	}

}
