package killinglewis.Models;

import java.util.ArrayList;

public class CompositeVertexArray {
    private ArrayList<VertexArray> models;

    public CompositeVertexArray(ArrayList<VertexArray> models) {
        this.models = models;
    }

    public void addModel(VertexArray model) {
        models.add(model);
    }

    public ArrayList<VertexArray> getModels() {
        return models;
    }

    public void draw() {
        for (VertexArray v : models) {
            v.draw();
        }
    }
}
