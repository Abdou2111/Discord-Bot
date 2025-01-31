package org.example;

import java.util.ArrayList;

public class JsonApiResponse {
    public String id;
    public String provider;
    public String model;
    public String object;
    public long created;
    public ArrayList<String> choices;
    public String system_fingerprint;
    public String usage;

    public JsonApiResponse(String id, String provider, String model, String object, long created, ArrayList<String> choices, String system_fingerprint, String usage) {
        this.id = id;
        this.provider = provider;
        this.model = model;
        this.object = object;
        this.created = created;
        this.choices = choices;
        this.system_fingerprint = system_fingerprint;
        this.usage = usage;
    }

    public String getId() {
        return id;
    }
}
