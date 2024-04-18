PGraphics sdg; // Sorted Grid
IntDict inventory;
String[] colorKeys;

void generate_sorted_colors() {

    inventory = new IntDict();
    seed.loadPixels();

    for (int i = 0; i < seed.pixels.length; i++) {
        String sorted_color = hex(seed.pixels[i]);
        if (inventory.hasKey(sorted_color) == true || i != 0) {
        inventory.increment(sorted_color);
        } else {
        inventory.set(sorted_color, 1);
        }
    }

    inventory.sortValuesReverse();
    colorKeys = inventory.keyArray();
}

void draw_sorted_grid(){

    // Create array of sorted colors    
    generate_sorted_colors();

    int index = 0;
    int row = 4;
    int col = 6;

    sdg = createGraphics(2400, 1600);
    sdg.beginDraw();

    for (int i=0; i < row; i++) {
        for (int j=0; j < col; j++) {

            int w = sdg.width / 6;
            int h = w;
            int remap_index = (int) map(index, 0, row * col, 0, inventory.size());

            sdg.noStroke();
            sdg.fill(unhex(colorKeys[remap_index]));
            sdg.rect(w * j, h * i, w, h);

            index++;
        }
    }

  sdg.endDraw();
}