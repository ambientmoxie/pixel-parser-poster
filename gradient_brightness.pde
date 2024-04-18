PGraphics gtb; // Gradient Brightness

void draw_gradient_brightness(){

    gtb = createGraphics(880, 4800);
    gtb.beginDraw();

    // Return average bright value of image
    ColorData result = avg_colors(seed);
    colorMode(HSB, 360, 100, 100);
    float average_brightness = brightness(result.hsb_color);

    int number_rect = 6; // Total number of rectangles
    int w = gtb.width;
    int h = 800; // Height of each rectangle

    for (int i = 0; i < number_rect; i++) {

        // Interpolate brightness value for the current rectangle
        float interp_brightness = lerp(average_brightness, 100, (float)i / (number_rect - 1));

        // Create color with interpolated brightness
        
        color grad_color = color(0, 0, interp_brightness);

        // Draw rectangle with gradient color
        gtb.noStroke();
        gtb.fill(grad_color);
        gtb.rect(0, h * i, w, h);
    }

    colorMode(RGB, 255, 255, 255);

    gtb.endDraw();

}