PGraphics clvb, clv; 

void draw_channel_version() {

    clvb = createGraphics(800, 800);
    clvb.beginDraw();

    // Calculate scale factors for both width and height
    float scaleX = (float)clvb.width / seed.width;
    float scaleY = (float)clvb.height / seed.height;

    // Use the larger scale factor to ensure the image covers the entire area
    float scale = max(scaleX, scaleY);

    // Calculate new dimensions for the image
    int img_width = (int)(seed.width * scale);
    int img_height = (int)(seed.height * scale);

    // Calculate position to center the image
    int img_x_pos = (clvb.width - img_width) / 2;
    int img_y_pos = (clvb.height - img_height) / 2;

    // Display the image
    clvb.imageMode(CORNER); // Use CORNER mode for easier positioning
    clvb.image(seed, img_x_pos, img_y_pos, img_width, img_height);

    clvb.endDraw();

    // Create PGraphix and load pixel array from buffer
    clv = createGraphics(2400, 800);
    clv.beginDraw();
    clvb.loadPixels(); 

    // Width for each rvb block
    float rvb_block_width = clv.width / 3;

    // Loop three times to produce red, green and blue version of the image
    for (int i= 0; i < 3; i++) {

        // push and pop the current matrix stack
        clv.pushMatrix();
        
        clv.translate(rvb_block_width * i, 0);

        clv.colorMode(RGB, 255);
        // Create a map of colored map of points
        for (int y= 0; y < clv.height; y++) {
            for (int x= 0; x < rvb_block_width; x++) {
                color c = clvb.pixels[index(x, y, clvb.width)];
                clv.strokeWeight(2);
                switch(i) {
                    case 0: 
                        clv.stroke(red(c), 0, 0);
                        //clv.stroke(255 - red(c), 255, 255); // Inverted red component
                        break;
                    case 1: 
                        clv.stroke(0, green(c), 0);
                        //clv.stroke(255, 255 - green(c), 255); // Inverted green component
                        break;   
                    case 2: 
                        clv.stroke(0, 0, blue(c));
                        //clv.stroke(255, 255, 255 - blue(c)); // Inverted blue component
                        break;
                }
                clv.point(x, y);
            }   
        }
        clv.colorMode(HSB, 360, 100, 100);
        clv.popMatrix();
    }
    clv.endDraw();
}