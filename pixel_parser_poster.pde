PImage seed;
PFont arial;
PGraphics flo, stripes;

String filename = "sharits.png";

void setup() {

  size(100, 100);
  seed = loadImage(filename);
  arial = createFont("arial.ttf", 15);
  textFont(arial);

  // Static RGB stripes, nothing fancy
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
  flo.save("export/ouput.png");

}
