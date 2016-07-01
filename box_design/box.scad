include <text_generator.scad>;

profile = 0;
showComponents = 0;

// sizes in mm
width = 80;
height = 80;
depth = 30; 
thickness = 3;
coverThickness = 2;
coverDepth = 1;
coverOffset = 0;
holeRadius = 2;

mountWidth = 14;
mountHeigth = 6;
mounthDepth = 5;

controlsOffset = 20;
ledRadius = 4;//2.5;
ledXPosition = controlsOffset;
switchRadius = 3;
switchXPosition = width - controlsOffset;

holeHeigth = thickness;
bottomHoleOffset = depth - thickness;
holePosition = 2 * thickness - 1;
widthInside = width - (2 * thickness);
heightInside = height - (2 * thickness);

module box() {
    difference() {
        cube([width, height, depth], false);
        translate([thickness, thickness, -thickness]) {
            cube([widthInside, heightInside, depth], false);
        }
    }
}

module hole(x, y, z) {
    translate([x, y, z]) {
        cylinder(holeHeigth, holeRadius, holeRadius,$fn=20, false);
    }
}

module holes(bottomOffset) {
    hole(holePosition, holePosition, bottomOffset);
    hole(holePosition, heightInside + 1, bottomOffset);
    hole(widthInside + 1, holePosition, bottomOffset);
    hole(widthInside + 1, heightInside + 1, bottomOffset);
}

module holeBox(x, y) {
    translate([x, y, 1]) {
        difference() {
            cube([8, 8, depth - 1 - thickness]);
            translate([2, 2, 0]) {
                cube([4, 4, depth - 1 - thickness]);
            }
        }
    }
}

module holeBoxes() {
    holeBox(1, 1);
    holeBox(1, heightInside - thickness);
    holeBox(widthInside - thickness, 1);
    holeBox(widthInside - thickness, heightInside - thickness);
}

module cover() {
    translate([0, 0, coverOffset]) {
        cube([width, height, coverThickness], false);
        translate([thickness, thickness, coverThickness]) {
            cube([widthInside, heightInside, coverDepth], false);
        }
    }
}

module ledHoles() {
    halfDepth = depth / 2;
    translate([ledXPosition, thickness + 1, halfDepth]) {
        rotate([90, 0, 0]) {
            heigth = thickness + 2;
            cylinder(heigth, ledRadius, ledRadius,$fn=20, false);
            translate([15, 0 ,0]) {
                cylinder(heigth, ledRadius, ledRadius,$fn=20, false);
            }
        }
    }
}

module switchHole() {
    halfDepth = depth / 2;
    translate([switchXPosition, thickness + 1, halfDepth]) {
        rotate([90, 0, 0]) {
            heigth = thickness + 2;
            cylinder(heigth, switchRadius, switchRadius,$fn=20, false);
        }
    }
}

module mount(x) {
    translate([(width - mountWidth) / 2, x, -(coverOffset + mountHeigth - 1)]) {
        difference() {
            cube([mountWidth, mounthDepth, mountHeigth], false);
            translate([(mountWidth - 4) / 2, 0, 3]) {
                cube([4, mounthDepth, 2], false);
            }
        }
    }
}

module verticalMount(y) {
    translate([width - 1, y, depth - mounthDepth]) {
        difference() {
            cube([mountHeigth, mountWidth, mounthDepth], false);
            translate([1, (mountWidth - 4) / 2, 0]) {
                cube([2, 4, mounthDepth], false);
            }
        }
    }
}

module logo() {
    rotate([90, 180, -90]) {
        translate([31.5, -15, -1]) {
            scale([1,1,1]) drawtext("BTC");
        }
    }
}

module sensorHoles() {
    holeSize = 3;
    translate([width - 3, (height - 1) / 2 - holeSize/2, 0]) {
        cube([3, holeSize, 5], false);
    }
    translate([thickness, (height - 1) / 2, 5]) {
        rotate([0, 90, 0]) {
            cylinder(width, holeSize/2, holeSize/2,$fn=20, false);
        }
    }
}

module batteryHolder() {
  translate([0, height - 33, depth - 22]) {
        cube([width, 3, 20]);
  }  
}

module components() {
    // battery box 55x25x25
    translate([12.5, height - 29, 1]) {
        cube([55, 25, 25]);
    }
    // bluno 5x30x34
    translate([10, 10, 24]) {
        cube([34, 30, 5]);
    }
}

module model() {
    difference() {
        box();
        holes(bottomHoleOffset);
        ledHoles();
        switchHole();
        sensorHoles();
        logo();
    }
    
    holeBoxes();
    verticalMount(0);
    verticalMount(height - mountWidth);
    batteryHolder();
    
    translation = profile == 1 
        ? [0, 0, -4] 
        : [0, 82, depth - thickness];
    
    translate(translation) {
        difference() {
            cover();
            holes(coverOffset);
        }
        mount(25);
        mount(50);
    }
    
    if (showComponents == 1) {
        components();
    }
}

rotate([0, 180, 0]) {
    translate([-width/2, -height/2, -depth]) {
        difference() {
            model();
            if (profile == 1) {
                translate([0, height / 2, -(depth - 10)]) {
                    cube([width / 2, height, 60], false);
                }
            }
        }
    }
}