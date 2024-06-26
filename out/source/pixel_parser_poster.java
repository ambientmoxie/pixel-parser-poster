/* autogenerated by Processing revision 1293 on 2024-04-08 */
import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class pixel_parser_poster extends PApplet {

PImage seed;
PFont arial;
PGraphics flo, stripes;

String filename = "mart.png";

public void setup() {

  /* size commented out by preprocessor */;
  seed = loadImage(filename);
  arial = createFont("arial.ttf", 15);
  textFont(arial);

  // Static RGB stripes
  stripes = createGraphics(80, 4800);
  stripes.beginDraw();

  for (int i= 0; i < 3; i++) {

    int w = stripes.width / 3;
    int h = stripes.height;

    stripes.noStroke();
    switch(i) {
    case 0:
      stripes.fill(255, 0, 0);
      break;
    case 1:
      stripes.fill(0, 255, 0);
      break;
    case 2:
      stripes.fill(0, 0, 255);
      break;
    }
    stripes.rect(w * i, 0, w, h);
  }
  
  stripes.endDraw();

  // Draw PGraphics components
  draw_main_image();
  draw_channel_version();
  draw_sorted_grid();
  draw_gradient_brightness();

  // Global composition
  flo = createGraphics(3360, 4800);
  flo.beginDraw();

  // Insert all components
  flo.image(mni, 0, 0);
  flo.image(gtb, mni.width, 0);
  flo.image(sdg, 0, mni.height);
  flo.image(stripes, flo.width - stripes.width, 0);
  flo.image(clv, 0, mni.height + sdg.height);

  flo.textSize(60); 
  flo.fill(0);
  
  ColorData result = avg_colors(seed);

  String hex = hex(result.rgb_color, 6);

  int rc = (int) red(result.rgb_color);
  int gc = (int) green(result.rgb_color);
  int bc = (int) blue(result.rgb_color);

  colorMode(HSB, 360, 100, 100);
  int hue_c = (int) hue(result.hsb_color); 
  int sat_c = (int) saturation(result.hsb_color);
  int bri_c = (int) brightness(result.hsb_color);

  flo.text("hex: #" + hex, 2500, 4620);
  flo.text("rgb: " + rc + ", " + gc + ", " + bc , 2500, 4700);
  flo.text("hsb: " + hue_c + "%, " + sat_c + "%, " + bri_c + "%", 2500, 4780);

  flo.endDraw();

  // Save the composition
  flo.save("export/final_ouput.png");

}

PGraphics clvb, clv; 

public void draw_channel_version() {

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
                int c = clvb.pixels[index(x, y, clvb.width)];
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
PGraphics gtb; // Gradient Brightness

public void draw_gradient_brightness(){

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
        
        int grad_color = color(0, 0, interp_brightness);

        // Draw rectangle with gradient color
        gtb.noStroke();
        gtb.fill(grad_color);
        gtb.rect(0, h * i, w, h);
    }

    colorMode(RGB, 255, 255, 255);

    gtb.endDraw();

}
PGraphics mnib, mni;

public void draw_main_image() {

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
    int c = result.rgb_color;
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
            int target_px = mnib.pixels[index(PApplet.parseInt(target_px_x), PApplet.parseInt(target_px_y), mnib.width)];
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
            mni.ellipse(ellipse_pos_x, ellipse_pos_y, grid_w - (grid_w / 1.8f), grid_h - (grid_h / 1.8f));
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
PGraphics sdg; // Sorted Grid
IntDict inventory;
String[] colorKeys;

public void generate_sorted_colors() {

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

public void draw_sorted_grid(){

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
// Return the index of the target px in the px array
public int index(int x, int y, int target_width) {
  return x + y * target_width;
}

// Custom object that returns average color and average brightness of an image
class ColorData {
  int rgb_color;
  int hsb_color;

  ColorData(int rgb_c, int hsb_c) {
    rgb_color = rgb_c;
    hsb_color = hsb_c;
  }
}

public ColorData avg_colors(PImage source) {
  source.loadPixels();

  float r_px = 0.0f;
  float g_px = 0.0f;
  float b_px = 0.0f;

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
  int avg_rgb_color = color(avg_r, avg_g, avg_b);

  // Convert the average RGB color to HSB
  colorMode(HSB, 360, 100, 100);
  float avg_hue = hue(avg_rgb_color);
  float avg_sat = saturation(avg_rgb_color);
  float avg_bri = brightness(avg_rgb_color);
  int avg_hsb_color = color(avg_hue, avg_sat, avg_bri);

  println("hue: " + avg_hue, "saturation: " + avg_sat, "brightness: " + avg_bri, "hue: " + avg_hue);

  // Reset the color mode to RGB
  colorMode(RGB, 255);

  return new ColorData(avg_rgb_color, avg_hsb_color);
}



// Compute the average color of an image and return it's brightness value
// float avg_brightness(PImage source) {
//   source.loadPixels();
  
//   float r_px = 0.0;
//   float g_px = 0.0;
//   float b_px = 0.0;
  
//   for (int i = 0; i < source.pixels.length; i++) {
//     float current_px_r = red(source.pixels[i]);
//     float current_px_g = green(source.pixels[i]);
//     float current_px_b = blue(source.pixels[i]);
    
//     r_px += current_px_r;
//     g_px += current_px_g;
//     b_px += current_px_b;
//   }

//   float avg_r = r_px /source.pixels.length;
//   float avg_g = g_px /source.pixels.length;
//   float avg_b = b_px /source.pixels.length;
  
//   color avg_rgb_color = color(avg_r, avg_g, avg_b);
  
//   // Convert the RGB color to HSB
//   colorMode(HSB, 360, 100, 100);
//   float average_brightness = brightness(avg_rgb_color);
//   // Reset the color mode to RGB
//   colorMode(RGB, 255);

//   println(avg_rgb_color, average_brightness);
  
//   // Create color in HSB mode and return
//   return average_brightness;
// }


  public void settings() { size(100, 100); }

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "pixel_parser_poster" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
