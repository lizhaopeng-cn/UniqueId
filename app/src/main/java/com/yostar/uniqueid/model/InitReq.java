package com.yostar.uniqueid.model;

import java.util.List;

public class InitReq {
    private List<TypeData> type_data;

    public List<TypeData> getType_data() {
        return type_data;
    }

    public void setType_data(List<TypeData> type_data) {
        this.type_data = type_data;
    }

    public static class TypeData{
        private String type;
        private String data;

        public TypeData(String type, String data){
            this.type = type;
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
