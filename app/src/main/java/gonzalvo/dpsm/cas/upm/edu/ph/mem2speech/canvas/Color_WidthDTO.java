package gonzalvo.dpsm.cas.upm.edu.ph.mem2speech.canvas;

import android.graphics.Path;

import java.util.HashMap;
import java.util.Map;

class Color_WidthDTO {
    private final Map<Path, Integer> colorsMap;
    private final Map<Path, Float> widthMap;

    Color_WidthDTO() {
        this.colorsMap = new HashMap<>();
        this.widthMap = new HashMap<>();
    }

    Integer getColor(Path path) {
        return this.colorsMap.get(path);
    }

    Float getStrokeWidth(Path path) {
        return this.widthMap.get(path);
    }

    void putPenProperty(Path path, int selectedColor, float strokeWidth) {
        this.colorsMap.put(path, selectedColor);
        this.widthMap.put(path, strokeWidth);
    }

    void clear() {
        this.colorsMap.clear();
        this.widthMap.clear();
    }
}
