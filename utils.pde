// Return the index of the target px in the px array
int index(int x, int y, int target_width) {
  return x + y * target_width;
}

// Custom object that returns average color and average brightness of an image
class ColorData {
  color rgb_color;
  color hsb_color;

  ColorData(color rgb_c, color hsb_c) {
    rgb_color = rgb_c;
    hsb_color = hsb_c;
  }
}

ColorData avg_colors(PImage source) {
  source.loadPixels();

  float r_px = 0.0;
  float g_px = 0.0;
  float b_px = 0.0;

  // Iterate over all pixels to sum up RGB values
  for (int i = 0; i < source.pixels.length; i++) {
    r_px += red(source.pixels[i]);
    g_px += green(source.pixels[i]);
    b_px += blue(source.pixels[i]);
  }

  // Calculate the average RGB values
  float avg_r = r_px / source.pixels.length;
  float avg_g = g_px / source.pixels.length;
  float avg_b = b_px / source.pixels.length;

  // Create the average RGB color
  color avg_rgb_color = color(avg_r, avg_g, avg_b);

  // Convert the average RGB color to HSB
  colorMode(HSB, 360, 100, 100);
  float avg_hue = hue(avg_rgb_color);
  float avg_sat = saturation(avg_rgb_color);
  float avg_bri = brightness(avg_rgb_color);
  color avg_hsb_color = color(avg_hue, avg_sat, avg_bri);

  println("hue: " + avg_hue, "saturation: " + avg_sat, "brightness: " + avg_bri, "hue: " + avg_hue);

  // Reset the color mode to RGB
  colorMode(RGB, 255);

  return new ColorData(avg_rgb_color, avg_hsb_color);
}
