package com.rocoinfo.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TestJackson {
    private static String json = "{\"indexs\":[{\"id\":\"6310_2\",\"createTime\":1359424596001,\"indexFields\":[{\"name\":\"keyword_score\",\"type\":1,\"analyzed\":true,\"highlight\":true,\"strValue\":\"2.1781344\",\"longValue\":0,\"intValue\":0},{\"name\":\"object_id\",\"type\":1,\"analyzed\":true,\"highlight\":true,\"strValue\":\"6310\",\"longValue\":0,\"intValue\":0},{\"name\":\"object_type\",\"type\":1,\"analyzed\":true,\"highlight\":true,\"strValue\":\"2\",\"longValue\":0,\"intValue\":0},{\"name\":\"user_id\",\"type\":1,\"analyzed\":true,\"highlight\":true,\"strValue\":\"657\",\"longValue\":0,\"intValue\":0},{\"name\":\"user_name\",\"type\":1,\"analyzed\":true,\"highlight\":true,\"strValue\":\"好书推荐\",\"longValue\":0,\"intValue\":0},{\"name\":\"publish_time\",\"type\":1,\"analyzed\":true,\"highlight\":true,\"strValue\":\"1324892396000\",\"longValue\":0,\"intValue\":0},{\"name\":\"name\",\"type\":1,\"analyzed\":true,\"highlight\":true,\"strValue\":\"水浒传\",\"longValue\":0,\"intValue\":0}]}]}";

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        JsonNode jn = jsonNode.get("indexs");
        System.out.println(jn.toString());

        Index p = mapper.readValue(json, Index.class);
        System.out.println(p.getIndexs());
        List<Model> indexs = p.getIndexs();
        for (Model m : indexs) {
            System.out.println(m.getId());
        }
    }

}

class Index {
    private List<Model> indexs;

    public List<Model> getIndexs() {
        return indexs;
    }

    public void setIndexs(List<Model> indexs) {
        this.indexs = indexs;
    }
}

class Model {
    private String id;
    private long createTime;
    private List<Field> indexFields;

    public Model() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<Field> getIndexFields() {
        return indexFields;
    }

    public void setIndexFields(List<Field> indexFields) {
        this.indexFields = indexFields;
    }


}

class Field {
    private String name;
    private int type;
    private boolean analyzed;
    private boolean highlight;
    private String strValue;
    private long longValue;
    private int intValue;

    public Field() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAnalyzed() {
        return analyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}  
