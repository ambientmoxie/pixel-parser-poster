PGraphics mnib, mni;

void draw_main_image() {

    // Main Image Buffer
    // --------------------   

    mnib = createGraphics(2400, 2400);
    mnib.beginDraw();

    // Calculate scale factors for both width and height
    float scaleX = (float)mnib.width / seed.width;
    float scaleY = (float)mnib.height / seed.height;

    // Use the larger scale factor to ensure the image covers the entire area
    float scale = max(scaleX, scaleY);

    // Calculate new dimensions for the image
    int img_width = (int)(seed.width * scale);
    int img_height = (int)(seed.height * scale);

    // Calculate position to center the image
    int img_x_pos = (mnib.width - img_width) / 2;
    int img_y_pos = (mnib.height - img_height) / 2;

    // Display the image
    mnib.imageMode(CORNER); // Use CORNER mode for easier positioning
    mnib.image(seed, img_x_pos, img_y_pos, img_width, img_height);
    mnib.endDraw();

    // Main Image
    // --------------------

    mni = createGraphics(2400, 2400);
    mni.beginDraw();

    mnib.loadPixels();

    // Nbr of rect by row/col
    float account = 70;

    ColorData result = avg_colors(seed);
    color c = result.rgb_color;
    // print(hex(c, 6));

    // Draw the grid
    for (int i = 0; i < account; i++) {
        for (int j = 0; j < account; j++) {

            float grid_w = mni.width / account;
            float grid_h = mni.height / account;
            float rect_pos_x = grid_w * i;
            float rect_pos_y = grid_h * j;

            // Target the pixel in the center of the rect
            float target_px_x = rect_pos_x + (grid_w / 2);
            float target_px_y = rect_pos_y + (grid_h / 2);

            // Get the pixel in the pixel array
            int target_px = mnib.pixels[index(int(target_px_x), int(target_px_y), mnib.width)];
            colorMode(HSB, 360, 100, 100);
            float px_brightness = brightness(target_px);

            // Ellipses coordinates
            float ellipse_pos_x = target_px_x;
            float ellipse_pos_y = target_px_y;
            
            colorMode(RGB, 255, 255, 255);
            if (px_brightness > 0 && px_brightness < 25) {

            // Green rectangle
            mni.fill(c);
            mni.stroke(c);
            mni.rect( rect_pos_x, rect_pos_y, grid_w, grid_h);
            } else if (px_brightness > 25 && px_brightness < 50) {

            // Green rectangle with SMALL white ellipse
            mni.fill(c);
            mni.stroke(c);
            mni.rect( rect_pos_x, rect_pos_y, grid_w, grid_h);
            mni.ellipseMode(CENTER);
            mni.noStroke();
            mni.fill(255);
            mni.ellipse(ellipse_pos_x, ellipse_pos_y, grid_w - (grid_w / 1.8), grid_h - (grid_h / 1.8));
            } else if (px_brightness > 50 && px_brightness < 75) {

            // Green rectangle with BIG white ellipse
            mni.fill(c);
            mni.stroke(c);
            mni.rect( rect_pos_x, rect_pos_y, grid_w, grid_h);
            mni.ellipseMode(CENTER);
            mni.noStroke();
            mni.fill(255);
            mni.ellipse(ellipse_pos_x, ellipse_pos_y, grid_w - (grid_w / 3), grid_h - (grid_h / 3));
            } else {

            // White rectangle
            mni.fill(255);
            mni.stroke(255);
            mni.rect( rect_pos_x, rect_pos_y, grid_w, grid_h);
            }
        }
    }

    mni.endDraw();
    
}